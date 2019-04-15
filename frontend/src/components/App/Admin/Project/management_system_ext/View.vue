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
  <view-comment-base entityType="project" :entity="entity" :icon="icon">
    <el-row>
      <label class="view__metadata__label">{{ projectManagementSystemName + ' SB human readable ID' }}</label>
      <div class="view__metadata__value">
        <span v-if="_.isEmpty(projectManagementSystemUrl)">{{ entity.projectManagementSystemSlug | formatValue }}</span>
        <a v-else id="projectManagementSystemId" class="view__metadata__link" :href="projectManagementSystemUrl"
           target="_blank">{{ entity.projectManagementSystemSlug | formatValue }}</a>
      </div>
    </el-row>

    <el-row slot="additional-content">
      <el-row class="list-view__separator">
        <el-row class="content-wrapper">{{ projectManagementSystemName }}</el-row>
      </el-row>
      <el-col class="view__metadata__additional-content__col" :xs="15">
        <project-management-system-info :projectManagementSystemSlug="entity.projectManagementSystemSlug"
                                        list-type="flat">
        </project-management-system-info>
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import ProjectManagementSystemInfo from './ProjectManagementSystemInfo'
  import View from '@/components/App/Admin/Project/View'
  import { projectManagementSystemName } from '@/utils/ExternalSystemUtils'

  export default {
    mixins: [
      View
    ],

    components: {
      ProjectManagementSystemInfo
    },

    data () {
      return {
        projectManagementSystemUrl: null
      }
    },

    computed: {
      projectManagementSystemName: function () {
        return projectManagementSystemName()
      }
    },

    events: {
      projectInfoUrl(url) {
        this.projectManagementSystemUrl = url
      }
    }
  }
</script>
