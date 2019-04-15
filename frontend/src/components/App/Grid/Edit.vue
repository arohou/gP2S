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
    <actions-header slot="action-header" :title="'Editing Grid'"
                    @actionConfirm="submitBaseFormBy('grid')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :grid="grid" :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import GridService from '@/services/GridService'
  import SampleService from '@/services/SampleService'
  import GridCommons from '@/components/App/Grid/GridCommons'
  import FormStateManager from '@/components/mixins/FormStateManager'
  import BaseForm from '@/components/App/Grid/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import FormComponent from '@/components/App/FormComponent'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'

  const service = new GridService()
  const sampleService = new SampleService()

  export default {
    name: 'grid-edit',

    props: ['useSlug'],

    mixins: [
      GridCommons, FormStateManager, ProjectResidentCommons
    ],

    components: {
      ActionsHeader,
      FormComponent,
      BaseForm
    },

    methods: {
      saveForm () {
        return Promise.all([service.saveGrid(this.projectId, this.grid),
          sampleService.updateAvailabilityForGridMaking(this.grid.sample)])
          .then(results => this.openViewForm('grid', results[0].data, this.useSlug))
          .catch(this.handleOptimisticLockingError)
      },

      loadFormData (slugOrId) {
        return this.fetchGrid(slugOrId)
      }
    }
  }
</script>
