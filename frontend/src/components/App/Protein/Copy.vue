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
    <actions-header slot="action-header" :title="'Copying Protein'"
                    @actionConfirm="submitBaseFormBy('protein')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :protein="protein" :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import Commons from '@/components/App/Protein/Commons'
  import BaseForm from '@/components/App/Protein/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import FormComponent from '../FormComponent'
  import FormStateManager from '@/components/mixins/FormStateManager'

  export default {
    name: 'protein-copy',

    props: ['useSlug', 'id'],

    mixins: [
      Commons, FormStateManager, ProjectResidentCommons
    ],

    components: {
      FormComponent,
      BaseForm, ActionsHeader
    },

    methods: {
      saveForm () {
        if (this.projectId) {
          this.$events.$emit('validateAndSaveProtein', this.protein.purificationId, this.createProtein)
        } else {
          this.alertNoProject()
        }
      },

      createProtein () {
        this._proteinService.createProtein(this.projectId, this.protein)
          .then(result => {
            this.openViewForm('protein', result.data, this.useSlug)
          })
          .then(() => this.$events.$emit('entitiesCountChanged'))
          .catch(error => this.$log.error(error))
          .finally(() => this.formSavingFinished())
      },

      loadFormData (slugOrId) {
        return this._proteinService.getProteinBy(slugOrId)
          .then(result => {
            this.protein = {
              ...result.data,
              id: null,
              slug: null,
              label: 'Copy of ' + result.data.label,
              purificationId: '',
              availableForSampleMaking: true,
            }
          })
      }
    }
  }
</script>

<style scoped>

</style>

