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

import SettingsService from '@/services/SettingsService'
import Token from '@/utils/MicroscopySessionToken'

import HandleOptimisticLocking from '@/components/mixins/HandleOptimisticLocking'
import CommonAlerts from '@/components/mixins/CommonAlerts'
import CommonValidations from '@/components/mixins/CommonValidations'
import FormCommons from '@/components/mixins/FormCommons'
import FilterCommons from '@/components/mixins/FilterCommons'

export default {

  mixins: [
    HandleOptimisticLocking, CommonAlerts, FormCommons, FilterCommons, CommonValidations
  ],

  data () {
    return {
      entity: {
        patternForDataStorageDirectoryName: ([
          Token.MICROSCOPY_START_DATE,
          Token.PROJECT_LABEL,
          Token.MICROSCOPE_LABEL,
          Token.GRID_ID,
          Token.MICROSCOPY_SESSION_ID
        ].join('_'))
      }
    }
  },

  watch: {
    'entity': function (newValue) {
      this.addTitleToEntityIfNeeded(newValue)
    }
  },

  methods: {
    fetchEntity () {
      return this._settingsService.get()
        .then(result => {
          this.entity = result.data
        })
        .catch(error => this.$log.error(error))
    },

    addTitleToEntityIfNeeded (entity) {
      entity.label = _.get(entity, 'label', 'Settings')
    }
  },

  created () {
    this._settingsService = new SettingsService()
  }
}
