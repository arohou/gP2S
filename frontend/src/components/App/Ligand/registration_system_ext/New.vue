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
    <actions-header slot="action-header" :title="'Creating ligand'" @actionConfirm="submitBaseFormBy('ligand')"
                    :isDialog="isDialog"></actions-header>
    <base-form slot="base-form" ref="baseForm" :ligand="ligand" :showLigandRegistrationInfo="!isDialog"
               :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import ComponentType from '@/components/App/Sample/ComponentType'
  import FormStateManager from '@/components/mixins/FormStateManager'
  import LigandEnabledFields from '@/components/App/Ligand/LigandEnabledFields'
  import Commons from '@/components/App/Ligand/registration_system_ext/Commons'
  import BaseForm from '@/components/App/Ligand/registration_system_ext/BaseForm'

  export default {
    name: 'ligand-new',

    props: {
      useSlug: {
        type: Boolean
      },
      isDialog: false // Flag: form displayed in a dialog box.
    },

    mixins: [
      Commons, FormStateManager
    ],

    components: {
      ActionsHeader,
      FormComponent,
      BaseForm
    },

    methods: {
      saveForm () {
        if (this.projectId) {
          this.$events.$emit('validateAndSaveLigand', this.createLigand)
        } else {
          this.alertNoProject()
        }
      },

      createLigand () {
        this._service.createLigand(this.projectId, this.ligand)
          .then(result => {
            if (this.isDialog) {
              this.$events.$emit('componentAdded', result.data, ComponentType.G)
            } else {
              this.openViewForm('ligand', result.data, this.useSlug) // Go to G view mode.
            }
          })
          .then(() => this.$events.emit('entitiesCountChanged'))
          .catch(error => this.$log.error(error))
          .finally(() => this.formSavingFinished())
      },

      loadFormData () {
        this.ligand = {
          id: '',
          label: this.defaultNewLabel('Ligand'),
          tubeLabel: '',
          concentration: '',
          availableForSampleMaking: true,
          conceptId: '',
          batchId: '',
          ligandEnabledFields: LigandEnabledFields.ALL,
          solvent: 'DMSO'
        }
      }
    }
  }
</script>

<style scoped>

</style>

