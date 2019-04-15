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
  <el-row>
    <dashboard-list-item :entity="entity" entityType="MICROSCOPY_SESSION">
      <template slot="left-pane">
        <div class="microscopy-session-view__components__title_column">
        <el-row class="list-view__link-label-wrapper list-view__padding">
          <router-link :to="{ name: 'microscopy_session-view', params: {id: entity.slug}}"
                       class="list-view__link-label" :id="'entity-'  + index + '-label-link'">
            {{ entity.label }}
          </router-link>
        </el-row>
        <el-row class="list-view__protocol-type list-view__padding">
          <div class="protocol-type-tag-cryo" v-if="entity.grid.protocolType === ProtocolType.Cryo.name">Cryo</div>
          <div class="protocol-type-tag-stain" v-else-if="entity.grid.protocolType === ProtocolType.Stain.name">Stain
          </div>
        </el-row>
        </div>
      </template>
      <el-col :sm="8">
        <el-row>
          <label class="list-view__label">Grid</label>
          <div class="list-view__value">
            <router-link v-if="!!entity.grid"
                           class="list-view__link-sub-label"
                           :to="{ name: 'grid-view', params: { id: entity.grid.slug }}"
                           :id="'entity-'  + index + '-grid-link'">
                {{ _.get(entity, 'grid.label', null) | formatValue }}
            </router-link>
            <span v-if="!entity.grid">{{ _.get(entity, 'grid.label', null) | formatValue }}</span>
          </div>
        </el-row>
        <el-row>
          <label class="list-view__label">Microscope</label>
          <div class="list-view__value">{{ _.get(entity, 'microscope.label', null) | formatValue }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="list-view__label">Number of images</label>
          <div class="list-view__value">
            {{ entity.numberOfImagesAcquired !== null ? entity.numberOfImagesAcquired.toLocaleString() : '—'}}
          </div>
        </el-row>
        <el-row>
          <label class="list-view__label">Detector</label>
          <div class="list-view__value">{{ _.get(entity, 'electronDetector.label', null) | formatValue }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="list-view__label">Session start</label>
          <div class="list-view__value">{{ entity.sessionStart | moment('lll') }}</div>
        </el-row>
        <el-row>
          <label class="list-view__label">Session finish</label>
          <div class="list-view__value">{{ entity.sessionFinish | moment('lll') | formatValue }}</div>
        </el-row>
      </el-col>
    </dashboard-list-item>
  </el-row>
</template>

<script>
  import FilterCommons from '@/components/mixins/FilterCommons'
  import ProtocolType from '@/components/App/Grid/ProtocolType'
  import DashboardListItem from '@/components/App/DashboardListItem'

  export default {
    name: 'processing-session-list-view-item',

    props: {
      entity: Object,
      index: Number,
      projectId: String
    },

    components: {DashboardListItem},

    mixins: [
      FilterCommons
    ],

    data () {
      return {
        ProtocolType: ProtocolType
      }
    },

  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .microscopy-session-view__components {

    &__title_column {
      float: none;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      &__using_row {
        margin-bottom: 1.462rem;
      }
    }

    &__grid_link {
      color: $text-action-color;
    }

    .list-view__link-label-wrapper {
      float: none;
    }

  }

  .list-view__protocol-type {
    float: left;
  }

  .list-view__padding {
    padding-bottom: 0.45rem;
  }
</style>
