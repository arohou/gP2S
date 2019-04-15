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
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Admin/Project/Commons'
  import ViewHeader from '@/components/App/ViewHeader'
  import Icon from '@/assets/images/admin.svg'
  import ViewCommentBase from '@/components/App/ViewCommentBase'

  export default {
    name: 'project-view',

    props: ['id'],

    mixins: [
      Commons
    ],

    components: {
      ViewHeader, ViewCommentBase
    },

    data () {
      return {
        icon: Icon
      }
    },

    watch: {
      async 'id' (id) {
        await this.fetchEntityBySlugOrId(id)
      }
    },

    async mounted () {
      this.$log.debug('props.id: ', this.id)
      await this.fetchEntityBySlugOrId(this.id)
      this.$events.on('actionEdit', () => this.actionEdit('admin-admin-project', this.id))
      this.$events.on('actionCopy', () => this.actionCopy('admin-admin-project', this.id))
    },

    beforeDestroy () {
      this.$events.off('actionEdit')
      this.$events.off('actionCopy')
    }
  }
</script>
