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
    <actions-header slot="action-header" :title="'Copying Sample Holder'" @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import Commons from '@/components/App/Admin/SampleHolder/Commons'
  import BaseForm from '@/components/App/Admin/SampleHolder/BaseForm'
  import Service from '@/services/SampleHolderService'
  import FormStateManager from '@/components/mixins/FormStateManager'

  const service = new Service()

  export default {
    name: 'sample-holder-copy',

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
                        this.openViewForm('admin-equipment-sample_holders', result.data, this.useSlug)
                      })
      },

      loadFormData (slugOrId) {
        return service.getEntityBySlugOrId(slugOrId)
                      .then(result => {
                        this.entity = {
                          id: '',
                          label: 'Copy of ' + result.data.label,
                          manufacturer: result.data.manufacturer,
                          model: result.data.model,
                          cryoCapable: result.data.cryoCapable,
                          microscopes: result.data.microscopes || [],
                          location: result.data.location,
                          maximumTilt: result.data.maximumTilt,
                          doubleTilt: result.data.doubleTilt
                        }
                      })
      }
    }
  }
</script>
