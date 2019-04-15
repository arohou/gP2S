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
  <div class="negative-stain-list-view list-view scrollable-container">
    <div class="list-view__header">
      <el-button class="list-view__action-buttons__submit"
                 @click="$router.push({ name: 'admin-protocols-negative_stain-new' })">
        Create new protocol
      </el-button>
    </div>

    <el-col class="negativeStain-view__components scrollable" :xs="15">
      <el-row v-for="(entity, index) in entities"
              :key="index">
        <dashboard-list-item :entity="entity" entityType="NEGATIVE_STAIN_PROTOCOL">
          <template slot="left-pane">
            <el-row class="list-view__link-label-wrapper">
              <router-link :to="{ name: 'admin-protocols-negative_stain-view', params: {id: entity.slug}}"
                           class="list-view__link-label" :id="'entity-'  + index + '-label-link'">{{ entity.label }}
              </router-link>
            </el-row>
          </template>

          <el-row>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Name of stain</label>
                <div class="list-view__value">{{ entity.name }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">pH of stain</label>
                <div class="list-view__value">{{ entity.ph | round(1) }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Concentration of stain</label>
                <div class="list-view__value">{{ entity.concentration | round(2) | formatUnit('%') }}</div>
              </el-row>
            </el-col>
          </el-row>
          <el-row>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Temperature</label>
                <div class="list-view__value">{{ entity.temperature | round(2) | formatUnit('°C') }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <label class="list-view__label">Incubation time of stain before blotting</label>
                <div class="list-view__value">{{ entity.incubationTime | formatUnit('s') }}</div>
              </el-row>
            </el-col>
            <el-col :sm="8">
              <el-row>
                <div class="view__metadata__description-wrapper">
                  <label class="list-view__label">Description</label>
                  <div class="list-view__value">{{ entity.description }}</div>
                </div>
              </el-row>
            </el-col>
          </el-row>
        </dashboard-list-item>
      </el-row>
    </el-col>
  </div>
</template>

<script>
  import Service from '@/services/NegativeStainProtocolService'
  import Commons from '@/components/App/Admin/NegativeStainProtocol/Commons'
  import DashboardListItem from '@/components/App/DashboardListItem'

  const service = new Service()

  export default {
    name: 'admin-protocols-negative_stain',

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

