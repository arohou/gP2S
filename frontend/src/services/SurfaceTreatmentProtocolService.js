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
 * Created by Przemyslaw Stankowski on 05.10.2017.
 */

import { HTTP } from '@/utils/http-common'
import Polarity from '@/components/App/Admin/SurfaceTreatmentProtocol/Polarity'

const URI = 'surface-treatment-protocol/'

function _decodePolarity (result) {
  if (result.data != null && typeof result.data[Symbol.iterator] === 'function') {
    result.data.forEach((element, index) => {
      if (element.polarity) {
        result.data[index].polarity = Polarity.enumValueOf(element.polarity)
      }
    })
  } else if (result.data && result.data.polarity) {
    result.data.polarity = Polarity.enumValueOf(result.data.polarity)
  }
  return result
}

function _encodePolarity (entity) {
  let result = Object.assign({}, entity)
  result.polarity = entity.polarity.name
  return result
}

export default class SurfaceTreatmentProtocolService {

  getEntityBySlugOrId (slugOrId) {
    return HTTP.get(URI + slugOrId).then(result => {
      return _decodePolarity(result)
    })
  }

  listEntities () {
    return HTTP.get(URI).then(result => {
      return _decodePolarity(result)
    })
  }

  createEntity (entity) {
    const body = JSON.stringify(_encodePolarity(entity))
    return HTTP.post(URI, body)
  }

  saveEntity (entity) {
    return HTTP.put(URI, _encodePolarity(entity))
  }
}
