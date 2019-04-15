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
  <div class="ligand-base-form base-form">
    <el-row>
      <el-col :span="15">
        <el-form class="ligand__form" ref="ligand" :model="ligand" :rules="rules" labelPosition="top">
          <el-form-item class="ligand__form__label" label="Ligand label" prop="label" :error="getError('label')">
            <el-input id="label" class="ligand__form__label-input" v-model="ligand.label"></el-input>
          </el-form-item>
          <el-form-item :label="registrationConfig.conceptIdLabel" prop="conceptId" :error="getError('conceptId')">
            <el-input id="conceptId" v-model="ligand.conceptId"></el-input>
          </el-form-item>
          <el-form-item :label="registrationConfig.batchIdLabel" prop="batchId" :error="getError('batchId')">
            <el-input id="batchId" v-model="ligand.batchId"></el-input>
          </el-form-item>
          <el-form-item label="Tube labeling" :error="getError('tubeLabel')">
            <el-input id="tubeLabel" v-model="ligand.tubeLabel"></el-input>
          </el-form-item>

          <el-form-item label="Concentration (μM)" prop="concentration" :error="getError('concentration')">
            <el-input id="concentration" v-model="ligand.concentration"></el-input>
          </el-form-item>
          <el-form-item label="Solvent" :error="getError('solvent')">
            <el-input id="solvent" v-model="ligand.solvent"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

  export default {
    name: 'ligand-base-form',

    props: {
      ligand: Object,
      projectId: String
    },

    mixins: [
      CommonValidations
    ],

    data () {
      return {
        rules: {
          label: [
            {
              required: true,
              message: 'Ligand label can\'t be empty',
              trigger: 'blur'
            }
          ],
          concentration: [
            {required: true, message: 'Please enter concentration'},
            {validator: this.checkConcentration, trigger: 'blur'}
          ]
        },
        registrationConfig: ligandRegistrationConfig()
      }
    },

    methods: {
      getInProjectsMessage: function (projects) {
        if (_.find(projects, {slug: this.projectId})) {
          return ''
        } else if (projects.length === 1) {
          return ' (in project "' + projects[0].label + '")'
        } else {
          return ' (in projects: ' + projects.map(project => '"' + project.label + '"')
                                             .join(', ') + ')'
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import '~loadme/dist/style/loadme.css';
  @import "~styles/globals/colors";

  .el-icon-check:before {
    content: none;
  }

  .el-icon-check {
    width: 1em;
  }

  .small-content {
    font-size: 0.846rem;
    color: $text-header-color--disabled;
    line-height: initial;
    margin-top: 0.8rem;
  }
</style>
<style lang="scss">
  .usage-error .el-form-item__error {
    margin-top: 0;
    position: relative;
    width: 200%; /* for long error so that it is not folded */
  }
</style>
