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

import LigandService from '@/services/LigandService'

import HandleOptimisticLocking from '@/components/mixins/HandleOptimisticLocking'
import CommonAlerts from '@/components/mixins/CommonAlerts'
import CommonValidations from '@/components/mixins/CommonValidations'
import FormCommons from '@/components/mixins/FormCommons'
import FilterCommons from '@/components/mixins/FilterCommons'
import LigandEnabledFields from '@/components/App/Ligand/LigandEnabledFields'
import LignadIcon from '@/assets/images/ligand_icon.svg'
import DialogsService from '@/services/DialogsService'
import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

export default {

  mixins: [
    HandleOptimisticLocking, CommonAlerts, FormCommons, FilterCommons, CommonValidations
  ],

  data () {
    return {
      ligand: {
        label: '',
        tubeLabel: '',
        concentration: '',
        availableForSampleMaking: true,
        conceptId: '',
        batchId: '',
        ligandEnabledFields: LigandEnabledFields.ALL,
        solvent: ''
      },
      icon: LignadIcon,
      _service: null,
      registrationConfig: ligandRegistrationConfig()
    }
  },

  methods: {
    fetchLigand (id) {
      let self = this
      return new Promise((resolve, reject) => {
        return this._service.getLigandBy(id)
                   .then(result => {
                     self.ligand = result.data
                     resolve(result.data)
                   })
                   .catch(error => {
                     this.$log.error(error)
                     reject()
                   })
      })
    },

    shouldSaveWithExistingConceptId (ligand) {
      return DialogsService.showConfirmDialog(
        'Ligand "' + ligand.label + '" is available'
        + this.getInProjectsMessage(ligand.projects)
        + ' and has the same ' + this.registrationConfig.conceptIdLabel + '. Is this really a new aliquot?',
        'Ligand already exists.', {
          confirmButtonText: 'Yes',
          cancelButtonText: 'No'
        })
    },

    shouldProceedWithSaving (conceptId) {
      return new Promise((resolve, reject) => {
        if(!conceptId){
          resolve()
          return
        }
        this._service.findRecentAvailableLigand(conceptId)
            .then((response) => {
                if (this._.isEmpty(response.data)) {
                  resolve()
                } else {
                  this.shouldSaveWithExistingConceptId(response.data)
                      .then(() => {
                        resolve()
                      })
                      .catch(() => reject())
                }
              }
            )
      })
    },

    getInProjectsMessage: function (projects) {
      if (this._.find(projects, {slug: this.projectId})) {
        return ''
      } else if (projects.length === 1) {
        return ' (in project "' + projects[0].label + '")'
      } else {
        return ' (in projects: ' + projects.map(project => '"' + project.label + '"')
                                           .join(', ') + ')'
      }
    }
  },

  beforeCreate () {
    this._service = new LigandService()
  }
}
