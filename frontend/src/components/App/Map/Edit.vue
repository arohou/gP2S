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
    <actions-header slot="action-header" :title="'Editing map'"
                    @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity" :projectId="projectId"
               :delete-confirmation-callback="confirmDeletingMap" :mapLinks="mapLinks"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import Commons from '@/components/App/Map/Commons'
  import BaseForm from '@/components/App/Map/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import FormStateManager from '@/components/mixins/FormStateManager'

  export default {
    name: 'map-edit',

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
        mapLinks: []
      }
    },

    methods: {
      saveForm () {
        return this._service.saveEntity({...this.entity, relationship: undefined})
          .then(result => result.data)
          .then(map => {
            return this._service.saveMapLinks(map.id, this.convertRelationshipsToMapLinks(this.entity.relationships))
              .then(() => {
                return map
              })
          })
          .then(map => this.openViewForm('map', map, this.useSlug))
          .catch(this.handleOptimisticLockingError)
      },

      loadFormData (slugOrId) {
        return this.fetchEntity(slugOrId)
          .then(this.$refs.baseForm.loadMaps)
          .then(() => {
            this._service.getMapLinks(this.entity.id).then(result => {
              this.mapLinks = result.data
            }).catch(error => this.$log.error(error))
          })
      },

      async confirmDeletingMap () {
        if (this.confirmDeletionCalled) {
          return true
        }

        try {
          await this.$confirm('Are you sure you want to delete this file?',
            'This action cannot be undone.', {
              confirmButtonText: 'Keep',
              cancelButtonText: 'Delete'
            })
          return false
        } catch (_) {
          this.confirmDeletionCalled = true
          return true
        }
      }
    },

    mounted () {
      this.$events.on('attachmentRemovingFinished', () => this.loadFormData(this.entity.slug))
    },

    beforeDestroy () {
      this.$events.off('attachmentRemovingFinished')
    }
  }
</script>

<style scoped>
</style>

