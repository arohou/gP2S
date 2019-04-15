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

  props: ['projectId'],

  data () {
    return {
      originalEntity: null
    }
  },

  methods: {
    initOriginalEntity (propertyName, immediate = false) {
      this.$log.debug('Watching for initial value of', propertyName)
      const unwatch = this.$watch(propertyName, function (newValue) {
        if (this.originalEntity === null && newValue !== null && newValue !== undefined) {
          // By design this is not a reactive data, i.e. it is not declared in data and is not
          // set here by this.$set. This is to make it immutable.
          this.originalEntity = _.cloneDeep(newValue)
          this.$log.debug('originalEntity set to:', this.originalEntity)
        }

        // Always try to unwatch. Unwatch can be undefined when called from immediate
        if (typeof unwatch === 'function') {
          unwatch()
          this.$log.debug('Stopped watching', propertyName)
        }
      }, {
        immediate: immediate
      })
    },

    /*
    * Create array of elements that contain all elements pass as options + current as first element.
    * filterCallback function may take control of filtering. String may be pass as filterCallback. In that case
    * string will be used to take object property values and that values will be used to filter duplicates
    * */
    options (options, current, filterCallback) {
      let res = [].concat(options) // Even if it's a single value it'll become an array.
      if (current !== null && typeof current !== 'undefined') {
        res = [current].concat(res)
      }
      return _.uniqBy(res, filterCallback)
    },

    openViewForm (routeName, data, useSlug) {
      this.$router.push({
        name: routeName + '-view',
        params: {projectId: this.projectId, id: data.slug}
      })
    },

    actionEdit (routeName, id) {
      this.$router.push({
        name: routeName + '-edit',
        params: {projectId: this.projectId, id: id}
      })
    },

    actionCopy (routeName, id) {
      this.$router.push({
        name: routeName + '-copy',
        params: {projectId: this.projectId, id: id}
      })
    },

    actionCancel () {
      if (this.isDialog) {
        this.$events.$emit('hideDialog') // Close a dialog.
      } else {
        this.$router.go(-1) // Back browser action.
      }
    },

    defaultNewLabel (label) {
      return label + ' ' + this.timestampForLabel()
    },

    timestampForLabel () {
      return moment().format('YY-MM-DD HH:mm')
    }
  }
}
