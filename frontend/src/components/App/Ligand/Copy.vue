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
    <actions-header slot="action-header" :title="'Copying ligand'"
                    @actionConfirm="submitBaseFormBy('ligand')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :ligand="ligand" :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import Commons from '@/components/App/Ligand/Commons'
  import FormStateManager from '@/components/mixins/FormStateManager'
  import BaseForm from '@/components/App/Ligand/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import LigandEnabledFields from '@/components/App/Ligand/LigandEnabledFields'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'

  export default {
    name: 'ligand-copy',

    props: ['useSlug', 'id'],

    mixins: [
      Commons, FormStateManager, ProjectResidentCommons
    ],

    components: {
      BaseForm, FormComponent, ActionsHeader
    },

    methods: {
      saveForm () {
        if (this.projectId) {
          return this.shouldProceedWithSaving(this.ligand.conceptId)
                     .then(() => this._service.createLigand(this.projectId, this.ligand))
                     .then(result => this.openViewForm('ligand', result.data, this.useSlug))
                     .then(() => this.$events.emit('entitiesCountChanged'))
        }
        this.alertNoProject()
        return false
      },

      loadFormData () {
        return this._service.getLigandBy(this.id)
                   .then(result => {
                     this.ligand = {
                       ...result.data,
                       id: null,
                       slug: null,
                       label: 'Copy of ' + result.data.label,
                       conceptId: null,
                       batchId: null,
                       ligandEnabledFields: LigandEnabledFields.ALL,
                       availableForSampleMaking: true
                     }
                   })
      }
    }
  }
</script>

<style scoped>

</style>

