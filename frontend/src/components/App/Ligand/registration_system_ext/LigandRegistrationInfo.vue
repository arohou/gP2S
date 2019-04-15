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
  <div class="ligand-registration-info"
       :class="{'ligand-registration-info__box-small': box === 'small', 'ligand-registration-info__box-large': box === 'large'}">
    <div :class="{'view__metadata': box === 'large'}" v-if="state === 'correct'">
      <div v-if="box === 'large'" class="no-margin">
        <el-row>
          <el-col :xs="24" :sm="18">
            <h5 class="view__metadata__label view__metadata_click_label" @click="showLargeStructureDrawing()">click to
              open</h5>
          </el-col>
          <el-col :xs="24" :sm="6">
            <h5 class="view__metadata__label ligand-registration-title-label">
              {{ligandRegistrationConfig.systemName}}</h5>
          </el-col>
        </el-row>
        <el-row>
          <label class="view__metadata__label">Molecular weight</label>
          <div class="view__metadata__value" id="ligand-registration-info__molecular-weight">
            {{ molecularWeight | formatUnit('Da') }}
          </div>
        </el-row>
      </div>

      <div class="structure-drawing-miniature"
           :class="{'structure-drawing-miniature__box-small': box === 'small', 'structure-drawing-miniature__box-large': box === 'large'}">
        <img :src="structureDrawing" @click="showLargeStructureDrawing()"
             id="ligand-registration-structure-drawing"></img>
        <span v-if="box === 'small'" class="structure-drawing-miniature__text" @click="showLargeStructureDrawing()">click to open</span>
      </div>

      <overlay-component :visible.sync="largeStructureDrawingVisible" :showClose="true">
        <div v-if="largeStructureDrawingVisible">
          <div v-bind:style="{ width: largeStructureDrawingWidth + 'px' }">
            <img :src="structureDrawingLarge" id="ligand-registration-structure-drawing-large"/>
          </div>
          <state-indicator state="processing" id="structure-drawing-large-spinning-wheel"
                           v-if="!structureDrawingLarge || structureDrawingLarge.length === 0"></state-indicator>
        </div>
      </overlay-component>
    </div>

    <div class="view__metadata before-data-loaded" v-if="state !== 'correct'">
      <el-row>
        <el-col :xs="24" :sm="18">
          <div v-if="isErrorState">
            <div class="error-message-wrapper">
              <h5 class="view__metadata__label">
                <span class="error-message">{{ error }}</span>
              </h5>
            </div>
          </div>
          <p class="view__metadata__value" v-if="state === 'processing'">
            <state-indicator :state="state"></state-indicator>&nbsp;
          </p>
          <span v-if="state === null">&nbsp;</span>
        </el-col>
        <el-col v-if="box === 'large'" :xs="24" :sm="6">
          <h5 class="view__metadata__label ligand-registration-title-label">{{ligandRegistrationConfig.systemName}}</h5>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import StateIndicator from '@/components/App/StateIndicatorComponent'
  import LigandRegistrationService from '@/components/App/Ligand/registration_system_ext/LigandRegistrationService'
  import OverlayComponent from '@/components/App/OverlayComponent'
  import FilterCommons from '@/components/mixins/FilterCommons'
  import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

  export default {
    props: {
      conceptId: String,
      batchId: String,
      box: {
        type: String,
        default: 'large'
      }
    },
    name: 'ligand-registration-info',

    components: {
      StateIndicator, OverlayComponent
    },

    mixins: [
      FilterCommons
    ],

    data () {
      return {
        molecularWeight: null,
        structureDrawing: null,
        structureDrawingLarge: null,
        error: null,
        largeStructureDrawingVisible: false,
        largeStructureDrawingWidth: 0,
        state: 'processing',
        ligandRegistrationData: null,
        ligandRegistrationConfig: ligandRegistrationConfig()
      }
    },

    computed: {
      isErrorState: function () {
        if (this.state === 'unknown' || this.state === 'invalid') {
          return true
        }

        return false
      }
    },

    events: {
      async ligandRegistrationLoaded (ligandRegistrationData) {
        if (!this.isEventForMe(_.get(ligandRegistrationData, 'conceptId'),
            _.get(ligandRegistrationData, 'batchId'))) {
          return
        }

        this.setError(null)
        this.ligandRegistrationData = ligandRegistrationData
        await Promise.all([
          this.setMolecularWeight(ligandRegistrationData.molecularWeight),
          this.fetchStructureDrawing(ligandRegistrationData.batchId),
        ])
        this.state = 'correct'
      },

      ligandRegistrationProcessing (conceptId, batchId) {
        if (!this.isEventForMe(conceptId, batchId)) {
          return
        }

        this.state = 'processing'
        this.error = null
      },

      ligandRegistrationServerError (conceptId, batchId, error) {
        if (!this.isEventForMe(conceptId, batchId)) {
          return
        }

        this.state = 'unknown'
        this.error = _.get(error, 'response.data', error)
        this.conceptId = null
      },

      conceptIdentifierInvalid (conceptId, batchId, errorMessage) {
        if (!this.isEventForMe(conceptId, batchId)) {
          return
        }
        this.state = 'invalid'
        this.error = _.isObject(errorMessage) ? errorMessage.errorMessage : errorMessage
      }
    },

    methods: {
      isEventForMe (conceptId, batchId) {

        if ((this.conceptId && this.conceptId === conceptId)
          || (this.batchId && this.batchId === batchId)
          || (batchId && conceptId && !this.batchId && !this.conceptId)) {
          return true
        }
        return false
      },

      showLargeStructureDrawing () {
        this.structureDrawingLarge = '' // Empty picture.

        this.largeStructureDrawingVisible = true

        let height = Math.round(document.documentElement.clientHeight * 0.7 - 60)
        this.largeStructureDrawingWidth = Math.round((248 / 240) * height)

        this._ligandRegistrationService.findBase64LigandPicture(this.ligandRegistrationData.batchId,
          this.largeStructureDrawingWidth, height)
          .then(response => {
            this.structureDrawingLarge = response.data
            this.state = 'correct'
          }).catch(error => {
          this.$log.error(error.response.data)
        })
      },

      async fetchStructureDrawing (batchId) {
        const width = (this.box === 'small') ? 100 : 248
        const height = (this.box === 'small') ? 100 : 240
        const response = await
          this._ligandRegistrationService.findBase64LigandPicture(batchId, width, height)
        this.structureDrawing = response.data
      },

      async setMolecularWeight (molecularWeight) {
        if (!molecularWeight) {
          throw new Error('Molecular weight not set in ' + this.ligandRegistrationConfig.systemName)
        }

        this.molecularWeight = molecularWeight
      },

      setError (error) {
        if (!error) {
          this.error = null
          return
        }

        if (this.error) {
          this.error += '\n' + error
        } else {
          this.error = error.toString()
        }
      }
    },

    created () {
      this._ligandRegistrationService = new LigandRegistrationService()
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .ligand-registration-info__box-small {
    border-radius: 0.3077rem;
    width: 7.692rem;
    margin-top: 1.308rem;
  }

  .ligand-registration-info__box-large {
    min-width: 21.54rem;
    float: right;
    background-color: #e5f0ff;
    padding-left: 0.4rem;
    padding-right: 0.4rem;
    padding-top: 0.4rem;
  }

  .ligand-registration-info .view__metadata {
    padding: 0.6154rem;
    margin: 0px;
  }

  .ligand-registration-info-label {
    margin-bottom: 0;
  }

  .ligand-registration-title-label {
    text-align: right;
    margin-right: 0.3077rem;
  }

  .structure-drawing-miniature {
    margin-bottom: 0;
    background-color: #ffffff;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 0.7em;
    border-radius: 0.3077rem;
    padding: 0.1538rem;
    border: solid 1px #9b9b9b;
    position: relative;
  }

  .structure-drawing-miniature__text {
    position: absolute;
    bottom: 0.3846rem;
    left: 1.154rem;
    cursor: pointer;
    color: $text-header-color--disabled;
  }

  .structure-drawing-miniature__box-small {
    width: 8rem;
  }

  .structure-drawing-miniature__box-large {
    width: 19.54rem;
  }

  .structure-drawing-miniature img {
    cursor: pointer;
  }

  .state-indicator {
    float: left;
    margin-left: 46%;
    margin-top: 0.1538rem;
  }

  .view__metadata.before-data-loaded .el-row {
    margin-bottom: 0 !important;
  }

  .error-message-wrapper h5 {
    margin-bottom: 0;
  }

  .no-margin {
    margin-bottom: 0;
  }

  .view__metadata__label, .view__metadata__value {
    margin-left: 0.4615rem;
    margin-right: 0.4615rem;
  }

  .view__metadata_click_label {
    color: #629be8;
  }

  .view__metadata_click_label:hover {
    text-decoration: underline;
    cursor: pointer;
  }

  #structure-drawing-large-spinning-wheel {
    margin-top: 46%;
    margin-left: 50%;
  }
</style>
<style lang="scss">
  .ligand-registration-info .el-dialog--large {
    width: auto;
    height: 70% !important;
  }
</style>
