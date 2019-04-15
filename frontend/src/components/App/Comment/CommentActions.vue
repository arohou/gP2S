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
  <div class="comment__actions">
    <div class="comment__actions__button">
      <el-button id="comment-action-buttons__edit" @click="$events.$emit('showCommentEdit', comment)" type="text"
                 title="Edit" class="comment__actions__button__is-disabled">
        <img src="~@/assets/images/edit.svg"/>
      </el-button>
    </div>
    <br/>
    <div class="comment__actions__button">
      <el-button id="comment-action-buttons__delete" @click="showDeleteDialog(comment.id)" type="text"
                 title="Delete">
        <img src="~@/assets/images/delete.svg"/>
      </el-button>
    </div>
  </div>
</template>

<script>
  import DialogsService from '@/services/DialogsService'
  import CommentService from '@/services/CommentService'
  import EntityNotFoundError from '@/errors/EntityNotFoundError'

  export default {
    name: 'CommentActions',
    props: {
      comment: {
        type: Object,
        required: true
      }
    },
    methods: {
      showDeleteDialog (commentId) {
        DialogsService.showConfirmDialog('Are you sure you want to delete ?')
          .then(() => this.deleteComment(commentId))
          .catch(() => {/* This prevents console error when user choose cancel */})
      },

      async deleteComment (commentId) {
        return this._commentService.deleteComment(commentId)
          .then(() => this.$events.$emit('commentDeleted', commentId))
          .catch(error => this.handleDeletingError(error))
      },

      handleDeletingError (error) {
        if (error instanceof EntityNotFoundError) {
          this.$events.$emit('reloadComments')
          return
        }
        this.$log.error(error)
        DialogsService.showError('Unknown server error: ' + error.response.status)
      }
    },

    created () {
      this._commentService = new CommentService()
    }
  }
</script>

<style lang="scss" scoped>
  .comment__actions {
    display: flex;
    justify-content: flex-end;
  }

  .comment__actions__button {
    margin-left: .5rem;
    float: left;
  }

  button img {
    height: 1.231rem;
    width: 1.5rem;
  }

  .comment__actions__button__is-disabled, .comment__actions__button__is-disabled:hover {
    border: none !important;
    background-color: transparent;
  }
</style>
