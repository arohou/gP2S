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
    <actions-header slot="action-header" :title="'Copying Project'"
                    @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import Commons from '@/components/App/Admin/Project/Commons'
  import BaseForm from '@/components/App/Admin/Project/BaseForm'
  import FormStateManager from '@/components/mixins/FormStateManager'

  export default {
    name: 'project-copy',

    props: ['useSlug', 'id'],

    mixins: [
      FormStateManager, Commons
    ],

    components: {
      BaseForm, FormComponent, ActionsHeader
    },

    methods: {
      saveForm () {
        this.$events.$emit('validateAndSaveProject', () => {
          this._projectService.createEntity(this.entity)
            .then(result => {
              this.openViewForm('admin-admin-project', result.data, this.useSlug)
            })
            .finally(() => this.formSavingFinished())
        })
      },

      loadFormData (slugOrId) {
        return this._projectService.getEntityBySlugOrId(slugOrId)
          .then(result => {
            this.entity = {
              id: '',
              label: 'Copy of ' + result.data.label,
              positionInPortfolio: result.data.positionInPortfolio,
              primaryContactUserId: result.data.primaryContactUserId,
              secondaryContactUserId: result.data.secondaryContactUserId,
              hasXRayEffort: result.data.hasXRayEffort,
              xrayEffortStatus: result.data.xrayEffortStatus,
              projectTeamLeaderName: result.data.projectTeamLeaderName,
              hasChemistryEffort: result.data.hasChemistryEffort,
              chemistryEffortStatus: result.data.chemistryEffortStatus,
              chemistryRepName: result.data.chemistryRepName

            }
          })
      }
    }
  }
</script>

<style scoped>

</style>

