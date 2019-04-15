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
  <view-comment-base entityType="grid" :entity="grid" :icon="icon" :title="grid.label" :slug="grid.slug">
    <div class="list-view__multiple-labels" slot="component-protocol-tag-wrapper">
      <div class="protocol-type-tag-cryo" v-if="grid.protocolType === ProtocolType.Cryo">Cryo</div>
      <div class="protocol-type-tag-stain" v-else-if="grid.protocolType === ProtocolType.Stain">Stain</div>
      <div class="component-tag-available" v-if="grid.isAvailable">Available</div>
    </div>

    <el-row class="view__metadata__row">
      <el-col :sm="8">
        <label class="view__metadata__label">Grid type</label>
        <div class="view__metadata__value">
          <router-link v-if="!!grid.gridType"
                       :to="{ name: 'admin-consumables-grid_type-view', params: {projectId: projectId, id: grid.gridType.slug }}"
                       class="view__metadata__link">{{ grid.gridType | formatLabel }}
          </router-link>
        </div>
      </el-col>
      <el-col :sm="8">
        <label class="view__metadata__label">Surface treatment protocol</label>
        <div class="view__metadata__value">
          <router-link v-if="!!grid.surfaceTreatmentProtocol"
                       :to="{ name: 'admin-protocols-surface_treatment-view', params: {projectId: projectId,
                             id:grid.surfaceTreatmentProtocol.slug }}"
                       class="view__metadata__link">{{ grid.surfaceTreatmentProtocol | formatLabel }}
          </router-link>
        </div>
      </el-col>
      <el-col v-if="grid.protocolType === ProtocolType.Cryo" :sm="8">
        <label class="view__metadata__label">Vitrification protocol</label>
        <div class="view__metadata__value">
          <router-link
            v-if="!!grid.vitrificationProtocol"
            :to="{ name: 'admin-protocols-vitrification-view', params: {projectId: projectId,
                  id:grid.vitrificationProtocol.slug}}"
            class="view__metadata__link">{{ grid.vitrificationProtocol | formatLabel }}
          </router-link>
        </div>
      </el-col>
      <el-col v-else-if="grid.protocolType === ProtocolType.Stain" :sm="8">
        <label class="view__metadata__label">Negative stain protocol</label>
        <div class="view__metadata__value">
          <router-link
            v-if="!!grid.negativeStainProtocol"
            :to="{ name: 'admin-protocols-negative_stain-view', params: {projectId: projectId,
                  id:grid.negativeStainProtocol.slug}}"
            class="view__metadata__link">{{ grid.negativeStainProtocol | formatLabel }}
          </router-link>
        </div>
      </el-col>
      <el-col v-else :sm="8">
        <label class="view__metadata__label">Preparation protocol</label>
      </el-col>
    </el-row>
    <el-row class="view__metadata__row">
      <el-col :sm="8">
        <label class="view__metadata__label">Incubation time</label>
        <div class="view__metadata__value">{{ grid.incubationTime | formatUnit('s') }}</div>
      </el-col>
      <el-col :sm="8">
        <label class="view__metadata__label">Volume applied</label>
        <div class="view__metadata__value">{{ grid.volume | formatUnit('μL') }}</div>
      </el-col>
      <el-col :sm="8">
        <concentration-view :concentration="grid.concentration"/>
      </el-col>
    </el-row>
    <el-row class="view__metadata__row">
      <el-col :sm="24">
        <label class="view__metadata__label">Sample added to grid</label>
        <div class="view__metadata__value">
          <router-link :to="{ name: 'sample-view', params: {id: _.get(grid, 'sample.slug') }}"
                       class="view__metadata__link"
                       id="grid__sample--link">{{ sampleLabel }}
          </router-link>
        </div>
      </el-col>
    </el-row>
    <el-row class="view__metadata__row" v-if="grid.protocolType === ProtocolType.Stain">
      <el-col :sm="8">
        <label class="view__metadata__label">Storage box label/number</label>
        <div class="view__metadata__value">{{ grid.storageBoxLabelNumber | formatValue }}</div>
      </el-col>
      <el-col :sm="8">
        <label class="view__metadata__label">Position within box</label>
        <div class="view__metadata__value">{{ grid.positionWithinBox | formatValue }}</div>
      </el-col>
    </el-row>
    <el-row class="view__metadata__row" v-if="grid.protocolType === ProtocolType.Cryo">
      <el-col :sm="8">
        <label class="view__metadata__label">Storage device</label>
        <div class="view__metadata__value">
          <router-link
            v-if="grid.cryoStorageDevice"
            :to="{ name: 'admin-equipment-cryo_storage_device-view', params: {projectId: projectId,
                  id:grid.cryoStorageDevice.slug}}"
            class="view__metadata__link">{{ grid.cryoStorageDevice | formatLabel }}
          </router-link>
          <div v-else class="view__metadata__value">{{ '—' }}</div>
        </div>
      </el-col>
      <el-col :sm="8" v-if="grid.cryoStorageDevice && grid.cryoStorageDevice.hasCylinders">
        <label class="view__metadata__label">Cylinder number/label</label>
        <div class="view__metadata__value">{{ grid.cylinderNumberLabel | formatValue }}</div>
      </el-col>
      <el-col :sm="8" v-if="grid.cryoStorageDevice && grid.cryoStorageDevice.hasTubes">
        <label class="view__metadata__label">Tube number/label</label>
        <div class="view__metadata__value">{{ grid.tubeNumberLabel | formatValue }}</div>
      </el-col>
      <el-col :sm="8" v-if="grid.cryoStorageDevice && grid.cryoStorageDevice.hasBoxes">
        <label class="view__metadata__label">Box number/label</label>
        <div class="view__metadata__value">{{ grid.boxNumberLabel | formatValue }}</div>
      </el-col>
    </el-row>

    <el-row slot="additional-content">
      <el-row class="related-entities-report">
        <related-entities-list :entity="grid" :projectId="projectId"/>
      </el-row>
    </el-row>
  </view-comment-base>
</template>

<script>
  import GridCommons from '@/components/App/Grid/GridCommons'
  import GridIcon from '@/assets/images/grid.svg'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import ConcentrationView from '@/components/App/ConcentrationView'
  import RelatedEntitiesList from '@/components/App/Grid/RelatedEntitiesList'

  export default {
    name: 'grid-view',

    props: ['id'],

    mixins: [
      GridCommons, ProjectResidentCommons
    ],

    components: {
      ViewCommentBase, ConcentrationView, RelatedEntitiesList
    },

    data () {
      return {
        icon: GridIcon
      }
    },

    methods: {
      componentLabel (component) {
        let result = component.aliquot.label
        if (result.finalDrop !== true) {
          result += ', ' + component.finalConcentration
          //Add proper concentration unit`
          result += ' μM'
        }

        return result
      },

      initEvents () {
        this.$events.on('actionEdit', data => this.actionEdit('grid', this.id))
        this.$events.on('actionCopy', data => this.actionCopy('grid', this.id))
      },

      removeEvents () {
        this.$events.off('actionEdit')
        this.$events.off('actionCopy')
      }
    },

    computed: {
      sampleLabel () {
        if (!this.grid.sample) {
          return ''
        }

        const components = [
          ...this.grid.sample.ligandComponent || [],
          ...this.grid.sample.proteinComponent || []
        ]

        return this.grid.sample.label + ' (' + components.map(this.componentLabel).join(', ') + ')'
      }
    },

    watch: {
      'id' (id) {
        this.fetchGrid(id)
      }
    },

    mounted () {
      this.fetchGrid(this.id)
      this.initEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .component-tag-available {
    margin-left: 0.662rem;
  }

  .view-header__content__box {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -webkit-align-items: center;
    -ms-flex-align: center;
    align-items: center;
    justify-content: center;
  }

</style>

