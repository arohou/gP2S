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
    <actions-header slot="action-header" :title="'Copying Surface Treatment Protocol'" @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import Commons from '@/components/App/Admin/SurfaceTreatmentProtocol/Commons'
  import BaseForm from '@/components/App/Admin/SurfaceTreatmentProtocol/BaseForm'
  import Service from '@/services/SurfaceTreatmentProtocolService'
  import FormStateManager from '@/components/mixins/FormStateManager'

  const service = new Service()

  export default {
    name: 'surface-treatment-protocol-copy',

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
                        this.openViewForm('admin-protocols-surface_treatment', result.data, this.useSlug)
                      })
      },

      loadFormData (slugOrId) {
        return service.getEntityBySlugOrId(slugOrId)
                      .then(result => {
                        this.entity = {
                          id: '',
                          label: 'Copy of ' + result.data.label,
                          duration: result.data.duration,
                          pressure: result.data.pressure,
                          current: result.data.current,
                          chemicalsInAtmosphere: result.data.chemicalsInAtmosphere,
                          machine: result.data.machine,
                          polarity: result.data.polarity
                        }
                      })
      }
    }
  }
</script>

<style scoped>

</style>

