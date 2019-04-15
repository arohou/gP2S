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
    <actions-header slot="action-header" :title="'Creating Surface Treatment Protocol'" @actionConfirm="submitBaseFormBy('mainForm')" @actionCancel="cancel('mainForm')"></actions-header>

    <base-form slot="base-form" ref="baseForm" :entity="entity"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import Commons from '@/components/App/Admin/SurfaceTreatmentProtocol/Commons'
  import BaseForm from '@/components/App/Admin/SurfaceTreatmentProtocol/BaseForm'
  import Service from '@/services/SurfaceTreatmentProtocolService'
  import Polarity from '@/components/App/Admin/SurfaceTreatmentProtocol/Polarity'
  import FormStateManager from '@/components/mixins/FormStateManager'

  const service = new Service()

  export default {
    name: 'surface-treatment-protocol-new',

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
                        this.openViewForm('admin-protocols-surface_treatment', result.data, this.useSlug)
                      })
      },

      loadFormData () {
        this.entity = {
          id: '',
          label: this.defaultNewLabel('Surface treatment protocol'),
          duration: '',
          pressure: '',
          current: '',
          chemicalsInAtmosphere: '',
          machine: null,
          polarity: Polarity.Positive
        }
      }
    }
  }
</script>

<style scoped>

</style>

