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
  <div class="map-list-view-item list-view-item">
    <dashboard-list-item :entity="entity" entityType="MAP">
      <template slot="left-pane">
        <el-row class="list-view__link-label-wrapper">
          <router-link :to="{ name: 'map-view', params: {id: entity.slug}}" class="list-view__link-label"
                       :id="'map-'  + index + '-label-link'">{{ entity.label }}
          </router-link>
        </el-row>
      </template>

      <el-row class="list-view-item__row">
        <el-col :sm="8">
          <el-row>
            <label class="list-view__label">Processing sessions</label>
          </el-row>
          <el-row class="list-view__link-label-wrapper">
            <router-link
              class="list-view__link-sub-label"
              :to="{ name: 'processing_session-view', params: {projectId: projectId, id: entity.processingSession.slug }}"
            >{{ entity.processingSession | formatLabel }}
            </router-link>
          </el-row>
        </el-col>
        <el-col :sm="8">
          <el-row>
            <label class="list-view__label">Symmetry applied</label>
            <div class="list-view__value">{{ entity.symmetryApplied | formatValue }}</div>
          </el-row>
        </el-col>
        <el-col :sm="8">
          <el-row class="base-path">
            <label class="list-view__label">Number of images</label>
            <div class="list-view__value">{{ entity.numberOfImages | formatAsLocalizedNumber }}</div>
          </el-row>
        </el-col>
      </el-row>
      <el-row class="list-view-item__row">
        <el-col :sm="8">
          <el-row>
            <label class="list-view__label">Resolution in best parts</label>
            <div class="list-view__value">{{ entity.estimatedResolutionInBestParts | formatUnit('Å') }}</div>
          </el-row>
        </el-col>
        <el-col :sm="8">
          <el-row>
            <label class="list-view__label">Average resolution</label>
            <div class="list-view__value">{{ entity.estimatedResolutionInAverageParts | formatUnit('Å') }}</div>
          </el-row>
        </el-col>
        <el-col :sm="8">
          <el-row class="base-path">
            <label class="list-view__label">Resolution in worst parts</label>
            <div class="list-view__value">{{ entity.estimatedResolutionInWorstParts | formatUnit('Å') }}</div>
          </el-row>
        </el-col>
      </el-row>
    </dashboard-list-item>
  </div>
</template>

<script>
  import FilterCommons from '@/components/mixins/FilterCommons'
  import DashboardListItem from '@/components/App/DashboardListItem'

  export default {
    name: 'map-list-view-item',

    props: {
      entity: Object,
      index: Number,
      projectId: String
    },

    components: {DashboardListItem},

    mixins: [
      FilterCommons
    ],

  }

</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .base-path {
    margin-bottom: -4rem; /* if path is big, extend to the next row */
  }
</style>
