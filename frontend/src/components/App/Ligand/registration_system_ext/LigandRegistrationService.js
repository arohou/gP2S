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

/**
 * Integration with CONCEPT, an external system.
 *
 * @author Cezary Krzyżanowski on 31.07.2017.
 */
import { HTTP } from '@/utils/http-common'

const LIGAND_REGISTRATION = 'ligand-registration/'

export default class LigandRegistrationService {

  constructor () {
    this.RESOURCE = process.env.LIGAND_REGISTRATION_SYSTEM_URL + '/#'
  }

  compoundUrl (conceptId) {
    return this.RESOURCE + '/dashboard/details/' + encodeURI(conceptId)
  }

  findLigand (conceptId) {
    return HTTP.get(LIGAND_REGISTRATION + 'findByConceptId/' + encodeURI(conceptId))
  }

  findLigandByBatchId (batchId) {
    return HTTP.get(LIGAND_REGISTRATION + 'findByBatchId/' + encodeURI(batchId))
  }

  findBase64LigandPicture (batchId, width, height) {
    return HTTP.get(LIGAND_REGISTRATION + 'findBase64LigandRegistrationPicture/' + encodeURI(batchId) + '?width=' + width + '&height=' + height)
  }
}
