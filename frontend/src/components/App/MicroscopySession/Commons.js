/*
 * Copyright 2018 Genentech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import MicroscopySessionService from '@/services/MicroscopySessionService'
import SettingsService from '@/services/SettingsService'
import ProjectsService from '@/services/ProjectService'

import HandleOptimisticLocking from '@/components/mixins/HandleOptimisticLocking'
import CommonAlerts from '@/components/mixins/CommonAlerts'
import CommonValidations from '@/components/mixins/CommonValidations'
import FormCommons from '@/components/mixins/FormCommons'
import FilterCommons from '@/components/mixins/FilterCommons'
import ProtocolType from '@/components/App/Grid/ProtocolType'
import Token from '@/utils/MicroscopySessionToken'
import DirectoryName from '@/utils/DirectoryName'

export default {

  mixins: [
    HandleOptimisticLocking, CommonAlerts, FormCommons, FilterCommons, CommonValidations
  ],

  data () {
    return {
      ProtocolType: ProtocolType,
      entity: {
        label: '',
        microscope: '',
        sampleHolder: '',
        grid: null,
        gridReturnedToStorage: false,
        sessionStart: '',
        sessionFinish: '',
        numberOfImagesAcquired: '',
        electronDetector: null,
        sessionStartDate: '',
        sessionStartTime: '',
        sessionFinishDate: '',
        sessionFinishTime: '',
        dataStorageDirectoryName: null,

        extractionVoltageKV: null,
        accelerationVoltageKV: null,
        gunLensSetting: null,
        condenser2ApertureDiameter: null,
        objectiveAperture: {},
        energyFilter: false,
        energyFilterSlitWidth: null,

        nominalMagnification: '',
        calibratedMagnification: '',
        pixelSize: '',
        nanoprobe: false,
        spotSize: '',
        diameterOfIlluminatedArea: '',
        countingMode: false,
        exposureRate: '',
        exposureDuration: '',
        doseFractionation: false,
        numberOfFrames: 1,
        superResolution: false,
        supersamplingFactor: null,
        pixelBinning: null,

        targetUnderfocusMin: '',
        targetUnderfocusMax: '',
        exposuresPerHole: '',
        softwareUsed: []
      },
      patternForDataStorageDirectoryName: null,
      project: null
    }
  },

  methods: {
    fetchEntityBySlugOrId (slugOrId) {
      this.$log.debug('Fetching data by: ', slugOrId)
      return this._microscopySessionService.getEntityBySlugOrId(slugOrId)
        .then(this.assignEntityAfterLoading)
        .catch(error => this.$log.error(error))
    },

    async assignEntityAfterLoading (result) {
      result.data.sessionStartDate = result.data.sessionStart || ''
      result.data.sessionStartTime = result.data.sessionStart || ''
      result.data.sessionFinishDate = result.data.sessionFinish || ''
      result.data.sessionFinishTime = result.data.sessionFinish || ''

      if (!result.data.nanoprobe) {
        result.data.nanoprobe = false
      }
      if (!result.data.countingMode) {
        result.data.countingMode = false
      }
      if (!result.data.doseFractionation) {
        result.data.doseFractionation = false
      }
      if (!result.data.superResolution) {
        result.data.superResolution = false
      }
      if (!result.data.objectiveAperture) {
        result.data.objectiveAperture = {
          diameter: '',
          phasePlate: null
        }
      }
      if (!result.data.softwareUsed) {
        result.data.softwareUsed = []
      }

      await this.fetchDirectoryPatternIfNeeded()
      this.entity = result.data
    },

    async fetchProjectIfNeeded () {
      if (this.project) {
        return this.project
      }

      this.project = await this._projectsService.getEntityBySlugOrId(this.projectId)
        .then(result => result.data)
        .catch(error => this.$log.error(error))
    },

    async fetchDirectoryPatternIfNeeded () {
      if (!this.patternForDataStorageDirectoryName) {
        this.patternForDataStorageDirectoryName = await this._settingsService.get()
          .then(result => this._.get(result, 'data.patternForDataStorageDirectoryName'))
          .catch(e => this.$log.error(e))
      }

      return this.patternForDataStorageDirectoryName
    },

    doesDirectoryNameContainId (directoryName) {
      const minimalPattern = new RegExp(
        /.*/.source
        + Token.MICROSCOPY_SESSION_ID.regExp.source
        + /.*/.source)

      return minimalPattern.test(directoryName)
    },

    tokenDate (timestamp) {
      return this.$moment(timestamp).locale('en').format('YYMMDD')
    },

    tokenTime (timestamp) {
      return this.$moment(timestamp).locale('en').format('HHmm')
    },

    padStartIfNotNil (entry) {
      if (this._.isNil(entry)) {
        return null
      }

      return this._.padStart(entry, 6, '0')
    },

    async addDataStorageDirectoryName (entity, pattern) {
      await this.fetchProjectIfNeeded()

      entity.dataStorageDirectoryName = DirectoryName.fromTokenString(
        pattern,
        [
          [Token.PROJECT_LABEL, this.project.label],
          [Token.GRID_ID, this.padStartIfNotNil(entity.grid.id)],
          [Token.GRID_LABEL, entity.grid.label],
          [Token.MICROSCOPY_LABEL, entity.label],
          [Token.MICROSCOPY_SESSION_ID, this.padStartIfNotNil(entity.id)],
          [Token.MICROSCOPY_START_DATE, this.tokenDate(entity.sessionStart)],
          [Token.MICROSCOPY_START_TIME, this.tokenTime(entity.sessionStart)],
          [Token.MICROSCOPE_LABEL, entity.microscope.label]
        ]
      ).toString()
      return entity
    },

    async reSaveIfNeeded (entity) {
      if (!this.doesDirectoryNameContainId(entity.dataStorageDirectoryName)) {
        return entity
      }

      const entityToSave = await this.addDataStorageDirectoryName(entity, this.patternForDataStorageDirectoryName)
      return this._microscopySessionService.saveEntity(this.projectId, entityToSave)
        .then(result => result.data)
        .catch(error => this.$log.error(error))
    },

    initServices () {
      this._microscopySessionService = new MicroscopySessionService()
      this._settingsService = new SettingsService()
      this._projectsService = new ProjectsService()
    }
  },

  created () {
    this.initServices()
  }
}
