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
    <actions-header slot="action-header" :title="'Editing protein'" @actionConfirm="submitBaseFormBy('protein')"
                    :isDialog="isDialog"></actions-header>
    <base-form slot="base-form" ref="baseForm" :protein="protein" :projectId="projectId"></base-form>

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
  import Commons from '@/components/App/Protein/Commons'
  import BaseForm from '@/components/App/Protein/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import ComponentType from '@/components/App/Sample/ComponentType'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import FormStateManager from '@/components/mixins/FormStateManager'

  export default {
    name: 'protein-edit',

    props: ['useSlug', 'id',
      'isDialog'], // Flag: form displayed in a dialog box.]

    mixins: [
      Commons, FormStateManager, ProjectResidentCommons
    ],

    data () {
      return {
        selectedProject: null,
        projects: []
      }
    },

    components: {
      ActionsHeader,
      BaseForm,
      FormComponent
    },

    methods: {
      saveForm () {
        this.$events.$emit('validateAndSaveProtein', this.protein.purificationId, () => {
          this._proteinService.saveProtein(this.protein)
            .then(result => {
              if (this.isDialog) {
                this.$events.$emit('componentAdded', result.data, ComponentType.PUR)
              } else {
                this.openViewForm('protein', result.data, this.useSlug) // Go to PUR view mode.
              }
            })
            .catch(this.handleOptimisticLockingError)
            .catch(error => this.$log.error(error))
            .finally(() => this.formSavingFinished())
        })
      },

      loadFormData (slugOrId) {
        return this.fetchProtein(slugOrId).then()
          .then(() => this.$events.$emit('initialDataLoad', this.protein.purificationId))
      },

      actionAddProjectAssociation () {
        this.$log.debug('actionAddProjectAssociation!')
      },

      actionDeleteFromProject () {
        this.$log.debug(this.selectedProject ? this.selectedProject.id : 'NULL')
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
