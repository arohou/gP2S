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
 * @author Cezary Krzyżanowski on 26.10.2017.
 */

import { HTTP } from '@/utils/http-common'
import ProtocolType from '@/components/App/Grid/ProtocolType'
import Polarity from '@/components/App/Admin/SurfaceTreatmentProtocol/Polarity'
import ConcentrationType from '@/components/App/ConcentrationType'
import _ from 'lodash'

const URI = 'grid/'

function _decodeEnums (entity) {
  if (entity && entity.protocolType) {
    entity.protocolType = ProtocolType.enumValueOf(entity.protocolType)
  }
  const polarity = _.get(entity, 'surfaceTreatmentProtocol.polarity')
  if (polarity) {
    entity.surfaceTreatmentProtocol.polarity = Polarity.enumValueOf(polarity)
  }
  const concentrationType = _.get(entity, 'concentration.concentrationType')
  if (concentrationType) {
    entity.concentration.concentrationType = ConcentrationType.enumValueOf(concentrationType)
  }
  return entity
}

function _encodeEnums (grid) {
  let result = Object.assign({}, grid)
  result.protocolType = grid.protocolType.name
  if (result.surfaceTreatmentProtocol) {
    result.surfaceTreatmentProtocol.polarity = grid.surfaceTreatmentProtocol.polarity.name
  }
  const concentrationType = _.get(grid, 'concentration.concentrationType')
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

function _cleanupFields (grid) {
  let result = Object.assign({}, grid)
  _cleanupCryoStorageFields(result)
  _cleanupStainStorageFields(result)

  if (_.get(result, 'concentration.isDilutedOrConcentrated')) {
    _cleanupDilutionOrConcentrationFactorFields(result)
  } else {
    _.set(result, 'concentration.concentrationType', ConcentrationType.Concentration)
    _.set(result, 'concentration.concentrationFactor', null)
    _.set(result, 'concentration.dilutionFactor', null)
  }
  return result
}

function _cleanupCryoStorageFields (result) {
  if (result.protocolType !== ProtocolType.Cryo) {
    result.cryoStorageDevice = null
    result.cylinderNumberLabel = null
    result.tubeNumberLabel = null
    result.boxNumberLabel = null
  }
}

function _cleanupStainStorageFields (result) {
  if (result.protocolType !== ProtocolType.Stain) {
    result.storageBoxLabelNumber = null
    result.positionWithinBox = null
    if (result.cryoStorageDevice) {
      if (!result.cryoStorageDevice.hasCylinders) {
        result.cylinderNumberLabel = null
      }
      if (!result.cryoStorageDevice.hasTubes) {
        result.tubeNumberLabel = null
      }
      if (!result.cryoStorageDevice.hasBoxes) {
        result.boxNumberLabel = null
      }
    }
  }
}

export default class GridService {

  getGridBy (slugOrId) {
    return HTTP.get(URI + slugOrId).then(result => {
      _decodeEnums(result.data)
      return result
    })
  }

  getGridProjectsBy (slugOrId) {
    return HTTP.get(URI + slugOrId + '/projects')
  }

  listEntities () {
    return HTTP.get(URI)
  }

  listEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/' + URI).then(result => {
      return result
    })
  }

  countEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/' + URI + 'count')
  }

  findAvailableGrids (projectSlugOrId) {
    return HTTP.get(URI + 'findAvailableGrids/' + projectSlugOrId)
  }

  findAvailableGridsOrGridWithId (projectSlugOrId, gridId) {
    return HTTP.get(URI + 'findAvailableGrids/' + projectSlugOrId + '/' + gridId)
  }

  createGrid (projectSlugOrId, grid) {
    const body = JSON.stringify(_encodeEnums(_cleanupFields(grid)))
    return HTTP.post(URI + projectSlugOrId, body)
  }

  saveGrid (projectSlugOrId, grid) {
    return HTTP.put(URI + projectSlugOrId, _encodeEnums(_cleanupFields(grid)))
  }

  getDefaultValues (projectSlugOrId) {
    return HTTP.get('default-value/grid/' + projectSlugOrId).then(result => {
      _decodeEnums(result.data)
      return result
    })
  }
}
