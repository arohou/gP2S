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
  <div class="entity-view">
    <div class="entity-view__content scrollable-container">
      <view-header :icon="icon"
                   :projects="projectsData.map(project => project.label)"
                   :id="entity.id"
                   :slug="entity.slug"
                   :createdDate="entity.createdDate"
                   :title="entity.label"
                   :disableCopy="disableCopy"
                   :createdBy="entity.createdBy">
        <slot name="header-title"></slot>
        <comment-indicator slot="comment" :entityType="entityType" :entityId="_.get(entity, 'id', null)"/>
        <div slot="component-protocol-tag">
          <slot name="component-protocol-tag-wrapper"></slot>
        </div>
      </view-header>
      <div class="view__content scrollable">
        <div class="view__metadata">
          <slot/>
        </div>
        <div class="view__comment-section">
          <el-row>
            <comment-list :entityType="entityType" :entityId="_.get(entity, 'id', null)"/>
          </el-row>
        </div>
        <div class="view__additional-content">
          <slot name="additional-content"/>
        </div>
      </div>
    </div>
    <div class="comment__box" v-if="showComments">
      <comment-container :entityType="entityType" :entityId="_.get(entity, 'id', null)"/>
    </div>

  </div>
</template>

<script>
  import ViewHeader from '@/components/App/ViewHeader'
  import CommentContainer from '@/components/App/Comment/CommentContainer'
  import CommentList from '@/components/App/Comment/CommentList'
  import CommentIndicator from '@/components/App/Comment/CommentIndicator'

  export default {
    name: 'BaseCommentView',

    props: {
      entityType: {
        type: String,
        required: true
      },

      entity: {
        type: Object
      },

      projectsData: {
        type: Array,
        default: () => []
      },

      icon: {
        type: String
      },

      disableCopy: {type: Boolean}
    },

    components: {
      ViewHeader, CommentContainer, CommentIndicator, CommentList
    },

    data () {
      return {
        showComments: false
      }
    },

    methods: {
      menuIndicatorListener () {
        this.showComments = false
      },

      openCommentDrawerListener () {
        this.showComments = !this.showComments
      },

      closeCommentDrawerListener () {
        this.showComments = false
      }
    },

    mounted () {
      this.$events.on('menuIndicator', this.menuIndicatorListener)
      this.$events.on('openCommentDrawer', this.openCommentDrawerListener)
      this.$events.on('closeCommentDrawer', this.closeCommentDrawerListener)
    },

    beforeDestroy () {
      this.$events.off('menuIndicator', this.menuIndicatorListener)
      this.$events.off('openCommentDrawer', this.openCommentDrawerListener)
      this.$events.off('closeCommentDrawer', this.closeCommentDrawerListener)
    }
  }
</script>

<style scoped>
  .entity-view {
    display: flex;
    height: 100%
  }

  .entity-view__content {
    flex: 1;
  }

  .view__comment-section {
    display: none;
  }
</style>
