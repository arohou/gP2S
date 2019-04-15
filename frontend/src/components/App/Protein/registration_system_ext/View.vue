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
  <view-comment-base entityType="protein" :entity="protein" :icon="icon">
    <div slot="component-protocol-tag-wrapper">
      <div class="component-tag-protein">Protein</div>
    </div>
    <div v-if="protein.availableForSampleMaking" slot="component-protocol-tag-wrapper" class="component-tag-available">
      Available
    </div>
    <el-row class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">PUR ID</label>
        <div class="view__metadata__value" id="protein-incubationTime">
          <a id="purificationId" class="view__metadata__link" :href="proteinRegistrationSystemUrl"
             target="_blank">{{ protein.purificationId}}</a>
        </div>
      </el-col>
      <el-col :sm="6">
        <concentration-view :concentration="protein.concentration"/>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Tube labeling</label>
        <div class="view__metadata__value" id="protein-tube-labeling">{{protein.tubeLabel | formatUnit}}</div>
      </el-col>
      <el-col :sm="3">
        <label class="view__metadata__label">&nbsp;</label>
        <div class="view__metadata__value" id="protein-solubization-matrix">&nbsp;</div>
      </el-col>
      <el-col :sm="3">
        <label class="view__metadata__label">&nbsp;</label>
        <div class="view__metadata__value" id="protein-cryorepresentative">&nbsp;</div>
      </el-col>
    </el-row>
    <h3 id="protein-registration-system-header" class="view__metadata__section-line">
      {{ proteinRegistrationSystemName }}</h3>
    <protein-registration-system-info :purId="protein.purificationId"
                                      :state="proteinPurificationRegistrationSystemState"
                                      list-type="flat"></protein-registration-system-info>
  </view-comment-base>
</template>

<script>
  import ProteinRegistrationService from '@/components/App/Protein/registration_system_ext/ProteinRegistrationService'
  import ProteinRegistrationSystemInfo from './ProteinRegistrationSystemInfo'
  import View from '@/components/App/Protein/View'
  import { proteinRegistrationSystemName } from '@/utils/ExternalSystemUtils'

  const proteinRegistrationService = new ProteinRegistrationService()

  export default {
    name: 'protein-view',

    props: ['id'],

    mixins: [
      View
    ],

    components: {
      ProteinRegistrationSystemInfo
    },

    computed: {
      proteinRegistrationSystemUrl () {
        return this.protein.purificationId ? proteinRegistrationService.purUrl(this.protein.purificationId) : ''
      },

      proteinRegistrationSystemName: function () {
        return proteinRegistrationSystemName()
      }
    },

    data () {
      return {
        proteinPurificationRegistrationSystemState: null
      }
    },

    methods: {
      loadProteinRegistrationSystemInfo (purId) {
        if (!purId) {
          return
        }

        this.proteinPurificationRegistrationSystemState = 'processing'

        this.callProteinRegistrationSystem(purId, this.protein)
      },

      loadData (id) {
        this.fetchProtein(id).then(() => this.loadProteinRegistrationSystemInfo(this.protein.purificationId))
      }
    }
  }
</script>

<style scoped>
  #protein-registration-system-header {
    font-size: 1.231rem;
    font-weight: bold;
    margin-bottom: 1.231rem;
  }
  .component-tag-available {
    margin-left: 0.662rem;
  }
</style>

