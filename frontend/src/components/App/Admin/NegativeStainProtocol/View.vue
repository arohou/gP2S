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
  <view-comment-base entityType="negative_stain_protocol" :entity="entity" :icon="icon">
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Name of stain</label>
          <div class="view__metadata__value">{{ entity.name }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">pH of stain</label>
          <div class="view__metadata__value">{{ entity.ph | round(1) }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Concentration of stain</label>
          <div class="view__metadata__value">{{ entity.concentration | round(2) | formatUnit('%') }}</div>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Temperature</label>
          <div class="view__metadata__value">{{ entity.temperature | round(2) | formatUnit('°C') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Incubation time of stain before blotting</label>
          <div class="view__metadata__value">{{ entity.incubationTime | formatUnit('s') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <div class="view__metadata__description-wrapper">
            <label class="view__metadata__label">Description</label>
            <div class="view__metadata__value">{{ entity.description }}</div>
          </div>
        </el-row>
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Admin/NegativeStainProtocol/Commons'
  import ViewHeader from '@/components/App/ViewHeader'
  import Icon from '@/assets/images/negative-stain-protocol.svg'
  import ViewCommentBase from '@/components/App/ViewCommentBase'

  export default {
    name: 'negative-stain-protocol-view',

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
      this.$events.on('actionEdit', data => this.actionEdit('admin-protocols-negative_stain', this.id))
      this.$events.on('actionCopy', data => this.actionCopy('admin-protocols-negative_stain', this.id))
    },

    beforeDestroy () {
      this.$events.off('actionEdit')
      this.$events.off('actionCopy')
    }
  }
</script>

<style scoped>

</style>

