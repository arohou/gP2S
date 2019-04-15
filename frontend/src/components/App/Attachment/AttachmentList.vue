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
  <div class="attachment_list" v-if="!_.isEmpty(attachments)">
    <div class="separator"></div>

    <h2>Attachments</h2>
    <div v-for="attachment in attachments" :key="attachment.id" class="attachment-row">

      <div v-if="!editMode" class="img-wrapper">
        <img src="~@/assets/images/attachment.svg"/>
      </div>
      <el-button v-else class="delete-button" @click="removeAttachment(attachment)">
        <img src="~@/assets/images/delete.svg"/>
      </el-button>

      <div>
        <a :href="createDownloadURL(attachment.mongoId)" target="_blank">
          {{ attachment.fileName }}
        </a>
      </div>

      <div class="clear"/>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'AttachmentList',
    props: {
      attachments: Array,
      editMode: false,
      removeAttachment: Function,
      attachmentService: Object
    },

    methods: {
      createDownloadURL (fileId) {
        if (!this.attachmentService) {
          return null
        }

        return this.attachmentService.getDownloadURL(fileId)
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  a, h2 {
    font-size: 0.7692rem;
    letter-spacing: -0.1px;
    color: $button-accent-text-color;
    text-decoration: none;
  }

  h2 {
    font-weight: bold;
    margin-bottom: 0.4rem;
  }

  a:hover {
    text-decoration: underline;
  }

  .img-wrapper img {
    width: 1.2rem;
    height: 1.2rem;
  }

  .delete-button img {
    width: 1rem;
    height: 1rem;
  }

  .attachment-row {
    min-height: 1.4rem;
  }

  .separator {
    border: solid 0.07692rem #e5e5e5;
    margin-bottom: 0.5rem;
    margin-top: 1rem;
  }

  .delete-button, .img-wrapper {
    height: 1rem;
    width: 1rem;
    float: left;

    padding: 0;
    border: 0;
    margin-right: 0.25rem;
  }

  .delete-button:hover {
    background-color: transparent;
    box-shadow: none;
    border-radius: 0;
  }
</style>
