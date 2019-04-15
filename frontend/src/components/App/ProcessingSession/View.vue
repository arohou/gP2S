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
  <view-comment-base entityType="processing_session" :entity="entity" :icon="icon">
    <el-row class="view__metadata__row">
      <el-col :sm="8">
        <label class="view__metadata__label">Microscopy sessions</label>
        <div class="view__metadata__value">
          <span v-for="(microscopySession, index) in entity.microscopySessions">
            <span v-if="index > 0">, </span>
            <router-link
              class="view__metadata__link"
              :to="{ name: 'microscopy_session-view', params: {projectId: projectId, id: microscopySession.slug }}"
            >{{ microscopySession | formatLabel }}</router-link>
          </span>
        </div>
      </el-col>
      <el-col :sm="8">
        <label class="view__metadata__label">Number of micrographs</label>
        <div class="view__metadata__value">{{ entity.numberOfMicrographs | formatAsLocalizedNumber }}</div>
      </el-col>
      <el-col :sm="8">
        <label class="view__metadata__label">Number of picked particles</label>
        <div class="view__metadata__value">{{ entity.numberOfPickedParticles | formatAsLocalizedNumber }}</div>
      </el-col>
    </el-row>
    <el-row class="view__metadata__row">
      <el-col :sm="24">
        <label class="view__metadata__label">Base path of processing directory</label>
        <div class="view__metadata__value">{{ entity.basePathOfProcessingDirectory | formatValue }}</div>
      </el-col>
    </el-row>

    <el-row :sm="24" class="software-section">
      <label class="view__metadata__label">Software used</label>

      <div v-if="!_.isEmpty(entity.usedImageProcessingSoftware)">
        <el-row :sm="24" class="small-margin view__metadata__row">
          <el-col :sm="8" class="sub-label">Program</el-col>
          <el-col :sm="4" class="sub-label">Version</el-col>
          <el-col :sm="12" class="sub-label">Notes</el-col>
        </el-row>

        <el-row class="small-margin view__metadata__row" v-for="(software, index) in entity.usedImageProcessingSoftware"
                :key="index">
          <el-col :sm="8" class="sub-label">
            <router-link class="list-view__link-label"
                         :to="{ name: 'admin-software-image_processing-view',
                               params: {id: software.imageProcessingSoftware.slug} }"
                         :id="'image_processing-'  + index + '-label-link'">
              {{software.imageProcessingSoftware.label }}
            </router-link>
          </el-col>
          <el-col :sm="4" class="sub-value">{{ software.softwareVersion | formatValue }}</el-col>
          <el-col :sm="12" class="sub-value">{{ software.notes | formatValue }}</el-col>
        </el-row>
      </div>
      <el-row v-else class="small-margin">—</el-row>
    </el-row>

    <el-row slot="additional-content">
      <el-row class="related-entities-report">
        <related-entities-list :entity="entity" :projectId="projectId"/>
      </el-row>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/ProcessingSession/Commons'
  import Icon from '@/assets/images/processing_session.svg'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import RelatedEntitiesList from '@/components/App/ProcessingSession/RelatedEntitiesList'

  export default {
    name: 'processing-session-view',

    props: ['id'],

    mixins: [
      Commons, ProjectResidentCommons
    ],

    components: {
      ViewCommentBase, RelatedEntitiesList
    },

    data () {
      return {
        icon: Icon
      }
    },

    methods: {
      initEvents () {
        this.$events.on('actionEdit', data => this.actionEdit('processing_session', this.id))
        this.$events.on('actionCopy', data => this.actionCopy('processing_session', this.id))
      },
      removeEvents () {
        this.$events.off('actionEdit')
        this.$events.off('actionCopy')
      }
    },

    watch: {
      'id' (id) {
        this.fetchEntity(id)
      }
    },

    mounted () {
      this.fetchEntity(this.id)
      this.initEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .view-header__content__box {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -webkit-align-items: center;
    -ms-flex-align: center;
    align-items: center;
    justify-content: center;
  }

  .view-header__content__title {
    width: 100%;
    padding-right: 1rem;
  }

  .sub-label {
    color: $text-header-color--disabled;
  }

  .sub-value {
    font-weight: bold;
  }

  .software-section {
    margin-bottom: 1.538rem;
  }

  .small-margin {
    margin-top: 0.7rem;
  }

  .print {
    color: red;
  }
</style>

