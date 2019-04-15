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
  <view-comment-base entityType="surface_treatment_protocol" :entity="entity" :icon="icon">
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Surface treatment machine</label>
          <div class="view__metadata__value">{{ entity.machine | formatLabel }}</div>
        </el-row>
      </el-col>
      <el-col :sm="6">
        <el-row>
          <label class="view__metadata__label">Pressure</label>
          <div class="view__metadata__value">{{ entity.pressure | formatUnit('mbar') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="5">
        <el-row>
          <label class="view__metadata__label">Duration</label>
          <div class="view__metadata__value">{{ entity.duration | formatUnit('s') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="5">
        <el-row>
          <label class="view__metadata__label">Polarity</label>
          <div class="view__metadata__value">{{ _.get(entity, 'polarity.name') | formatValue }}</div>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Current</label>
          <div class="view__metadata__value">{{ entity.current | formatUnit('mA') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="16">
        <el-row>
          <label class="view__metadata__label">Chemicals in atmosphere</label>
          <div class="view__metadata__value">{{ entity.chemicalsInAtmosphere | formatValue }}</div>
        </el-row>
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Admin/SurfaceTreatmentProtocol/Commons'
  import ViewHeader from '@/components/App/ViewHeader'
  import Icon from '@/assets/images/surface-treatment-protocol.svg'
  import ViewCommentBase from '@/components/App/ViewCommentBase'

  export default {
    name: 'surface-treatment-protocol-view',

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
      this.$events.on('actionEdit', data => this.actionEdit('admin-protocols-surface_treatment', this.id))
      this.$events.on('actionCopy', data => this.actionCopy('admin-protocols-surface_treatment', this.id))
    },

    beforeDestroy () {
      this.$events.off('actionEdit')
      this.$events.off('actionCopy')
    }
  }
</script>

<style scoped>

</style>

