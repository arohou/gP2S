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
  <div class="processing-session-base-form base-form">
    <el-form ref="mainForm" labelPosition="top" :model="entity" :rules="rules">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="8">
          <el-form-item label="Processing session label" prop="label" :error="getError('label')">
            <el-input class="processing-session-base-form__form__label-input" v-model="entity.label"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Number of micrographs" prop="numberOfMicrographs"
                        :error="getError('numberOfMicrographs')">
            <el-input id="incubation-time" v-model="entity.numberOfMicrographs"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Number of picked particles" prop="numberOfPickedParticles"
                        :error="getError('numberOfPickedParticles')">
            <el-input id="volume" v-model="entity.numberOfPickedParticles"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="24">
          <el-form-item label="Base path of processing directory" prop="basePathOfProcessingDirectory"
                        :error="getError('basePathOfProcessingDirectory')">
            <el-input id="relative-concentration" v-model="entity.basePathOfProcessingDirectory"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="24">
        <el-col>
          <multiple-select :selectedEntities="entity.microscopySessions" entitiesName="microscopySessions"
                           :allEntities="allMicroscopySessions"
                           class="model-base-form__map-selector-container"
                           entity-label="Microscopy session"
                           placeholder="Choose a microscopy session"
                           error-message="Please choose a microscopy session">
            <el-form-item label="Input microscopy session(s)" prop="microscopySessions"
                          :error="getError('microscopySessions')"
                          :rules="{
                              required: true,
                              validator: this.isEmptyArray('Microscopy sessions list'),
                              message: 'At least one microscopy session needs to be selected'}"
                          class="el-form-item-as-label">
            </el-form-item>
          </multiple-select>
        </el-col>
      </el-row>

      <br/>

      <el-row :gutter="24">
        <el-col>
          <multiple-select-extended :selectedEntities="entity.usedImageProcessingSoftware"
                                    entitiesName="usedImageProcessingSoftware"
                                    :allEntities="availableImageProcessingSoftware"
                                    :config="softwareSelectConfig"
                                    class="model-base-form__map-selector-container"
                                    :defaultValues="defaultValues">
            <el-form-item label="Software used" prop="usedImageProcessingSoftware"
                          :error="getError('usedImageProcessingSoftware')"
                          :rules="{
                            required: true,
                            validator: this.isEmptyArray('Software list'),
                            message: 'Please specify at least one program used in this processing session'}"
                          class="el-form-item-as-label">
            </el-form-item>
          </multiple-select-extended>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import MultipleSelect from '@/components/Selectors/MultipleSelect'
  import MultipleSelectExtended from '@/components/Selectors/MultipleSelectWithAdditionalFields'
  import MicroscopySessionService from '@/services/MicroscopySessionService'
  import ImageProcessingSoftwareService from '@/services/ImageProcessingSoftwareService'
  import ProcessingSessionService from '@/services/ProcessingSessionService'

  export default {
    name: 'processing-session-base-form',

    props: ['entity', 'projectId'],

    mixins: [CommonValidations],

    components: {MultipleSelect, MultipleSelectExtended},

    data () {
      return {
        _microscopySessionService: Object,
        _imageProcessingSoftwareService: Object,
        allMicroscopySessions: [], // All microscopy sessions for the current project.
        availableImageProcessingSoftware: [],
        softwareSelectConfig: [
          {
            type: 'select-with-date',
            propertyName: 'imageProcessingSoftware',
            label: 'Program',
            required: true,
            errorMessage: 'Please choose software',
            placeholder: 'Choose software'
          },
          {
            type: 'select',
            propertyName: 'softwareVersion',
            listItemsName: 'imageProcessingSoftware.softwareVersions',
            label: 'Software version',
            required: true,
            errorMessage: 'Please choose software version',
            placeholder: 'Choose software version'
          },
          {
            type: 'textarea',
            propertyName: 'notes',
            label: 'Notes',
            required: false,
            placeholder: 'Notes'
          },
        ],
        rules: {
          label: [
            {
              required: true,
              message: 'Please provide a processing session label',
              trigger: 'blur'
            }
          ],
          numberOfMicrographs: [{
            required: true,
            validator: this.isIntegerGreaterThanOrEqualTo('Number of micrographs', 1),
            trigger: 'blur'
          }],
          numberOfPickedParticles: [{
            required: true,
            validator: this.isIntegerGreaterThanOrEqualTo('Number of picked particles', 1),
            trigger: 'blur'
          }]
        },
        defaultValues: null
      }
    },

    methods: {
      loadMicroscopySessions () {
        return this._microscopySessionService.listEntitiesByProject(this.projectId)
          .then(result => {
            this.allMicroscopySessions = result.data
          })
          .catch(error => this.$log.error(error))
      },

      loadAvailableSoftware () {
        return this._imageProcessingSoftwareService.listEntities()
          .then(result => {
            this.availableImageProcessingSoftware = result.data || []
          })
      },

      updateDefaultValues () {
        this._processingSessionService.getDefaultValues(this.projectId).then((result) => {
          this.defaultValues = result.data
        })
      }
    },

    watch: {
      'projectId': function () {
        this.updateDefaultValues()
      },
      'entity.microscopySessions': function () {
        this.$nextTick().then(() => {
          this.$refs['mainForm'].validateField('microscopySessions')
        })
      },
      'entity.usedImageProcessingSoftware': function () {
        this.$nextTick().then(() => {
          this.$refs['mainForm'].validateField('usedImageProcessingSoftware')
        })
      }
    },

    created () {
      this._microscopySessionService = new MicroscopySessionService()
      this._imageProcessingSoftwareService = new ImageProcessingSoftwareService()
      this._processingSessionService = new ProcessingSessionService()

      this.loadAvailableSoftware()
      this.updateDefaultValues()
    }
  }
</script>

