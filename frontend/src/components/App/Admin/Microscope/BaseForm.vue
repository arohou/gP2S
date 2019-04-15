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
  <div class="microscope-base-form base-form">
    <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" labelPosition="top">
      <div class="base-form__metadata">
        <el-row>
          <el-col>
            <el-form-item label="Microscope label" prop="label" :error="getError('label')">
              <el-input v-model="entity.label"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="Manufacturer" prop="manufacturer" :error="getError('manufacturer')">
              <el-input v-model="entity.manufacturer"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Model" prop="model" :error="getError('model')">
              <el-input v-model="entity.model"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Location" prop="location" :error="getError('location')">
              <el-input v-model="entity.location"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col class="el-col--no-padding" :span="8">
            <el-col :span="14">
              <el-form-item label="Available acceleration voltages (kV)" prop="availableVoltagesKV"
                            :error="getError('availableVoltagesKV')">
                <el-select v-model="entity.availableVoltagesKV" multiple placeholder="Select" class="width-100"
                           @change="adjustDefaultVoltage()">
                  <el-option
                    v-for="item in [80, 120, 200, 300]"
                    :key="item"
                    :label="item"
                    :value="item">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="Default acceleration voltage" prop="defaultVoltageKV"
                            :error="getError('defaultVoltageKV')">
                <el-select v-model="entity.defaultVoltageKV" placeholder="Select"
                           @change="$refs['mainForm'].validateField('defaultVoltageKV')">
                  <el-option
                    v-for="voltage in this.sortedAvailableVoltagesKV"
                    :key="voltage"
                    :label="voltage"
                    :value="voltage">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Default extraction voltage (kV)" prop="defaultExtractionVoltageKV"
                          :error="getError('defaultExtractionVoltageKV')">
              <el-input v-model="entity.defaultExtractionVoltageKV"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Default gun lens setting" prop="defaultGunLensSetting"
                          :error="getError('defaultGunLensSetting')">
              <el-input v-model="entity.defaultGunLensSetting"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="Condenser (&quot;C2&quot;) apertures">
              <el-row class="small-margin">
                <el-col :sm="1">&nbsp;</el-col>
                <el-col :sm="6">Diameter (μm)</el-col>
                <el-col :sm="1">&nbsp;</el-col>
                <el-col :sm="6">Is default?</el-col>
              </el-row>
              <el-row class="small-margin">
                <el-form-item prop="condenser1ApertureDiameter" :error="getError('condenser1ApertureDiameter')"
                              class="no-margin">
                  <el-row :gutter="14">
                    <el-col :sm="1"><span>1.</span></el-col>
                    <el-col :sm="6">
                      <el-input v-model="entity.condenser1ApertureDiameter"></el-input>
                    </el-col>
                    <el-col :sm="1">&nbsp;</el-col>
                    <el-col :sm="6">
                      <el-radio v-model="entity.defaultCondenserApertureIndex" :label="1">&nbsp;</el-radio>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-row>
              <el-row class="small-margin">
                <el-form-item prop="condenser2ApertureDiameter" :error="getError('condenser2ApertureDiameter')"
                              class="no-margin">
                  <el-row :gutter="14">
                    <el-col :sm="1"><span>2.</span></el-col>
                    <el-col :sm="6">
                      <el-input v-model="entity.condenser2ApertureDiameter"></el-input>
                    </el-col>
                    <el-col :sm="1">&nbsp;</el-col>
                    <el-col :sm="6">
                      <el-radio v-model="entity.defaultCondenserApertureIndex" :label="2">&nbsp;</el-radio>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-row>
              <el-row class="small-margin">
                <el-form-item prop="condenser3ApertureDiameter" :error="getError('condenser3ApertureDiameter')"
                              class="no-margin">
                  <el-row :gutter="14">
                    <el-col :sm="1"><span>3.</span></el-col>
                    <el-col :sm="6">
                      <el-input v-model="entity.condenser3ApertureDiameter"></el-input>
                    </el-col>
                    <el-col :sm="1">&nbsp;</el-col>
                    <el-col :sm="6">
                      <el-radio v-model="entity.defaultCondenserApertureIndex" :label="3">&nbsp;</el-radio>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-row>
              <el-row class="small-margin">
                <el-form-item prop="condenser4ApertureDiameter" :error="getError('condenser4ApertureDiameter')"
                              class="no-margin">
                  <el-row :gutter="14">
                    <el-col :sm="1"><span>4.</span></el-col>
                    <el-col :sm="6">
                      <el-input v-model="entity.condenser4ApertureDiameter"></el-input>
                    </el-col>
                    <el-col :sm="1">&nbsp;</el-col>
                    <el-col :sm="6">
                      <el-radio v-model="entity.defaultCondenserApertureIndex" :label="4">&nbsp;</el-radio>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-row>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Sample insertion mechanism" prop="sampleInsertionMechanism"
                          :error="getError('sampleInsertionMechanism')">
              <el-select v-model="entity.sampleInsertionMechanism" placeholder="Select"
                         @change="$refs['mainForm'].validateField('sampleInsertionMechanism')">
                <el-option
                  v-for="item in [{value: 'SIDE_ENTRY_HOLDER', label: 'side-entry holder'}, {value: 'AUTO_LOADER', label: 'autoloader'}]"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Objective apertures">
              <el-row class="small-margin" :gutter="14">
                <el-col :sm="1">&nbsp;</el-col>
                <el-col :sm="6">Diameter (μm)</el-col>
                <el-col :sm="11">Phase plate</el-col>
                <el-col :sm="6">Is default?</el-col>
              </el-row>
              <el-row class="small-margin" :class="{'phase-plate-active': entity.objectiveAperture1.phasePlate}">
                <el-form-item prop="objectiveAperture1" :error="getError('objectiveAperture1')" class="no-margin"
                              :show-message="!entity.objectiveAperture1.phasePlate">
                  <el-row :gutter="14">
                    <el-col :sm="1"><span>1.</span></el-col>
                    <el-col :sm="6">
                      <el-input v-model="entity.objectiveAperture1.diameter"
                                :disabled="entity.objectiveAperture1.phasePlate"></el-input>
                    </el-col>
                    <el-col :sm="11">
                      <el-form-item prop="objectiveAperture1.phasePlate">
                        <el-radio-group v-model="entity.objectiveAperture1.phasePlate"
                                        @change="removeDiameterIfPhasePlateChecked(entity.objectiveAperture1.phasePlate, 1, entity.objectiveAperture1.phasePlate)">
                          <el-radio-button :label="true">Yes</el-radio-button>
                          <el-radio-button :label="false">No</el-radio-button>
                        </el-radio-group>
                      </el-form-item>
                    </el-col>
                    <el-col :sm="6">
                      <el-radio v-model="entity.defaultObjectiveApertureIndex" :label="1">&nbsp;</el-radio>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-row>
              <el-row class="small-margin" :class="{'phase-plate-active': entity.objectiveAperture2.phasePlate}">
                <el-form-item prop="objectiveAperture2" :error="getError('objectiveAperture2')" class="no-margin"
                              :show-message="!entity.objectiveAperture2.phasePlate">
                  <el-row :gutter="14">
                    <el-col :sm="1">2.</el-col>
                    <el-col :sm="6">
                      <el-input v-model="entity.objectiveAperture2.diameter"
                                :disabled="entity.objectiveAperture2.phasePlate"></el-input>
                    </el-col>
                    <el-col :sm="11">
                      <el-form-item prop="objectiveAperture2.phasePlate">
                        <el-radio-group v-model="entity.objectiveAperture2.phasePlate"
                                        @change="removeDiameterIfPhasePlateChecked(entity.objectiveAperture2.phasePlate, 2, entity.objectiveAperture2.phasePlate)">
                          <el-radio-button :label="true">Yes</el-radio-button>
                          <el-radio-button :label="false">No</el-radio-button>
                        </el-radio-group>
                      </el-form-item>
                    </el-col>
                    <el-col :sm="6">
                      <el-radio v-model="entity.defaultObjectiveApertureIndex" :label="2">&nbsp;</el-radio>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-row>
              <el-row class="small-margin" :class="{'phase-plate-active': entity.objectiveAperture3.phasePlate}">
                <el-form-item prop="objectiveAperture3" :error="getError('objectiveAperture3')" class="no-margin"
                              :show-message="!entity.objectiveAperture3.phasePlate">
                  <el-row :gutter="14">
                    <el-col :sm="1">3.</el-col>
                    <el-col :sm="6">
                      <el-input v-model="entity.objectiveAperture3.diameter"
                                :disabled="entity.objectiveAperture3.phasePlate"></el-input>
                    </el-col>
                    <el-col :sm="11">
                      <el-form-item prop="objectiveAperture3.phasePlate">
                        <el-radio-group v-model="entity.objectiveAperture3.phasePlate"
                                        @change="removeDiameterIfPhasePlateChecked(entity.objectiveAperture3.phasePlate, 3, entity.objectiveAperture3.phasePlate)">
                          <el-radio-button :label="true">Yes</el-radio-button>
                          <el-radio-button :label="false">No</el-radio-button>
                        </el-radio-group>
                      </el-form-item>
                    </el-col>
                    <el-col :sm="6">
                      <el-radio v-model="entity.defaultObjectiveApertureIndex" :label="3">&nbsp;</el-radio>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-row>
              <el-row class="small-margin" :class="{'phase-plate-active': entity.objectiveAperture4.phasePlate}">
                <el-form-item prop="objectiveAperture4" :error="getError('objectiveAperture4')" class="no-margin"
                              :show-message="!entity.objectiveAperture4.phasePlate">
                  <el-row :gutter="14">
                    <el-col :sm="1">4.</el-col>
                    <el-col :sm="6">
                      <el-input v-model="entity.objectiveAperture4.diameter"
                                :disabled="entity.objectiveAperture4.phasePlate"></el-input>
                    </el-col>
                    <el-col :sm="11">
                      <el-form-item prop="objectiveAperture4.phasePlate">
                        <el-radio-group v-model="entity.objectiveAperture4.phasePlate"
                                        @change="removeDiameterIfPhasePlateChecked(entity.objectiveAperture4.phasePlate, 4, entity.objectiveAperture4.phasePlate)">
                          <el-radio-button :label="true">Yes</el-radio-button>
                          <el-radio-button :label="false">No</el-radio-button>
                        </el-radio-group>
                      </el-form-item>
                    </el-col>
                    <el-col :sm="6">
                      <el-radio v-model="entity.defaultObjectiveApertureIndex" :label="4">&nbsp;</el-radio>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-row>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="Energy filter?" prop="energyFilter" :error="getError('energyFilter')">
              <el-radio-group v-model="entity.energyFilter">
                <el-radio-button :label="true">Yes</el-radio-button>
                <el-radio-button :label="false">No</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Default energy filter slit width (eV)" prop="defaultEnergyFilterSlitWidth"
                          :error="getError('defaultEnergyFilterSlitWidth')"
                          :rules="{
                            required: !!entity.energyFilter,
                            validator: this.isGreaterThan('Energy filter slit width', 0),
                            trigger: 'blur',
                          }">
              <el-input v-model="entity.defaultEnergyFilterSlitWidth" :disabled="!entity.energyFilter"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Default spot size" prop="defaultSpotSize" :error="getError('defaultSpotSize')">
              <el-input v-model="entity.defaultSpotSize"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import InsertionMechanismType from '@/components/App/Admin/Microscope/InsertionMechanismType'
  import ValidationError from '@/errors/ValidationError'

  export default {
    name: 'microscope-base-form',

    props: {
      entity: {}
    },

    mixins: [
      CommonValidations
    ],
    components: [
      InsertionMechanismType
    ],

    data () {
      return {
        rules: {
          label: [
            {
              required: true,
              message: 'Microscope label can\'t be empty',
              trigger: 'blur'
            }
          ],
          manufacturer: [{
            required: true,
            message: 'Please enter manufacturer',
            trigger: 'blur'
          }],
          model: [{
            required: true,
            message: 'Please enter model',
            trigger: 'blur'
          }],
          location: [{
            required: true,
            message: 'Please enter location',
            trigger: 'blur'
          }],
          defaultExtractionVoltageKV: [{
            required: true,
            validator: this.isGreaterThan('Extraction voltage', 0),
            trigger: 'blur'
          }],
          defaultVoltageKV: [{
            required: true,
            message: 'Choose one of available acceleration voltages',
            validator: this.isGreaterThan('Default acceleration voltage', 0),
            trigger: 'change'
          }],
          availableVoltagesKV: [{
            required: true,
            validator: this.isEmptyArray('List of available acceleration voltages'),
            trigger: 'change'
          }],
          defaultGunLensSetting: [{
            required: true,
            validator: this.isIntegerGreaterThan('Gun lens setting', 0),
            trigger: 'blur'
          }],
          sampleInsertionMechanism: [{
            required: true,
            message: 'Please choose sample insertion mechanism',
            trigger: 'change'
          }],
          condenser1ApertureDiameter: [{
            required: false,
            validator: this.isIntegerGreaterThan('Diameter', 0),
            trigger: 'blur'
          }],
          condenser2ApertureDiameter: [{
            required: false,
            validator: this.isIntegerGreaterThan('Diameter', 0),
            trigger: 'blur'
          }],
          condenser3ApertureDiameter: [{
            required: false,
            validator: this.isIntegerGreaterThan('Diameter', 0),
            trigger: 'blur'
          }],
          condenser4ApertureDiameter: [{
            required: false,
            validator: this.isIntegerGreaterThan('Diameter', 0),
            trigger: 'blur'
          }],
          objectiveAperture1: [{
            required: false,
            validator: this.validateObjectiveAperture(),
            trigger: 'blur'
          }],
          objectiveAperture2: [{
            required: false,
            validator: this.validateObjectiveAperture(),
            trigger: 'blur'
          }],
          objectiveAperture3: [{
            required: false,
            validator: this.validateObjectiveAperture(),
            trigger: 'blur'
          }],
          objectiveAperture4: [{
            required: false,
            validator: this.validateObjectiveAperture(),
            trigger: 'blur'
          }],
          defaultSpotSize: [{
            required: true,
            validator: this.isIntegerGreaterThan('Default spot size', 0),
            trigger: 'blur'
          }]
        }
      }
    },

    computed: {
      sortedAvailableVoltagesKV: function () {
        return this.entity.availableVoltagesKV.slice().sort((a, b) => a - b)
      }
    },

    mounted () {
      this.$refs['mainForm'].resetFields()
    },

    methods: {
      adjustDefaultVoltage () {
        if (this.entity.availableVoltagesKV) {
          if (this.entity.defaultVoltageKV && this.entity.availableVoltagesKV.indexOf(this.entity.defaultVoltageKV) >= 0) {
            return
          } else if (this.entity.availableVoltagesKV.length > 0 && this.entity.defaultVoltageKV != null) {
            this.entity.defaultVoltageKV = this.entity.availableVoltagesKV[0]
            return
          }
          this.entity.defaultVoltageKV = null
        }
      },
      removeDiameterIfPhasePlateChecked (phasePlate, index) {
        if (phasePlate === true) {
          switch (index) {
            case 1:
              this.entity.objectiveAperture1.diameter = null
              break
            case 2:
              this.entity.objectiveAperture2.diameter = null
              break
            case 3:
              this.entity.objectiveAperture3.diameter = null
              break
            case 4:
              this.entity.objectiveAperture4.diameter = null
              break
          }
        }
      },
      validateObjectiveAperture () {
        return (rule, objectiveAperture, callback) => {
          if (!rule.required && !objectiveAperture.diameter) {
            callback()
          } else if (objectiveAperture && !objectiveAperture['phasePlate']) {
            let diameter = objectiveAperture['diameter']
            if (!diameter || /\D/.test(diameter) || diameter <= 0) {
              return callback(new ValidationError('Diameter should be an integer greater than 0'))
            } else if (this.isHigherThanMaxInteger(objectiveAperture.diameter)) {
              const MAX_INTEGER = 999999999
              return callback(new ValidationError('Diameter should not be higher than ' + MAX_INTEGER.toLocaleString()))
            }
          }
          return callback()
        }
      }
    }
  }
</script>

<style scoped>
  .el-radio, .el-checkbox {
    margin-left: 12%;
    width: 8%;
  }

  .small-margin {
    margin-bottom: 0.3em;
  }

  .small-margin .el-form-item.is-error {
    margin-bottom: 1.3em;
  }

  .small-margin.phase-plate-active .el-form-item.is-error {
    margin-bottom: 0;
  }

  .no-margin {
    margin-bottom: 0;
  }
</style>
