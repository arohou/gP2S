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

import Service from '@/services/ProcessingSessionService'

import HandleOptimisticLocking from '@/components/mixins/HandleOptimisticLocking'
import CommonAlerts from '@/components/mixins/CommonAlerts'
import CommonValidations from '@/components/mixins/CommonValidations'
import FormCommons from '@/components/mixins/FormCommons'
import FilterCommons from '@/components/mixins/FilterCommons'

export default {

  props: ['id'],

  mixins: [
    HandleOptimisticLocking, CommonAlerts, FormCommons, FilterCommons, CommonValidations
  ],

  data () {
    return {
      entity: {
        label: null,
        numberOfMicrographs: null,
        numberOfPickedParticles: null,
        basePathOfProcessingDirectory: null,
        microscopySessions: [],
        usedImageProcessingSoftware: []
      },
      _service: null
    }
  },

  methods: {
    fetchEntity (slugOrId) {
      // Get list of project that this entity belongs to (used in switching between projects).
      this.$log.debug('Fetching data of processing session id: ', slugOrId)
      return this._service.getEntityBy(slugOrId)
        .then(result => this.entity = result.data)
        .catch(error => this.$log.error(error))
    },
  },

  created () {
    this._service = new Service()
  }
}
