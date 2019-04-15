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
    <actions-header slot="action-header" :title="'Copying Grid'"
                    @actionConfirm="submitBaseFormBy('grid')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :grid="grid" :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import GridService from '@/services/GridService'
  import GridCommons from '@/components/App/Grid/GridCommons'
  import FormStateManager from '@/components/mixins/FormStateManager'
  import BaseForm from '@/components/App/Grid/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import FormComponent from '@/components/App/FormComponent'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'

  const service = new GridService()

  export default {
    name: 'grid-copy',

    props: ['useSlug'],

    mixins: [
      FormStateManager, GridCommons, ProjectResidentCommons
    ],

    components: {
      BaseForm, FormComponent, ActionsHeader
    },

    methods: {
      isSampleAvailableForGridMaking (grid) {
        return grid.sample && grid.sample.availableForGridMaking
      },

      loadFormData (slugOrId) {
        return service.getGridBy(slugOrId)
                      .then(result => {
                        result.data.label = 'Copy of ' + result.data.label
                        result.data.id = null
                        result.data.slug = null

                        if (!this.isSampleAvailableForGridMaking(result.data)) {
                          result.data.sample = null
                        }

                        this.grid = result.data
                      })
      }
    }
  }
</script>

<style scoped>

</style>

