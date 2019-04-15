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
  <div class="microscopy-session-base-form">
    <el-form class="em-main-form accordion-form" ref="mainForm" :model="entity" :rules="rules"
             labelPosition="top">
      <el-collapse v-model="activeNames">
        <el-collapse-item title="Basic Information"
                          name="basic-information"
                          class="base-form__metadata"
                          id="basic-information-section">
          <basic-information :entity="entity" :projectId="projectId"
                             :patternForDataStorageDirectoryName="patternForDataStorageDirectoryName"
                             :allRules="rules" :mainForm="$refs['mainForm']"
                             :allErrors="errors"
                             :originalEntityBaseForm="originalEntity"
                             :shouldSetDefaultGridReturned="shouldSetDefaultGridReturned"
                             ref="basicInformation"></basic-information>
        </el-collapse-item>

        <el-collapse-item title="Microscope settings"
                          name="microscope-settings"
                          class="base-form__metadata"
                          id="microscope-settings-section">
          <microscope-settings :entity="entity" :projectId="projectId" :allRules="rules"
                               :mainForm="$refs['mainForm']" :allErrors="errors"
                               :originalEntityBaseForm="originalEntity"></microscope-settings>
        </el-collapse-item>

        <el-collapse-item title="Exposure settings"
                          name="exposure-settings"
                          class="base-form__metadata"
                          id="exposure-settings-section">
          <exposure-settings :entity="entity" :projectId="projectId" :allRules="rules"
                             :mainForm="$refs['mainForm']" :allErrors="errors"
                             :originalEntityBaseForm="originalEntity"
                             ref="exposureSettings"></exposure-settings>
        </el-collapse-item>

        <el-collapse-item title="Microscope control"
                          name="microscope-control"
                          class="base-form__metadata"
                          id="microscope-control-section">
          <microscope-control :entity="entity" :projectId="projectId" :allRules="rules"
                              :mainForm="$refs['mainForm']" :allErrors="errors"
                              :originalEntityBaseForm="originalEntity"></microscope-control>
        </el-collapse-item>
      </el-collapse>
    </el-form>
  </div>
</template>

<script>
  import FormCommons from '@/components/mixins/FormCommons'
  import BasicInformation from '@/components/App/MicroscopySession/BaseFormBasicInformation'
  import MicroscopeSettings from '@/components/App/MicroscopySession/BaseFormMicroscopeSettings'
  import ExposureSettings from '@/components/App/MicroscopySession/BaseFormExposureSettings'
  import MicroscopeControl from '@/components/App/MicroscopySession/BaseFormMicroscopeControl'
  import CommonValidations from '@/components/mixins/CommonValidations'

  export default {
    name: 'microscopy-session-base-form',

    props: {
      projectId: String,
      entity: Object,
      useOriginalEntity: {
        type: Boolean,
        default: false
      },
      shouldSetDefaultGridReturned: {
        type: Boolean,
        default: true
      },
      patternForDataStorageDirectoryName: String
    },

    mixins: [
      CommonValidations, FormCommons
    ],

    components: {
      BasicInformation, MicroscopeSettings, ExposureSettings, MicroscopeControl
    },

    events: {
      saveFormFailed () {
        if (this.$refs.mainForm) {
          // find all fields on form that have error property set
          let fields = this.$refs.mainForm.fields.filter(f => !!f.error)
          // get unique list of ElCollapseItem names
          let active = Array.from(new Set(fields.map(f => this.getParenElCollapseItemName(f))))
          // activate ElCollapseItem by names
          this.activeNames = active
        }
      },

      formValidationFailed () {
        if (this.$refs.mainForm) {
          // find all fields on form that validation did not success
          let fields = this.$refs.mainForm.fields.filter(f => f.validateState !== 'success')
          // get unique list of ElCollapseItem names
          let active = Array.from(new Set(fields.map(f => this.getParenElCollapseItemName(f))))
          // activate ElCollapseItem by names
          this.activeNames = active
        }
      }
    },

    data () {
      return {
        activeNames: ['basic-information'],
        rules: {}
      }
    },

    methods: {
      getParenElCollapseItemName (component) {
        if (component) {
          if (component.constructor.options.name === 'ElCollapseItem') { //Collapse Item found
            return component.name
          } else if (component.constructor.options.name === 'ElForm') { //fallback, when there was no Collapse Item and method hit form element
            return null
          }
          return this.getParenElCollapseItemName(component.$parent) //process parent of current component
        }
        return null
      },

      loadMicroscopes () {
        return this.$refs.basicInformation.loadMicroscopes()
      },

      loadGrids () {
        return this.$refs.basicInformation.loadGrids()
      },
    },

    created () {
      if (this.useOriginalEntity) {
        this.initOriginalEntity('entity')
      }
    },
  }
</script>

<style lang="scss">
  //fix for position of collapse arrow indicator inside scrollable container
  .el-collapse-item__header {
    position: relative;
  }

  //proper alignment of collapse arrow indicator
  .el-collapse-item__header__arrow {
    right: 0.7692rem !important;
  }
</style>

<style lang="scss" scoped>
  @import '~styles/globals/constants';

  .em-main-form /deep/ .el-row--flex {
    margin-left: 0 !important;
    margin-right: 0 !important;

    > * {
      flex: 1;
      margin-left: 1.615rem;
    }

    > :first-child {
      margin-left: 0;
    }

    .el-form-item__content {
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      align-items: flex-end;
      justify-content: space-between;

      .el-input__inner {
        display: inline-flex;
      }

    }

  }

</style>
