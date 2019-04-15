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
    <actions-header slot="action-header" :title="'Creating Microscope'" @actionConfirm="submitBaseFormBy('mainForm')"
                    @actionCancel="cancel('mainForm')"></actions-header>
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
    name: 'microscope-new',

    props: {
      useSlug: {
        type: Boolean
      }
    },

    mixins: [
      FormStateManager, Commons
    ],

    components: {
      ActionsHeader, FormComponent, BaseForm
    },

    methods: {
      saveForm () {
        return service.createEntity(this.entity)
                      .then(result => {
                        this.openViewForm('admin-equipment-microscopes', result.data, this.useSlug)
                      })
      },

      loadFormData () {
        this.entity = {
          label: this.defaultNewLabel('Microscope'),
          manufacturer: 'FEI',
          model: '',
          location: '',
          availableVoltagesKV: [],
          defaultVoltageKV: null,
          defaultExtractionVoltageKV: null,
          defaultGunLensSetting: null,
          condenser1ApertureDiameter: null,
          condenser2ApertureDiameter: null,
          condenser3ApertureDiameter: null,
          condenser4ApertureDiameter: null,
          defaultCondenserApertureIndex: 1,
          sampleInsertionMechanism: null,
          objectiveAperture1: {phasePlate: false, diameter: null},
          objectiveAperture2: {phasePlate: false, diameter: null},
          objectiveAperture3: {phasePlate: false, diameter: null},
          objectiveAperture4: {phasePlate: false, diameter: null},
          defaultObjectiveApertureIndex: 1,
          energyFilter: false,
          defaultEnergyFilterSlitWidth: null,
          defaultSpotSize: null
        }
      }
    }
  }
</script>

<style scoped>

</style>

