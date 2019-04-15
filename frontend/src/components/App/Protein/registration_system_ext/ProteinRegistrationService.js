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
 * Integration with Protein registration system, an external system.
 *
 * @author Cezary Krzyżanowski on 31.07.2017.
 */

import { HTTP } from '@/utils/http-common'

const PROTEIN_REGISTRATION_SYSTEM_URL = 'protein-registration/'

export default class ProteinRegistrationService {

  constructor () {
    this.RESOURCE = process.env.PROTEIN_REGISTRATION_SYSTEM_URL + '/#'
  }

  purUrl (purId) {
    return this.RESOURCE + '/search/entity/' + encodeURI(purId)
  }

  findProteinRegistrationEntity (purId) {
    return HTTP.get(PROTEIN_REGISTRATION_SYSTEM_URL + 'find/' + encodeURI(purId))
  }
}
