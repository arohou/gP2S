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
  <div class="sample-holder-list-view list-view scrollable-container">
    <div class="list-view__header">
      <el-button class="list-view__action-buttons__submit"
                 @click="$router.push({ name: 'admin-equipment-sample_holders-new' })">
        Create new holder
      </el-button>
    </div>

    <el-col class="sample-holder-view__components scrollable" :xs="15">
      <el-row v-for="(entity, index) in entities"
              :key="index">
        <dashboard-list-item :entity="entity" entityType="SAMPLE_HOLDER">
          <template slot="left-pane">
            <el-row class="list-view__link-label-wrapper">
              <router-link :to="{ name: 'admin-equipment-sample_holders-view', params: {id: entity.slug}}"
                           class="list-view__link-label" :id="'entity-'  + index + '-label-link'">{{ entity.label }}
              </router-link>
            </el-row>
          </template>
          <el-row>
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
                <label class="list-view__label">Cryo-capable?</label>
                <div class="list-view__value">{{ entity.cryoCapable | formatBoolean }}</div>
              </el-row>
            </el-col>
          </el-row>
          <el-row>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Can be used with the following microscopes</label>
                <div class="list-view__value">{{ entity.microscopes | sortMicrsocopeLabels }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Location</label>
                <div class="list-view__value">{{ entity.location }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Maximum tilt</label>
                <div class="list-view__value">{{ entity.maximumTilt | formatUnit('°', false) }}</div>
              </el-row>
            </el-col>
          </el-row>
        </dashboard-list-item>
      </el-row>
    </el-col>
  </div>
</template>

<script>
  import Service from '@/services/SampleHolderService'
  import Commons from '@/components/App/Admin/SampleHolder/Commons'
  import DashboardListItem from '@/components/App/DashboardListItem'

  const service = new Service()

  export default {
    name: 'admin-equipment-sample_holders',

    data () {
      return {
        entities: []
      }
    },

    components: {DashboardListItem},

    mixins: [
      Commons
    ],

    watch: {
      projectId: function () {
        this.loadProtocols()
      }
    },

    methods: {
      loadProtocols () {
        service.listEntities()
          .then(result => {
            this.entities = result.data
          })
          .catch(error => this.$log.error(error))
      }
    },

    mounted () {
      this.loadProtocols()
    }
  }
</script>

