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
  <el-dialog
    custom-class="reply-box-used-in-el-dialog-of-comment-form"
    :visible="visible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :showClose="false"
    size="tiny">

    <form-loading-overlay v-if="saveInProgress" overlayText="Saving files. Please wait..."></form-loading-overlay>

    <div slot="title" class="reply-box__title">
      <div class="reply-box__title__username">
        <user-full-name :userId='comment.createdBy'/>
      </div>
      <div class="reply-box__title__date-time">&nbsp;</div>
    </div>
    <el-input
      :maxlength="1000"
      type="textarea"
      :rows="9"
      placeholder="Type comment here ..."
      v-model="editedContent">
    </el-input>

    <attachment :entity="comment" :submitCallback="afterSuccessfulSubmission" ref="attachment"
                :visible="visible" entityType="comment" :attachmentService="commentService"></attachment>

    <div slot="footer" class="reply-box__buttons">
      <el-button class="reply-box__button reply-box__button-secondary" type="text"
                 @click="onCancel">Cancel
      </el-button>
      <el-button :disabled="saving" class="reply-box__button reply-box__button-primary" type="text"
                 @click="onSubmit">Save
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import Attachment from '@/components/App/Attachment/AttachmentForm'
  import UserFullName from '@/components/App/UserFullName'
  import FormLoadingOverlay from '@/components/App/FormLoadingOverlay'
  import CommentService from '@/services/CommentService'

  export default {
    props: {
      savedEventName: {
        type: String,
        required: true
      },
      showEventName: {
        type: String,
        required: true
      }
    },

    components: {
      UserFullName,
      Attachment,
      FormLoadingOverlay
    },

    data () {
      const commentService = new CommentService()

      return {
        editedContent: '',
        visible: false,
        saving: false,
        comment: {
          createdBy: null,
          attachments: []
        },
        saveInProgress: false,
        commentService: commentService
      }
    },

    methods: {
      onCancel () {
        this.visible = false
      },

      /**
       * Abstract method to be overridden for specific component
       * @param comment
       * @returns {*}
       */
      save (comment, attachmentsMofied) {
        throw new Error('Unimplemented method error!')
      },

      onSubmit () {
        if (_.isEmpty(_.trim(this.editedContent))) {
          return
        }
        this.startFormSaving()
        this.comment.content = this.editedContent

        this.save(this.comment, this.$refs['attachment'].checkAttachmentsModified())
          .then(response => {
            this.comment.id = response.data.id
            this.comment.modifiedDate = response.data.modifiedDate
            this.comment.createdDate = response.data.createdDate
            this.saveInProgress = true
            this.$refs['attachment'].submit()
          })
          .catch(this.handleFormSubmitError)
          .finally(this.stopFormSaving)
      },

      afterSuccessfulSubmission (updatedComment) {
        if (updatedComment) {
          this.$events.emit(this.savedEventName, updatedComment)
        }
        this.visible = false
        this.saveInProgress = false
      },

      handleFormSubmitError (error) {
        this.$log.error(error)
      },

      showCommentEdit (comment) {
        this.comment = comment
        this.editedContent = comment.content
        this.visible = true
      },

      startFormSaving () {
        this.saving = true
      },

      stopFormSaving () {
        this.saving = false
      }
    },

    mounted () {
      this.editedContent = this.comment.content
      this.$events.on(this.showEventName, this.showCommentEdit)
    },

    beforeDestroy () {
      this.$events.off(this.showEventName)
    }

  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .comment__reply-box {
    padding: 1rem 1rem 1rem 1rem;
    margin: 0.5rem 0.5rem 0.5rem 0.5rem;
    border-radius: 5px;
    background-color: $text-header-color--disabled;
  }

  .reply-box__title {
    display: flex;
    font-weight: bold;
    margin-right: 1.231rem;
    margin-left: 1.231rem;
    margin-top: 1.231rem;
  }

  .reply-box__button {
    font-size: 1.077rem;
    letter-spacing: -0.1px;
  }

  .reply-box__button-primary {
    color: $button-accent-text-color-primary;
    font-weight: bold;
  }

  .reply-box__button-secondary {
    color: $button-accent-text-color;
  }
</style>

<style lang="scss">
  .reply-box-used-in-el-dialog-of-comment-form {
    top: 5rem !important;
    border-radius: 5px;
    right: 1rem;
    left: unset;
    -ms-transform: translateX(0%);
    transform: translateX(0%);
  }

  .form-loading-overlay {
    margin-left: -1.55rem;
    margin-top: -6.2rem;
  }
</style>
