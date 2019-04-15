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
  <div class="comment-list">
    <el-row class="list-view__separator" v-if="!_.isEmpty(comments)">
      <el-row class="content-wrapper comments-header">Comments</el-row>
    </el-row>

    <div v-for="comment in comments" :key="comment.id" class="comment-box comment-box-default">
      <CommentInfo :comment="comment"/>
      <CommentText :content="comment.content"/>
      <attachment-list :attachments="comment.attachments" :attachmentService="_commentService"></attachment-list>
    </div>
  </div>
</template>

<script>
  import CommentText from './CommentText'
  import CommentInfo from './CommentInfo'
  import commentsUtil from './Comments'
  import CommentService from '@/services/CommentService'
  import AttachmentList from '@/components/App/Attachment/AttachmentList'

  export default {
    name: 'CommentList',
    props: ['entityId', 'entityType'],
    components: {CommentText, CommentInfo, AttachmentList},
    data () {
      return {
        comments: []
      }
    },

    watch: {
      'entityId' (entityId) {
        this.loadComments(this.entityType, entityId)
      },
    },

    methods: {
      loadComments (entityType, entityId) {
        if (!entityType || !entityId) {
          return
        }

        this._commentService.listComments(entityType, entityId)
          .then(result => {
            this.comments = result.data
          })
          .catch(error => this.$log.error(error))
      },

      commentAdded (addedComment) {
        this.comments.push(addedComment)
      },

      commentUpdated (updatedComment) {
        this.comments = commentsUtil.replaceUpdatedComment(this.comments, updatedComment)
      },

      commentDeleted (commentId) {
        this.comments = this.comments.filter(c => c.id !== commentId)
      },

      mountEvents () {
        this.$events.on('commentAdded', this.commentAdded)
        this.$events.on('commentUpdated', this.commentUpdated)
        this.$events.on('commentDeleted', this.commentDeleted)
        this.$events.on('reloadComments', () => this.loadComments(this.entityType, this.entityId))

      },

      removeEvents () {
        this.$events.off('commentAdded', this.commentAdded)
        this.$events.off('commentUpdated', this.commentUpdated)
        this.$events.off('commentDeleted', this.commentDeleted)
        this.$events.off('reloadComments')
      }
    },

    created () {
      this._commentService = new CommentService()
    },

    mounted () {
      this.loadComments(this.entityType, this.entityId)
      this.mountEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .comment-container {
    background-color: $background-accent-color-comment-panel;
    box-shadow: 0 0 16px 0 rgba(0, 0, 0, 0.18);
  }

  .comment-container__button-box {
    text-align: center;
    padding-top: 1.2rem;
    padding-bottom: 1.2rem;
    background-color: $background-accent-color-comment-button-box;
    box-shadow: 0 0 16px 0 rgba(0, 0, 0, 0.18);
  }

  .no-comments {
    text-align: center;
    padding-top: 3rem;
    padding-bottom: 3rem;
  }

  .comment-box {
    padding: 1rem 1rem 1rem 1rem;
    margin: 0.5rem 0.5rem 0.5rem 0.5rem;
    border-radius: 0.3846rem;
  }

  .comment-box-owner {
    background-color: $background-accent-color-comment-owner-box;
  }

  .comment-box-default {
    background-color: $background-accent-color-comment-box;
  }
</style>


