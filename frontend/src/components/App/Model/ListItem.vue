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
  <div class="model-list-view-item list-view-item">
    <dashboard-list-item :entity="entity" entityType="MODEL">
      <template slot="left-pane">
        <el-row class="list-view__link-label-wrapper">
          <router-link :to="{ name: 'model-view', params: {id: entity.slug}}" class="list-view__link-label"
                       :id="'model-'  + index + '-label-link'">{{ entity.label }}
          </router-link>
        </el-row>
        <el-row class="list-view__entity-id">
          <el-row>
            <label class="list-view__label">Maps</label>
          </el-row>
          <el-row class="list-view__link-label-wrapper">
              <span v-for="(map, index) in entity.maps">
                <span v-if="index > 0">, </span>
                <router-link
                  class="list-view__link-sub-label"
                  :to="{ name: 'map-view', params: {projectId: projectId, id: map.slug }}">
                  {{ map | formatLabel }}</router-link>
              </span>
          </el-row>
        </el-row>
      </template>

      <el-row>
        <el-col :sm="8">
          <el-row>
            <label class="list-view__label">Resolution</label>
            <div class="list-view__value">{{ entity.resolution  | formatUnit('Å') }}</div>
          </el-row>
        </el-col>
      </el-row>
      <el-row>
        <el-col :sm="8">
          <el-row>
            <label class="list-view__label">&nbsp;</label>
            <div class="list-view__value">&nbsp;</div>
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
    name: 'model-list-view-item',

    props: {
      entity: Object,
      index: Number,
      projectId: String
    },

    components: {DashboardListItem},

    mixins: [
      FilterCommons
    ]
  }

</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .base-path {
    margin-bottom: -4rem; /* if path is big, extend to the next row */
  }
</style>
