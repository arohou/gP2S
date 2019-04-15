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
  <form-component>
    <actions-header slot="action-header" :title="'Editing ligand'" @actionConfirm="submitBaseFormBy('ligand')"
                    :isDialog="isDialog"></actions-header>
    <base-form slot="base-form" ref="baseForm" :ligand="ligand" :showLigandRegistrationInfo="!isDialog"
               :projectId="projectId"></base-form>

    <!-- Temporary disabled until 1.1 -->
    <div type="flex" v-if="false && !isDialog" class="base-form">
      <el-col :sm="15" id="projects-form">
        <el-row :gutter="20" type="flex">
          <el-col :span="12">
            <label class="el-form-item__label">
              Projects
            </label>
            <el-select v-model="selectedProject" filterable placeholder="Project context"
                       value-key="id">
              <el-option
                v-for="item in projectsData"
                :key="item.id"
                :label="item.label"
                :value="item">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="12">
            <el-col :span="3">
              <label class="el-form-item__label">&nbsp;</label>
              <el-button @click="actionDeleteFromProject()" type="danger">Remove</el-button>
            </el-col>
          </el-col>
        </el-row>
        <el-row :gutter="20" type="flex">
          <el-col :span="12">
            <el-button @click="actionAddProjectAssociation()" id="add-to-project-button">Add to project</el-button>
          </el-col>
        </el-row>
      </el-col>
    </div>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import ComponentType from '@/components/App/Sample/ComponentType'
  import FormStateManager from '@/components/mixins/FormStateManager'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import Commons from '@/components/App/Ligand/registration_system_ext/Commons'
  import BaseForm from '@/components/App/Ligand/registration_system_ext/BaseForm'

  export default {
    name: 'ligand-edit',

    props: ['useSlug', 'id', 'isDialog'], // Flag: form displayed in a dialog box.]

    mixins: [
      Commons, FormStateManager, ProjectResidentCommons
    ],

    components: {
      ActionsHeader,
      FormComponent,
      BaseForm
    },

    data () {
      return {
        selectedProject: null,
        projects: [],
        projectsData: []
      }
    },

    methods: {
      saveForm () {
        this.$events.$emit('validateAndSaveLigand', this.createLigand)
      },

      createLigand () {
        this._service.saveLigand(this.ligand)
          .then(result => {
            if (this.isDialog) {
              this.$events.$emit('componentAdded', result.data, ComponentType.G)
            } else {
              this.openViewForm('ligand', result.data, this.useSlug) // Go to G view mode.
            }
          })
          .catch(this.handleOptimisticLockingError)
          .catch(error => this.$log.error(error))
          .finally(() => this.formSavingFinished())
      },

      loadFormData () {
        this.$log.debug('props.id: ', this.id)
        return this.fetchLigand(this.id)
          .then(() => {
            if (this.ligand) {
              this.$events.$emit('initialLigandRegistrationCall', this.ligand.conceptId, this.ligand.batchId)
            }
          })
      },

      actionAddProjectAssociation () {
        this.$log.debug('actionAddProjectAssociation!')
      },

      actionDeleteFromProject (index, row) {
        this.$log.debug(index, row)
      }
    },

    watch: {
      'projectsData' (projectsData) {
        this.selectedProject = projectsData && projectsData[0] ? this.projectsData[0] : null
      }
    }
  }
</script>

<style scoped>
  #add-to-project-button {
    margin-top: 2rem;
  }

  #projects-form {
    margin-top: -0.846rem;
  }
</style>

