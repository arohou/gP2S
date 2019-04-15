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

import GridService from '@/services/GridService'
import SampleService from '@/services/SampleService'
import HandleOptimisticLocking from '@/components/mixins/HandleOptimisticLocking'
import CommonAlerts from '@/components/mixins/CommonAlerts'
import CommonValidations from '@/components/mixins/CommonValidations'
import FormCommons from '@/components/mixins/FormCommons'
import FilterCommons from '@/components/mixins/FilterCommons'
import ProtocolType from '@/components/App/Grid/ProtocolType'
import ConcentrationType from '@/components/App/ConcentrationType'

const service = new GridService()
const sampleService = new SampleService()

export default {

  props: ['id'],

  mixins: [
    HandleOptimisticLocking, CommonAlerts, FormCommons, FilterCommons, CommonValidations
  ],

  data () {
    return {
      ProtocolType: ProtocolType,
      grid: {
        index: null,
        label: null,
        gridType: null,
        isAvailable: null,
        surfaceTreatmentProtocol: null,
        protocolType: null,
        vitrificationProtocol: null,
        negativeStainProtocol: null,
        incubationTime: null,
        volume: null,
        concentration: {
          isDilutedOrConcentrated: false,
          concentrationType: ConcentrationType.Dilution,
          dilutionFactor: null,
          concentrationFactor: null
        },
        sample: null,
        storageBoxLabelNumber: null,
        positionWithinBox: null,
        cryoStorageDevice: null,
        cylinderNumberLabel: null,
        tubeNumberLabel: null,
        boxNumberLabel: null
      }
    }
  },

  methods: {
    saveForm () {
      if (this.projectId) {
        return Promise.all([service.createGrid(this.projectId, this.grid), sampleService.updateAvailabilityForGridMaking(this.grid.sample)])
          .then(results => this.openViewForm('grid', results[0].data, this.useSlug))
      }
      this.alertNoProject()
      return false
    },

    fetchGrid (slugOrId) {
      return service.getGridBy(slugOrId)
        .then(result => {
          this.grid = result.data
        })
        .catch(error => this.$log.error(error))
    }
  }
}
