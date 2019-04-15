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
  <div class="electron-detector-list-view list-view scrollable-container">
    <div class="list-view__header">
      <el-button class="list-view__action-buttons__submit"
                 @click="$router.push({ name: 'admin-equipment-electron_detectors-new' })">
        Create new electron detector
      </el-button>
    </div>

    <el-col class="electron-detector-view__components scrollable" :xs="15">
      <el-row v-for="(entity, index) in entities"
              :key="index">
        <dashboard-list-item :entity="entity" entityType="ELECTRON_DETECTOR">
          <template slot="left-pane">
            <el-row class="list-view__link-label-wrapper">
              <router-link :to="{ name: 'admin-equipment-electron_detectors-view', params: {id: entity.slug}}"
                           class="list-view__link-label" :id="'entity-'  + index + '-label-link'">{{ entity.label }}
              </router-link>
            </el-row>
          </template>

          <el-col :sm="24">
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Manufacturer</label>
                <div class="list-view__value">{{ entity.manufacturer }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Model</label>
                <div class="list-view__value">{{ entity.model }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Microscope</label>
                <div class="list-view__value">{{ entity.microscope | formatLabel }}</div>
              </el-row>
            </el-col>
          </el-col>
          <el-col :sm="24">
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Counts-per-electrons factor</label>
                <div class="list-view__value">{{ entity.countsPerElectronsFactor | formatValue }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Pixel linear dimension</label>
                <div class="list-view__value">{{ entity.pixelLinearDimensionUm | round(2) | formatUnit('μm') }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Counts-per-electrons</label>
                <div class="list-view__value">{{ entity.countsPerElectronsFactor | formatValue }}</div>
              </el-row>
            </el-col>
          </el-col>
        </dashboard-list-item>
      </el-row>
    </el-col>
  </div>
</template>

<script>
  import ElectronDetectorService from '@/services/ElectronDetectorService'
  import DashboardListItem from '@/components/App/DashboardListItem'
  import Commons from '@/components/App/Admin/ElectronDetector/Commons'

  const service = new ElectronDetectorService()

  export default {
    name: 'admin-equipment-electron_detectors',

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
