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
                <label class="view__metadata__label">{{ligandRegistrationConfig.conceptIdLabel}}</label>
                <div class="view__metadata__value" id="ligand-conceptId">
                  <a id="conceptId" class="view__metadata__link" :href="ligandRegistrationUrl" target="_blank">{{ligand.conceptId}}</a>
                </div>
              </el-col>
            </el-row>
          </el-col>
          <el-col :sm="6">
            <label class="view__metadata__label">{{ligandRegistrationConfig.batchIdLabel}}</label>
            <div class="view__metadata__value"
                 id="ligand-batchId">

              <state-indicator state="processing" v-if="fullBatchIdState === ProcessingState.PROCESSING">
              </state-indicator>

              <a id="ligand-batchId-link" class="view__metadata__link" :href="ligandRegistrationUrl" target="_blank"
                 v-if="fullBatchIdState !== ProcessingState.PROCESSING">
                {{ currentFullBatchId }}</a>

              <div v-if="fullBatchIdState === ProcessingState.OUTDATED">
                <div class="text-outdated">{{ligandRegistrationConfig.batchIdLabel}} has changed in
                  {{ligandRegistrationConfig.systemName}}.<br/>Previously
                  registered {{ligandRegistrationConfig.batchIdLabel}} was</div>
                <div><b>{{ ligand.batchId }}</b></div>
              </div>

              <div v-if="fullBatchIdState === ProcessingState.ERROR">
                <div class="text-outdated">{{ fullBatchIdValidationMessage }}<br/>
                  This is previously registered {{ligandRegistrationConfig.batchIdLabel}}
                </div>
              </div>

            </div>
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


      <el-col :sm="5" class="pictureColumn">
        <label class="view__metadata__label">Structure</label>
        <ligand-registration-info :conceptId="ligand.conceptId" :batchId="ligand.batchId" box="small" />
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import LigandRegistrationService from '@/components/App/Ligand/registration_system_ext/LigandRegistrationService'
  import Commons from '@/components/App/Ligand/registration_system_ext/Commons'
  import LigandRegistrationInfo from '@/components/App/Ligand/registration_system_ext/LigandRegistrationInfo'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import StateIndicator from '@/components/App/StateIndicatorComponent'
  import { Enum } from 'enumify'
  import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

  export default {
    name: 'ligand-view',

    props: ['id'],

    data () {
      class ProcessingState extends Enum {}

      ProcessingState.initEnum(['PROCESSING', 'ERROR', 'VALID', 'OUTDATED'])

      return {
        _ligandRegistrationService: Object,
        ProcessingState: ProcessingState,
        fullBatchIdState: ProcessingState.PROCESSING,
        fullBatchIdValidationMessage: String,
        currentFullBatchId: String,
        ligandRegistrationConfig: ligandRegistrationConfig()
      }
    },

    mixins: [
      Commons, ProjectResidentCommons
    ],

    components: {
      LigandRegistrationInfo, ViewCommentBase, StateIndicator
    },

    computed: {
      ligandRegistrationUrl () {
        return this.ligand.conceptId ? this._ligandRegistrationService.compoundUrl(this.ligand.conceptId) : ''
      }
    },

    watch: {
      'id' (id) {
        this.fetchLigand(id)
      },

      'ligand.conceptId' (conceptId) {
        this._ligandRegistrationService.findLigand(this.ligand.conceptId).then(response => {
          this.validateFullBatchId(_.get(response, 'data.batchId'), null)
          return response.data
        }).then((ligandRegistrationData) => {
          this.$events.$emit('ligandRegistrationLoaded', ligandRegistrationData)
        }).catch(error => {
          this.validateFullBatchId(null, error.response.data)
        })
      }
    },

    methods: {
      validateFullBatchId (fullBatchId, errorMessage) {
        this.currentFullBatchId = fullBatchId

        if (errorMessage || _.isNil(fullBatchId)) {
          this.fullBatchIdValidationMessage = errorMessage ? errorMessage : this.ligandRegistrationConfig.systemName +
          ' Error'
          this.fullBatchIdState = this.ProcessingState.ERROR
          this.currentFullBatchId = this.ligand.batchId
        } else if (fullBatchId !== this.ligand.batchId) {
          this.fullBatchIdValidationMessage =
            this.ligandRegistrationConfig.batchIdLabel + ' has changed in ' + this.ligandRegistrationConfig.systemName + '. Previously registered ' + this.ligandRegistrationConfig.batchIdLabel + ' was ' + this.ligand.batchId
          this.fullBatchIdState = this.ProcessingState.OUTDATED
        } else {
          this.fullBatchIdState = this.ProcessingState.VALID
        }
      },

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
      this._ligandRegistrationService = new LigandRegistrationService()

      this.$log.debug('props.id: ', this.id)
      this.fetchLigand(this.id)
      this.initEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    }
  }
</script>

<style scoped>
  .state-indicator {
    width: 50%;
  }

  .text-outdated {
    font-size: .9rem;
    margin-bottom: 0.3rem;
    font-weight: normal;
  }

  .valuesColumn {
    width: 79.16667%;
  }

  .pictureColumn {
    width: 20.83333%;
  }

  .component-tag-available {
    margin-left: 0.662rem;
  }
</style>
