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

import Service from '@/services/ModelService'

import HandleOptimisticLocking from '@/components/mixins/HandleOptimisticLocking'
import CommonAlerts from '@/components/mixins/CommonAlerts'
import CommonValidations from '@/components/mixins/CommonValidations'
import FormCommons from '@/components/mixins/FormCommons'
import FilterCommons from '@/components/mixins/FilterCommons'
import ModelCommons from '@/components/App/Model/ModelCommons'

export default {

  props: ['id'],

  mixins: [
    HandleOptimisticLocking, CommonAlerts, FormCommons, FilterCommons, CommonValidations, ModelCommons
  ],

  data () {
    return {
      _service: null,
      entity: {
        label: null,
        resolution: null,
        maps: [],
        relationships: []
      }
    }
  },

  methods: {
    fetchEntity (slugOrId) {
      this.$log.debug('Fetching data of model id: ', slugOrId)
      return this._service.getEntityBy(slugOrId)
        .then(result => {
          this.entity = Object.assign({relationships: []}, result.data)
        })
        .catch(error => this.$log.error(error))
    },

    convertRelationshipsToModelLinks () {
      let modelLinks = []

      this.entity.relationships.forEach((relationship) => {
        let isParent = !!relationship.relationshipType.match('^HAS_.+')

        let modelLink = {
          relationshipType: relationship.relationshipType.replace('HAS_', '').replace('IS_', ''),
          parentModel: isParent ? this.entity : relationship.relatedModel,
          childModel: isParent ? relationship.relatedModel : this.entity,
          comment: relationship.comment
        }

        modelLinks.push(modelLink)
      })

      return modelLinks
    }
  },

  mounted () {
    const service = new Service()
    this._service = service
  }
}
