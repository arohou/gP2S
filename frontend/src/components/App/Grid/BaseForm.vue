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
  <div class="grid-base-form base-form">
    <el-form class="grid-base-form__form" ref="grid" labelPosition="top" :model="grid" :rules="rules">
      <!-- Grid Basic Properties -->
      <el-row :gutter="20">
        <el-col :xs="24" :sm="16">
          <el-form-item label="Grid label" prop="label" :error="getError('label')">
            <el-input class="grid-base-form__form__label-input" v-model="grid.label"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Protocol type" prop="protocolType" :error="getError('protocolType')">
            <el-radio-group id="protocol-type" v-model="grid.protocolType">
              <el-radio-button :label="ProtocolType.Cryo">{{ ProtocolType.Cryo.name }}</el-radio-button>
              <el-radio-button :label="ProtocolType.Stain">{{ ProtocolType.Stain.name }}</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="8">
          <el-form-item label="Grid type" prop="gridType"
                        :error="getError('gridType')">
            <el-select v-model="grid.gridType" filterable placeholder="Select grid used"
                       value-key="id">
              <el-option
                v-for="item in gridTypes"
                :key="item.id"
                :label="item.label"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Surface treatment protocol" prop="surfaceTreatmentProtocol"
                        :error="getError('surfaceTreatmentProtocol')">
            <el-select v-model="grid.surfaceTreatmentProtocol" filterable
                       placeholder="Select a surface treatment protocol"
                       value-key="id">
              <el-option
                v-for="item in surfaceTreatmentProtocols"
                :key="item.id"
                :label="item.label"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item v-if="grid.protocolType === ProtocolType.Cryo"
                        label="Vitrification protocol" prop="vitrificationProtocol"
                        :error="getError('vitrificationProtocol')">
            <el-select v-model="grid.vitrificationProtocol"
                       placeholder="Select a vitrification protocol"
                       value-key="id"
                       @change="vitrificationProtocolSelected">
              <el-option
                v-for="item in vitrificationProtocols"
                :key="item.id"
                :label="item.label"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item v-else-if="grid.protocolType === ProtocolType.Stain"
                        label="Negative stain protocol" prop="negativeStainProtocol"
                        :error="getError('negativeStainProtocol')">
            <el-select v-model="grid.negativeStainProtocol"
                       placeholder="Select a negative stain protocol"
                       value-key="id"
                       @change="negativeStainProtocolSelected">
              <el-option
                v-for="item in negativeStainProtocols"
                :key="item.id"
                :label="item.label"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- Grid Sample Properties -->
      <div class="form__section__separator"></div>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="16">
          <el-form-item label="Sample" prop="sample"
                        :error="getError('sample')">
            <el-select v-model="grid.sample" filterable
                       placeholder="Select a sample"
                       value-key="id">
              <el-option
                v-for="item in samples"
                :key="item.id"
                :label="item.label"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Sample is available for further use?" :error="getError('availableForGridMaking')">
            <el-radio-group v-if="grid.sample" id="available-for-grid-making"
                            v-model="grid.sample.availableForGridMaking">
              <el-radio-button :label="true">{{ 'Yes' }}</el-radio-button>
              <el-radio-button :label="false">{{ 'No' }}</el-radio-button>
            </el-radio-group>
            <!-- Added this to show disabled component, due to: https://github.com/vuejs/vue/issues/3732 (v-model with uninitialized property throwing error)-->
            <el-radio-group v-if="!grid.sample" :disabled="true">
              <el-radio-button :label="true">{{ 'Yes' }}</el-radio-button>
              <el-radio-button :label="false">{{ 'No' }}</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col>
          <concentration-component :concentration="grid.concentration"/>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="8">
          <el-form-item label="Incubation time (s)" prop="incubationTime" :error="getError('incubationTime')">
            <el-input id="incubation-time" v-model="grid.incubationTime"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Volume applied (μL)" prop="volume" :error="getError('volume')">
            <el-input id="volume" v-model="grid.volume"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- Grid Storage Properties -->
      <div class="form__section__separator"></div>
      <el-row :gutter="20" v-if="grid.protocolType === ProtocolType.Stain">
        <el-col :xs="24" :sm="8">
          <el-form-item label="Storage box label/number" prop="storageBoxLabelNumber"
                        :error="getError('storageBoxLabelNumber')">
            <el-input id="storage-box-label-number" v-model="grid.storageBoxLabelNumber"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Position within box" prop="positionWithinBox" :error="getError('positionWithinBox')">
            <el-input id="position-within-box" v-model="grid.positionWithinBox"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="grid.protocolType === ProtocolType.Cryo">
        <el-col :xs="24" :sm="8">
          <el-form-item label="Storage device" prop="cryoStorageDevice"
                        :error="getError('cryoStorageDevice')">
            <el-select v-model="grid.cryoStorageDevice" filterable
                       placeholder="Select a storage device"
                       value-key="id">
              <el-option
                v-for="item in cryoStorageDevices"
                :key="item.id"
                :label="item.label"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" v-if="grid.cryoStorageDevice && grid.cryoStorageDevice.hasCylinders">
          <el-form-item label="Cylinder number/label" prop="cylinderNumberLabel"
                        :error="getError('cylinderNumberLabel')">
            <el-input id="cylinder-number-label" v-model="grid.cylinderNumberLabel"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" v-if="grid.cryoStorageDevice && grid.cryoStorageDevice.hasTubes">
          <el-form-item label="Tube number/label" prop="tubeNumberLabel" :error="getError('tubeNumberLabel')">
            <el-input id="tube-number-label" v-model="grid.tubeNumberLabel"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" v-if="grid.cryoStorageDevice && grid.cryoStorageDevice.hasBoxes">
          <el-form-item label="Box number/label" prop="boxNumberLabel" :error="getError('boxNumberLabel')">
            <el-input id="box-number-label" v-model="grid.boxNumberLabel"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import ProtocolType from '@/components/App/Grid/ProtocolType'
  import GridTypeService from '@/services/GridTypeService'
  import SurfaceTreatmentProtocolService from '@/services/SurfaceTreatmentProtocolService'
  import VitrificationProtocolService from '@/services/VitrificationProtocolService'
  import NegativeStainProtocolService from '@/services/NegativeStainProtocolService'
  import SampleService from '@/services/SampleService'
  import CryoStorageDeviceService from '@/services/CryoStorageDeviceService'
  import FormCommons from '@/components/mixins/FormCommons'
  import SampleCommons from '@/components/mixins/SampleCommons'
  import ConcentrationComponent from '@/components/App/ConcentrationComponent'

  const gridTypeService = new GridTypeService()
  const surfaceTreatmentProtocolService = new SurfaceTreatmentProtocolService()
  const vitrificationProtocolService = new VitrificationProtocolService()
  const negativeStainProtocolService = new NegativeStainProtocolService()
  const sampleService = new SampleService()
  const cryoStorageDeviceService = new CryoStorageDeviceService()

  export default {
    name: 'grid-base-form',

    props: ['grid', 'defaultGridTypeSlugOrId', 'defaultSurfaceTreatmentProtocolSlugOrId',
      'defaultVitrificationProtocolSlugOrId', 'defaultNegativeStainProtocolSlugOrId',
      'defaultSampleSlugOrId', 'projectId'],

    mixins: [CommonValidations, FormCommons, SampleCommons],

    components: {ConcentrationComponent},

    data () {
      return {
        ProtocolType: ProtocolType,
        gridTypes: [],
        surfaceTreatmentProtocols: [],
        vitrificationProtocols: [],
        negativeStainProtocols: [],
        samples: [],
        cryoStorageDevices: [],

        rules: {
          label: [
            {
              required: true,
              message: 'Please provide a grid label',
              trigger: 'blur'
            }
          ],

          protocolType: [{
            required: true,
            validator: this.isDefined('Protocol type'),
            trigger: 'change'
          }],

          gridType: [{
            required: true,
            validator: this.isDefined('Grid type'),
            trigger: 'change'
          }],

          surfaceTreatmentProtocol: [{
            required: true,
            validator: this.isDefined('Surface treatment protocol'),
            trigger: 'change'
          }],

          vitrificationProtocol: [{
            required: true,
            validator: this.isDefined('Vitrification protocol'),
            trigger: 'change'
          }],

          negativeStainProtocol: [{
            required: true,
            validator: this.isDefined('Negative stain protocol'),
            trigger: 'change'
          }],

          sample: [{
            required: true,
            validator: this.isDefined('Sample'),
            trigger: 'change'
          }],

          incubationTime: [{
            required: false,
            validator: this.isGreaterThan('Incubation time', 0),
            trigger: 'blur'
          }],

          volume: [{
            required: true,
            validator: this.isGreaterThan('Volume applied', 0),
            trigger: 'blur'
          }],

          storageBoxLabelNumber: [{
            required: true,
            message: 'Please provide storage box label/number',
            trigger: 'blur'
          }],
          positionWithinBox: [{
            required: true,
            message: 'Please provide position within box',
            trigger: 'blur'
          }],
          cryoStorageDevice: [{
            required: true,
            validator: this.isDefined('Storage device'),
            trigger: 'change'
          }],
          cylinderNumberLabel: [{
            required: true,
            message: 'Please provide cylinder number/label',
            trigger: 'blur'
          }],
          tubeNumberLabel: [{
            required: true,
            message: 'Please provide tube number/label',
            trigger: 'blur'
          }],
          boxNumberLabel: [{
            required: true,
            message: 'Please provide box number/label',
            trigger: 'blur'
          }],
        }
      }
    },

    methods: {
      initDefaultGridType () {
        if (!!this.defaultGridTypeSlugOrId && !!this.gridTypes && this.gridTypes.length > 0) {
          let gridType = this.gridTypes.find((aGridType, index, array) => {
            return aGridType.id === parseInt(this.defaultGridTypeSlugOrId)
              || aGridType.slug === this.defaultGridTypeSlugOrId
          })
          if (gridType) {
            this.grid.gridType = gridType
          }
        }
      },

      initDefaultSurfaceTreatmentProtocol () {
        if (!!this.defaultSurfaceTreatmentProtocolSlugOrId && !!this.surfaceTreatmentProtocols
          && this.surfaceTreatmentProtocols.length > 0) {
          let surfaceTreatmentProtocol = this.surfaceTreatmentProtocols.find((aSurfaceTreatmentProtocol, index, array) => {
            return aSurfaceTreatmentProtocol.id === parseInt(this.defaultSurfaceTreatmentProtocolSlugOrId)
              || aSurfaceTreatmentProtocol.slug === this.defaultSurfaceTreatmentProtocolSlugOrId
          })
          if (surfaceTreatmentProtocol) {
            this.grid.surfaceTreatmentProtocol = surfaceTreatmentProtocol
          }
        }
      },

      initDefaultVitrificationProtocol () {
        if (this.grid.protocolType === ProtocolType.Cryo
          && !!this.defaultVitrificationProtocolSlugOrId && !!this.vitrificationProtocols
          && this.vitrificationProtocols.length > 0) {
          let vitrificationProtocol = this.vitrificationProtocols.find((aVitrificationProtocol, index, array) => {
            return aVitrificationProtocol.id === parseInt(this.defaultVitrificationProtocolSlugOrId)
              || aVitrificationProtocol.slug === this.defaultVitrificationProtocolSlugOrId
          })
          if (vitrificationProtocol) {
            this.grid.vitrificationProtocol = vitrificationProtocol
          }
        }
      },

      initDefaultNegativeStainProtocol () {
        if (this.grid.protocolType === ProtocolType.Stain &&
          !!this.defaultNegativeStainProtocolSlugOrId && !!this.negativeStainProtocols
          && this.negativeStainProtocols.length > 0) {
          let negativeStainProtocol = this.negativeStainProtocols.find((aNegativeStainProtocol, index, array) => {
            return aNegativeStainProtocol.id === parseInt(this.defaultNegativeStainProtocolSlugOrId)
              || aNegativeStainProtocol.slug === this.defaultNegativeStainProtocolSlugOrId
          })
          if (negativeStainProtocol) {
            this.grid.negativeStainProtocol = negativeStainProtocol
          }
        }
      },

      loadSamplesAvailableForGridMaking () {
        sampleService.findSamplesAvailableForGridMaking(this.projectId)
          .then(result => {
            // Original sample shouldn't be copied if it is not available for grid making.
            this.samples = this.isCopiedEntity() ? result.data
              : this.options(result.data, _.get(this, 'originalEntity.sample'), 'id')
          }).catch(error => this.$log.error(error))
      },

      vitrificationProtocolSelected (vitrificationProtocol) {
        this.grid.negativeStainProtocol = null
      },

      negativeStainProtocolSelected (negativeStain) {
        this.grid.vitrificationProtocol = null
      },

      loadDependencies () {
        gridTypeService.listEntities()
          .then(result => {
            this.gridTypes = this.options(result.data, _.get(this, 'originalEntity.gridType'), 'id')
            this.initDefaultGridType()
          }).catch(error => this.$log.error(error))
        surfaceTreatmentProtocolService.listEntities()
          .then(result => {
            this.surfaceTreatmentProtocols = this.options(result.data, _.get(this,
              'originalEntity.surfaceTreatmentProtocol'), 'id')
            this.initDefaultSurfaceTreatmentProtocol()
          }).catch(error => this.$log.error(error))
        vitrificationProtocolService.listEntities()
          .then(result => {
            this.vitrificationProtocols = this.options(result.data,
              _.get(this, 'originalEntity.vitrificationProtocol'), 'id')
            this.initDefaultVitrificationProtocol()
          }).catch(error => this.$log.error(error))
        negativeStainProtocolService.listEntities()
          .then(result => {
            this.negativeStainProtocols = this.options(result.data,
              _.get(this, 'originalEntity.negativeStainProtocol'), 'id')
            this.initDefaultNegativeStainProtocol()
          }).catch(error => this.$log.error(error))

        cryoStorageDeviceService.listEntities()
          .then(result => {
            this.cryoStorageDevices = this.options(result.data,
              _.get(this, 'originalEntity.cryoStorageDevice'), 'id')
          }).catch(error => this.$log.error(error))

        this.loadSamplesAvailableForGridMaking()
      },

      isCopiedEntity () {
        return this.$route.name === 'grid-copy'
      }
    },

    watch: {
      'projectId': function () {
        this.loadSamplesAvailableForGridMaking()
      },

      defaultGridTypeSlugOrId: function () {
        this.initDefaultGridType()
      },

      defaultSurfaceTreatmentProtocolSlugOrId: function () {
        this.initDefaultSurfaceTreatmentProtocol()
      },

      defaultVitrificationProtocolSlugOrId: function () {
        this.initDefaultVitrificationProtocol()
      },

      defaultNegativeStainProtocolSlugOrId: function () {
        this.initDefaultNegativeStainProtocol()
      },

      'originalEntity': function () {
        this.loadDependencies()
      }
    },

    created () {
      this.initOriginalEntity('grid')
    }
  }
</script>

<style lang="scss" scoped>

</style>

