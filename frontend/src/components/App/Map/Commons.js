/*
 * Copyright 2018 Genentech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Service from '@/services/MapService'

import HandleOptimisticLocking from '@/components/mixins/HandleOptimisticLocking'
import CommonAlerts from '@/components/mixins/CommonAlerts'
import CommonValidations from '@/components/mixins/CommonValidations'
import FormCommons from '@/components/mixins/FormCommons'
import FilterCommons from '@/components/mixins/FilterCommons'
import MapCommons from '@/components/App/Map/MapCommons'

export default {

  props: ['id'],

  mixins: [
    HandleOptimisticLocking, CommonAlerts, FormCommons, FilterCommons, CommonValidations, MapCommons
  ],

  data () {
    return {
      _service: Object,
      entity: {
        label: null,
        pixelSize: null,
        isolevelForSurfaceRendering: null,
        processingSession: null,
        numberOfImages: null,
        estimatedResolutionInBestParts: null,
        estimatedResolutionInAverageParts: null,
        estimatedResolutionInWorstParts: null,
        symmetryApplied: 'C1',
        description: null,
        attachmentFileName: null,
        relationships: []
      }
    }
  },

  methods: {
    fetchEntity (slugOrId) {
      this.$log.debug('Fetching data of map id: ', slugOrId)
      return this._service.getEntityBy(slugOrId)
        .then(result => {
          this.entity = Object.assign({relationships: []}, result.data)
        })
        .catch(error => this.$log.error(error))
    },

    convertRelationshipsToMapLinks () {
      let mapLinks = []

      this.entity.relationships.forEach((relationship) => {
        let isParent = !!relationship.relationshipType.match('^HAS_.+')

        let mapLink = {
          relationshipType: relationship.relationshipType.replace('HAS_', '').replace('IS_', ''),
          parentMap: isParent ? this.entity : relationship.relatedMap,
          childMap: isParent ? relationship.relatedMap : this.entity,
          comment: relationship.comment
        }

        mapLinks.push(mapLink)
      })

      return mapLinks
    }
  },

  created () {
    this._service = new Service()
  }
}
