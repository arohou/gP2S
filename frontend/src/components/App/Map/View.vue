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
  <view-comment-base entityType="map" :entity="entity" :icon="icon">
    <el-row class="view__metadata__row">
      <el-col :sm="6">
        <el-row class="view__metadata__description-wrapper">
          <label class="view__metadata__label">Map file</label>
          <div class="view__metadata__value">
            <a v-if="entity.attachmentFileName"
               class="view__metadata__link"
               :href="downloadUrl">{{ entity.attachmentFileName }}</a>
            <span v-else>—</span>
          </div>
        </el-row>
      </el-col>
      <el-col :sm="6">
        <el-row>
          <label class="view__metadata__label">Pixel size</label>
          <div class="view__metadata__value">{{ entity.pixelSize | formatUnit('Å') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="6">
        <el-row>
          <label class="view__metadata__label">Isolevel for surface rendering</label>
          <div class="view__metadata__value">{{ entity.isolevelForSurfaceRendering | formatValue }}</div>
        </el-row>
      </el-col>
      <el-col :sm="6">
        <el-row class="base-path">
          <label class="view__metadata__label">Processing session</label>
          <div class="view__metadata__value">
            <router-link
              class="view__metadata__link"
              :to="{ name: 'processing_session-view', params: {projectId: projectId, id: entity.processingSession ? entity.processingSession.slug : ''}}"
            >{{ entity.processingSession | formatLabel }}
            </router-link>
          </div>
        </el-row>
      </el-col>
    </el-row>
    <el-row class="view__metadata__row">
      <el-col :sm="6">
        <el-row class="base-path">
          <label class="view__metadata__label">Number of images</label>
          <div class="view__metadata__value">{{ entity.numberOfImages | formatAsLocalizedNumber }}</div>
        </el-row>
      </el-col>
      <el-col :sm="6">
        <el-row class="base-path">
          <label class="view__metadata__label">Symmetry applied</label>
          <div class="view__metadata__value">{{ entity.symmetryApplied | formatValue }}</div>
        </el-row>
      </el-col>
      <el-col :sm="6"/>
      <el-col :sm="6"/>
    </el-row>
    <el-row class="view__metadata__row">
      <el-col :sm="6">
        <el-row>
          <label class="view__metadata__label">Resolution in best parts</label>
          <div class="view__metadata__value">{{ entity.estimatedResolutionInBestParts | formatUnit('Å') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="6">
        <el-row>
          <label class="view__metadata__label">Average resolution</label>
          <div class="view__metadata__value">{{ entity.estimatedResolutionInAverageParts | formatUnit('Å') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="6">
        <el-row>
          <label class="view__metadata__label">Resolution in worst parts</label>
          <div class="view__metadata__value">{{ entity.estimatedResolutionInWorstParts | formatUnit('Å') }}</div>
        </el-row>
      </el-col>
      <el-col :sm="6"/>
    </el-row>
    <el-row class="view__metadata__row">
      <el-col :sm="24">
        <el-row class="view__metadata__description-wrapper">
          <label class="view__metadata__label">Description</label>
          <div class="view__metadata__value">{{ entity.description | formatValue }}</div>
        </el-row>
      </el-col>
    </el-row>

    <el-row :sm="24" class="view__metadata__row">
      <el-col :sm="24">
        <el-row>
          <label class="view__metadata__label">Related maps</label>

          <el-row v-if="!_.isEmpty(relationshipsGrouped)" class="related-maps-separator">
            <el-row v-for="(relationship, index) in relationshipsGrouped" :key="index" class="small-margin">
              <el-row :sm="24" class="sub-value">
                {{ relationship.relationshipTypeLabel | formatValue }}
              </el-row>
              <el-row :sm="24" class="sub-label"
                      v-for="(relatedMap, index2) in relationship.relatedMaps" :key="index2">
                <el-col class="value-separator">
                  <router-link class="list-view__link-label" :to="{ name: 'map-view', params: {id: relatedMap.slug} }"
                               :id="'image_processing-'  + index + '-label-link'">
                    {{ relatedMap.label }}
                  </router-link>
                  <span v-if="relatedMap.comment">
                  {{ '&nbsp;&nbsp;(' + relatedMap.comment + ')' }}
                </span>
                </el-col>
              </el-row>
            </el-row>
          </el-row>
          <el-row v-else class="view__metadata__value">
            <span>—</span>
          </el-row>
        </el-row>
      </el-col>
    </el-row>

    <el-row slot="additional-content">
      <el-row class="related-entities-report">
        <related-entities-list :entity="entity" :projectId="projectId"/>
      </el-row>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Map/Commons'
  import Icon from '@/assets/images/map.svg'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import RelatedEntitiesList from '@/components/App/Map/RelatedEntitiesList'

  export default {
    name: 'map-view',

    props: ['id'],

    mixins: [
      Commons, ProjectResidentCommons
    ],

    components: {
      ViewCommentBase, RelatedEntitiesList
    },

    data () {
      return {
        icon: Icon,
        relationshipsGrouped: []
      }
    },

    methods: {
      loadEntity: function (id) {
        this.fetchEntity(id)
          .then(() => {
            this._service.getMapLinks(this.entity.id).then(result => {
              this.createGroupedRelationships(result.data)
            }).catch(error => this.$log.error(error))
          })
      },

      createGroupedRelationships: function (mapLinks) {
        this.relationshipsGrouped = []

        let relationships = this.convertMapLinksToRelationships(mapLinks, this.entity)

        let relationshipTypes = this.getRelationshipTypes()

        relationshipTypes.forEach((relationshipType) => {
          let relatedMaps = relationships.filter(
            relationship => relationship.relationshipType === relationshipType.value)
            .map(relationship => Object.assign({comment: relationship.comment}, relationship.relatedMap))

          if (!_.isEmpty(relatedMaps)) {
            this.relationshipsGrouped.push({
              relationshipTypeLabel: relationshipType.label,
              relatedMaps: relatedMaps.sort((map1, map2) => map2.createdDate - map1.createdDate)
            })
          }
        })
      },

      initEvents () {
        this.$events.on('actionEdit', () => this.actionEdit('map', this.id))
        this.$events.on('actionCopy', () => this.actionCopy('map', this.id))
      },

      removeEvents () {
        this.$events.off('actionEdit')
        this.$events.off('actionCopy')
      }
    },

    computed: {
      downloadUrl: function () {
        return this._service.getDownloadURL(this.entity.attachmentMongoId)
      }
    },

    watch: {
      'id' (id) {
        this.loadEntity(id)
      }
    },

    mounted () {
      this.loadEntity(this.id)
      this.initEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .sub-label {
    color: $text-header-color--disabled;
  }

  .sub-value {
    font-weight: bold;
  }

  .small-margin {
    margin-top: 0.7rem;
  }

  .value-separator {
    margin-top: 0.2rem;
  }

  .related-maps-separator {
    margin-bottom: 1.538rem;
  }
</style>

