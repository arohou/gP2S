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
  <div class="surfaceTreatment-list-view list-view scrollable-container">
    <div class="list-view__header">
      <el-button class="list-view__action-buttons__submit"
                 @click="$router.push({ name: 'admin-protocols-surface_treatment-new' })">
        Create new protocol
      </el-button>
    </div>

    <el-col class="surfaceTreatment-view__components scrollable" :xs="15">
      <el-row v-for="(entity, index) in entities"
              :key="index">
        <dashboard-list-item :entity="entity" entityType="SURFACE_TREATMENT_PROTOCOL">
          <template slot="left-pane">
            <el-row class="list-view__link-label-wrapper">
              <router-link :to="{ name: 'admin-protocols-surface_treatment-view', params: {id: entity.slug}}"
                           class="list-view__link-label" :id="'surfaceTreatmentProtocol-'  + index + '-label-link'">{{
                entity.label }}
              </router-link>
            </el-row>
          </template>
          <el-row>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Surface treatment machine</label>
                <div class="list-view__value">{{ entity.machine | formatLabel }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Pressure</label>
                <div class="list-view__value">{{ entity.pressure | formatUnit('mbar') }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Duration</label>
                <div class="list-view__value">{{ entity.duration | formatUnit('s') }}</div>
              </el-row>
            </el-col>
          </el-row>
          <el-row>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Current</label>
                <div class="list-view__value">{{ entity.current | formatUnit('mA') }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Chemicals in atmosphere</label>
                <div class="list-view__value">{{ entity.chemicalsInAtmosphere | formatValue }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Polarity</label>
                <div class="list-view__value">{{ _.get(entity, 'polarity.name') | formatValue }}</div>
              </el-row>
            </el-col>
          </el-row>
        </dashboard-list-item>
      </el-row>
    </el-col>
  </div>
</template>

<script>
  import Service from '@/services/SurfaceTreatmentProtocolService'
  import Commons from '@/components/App/Admin/SurfaceTreatmentProtocol/Commons'
  import DashboardListItem from '@/components/App/DashboardListItem'

  const service = new Service()

  export default {
    name: 'admin-protocols-surface_treatment',

    data () {
      return {
        entities: []
      }
    },

    components: {DashboardListItem},

    mixins: [
      Commons
    ],

    methods: {
      loadEntities () {
        service.listEntities()
          .then(result => {
            this.entities = result.data
          })
          .catch(error => this.$log.error(error))
      }
    },

    mounted () {
      this.loadEntities()
    }
  }

</script>

