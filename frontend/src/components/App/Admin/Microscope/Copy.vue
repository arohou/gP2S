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
    <actions-header slot="action-header" :title="'Copying Microscope'" @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import Commons from '@/components/App/Admin/Microscope/Commons'
  import BaseForm from '@/components/App/Admin/Microscope/BaseForm'
  import Service from '@/services/MicroscopeService'
  import FormStateManager from '@/components/mixins/FormStateManager'

  const service = new Service()

  export default {
    name: 'microscope-copy',

    props: ['useSlug', 'id'],

    mixins: [
      FormStateManager, Commons
    ],

    components: {
      BaseForm, FormComponent, ActionsHeader
    },

    methods: {
      saveForm () {
        return service.createEntity(this.entity)
          .then(result => {
            this.openViewForm('admin-equipment-microscopes', result.data, this.useSlug)
          })
      },

      loadFormData (slugOrId) {
        return service.getEntityBySlugOrId(slugOrId)
          .then(result => {
            this.entity = {
              id: null,
              label: 'Copy of ' + result.data.label,
              manufacturer: result.data.manufacturer,
              model: result.data.model,
              location: result.data.location,
              defaultExtractionVoltageKV: result.data.defaultExtractionVoltageKV,
              defaultGunLensSetting: result.data.defaultGunLensSetting,
              availableVoltagesKV: result.data.availableVoltagesKV,
              defaultVoltageKV: result.data.defaultVoltageKV,
              condenser1ApertureDiameter: result.data.condenser1ApertureDiameter,
              condenser2ApertureDiameter: result.data.condenser2ApertureDiameter,
              condenser3ApertureDiameter: result.data.condenser3ApertureDiameter,
              condenser4ApertureDiameter: result.data.condenser4ApertureDiameter,
              defaultCondenserApertureIndex: result.data.defaultCondenserApertureIndex,
              sampleInsertionMechanism: result.data.sampleInsertionMechanism,
              objectiveAperture1: result.data.objectiveAperture1,
              objectiveAperture2: result.data.objectiveAperture2,
              objectiveAperture3: result.data.objectiveAperture3,
              objectiveAperture4: result.data.objectiveAperture4,
              defaultObjectiveApertureIndex: result.data.defaultObjectiveApertureIndex,
              energyFilter: result.data.energyFilter,
              defaultEnergyFilterSlitWidth: result.data.defaultEnergyFilterSlitWidth,
              defaultSpotSize: result.data.defaultSpotSize
            }
          })
      }
    }
  }
</script>

<style scoped>

</style>

