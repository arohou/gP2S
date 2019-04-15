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
  <div class="model-base-form base-form">
    <el-form class="model-base-form__form" ref="mainForm" labelPosition="top" :model="entity" :rules="rules">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="16">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12">
              <el-form-item label="Model label" prop="label" :error="getError('label')">
                <el-input id="label" class="model-base-form__form__label-input" v-model="entity.label"></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12">
              <el-form-item label="Resolution (Å)" prop="resolution" :error="getError('resolution')">
                <el-input id="resolution" v-model="entity.resolution"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="el-form-item">
            <el-col :xs="24" :sm="24">
              <multiple-select :selectedEntities="entity.maps" entitiesName="maps" :allEntities="allMapsForProject"
                               class="model-base-form__map-selector-container"
                               entity-label="Map"
                               placeholder="Choose a map"
                               error-message="Please choose a map">
                <el-form-item label="Input map(s)" prop="maps"
                              :error="getError('maps')"
                              :rules="{
                              required: true,
                              validator: isEmptyArray('Maps list'),
                              message: 'At least one map needs to be selected'}"
                              class="el-form-item-as-label">
                </el-form-item>
              </multiple-select>
            </el-col>
          </el-row>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-row>
            <el-col>
              <el-form-item label="Model file" prop="attachmentFileName" :error="getError('attachmentFileName')"
                            ref="attachmentFileName">
                <single-file-selector :entity="entity" :attachmentService="modelService"
                                      :delete-confirmation-callback="deleteConfirmationCallback"></single-file-selector>
              </el-form-item>
            </el-col>
          </el-row>
        </el-col>

        <el-col :sm="24">
          <el-row>
            <multiple-select-extended :selectedEntities="entity.relationships"
                                      entitiesName="relationships"
                                      :allEntities="allModels"
                                      :config="relationshipsConfig"
                                      addNewLabel="Link to another model"
                                      :onRemove="validateRelationshipTypes">
              <el-form-item label="Related models" prop="relationships"
                            :error="getError('relationships')"
                            class="el-form-item-as-label">
              </el-form-item>
            </multiple-select-extended>
          </el-row>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import MapService from '@/services/MapService'
  import ModelService from '@/services/ModelService'
  import FormCommons from '@/components/mixins/FormCommons'
  import MultipleSelect from '@/components/Selectors/MultipleSelect'
  import MultipleSelectExtended from '@/components/Selectors/MultipleSelectWithAdditionalFields'
  import SingleFileSelector from '@/components/App/Attachment/SingleFileSelector'
  import ModelCommons from '@/components/App/Model/ModelCommons'
  import ValidationError from '@/errors/ValidationError'

  export default {
    name: 'model-base-form',

    props: {
      entity: {
        type: Object,
        required: true
      },
      projectId: {
        type: [String, Number],
        required: true
      },
      deleteConfirmationCallback: {
        type: Function
      },
      modelLinks: Array
    },

    mixins: [CommonValidations, FormCommons, ModelCommons],

    components: {MultipleSelect, SingleFileSelector, MultipleSelectExtended},

    data() {
      const mapService = new MapService()
      const modelService = new ModelService()

      return {
        processingSessions: [],
        mapService: mapService,
        modelService: modelService,
        rules: {
          label: [
            {
              required: true,
              message: 'Please provide a model label',
              trigger: 'blur'
            }
          ],
          resolution: [{
            required: true,
            validator: this.isGreaterThan('Resolution', 0),
            trigger: 'blur'
          }],
          attachmentFileName: [{
            required: true,
            message: 'Please attach the model file (typically in PDB or mmCIF format)',
            trigger: 'change'
          }],
        },
        allMapsForProject: [],
        relationshipsConfig: [
          {
            type: 'select',
            propertyName: 'relationshipType',
            listItems: this.getRelationshipTypes(),
            label: 'Relationship type',
            required: true,
            errorMessage: 'Please choose relationship type',
            placeholder: 'Choose relationship type',
            validator: this.validateRelationshipType,
            onchange: this.validateRelationshipTypes
          },
          {
            type: 'select-with-date',
            notUnique: true,
            propertyName: 'relatedModel',
            label: 'Model',
            required: true,
            errorMessage: 'Please choose related model',
            placeholder: 'Choose related model',
            onchange: this.validateRelationshipTypes
          },
          {
            type: 'textarea',
            propertyName: 'comment',
            label: 'Comment',
            required: false,
            placeholder: 'Comment',
            validator: this.validateCommentLength
          },
        ],
        allModels: []
      }
    },

    methods: {
      loadMaps() {
        return this.mapService.listEntitiesByProject(this.projectId)
          .then(result => {
            this.allMapsForProject = result.data || []
          })
          .catch(error => this.$log.error(error))
      },

      loadModels() {
        this.modelService.listEntitiesByProject(this.projectId).then(
          (result) => {
            this.allModels = (result.data || []).filter(model => model.id !== this.entity.id)
          }).catch(error => this.$log.error(error))
      },

      isNewEntity() {
        return this.$route.name === 'model-new'
      },

      validateRelationshipType(relation) {
        return (rule, value, callback) => {
          if (_.isEmpty(value)) {
            return callback(new ValidationError('Please choose relationship type'))
          }

          let itemsWithGivenRelation = this.entity.relationships.filter(singleEntity => {
            return (value === singleEntity.relationshipType && _.get(singleEntity, 'relatedModel.id')
              && _.get(singleEntity, 'relatedModel.id') === _.get(relation, 'relatedModel.id'))
          })

          if (itemsWithGivenRelation.length > 1) {
            return callback(new ValidationError('Relation exists for model '
              + _.get(relation, 'relatedModel.label')))
          }

          return callback()
        }
      }
    },

    watch: {
      'entity.id': function () {
        this.loadMaps()
      },

      'entity.maps': function () {
        this.$nextTick().then(() => {
          this.$refs['mainForm'].validateField('maps')
        })
      },
      'entity.attachmentFileName': function () {
        this.$refs['attachmentFileName'].validate()
      },
      'modelLinks': function (modelLinks) {
        this.entity.relationships = this.convertModelLinksToRelationships(modelLinks, this.entity)
      }
    },

    created() {
      this.initOriginalEntity('entity')
    },

    mounted() {
      this.loadMaps()
    }
  }
</script>

<style scoped>
  .model-base-form__map-selector-container {
    width: 100%
  }
</style>
