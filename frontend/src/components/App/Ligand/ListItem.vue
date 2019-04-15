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
    <dashboard-list-item :entity="entity" entityType="LIGAND">
      <template slot="left-pane">
        <el-row class="list-view__link-label-wrapper">
          <router-link :to="{ name: 'ligand-view', params: {id: entity.slug}}" class="list-view__link-label"
                       :id="'ligand-'  + index + '-label-link'">{{ entity.label }}
          </router-link>
        </el-row>
        <el-row class="list-view__entity-protocol-type">
          <el-row class="list-view__component-type">
            <div class="component-tag-ligand">Ligand</div>
            <div v-if="entity.availableForSampleMaking" class="component-tag-available">Available</div>
          </el-row>
        </el-row>
      </template>

      <el-row class="list-view-item__row">
        <el-col :sm="8">
          <el-row class="list-view__label">{{registrationConfig.conceptIdLabel}}</el-row>
          <el-row class="list-view__value">{{ entity.conceptId | formatValue }}</el-row>
        </el-col>
        <el-col :sm="8">
          <el-row class="list-view__label">{{registrationConfig.batchIdLabelListItem}}</el-row>
          <el-row class="list-view__value">{{ entity.batchId | formatValue }}</el-row>
        </el-col>
        <el-col :sm="8">
          <label class="list-view__label" :for="'ligand-'  + index + '-concentration'">
            {{ concentrationLabel }}</label>
          <div class="list-view__value" :id="'ligand-'  + index + '-concentration'">
            {{ entity.concentration | formatUnit('μM') }}
          </div>
        </el-col>
      </el-row>
      <el-row class="list-view-item__row">
        <el-col :sm="8">
          <label class="list-view__label" :for="'ligand-'  + index + '-solvent'">Solvent</label>
          <div class="list-view__value" :id="'ligand-'  + index + '-solvent'">{{ entity.solvent | formatValue }}</div>
        </el-col>
      </el-row>
    </dashboard-list-item>
  </el-row>
</template>

<script>
  import FilterCommons from '@/components/mixins/FilterCommons'
  import DashboardListItem from '@/components/App/DashboardListItem'
  import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

  export default {
    name: 'ligand-item',

    props: {
      entity: Object,
      index: Number,
      concentrationLabel: {
        type: String,
        default: 'Unknown concentration label.'
      }
    },

    components: {DashboardListItem},

    mixins: [FilterCommons],

    data () {
      return {
        registrationConfig: ligandRegistrationConfig()
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

  .list-view__component-type {
    float: left;
  }

  .component-tag-available {
    margin-left: 0.662rem;
  }
</style>
