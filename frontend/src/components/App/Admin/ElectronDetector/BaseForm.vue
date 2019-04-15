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
  <div class="electron-detector-base-form base-form">
    <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" labelPosition="top">
      <div class="base-form__metadata">
        <el-row :gutter="24">
          <el-col :sm="16">
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="Electron detector label" prop="label" :error="getError('label')">
                  <el-input v-model="entity.label"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="Manufacturer" prop="manufacturer" :error="getError('manufacturer')">
                  <el-input v-model="entity.manufacturer"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="Model" prop="model" :error="getError('model')">
                  <el-input v-model="entity.model"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="Microscope" prop="microscope" :error="getError('microscope')">
                  <el-select v-model="entity.microscope" filterable placeholder="Select a microscope"
                             value-key="id">
                    <el-option
                      v-for="item in microscopes"
                      :key="item.id"
                      :label="item.label"
                      :value="item">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="Counts-per-electrons factor" prop="countsPerElectronsFactor"
                              :error="getError('countsPerElectronsFactor')">
                  <el-input v-model="entity.countsPerElectronsFactor"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="Pixel linear dimension (μm)" prop="pixelLinearDimensionUm"
                              :error="getError('pixelLinearDimensionUm')">
                  <el-input v-model="entity.pixelLinearDimensionUm"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="Number of pixel columns" prop="numberOfPixelColumns"
                              :error="getError('numberOfPixelColumns')">
                  <el-input v-model="entity.numberOfPixelColumns"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="Number of pixel rows" prop="numberOfPixelRows"
                              :error="getError('numberOfPixelRows')">
                  <el-input v-model="entity.numberOfPixelRows"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="Counting mode available?" prop="countingModeAvailable"
                              :error="getError('countingModeAvailable')">
                  <el-radio-group v-model="entity.countingModeAvailable">
                    <el-radio-button :label="true">Yes</el-radio-button>
                    <el-radio-button :label="false">No</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="Dose fractionation available?" prop="doseFractionationAvailable"
                              :error="getError('doseFractionationAvailable')">
                  <el-radio-group v-model="entity.doseFractionationAvailable">
                    <el-radio-button :label="true">Yes</el-radio-button>
                    <el-radio-button :label="false">No</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="Super resolution available?" prop="superResolutionAvailable"
                              :error="getError('superResolutionAvailable')">
                  <el-radio-group v-model="entity.superResolutionAvailable">
                    <el-radio-button :label="true">Yes</el-radio-button>
                    <el-radio-button :label="false">No</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :sm="8">
            <el-row>
              <el-form-item label="Magnifications" id="magnificationsForm"
                            :prop="'availableMagnifications'"
                            :error="getError('availableMagnifications')">
                <el-button class="sample-base-form__add-existing"
                           @click="addMagnification()">Add new
                </el-button>
                <el-row class="small-margin">
                  <el-col :sm="8" class="sub-label">Nominal (×)</el-col>
                  <el-col :sm="8" class="sub-label">Calibrated (×)</el-col>
                </el-row>
                <el-row v-for="(magnification, index) in entity.availableMagnifications"
                        :key="index"
                        class="view__metadata__value view__metadata__microscope__list-value">
                  <el-row class="small-margin">
                    <el-col :sm="8">
                      <el-form-item
                        class="no-margin" :rules="{
                            required: true,
                            validator: isIntegerGreaterThan('Nominal magnification', 0),
                            trigger: 'blur',
                          }"
                        :prop="'availableMagnifications[' + index + '].nominalMagnification'"
                        :error="getError('availableMagnifications[' + index + '].nominalMagnification')">
                        <el-input class="width-80" v-model="magnification.nominalMagnification"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :sm="8">
                      <el-form-item
                        class="no-margin" :rules="{
                            required: true,
                            validator: isGreaterThan('Calibrated magnification', 0),
                            trigger: 'blur',
                          }"
                        :prop="'availableMagnifications[' + index + '].calibratedMagnification'"
                        :error="getError('availableMagnifications[' + index + '].calibratedMagnification')">
                        <el-input class="width-80" v-model="magnification.calibratedMagnification"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :sm="8">
                      <el-form-item class="no-margin">
                        <el-button @click="removeMagnification(index)" type="danger">Remove</el-button>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-row>
              </el-form-item>
            </el-row>
          </el-col>
        </el-row>
      </div>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import FormCommons from '@/components/mixins/FormCommons'
  import Service from '@/services/MicroscopeService'

  const service = new Service()

  export default {
    name: 'electron-detector-base-form',

    props: {
      entity: {}
    },

    mixins: [
      CommonValidations, FormCommons
    ],

    data () {
      return {
        microscopes: [],
        rules: {
          label: [
            {
              required: true,
              message: 'Electron detector label can\'t be empty',
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
          microscope: [{
            required: true,
            validator: this.isDefined('Microscope'),
            trigger: 'change'
          }],
          countsPerElectronsFactor: [{
            required: true,
            validator: this.isGreaterThan('Counts-per-electrons factor', 0),
            trigger: 'blur'
          }],
          pixelLinearDimensionUm: [{
            required: true,
            validator: this.isGreaterThan('Pixel linear dimension', 0),
            trigger: 'blur'
          }],
          numberOfPixelColumns: [{
            required: true,
            validator: this.isIntegerGreaterThan('Number of pixel columns', 0),
            trigger: 'blur'
          }],
          numberOfPixelRows: [{
            required: true,
            validator: this.isIntegerGreaterThan('Number of pixel rows', 0),
            trigger: 'blur'
          }],
          availableMagnifications: [{
            required: true,
            validator: this.isEmptyArray('List of available magnifications'),
            trigger: 'change'
          }]
        }
      }
    },

    methods: {
      removeMagnification (index) {
        if (!this.entity.availableMagnifications) {
          return
        }
        this.entity.availableMagnifications.splice(index, 1)

        this.$refs['mainForm'].validateField('availableMagnifications')
      },
      addMagnification () {
        if (!this.entity.availableMagnifications) {
          this.entity.availableMagnifications = []
        }
        this.entity.availableMagnifications.push({
          nominalMagnification: null,
          calibratedMagnification: null
        })

        this.$refs['mainForm'].validateField('availableMagnifications')
      }
    },

    created () {
      this.initOriginalEntity('entity')
    },

    mounted () {
      service.listEntities()
        .then(result => {
          this.microscopes = this.options(result.data, _.get(this, 'originalEntity.microscope'), 'id')
        })
        .catch(error => this.$log.error(error))
    }
  }
</script>

<style scoped>
  .small-margin {
    margin-bottom: 0.3em;
  }

  .small-margin .el-form-item.is-error {
    margin-bottom: 1.3em;
  }

  .small-margin.phase-plate-active .el-form-item.is-error {
    margin-bottom: 0;
  }

  #magnificationsForm .small-margin .el-form-item.is-error {
    margin-bottom: 4rem;
  }

  .no-margin {
    margin-bottom: 0;
  }
</style>
