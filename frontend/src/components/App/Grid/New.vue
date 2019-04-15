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
    <actions-header slot="action-header" :title="'Creating Grid'" @actionConfirm="submitBaseFormBy('grid')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :grid="grid" :projectId="projectId"
               :defaultGridTypeSlugOrId="defaultGridTypeSlugOrId"
               :defaultSurfaceTreatmentProtocolSlugOrId="defaultSurfaceTreatmentProtocolSlugOrId"
               :defaultVitrificationProtocolSlugOrId="defaultVitrificationProtocolSlugOrId"
               :defaultNegativeStainProtocolSlugOrId="defaultNegativeStainProtocolSlugOrId"
               :defaultSampleSlugOrId="defaultSampleSlugOrId"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import GridService from '@/services/GridService'
  import GridCommons from '@/components/App/Grid/GridCommons'
  import FormStateManager from '@/components/mixins/FormStateManager'
  import BaseForm from '@/components/App/Grid/BaseForm'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import ProtocolType from '@/components/App/Grid/ProtocolType'

  const service = new GridService()

  export default {

    name: 'grid-new',

    props: ['useSlug'],

    mixins: [
      FormStateManager, GridCommons
    ],

    components: {
      ActionsHeader,
      FormComponent,
      BaseForm
    },

    data () {
      return {
        defaultGridTypeSlugOrId: null,
        defaultSurfaceTreatmentProtocolSlugOrId: null,
        defaultVitrificationProtocolSlugOrId: null,
        defaultNegativeStainProtocolSlugOrId: null,
        defaultSampleSlugOrId: null,
      }
    },

    methods: {
      loadFormData () {
        // Yes, I'm copying what's already there to trigger the originalEntity filling in
        // the BaseForm. Hacky. I know.
        this.grid = {...this.grid}
        this.grid.label = this.defaultNewLabel('Grid')
        this.grid.protocolType = ProtocolType.Cryo

        return service.getDefaultValues(this.projectId)
                      .then(result => {
                        const defaultGridValues = result.data
                        this.grid.protocolType = defaultGridValues.protocolType || ProtocolType.Cryo
                        this.grid.incubationTime = defaultGridValues.incubationTime || null
                        this.grid.volume = defaultGridValues.volume || null

                        this.grid.cylinderNumberLabel = defaultGridValues.cylinderNumberLabel || null
                        this.grid.tubeNumberLabel = defaultGridValues.tubeNumberLabel || null

                        this.defaultGridTypeSlugOrId = defaultGridValues.gridType || null
                        this.defaultSurfaceTreatmentProtocolSlugOrId = defaultGridValues.surfaceTreatmentProtocol || null
                        this.defaultVitrificationProtocolSlugOrId = defaultGridValues.vitrificationProtocol || null
                        this.defaultNegativeStainProtocolSlugOrId = defaultGridValues.negativeStainProtocol || null
                        this.defaultSampleSlugOrId = defaultGridValues.sample || null
                      })
      }
    }
  }
</script>

<style scoped>

</style>

