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
 * @author Cezary Krzyżanowski on 24.08.2017.
 */

import { HTTP } from '@/utils/http-common'
import ComponentType from '@/components/App/Sample/ComponentType'

const URI = 'sample/'

export default class SampleService {

  getSampleBy (slugOrId) {
    return HTTP.get(URI + slugOrId)
  }

  listEntities () {
    return HTTP.get(URI)
  }

  listSamplesWithProjectLabels (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/sampleWithProjectLabels')
  }

  listEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/' + URI)
  }

  countEntitiesByProject (slugOrId) {
    return HTTP.get('project/' + slugOrId + '/sample/count')
  }

  findSamplesAvailableForGridMaking (projectSlugOrId) {
    return HTTP.get(URI + '/findSamplesAvailableForGridMaking/' + projectSlugOrId)
  }

  getSampleProjectsBy (slugOrId) {
    return HTTP.get(URI + slugOrId + '/projects')
  }

  resetDefaultFields (data) {
    const nullables = ['id', 'slug', 'version']
    nullables.forEach(key => data[key] = null)
  }

  removeAuditFields (data) {
    const audits = ['createdBy', 'createdDate', 'modifiedBy', 'modifiedDate']
    audits.forEach(key => delete data[key])
  }

  createSample (projectSlugOrId, sample) {
    // A copy needs to be made so that in case of error, the form that gets saved is not corrupted.
    let sampleToSave = Object.assign({}, sample)

    this.resetDefaultFields(sampleToSave)
    this.removeAuditFields(sampleToSave)

    if (sampleToSave.components) {
      sampleToSave.components.forEach(c => this.resetDefaultFields(c))
      sampleToSave.components.forEach(c => this.removeAuditFields(c))
      sampleToSave.proteinComponent = this.getProteinComponents(sampleToSave.components)
      sampleToSave.ligandComponent = this.getLigandComponents(sampleToSave.components)
    }
    delete sampleToSave.components

    const body = JSON.stringify(sampleToSave)
    return HTTP.post(URI + projectSlugOrId, body)
  }

  updateAvailabilityForGridMaking (sample) {
    return HTTP.patch(URI + sample.id, {
      availableForGridMaking: sample.availableForGridMaking
    })
  }

  saveSample (sample) {
    // A copy needs to be made so that in case of error, the form that gets saved is not corrupted.
    let sampleToSave = Object.assign({}, sample)
    sampleToSave.proteinComponent = this.getProteinComponents(sample.components)
    sampleToSave.ligandComponent = this.getLigandComponents(sample.components)
    delete sampleToSave.components
    return HTTP.put(URI, sampleToSave)
  }

  getProteinComponents (components) {
    return components.filter(x => {
      return x.type === ComponentType.PUR ||
        x.type === ComponentType.PUR.name
    })
  }

  getLigandComponents (components) {
    return components.filter(x => {
      return x.type === ComponentType.G ||
        x.type === ComponentType.G.name
    })
  }
}
