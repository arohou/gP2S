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
  <view-comment-base entityType="electron_detector" :entity="entity" :icon="icon">
    <el-row>
      <el-col :sm="16">
        <el-row>
          <el-col :sm="12">
            <el-row>
              <label class="view__metadata__label">Manufacturer</label>
              <div class="view__metadata__value">{{ entity.manufacturer }}</div>
            </el-row>
          </el-col>
          <el-col :sm="12">
            <el-row>
              <label class="view__metadata__label">Model</label>
              <div class="view__metadata__value">{{ entity.model }}</div>
            </el-row>
          </el-col>
        </el-row>
        <el-row>
          <el-row>
            <el-col :sm="12">
              <label class="view__metadata__label">Microscope</label>
              <div class="view__metadata__value">{{ entity.microscope | formatLabel }}</div>
            </el-col>
            <el-col :sm="12">
              <label class="view__metadata__label">Counts-per-electrons factor</label>
              <div class="view__metadata__value">{{ entity.countsPerElectronsFactor | formatValue }}</div>
            </el-col>
          </el-row>
        </el-row>
        <el-row>
          <el-row>
            <el-col :sm="12">
              <label class="view__metadata__label">Pixel linear dimension</label>
              <div class="view__metadata__value">{{ entity.pixelLinearDimensionUm | round(2) | formatUnit('μm') }}
              </div>
            </el-col>
            <el-col :sm="12">
              <label class="view__metadata__label">Number of pixel columns</label>
              <div class="view__metadata__value">{{ entity.numberOfPixelColumns | formatUnit }}</div>
            </el-col>
          </el-row>
        </el-row>
        <el-row>
          <el-row>
            <el-col :sm="12">
              <label class="view__metadata__label">Number of pixel rows</label>
              <div class="view__metadata__value">{{ entity.numberOfPixelRows | formatUnit }}</div>
            </el-col>
            <el-col :sm="12">
              <label class="view__metadata__label">Counting mode available?</label>
              <div class="view__metadata__value">{{ entity.countingModeAvailable | formatBoolean }}</div>
            </el-col>
          </el-row>
        </el-row>
        <el-row>
          <el-row>
            <el-col :sm="12">
              <label class="view__metadata__label">Dose fractionation available?</label>
              <div class="view__metadata__value">{{ entity.doseFractionationAvailable | formatBoolean }}</div>
            </el-col>
            <el-col :sm="12">
              <label class="view__metadata__label">Super resolution available?</label>
              <div class="view__metadata__value">{{ entity.superResolutionAvailable | formatBoolean }}</div>
            </el-col>
          </el-row>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Magnifications</label>
          <el-row class="small-margin">
            <el-col :sm="12">
              <el-col :sm="12" class="sub-label">Nominal</el-col>
              <el-col :sm="10" class="sub-label">Calibrated</el-col>
            </el-col>
          </el-row>
          <div id="view__metadata__magnification__list">
            <el-row v-for="(magnification, index) in entity.availableMagnifications"
                    :key="index"
                    class="view__metadata__value view__metadata__magnification__list-value">
              <el-col :sm="12">
                <el-col :sm="12">
                  {{ magnification.nominalMagnification | formatUnit('×') }}
                </el-col>
                <el-col :sm="10">
                  {{ magnification.calibratedMagnification | formatUnit('×') }}
                </el-col>
              </el-col>
            </el-row>
          </div>
        </el-row>
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Admin/ElectronDetector/Commons'
  import ViewHeader from '@/components/App/ViewHeader'
  import Icon from '@/assets/images/electron-detector.svg'
  import ViewCommentBase from '@/components/App/ViewCommentBase'

  export default {
    name: 'electron-detector-view',

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
      this.$events.on('actionEdit', data => this.actionEdit('admin-equipment-electron_detectors', this.id))
      this.$events.on('actionCopy', data => this.actionCopy('admin-equipment-electron_detectors', this.id))
    },

    beforeDestroy () {
      this.$events.off('actionEdit')
      this.$events.off('actionCopy')
    }
  }
</script>

<style scoped>
  .view__metadata__magnification__list-value {
    margin-bottom: 0;
  }

  .small-margin {
    margin-top: 0.7rem;
  }

  .view__metadata__magnification__list-value-default-label, .sub-label {
    color: #9b9b9b;
    font: 1.08rem "Helvetica Neue Light", "Lucida Grande", "Calibri", "Arial", sans-serif;
  }

  #view__metadata__magnification__list {
    margin-bottom: 1.923rem;
  }

  .view__metadata__value {
    margin-right: 0rem;
  }
</style>
