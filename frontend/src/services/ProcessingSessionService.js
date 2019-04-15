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
 * @author Przemysław Stankowski on 19.01.2018.
 */

import { HTTP } from '@/utils/http-common'

const URI = 'processing-session/'

export default class ProcessingSessionService {

  listEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/' + URI)
  }

  countEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/' + URI + 'count')
  }

  getEntityBy (slugOrId) {
    return HTTP.get(URI + slugOrId)
  }

  listEntityProjectsBy (slugOrId) {
    return HTTP.get(URI + slugOrId + '/projects')
  }

  createEntity (projectSlugOrId, entity) {
    const body = JSON.stringify(entity)
    return HTTP.post(URI + projectSlugOrId, body)
  }

  saveEntity (projectSlugOrId, entity) {
    return HTTP.put(URI + projectSlugOrId, entity)
  }

  getDefaultValues (projectSlugOrId) {
    return HTTP.get('default-value/processing_session/' + projectSlugOrId).then(result => {
      return result
    })
  }
}
