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
  <view-comment-base entityType="microscope" :entity="entity" :icon="icon">
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
          <label class="view__metadata__label">Location</label>
          <div class="view__metadata__value">{{ entity.location }}</div>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Available acceleration voltages</label>
          <el-col class="view__metadata__attributes-list">
            <el-row v-for="(voltage, index) in sortedAvailableVoltagesKV"
                    :key="index"
                    class="view__metadata__value view__metadata__microscope__list-value">
              <el-col>
                  <span v-if="entity.defaultVoltageKV && entity.defaultVoltageKV == voltage">
                    <span class="view__metadata__microscope__list-value-default">{{ voltage | formatUnit('kV')
                      }}</span><span class="view__metadata__microscope__list-value-default-label">&nbsp;(default)</span>
                  </span>
                <span v-else>{{ voltage }}&nbsp;kV</span>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Default extraction voltage</label>
          <div class="view__metadata__value">{{ entity.defaultExtractionVoltageKV | formatUnit('kV') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Default gun lens setting</label>
          <div class="view__metadata__value">{{ entity.defaultGunLensSetting }}</div>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Condenser ("C2") apertures</label>
          <el-col class="view__metadata__attributes-list">
            <el-row
              v-for="(diameter, index) in [entity.condenser1ApertureDiameter, entity.condenser2ApertureDiameter, entity.condenser3ApertureDiameter, entity.condenser4ApertureDiameter]"
              :key="index"
              class="view__metadata__value view__metadata__microscope__list-value">
              <el-col>
                <span class="sub-label">{{ index + 1 }}.&nbsp;</span>
                <span
                  v-if="entity.defaultCondenserApertureIndex && entity.defaultCondenserApertureIndex - 1 === index">
                    <span class="view__metadata__microscope__list-value-default">{{ diameter | formatUnit('μm')
                      }}</span><span class="view__metadata__microscope__list-value-default-label">&nbsp;(default)</span>
                  </span>
                <span v-else>{{ diameter | formatUnit('μm') }}</span>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Sample insertion mechanism</label>
          <div class="view__metadata__value">{{ sampleInsertionMechanismText }}</div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Objective apertures</label>
          <el-row class="small-margin">
            <el-col>
              <el-col :sm="2">&nbsp;
              </el-col>
              <el-col :sm="14" class="sub-label">Diameter</el-col>
              <el-col :sm="8" class="sub-label">Phase plate</el-col>
            </el-col>
          </el-row>
          <el-col class="view__metadata__attributes-list">
            <el-row
              v-for="(objectiveAperture, index) in [entity.objectiveAperture1, entity.objectiveAperture2, entity.objectiveAperture3, entity.objectiveAperture4]"
              :key="index"
              class="view__metadata__value view__metadata__microscope__list-value">
              <el-col>
                <el-row>
                  <el-col :sm="2">
                    <span class="sub-label">{{ index + 1 }}.&nbsp;</span>
                  </el-col>
                  <el-col :sm="14" class="view__metadata__microscope__list-value-default"
                          v-if="entity.defaultObjectiveApertureIndex && entity.defaultObjectiveApertureIndex - 1 === index">
                        <span
                          v-if="objectiveAperture.diameter && !objectiveAperture.phasePlate">{{ objectiveAperture.diameter | formatUnit('μm')
                          }}</span><span v-else>—</span>
                    <span :sm="20"
                          class="view__metadata__microscope__list-value-default-label">&nbsp;(default)</span>
                  </el-col>
                  <el-col v-else :sm="14"><span
                    v-if="objectiveAperture.diameter && !objectiveAperture.phasePlate">{{ objectiveAperture.diameter | formatUnit('μm')
                        }}</span><span v-else>—</span></el-col>
                  <el-col :sm="8">{{ objectiveAperture.phasePlate | formatBoolean }}</el-col>
                </el-row>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Energy filter?</label>
          <div class="view__metadata__value">
            {{ entity.energyFilter | formatBoolean }}
          </div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Default energy filter slit width</label>
          <div class="view__metadata__value">
            <span v-if="entity.energyFilter">{{ entity.defaultEnergyFilterSlitWidth | formatUnit('eV') }}</span>
            <span v-else>—</span>
          </div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Default spot size</label>
          <div class="view__metadata__value">{{ entity.defaultSpotSize }}</div>
        </el-row>
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Admin/Microscope/Commons'
  import ViewHeader from '@/components/App/ViewHeader'
  import Icon from '@/assets/images/microscope.svg'
  import InsertionMechanismType from '@/components/App/Admin/Microscope/InsertionMechanismType'
  import ViewCommentBase from '@/components/App/ViewCommentBase'

  export default {
    name: 'microscope-view',

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

    computed: {
      sampleInsertionMechanismText () {
        switch (this.entity.sampleInsertionMechanism) {
          case InsertionMechanismType.SIDE_ENTRY_HOLDER.name:
            return 'side-entry holder'
          case InsertionMechanismType.AUTO_LOADER.name:
            return 'autoloader'
        }
        return null
      },

      sortedAvailableVoltagesKV () {
        return this.entity.availableVoltagesKV.slice().sort((a, b) => a - b)
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
      this.$events.on('actionEdit', data => this.actionEdit('admin-equipment-microscopes', this.id))
      this.$events.on('actionCopy', data => this.actionCopy('admin-equipment-microscopes', this.id))
    },

    beforeDestroy () {
      this.$events.off('actionEdit')
      this.$events.off('actionCopy')
    }
  }
</script>

<style scoped>
  .view__metadata__microscope__list-value, .view__metadata__microscope__list-value .el-row, .small-margin {
    margin-bottom: 0.3rem;
    /*margin-top: 0.3rem;*/
  }

  .small-margin {
    margin-top: 0.7rem;
  }

  .view__metadata__microscope__list-value-default-label, .sub-label {
    color: #9b9b9b;
    font: 14px "Helvetica Neue Light", "Lucida Grande", "Calibri", "Arial", sans-serif;
  }

  .view__metadata__microscope__list-value-default {
    font-weight: bold;
  }

  .view-checkbox {
    margin-left: 1.2em;
  }

  .view__metadata__attributes-list {
    margin-bottom: 1.623rem;
  }
</style>
