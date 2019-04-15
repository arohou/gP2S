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
    <el-row :gutter="24">
      <el-col :span="8">
        <microscope-label
          :microscope-label="_.get(entity, 'microscope.label')"></microscope-label>
      </el-col>
      <el-col :span="8">
        <detector-label
          :detector-label="_.get(entity, 'electronDetector.label')"></detector-label>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="8">
        <el-form-item label="Magnification (image pixel size)" prop="nominalMagnification"
                      :error="getError('nominalMagnification')">
          <el-select
            v-model="magnification"
            placeholder="Select nominal magnification"
            @change="fetchElectronDetectorsForMicroscope"
            value-key="label"
            :disabled="!entity.microscope || !entity.electronDetector"
            id="microscopy-session__nominal-magnification">
            <el-option
              v-for="item in availableMagnifications"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Nanoprobe?" prop="nanoprobe"
                      :error="getError('nanoprobe')">
          <el-radio-group id="microscopy-session__nanoprobe" v-model="entity.nanoprobe">
            <el-radio-button :label="true" name="nanoprobe">Yes</el-radio-button>
            <el-radio-button :label="false" name="nanoprobe">No</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Spot size" prop="spotSize"
                      :error="getError('spotSize')" id="microscopy-session__spot-size">
          <el-input v-model="entity.spotSize"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="8">
        <el-form-item label="Diameter of illuminated area (μm)" prop="diameterOfIlluminatedArea"
                      id="microscopy-session__diameter-of-illuminated-area"
                      :error="getError('diameterOfIlluminatedArea')">
          <el-input v-model="entity.diameterOfIlluminatedArea"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Use counting mode?" prop="countingMode" :error="getError('countingMode')">
          <el-radio-group id="microscopy-session__counting-mode" v-model="entity.countingMode"
                          :disabled="!_.get(entity, 'electronDetector.countingModeAvailable')"
                          @change="mainForm.validateField('exposureRate')">
            <el-radio-button :label="true" name="counting-mode">Yes</el-radio-button>
            <el-radio-button :label="false" name="counting-mode">No</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item id="microscopy-session__exposure-rate-item"
                      :label="createExposureRateLabel(entity.countingMode)"
                      prop="exposureRate" :error="getError('exposureRate')"
                      :class="{'is-required': entity.countingMode}">
          <el-input v-model="entity.exposureRate" :disabled="!entity.electronDetector"
                    id="microscopy-session__exposure-rate"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="8">
        <el-form-item label="Exposure duration (s)" prop="exposureDuration"
                      :error="getError('exposureDuration')">
          <el-input v-model="entity.exposureDuration" :disabled="!entity.electronDetector"
                    id="microscopy-session__exposure-duration"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Dose fractionation?" prop="doseFractionation" :error="getError('doseFractionation')">
          <el-radio-group v-model="entity.doseFractionation" id="microscopy-session__dose-fractionation"
                          :disabled="!_.get(entity, 'electronDetector.doseFractionationAvailable')">
            <el-radio-button :label="true" name="dose-fractionation">Yes</el-radio-button>
            <el-radio-button :label="false" name="dose-fractionation">No</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Number of frames" prop="numberOfFrames" :error="getError('numberOfFrames')">
          <el-input v-model="entity.numberOfFrames" id="microscopy-session__number-of-frames"
                    :disabled="!entity.doseFractionation"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="8">
        <el-form-item label="Super resolution?" prop="superResolution" :error="getError('superResolution')">
          <el-radio-group id="microscopy-session__super-resolution" v-model="entity.superResolution"
                          :disabled="!_.get(entity, 'electronDetector.superResolutionAvailable')">
            <el-radio-button :label="true" name="super-resolution">Yes</el-radio-button>
            <el-radio-button :label="false" name="super-resolution">No</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Supersampling factor" prop="supersamplingFactor"
                      :error="getError('supersamplingFactor')"
                      :class="{'is-required': !!entity.superResolution}">
          <el-input v-model="entity.supersamplingFactor" id="microscopy-session__supersampling-factor"
                    :disabled="!entity.superResolution"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Pixel binning" prop="pixelBinning" :error="getError('pixelBinning')"
                      :class="{'is-required': !entity.superResolution}">
          <el-input v-model="entity.pixelBinning" id="microscopy-session__pixel-binning"
                    :disabled="!entity.electronDetector || !!entity.superResolution"></el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <div class="edit__metadata">
      <el-row :gutter="24">
        <el-col :sm="8">
          <label class="view__metadata__computed-data__label">Image pixel size</label>
          <div class="view__metadata__value" id="microscopy-session__image-pixel-size">
            {{ calculateImagePixelSize(entity) | round(3) | formatUnit('Å') }}
          </div>
        </el-col>
        <el-col :sm="8">
          <label class="view__metadata__computed-data__label">
            {{ entity.countingMode ? 'Exposure rate' : 'Estimated exposure rate'}}</label>
          <div class="view__metadata__value" id="microscopy-session__exposure-rate-eA2s">
            {{ calculateExposureRate(entity) | round(3) | formatUnit('electrons/Å&sup2;/s') }}
          </div>
        </el-col>
        <el-col :sm="8">
          <label class="view__metadata__computed-data__label">
            {{ entity.countingMode ? 'Total exposure' : 'Estimated total exposure' }}</label>
          <div class="view__metadata__value" id="microscopy-session__total-exposure">
            {{ calculateTotalExposure(entity) | round(3) | formatUnit('electrons/Å&sup2;') }}
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <el-col :sm="8">
          <label class="view__metadata__computed-data__label">Frame duration</label>
          <div class="view__metadata__value" id="microscopy-session__frame-duration">
            {{ calculateFrameDuration(entity) | round(3) | formatUnit('s') }}
          </div>
        </el-col>
        <el-col :sm="8">
          <label class="view__metadata__computed-data__label">
            {{ entity.countingMode ? 'Exposure' : 'Estimated exposure' }} per frame</label>
          <div class="view__metadata__value" id="microscopy-session__exposure-per-frame">
            {{ calculateExposurePerFrame(entity) | round(3) | formatUnit('electrons/Å&sup2;') }}
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import CommonAlerts from '@/components/mixins/CommonAlerts'
  import FormCommons from '@/components/mixins/FormCommons'
  import ValidationError from '@/errors/ValidationError'
  import Utils from '@/components/App/MicroscopySession/Utils'
  import FilterCommons from '@/components/mixins/FilterCommons'
  import MicroscopeLabel from './MicroscopeLabel'
  import DetectorLabel from './DetectorLabel'

  export default {
    components: {
      DetectorLabel,
      MicroscopeLabel
    },
    name: 'microscopy-session-base-form',

    props: {
      allRules: Object,
      mainForm: Object,
      projectId: String,
      entity: Object,
      allErrors: Object,
      originalEntityBaseForm: Object,
    },

    mixins: [
      CommonValidations, Utils, FilterCommons, FormCommons, CommonAlerts
    ],

    data () {
      return {
        electronDetectorLoaded: false,
        availableMagnifications: [],
        magnification: null, // For storing rounded nominal magnification value.
        changingElectronDetector: false,
        isExposureSettingsUpdated: false,

        rules: {
          nominalMagnification: [{
            required: true,
            message: 'Magnification can\'t be empty',
            validator: this.isGreaterThanOrEqualTo('Magnification', 0),
            trigger: 'blur'
          }],
          spotSize:
            [{
              required: false,
              validator: this.isIntegerGreaterThan('Spot size', 0),
              trigger: 'blur'
            }],
          diameterOfIlluminatedArea: [{
            required: false,
            validator: this.isGreaterThan('Diameter of illuminated area', 0),
            trigger: 'blur'
          }],
          exposureRate: [{
            validator: this.exposureRateValidator(),
            trigger: 'blur'
          }],
          exposureDuration: [{
            required: true,
            validator: this.isGreaterThan('Exposure duration', 0),
            trigger: 'blur'
          }],
          numberOfFrames: [{
            required: false,
            validator: this.isIntegerGreaterThan('Number of frames', 0),
            trigger: 'blur'
          }],
          supersamplingFactor:
            [{
              validator: this.supersamplingFactorValidator(),
              trigger: 'blur'
            }, {
              validator: this.isIntegerGreaterThanOrEqualTo('Supersampling factor', 2),
              trigger: 'blur'
            }],
          pixelBinning:
            [{
              validator: this.pixelBinningValidator(),
              trigger: 'blur'
            }, {
              validator: this.isIntegerGreaterThan('Pixel binning', 0),
              trigger: 'blur'
            }],
          targetUnderfocusMin:
            [{
              required: false,
              validator: this.isGreaterThan('Target underfocus min', 0),
              trigger: 'blur'
            }],
          targetUnderfocusMax:
            [{
              required: false,
              validator: this.isGreaterThan('Target underfocus max', 0),
              trigger: 'blur'
            }],
          exposuresPerHole:
            [{
              required: false,
              validator: this.isIntegerGreaterThan('Number of exposures per hole', 0),
              trigger: 'blur'
            }]
        }
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

      'entity.electronDetector': function (electronDetector, previousElectronDetector) {
        this.changingElectronDetector = !!previousElectronDetector //skip initial detector change

        this.$nextTick().then(() => {
          this.updateAvailableMagnifications()

          if (!this.electronDetectorLoaded && !this.isNewEntity()) { // Don't change initially electron detector for copy and edit.
            this.assignInitialMagnficationValue()
          } else {
            this.resetMagnfication(electronDetector)
          }
        }).then(() => {
          this.alertExposureSettingsChange()
          this.changingElectronDetector = false
        })
      },

      'entity.doseFractionation': function (doseFractionation, previousDoseFractionation) {
        this.recordExposureSettingsUpdate(doseFractionation, previousDoseFractionation)
        if (!doseFractionation) {
          this.entity.numberOfFrames = 1
        }
      },

      'entity.superResolution': function (superResolution, previousSuperResolution) {
        this.recordExposureSettingsUpdate(superResolution, previousSuperResolution)
        this.setSupersamplingFactorToDefaultIfNeeded(superResolution)
        this.setPixelBinningToDefaultIfNeeded(superResolution)
      },

      'magnification': function (magnification) {
        this.assignMagnification(magnification)
      },

      'entity.countingMode': function (countingMode, previousCountingMode) {
        this.recordExposureSettingsUpdate(countingMode, previousCountingMode)
      },

      'entity.numberOfFrames': function (numberOfFrames, previousNumberOfFrames) {
        this.recordExposureSettingsUpdate(numberOfFrames, previousNumberOfFrames)
      },

      'entity.supersamplingFactor': function (supersamplingFactor, previousSupersamplingFactor) {
        this.recordExposureSettingsUpdate(supersamplingFactor, previousSupersamplingFactor)
      }
    },

    methods: {
      // region Called from watchers.
      assignInitialMagnficationValue () {
        // Assign an initial value (label) of magnification.
        this.magnification = _.get(this.nominalMagnificationToOption(_.get(this, 'entity')), 'value')
        this.electronDetectorLoaded = true
      },

      resetMagnfication (electronDetector) {
        this.clearMagnification()
        this.updateExposureSettingsCheckboxes(electronDetector)
      },

      updateExposureSettingsCheckboxes (electronDetector) {
        this.entity.countingMode = !!_.get(electronDetector, 'countingModeAvailable')
        this.entity.doseFractionation = !!_.get(electronDetector, 'doseFractionationAvailable')
        this.entity.superResolution = !!_.get(electronDetector, 'superResolutionAvailable')
      },

      alertExposureSettingsChange () {
        if (this.isExposureSettingsUpdated) {
          this.isExposureSettingsUpdated = false
          this.alertWarnOK('Exposure settings were reset when you changed your choice of detector')
        }
      },

      recordExposureSettingsUpdate (currentObject, previousObject) {
        // record exposure settings change only when is due to electron detector change and new value is different from old
        if (this.changingElectronDetector === true && currentObject !== previousObject) {
          this.isExposureSettingsUpdated = true
        }
      },

      fetchElectronDetectorsForMicroscope () {
        this.$events.$emit('fetchElectronDetectorsForMicroscope')
      },

      setSupersamplingFactorToDefaultIfNeeded: function (superResolution) {
        if (!superResolution) {
          this.entity.supersamplingFactor = null
        } else if (!this.entity.supersamplingFactor) {
          const originalSupersamplingFactor = _.get(this, 'originalEntity.supersamplingFactor')
          this.entity.supersamplingFactor = originalSupersamplingFactor ? originalSupersamplingFactor : 2
        }
      },

      setPixelBinningToDefaultIfNeeded: function (superResolution) {
        if (superResolution) {
          this.entity.pixelBinning = null
        } else if (!this.entity.pixelBinning) {
          const originalPixelBinning = _.get(this, 'originalEntity.pixelBinning')
          this.entity.pixelBinning = originalPixelBinning ? originalPixelBinning : 1
        }
      },

      // endregion

      // region Magnification functions.

      getHistoricalMagnification (originalEntity) {
        if (!originalEntity) {
          return null
        }
        return this.nominalMagnificationToOption(originalEntity)
      },

      updateAvailableMagnifications () {
        this.availableMagnifications = []

        if (!this.entity.microscope || !this.entity.electronDetector) {
          this.clearMagnification()
          return
        }

        this.availableMagnifications = this.options(
          this.getAvailableMagnificationsForElectronDetector(this.entity.electronDetector),
          this.getHistoricalMagnification(this.originalEntity),
          'value'
        )
      },

      nominalMagnificationToOption (entity) {
        if (_.isNil(entity) || _.isNil(entity.nominalMagnification)) {
          return null
        }

        let option = {
          nominalMagnification: entity.nominalMagnification,
          calibratedMagnification: entity.calibratedMagnification,
          pixelSize: entity.pixelSize
        }
        option.value = option.nominalMagnification + ' '
          + parseFloat(_.get(option, 'pixelSize', 0).toFixed(3))
        option.label = this.createMagnificationLabel(entity.electronDetector, entity, option.pixelSize)

        return option
      },

      getAvailableMagnificationsForElectronDetector (electronDetector) {
        return electronDetector.availableMagnifications.map(availableMagnification =>
          this.availableMagnificationToOption(electronDetector, availableMagnification))
      },

      calculatePixelSize (electronDetector, magnification) {
        return (electronDetector.pixelLinearDimensionUm * 10000) / magnification.calibratedMagnification
      },

      createMagnificationLabel (electronDetector, magnification, previousPixelSize = null) {
        const pixelSize = previousPixelSize ? previousPixelSize : this.calculatePixelSize(electronDetector, magnification)

        return parseFloat((magnification.nominalMagnification || 0).toFixed(3))
          + ' × (' + parseFloat((pixelSize || 0).toFixed(3)) + ' Å/pixel)'
      },

      availableMagnificationToOption (electronDetector, availableMagnification) {
        if (_.isNil(availableMagnification)) {
          return null
        }

        const pixelSize = this.calculatePixelSize(electronDetector, availableMagnification)
        return Object.assign({
          pixelSize: pixelSize,
          value: availableMagnification.nominalMagnification + ' ' + parseFloat((pixelSize || 0).toFixed(3)),
          label: this.createMagnificationLabel(electronDetector, availableMagnification)
        }, availableMagnification)
      },

      clearMagnification () {
        this.magnification = null
        this.entity.nominalMagnification = null
        this.entity.calibratedMagnification = null
        this.entity.pixelSize = null
      },

      assignMagnification (magnification) {
        if (!magnification || !this.availableMagnifications) {
          return
        }

        const magnificationParts = magnification.split(' ')
        const nominalMagnification = magnificationParts[0]
        const pixelSize = magnificationParts[1]

        const selectedAvailableMagnification = this.availableMagnifications.find(availableMagnification =>
          '' + parseFloat((availableMagnification.pixelSize || 0).toFixed(3)) === pixelSize
          && availableMagnification.nominalMagnification + '' === nominalMagnification)

        if (selectedAvailableMagnification) {
          this.entity.nominalMagnification = selectedAvailableMagnification.nominalMagnification
          this.entity.calibratedMagnification = selectedAvailableMagnification.calibratedMagnification
          this.entity.pixelSize = selectedAvailableMagnification.pixelSize
        }

        this.mainForm.validateField('nominalMagnification')
      },

      // endregion

      // region Custom validators
      exposureRateValidator () {
        return (rule, value, callback) => {
          if (!value && this.entity.countingMode || value && (!this.isNumeric(value) || value <= 0)) {
            callback(new ValidationError('Exposure rate should be a number greater than 0'))
          } else if (this.isHigherThanMaxNumber(value)) {
            const MAX_NUMBER = 9999999999999999999.999999
            return callback(new ValidationError('Exposure rate should not be higher than ' +
              MAX_NUMBER.toLocaleString()))
          }
          callback()
        }
      },

      pixelBinningValidator () {
        return (rule, value, callback) => {
          return !this.entity.superResolution && !value
            ? callback(new ValidationError('Pixel binning should be an integer greater than 0'))
            : callback()
        }
      },

      supersamplingFactorValidator () {
        return (rule, value, callback) => {
          return this.entity.superResolution && !value
            ? callback(new ValidationError('Supersampling factor should be an integer greater than or equal to 2'))
            : callback()
        }
      },
      // endregion

      createExposureRateLabel (countingMode) {
        return 'Exposure rate (' + (countingMode ? 'electrons' : 'counts') + '/pixel/s)'
      },

      isNewEntity () {
        return this.$route.name === 'microscopy_session-new'
      },
    },

    created () {
      Object.assign(this.allRules || {}, this.rules)
    },
  }
</script>
