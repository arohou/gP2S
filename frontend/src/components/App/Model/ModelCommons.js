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

export default {
  methods: {
    convertModelLinksToRelationships (modelLinks, model) {
      if (!modelLinks || !model.id) {
        return
      }

      let relationships = []

      modelLinks.forEach((modelLink) => {
        let isParent = model.id === modelLink.parentModel.id

        let relationship = {
          relationshipType: (isParent ? 'HAS_' : 'IS_') + modelLink.relationshipType,
          relatedModel: isParent ? modelLink.childModel : modelLink.parentModel,
          comment: modelLink.comment
        }

        relationships.push(relationship)
      })

      return relationships
    },

    getRelationshipTypes () {
      return [
        {value: 'HAS_REFINED', label: 'has refined version'},
        {value: 'IS_REFINED', label: 'is refined version of'},
      ]
    }
  }
}
