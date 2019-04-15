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

import ProteinRegistrationService from '@/components/App/Protein/registration_system_ext/ProteinRegistrationService'
import EntityNotFoundError from '@/errors/EntityNotFoundError'

const proteinRegistrationService = new ProteinRegistrationService()

export default {

  name: 'service-commons',

  methods: {
    callProteinRegistrationSystem (purId, currentProtein) {
      this.$events.$emit('proteinRegistrationSystemProcessing', purId)

      return new Promise((resolve, reject) => {
        proteinRegistrationService.findProteinRegistrationEntity(purId)
          .then(response => {
            resolve(this.interpretProteinRegistrationSystemResult(purId, currentProtein, response))
          })
          .catch(error => {
            if (error instanceof EntityNotFoundError) {
              this.$events.$emit('proteinRegistrationSystemPurInvalid', purId)
              reject({purId: purId})
              return
            }
            this.$events.$emit('proteinRegistrationSystemServerError', purId, error.response.data)
            reject({message: error.response.data})
          })
      })
    },

    interpretProteinRegistrationSystemResult (purId, currentProtein, response) {
      return new Promise((resolve, reject) => {
        if (!(response && response.data && response.data['purId'])) {
          reject({purId: purId})
          this.$events.$emit('proteinRegistrationSystemPurInvalid', purId)
        } else if (currentProtein.purificationId && purId !== currentProtein.purificationId) {
          // Prevent overlapping validations when one validation has not finished and another one stared
          this.$log.debug('wrong id ' + purId)
          reject()
        } else {
          if (!response.data.molecularWeightDa) {
            this.$events.$emit('purHasNoMolecularWeight', purId)
            reject()
            return
          }
          this.$events.$emit('proteinRegistrationSystemPurLoaded', purId, response.data['purName'], response.data['batchConcentration'],
            response.data['molecularWeightDa'], response.data['bufferComposition'], response.data['conceptUID'])
          resolve()
        }
        this.$log.debug(JSON.stringify(response))
      })
    }
  }
}
