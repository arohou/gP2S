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
 * Created by Przemyslaw Stankowski on 19.07.2017.
 */

import { HTTP } from '@/utils/http-common'
import ConcentrationType from '@/components/App/ConcentrationType'
import _ from 'lodash'

const URI = 'protein/'

function _decodeEnums (entity) {
  const concentrationType = _.get(entity, 'concentration.concentrationType')
  if (concentrationType) {
    entity.concentration.concentrationType = ConcentrationType.enumValueOf(concentrationType)
  }
  return entity
}

function _encodeEnums (entity) {
  let result = Object.assign({}, entity)
  const concentrationType = _.get(result, 'concentration.concentrationType')
  if (concentrationType) {
    result.concentration = Object.assign({}, result.concentration)
    result.concentration.concentrationType = concentrationType.name
  }
  return result
}

function _cleanupDilutionOrConcentrationFactorFields (result) {
  const concentrationType = _.get(result, 'concentration.concentrationType')
  if (ConcentrationType.Dilution === concentrationType) {
    _.set(result, 'concentration.concentrationFactor', null)
  } else if (ConcentrationType.Concentration === concentrationType) {
    _.set(result, 'concentration.dilutionFactor', null)
  }
}

function _cleanupDilutionAndConcentrationFactorFields (result) {
  _.set(result, 'concentration.concentrationType', ConcentrationType.Concentration)
  _.set(result, 'concentration.concentrationFactor', null)
  _.set(result, 'concentration.dilutionFactor', null)
}

function _cleanupFields (entity) {
  let result = Object.assign({}, entity)
  if (_.get(result, 'concentration.isDilutedOrConcentrated')) {
    _cleanupDilutionOrConcentrationFactorFields(result)
  } else {
    _cleanupDilutionAndConcentrationFactorFields(result)
  }
  return result
}

export default class ProteinService {

  listEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/' + URI).then(result => {
      result.data.forEach(entity => {_decodeEnums(entity)})
      return result
    })
  }

  countEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/' + URI + 'count')
  }

  getProteinBy (slugOrId) {
    return HTTP.get(URI + slugOrId).then(result => {
      _decodeEnums(result.data)
      return result
    })
  }

  getProteinProjectsBy (slugOrId) {
    return HTTP.get(URI + slugOrId + '/projects')
  }

  createProtein (projectSlugOrId, protein) {
    const body = JSON.stringify(_encodeEnums(_cleanupFields(protein)))
    return HTTP.post(URI + projectSlugOrId, body)
  }

  saveProtein (protein) {
    return HTTP.put(URI, _encodeEnums(_cleanupFields(protein)))
  }

  findRecentAvailableProtein (purId) {
    return HTTP.get(URI + 'findRecentAvailableProtein/' + purId)
  }
}
