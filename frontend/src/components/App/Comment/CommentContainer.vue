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
  <div class="comment-container">
    <comment-title/>
    <div v-if="_.isEmpty(comments)" class="no-comments">
      <span>There are no comments yet.</span>
    </div>
    <div v-else>
      <div v-for="comment in comments" :key="comment.id" class="comment-box"
           :class="[actionsVisible(comment) ? 'comment-box-owner' : 'comment-box-default' ]">
        <CommentInfo :comment="comment"/>
        <CommentText :content="comment.content"/>
        <attachment-list :attachments="comment.attachments" :attachmentService="_commentService"></attachment-list>
        <CommentActions :comment="comment" v-if="actionsVisible(comment)"/>
      </div>
    </div>

    <div class="comment-container__button-box">
      <el-button type="primary"
                 @click="$events.emit('showCommentNew', {createdBy: unixId, content: '', attachments: []})">
        Add new comment
      </el-button>
    </div>

    <comment-new savedEventName='commentAdded' showEventName='showCommentNew' :entityId="entityId"
                 :entityType="entityType"/>
    <comment-edit savedEventName='commentUpdated' showEventName='showCommentEdit'/>
  </div>
</template>

<script>
  import CommentNew from './CommentNew'
  import CommentEdit from './CommentEdit'
  import CommentText from './CommentText'
  import CommentInfo from './CommentInfo'
  import CommentTitle from './CommentTitle'
  import commentsUtil from './Comments'
  import CommentActions from './CommentActions'
  import CommentService from '@/services/CommentService'
  import AttachmentList from '@/components/App/Attachment/AttachmentList'
  import UserInfoUtil from '@/utils/UserInfoUtil'

  export default {
    name: 'CommentContainer',
    props: ['entityId', 'entityType'],
    components: {CommentNew, CommentEdit, CommentText, CommentInfo, CommentActions, CommentTitle, AttachmentList},
    data () {
      return {
        comments: [],
        unixId: null,
        _commentService: Object
      }
    },

    watch: {
      'entityId' (entityId) {
        this.loadComments(this.entityType, entityId)
      },
    },

    methods: {
      actionsVisible (comment) {
        if (!process.env.SECURITY_ENABLED) {
          return true
        }
        return this.unixId === comment.createdBy
      },

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
      this.unixId = UserInfoUtil.getUsername()
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


