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

import LigandRegistrationService from '@/components/App/Ligand/registration_system_ext/LigandRegistrationService'
import EntityNotFoundError from '@/errors/EntityNotFoundError'
import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

const ligandRegistrationService = new LigandRegistrationService()

export default {

  name: 'ligand-registration-commons',

  data () {
    return {
      ligandRegistrationConfig: ligandRegistrationConfig()
    }
  },

  computed: {
    invalidConceptIdMessage () {
      return 'Invalid ' + this.ligandRegistrationConfig.conceptIdLabel
    },

    invalidBatchIdMessage () {
      return 'Invalid ' + this.ligandRegistrationConfig.batchIdLabel
    }
  },

  methods: {
    callLigandRegistration (conceptId, batchId, pictureNotRefreshed) {
      if (!pictureNotRefreshed) {
        this.$events.$emit('ligandRegistrationProcessing', conceptId, batchId)
      }

      return new Promise((resolve, reject) => {
        (conceptId ? ligandRegistrationService.findLigand(conceptId) : ligandRegistrationService.findLigandByBatchId(batchId))
          .then(response => resolve(this.interpretLigandRegistrationResult(conceptId, batchId, response)))
          .catch(error => {
            if (error instanceof EntityNotFoundError) {
              this.$events.$emit('conceptIdentifierInvalid', conceptId, batchId,
                conceptId ? this.invalidConceptIdMessage : this.invalidBatchIdMessage)
              reject({conceptId: conceptId, batchId: batchId})
              return
            }

            const errorMessage = this._.get(error, 'response.data')
            this.$events.$emit('ligandRegistrationServerError', conceptId, batchId, errorMessage)
            reject({message: errorMessage})
          })
      })
    },

    interpretLigandRegistrationResult (conceptId, batchId, response) {
      return new Promise((resolve, reject) => {
          if (!(response && response.data && response.data['conceptId'])) {
            reject({
              conceptId: conceptId,
              batchId: batchId
            })
            this.$events.$emit('conceptIdentifierInvalid', conceptId, batchId,
              conceptId ? this.invalidConceptIdMessage : this.invalidBatchIdMessage)
          } else if (conceptId && this.ligand.conceptId && conceptId !== this.ligand.conceptId
            || batchId && this.ligand.batchId && batchId !== this.ligand.batchId) {
            // Prevent overlapping validations when one validation has not finished and another one stared.
            this.$log.debug('wrong id ' + conceptId)
            reject()
          } else if (!this._.get(response, 'data.molecularWeight')) {
            this.$events.$emit('ligandHasNoMolecularWeight', conceptId, batchId)
            reject()
          } else {
            resolve(response.data)
            this.$events.$emit('ligandRegistrationLoaded', response.data)
          }
        }
      )
    }
  }
}
