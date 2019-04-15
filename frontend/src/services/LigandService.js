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
 * @author Cezary Krzyżanowski on 02.08.2017.
 */

import { HTTP } from '@/utils/http-common'

const URI = 'ligand/'

export default class LigandService {

  listEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/' + URI)
  }

  countEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/' + URI + 'count')
  }

  getLigandBy (slugOrId) {
    return HTTP.get(URI + slugOrId)
  }

  getLigandProjectsBy (slugOrId) {
    return HTTP.get(URI + slugOrId + '/projects')
  }

  createLigand (projectSlugOrId, ligand) {
    const body = JSON.stringify(Object.assign({}, ligand))
    return HTTP.post(URI + projectSlugOrId, body)
  }

  saveLigand (ligand) {
    return HTTP.put(URI, Object.assign({}, ligand))
  }

  findRecentAvailableLigand (conceptId) {
    return HTTP.get(URI + 'findRecentAvailableLigand/' + conceptId)
  }
}
