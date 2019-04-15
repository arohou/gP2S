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
      <el-col>
        <microscope-label
          :microscope-label="_.get(entity, 'microscope.label')"></microscope-label>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="8">
        <el-form-item label="Extraction voltage (kV)" prop="extractionVoltageKV"
                      :error="getError('extractionVoltageKV')">
          <el-input v-model="entity.extractionVoltageKV" id="microscopy-session__extraction-voltage"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Acceleration voltage (kV)" prop="accelerationVoltageKV"
                      :error="getError('accelerationVoltageKV')">
          <el-select v-model="entity.accelerationVoltageKV" placeholder="Select" :disabled="!entity.microscope"
                     id="microscopy-session__acceleration-voltage-kv">
            <el-option v-if="!!accelerationVoltagesKVList"
                       v-for="item in accelerationVoltagesKVList"
                       :key="item"
                       :label="item"
                       :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Gun lens setting" prop="gunLensSetting"
                      :error="getError('gunLensSetting')">
          <el-input v-model="entity.gunLensSetting" id="microscopy-session__gun-lens-setting"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="8">
        <el-form-item label="C2 aperture diameter (μm)" prop="condenser2ApertureDiameter"
                      :error="getError('condenser2ApertureDiameter')">
          <el-select v-model="entity.condenser2ApertureDiameter" placeholder="Select"
                     value-key="diameter"
                     id="microscopy-session__c2-aperture-diameter" :disabled="!entity.microscope">
            <el-option v-if="!!condenser2ApertureDiametersList"
                       v-for="item in condenser2ApertureDiametersList"
                       :key="item"
                       :label="item | formatValue"
                       :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Objective aperture" prop="objectiveAperture"
                      :error="getError('objectiveAperture')">
          <el-select v-model="entity.objectiveAperture" placeholder="Select"
                     value-key="id"
                     id="microscopy-session__objective-aperture" :disabled="!entity.microscope">
            <el-option v-if="!!item"
                       v-for="item in objectiveAperturesList"
                       :key="item.id"
                       :label="item.phasePlate ? 'Phase plate' : $options.filters.formatUnit(item.diameter, 'μm')"
                       :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Energy filter?" prop="nanoprobe"
                      :error="getError('energyFilter')">
          <el-radio-group id="microscopy-session__energy-filter" v-model="entity.energyFilter"
                          :disabled="!_.get(entity, 'microscope.energyFilter', false)">
            <el-radio-button :label="true" name="energyFilter">Yes</el-radio-button>
            <el-radio-button :label="false" name="energyFilter">No</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="8">
        <el-form-item label="Energy filter slit width (eV)" prop="energyFilterSlitWidth"
                      :error="getError('energyFilterSlitWidth')"
                      :rules="{
                            required: !!entity.energyFilter,
                            validator: this.isGreaterThan('Energy filter slit width', 0),
                            trigger: 'blur',
                          }">
          <el-input v-model="entity.energyFilterSlitWidth" :disabled="!entity.energyFilter"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import FormCommons from '@/components/mixins/FormCommons'
  import Utils from '@/components/App/MicroscopySession/Utils'
  import FilterCommons from '@/components/mixins/FilterCommons'
  import MicroscopeLabel from './MicroscopeLabel'

  export default {
    components: {MicroscopeLabel},
    name: 'microscopy-session-base-form',

    props: {
      allRules: Object,
      mainForm: Object,
      projectId: String,
      entity: Object,
      allErrors: Object,
      originalEntityBaseForm: Object
    },

    mixins: [
      CommonValidations, Utils, FilterCommons, FormCommons
    ],

    data () {
      return {
        microscopeLoaded: false,

        rules: {
          extractionVoltageKV: [{
            required: true,
            validator: this.isGreaterThan('Extraction voltage', 0),
            trigger: 'blur'
          }],
          gunLensSetting: [{
            required: false,
            validator: this.isIntegerGreaterThan('Gun lens setting', 0),
            trigger: 'blur'
          }]
        }
      }
    },

    computed: {
      accelerationVoltagesKVList: function () {
        if (!_.get(this, 'entity.microscope')) {
          return []
        }
        else if (_.get(this, 'originalEntity.microscope.id') === this.entity.microscope.id) {
          return this.options(_.get(this, 'entity.microscope.availableVoltagesKV', []),
            _.get(this, 'originalEntity.accelerationVoltageKV'))
        }
        else {
          return this.options(_.get(this, 'entity.microscope.availableVoltagesKV', []))
        }
      },

      condenser2ApertureDiametersList: function () {
        if (!_.get(this, 'entity.microscope')) {
          return []
        }

        let condenser2ApertureDiameters = [
          this.entity.microscope.condenser1ApertureDiameter,
          this.entity.microscope.condenser2ApertureDiameter,
          this.entity.microscope.condenser3ApertureDiameter,
          this.entity.microscope.condenser4ApertureDiameter]

        if (_.get(this, 'originalEntity.microscope.id') === this.entity.microscope.id) {
          return this.options(condenser2ApertureDiameters,
            _.get(this, 'originalEntity.condenser2ApertureDiameter'))
        }

        return this.options(condenser2ApertureDiameters)
      },

      objectiveAperturesList: function () {
        if (!_.get(this, 'entity.microscope')) {
          return []
        }

        // Create objective apertures list.
        const availableApertures = [this.entity.microscope.objectiveAperture1,
          this.entity.microscope.objectiveAperture2, this.entity.microscope.objectiveAperture3,
          this.entity.microscope.objectiveAperture4].map(this.decorateObjectiveApertureWithId)

        if (this.entity.microscope.id === _.get(this, 'originalEntity.microscope.id')) {
          return this.options(availableApertures,
            this.decorateObjectiveApertureWithId(_.get(this, 'originalEntity.objectiveAperture')), 'id')
        } else {
          return this.options(availableApertures, null, 'id')
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

      'entity.microscope': function (newMicroscope, oldMicroscope) {
        this.updateCondenser2ApertureDiameter(newMicroscope)
        this.updateMicroscopeSettings(newMicroscope)
      },

      'entity.objectiveAperture.diameter': function (objectiveApertureDiameter) {
        // Decorate objective aperture with ID with ID necessary for selection list.
        this.entity.objectiveAperture = this.decorateObjectiveApertureWithId({
          phasePlate: this.entity.objectiveAperture.phasePlate,
          diameter: objectiveApertureDiameter
        })
      },

      'entity.energyFilter': function (energyFilter) {
        if (!energyFilter) {
          this.entity.energyFilterSlitWidth = null
        }
      },
    },

    methods: {
      // region Called from watchers.

      updateCondenser2ApertureDiameter (microscope) {
        if (!microscope) {
          return
        }

        if (this.isNewEntity() || this.microscopeLoaded) {
          this.entity.condenser2ApertureDiameter = this.getDefaultCondenser2ApertureDiameter(microscope)
        }
      },

      updateObjectiveApertureFromMicroscopeDefault (microscope) {
        if (microscope.defaultObjectiveApertureIndex) {
          this.entity.objectiveAperture = this.getDefaultObjectiveAperture(microscope)
          this.entity.objectiveAperture = this.decorateObjectiveApertureWithId(this.entity.objectiveAperture)
        }
      },

      setMicroscopeLoadedFlag () {
        // This is crucial to make sure, that all events triggered by microscope change
        // get executed before we set this flag.
        this.$nextTick().then(() => {
          this.microscopeLoaded = true
        })
      },

      extractMicroscopeSettings (microscope) {
        this.entity.accelerationVoltageKV = microscope.defaultVoltageKV
        this.entity.extractionVoltageKV = microscope.defaultExtractionVoltageKV
        this.entity.gunLensSetting = microscope.defaultGunLensSetting

        const accelerationVoltageUnavailable = microscope && this.entity.condenser2ApertureDiameter
          && microscope.availableVoltagesKV
          && !microscope.availableVoltagesKV.includes(this.entity.accelerationVoltageKV)
        if (accelerationVoltageUnavailable) {
          this.entity.accelerationVoltageKV = null
        }

        this.entity.energyFilter = microscope.energyFilter
        this.entity.energyFilterSlitWidth = microscope.energyFilter ? this.entity.microscope.defaultEnergyFilterSlitWidth : null

        this.updateObjectiveApertureFromMicroscopeDefault(microscope)
      },

      updateMicroscopeSettings (microscope) {
        if (!this.microscopeLoaded && !this.isNewEntity()) {
          this.setMicroscopeLoadedFlag()
        } else if (microscope) {
          this.extractMicroscopeSettings(microscope)
        }
      },
      // endregion

      /**
       * Adds a unique id field to the objectiveAperture object.
       *
       * The {id} is used in the UI selector to map label to the value.
       *
       * @param {Object} objectiveAperture — The object to add the id field to.
       * @returns {Object} - the objectiveAperture with the id field.
       */
      decorateObjectiveApertureWithId (objectiveAperture) {
        if (!objectiveAperture) {
          return null
        }

        return Object.assign({
          id: objectiveAperture.phasePlate ? 'phasePlate' : objectiveAperture.diameter || '—'
        }, objectiveAperture)
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
