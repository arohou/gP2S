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
  <el-row :key="index"
          class="sample-base-form__components__component-row"
          :gutter="20">
    <el-col :span="6">
      <el-form-item class="sample-base-form__components__component"
                    label="Protein"
                    :rules="{
                          required: true,
                          message: 'Please select a component',
                          trigger: 'change'
                        }"
                    :prop="'components.' + index + '.aliquot.label'">
        <el-col :span="20">
          <aliquot-selector :projectId="projectId" :component="component"></aliquot-selector>
        </el-col>

        <el-button class="sample-base-form__components__edit"
                   @click="editComponent()" size="small">
          <img src="~@/assets/images/edit.svg" alt="Edit"/>
        </el-button>
      </el-form-item>
    </el-col>

    <el-col :span="4">
      <el-form-item label="Unit" :error="getError('unit')">
        <el-radio-group id="unit-type" v-model="unitType" :disabled="conversionIsNotAvailable"
                        :title="unitTypeDisabledMessage">
          <el-radio-button :label="UnitType.MICROMOLAR">{{ UnitType.MICROMOLAR.unitValue }}</el-radio-button>
          <el-radio-button :label="UnitType.MGPERML">{{ UnitType.MGPERML.unitValue}}</el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-col>

    <el-col :span="3">
      <el-form-item class="sample-base-form__components__concentration"
                    :label="uMLabel"
                    :rules="{
                            required: true,
                            validator: isGreaterThan('Concentration', 0),
                            trigger: 'blur'
                          }"
                    :prop="'components.' + index + '.finalConcentration'">
        <el-input v-model="component.finalConcentration"
                  :disabled="isFinalConcentrationInUmDisabled"
                  @blur="performConversionToMgPerMl()"></el-input>
      </el-form-item>
    </el-col>

    <el-col :span="3">
      <el-form-item class="sample-base-form__components__concentration"
                    :label="mgPerMlLabel"
                    :rules="{
                            required: true,
                            validator: isGreaterThan('Concentration', 0),
                            trigger: 'blur'
                          }"
                    :prop="'components.' + index + '.finalConcentrationInMgPerMl'">
        <span v-if="conversionIsNotAvailable">{{NOT_APPLY_MESSAGE}}</span>
        <el-input v-else v-model="component.finalConcentrationInMgPerMl"
                  :disabled="isFinalConcentrationInMgPerMlDisabled"
                  @blur="performConversionToUm()"></el-input>
      </el-form-item>
    </el-col>

    <el-col :span="3">
      <el-form-item class="sample-base-form__components__used-volume"
                    label="MW used for conversion">
        <span v-if="conversionIsNotAvailable" class="el-form-item__error">{{molecularWeightNotAvailableMessage}}</span>
        <span v-else>{{molecularWeightDa | formatToKDa}}</span>
      </el-form-item>
    </el-col>
    <el-col :span="2">
      <el-form-item class="no-label">
        <el-checkbox v-model="lastDrop">last drop</el-checkbox>
      </el-form-item>
    </el-col>
    <el-col :span="2">
      <el-form-item class="no-label">
        <el-button @click="removeComponent(index)" type="danger">Remove</el-button>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script>
  import ComponentType from '@/components/App/Sample/ComponentType'
  import AliquotSelector from '@/components/Selectors/AliquotSelector'
  import CommonValidations from '@/components/mixins/CommonValidations'
  import UnitType from '@/components/App/Sample/UnitType'
  import ProteinService from '@/services/ProteinService'
  import ProteinRegistrationService from '@/components/App/Protein/registration_system_ext/ProteinRegistrationService'
  import DialogsService from '@/services/DialogsService'
  import { proteinRegistrationSystemName } from '@/utils/ExternalSystemUtils'
  import ConcentrationConversionService from '@/services/ConcentrationConversionService'

  const proteinRegistrationService = new ProteinRegistrationService()
  const proteinService = new ProteinService()

  export default {
    name: 'ProteinComponent',

    components: {
      AliquotSelector
    },

    mixins: [CommonValidations],

    props: {
      component: {
        type: Object,
        required: true
      },
      index: {
        type: Number,
        required: true
      },
      projectId: {
        type: String,
        required: true
      }
    },

    data () {
      return {
        UnitType: UnitType,
        ComponentType: ComponentType,
        unitType: UnitType.MICROMOLAR,
        unitTypeDisabledMessage: '',
        conversionIsNotAvailable: true,
        molecularWeightDa: null,
        molecularWeightNotAvailableMessage: '',
        rules: {
          unitType: [{
            required: true,
            validator: this.isDefined('Unit type'),
            trigger: 'change'
          }],
        }
      }
    },

    methods: {
      initMessages () {
        this.PROTEIN_SERVICE_NOT_RESPONDING_MESSAGE_UNIT_SELECTOR =
          this.proteinRegistrationServiceName + ' is not responding. Please provide concentration in μM.'
        this.PROTEIN_SERVICE_NOT_RESPONDING_MESSAGE_MWEIGHT_LABEL =
          this.proteinRegistrationServiceName + ' is not responding. Conversion not possible.'
        this.NO_MOLECULAR_WEIGHT_MESSAGE_UNIT_SELECTOR =
          'There is no molecular weight in ' + this.proteinRegistrationServiceName
          + ' for this protein. Please provide concentration in μM.'
        this.NO_MOLECULAR_WEIGHT_MESSAGE_MWEIGHT_LABEL =
          'There is no molecular weight in ' + this.proteinRegistrationServiceName
          + ' for this protein. Conversion not possible.'
        this.NOT_APPLY_MESSAGE = 'NA'

        this.unitTypeDisabledMessage = this.PROTEIN_SERVICE_NOT_RESPONDING_MESSAGE_UNIT_SELECTOR
      },

      editComponent () {
        this.$events.$emit('editOfProteinComponent', this.component.aliquot.id)
      },

      removeComponent (index) {
        this.$events.$emit('componentRemoved', index)
      },

      proteinServiceResponded (molecularWeightDa) {
        if (!molecularWeightDa) {
          this.molecularWeightNotAvailable()
          return
        }
        this.conversionIsNotAvailable = false
        this.unitTypeDisabledMessage = ''
        this.molecularWeightDa = molecularWeightDa
      },
      proteinServiceNotResponding () {
        this.conversionIsNotAvailable = true
        this.unitTypeDisabledMessage = this.PROTEIN_SERVICE_NOT_RESPONDING_MESSAGE_UNIT_SELECTOR
        this.molecularWeightNotAvailableMessage = this.PROTEIN_SERVICE_NOT_RESPONDING_MESSAGE_MWEIGHT_LABEL
        this.makeSureMgPerMlValidationWillPass()
      },
      loadMolecularWeightDa () {
        return proteinRegistrationService.findProteinRegistrationEntity(this.getPurId())
          .then(response => this.proteinServiceResponded(response.data.molecularWeightDa))
          .catch(this.proteinServiceNotResponding)
      },
      /**
       * When ProteinService is not responding, we cannot calculate finalConcentrationInMgPerMl for the user
       * However, to pass the validation, we need to assign valid value to this field
       */
      makeSureMgPerMlValidationWillPass () {
        this.component.finalConcentrationInMgPerMl = 1
      },
      molecularWeightNotAvailable () {
        this.conversionIsNotAvailable = true
        this.unitTypeDisabledMessage = this.NO_MOLECULAR_WEIGHT_MESSAGE_UNIT_SELECTOR
        this.molecularWeightNotAvailableMessage = this.NO_MOLECULAR_WEIGHT_MESSAGE_MWEIGHT_LABEL
        this.makeSureMgPerMlValidationWillPass()
      },
      conversionCannotBePerformed (concentration) {
        return this.conversionIsNotAvailable
          || !concentration || !this.isNumeric(concentration) || concentration <= 0
      },
      getPurId () {
        return this.component.aliquot.purificationId
      },
      performConversionToMgPerMl () {
        const finalConcentrationUm = this._.get(this, 'component.finalConcentration')
        if (this.conversionCannotBePerformed(finalConcentrationUm)) {
          return
        }
        this.component.finalConcentrationInMgPerMl =
          this._concentrationConversionService.uMToMgPerMl(finalConcentrationUm, this.molecularWeightDa)
      },
      performConversionToUm () {
        if (this.conversionCannotBePerformed(this.component.finalConcentrationInMgPerMl)) {
          return
        }
        this.component.finalConcentration = this._concentrationConversionService.mgPerMlToUm(
          this.component.finalConcentrationInMgPerMl, this.molecularWeightDa)
      },
      isThisNewProteinSelected () {
        return !this.getPurId()
      },
      assignProtein (protein) {
        this.component.aliquot = protein
        this.component.aliquot.concentration.concentrationType = protein.concentration.concentrationType.name
      },
      reloadProteinComponent () {
        this.unitType = UnitType.MICROMOLAR
        if (this.isThisNewProteinSelected()) {
          proteinService.getProteinBy(this.component.aliquot.id)
                        .then(response => this.assignProtein(response.data))
                        .then(this.loadMolecularWeightDa)
                        .then(this.performConversionToMgPerMl)
                        .catch(error => DialogsService.showError(this._.get(error, 'errorMessage', error)))
        } else {
          this.loadMolecularWeightDa()
              .then(this.performConversionToMgPerMl)
              .catch(error => DialogsService.showError(this._.get(error, 'errorMessage', error)))
        }
      }
    },

    computed: {
      isFinalConcentrationInMgPerMlDisabled () {
        return this.unitType === UnitType.MICROMOLAR
      },
      isFinalConcentrationInUmDisabled () {
        return !this.isFinalConcentrationInMgPerMlDisabled
      },
      uMLabel () {
        if (this.isFinalConcentrationInUmDisabled) {
          return 'Converted (μM)'
        } else {
          return 'Final conc. (μM)'
        }
      },
      mgPerMlLabel () {
        if (this.isFinalConcentrationInMgPerMlDisabled) {
          return 'Converted (mg/ml)'
        } else {
          return 'Final conc. (mg/ml)'
        }
      },
      proteinRegistrationServiceName: function () {
        return proteinRegistrationSystemName()
      },

      lastDrop: {
        get: function () {
          return !this.component.aliquot.availableForSampleMaking
        },

        set: function (value) {
          this.component.aliquot.availableForSampleMaking = !value
        }
      }
    },

    watch: {
      'component.aliquot' () {
        this.reloadProteinComponent()
      }
    },

    filters: {
      formatToKDa (molecularWeightDa) {
        return molecularWeightDa ? (molecularWeightDa / 1000) + ' kDa' : null
      }
    },

    created () {
      this._concentrationConversionService = new ConcentrationConversionService()
      this.initMessages()
    },

    mounted () {
      this.reloadProteinComponent()
    },
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .no-label {
    margin-top: 2.231rem;
  }
</style>
