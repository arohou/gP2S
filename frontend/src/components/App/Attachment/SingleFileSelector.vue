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
  <div class="SingleFileSelector">
    <gallery v-if="!uploadInProgress && !entity.attachmentMongoId"
             :uploader="uploader"
             :file-input-multiple="false"
             :dropzone-multiple="false">
    </gallery>

    <el-row type="flex" justify="start" v-else-if="uploadInProgress">
      <progress-bar
        :uploader="uploader" :hideBeforeStart="false"></progress-bar>
      <cancel-button
        :uploader="uploader"
        :id="uploadFileId"></cancel-button>
    </el-row>

    <div v-else>
      <a class="view__metadata__link"
         :href="downloadUrl">{{ entity.attachmentFileName }}</a>
      <el-button class="delete-button" @click="removeAttachment(entity.attachmentMongoId)">
        <img src="~@/assets/images/delete.svg"/>
      </el-button>
    </div>

  </div>
</template>

<script>
  import FineUploaderTraditional from 'fine-uploader-wrappers'
  import Gallery from 'vue-fineuploader/gallery'
  import ProgressBar from 'vue-fineuploader/progress-bar'
  import Thumbnail from 'vue-fineuploader/thumbnail'
  import CommonAlerts from '@/components/mixins/CommonAlerts'
  import CancelButton from 'vue-fineuploader/cancel-button'

  export default {
    name: 'SingleFileSelector',

    props: {
      entity: {
        type: Object,
        required: true
      },
      attachmentService: {
        type: Object,
        required: true
      },
      deleteConfirmationCallback: {
        type: Function
      }
    },

    components: {Gallery, ProgressBar, Thumbnail, CancelButton},

    mixins: [CommonAlerts],

    computed: {
      downloadUrl: function () {
        return this.attachmentService.getDownloadURL(this.entity.attachmentMongoId)
      }
    },

    data () {
      const uploader = this.createUploader()

      return {
        uploader: uploader,
        uploadInProgress: false,
        uploadFileId: null
      }
    },

    methods: {
      async removeAttachment (fileId) {
        if (this.deleteConfirmationCallback) {
          if (await this.deleteConfirmationCallback() === false) {
            return
          }
        }
        this.attachmentRemovingStart()

        await this.attachmentService.deleteAttachment(fileId)

        this.entity.attachmentFileName = null
        this.entity.attachmentMongoId = null

        this.attachmentRemovingFinished()
      },

      parseUploadResult (fileId, fileName, responseJSON) {
        const mongoId = this._.get(responseJSON, 'mongoId')
        this.$log.debug(`Uploaded [${fileId}] ${fileName}. MongoID: ${mongoId}`)

        if (!this.entity) {
          return
        }

        this.entity.attachmentFileName = fileName
        this.entity.attachmentMongoId = mongoId
        this.uploadInProgress = false
        this.uploadingFinished()
      },

      handleError (fileId, fileName, errorReason) {
        this.$log.debug(`Uploaded [${fileId}] ${fileName}. errorReason: ${errorReason}`)
        this.alertErrorOK(`Failed to upload file: '${fileName}'`)
        this.uploadInProgress = false
        this.uploadingFinished()
      },

      fileSubmitted (fileId, fileName) {
        this.$log.debug(`File submitted [${fileId}] ${fileName}`)
        this.uploadFileId = fileId
        this.uploadInProgress = true
        this.uploadingStarted()
      },

      uploadCanceled (fileId, fileName) {
        this.$log.debug(`Upload canceled [${fileId}] ${fileName}`)
        this.uploadFileId = null
        this.uploadInProgress = false
        this.uploadingFinished()
      },

      createUploader () {
        const uploader = new FineUploaderTraditional({
          options: {
            request: {
              //TODO make url general
              endpoint: this.attachmentService.getUploadURL()
            },
            multiple: false,
            retry: {
              enableAuto: false
            }
          }
        })
        uploader.on('complete', this.parseUploadResult)
        uploader.on('onError', this.handleError)
        uploader.on('onSubmitted', this.fileSubmitted)
        uploader.on('cancel', this.uploadCanceled)
        return uploader
      },

      uploadingStarted () {
        this.$events.$emit('formSavingStarted')
      },

      uploadingFinished () {
        this.$events.$emit('formSavingFinished')
      },

      attachmentRemovingStart () {
        this.$events.$emit('attachmentRemovingStart')
      },

      attachmentRemovingFinished () {
        this.$events.$emit('attachmentRemovingFinished')
      },
    }
  }
</script>

<style lang="scss" scoped>
  @import '~styles/globals/colors';

  /deep/ .vue-fine-uploader-total-progress-bar-container {
    background: #F2F2F2;
    border-radius: 0.1875rem;
    box-shadow: 0 0.125rem 0.3125rem rgba(0, 0, 0, 0.2) inset;
  }

  /deep/ .vue-fine-uploader-total-progress-bar-container {
    display: inline-block;
    height: 1.5625rem;
    margin-left: 0.625rem;
    margin-right: 0.625rem;
    margin-top: 0.25rem;
    width: 60%;

    .vue-fine-uploader-total-progress-bar {
      border-radius: 0.1875rem;
      background: $text-action-color;
      box-shadow: 0 0.125rem 0.3125rem rgba(0, 0, 0, 0.2) inset;
      height: inherit;
    }
  }
</style>
