<!--
  - Copyright 2018 Genentech Inc.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <div class="base-form">
    <el-row type="flex">
      <el-form-item label="Name" prop="label" :error="getError('label')">
        <el-input v-model="entity.label"></el-input>
      </el-form-item>
      <el-button class="now_button--invisible">Now</el-button>
    </el-row>

    <el-row type="flex" align="bottom" justify="space-between">
      <el-form-item prop="sessionStart"
                    :error="getError('sessionStart')">
        <div class="date-input">
          <label for="microscopy-session__date-started"
                 class="el-form-item__label">Date started</label>
          <date-selector id="microscopy-session__date-started"
                         v-model="entity.sessionStartDate"></date-selector>
        </div>
        <div class="time-input">
          <label
            for="microscopy-session__time-started"
            class="el-form-item__label">Time started</label>
          <time-selector id="microscopy-session__time-started"
                         v-model="entity.sessionStartTime"></time-selector>
        </div>
      </el-form-item>

      <el-form-item prop="sessionFinish"
                    :error="getError('sessionFinish')">
        <div class="date-input">
          <label
            for="microscopy-session__date-finished"
            class="el-form-item__label">Date finished</label>
          <date-selector id="microscopy-session__date-finished"
                         v-model="entity.sessionFinishDate"></date-selector>
        </div>

        <div class="time-input">
          <label
            for="microscopy-session__time-finished"
            class="el-form-item__label">Time finished</label>
          <time-selector id="microscopy-session__time-finished"
                         v-model="entity.sessionFinishTime"></time-selector>
        </div>
      </el-form-item>

      <el-button class="now_button"
                 @click="setSessionFinishToNow">Now
      </el-button>
    </el-row>

    <el-row type="flex"
            class="grid-row">
      <el-form-item label="Grid imaged" prop="grid"
                    :error="getError('grid')"
                    ref="field-grid">
        <el-select v-model="entity.grid" filterable placeholder="Select grid used"
                   value-key="id"
                   @change="setDefaultGridReturnedToStorage">
          <el-option
            v-for="item in grids"
            :key="item.id"
            :label="item.label"
            :value="item">
          </el-option>
        </el-select>
        <div v-if="!!entity.grid" class="grid-details view__metadata__value">
          <div class="grid-details__created">
            <span>Created by&nbsp;</span>
            <user-full-name :userId="_.get(entity, 'grid.createdBy')"/>
            <span>&nbsp;on {{ _.get(entity, 'grid.createdDate') | moment('lll') }}</span>
          </div>
          <span>Sample: {{ _.get(entity, 'grid.sample.label', '—') }}</span>
        </div>
      </el-form-item>

      <el-form-item label="Grid returned to storage?" prop="gridReturnedToStorage"
                    :error="getError('gridReturnedToStorage')">
        <el-radio-group id="gridReturnedToStorage" v-model="entity.gridReturnedToStorage">
          <el-radio-button :label="true" name="grid-returned-to-storage">Yes</el-radio-button>
          <el-radio-button :label="false" name="grid-returned-to-storage">No</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-button class="now_button--invisible">Now</el-button>

    </el-row>

    <el-row type="flex">
      <el-form-item label="Microscope" prop="microscope"
                    :error="getError('microscope')">
        <el-select v-model="entity.microscope" placeholder="Select microscope used"
                   value-key="id"
                   id="basic-information__microscope">
          <el-option
            v-for="item in microscopes"
            :key="item.id"
            :label="item.label"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="Detector" prop="electronDetector"
                    :error="getError('electronDetector')"
                    ref="field-detector">
        <el-select v-model="entity.electronDetector"
                   placeholder="Select electron detector used"
                   value-key="id" :disabled="!entity.microscope"
                   id="microscopy-session__electron-detector">
          <el-option
            v-for="item in electronDetectors"
            :key="item.id"
            :label="item.label"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>

      <el-button class="now_button--invisible">Now</el-button>
    </el-row>

    <el-row type="flex">
      <el-form-item label="Number of images" prop="numberOfImagesAcquired"
                    :error="getError('numberOfImagesAcquired')">
        <el-input v-model="entity.numberOfImagesAcquired"></el-input>
      </el-form-item>
      <el-form-item label="Sample holder" prop="sampleHolder"
                    :error="getError('sampleHolder')"
                    :class="{'is-required': _.get(entity, 'microscope.sampleInsertionMechanism') === InsertionMechanismType.SIDE_ENTRY_HOLDER.name}"
                    ref="field-sample_holder">
        <el-select v-model="entity.sampleHolder"
                   :placeholder="_.get(entity, 'microscope.sampleInsertionMechanism') === InsertionMechanismType.AUTO_LOADER.name ? 'autoloader' : 'Select sample holder'"
                   value-key="id"
                   id="microscopy-session__sample-holder"
                   :disabled="_.get(entity, 'microscope.sampleInsertionMechanism') === InsertionMechanismType.AUTO_LOADER.name">
          <el-option
            v-for="item in filteredSampleHolders"
            :key="item.id"
            :label="item.label"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>

      <el-button class="now_button--invisible">Now</el-button>
    </el-row>
    <el-row>
      <label class="el-form-item__label">Data storage directory name</label>
      <div v-if="isEntityCreated" class="view__metadata__value"
           id="microscopy-session__data_storage_directory_name">
        {{ _.get(entity, 'dataStorageDirectoryName') | formatValue }}
      </div>
      <div v-else class="view__metadata__value"
           id="microscopy-session__pattern-for-data-storage-directory-name">
        Will follow this pattern: {{ patternForDataStorageDirectoryName | formatValue }}
      </div>
    </el-row>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import CommonAlerts from '@/components/mixins/CommonAlerts'
  import FormCommons from '@/components/mixins/FormCommons'
  import DateSelector from '@/components/Selectors/DateSelector'
  import TimeSelector from '@/components/Selectors/TimeSelector'
  import MicroscopeService from '@/services/MicroscopeService'
  import SampleHolderService from '@/services/SampleHolderService'
  import GridService from '@/services/GridService'
  import ProtocolType from '@/components/App/Grid/ProtocolType'
  import moment from 'moment'
  import ValidationError from '@/errors/ValidationError'
  import InsertionMechanismType from '@/components/App/Admin/Microscope/InsertionMechanismType'
  import Utils from '@/components/App/MicroscopySession/Utils'
  import FilterCommons from '@/components/mixins/FilterCommons'
  import UserFullName from '@/components/App/UserFullName'

  import { isEntityCreated } from '@/utils/EntityUtils'

  export default {
    name: 'microscopy-session-base-form',

    props: {
      allRules: Object,
      mainForm: Object,
      projectId: String,
      entity: Object,
      allErrors: Object,
      originalEntityBaseForm: Object,
      shouldSetDefaultGridReturned: {
        type: Boolean,
        default: true
      },
      patternForDataStorageDirectoryName: String,
    },

    mixins: [CommonValidations, Utils, FilterCommons, FormCommons, CommonAlerts],

    components: {DateSelector, TimeSelector, UserFullName},

    data () {
      return {
        _gridService: null,
        _sampleHolderService: null,
        _microscopeService: null,

        microscopes: [],
        sampleHolders: [],
        filteredSampleHolders: [],
        grids: [],
        electronDetectors: null,
        defaultMicroscope: [Object, String],

        InsertionMechanismType: InsertionMechanismType,

        rules: {
          label: [
            {
              required: true,
              message: 'Microscopy session label can\'t be empty',
              trigger: 'blur'
            }
          ],
          sessionStart:
            [{
              required: true,
              validator: this.isDefined('Session start date and time'),
              trigger: 'blur'
            }, {
              required: true,
              validator: this.isSessionStartBeforeSessionFinish,
              trigger: 'blur'
            }],
          sessionFinish:
            [{
              required: false,
              validator: this.isSessionFinishAfterSessionStart,
              trigger: 'blur'
            }],
          microscope:
            [{
              required: true,
              validator: this.isDefined('Microscope'),
              trigger: 'change'
            }],
          grid:
            [{
              required: true,
              validator: this.isDefined('Grid'),
              trigger: 'change'
            }],
          electronDetector:
            [{
              required: true,
              validator: this.isDefined('Electron detector'),
              trigger: 'change'
            }],
          numberOfImagesAcquired:
            [{
              required: false,
              validator: this.isIntegerGreaterThanOrEqualTo('Number of images acquired', 0),
              trigger: 'blur'
            }],
          sampleHolder: [{
            validator: this.sampleHolderValidator(),
            trigger: 'change'
          }]
        }
      }
    },

    computed: {
      isEntityCreated: function () {
        return isEntityCreated(this.entity)
      }
    },

    watch: {
      // region Injecting form values that are applicable for all sections.
      'allErrors': function () {
        this.errors = this.allErrors // Inject updated error messages from main form.
      },

      'originalEntityBaseForm': function () {
        this.originalEntity = this.originalEntityBaseForm // Inject original entity.
      },
      // endregion

      // region Session Start & Finish Dates & Times
      'entity.sessionStartDate': function (value) {
        this.setSessionStart(value, this.entity.sessionStartTime)
      },

      'entity.sessionStartTime':
        function (value) {
          this.setSessionStart(this.entity.sessionStartDate, value)
        },

      'entity.sessionFinishDate': function (value) {
        this.setSessionFinish(value, this.entity.sessionFinishTime)
      },

      'entity.sessionFinishTime': function (value) {
        this.setSessionFinish(this.entity.sessionFinishDate, value)
      },
      // endregion

      projectId: function () {
        this.loadGrids()
      },

      'entity': function () {
        this.loadGrids()
      },

      'entity.microscope': function (newMicroscope, oldMicroscope) {
        const sampleHolderIsApplicable = _.get(newMicroscope, 'sampleInsertionMechanism')
          === InsertionMechanismType.SIDE_ENTRY_HOLDER.name

        if (sampleHolderIsApplicable) {
          this.updateSampleHoldersList()
        } else {
          this.clearSampleHolder()
        }

        this.fetchElectronDetectorsForMicroscope()
        this.updateElectronDetector(newMicroscope, oldMicroscope)
      },

      'entity.grid': function () {
        this.updateSampleHolder()
      },
    },

    methods: {
      // region Session Start & Finish.
      createSessionMoment (sessionDate = null, sessionTime = null, startOrFinishString) {
        const date = moment(sessionDate)
        const time = moment(sessionTime)
        let startMoment = moment({
          year: date.year(),
          month: date.month(),
          date: date.date(),
          hour: time.hour(),
          minute: time.minute()
        })
        this.$log.debug('Input ' + startOrFinishString + ' date: ', sessionDate, '; input ' + startOrFinishString + ' time:', sessionTime,
          '\n; moment ' + startOrFinishString + ' date: ', date.format(), '; moment ' + startOrFinishString + ' time: ', time.format(),
          '\n' + startOrFinishString + 'Moment: ', startMoment.format(), '; saving: ', startMoment.valueOf())

        return startMoment.valueOf()
      },

      setSessionStart (startDate, startTime) {
        if (!startDate || !startTime) {
          this.entity.sessionStart = null
          return
        }

        // Both time and date are needed to set session start.
        this.entity.sessionStart = this.createSessionMoment(startDate, startTime, 'start')

        this.triggerDatesValidation()
      },

      setSessionFinish (finishDate = null, finishTime = null) {
        if (!finishDate || !finishTime) {
          this.entity.sessionFinish = null
          return
        }

        // Both time and date are needed to set session finish.
        this.entity.sessionFinish = this.createSessionMoment(finishDate, finishTime, 'finish')

        this.triggerDatesValidation()
      },

      setSessionFinishToNow () {
        this.entity.sessionFinish = this.entity.sessionFinishDate = this.entity.sessionFinishTime = moment().valueOf()
      },
      // endregion

      //region Called from watchers / onchange.

      initDefaultMicroscope () {
        if (this.isNewEntity() && !!this.defaultMicroscope && !!this.microscopes && this.microscopes.length > 0) {
          let microscope = this.microscopes.find((aMicroscope) => {
            return aMicroscope.id === parseInt(this.defaultMicroscope)
              || aMicroscope.slug === this.defaultMicroscope
          })
          if (microscope) {
            this.entity.microscope = microscope
          }
        }
      },

      setDefaultGridReturnedToStorage (value) {
        if (!!value && this.shouldSetDefaultGridReturned) {
          switch (value.protocolType) {
            case ProtocolType.Cryo.name:
              this.entity.gridReturnedToStorage = false
              break
            case ProtocolType.Stain.name:
              this.entity.gridReturnedToStorage = true
              break
          }
        }
      },

      updateElectronDetector (newMicroscope, oldMicroscope) {
        if (!_.get(oldMicroscope, 'id')  // Ignore initial load of data.
          || _.get(oldMicroscope, 'id') === _.get(newMicroscope, 'id')
          || !this.entity.electronDetector) {
          // Ignore if same microscope has been set or detector hasn't been chosen previously.
          return
        }

        this.entity.electronDetector = null
        this.alertWarnOK('Your choice of detector was reset when you changed your choice of microscope.')
      },

      updateSampleHolder () {
        if (!_.get(this, 'entity.grid') || _.isEmpty(this.sampleHolders)) {
          return
        }

        if (this.entity.grid.protocolType === ProtocolType.Cryo.name) {
          this.filteredSampleHolders = this.sampleHolders.filter(element => element.cryoCapable)
        } else if (this.entity.grid.protocolType === ProtocolType.Stain.name) {
          // Sort by cryoCapable: non cryoCapable go first.
          this.filteredSampleHolders = this.sampleHolders.sort((first, second) => {
            return _.get(first, 'cryoCapable', null) - _.get(second, 'cryoCapable', null)
          })
        } else {
          this.filteredSampleHolders = this.sampleHolders
        }

        this.filteredSampleHolders = this.options(this.filteredSampleHolders, _.get(this, 'originalEntity.sampleHolder'), 'id')
      },

      updateSampleHoldersList () {
        this._sampleHolderService.findByMicroscope(this.entity.microscope.id)
          .then(result => {
            this.sampleHolders = result.data
            this.updateSampleHolder()
          }).catch(error => this.$log.error(error))
      },

      clearSampleHolder () {
        this.$refs['field-sample_holder'].resetField() // Remove error message from sample holder.
        this.entity.sampleHolder = null
      },
      // endregion

      // region Data loaders

      fetchElectronDetectorsForMicroscope () {
        if (!this.entity.microscope || !this.entity.microscope.id) {
          this.electronDetectors = []
          return
        }

        this._microscopeService.getElectronDetectorsBySlugOrId(this.entity.microscope.id)
          .then(result => {
            this.electronDetectors = this.options(result.data, _.get(this, 'originalEntity.electronDetector'), 'id')

            let electronDetectorIsOnList = this.entity.electronDetector
              && this.electronDetectors.map(detector => detector.id).includes(this.entity.electronDetector.id)

            if (!electronDetectorIsOnList) {
              this.entity.electronDetector = null
              this.$refs['field-detector'].resetField() // Remove error message from detector.
            }
          }).catch(error => this.$log.error(error))
      },

      loadGrids () {
        const entityHasGrid = this.entity && this.entity.grid && this.entity.id
        let availableGrids = entityHasGrid ? this._gridService.findAvailableGridsOrGridWithId(this.projectId, this.entity.grid.id)
          : this._gridService.findAvailableGrids(this.projectId)

        availableGrids.then(result => {
          this.grids = this.options(result.data, _.get(this, 'originalEntity.grid'), 'id')

          let entityGridIsOnList = this.grids && this.entity && this.entity.grid
            && this.grids.map(grid => grid.id).includes(this.entity.grid.id)

          if (!entityGridIsOnList) {
            this.entity.grid = null
            this.$refs['field-grid'].resetField() // Reset error message.
          }
        }).catch(error => this.$log.error(error))
      },

      loadMicroscopes () {
        this._microscopeService.listEntities()
          .then(result => {
            this.microscopes = this.options(result.data, _.get(this, 'originalEntity.microscope'), 'id')
            this.initDefaultMicroscope() // Takes effect only for new microscopy session (defaultMicroscope is set).
          }).catch(error => this.$log.error(error))
      },
      // endregion

      // region Custom Validators

      sampleHolderValidator () {
        return (rule, value, callback) => {
          if (!this.entity.microscope || this.entity.microscope.sampleInsertionMechanism
            !== InsertionMechanismType.SIDE_ENTRY_HOLDER.name) {
            return callback()
          }

          if (!value) { // Sample holder should be specified for side-entry holder.
            return callback(new ValidationError('Sample holder should be specified'))
          } else if (this.entity.grid && this.entity.grid.protocolType === ProtocolType.Cryo.name
            && !this.entity.sampleHolder.cryoCapable) { // If grid is a cryo grid, only let the user choose among cryo-capable holders.
            return callback(new ValidationError('Sample holder should be a cryo-capable holder'))
          }

          return callback()
        }
      },

      isSessionStartBeforeSessionFinish (rule, value, callback) {
        this.$log.debug('isSessionStartBeforeSessionFinish sessionStart: ', moment(this.entity.sessionStart).format(),
          ' sessionFinish: ', moment(this.entity.sessionFinish).format())
        if (!!this.entity.sessionFinish && !!this.entity.sessionStart
          && !moment(this.entity.sessionStart).isBefore(moment(this.entity.sessionFinish))) {
          return callback(new ValidationError('Session start has to be before session finish'))
        } else {
          return callback()
        }
      },

      isSessionFinishAfterSessionStart (rule, value, callback) {
        this.$log.debug('isSessionFinishAfterSessionStart sessionStart: ', moment(this.entity.sessionStart).format(),
          ' sessionFinish: ', moment(this.entity.sessionFinish).format())
        if (!!this.entity.sessionFinish && !!this.entity.sessionStart
          && !moment(this.entity.sessionFinish).isAfter(moment(this.entity.sessionStart))) {
          return callback(new ValidationError('Session finish has to be after session start'))
        } else {
          return callback()
        }
      },
      // endregion

      initServices () {
        this._gridService = new GridService()
        this._sampleHolderService = new SampleHolderService()
        this._microscopeService = new MicroscopeService()
      },

      isNewEntity () {
        return this.$route.name === 'microscopy_session-new'
      },

      triggerDatesValidation () {
        let self = this
        this.$nextTick().then(() => {
          self.mainForm.validateField('sessionFinish')
          self.mainForm.validateField('sessionStart')
        })
      },

    },

    created () {
      this.initServices()
      Object.assign(this.allRules || {}, this.rules)
    },

    mounted () {
      this.$events.on('fetchElectronDetectorsForMicroscope', data => this.fetchElectronDetectorsForMicroscope())
      this.$events.on('setDefaultMicroscope', microscope => this.defaultMicroscope = microscope)
    },

    beforeDestroy () {
      this.$events.off('fetchElectronDetectorsForMicroscope')
      this.$events.off('setDefaultMicroscope')
    }
  }
</script>

<style lang="scss" scoped>
  @import '~styles/globals/constants';

  .date-input {
    flex: 2;
  }

  .time-input {
    flex: 1;
    margin-left: 1.615rem;
  }

  .now_button {
    height: 2.769rem;
    margin-bottom: $form-item-margin-bottom;
    flex: 0 !important;
    margin-left: 1.308rem !important;

    &--invisible {
      @extend .now_button;
      visibility: hidden;
    }
  }

  .grid-details {
    margin-top: 0.5rem;
    line-height: 1.385rem;

    .grid-details__created {
      display: flex;
    }
  }

  .grid-row {

    /deep/ .el-form-item__content {
      flex-direction: column !important;
      align-items: flex-start !important;
    }
  }

</style>
