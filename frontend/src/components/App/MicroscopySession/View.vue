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
  <view-comment-base entityType="microscopy_session" :entity="entity" :icon="icon" :title="entity.label" :slug="entity.slug">
    <div slot="component-protocol-tag-wrapper">
      <div class="protocol-type-tag-cryo"
           v-if="entity.grid && entity.grid.protocolType === ProtocolType.Cryo.name">Cryo
      </div>
      <div class="protocol-type-tag-stain"
           v-else-if="entity.grid && entity.grid.protocolType === ProtocolType.Stain.name">Stain
      </div>
    </div>
    <h2 class="view__metadata__section-header">Basic Information</h2>
    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">Microscope</label>
        <div class="view__metadata__value" id="microscopy-session__microscope">
          {{ _.get(entity, 'microscope.label', null) | formatValue }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Started</label>
        <div class="view__metadata__value" id="microscopy-session__session-start">
          {{ entity.sessionStart | moment('lll') | formatValue }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Finished</label>
        <div class="view__metadata__value" id="microscopy-session__session-finish">
          {{ entity.sessionFinish | moment('lll') | formatValue }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Number of images</label>
        <div class="view__metadata__value" id="microscopy-session__number-of-images-acquired"
        >{{ entity.numberOfImagesAcquired | formatAsLocalizedNumber }}
        </div>
      </el-col>
    </el-row>
    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">Sample holder</label>
        <div class="view__metadata__value">{{ entity.sampleHolder ? entity.sampleHolder.label : '—' }}</div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Detector</label>
        <div class="view__metadata__value" id="microscopy-session__electron-detector">
          {{ _.get(entity, 'electronDetector.label', null) | formatValue }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Grid</label>
        <div v-if="entity.grid"
             class="view__metadata__value">
          <router-link
            class="view__metadata__link"
            id="microscopy-session__grid--link"
            :to="{ name: 'grid-view', params: {projectId: projectId, id: entity.grid.slug }}">
            {{ _.get(entity, 'grid.label', null) | formatValue }}<br>({{gridReturnedToStorageLabel}})
          </router-link>
        </div>
        <div class="view__metadata__value" id="microscopy-session__grid--empty" v-else>—</div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Sample</label>
        <div class="view__metadata__value">
          <router-link
            class="view__metadata__link"
            v-if="_.get(entity, 'grid.sample')"
            id="microscopy-session__sample--link"
            :to="{ name: 'sample-view', params: {projectId: projectId, id: entity.grid.sample.slug }}">
            {{ _.get(entity, 'grid.sample.label', null) | formatValue }}
          </router-link>
          <div class="view__metadata__value" id="microscopy-session__sample-empty" v-else>—</div>
        </div>
      </el-col>
    </el-row>
    <el-row :sm="24" class="view__metadata__row">
      <el-col>
        <label class="view__metadata__label">Data storage directory name</label>
        <div class="view__metadata__value"
             id="microscopy-session__data_storage_directory_name">
          {{ _.get(entity, 'dataStorageDirectoryName') | formatValue }}
        </div>
      </el-col>
    </el-row>

    <h2 class="view__metadata__section-header view__metadata__section-line">Microscope settings</h2>
    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">Extraction voltage</label>
        <div class="view__metadata__value" id="microscopy-session__extraction-voltage">
          {{ entity.extractionVoltageKV | formatUnit('kV') }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Acceleration voltage</label>
        <div class="view__metadata__value" id="microscopy-session__acceleration-voltage">
          {{ entity.accelerationVoltageKV | formatUnit('kV') }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Gun lens setting</label>
        <div class="view__metadata__value" id="microscopy-session__gun-lens-setting">
          {{ entity.gunLensSetting | formatValue }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">C2 aperture diameter</label>
        <div class="view__metadata__value" id="microscopy-session__c2-perture-diameter">
          {{ entity.condenser2ApertureDiameter | formatUnit('μm') }}
        </div>
      </el-col>
    </el-row>
    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">Objective aperture</label>
        <div class="view__metadata__value" id="microscopy-session__objective-aperture">
          <span v-if="entity.objectiveAperture && entity.objectiveAperture.phasePlate">Phase plate</span>
          <span v-if="entity.objectiveAperture && !entity.objectiveAperture.phasePlate">
                {{ entity.objectiveAperture.diameter | formatUnit('μm') }}
              </span>
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Energy filter?</label>
        <div class="view__metadata__value" id="microscopy-session__energy-filter">
          {{ entity.energyFilter | formatBoolean }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Energy filter slit width</label>
        <div class="view__metadata__value" id="microscopy-session__energy-filter-slit-width">
          {{ entity.energyFilterSlitWidth | formatUnit('eV') }}
        </div>
      </el-col>
      <el-col :sm="6">
      </el-col>
    </el-row>

    <h2 class="view__metadata__section-header view__metadata__section-line">Exposure settings</h2>
    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">Magnification (image pixel size)</label>
        <div class="view__metadata__value" id="microscopy-session__nominal-magnification">
          {{ entity.nominalMagnification ? entity.nominalMagnification + ' × (' + parseFloat((entity.pixelSize ||
          0).toFixed(3)) + ' Å/pixel)' : '—'}}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Nanoprobe</label>
        <div class="view__metadata__value" id="microscopy-session__nanoprobe">
          <div class="view__metadata__value">{{ entity.nanoprobe | formatBoolean }}</div>
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Spot size</label>
        <div class="view__metadata__value" id="microscopy-session__spot-size">
          {{ entity.spotSize | formatValue }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Diameter of illuminated area</label>
        <div class="view__metadata__value" id="microscopy-session__diameter">
          {{ entity.diameterOfIlluminatedArea | formatUnit('μm') }}
        </div>
      </el-col>
    </el-row>
    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">Use counting mode</label>
        <div class="view__metadata__value" id="microscopy-session__counting-mode">
          {{ entity.countingMode | formatBoolean }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Exposure rate</label>
        <div class="view__metadata__value" id="microscopy-session__exposure-rate">
          <div class="view__metadata__value">
            {{ entity.exposureRate | formatUnit((entity.countingMode ? 'electrons' : 'counts') + '/pixel/s') }}
          </div>
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Exposure duration</label>
        <div class="view__metadata__value" id="microscopy-session__exposure-duration">
          {{ entity.exposureDuration | formatUnit('s') }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Dose fractionation</label>
        <div class="view__metadata__value" id="microscopy-session__dose-fractionation">
          {{ entity.doseFractionation | formatBoolean }}
        </div>
      </el-col>
    </el-row>

    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">Number of frames</label>
        <div class="view__metadata__value" id="microscopy-session__number-of-frames">
          {{ entity.numberOfFrames | formatValue }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Super resolution</label>
        <div class="view__metadata__value" id="microscopy-session__super-resolution">
          {{ entity.superResolution | formatBoolean }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Supersampling factor</label>
        <div class="view__metadata__value" id="microscopy-session__supersampling-factor">
          {{ entity.supersamplingFactor | formatValue }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Pixel binning</label>
        <div class="view__metadata__value" id="microscopy-session__pixel-binning">
          {{ entity.pixelBinning | formatValue }}
        </div>
      </el-col>
    </el-row>

    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__computed-data__label">Image pixel size</label>
        <div class="view__metadata__value" id="microscopy-session__image-pixel-size">
          {{ calculateImagePixelSize(entity) | round(3) | formatUnit('Å') }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__computed-data__label">
          {{ entity.countingMode ? 'Exposure rate' : 'Estimated exposure rate' }}
        </label>
        <div class="view__metadata__value" id="microscopy-session__exposure-rate-eA2s">
          {{ calculateExposureRate(entity) | round(3) | formatUnit('electrons/Å&sup2;/s') }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__computed-data__label">
          {{ entity.countingMode ? 'Total exposure' : 'Estimated total exposure' }}
        </label>
        <div class="view__metadata__value" id="microscopy-session__total-exposure">
          {{ calculateTotalExposure(entity) | round(3) | formatUnit('electrons/Å&sup2;') }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__computed-data__label">Frame duration</label>
        <div class="view__metadata__value" id="microscopy-session__frame-duration">
          {{ calculateFrameDuration(entity) | round(3) | formatUnit('s') }}
        </div>
      </el-col>
    </el-row>
    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__computed-data__label">
          {{ entity.countingMode ? 'Exposure' : 'Estimated exposure' }} per frame
        </label>
        <div class="view__metadata__value" id="microscopy-session__exposure-per-frame">
          {{ calculateExposurePerFrame(entity) | round(3) | formatUnit('electrons/Å&sup2;') }}
        </div>
      </el-col>
    </el-row>

    <h2 class="view__metadata__section-header view__metadata__section-line">Microscope control</h2>
    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">Target underfocus min</label>
        <div class="view__metadata__value" id="microscopy-session__target-underfocus-min">
          {{ entity.targetUnderfocusMin | formatUnit('μm') }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Target underfocus max</label>
        <div class="view__metadata__value" id="microscopy-session__target-underfocus-max">
          {{ entity.targetUnderfocusMax | formatUnit('μm') }}
        </div>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Number of exposures per hole</label>
        <div class="view__metadata__value" id="microscopy-session__exposures-per-hole">
          {{ entity.exposuresPerHole | formatValue }}
        </div>
      </el-col>
    </el-row>

    <el-row slot="additional-content">
      <el-row class="related-entities-report">
        <related-entities-list :entity="entity" :projectId="projectId" currentView="model"/>
      </el-row>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/MicroscopySession/Commons'
  import Icon from '@/assets/images/microscopy-session.svg'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import Utils from '@/components/App/MicroscopySession/Utils'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import RelatedEntitiesList from '@/components/App/MicroscopySession/RelatedEntitiesList'

  export default {
    name: 'microscopy-session-view',

    props: ['id'],

    mixins: [
      Commons, ProjectResidentCommons, Utils
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
        this.$events.on('actionEdit', data => this.actionEdit('microscopy_session', this.id))
        this.$events.on('actionCopy', data => this.actionCopy('microscopy_session', this.id))
      },
      removeEvents () {
        this.$events.off('actionEdit')
        this.$events.off('actionCopy')
      }
    },

    computed: {
      gridReturnedToStorageLabel: function () {
        let result = 'returned to storage'
        if (this.entity.gridReturnedToStorage !== true) {
          result = 'not ' + result
        }

        return result
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
      this.initEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    }
  }
</script>

<style scoped lang="scss">
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
</style>

