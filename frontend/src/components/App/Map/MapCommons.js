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
    convertMapLinksToRelationships (mapLinks, map) {
      if (!mapLinks || !map.id) {
        return
      }

      let relationships = []

      mapLinks.forEach((mapLink) => {
        let isParent = map.id === mapLink.parentMap.id

        let relationship = {
          relationshipType: (isParent ? 'HAS_' : 'IS_') + mapLink.relationshipType,
          relatedMap: isParent ? mapLink.childMap : mapLink.parentMap,
          comment: mapLink.comment
        }

        relationships.push(relationship)
      })

      return relationships
    },

    getRelationshipTypes () {
      return [
        {value: 'HAS_FILTERED', label: 'has filtered version'},
        {value: 'IS_FILTERED', label: 'is filtered version of'},

        {value: 'HAS_MASKED', label: 'has masked version'},
        {value: 'IS_MASKED', label: 'is masked version of'},

        {value: 'HAS_RESAMPLED', label: 'has resampled version'},
        {value: 'IS_RESAMPLED', label: 'is resampled version of'},

        {value: 'HAS_REFINED', label: 'has refined version'},
        {value: 'IS_REFINED', label: 'is refined version of'},

        {value: 'HAS_LOCAL_RESOLUTION', label: 'has local resolution map'},
        {value: 'IS_LOCAL_RESOLUTION', label: 'is local resolution map of'}
      ]
    }
  }
}
