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
  <div v-if="entity">
    <el-row class="file-uploader">
      <Gallery :uploader="uploader" v-if="uploader && visible"></Gallery>
    </el-row>

    <attachment-list :attachments="allAttachments" :editMode="true"
                     :removeAttachment="removeAttachment"
                     :attachmentService="attachmentService"></attachment-list>
  </div>
</template>

<script>
  import FineUploaderTraditional from 'fine-uploader-wrappers'
  import Gallery from 'vue-fineuploader/gallery'
  import DialogsService from '@/services/DialogsService'
  import AttachmentList from '@/components/App/Attachment/AttachmentList'

  export default {
    name: 'file-uploader',
    components: {
      Gallery,
      AttachmentList
    },

    props: {
      entity: Object,
      submitCallback: Function,
      visible: false,
      entityType: String,
      attachmentService: Object
    },

    data () {
      return {
        uploader: null,
        allAttachments: [],
        attachmentsForRemoval: []
      }
    },

    methods: {
      createUploader (endpoint) {
        this.uploader = new FineUploaderTraditional({
          options: {
            request: {
              endpoint: endpoint
            },
            deleteFile: {
              enabled: false
            },
            retry: {
              enableAuto: false
            },
            autoUpload: false // Don't upload once file is selected.
          }
        })

        this.connectUploaderEvents(this.uploader)
      },

      connectUploaderEvents (uploader) {
        uploader.on('error', this.handleUploadErrors)
        uploader.on('complete', this.fileUploadSuccess)
        uploader.on('allComplete', this.executeSubmitCallback)
      },

      upload () {
        this.uploader.methods.uploadStoredFiles()
      },

      submit () {
        this.uploader.methods.setParams({entityId: this.entity.id, entityType: this.entityType})

        this.deleteAttachments()
        this.upload()
      },

      executeSubmitCallback () {
        this.submitCallback(Object.assign(Object.assign({}, this.entity), {attachments: this.allAttachments}))
      },

      deleteAttachments () {
        if (_.isEmpty(this.attachmentsForRemoval)) {
          return
        }

        this.attachmentService.deleteAttachments(this.attachmentsForRemoval)
      },

      fileUploadSuccess (index, fileName, response) {
        if (!response.success) {
          return
        }

        this.allAttachments = [
          {mongoId: response.mongoId, fileName: fileName, id: response.id},
          ...this.allAttachments
        ]
      },

      isNoFilesError (errorMessage) {
        return errorMessage === this.uploader.methods._options.messages.noFilesError
      },

      showErrorDialog (name, errorMessage) {
        this.$log.error(errorMessage)
        DialogsService.showError('Failed uploading ' + name + ': ' + errorMessage)
      },

      handleUploadErrors (id, name, errorMessage) {
        if (this.isNoFilesError(errorMessage)) {
          this.executeSubmitCallback()
          return
        }
        this.showErrorDialog(name, errorMessage)
      },

      removeAttachment (attachment) {
        this.attachmentsForRemoval.push(attachment.id)
        this.allAttachments = this.allAttachments.filter(el => el.id !== attachment.id)
      },

      resetUploader () {
        if (!this.uploader) {
          return
        }

        this.uploader.methods.cancelAll()
        this.uploader.methods.clearStoredFiles()
      },

      setAllAttachmentsFromEntity () {
        if (!this.entity) {
          return
        }

        this.allAttachments = [...this.entity.attachments]
      },

      resetComponent () {
        this.resetUploader()
        this.setAllAttachmentsFromEntity()

        this.attachmentsForRemoval = []
      },

      checkAttachmentsModified () {
        return !_.isEmpty(this.attachmentsForRemoval)
          || !_.isEmpty(this.uploader.methods.getUploads({status: ['submitted']}))
      }
    },

    watch: {
      'visible': {
        immediate: true,
        handler: function (visible) {
          if (visible) {
            this.resetComponent()
          }
        }
      },

      'attachmentService': {
        immediate: true,
        handler: function (service) {
          this.createUploader(service.getUploadURL())
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import '~styles/globals/colors';

  .file-uploader {
    /deep/ .vue-fine-uploader-gallery-nodrop-container, /deep/ .vue-fine-uploader-gallery-dropzone {
      margin-top: 1rem;
      min-height: 10rem;
    }

    /deep/ .vue-fine-uploader-file-progress-bar,
    /deep/ .vue-fine-uploader-total-progress-bar {
      border-radius: 0.1875rem;
    }

    /deep/ .vue-fine-uploader-file-progress-bar-container {
      background: #F2F2F2;
      border-radius: 0.1875rem;
      box-shadow: 0 0.125rem 0.3125rem rgba(0, 0, 0, 0.2) inset;
      position: absolute;
    }

    /deep/ .vue-fine-uploader-file-progress-bar-container {
      left: 50%;
      opacity: 0.9;
      top: 3.75rem;
      transform: translateX(-50%);
      width: 90%;
      z-index: 1;
      height: 1.1875rem;
    }

    /deep/ .vue-fine-uploader-file-progress-bar,
    /deep/ .vue-fine-uploader-total-progress-bar {
      background: $text-action-color;
      box-shadow: 0 0.125rem 0.3125rem rgba(0, 0, 0, 0.2) inset;
      height: inherit;
    }

  }

</style>
