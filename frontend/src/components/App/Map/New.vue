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
    <actions-header slot="action-header" :title="'Creating map'"
                    @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity" :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import Commons from '@/components/App/Map/Commons'
  import BaseForm from '@/components/App/Map/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import FormStateManager from '@/components/mixins/FormStateManager'

  export default {

    name: 'map-new',

    props: ['useSlug'],

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
        if (!this.projectId) {
          this.alertNoProject()
          return false
        }

        return this._service.createEntity(
          this.projectId, {...this.entity, relationship: undefined})
          .then(result => result.data)
          .then(map => {
            this.entity.id = map.id
            return this._service.saveMapLinks(map.id, this.convertRelationshipsToMapLinks(this.entity.relationships))
              .then(() => {
                return map
              })
          })
          .then(map => this.openViewForm('map', map, this.useSlug))
      },

      loadFormData () {
        this.entity.label = this.defaultNewLabel('Map')
        this.$refs.baseForm.loadMaps()
      }
    }
  }
</script>
