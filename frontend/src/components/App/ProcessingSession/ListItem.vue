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
  <div class="processing-session-list-view-item list-view-item">
    <dashboard-list-item :entity="entity" entityType="PROCESSING_SESSION">
      <template slot="left-pane">
        <el-row class="list-view__link-label-wrapper">
          <router-link :to="{ name: 'processing_session-view', params: {id: entity.slug}}" class="list-view__link-label"
                       :id="'processing_session-'  + index + '-label-link'">{{ entity.label }}
          </router-link>
        </el-row>
        <el-row class="list-view__entity-id">
          <el-row>
            <label class="list-view__label">Microscopy sessions</label>
          </el-row>
          <el-row class="list-view__link-label-wrapper">
              <span v-for="(microscopySession, index) in entity.microscopySessions">
                <span v-if="index > 0">, </span>
                <router-link
                  class="list-view__link-sub-label"
                  :to="{ name: 'microscopy_session-view', params: {projectId: projectId, id: microscopySession.slug }}"
                >{{ microscopySession | formatLabel }}</router-link>
              </span>
          </el-row>
        </el-row>
      </template>
      <el-row>
        <el-col :sm="8">
          <el-row>
            <label class="list-view__label">Number of micrographs</label>
            <div class="list-view__value">{{ entity.numberOfMicrographs | formatAsLocalizedNumber }}</div>
          </el-row>
        </el-col>
        <el-col :sm="8">
          <el-row>
            <label class="list-view__label">Number of picked particles</label>
            <div class="list-view__value">{{ entity.numberOfPickedParticles | formatAsLocalizedNumber }}</div>
          </el-row>
        </el-col>
        <el-col :sm="8">
          <el-row class="base-path">
            <label class="list-view__label">Base path of processing directory</label>
            <div class="list-view__value">{{ entity.basePathOfProcessingDirectory | formatValue }}</div>
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
    name: 'processing-session-list-view-item',

    props: {
      entity: Object,
      index: Number,
      projectId: String
    },

    components: {DashboardListItem},

    mixins: [FilterCommons],

  }

</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .base-path {
    margin-bottom: -4rem; /* if path is big, extend to the next row */
  }
</style>
