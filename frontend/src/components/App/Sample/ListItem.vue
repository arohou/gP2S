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
  <div class="processing-session-list-view-item list-view-item" v-if="entity">

    <dashboard-list-item :entity="entity" entityType="SAMPLE">
      <template slot="left-pane">
        <el-row class="list-view__link-label-wrapper">
          <router-link :to="{ name: 'sample-view', params: {id: entity.slug}}" class="list-view__link-label"
                       :id="'sample-'  + index + '-label-link'">{{ entity.label }}
          </router-link>
        </el-row>
        <el-row class="list-view__label list-view__entity-protocol-type">
          <div v-if="entity.availableForGridMaking" class="component-tag-available">Available</div>
        </el-row>
      </template>

      <el-col :sm="16">
        <el-row>
          <label class="list-view__label" :for="'sample-'  + index + '-components'">Components</label>
          <div class="list-view__value" :id="'sample-'  + index + '-components'">
            <span v-for="(component, idx) in components" :key="idx" v-if="!_.isEmpty(components)">
              <span v-if="idx != 0">, </span>

              <router-link
                class="list-view__link-sub-label"
                :to="{ name: getComponentLinkName(component),
                params: {projectId: projectId, id: component.aliquot.slug }}"
              >{{ component.aliquot.label }} ({{ component.finalConcentration | formatUnit('μM') }})</router-link>
            </span>
            <span v-if="_.isEmpty(components)">—</span>
          </div>
        </el-row>
        <el-row>
          <label class="list-view__label" :for="'sample-'  + index + '-other-buffer-components'">Buffer</label>
          <div class="list-view__value" :id="'sample-'  + index + '-other-buffer-components'">{{
            entity.otherBufferComponents | formatValue }}
          </div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="list-view__label" :for="'sample-'  + index + '-incubation-time'">Incubation time</label>
          <div class="list-view__value" :id="'sample-'  + index + '-incubation-time'">{{ entity.incubationTime |
          formatUnit('min') }}
          </div>
        </el-row>
        <el-row>
          <label class="list-view__label" :for="'sample-'  + index + '-incubation-temperature'">Incubation
            temperature</label>
          <div class="list-view__value" :id="'sample-'  + index + '-incubation-temperature'">{{
            entity.incubationTemperature | formatUnit('°C') }}
          </div>
        </el-row>
      </el-col>
    </dashboard-list-item>
  </div>
</template>

<script>
  import FilterCommons from '@/components/mixins/FilterCommons'
  import DashboardListItem from '@/components/App/DashboardListItem'
  import Commons from '@/components/App/Sample/Commons'
  import ComponentType from '@/components/App/Sample/ComponentType'

  export default {
    name: 'processing-session-list-view-item',

    props: {
      entity: Object,
      index: Number,
      projectId: String
    },

    components: {DashboardListItem},

    mixins: [FilterCommons, Commons],

    computed: {
      components: function () {
        return this.mapComponents(this.entity)
      }
    },

    methods: {
      getComponentLinkName: function (component) {
        return (component.type === ComponentType.PUR ? 'protein' : 'ligand') + '-view'
      }
    }
  }

</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .base-path {
    margin-bottom: -4rem; /* if path is big, extend to the next row */
  }
</style>



