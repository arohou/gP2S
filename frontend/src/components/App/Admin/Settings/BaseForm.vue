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
  <div class="project-base-form base-form">
    <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" labelPosition="top">
      <el-row class="base-form__metadata">
        <el-form-item label="Pattern for data storage directory name"
                      prop="patternForDataStorageDirectoryName"
                      :error="getError('patternForDataStorageDirectoryName')">
          <el-input v-model="entity.patternForDataStorageDirectoryName"></el-input>
        </el-form-item>
        <directory-substitution-tooltip
          :template-string="entity.patternForDataStorageDirectoryName"></directory-substitution-tooltip>
      </el-row>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import ValidationError from '@/errors/ValidationError'

  import Token from '@/utils/MicroscopySessionToken'
  import DirectorySubstitutionTooltip from './DirectorySubstitutionTooltip'

  export default {
    name: 'settings-base-form',

    props: {
      entity: Object
    },

    mixins: [
      CommonValidations
    ],

    components: {DirectorySubstitutionTooltip},

    data () {
      return {
        rules: {
          patternForDataStorageDirectoryName: [
            {
              required: true,
              validator: this.validatePatternForDataStorageDirectoryName,
              trigger: 'blur'
            }
          ]
        }
      }
    },

    methods: {
      validatePatternForDataStorageDirectoryName: (rule, value, callback) => {
        const minimalPattern = new RegExp(
          /.*/.source
          + Token.MICROSCOPY_SESSION_ID.regExp.source
          + /.*/.source)

        if (minimalPattern.test(value)) {
          callback()
          return
        }

        callback(new ValidationError('The pattern has to contain ' +
          Token.MICROSCOPY_SESSION_ID.string))
      }
    }
  }
</script>
