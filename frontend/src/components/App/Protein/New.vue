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
    <actions-header slot="action-header" :title="'Creating protein'" @actionConfirm="submitBaseFormBy('protein')"
                    @actionCancel="cancel('protein')" :isDialog="isDialog"></actions-header>

    <base-form slot="base-form" ref="baseForm" :protein="protein" :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import Commons from '@/components/App/Protein/Commons'
  import BaseForm from '@/components/App/Protein/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import ComponentType from '@/components/App/Sample/ComponentType'
  import FormStateManager from '@/components/mixins/FormStateManager'
  import ConcentrationType from '@/components/App/ConcentrationType'

  export default {
    name: 'protein-new',

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
      ActionsHeader, FormComponent, BaseForm
    },

    methods: {
      saveForm () {
        if (this.projectId) {
          this.$events.$emit('validateAndSaveProtein', this.protein.purificationId, () => {
            this._proteinService.createProtein(this.projectId, this.protein)
              .then(result => {
                if (this.isDialog) {
                  this.$events.$emit('componentAdded', result.data, ComponentType.PUR)
                } else {
                  this.openViewForm('protein', result.data, this.useSlug) // Go to PUR view mode.
                }
              })
              .then(() => this.$events.emit('entitiesCountChanged'))
              .catch(error => this.$log.error(error))
              .finally(() => this.formSavingFinished())
          })
        } else {
          this.alertNoProject()
        }
      },

      loadFormData () {
        const defaultTitle = this.defaultNewLabel('Protein')
        this.protein = {
          id: '',
          label: defaultTitle,
          purificationId: '',
          tubeLabel: '',
          availableForSampleMaking: true,
          concentration: {
            isDilutedOrConcentrated: false,
            concentrationType: ConcentrationType.Dilution,
            dilutionFactor: null,
            concentrationFactor: null
          }
        }
      }
    }
  }
</script>

<style scoped>

</style>

