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

import SampleService from '@/services/SampleService'

import HandleOptimisticLocking from '@/components/mixins/HandleOptimisticLocking'
import CommonAlerts from '@/components/mixins/CommonAlerts'
import CommonValidations from '@/components/mixins/CommonValidations'
import FormCommons from '@/components/mixins/FormCommons'
import FilterCommons from '@/components/mixins/FilterCommons'

import ComponentType from '@/components/App/Sample/ComponentType'

const service = new SampleService()

export default {

  props: ['id'],

  mixins: [
    HandleOptimisticLocking, CommonAlerts, FormCommons, FilterCommons, CommonValidations
  ],

  data () {
    return {
      sample: {
        label: null,
        incubationTime: null,
        incubationTemperature: null,
        otherBufferComponents: null,
        protocolDescription: null,
        availableForGridMaking: true,
        ligandComponent: [],
        proteinComponent: [],
        components: []
      }
    }
  },

  methods: {
    getDefaultComponent () {
      return {
        id: null,
        finalConcentration: null,
        finalConcentrationInMgPerMl: null, //to render finalConcentrationInMgPerMl at start
        finalDrop: false,
        type: null,
        aliquot: {
          label: null
        }
      }
    },

    mapComponents: function (sample) {
      return [
        ...(sample.proteinComponent || []).map(x => {
          x.type = ComponentType.PUR
          x.finalConcentrationInMgPerMl = null //to render finalConcentrationInMgPerMl at start
          return x
        }),
        ...(sample.ligandComponent || []).map(x => {
          x.type = ComponentType.G
          return x
        })
      ].sort((c1, c2) => c1.aliquot.label.localeCompare(c2.aliquot.label))
    },

    fetchSample (slugOrId) {
      this.$log.debug('Fetching data of sample id: ', slugOrId)
      return service.getSampleBy(slugOrId)
        .then(result => {
          result.data.components = this.mapComponents(result.data)
          this.sample = result.data
        })
        .catch(error => this.$log.error(error))
    }
  }
}
