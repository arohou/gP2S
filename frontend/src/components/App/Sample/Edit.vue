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
    <actions-header slot="action-header" :title="'Editing sample'"
                    @actionConfirm="submitBaseFormBy('sample')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :sample="sample" :template="getDefaultComponent()"
               :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import SampleService from '@/services/SampleService'
  import Commons from '@/components/App/Sample/Commons'
  import BaseForm from '@/components/App/Sample/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import FormStateManager from '@/components/mixins/FormStateManager'

  const service = new SampleService()

  export default {
    name: 'sample-edit',

    props: ['useSlug'],

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
        return service.saveSample(this.sample)
          .then(result => this.openViewForm('sample', result.data, this.useSlug))
          .catch(this.handleOptimisticLockingError)
      },

      loadFormData (slugOrId) {
        return this.fetchSample(slugOrId, true)
      },

      actionAddProjectAssociation () {
        this.$log.debug('actionAddProjectAssociation!')
      },

      actionDeleteFromProject (index, row) {
        this.$log.debug(index, row)
      }
    }
  }
</script>
