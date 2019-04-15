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
            <el-col :span="12">
              <el-form-item :label="projectManagementSystemName + ' SB human readable ID'"
                            prop="projectManagementSystemSlug"
                            :error="getError('projectManagementSystemSlug')">
                <el-input v-model="entity.projectManagementSystemSlug"
                          @blur="validateProjectManagementSystemSlug()">
                  <i class="el-icon-check" slot="append">
                    <state-indicator :state="projectManagementSystemSlugValidationState"></state-indicator>
                  </i>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </el-row>

    <el-row>
      <project-management-system-info :projectManagementSystemSlug="entity.projectManagementSystemSlug"/>
    </el-row>
  </div>
</template>

<script>
  import ProjectManagementSystemInfo from './ProjectManagementSystemInfo'
  import BaseForm from '@/components/App/Admin/Project/BaseForm'
  import StateIndicator from '@/components/App/StateIndicatorComponent'
  import { projectManagementSystemName } from '@/utils/ExternalSystemUtils'

  export default {
    mixins: [
      BaseForm
    ],

    components: {
      ProjectManagementSystemInfo,
      StateIndicator
    },

    data () {
      return {
        projectManagementSystemSlugValidationState: null,
        validProjectManagementSystemSlugs: [],
        originalProjectManagementSystemSlug: null,
        rules: {
          label: [
            {
              required: true,
              message: 'Project label can\'t be empty',
              trigger: 'blur'
            }
          ],
          projectManagementSystemSlug: [
            {
              required: true,
              message: 'Please enter a ' + this.createProjectManagementSystemName() + ' SB human readable ID',
              trigger: 'blur'
            }
          ]
        }
      }
    },

    computed: {
      projectManagementSystemName: function () {
        return this.createProjectManagementSystemName()
      }
    },

    methods: {
      setProjectManagementSystemError (error) {
        this.errors.projectManagementSystemSlug = error
      },

      async validateProjectManagementSystemSlug () {
        this.setProjectManagementSystemError('')

        if (!this.entity.projectManagementSystemSlug) {
          this.handleEmptyProjectManagementSystemSlug()
          throw new Error('Empty projectManagementSystemSlug')
        }

        if (this.validProjectManagementSystemSlugs.includes(this.entity.projectManagementSystemSlug)) {
          this.projectManagementSystemSlugValidationState = 'correct'
          return
        } else {
          this.projectManagementSystemSlugValidationState = 'processing'
        }

        try {
          const projectManagementSystemProject =
            await this._projectService.findProjectManagementSystemEntity(this.entity.projectManagementSystemSlug)
          this.handleProjectManagementSystemProjectValidated(projectManagementSystemProject.displayId)
        } catch (error) {
          this.handleProjectManagementSystemError(error)
          throw error
        }
      },

      handleEmptyProjectManagementSystemSlug () {
        this.projectManagementSystemSlugValidationState = 'invalid'
        this.setProjectManagementSystemError('Please enter a ' + this.projectManagementSystemName + ' SB human readable ID')
      },

      handleProjectManagementSystemError (error) {
        if (!error) {
          return
        }

        this.projectManagementSystemSlugValidationState = 'invalid'
        this.setProjectManagementSystemError('Invalid ' + this.projectManagementSystemName + ' SB human readable ID')
      },

      handleProjectManagementSystemProjectValidated (projectManagementSystemSlug) {
        this.validProjectManagementSystemSlugs.push(projectManagementSystemSlug)
        this.setProjectManagementSystemError('')
        this.projectManagementSystemSlugValidationState = 'correct'
      },

      async initialProjectManagementSystemCall (originalProjectManagementSystemSlug) {
        this.originalProjectManagementSystemSlug = originalProjectManagementSystemSlug

        if (originalProjectManagementSystemSlug) {
          this.projectManagementSystemSlugValidationState = 'correct'
          this.validProjectManagementSystemSlugs.push(originalProjectManagementSystemSlug)

          try {
            await this._projectService.findProjectManagementSystemEntity(originalProjectManagementSystemSlug)
          } catch (error) {
            this.$log.error(error)
          }
        }
      },

      validateAndSaveProject (saveCallback) {
        this.$events.$emit('formSavingStarted')

        return this.validateProjectManagementSystemSlug(this.entity.projectManagementSystemSlug)
          .then(saveCallback)
          .catch(() => {
            this.$events.$emit('formSavingFinished')
          })
      },

      createProjectManagementSystemName () {
        return projectManagementSystemName()
      }
    },

    mounted () {
      this.$events.on('initialProjectManagementSystemCall', this.initialProjectManagementSystemCall)
    },

    beforeDestroy () {
      this.$events.off('initialProjectManagementSystemCall', this.initialProjectManagementSystemCall)
    }
  }
</script>

<style lang="scss" scoped>
  @import '~loadme/dist/style/loadme.css';

  .el-icon-check {
    width: 1em;
  }

  .el-icon-check:before {
    content: none;
  }
</style>
