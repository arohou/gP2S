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
    <actions-header slot="action-header" :title="'Copying Microscopy session'"
                    @actionConfirm="submitBaseFormBy('mainForm')"></actions-header>
    <base-form slot="base-form" ref="baseForm" :entity="entity"
               :patternForDataStorageDirectoryName="patternForDataStorageDirectoryName"
               :projectId="projectId"></base-form>
  </form-component>
</template>

<script>
  import FormComponent from '@/components/App/FormComponent'
  import moment from 'moment'
  import ActionsHeader from '@/components/App/ActionsHeader'
  import Commons from '@/components/App/MicroscopySession/Commons'
  import Utils from '@/components/App/MicroscopySession/Utils'
  import BaseForm from '@/components/App/MicroscopySession/BaseForm'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import FormStateManager from '@/components/mixins/FormStateManager'

  export default {
    name: 'microscopy-session-copy',

    props: ['useSlug', 'id'],

    mixins: [
      Commons, FormStateManager, ProjectResidentCommons, Utils
    ],

    components: {
      BaseForm, FormComponent, ActionsHeader
    },

    methods: {
      saveForm () {
        return this.addDataStorageDirectoryName(this.entity, this.patternForDataStorageDirectoryName)
          .then(entity => this._microscopySessionService.createEntity(this.projectId, entity))
          .then(result => result.data)
          .then(entity => this.reSaveIfNeeded(entity))
          .then(entity => this.openViewForm('microscopy_session', entity, this.useSlug))
      },

      loadFormData () {
        return this._microscopySessionService.getEntityBySlugOrId(this.id)
          .then(result => result.data)
          .then(entity => this.prepareCopiedEntity(entity))
          .then(entity => this.entity = entity)
          .then(() => this.fetchDirectoryPatternIfNeeded())
          .then(this.$refs.baseForm.loadMicroscopes)
      },

      prepareCopiedEntity: function (entity) {
        let result = Object.assign(entity, {})
        result.id = null
        result.slug = null
        result.label = 'Copy of ' + entity.label
        result.grid = null
        result.gridReturnedToStorage = false
        result.sessionStartDate = result.sessionStartTime = result.sessionStart = moment().valueOf()
        result.sessionFinishDate = result.sessionFinishTime = result.sessionFinish = ''
        result.numberOfImagesAcquired = null
        result.dataStorageDirectoryName = null

        if (!this.isApplicableObjectiveAperture(result.microscope, result.objectiveAperture)) {
          result.objectiveAperture = this.getDefaultObjectiveAperture(result.microscope)
        }
        if (!this.isApplicableAccelerationVoltageKV(result.microscope, result.accelerationVoltageKV)) {
          result.accelerationVoltageKV = result.microscope.defaultVoltageKV
        }
        if (!this.isApplicableCondenser2ApertureDiameter(result.microscope, result.condenser2ApertureDiameter)) {
          result.condenser2ApertureDiameter = this.getDefaultCondenser2ApertureDiameter(result.microscope)
        }
        if (!this.isApplicableMagnification(result.electronDetector, result.nominalMagnification)) {
          result.nominalMagnification = null
        }
        return result
      }
    }
  }
</script>

<style scoped>

</style>

