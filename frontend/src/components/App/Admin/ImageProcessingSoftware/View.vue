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
  <view-comment-base entityType="image_processing_software" :entity="entity" :icon="icon">
    <el-row>
      <label class="list-view__label">Available version(s)</label>
      <div class="list-view__value">
                  <span v-for="(version, index) in entity.softwareVersions">
                    {{ version }}
                    <span v-if="index < entity.softwareVersions.length - 1">, </span>
                  </span>
      </div>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Admin/ImageProcessingSoftware/Commons'
  import ViewHeader from '@/components/App/ViewHeader'
  import Icon from '@/assets/images/comment-grey.svg'
  import ViewCommentBase from '@/components/App/ViewCommentBase'

  export default {
    name: 'image-processing-view',

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
      'id' (id) {
        this.fetchEntityBySlugOrId(id)
      }
    },

    mounted () {
      this.$log.debug('props.id: ', this.id)
      this.fetchEntityBySlugOrId(this.id)
      this.$events.on('actionEdit', data => this.actionEdit('admin-software-image_processing', this.id))
      this.$events.on('actionCopy', data => this.actionCopy('admin-software-image_processing', this.id))
    },

    beforeDestroy () {
      this.$events.off('actionEdit')
      this.$events.off('actionCopy')
    }
  }
</script>
