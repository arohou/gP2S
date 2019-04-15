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
    <actions-header slot="action-header" :title="'Copying model'"
                    @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity" :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import Commons from '@/components/App/Model/Commons'
  import BaseForm from '@/components/App/Model/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import FormStateManager from '@/components/mixins/FormStateManager'

  export default {
    name: 'model-copy',

    props: ['useSlug'],

    mixins: [
      Commons, FormStateManager, ProjectResidentCommons
    ],

    components: {
      BaseForm, FormComponent, ActionsHeader
    },

    methods: {
      saveForm () {
        if (!this.projectId) {
          this.alertNoProject()
          return false
        }

        return this._service.createEntity(
          this.projectId, {...this.entity, relationship: undefined})
          .then(result => result.data)
          .then(model => {
            this.entity.id = model.id
            return this._service.saveModelLinks(model.id, this.convertRelationshipsToModelLinks(this.entity.relationships))
              .then(() => {
                return model
              })
          })
          .then(model => this.openViewForm('model', model, this.useSlug))
      },

      loadFormData (id) {
        return this._service.getEntityBy(id)
          .then(result => result.data)
          .then(entity => this.prepareCopiedEntity(entity))
          .then(data => this.entity = data)
          .then(this.$refs.baseForm.loadModels)
      },

      prepareCopiedEntity: function (entity) {
        let result = Object.assign(entity, {})
        result.label = 'Copy of ' + result.label
        result.id = null
        result.slug = null
        result.attachmentMongoId = null
        result.attachmentFileName = null
        result.createdBy = null
        result.createdDate = null
        result.modifiedBy = null
        result.modifiedDate = null
        result.relationships = []
        return result
      }
    }
  }
</script>

<style scoped>

</style>

