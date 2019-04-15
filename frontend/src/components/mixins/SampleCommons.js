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

import _ from 'lodash'

export default {
  methods: {

    findSampleInArray (slugOrId, samples) {
      if (!slugOrId || _.isEmpty(samples)) {
        return null
      }

      return samples.find((aSample) => this.doesSampleHaveSlugOrId(slugOrId, aSample))
    },

    doesSampleHaveSlugOrId (slugOrId, sample) {
      if (sample.id === parseInt(slugOrId) || sample.slug === slugOrId) {
        return true
      }

      return false
    }
  }
}

