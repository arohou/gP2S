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
    <actions-header slot="action-header" :title="'Creating Microscopy session'"
                    @actionConfirm="submitBaseFormBy('mainForm')"
                    @actionCancel="cancel('mainForm')"></actions-header>

    <base-form slot="base-form" ref="baseForm" :entity="entity"
               :patternForDataStorageDirectoryName="patternForDataStorageDirectoryName"
               :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import moment from 'moment'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import Commons from '@/components/App/MicroscopySession/Commons'
  import BaseForm from '@/components/App/MicroscopySession/BaseForm'
  import FormStateManager from '@/components/mixins/FormStateManager'

  export default {
    name: 'microscopy-session-new',

    props: {
      useSlug: {
        type: Boolean
      }
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
        return this.addDataStorageDirectoryName(this.entity, this.patternForDataStorageDirectoryName)
          .then(entity => this._microscopySessionService.createEntity(this.projectId, entity))
          .then(result => result.data)
          .then(entity => this.reSaveIfNeeded(entity))
          .then(entity => this.openViewForm('microscopy_session', entity, this.useSlug))
      },

      async loadFormData () {
        const newEntity = Object.assign({}, this.entity)
        newEntity.label = this.defaultNewLabel('Microscopy session')
        newEntity.sessionStartDate = newEntity.sessionStartTime = newEntity.sessionStart = moment()
          .valueOf()
        newEntity.sessionFinishDate = newEntity.sessionFinishTime = newEntity.sessionFinish
        this.entity = newEntity

        await this.fetchDirectoryPatternIfNeeded()

        return this._microscopySessionService.getDefaultValues(this.projectId)
          .then(result => {
            this.$events.emit('setDefaultMicroscope', result.data.microscope)
          })
          .then(this.$refs['baseForm'].loadMicroscopes)
          .then(this.$refs['baseForm'].loadGrids)
      }
    }
  }
</script>

<style scoped>

</style>

