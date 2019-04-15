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
    <actions-header slot="action-header" :title="'Editing Microscopy session'"
                    @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity"
               :patternForDataStorageDirectoryName="patternForDataStorageDirectoryName"
               :only-available-grids="false"
               :shouldSetDefaultGridReturned="false"
               :projectId="projectId"
               :useOriginalEntity="true"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import Commons from '@/components/App/MicroscopySession/Commons'
  import BaseForm from '@/components/App/MicroscopySession/BaseForm'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import FormStateManager from '@/components/mixins/FormStateManager'

  export default {
    name: 'microscopy-session-edit',

    props: ['useSlug', 'id'],

    mixins: [
      Commons, FormStateManager, ProjectResidentCommons
    ],

    components: {
      ActionsHeader,
      FormComponent,
      BaseForm
    },

    methods: {
      saveForm () {
        return this._microscopySessionService.saveEntity(this.projectId, this.entity)
          .then(result => this.openViewForm('microscopy_session', result.data, this.useSlug))
          .catch(this.handleOptimisticLockingError)
      },

      loadFormData (slugOrId) {
        return this.fetchEntityBySlugOrId(slugOrId)
          .then(this.$refs.baseForm.loadMicroscopes)
      }
    }
  }
</script>

<style scoped>

</style>

