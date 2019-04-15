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
  <div class="aliquot-selector">
    <el-select filterable remote placeholder="Please enter a keyword" noDataText="No components available"
               v-model="selectedAliquot" :remote-method="fetchAliquots" :loading="loading" value-key="key"
               @change="actionChange()">
      <el-option v-for="(aliquot) in options" :key="aliquot.slug" :value="aliquot" :label="aliquot.label">
        <div>
          <div>
            <div class="em-aliquot-selector__item-title">{{aliquot.label}}</div>
            <div class="em-aliquot-selector__item-info-id">&nbsp;&nbsp;&nbsp;#{{aliquot.id}}</div>
          </div>
          <div class="em-aliquot-selector__item-info">
            <div class="em-aliquot-selector__item-info-reference">
              <span v-if="aliquot.type === 'PROTEIN'">{{registrationConfig.protein.purIdLabelShort}}:</span><span
              v-if="aliquot.type === 'LIGAND'">{{registrationConfig.ligand.batchIdLabelShort}}:</span>&nbsp;<span>{{aliquot
              .reference}}</span>
            </div>
            <div class="em-aliquot-selector__item-info-slug">&nbsp;&nbsp;&nbsp;#{{aliquot.slug}}</div>
          </div>
        </div>
      </el-option>
    </el-select>
  </div>
</template>

<script>
  import AliquotSearchService from '@/services/AliquotSearchService'
  import ComponentType from '@/components/App/Sample/ComponentType'
  import { registrationConfig } from '@/utils/ExternalSystemUtils'

  const searchService = new AliquotSearchService()

  export default {
    name: 'aliquot-selector',

    props: ['projectId', 'component'],

    data () {
      return {
        options: [],
        loading: false,
        selectedAliquot: '',
        registrationConfig: registrationConfig()
      }
    },

    methods: {
      calculateComponentType (aliquotType) {
        return aliquotType === 'PROTEIN' ? ComponentType.PUR : ComponentType.G
      },

      actionChange () {
        if (this.selectedAliquot && this.selectedAliquot.id) {
          this.component.aliquot = this.selectedAliquot
          this.component.aliquot.type = this.calculateComponentType(this.selectedAliquot.type)
          if (this.selectedAliquotTypeChanged(this.component, this.selectedAliquot)) {
            this.component.id = null
            this.component.version = null
          }
          this.component.type = this.selectedAliquot.type
        }
      },

      selectedAliquotTypeChanged (component, selectedAliquot) {
        return component.type !== selectedAliquot.type
      },

      fetchAliquots (query = '') {
        this.loading = true
        searchService.findAliquots(this.projectId, query)
                     .then(result => {
                       this.options = result.data
                     })
                     .catch(error => this.$log.error(error))
                     .finally(() => this.loading = false)
      },

      setSelectedAliquot (component) {
        if (component && component.aliquot && component.aliquot.label) {
          this.selectedAliquot = component.aliquot.label
        } else {
          this.fetchAliquots('')
        }
      }
    },

    watch: {
      'component' () {
        this.setSelectedAliquot(this.component)
      }
    },

    mounted () {
      this.setSelectedAliquot(this.component)
    }
  }
</script>

<style lang="scss" scoped>
  .aliquot-selector {
    float: left;
    margin-right: 0.5ex;
    width: 100%
  }

  .el-select-dropdown__item {
    height: auto;
  }

  .em-aliquot-selector__item-title {
    font-size: 15px;
    font-weight: bold;
    display: inline-flex;
  }

  .em-aliquot-selector__item-info {
    color: #9b9b9b;
    font-size: 13px
  }

  .em-aliquot-selector__item-info-slug,
  .em-aliquot-selector__item-info-id {
    color: #9b9b9b;
    float: right;
    display: inline-flex;
  }

  .selected .em-aliquot-selector__item-info,
  .selected .em-aliquot-selector__item-info-id,
  .selected .em-aliquot-selector__item-info-slug {
    color: #e0e0e0;
  }

  .em-aliquot-selector__item-info-reference {
    display: inline-flex;
  }

  .aliquot-selector /deep/ .el-input__inner {
    padding-right: 55px;
    background-image: url('../../assets/images/search_16x15.png');
    background-repeat: no-repeat;
    background-position: 95% center;
    outline: 0;
  }

</style>
