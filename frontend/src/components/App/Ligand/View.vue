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
  <view-comment-base entityType="ligand" :entity="ligand" :icon="icon">
    <div slot="component-protocol-tag-wrapper">
      <div class="component-tag-ligand">Ligand</div>
    </div>
    <div v-if="ligand.availableForSampleMaking" slot="component-protocol-tag-wrapper" class="component-tag-available">
      Available
    </div>
    <el-row class="view__metadata__row">
      <el-col :sm="19" class="valuesColumn">
        <el-row class="view__metadata__row">
          <el-col :sm="6">
            <el-row>
              <el-col>
                <label class="view__metadata__label">{{registrationConfig.conceptIdLabel}}</label>
                <div class="view__metadata__value" id="ligand-conceptId">{{ligand.conceptId | formatValue }}</div>
              </el-col>
            </el-row>
          </el-col>
          <el-col :sm="6">
            <label class="view__metadata__label">{{registrationConfig.batchIdLabel}}</label>
            <div class="view__metadata__value" id="ligand-batchId">{{ ligand.batchId | formatValue }}</div>
          </el-col>
          <el-col :sm="6">
            <label class="view__metadata__label">Concentration</label>
            <div class="view__metadata__value" id="ligand-concentration">
              {{ ligand.concentration | formatUnit('μM') }}
            </div>
          </el-col>
          <el-col :sm="6">
            <label class="view__metadata__label">Solvent</label>
            <div class="view__metadata__value" id="ligand-solvent">{{ ligand.solvent | formatValue }}</div>
          </el-col>
        </el-row>
        <el-row class="view__metadata__row">
          <el-col :sm="6">
            <label class="view__metadata__label">Tube label</label>
            <div class="view__metadata__value" id="ligand-tubeLabel">{{ ligand.tubeLabel | formatValue }}</div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Ligand/Commons'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

  export default {
    name: 'ligand-view',

    props: ['id'],

    data () {
      return {
        registrationConfig: ligandRegistrationConfig()
      }
    },

    mixins: [
      Commons, ProjectResidentCommons
    ],

    components: {
      ViewCommentBase
    },

    watch: {
      'id' (id) {
        this.fetchLigand(id)
      }
    },

    methods: {
      initEvents () {
        this.$events.on('actionEdit', data => this.actionEdit('ligand', this.id))
        this.$events.on('actionCopy', data => this.actionCopy('ligand', this.id))
      },
      removeEvents () {
        this.$events.off('actionEdit')
        this.$events.off('actionCopy')
      }
    },

    mounted () {
      this.fetchLigand(this.id)
      this.initEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    }
  }
</script>

<style scoped>
  .valuesColumn {
    width: 79.16667%;
  }
  .component-tag-available {
    margin-left: 0.662rem;
  }
</style>
