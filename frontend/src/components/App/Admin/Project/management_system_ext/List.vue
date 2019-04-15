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
  <div class="project-list-view list-view scrollable-container">
    <div class="list-view__header">
      <el-button class="list-view__action-buttons__submit" @click="$router.push({ name: 'admin-admin-project-new' })">
        Create new project
      </el-button>
    </div>

    <el-col class="list-view__components scrollable" :xs="15">
      <el-row v-for="(entity, index) in entities"
              :key="index">
        <dashboard-list-item :entity="entity" entityType="PROJECT">
          <template slot="left-pane">
            <el-row class="list-view__link-label-wrapper">
              <router-link :to="{ name: 'admin-admin-project-view', params: {id: entity.slug}}"
                           class="list-view__link-label" :id="'project-'  + index + '-label-link'">{{ entity.label }}
              </router-link>
            </el-row>
          </template>
          <el-row>
            <label class="list-view__label">{{ projectManagementSystemName + ' SB human readable ID' }}</label>
            <div class="list-view__value">{{ entity.projectManagementSystemSlug | formatValue }}</div>
          </el-row>
        </dashboard-list-item>
      </el-row>
    </el-col>
  </div>
</template>

<script>
  import Commons from '@/components/App/Admin/Project/Commons'
  import DashboardListItem from '@/components/App/DashboardListItem'
  import { projectManagementSystemName } from '@/utils/ExternalSystemUtils'

  export default {
    name: 'project-list-view',

    data () {
      return {
        entities: []
      }
    },

    components: {DashboardListItem},

    mixins: [
      Commons
    ],

    computed: {
      projectManagementSystemName: function () {
        return projectManagementSystemName()
      }
    },

    methods: {
      loadEntities () {
        this._projectService.listEntities()
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
