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
  <view-comment-base entityType="model" :entity="entity" :icon="icon">
    <el-row class="view__metadata__row">
      <el-col :sm="8">
        <el-row class="view__metadata__description-wrapper">
          <label class="view__metadata__label">Model file</label>
          <div class="view__metadata__value">
            <a v-if="entity.attachmentFileName"
               class="view__metadata__link"
               :href="downloadUrl">{{ entity.attachmentFileName }}</a>


            <span v-else>—</span>
          </div>
        </el-row>
      </el-col>
      <el-col :sm="8">
        <el-row>
          <label class="view__metadata__label">Resolution</label>
          <div class="view__metadata__value">{{ entity.resolution | formatUnit('Å') }}</div>
        </el-row>
      </el-col>
    </el-row>

    <el-row :sm="24" class="view__metadata__row relationships-section">
      <el-col :sm="24">
        <el-row>
          <label class="view__metadata__label">Related models</label>

          <el-row v-if="!_.isEmpty(relationshipsGrouped)">
            <el-row v-for="(relationship, index) in relationshipsGrouped" :key="index" class="small-margin">
              <el-row :sm="24" class="sub-value">
                {{ relationship.relationshipTypeLabel | formatValue }}
              </el-row>
              <el-row :sm="24" class="sub-label"
                      v-for="(relatedModel, index2) in relationship.relatedModels" :key="index2">
                <el-col class="value-separator">
                  <router-link class="list-view__link-label"
                               :to="{ name: 'model-view', params: {id: relatedModel.slug} }"
                               :id="'image_processing-'  + index + '-label-link'">
                    {{ relatedModel.label }}
                  </router-link>
                  <span v-if="relatedModel.comment">
              {{ '&nbsp;&nbsp;(' + relatedModel.comment + ')' }}
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
  import Commons from '@/components/App/Model/Commons'
  import Icon from '@/assets/images/model.svg'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import ModelService from '@/services/ModelService'
  import RelatedEntitiesList from '@/components/App/Model/RelatedEntitiesList'

  export default {
    name: 'model-view',

    props: ['id'],

    mixins: [
      Commons, ProjectResidentCommons
    ],

    created () {
      this._modelService = new ModelService()
    },

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
            this._modelService.getModelLinks(this.entity.id).then(result => {
              this.createGroupedRelationships(result.data)
            }).catch(error => this.$log.error(error))
          })
      },

      createGroupedRelationships: function (modelLinks) {
        this.relationshipsGrouped = []

        let relationships = this.convertModelLinksToRelationships(modelLinks, this.entity)

        let relationshipTypes = this.getRelationshipTypes()

        relationshipTypes.forEach((relationshipType) => {
          let relatedModels = relationships.filter(
            relationship => relationship.relationshipType === relationshipType.value)
            .map(relationship => Object.assign({comment: relationship.comment}, relationship.relatedModel))

          if (!_.isEmpty(relatedModels)) {
            this.relationshipsGrouped.push({
              relationshipTypeLabel: relationshipType.label,
              relatedModels: relatedModels.sort((model1, model2) => model2.createdDate - model1.createdDate)
            })
          }
        })
      }
    },

    computed: {
      downloadUrl: function () {
        return this._modelService.getDownloadURL(this.entity.attachmentMongoId)
      }
    },

    watch: {
      'id' (id) {
        this.loadEntity(id)
      }
    },

    mounted () {
      this.loadEntity(this.id)
      this.$events.on('actionEdit', () => this.actionEdit('model', this.id))
      this.$events.on('actionCopy', () => this.actionCopy('model', this.id))
    },

    beforeDestroy () {
      this.$events.off('actionEdit')
      this.$events.off('actionCopy')
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

  .relationships-section {
    margin-bottom: 1.538rem;
  }
</style>
