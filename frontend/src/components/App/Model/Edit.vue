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
    <actions-header slot="action-header" :title="'Editing model'"
                    @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity" :projectId="projectId" :modelLinks="modelLinks"
               :delete-confirmation-callback="confirmDeletingModel"></base-form>
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
    name: 'model-edit',

    props: ['useSlug'],

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
        confirmDeletionCalled: false,
        modelLinks: []
      }
    },

    methods: {
      saveForm () {
        return this._service.saveEntity(this.entity)
          .then(result => {
            this.openViewForm('model', result.data, this.useSlug)
          })
          .catch(this.handleOptimisticLockingError)
      },

      saveForm () {
        return this._service.saveEntity({...this.entity, relationship: undefined})
          .then(result => result.data)
          .then(model => {
            return this._service.saveModelLinks(model.id, this.convertRelationshipsToModelLinks(this.entity.relationships))
              .then(() => {
                return model
              })
          })
          .then(model => this.openViewForm('model', model, this.useSlug))
          .catch(this.handleOptimisticLockingError)
      },

      loadFormData (slugOrId) {
        return this.fetchEntity(slugOrId)
          .then(this.$refs.baseForm.loadModels)
          .then(() => {
            this._service.getModelLinks(this.entity.id).then(result => {
              this.modelLinks = result.data
            }).catch(error => this.$log.error(error))
          })
      },

      attachmentRemovingStart () {
        this.$events.$emit('formSavingStarted')
      },

      attachmentRemovingFinished () {
        return this._service.getEntityBy(this.entity.slug)
          .then(result => {
            this.entity = result.data
          })
          .finally(() => {
            this.$events.$emit('formSavingFinished')
          })
      },

      async confirmDeletingModel () {
        if (this.confirmDeletionCalled) {
          return true
        }

        try {
          await this.$confirm('Are you sure you want to delete this file?',
            'This action cannot be undone.', {
              confirmButtonText: 'Keep',
              cancelButtonText: 'Delete'
            })
          this.$events.$emit('formSavingFinished')
          return false
        } catch (_) {
          this.confirmDeletionCalled = true
          return true
        }
      }
    },

    mounted () {
      this.$events.on('attachmentRemovingStart', () => this.attachmentRemovingStart())
      this.$events.on('attachmentRemovingFinished', () => this.attachmentRemovingFinished())
    },

    beforeDestroy () {
      this.$events.off('attachmentRemovingStart')
      this.$events.off('attachmentRemovingFinished')
    }
  }
</script>

<style scoped>
</style>

