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

import { HTTP } from '@/utils/http-common'
import AttachmentService from '@/services/AttachmentService'

const URI = 'model/'
const URI_MODEL_LINK = 'model-link/'

export default class ModelService extends AttachmentService {

  constructor () {
    super(URI)
  }

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

  saveEntity (entity) {
    return HTTP.put(URI, entity)
  }

  saveModelLinks (slugOrId, entity) {
    const body = JSON.stringify(entity)
    return HTTP.post(URI_MODEL_LINK + slugOrId, body)
  }

  getModelLinks (slugOrId) {
    return HTTP.get(URI_MODEL_LINK + slugOrId)
  }
}
