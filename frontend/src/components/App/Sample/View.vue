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
  <view-comment-base entityType="sample" :entity="sample" :icon="icon">
    <div v-if="sample.availableForGridMaking" slot="component-protocol-tag-wrapper" class="component-tag-available">
      Available
    </div>
    <el-row class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">Incubation time</label>
        <div class="view__metadata__value" id="sample-incubationTime">{{ sample.incubationTime |
        formatUnit('min')
          }}
        </div>
      </el-col>

      <el-col :sm="5">
        <label class="view__metadata__label">Incubation temperature</label>
        <div class="view__metadata__value" id="sample-incubationTemperature">{{ sample.incubationTemperature |
        formatUnit('°C') }}
        </div>
      </el-col>
      <el-col :sm="13">
        <label class="view__metadata__label">Buffer</label>
        <div class="view__metadata__value" id="sample-buffer">{{ sample.otherBufferComponents | formatUnit
          }}
        </div>
      </el-col>

    </el-row>
    <el-row class="view__metadata__row">
      <el-col :sm="24">
        <div class="view__metadata__description-wrapper">
          <label class="view__metadata__label">Protocol description</label>
          <div class="view__metadata__value" id="sample-protocolDescription">{{ sample.protocolDescription |
          formatValue }}
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row slot="additional-content" class="sample-view__components">
      <el-row class="related-entities-report">
        <related-entities-list :entity="sample" :projectId="projectId"/>
      </el-row>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Sample/Commons'
  import SampleIcon from '@/assets/images/sample_icon.svg'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import CommentService from '@/services/CommentService'
  import RelatedEntitiesList from '@/components/App/Sample/RelatedEntitiesList'

  export default {
    name: 'sample-view',

    props: ['id'],

    mixins: [
      Commons, ProjectResidentCommons
    ],

    components: {
      ViewCommentBase, RelatedEntitiesList
    },

    data () {
      return {
        icon: SampleIcon
      }
    },

    methods: {
      actionAddProjectAssociation () {
        this.$log.debug('actionAddProjectAssociation!')
      },

      actionDeleteFromProject (index, row) {
        this.$log.debug(index, row)
      },

      initEvents () {
        this.$events.on('actionEdit', () => this.actionEdit('sample', this.id))
        this.$events.on('actionCopy', () => this.actionCopy('sample', this.id))
      },

      removeEvents () {
        this.$events.off('actionEdit')
        this.$events.off('actionCopy')
      }
    },

    watch: {
      'id' (id) {
        this.fetchSample(id)
      }
    },

    mounted () {
      this._commentService = new CommentService()
      this.fetchSample(this.id)
      this.initEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    }
  }
</script>

