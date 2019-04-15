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
  <view-comment-base entityType="sample_holder" :entity="entity" :icon="icon">
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Manufacturer</label>
          <div class="view__metadata__value">{{ entity.manufacturer }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Model</label>
          <div class="view__metadata__value">{{ entity.model }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Cryo-capable?</label>
          <div class="view__metadata__value">{{ entity.cryoCapable | formatBoolean }}</div>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Can be used with the following microscopes</label>
          <div class="view__metadata__value">
            {{ entity.microscopes | sortMicrsocopeLabels }}
          </div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Location</label>
          <div class="view__metadata__value">{{ entity.location }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Maximum tilt</label>
          <div class="view__metadata__value">{{ entity.maximumTilt | formatUnit('°', false) }}</div>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Double tilt?</label>
          <div class="view__metadata__value">{{ entity.doubleTilt | formatBoolean }}</div>
        </el-row>
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Admin/SampleHolder/Commons'
  import ViewHeader from '@/components/App/ViewHeader'
  import Icon from '@/assets/images/sample-holder.svg'
  import ViewCommentBase from '@/components/App/ViewCommentBase'

  export default {
    name: 'sample-holder-view',

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
      this.$events.on('actionEdit', data => this.actionEdit('admin-equipment-sample_holders', this.id))
      this.$events.on('actionCopy', data => this.actionCopy('admin-equipment-sample_holders', this.id))
    },

    beforeDestroy () {
      this.$events.off('actionEdit')
      this.$events.off('actionCopy')
    }
  }
</script>
