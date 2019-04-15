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
  <div class="grid-list-view-item list-view-item">
    <dashboard-list-item :entity="entity" entityType="GRID">
      <template slot="left-pane">
        <div class="list-view__entity-title-column">
          <el-row class="list-view__link-label-wrapper">
            <router-link :to="{ name: 'grid-view', params: {id: entity.slug}}" class="list-view__link-label"
                         :id="'grid-'  + index + '-label-link'">{{ entity.label }}
            </router-link>
          </el-row>
          <el-row class="list-view__entity-protocol-type">
            <el-row class="list-view__multiple-labels">
              <div class="protocol-type-tag-cryo" v-if="protocolType === ProtocolType.Cryo">Cryo</div>
              <div class="protocol-type-tag-stain" v-else-if="protocolType === ProtocolType.Stain">Stain</div>
              <div class="component-tag-available" v-if="entity.isAvailable">Available</div>
            </el-row>
          </el-row>
        </div>
      </template>
      <el-col :sm="8">
        <el-row>
          <label class="list-view__label">Sample</label>
          <div class="list-view__value">
            <router-link :to="{ name: 'sample-view', params: {id: entity.sample.slug}}"
                         class="list-view__link-sub-label" :id="'sample-'  + index + '-label-link'">
              {{ entity.sample | formatLabel}}
            </router-link>
          </div>
        </el-row>
        <el-row>
          <label class="list-view__label">Protocol</label>
          <div class="list-view__value" v-if="entity.protocolType === ProtocolType.Cryo.name">{{
            entity.vitrificationProtocol | formatLabel }}
          </div>
          <div class="list-view__value" v-if="entity.protocolType === ProtocolType.Stain.name">{{
            entity.negativeStainProtocol | formatLabel }}
          </div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="list-view__label">Grid type</label>
          <div class="list-view__value">{{ entity.gridType | formatLabel }}</div>
        </el-row>
        <el-row>
          <label class="list-view__label">Surface treatment</label>
          <div class="list-view__value">{{ entity.surfaceTreatmentProtocol | formatLabel }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="list-view__label">Incubation time</label>
          <div class="list-view__value">{{ entity.incubationTime | formatUnit('s') }}</div>
        </el-row>
        <el-row>
          <concentration-view :concentration="entity.concentration" listItem/>
        </el-row>
      </el-col>
    </dashboard-list-item>
  </div>
</template>

<script>
  import GridCommons from '@/components/App/Grid/GridCommons'
  import ConcentrationView from '@/components/App/ConcentrationView'
  import DashboardListItem from '@/components/App/DashboardListItem'
  import ProtocolType from '@/components/App/Grid/ProtocolType'

  export default {
    name: 'grid-list-view-item',

    props: {
      entity: Object,
      index: Number
    },

    mixins: [GridCommons],

    components: {ConcentrationView, DashboardListItem},

    computed: {
      protocolType: function () {
        return ProtocolType.enumValueOf(this.entity.protocolType)
      }
    }
  }

</script>

<style lang="scss" scoped>

  .list-view__entity-id {
    &__using_row {
      margin-top: 0.662rem;
    }
  }

  .component-tag-available {
    margin-left: 0.662rem;
  }

  .list-view__entity-title-column {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
  }

  .list-view__entity-protocol-type {
    margin-top: 0.292rem;
  }

</style>

