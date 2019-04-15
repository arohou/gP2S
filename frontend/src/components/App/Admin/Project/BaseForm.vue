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
    <el-row>
      <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" labelPosition="top">
        <div class="base-form__metadata">
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="Project label" prop="label" :error="getError('label')">
                <el-input v-model="entity.label"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </el-row>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import ProjectService from '@/services/ProjectService'

  export default {
    name: 'project-base-form',

    props: {
      entity: {}
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
              message: 'Project label can\'t be empty',
              trigger: 'blur'
            }
          ]
        }
      }
    },

    methods: {
      validateAndSaveProject (saveCallback) {
        saveCallback()
      }
    },

    created () {
      this._projectService = new ProjectService()
    },

    mounted () {
      this.$events.on('validateAndSaveProject', this.validateAndSaveProject)
    },

    beforeDestroy () {
      this.$events.off('validateAndSaveProject', this.validateAndSaveProject)
    }
  }
</script>

<style lang="scss" scoped>
  .el-icon-check {
    width: 1em;
  }

  .el-icon-check:before {
    content: none;
  }
</style>
