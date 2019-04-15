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
  <view-comment-base entityType="vitrification_protocol" :entity="entity" :icon="icon">
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Vitrification machine</label>
          <div class="view__metadata__value">{{ entity.vitrificationMachine | formatLabel }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Relative humidity</label>
          <div class="view__metadata__value">{{ entity.relativeHumidity | formatUnit('%') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Temperature</label>
          <div class="view__metadata__value">{{ entity.temperature | formatUnit('°C') }}</div>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Blot paper</label>
          <div class="view__metadata__value">{{ entity.blottingPaper | formatLabel }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Blot force</label>
          <div class="view__metadata__value">{{ entity.blotForce }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Number of blots</label>
          <div class="view__metadata__value">{{ entity.numberOfBlots }}</div>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Blot time</label>
          <div class="view__metadata__value">{{ entity.blotTime | round(1) | formatUnit('s') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Wait time</label>
          <div class="view__metadata__value">{{ entity.waitTime | round(1) | formatUnit('s') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Drain time</label>
          <div class="view__metadata__value">{{ entity.drainTime | round(1) | formatUnit('s') }}</div>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Number of sample applications</label>
          <div class="view__metadata__value">{{ entity.numberOfSampleApplications }}</div>
        </el-row>
      </el-col>
      <el-col :sm="16">
        <el-row>
          <div class="view__metadata__description-wrapper">
            <label class="view__metadata__label">Description</label>
            <div class="view__metadata__value">{{ entity.description | formatValue }}</div>
          </div>
        </el-row>
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Admin/VitrificationProtocol/Commons'
  import ViewHeader from '@/components/App/ViewHeader'
  import Icon from '@/assets/images/vitrification-protocol.svg'
  import ViewCommentBase from '@/components/App/ViewCommentBase'

  export default {
    name: 'vitrification-protocol-view',

    props: ['id'],

    mixins: [
      Commons
    ],

    components: {
      ViewHeader, ViewCommentBase
    },

    data () {
      return {
        icon: Icon
      }
    },

    watch: {
      'id' (id) {
        this.fetchEntityBySlugOrId(id)
      }
    },

    mounted () {
      this.$log.debug('props.id: ', this.id)
      this.fetchEntityBySlugOrId(this.id)
      this.$events.on('actionEdit', data => this.actionEdit('admin-protocols-vitrification', this.id))
      this.$events.on('actionCopy', data => this.actionCopy('admin-protocols-vitrification', this.id))
    },

    beforeDestroy () {
      this.$events.off('actionEdit')
      this.$events.off('actionCopy')
    }
  }
</script>

<style scoped>

</style>

