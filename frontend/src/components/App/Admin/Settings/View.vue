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
  <view-comment-base entityType="project" :entity="entity" :icon="icon"
                     disableCopy>
    <el-row>
      <label class="view__metadata__label">Pattern for data storage directory name</label>
      <div class="view__metadata__value">{{ entity.patternForDataStorageDirectoryName }}</div>
      <directory-substitution-tooltip
        :template-string="entity.patternForDataStorageDirectoryName"></directory-substitution-tooltip>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Admin/Settings/Commons'
  import Icon from '@/assets/images/gear_active.svg'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import DirectorySubstitutionTooltip from './DirectorySubstitutionTooltip'

  export default {
    name: 'settings-view',

    mixins: [
      Commons
    ],

    components: {
      DirectorySubstitutionTooltip,
      ViewCommentBase
    },

    data () {
      return {
        icon: Icon
      }
    },

    methods: {
      connectEvents: function () {
        this.$events.on('actionEdit', data => this.actionEdit('admin-admin-settings', this.id))
      },

      disconnectEvents: function () {
        this.$events.off('actionEdit')
      }
    },

    mounted () {
      this.fetchEntity()
      this.connectEvents()
    },

    beforeDestroy () {
      this.disconnectEvents()
    }
  }
</script>
