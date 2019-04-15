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
  <el-row
    :class="{ 'protein-registration-system-box': listType === 'box', 'protein-registration-system-flat': listType === 'flat' }">
    <div class="view__metadata title-row" v-if="listType ==='box'">
      <el-row>
        <label class="view__metadata__label">{{ proteinRegistrationSystemName }}</label>
      </el-row>
    </div>

    <div :class="{ 'view__metadata': listType == 'box', 'view__metadata__row': listType === 'flat' }"
         v-if="state === 'correct'">
      <div class="first-row">
        <el-col :sm="12" class="flat-row">
          <label class="view__metadata__label">Concept UID</label>
          <div class="view__metadata__value" id="protein-protein-registration-system-concept-uid">
            <a id="purificationId" class="view__metadata__link" :href="proteinRegistrationSystemUrl(conceptUID)"
               target="_blank">
              {{ conceptUID }}</a>
          </div>
        </el-col>
        <el-col :sm="12" class="flat-row">
          <label class="view__metadata__label">Concentration</label>
          <div class="view__metadata__value" id="protein-protein-registration-system-batch-concentration">
            {{ batchConcentration | formatValue }}
          </div>
        </el-col>
      </div>
      <div class="last-row">
        <el-col :sm="12" class="flat-row">
          <label class="view__metadata__label">Molecular weight</label>
          <div class="view__metadata__value" id="protein-protein-registration-system-molecular-weight">
            {{ molecularWeightDa | formatValue }}
          </div>
        </el-col>
        <el-col :sm="12" class="flat-row">
          <label class="view__metadata__label">Buffer</label>
          <div class="view__metadata__value" id="protein-protein-registration-system-buffer-composition">
            {{ bufferComposition | formatValue }}
          </div>
        </el-col>
      </div>
    </div>

    <div class="view__metadata before-data-loaded" v-if="state !== 'correct'">
      <el-row>
        <el-col :sm="24">
          <div v-if="isErrorState">
            <div class="error-message-wrapper">
              <div class="view__metadata__label">
                <span class="error-message">{{ error }}</span>
              </div>
            </div>
          </div>
          <div class="view__metadata__value state-indicator-processing" v-if="state === 'processing'">
            <state-indicator :state="state"></state-indicator>&nbsp;
          </div>
          <span v-if="state === null">&nbsp;</span>
        </el-col>
      </el-row>
    </div>
  </el-row>
</template>

<script>
  import StateIndicator from '@/components/App/StateIndicatorComponent'
  import ProteinRegistrationService from '@/components/App/Protein/registration_system_ext/ProteinRegistrationService'
  import FilterCommons from '@/components/mixins/FilterCommons'
  import { proteinRegistrationSystemName } from '@/utils/ExternalSystemUtils'

  const proteinRegistrationService = new ProteinRegistrationService()

  export default {
    name: 'protein-registration-system-info',

    components: {
      StateIndicator
    },

    mixins: [
      FilterCommons
    ],

    props: {
      purId: String,
      listType: {
        type: String,
        default: 'box'
      },

    },

    data () {
      return {
        batchConcentration: null,
        molecularWeightDa: null,
        bufferComposition: null,
        conceptUID: null,
        error: null,
        state: 'processing'
      }
    },

    events: {
      proteinRegistrationSystemPurLoaded (purId, purName, batchConcentration, molecularWeightDa, bufferComposition, conceptUID) {
        if (!this.purId || this.purId !== purId) {
          return
        }

        this.loadInfo(purId, purName, batchConcentration, molecularWeightDa, bufferComposition, conceptUID)
        this.error = null
        this.state = 'correct'
      },
      proteinRegistrationSystemServerError (purId, error) {
        if (!this.purId || this.purId !== purId) {
          return
        }

        this.error = error
        this.state = 'unknown'
      },
      proteinRegistrationSystemPurInvalid (purId) {
        if (!this.purId || this.purId !== purId) {
          return
        }

        this.error = 'Invalid PUR ID'
        this.state = 'invalid'
      },
      proteinRegistrationSystemProcessing (purId) {
        if (!this.purId || this.purId !== purId) {
          return
        }

        this.error = null
        this.state = 'processing'
      }
    },

    methods: {
      loadInfo (purId, purName, batchConcentration, molecularWeightDa, bufferComposition, conceptUID) {
        this.purId = purId
        this.purName = purName
        this.batchConcentration = batchConcentration
        this.molecularWeightDa = molecularWeightDa ? (molecularWeightDa / 1000) + ' kDa' : null
        this.bufferComposition = bufferComposition
        this.conceptUID = conceptUID
      },

      proteinRegistrationSystemUrl (uid) {
        return this.purId ? proteinRegistrationService.purUrl(uid) : ''
      }
    },

    computed: {
      isErrorState: function () {
        if (this.state === 'unknown' || this.state === 'invalid') {
          return true
        }

        return false
      },

      proteinRegistrationSystemName: function () {
        return proteinRegistrationSystemName()
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .protein-registration-system-box {
    background-color: $list-border-color;
    .view__metadata {
      margin-top: 1.154rem;
      width: 93.5%;
    }

    .first-row .el-col {
      margin-bottom: 2.462rem;
    }
    .last-row .el-col {
      margin-bottom: 1.154rem;
    }
    .first-row, .last-row {
      width: 100%;
    }
    .title-row {
      border-bottom: 1px solid $text-header-color--disabled;
      margin-bottom: 1rem;
      .el-row {
        margin-bottom: 0;
        text-align: right;
      }
      .view__metadata__label {
        margin-right: 1.5rem;
      }
    }
  }

  .protein-registration-system-flat {
    .first-row, .last-row {
      width: 50%;
    }
    .last-row {
      float: left;
    }
    .flat-row {
      width: 50%;
      float: left;
    }
  }

  .last-row {
    margin-bottom: 0;
  }

  .state-indicator {
    margin-left: auto;
    margin-right: auto;
    width: 1.846rem;
    height: 1.846rem;
    margin-top: -1.3rem;
  }

  .state-indicator-processing {
    margin-top: 0;
  }

  .view__metadata.before-data-loaded .el-row {
    margin-bottom: 0 !important;
  }

  .protein-registration-system-flat .view__metadata {
    width: 100%;
  }
</style>

