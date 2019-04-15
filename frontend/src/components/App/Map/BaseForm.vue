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
  <div class="map-base-form base-form">
    <el-form class="map-base-form__form" ref="mainForm" labelPosition="top" :model="entity" :rules="rules">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="8">
          <el-form-item label="Map label" prop="label" :error="getError('label')">
            <el-input id="label" class="map-base-form__form__label-input" v-model="entity.label"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Pixel size (Å)" prop="pixelSize" :error="getError('pixelSize')">
            <el-input id="pixel-size" v-model="entity.pixelSize"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Isolevel for surface rendering" prop="isolevelForSurfaceRendering"
                        :error="getError('isolevelForSurfaceRendering')">
            <el-input id="isolevel-for-surface-rendering" v-model="entity.isolevelForSurfaceRendering"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="8">
          <el-form-item label="Processing session" prop="processingSession" :error="getError('processingSession')">

            <el-select placeholder="Please enter a keyword" noDataText="No processing sessions available"
                       v-model="entity.processingSession" value-key="id" id="processing-session"
                       class="input-with-search">
              <el-option v-for="item in processingSessions"
                         :key="item.id" :value="item" :label="item.label">
                <div style="height: 3rem">
                  <div class="selector__item-title">{{item.label}}</div>
                  <div class="selector__item-datetime">created: {{ item.createdDate | moment('YYYY.MM.DD HH:mm') }}
                  </div>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Number of images" prop="numberOfImages" :error="getError('numberOfImages')">
            <el-input id="number-of-images" v-model="entity.numberOfImages"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Symmetry applied" prop="symmetryApplied" :error="getError('symmetryApplied')">
            <el-input id="symmetry-applied" v-model="entity.symmetryApplied"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="8">
          <el-form-item label="Resolution in best parts (Å)" prop="estimatedResolutionInBestParts"
                        :error="getError('estimatedResolutionInBestParts')">
            <el-input id="estimated-resolution-in-best-parts" v-model="entity.estimatedResolutionInBestParts"
                      @blur="onChangeEstimatedResolutionInBestParts()"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Average resolution (Å)" prop="estimatedResolutionInAverageParts"
                        :error="getError('estimatedResolutionInWorstParts')">
            <el-input id="estimated-resolution-in-average-parts"
                      v-model="entity.estimatedResolutionInAverageParts"></el-input>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-form-item label="Resolution in worst parts (Å)" prop="estimatedResolutionInWorstParts"
                        :error="getError('estimatedResolutionInWorstParts')">
            <el-input id="estimated-resolution-in-worst-parts"
                      v-model="entity.estimatedResolutionInWorstParts"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :sm="16">
          <el-row>
            <el-form-item label="Description" prop="description" :error="getError('description')">
              <el-input type="textarea" v-model="entity.description"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <multiple-select-extended :selectedEntities="entity.relationships"
                                      entitiesName="relationships"
                                      :allEntities="allMaps"
                                      :config="relationshipsConfig"
                                      addNewLabel="Link to another map"
                                      :onRemove="validateRelationshipTypes">
              <el-form-item label="Related maps" prop="relationships"
                            :error="getError('relationships')"
                            class="el-form-item-as-label">
              </el-form-item>
            </multiple-select-extended>
          </el-row>
        </el-col>


        <el-col :xs="24" :sm="8">
          <el-form-item label="Map file" prop="attachmentFileName" :error="getError('attachmentFileName')"
                        ref="attachmentFileName">
            <single-file-selector ref="mapFile" :entity="entity"
                                  :attachmentService="_mapService"
                                  :delete-confirmation-callback="deleteConfirmationCallback"></single-file-selector>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import ValidationError from '@/errors/ValidationError'
  import FormCommons from '@/components/mixins/FormCommons'
  import SingleFileSelector from '@/components/App/Attachment/SingleFileSelector'
  import ProcessingSessionService from '@/services/ProcessingSessionService'
  import MapService from '@/services/MapService'
  import MultipleSelectExtended from '@/components/Selectors/MultipleSelectWithAdditionalFields'
  import MapCommons from '@/components/App/Map/MapCommons'

  export default {
    name: 'map-base-form',

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
      mapLinks: Array
    },

    mixins: [CommonValidations, FormCommons, MapCommons],

    components: {SingleFileSelector, MultipleSelectExtended},

    data () {
      return {
        test: [],
        _mapService: null,
        processingSessions: [],
        rules: {
          label: [
            {
              required: true,
              message: 'Please provide a map label',
              trigger: 'blur'
            }
          ],
          pixelSize: [{
            required: true,
            validator: this.isGreaterThan('Pixel size', 0),
            trigger: 'blur'
          }],
          isolevelForSurfaceRendering: [{
            required: true,
            validator: this.isNumber('Isolevel for surface rendering'),
            trigger: 'blur'
          }],
          processingSession: [{
            required: true,
            validator: this.isDefined('Processing session'),
            trigger: 'blur'
          }],
          numberOfImages: [{
            required: true,
            validator: this.isIntegerGreaterThan('Number of images', 0),
            trigger: 'blur'
          }],
          estimatedResolutionInBestParts: [{
            required: true,
            validator: this.isGreaterThan('Estimated resolution in best parts', 0),
            trigger: 'blur'
          }],
          estimatedResolutionInAverageParts: [{
            required: true,
            validator: this.isGreaterThan('Estimated resolution in average parts', 0)
          }, {
            required: true,
            validator: this.validateEstimatedResolutionInAverageParts(),
            trigger: 'blur'
          }],
          estimatedResolutionInWorstParts: [{
            required: true,
            validator: this.isGreaterThan('Estimated resolution in worst parts', 0)
          }, {
            required: true,
            validator: this.validateEstimatedResolutionInWorstParts(),
            trigger: 'blur'
          }],
          symmetryApplied: [{
            required: true,
            validator: this.validateSymmetryApplied(),
            trigger: 'blur'
          }],
          attachmentFileName: [{
            required: true,
            message: 'Please choose map file',
            trigger: 'change'
          }],
        },
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
            propertyName: 'relatedMap',
            label: 'Map',
            required: true,
            errorMessage: 'Please choose related map',
            placeholder: 'Choose related map',
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
        allMaps: []
      }
    },

    methods: {
      loadProcessingSessions () {
        this._processingSessionsService.listEntitiesByProject(this.projectId).then(result => {
          this.sortProcessingSessions(result.data)
          this.processingSessions = this.options(result.data, _.get(this, 'originalEntity.processingSession'), 'id')
        }).catch(error => this.$log.error(error))
      },

      validateEstimatedResolutionInWorstParts () {
        return (rule, value, callback) => {
          if (!value || value <= 0 || !this.isNumeric(value)) {
            callback(new ValidationError('Estimated resolution in worst parts should be a number greater than 0'))
          } else if (parseFloat(value) < parseFloat(this.entity.estimatedResolutionInBestParts)) {
            return callback(new ValidationError(
              'Estimated resolution in worst parts should be >= estimated resolution in best parts'))
          } else {
            return callback()
          }
        }
      },

      validateEstimatedResolutionInAverageParts () {
        return (rule, value, callback) => {
          if (!value || value <= 0 || !this.isNumeric(value)) {
            callback(new ValidationError('Estimated resolution in worst parts should be a number greater than 0'))
          } else if (parseFloat(value) < parseFloat(this.entity.estimatedResolutionInBestParts)) {
            return callback(new ValidationError(
              'Estimated resolution in average parts should be >= estimated resolution in best parts'))
          } else if (parseFloat(value) > parseFloat(this.entity.estimatedResolutionInWorstParts)) {
            return callback(new ValidationError(
              'Estimated resolution in average parts should be <= estimated resolution in worst parts'))
          } else {
            return callback()
          }
        }
      },

      validateSymmetryApplied () {
        return (rule, value, callback) => {
          if (_.isEmpty(value)) {
            return callback(new ValidationError('Symmetry applied should be provided'))
          }
          if (/^(C\d+|D\d+|O|T|I)$/.test(value)) {
            callback()
          } else {
            return callback(new ValidationError(
              'Symmetry applied should be in format C[d+] or D[d+] or O or T or I'))
          }
        }
      },

      onChangeEstimatedResolutionInBestParts () {
        if (this.entity.estimatedResolutionInWorstParts) {
          this.$refs['mainForm'].validateField('estimatedResolutionInWorstParts')
        }
      },

      sortProcessingSessions (processingSessions) {
        if (!_.isEmpty(processingSessions)) {
          processingSessions.sort((a, b) => {
            return a.createdDate && b.createdDate && a.createdDate !== b.createdDate
              ? a.createdDate <= b.createdDate : a.label > b.label
          })
        }
      },

      loadMaps () {
        this._mapService.listEntitiesByProject(this.projectId).then(
          (result) => {
            this.allMaps = (result.data || []).filter(map => map.id !== this.entity.id)
          }).catch(error => this.$log.error(error))
      },

      validateRelationshipType (relation) {
        return (rule, value, callback) => {
          if (_.isEmpty(value)) {
            return callback(new ValidationError('Please choose relationship type'))
          }

          let itemsWithGivenRelation = this.entity.relationships.filter(singleEntity => {
            return (value === singleEntity.relationshipType && _.get(singleEntity, 'relatedMap.id')
              && _.get(singleEntity, 'relatedMap.id') === _.get(relation, 'relatedMap.id'))
          })

          if (itemsWithGivenRelation.length > 1) {
            return callback(new ValidationError('Relation exists for map '
              + _.get(relation, 'relatedMap.label')))
          }

          return callback()
        }
      }
    },

    watch: {
      'entity.id': function () {
        this.loadProcessingSessions()
        this.loadMaps()
      },
      'entity.attachmentFileName': function () {
        this.$refs['attachmentFileName'].validate()
      },
      'mapLinks': function (mapLinks) {
        this.entity.relationships = this.convertMapLinksToRelationships(mapLinks, this.entity)
      }
    },

    created () {
      this._processingSessionsService = new ProcessingSessionService()
      this.initOriginalEntity('entity')
      this._mapService = new MapService()
    },

    mounted () {
      this.loadProcessingSessions()
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .selector__item-title {
    font-size: 1.15rem;
    font-weight: bold;
    display: inline-flex;
  }

  .selector__item-datetime {
    color: $text-header-color--disabled;
    font-size: 0.9rem;
  }

  .el-select-dropdown__item {
    height: auto;
  }

  .el-select-dropdown__item.selected .selector__item-datetime {
    color: $text-gray-selected;
  }
</style>
