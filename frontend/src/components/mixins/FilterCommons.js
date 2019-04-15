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

import moment from 'moment'
import _ from 'lodash'

export default {

  filters: {
    formatLabel(entity, defaultText = '—') {
      if (entity && entity.label) {
        return entity.label
      }
      return defaultText
    },

    formatValue(value, defaultText = '—') {
      return value || value === 0 ? value : defaultText
    },

    formatUnit(value, unit, space = true) {
      if (!value && value !== 0) {
        return '—'
      } else if (!unit) {
        return value
      }

      if (space) {
        return value + ' ' + unit
      } else {
        return value + unit
      }
    },

    formatBoolean(value) {
      return value === true ? 'Yes' : 'No'
    },

    formatAsLocalizedNumber(value) {
      return !_.isNil(value) ? value.toLocaleString() : '—'
    },

    round(value, decimals) {
      return !value && value !== 0 ? null : parseFloat((value || 0).toFixed(decimals))
    },

    hashPrefix(value) {
      if (!value) {
        return ''
      }

      return '#' + value
    },

    formatDate(value) {
      const momentDate = moment(value)
      if (momentDate.isValid()) {
        return momentDate.format('lll')
      } else {
        return '—'
      }
    },

    formatArray(array, ...filters) {
      if (_.isEmpty(array)) {
        return '—'
      }

      return array
        .map(value =>
          filters.reduce((result, filter) => filter(result), value))
        .map(value => !value ? '—' : value)
        .join(', ')
    },
  }
}
