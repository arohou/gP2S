<!--
  - Copyright 2018 Genentech Inc.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <div class="ligand-base-form base-form">
    <el-row>
      <el-col :span="15">
        <el-form class="ligand__form" ref="ligand" :model="ligand" :rules="rules" labelPosition="top">
          <el-form-item class="ligand__form__label" label="Ligand label" prop="label" :error="getError('label')">
            <el-input id="label" class="ligand__form__label-input" v-model="ligand.label"></el-input>
          </el-form-item>
          <el-form-item :label="registrationConfig.conceptIdLabel" prop="conceptId"
                        :show-message="isConceptFieldEnabled()"
                        :error="getError('conceptId')">

            <el-input id="conceptId" v-model="ligand.conceptId"
                      @blur="conceptIdChanged"
                      @focus="enableConceptId"
                      :disabled="!isConceptFieldEnabled()">
              <i class="el-icon-check" slot="append">
                <state-indicator :state="conceptIdValidationState"></state-indicator>
              </i>
            </el-input>
          </el-form-item>
          <el-form-item :label="registrationConfig.batchIdLabel" prop="batchId"
                        :show-message="isBatchFieldEnabled()"
                        :error="getError('batchId')">
            <el-input id="batchId" v-model="ligand.batchId"
                      @blur="batchIdChanged"
                      @focus="enableBatchId"
                      :disabled="!isBatchFieldEnabled()">
              <i class="el-icon-check" slot="append">
                <state-indicator :state="batchIdValidationState"></state-indicator>
              </i>
            </el-input>
          </el-form-item>
          <el-form-item label="Tube labeling" :error="getError('tubeLabel')">
            <el-input id="tubeLabel" v-model="ligand.tubeLabel"></el-input>
          </el-form-item>

          <el-form-item label="Concentration (μM)" prop="concentration" :error="getError('concentration')">
            <el-input id="concentration" v-model="ligand.concentration"></el-input>
          </el-form-item>
          <el-form-item label="Solvent" :error="getError('solvent')">
            <el-input id="solvent" v-model="ligand.solvent"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="8" class='BATCH-info-box' v-if="showLigandRegistrationInfo">
        <ligand-registration-info :conceptId="ligand.conceptId" :batchId="ligand.batchId"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import LigandRegistrationInfo from '@/components/App/Ligand/registration_system_ext/LigandRegistrationInfo'
  import CommonValidations from '@/components/mixins/CommonValidations'
  import StateIndicator from '@/components/App/StateIndicatorComponent'
  import LigandRegistrationCommons from '@/components/App/Ligand/registration_system_ext/LigandRegistrationCommons'
  import LigandEnabledFields from '@/components/App/Ligand/LigandEnabledFields'
  import ValidationError from '@/errors/ValidationError'
  import LigandService from '@/services/LigandService'
  import DialogsService from '@/services/DialogsService'

  import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

  export default {
    name: 'ligand-base-form',

    props: {
      ligand: Object,
      showLigandRegistrationInfo: {
        type: Boolean,
        default: true
      },
      projectId: String
    },

    mixins: [
      CommonValidations, LigandRegistrationCommons
    ],

    components: {
      LigandRegistrationInfo, StateIndicator
    },

    data () {
      return {
        _ligandService: Object,
        usageSumError: false,
        conceptIdValidationState: null,
        batchIdValidationState: null,
        validConceptIds: {},   // Map of CONCEPT id => batchId
        validBatchIds: {},  // Map of batchId id => CONCEPT id
        originalConceptId: null,
        originalBatchId: null,
        batchIdforLigandInfo: null,
        previousConceptId: null,
        previousBatchId: null,
        rules: {
          label: [
            {
              required: true,
              message: 'Ligand label can\'t be empty',
              trigger: 'blur'
            }
          ],
          conceptId: [{
            validator: this.existsConceptIdOrBatchId('Protocol type'),
            message: this.ligandRegistrationIdsValidationMessage,
            trigger: 'blur'
          }],
          batchId: [{
            validator: this.batchIdValidator(),
            trigger: 'blur'
          },
          ],
          concentration: [
            {required: true, message: 'Please enter concentration'},
            {validator: this.checkConcentration, trigger: 'blur'}
          ]
        },
        registrationConfig: ligandRegistrationConfig()
      }
    },

    computed: {
      ligandRegistrationIdsValidationMessage () {
        return 'Please enter ' + this.registrationConfig.conceptIdLabel + ' or ' + this.registrationConfig.batchIdLabel
      }
    },

    events: {
      initialLigandRegistrationCall (originalConceptId, originalBatchId) {
        this.validConceptIds = {}
        this.validBatchIds = {}

        if (originalConceptId) {
          this.conceptIdValidationState = 'correct'
          this.ligand.batchId = null

          this.callLigandRegistration(originalConceptId, null)
            .then((ligandRegistrationData) => {
              this.ligandRegistrationEntityLoaded(ligandRegistrationData)
            }).catch(error => {
          })
        }

        this.originalConceptId = this.previousConceptId = originalConceptId
        this.originalBatchId = this.previousBatchId = originalBatchId
      },

      validateAndSaveLigand (saveCallback) {
        this.$events.$emit('formSavingStarted')

        let validationMethod = this.ligand.ligandEnabledFields === LigandEnabledFields.BATCH
          ? this.validateBatchId(this.ligand.batchId).catch()
          : this.validateConceptId(this.ligand.conceptId).catch()

        validationMethod.then(() => this.shouldProceedWithSaving(this.ligand.conceptId))
          .then(saveCallback)
          .catch(() => this.$events.$emit('formSavingFinished'))
      },

      ligandHasNoMolecularWeight (conceptId, batchId) {
        if (!this.isEventForMe(conceptId, batchId)) {
          return
        }

        this.displayNoMolecularWeightInCONCEPTDialog()
      }
    },

    methods: {
      isEventForMe (conceptId, batchId) {
        if (_.get(this.ligand, 'conceptId') && _.get(this.ligand, 'conceptId') === conceptId) {
          return true
        } else if (_.get(this.ligand, 'batchId') && _.get(this.ligand, 'batchId') === batchId) {
          return true
        }

        return false
      },

      isBatchFieldEnabled () {
        return (!this.ligand.conceptId && !this.ligand.batchId)
          || this.ligand.ligandEnabledFields && this.ligand.ligandEnabledFields !== LigandEnabledFields.CONCEPT
      },

      isConceptFieldEnabled () {
        return (!this.ligand.conceptId && !this.ligand.batchId)
          || this.ligand.ligandEnabledFields && this.ligand.ligandEnabledFields !== LigandEnabledFields.BATCH
      },

      existsConceptIdOrBatchId () {
        return (rule, value, callback) => {
          if (_.isEmpty(this.ligand.conceptId) && _.isEmpty(this.ligand.batchId)) {
            return callback(new ValidationError(this.ligandRegistrationIdsValidationMessage))
          }
          return callback()
        }
      },
      batchIdValidator () {
        return (rule, value, callback) => {
          if (_.isEmpty(this.ligand.conceptId) && _.isEmpty(this.ligand.batchId)) {
            return callback(new
            ValidationError(this.ligandRegistrationIdsValidationMessage))
          } else if (_.isEmpty(this.ligand.batchId)) {
            return callback()
          }
          return this.checkBatchIdFormat(rule, value, callback)
        }
      },

      validateConceptId (conceptId) {
        this.setConceptIdError('')
        this.setBatchIdError('')

        return new Promise((resolve, reject) => {
          this.setConceptIdError('')
          if (!conceptId) {
            this.setConceptIdError(this.ligandRegistrationIdsValidationMessage)
            this.conceptIdValidationState = null
            this.$events.$emit('ligandRegistrationProcessing', conceptId, null)
            reject()
            return
          }

          if (Object.keys(this.validConceptIds).includes(conceptId)) { // Don't validate CONCEPT id if it has been validated.
            this.ligand.batchId = this.validConceptIds[conceptId]
            this.conceptIdValidationState = 'correct'
            resolve()
          } else {
            this.conceptIdValidationState = 'processing'
          }

          this.callLigandRegistration(conceptId, null, this.previousConceptId === conceptId)
            .then((ligandRegistrationData) => {
              this.ligandRegistrationEntityLoaded(ligandRegistrationData)
              resolve()
            }).catch((error) => {
            this.setConceptIdError(error)
            reject()
          })
        })
      },

      validateBatchId (batchId) {
        return new Promise((resolve, reject) => {
          this.setBatchIdError('')
          if (!batchId) {
            this.setBatchIdError(this.ligandRegistrationIdsValidationMessage)
            this.batchIdValidationState = null
            this.$events.$emit('ligandRegistrationProcessing', null, batchId)
            reject()
            return
          } else if (!/^G\d{8}\.\d{1,10}-\d{1,10}$/.test(batchId)) {
            this.$events.$emit('conceptIdentifierInvalid', null, batchId,
              'Invalid ' + this.registrationConfig.batchIdLabel)
            this.setBatchIdError(this.registrationConfig.batchIdLabel + ' should be in format G[8d].[d]-[d]')
            reject()
            return
          }

          if (Object.keys(this.validBatchIds).includes(batchId)) {
            // Don't validate batch id if it has been validated.
            this.ligand.conceptId = this.validBatchIds[batchId]
            this.batchIdValidationState = 'correct'
            resolve()
          } else {
            this.batchIdValidationState = 'processing'
          }

          this.callLigandRegistration(null, batchId)
            .then((ligandRegistrationData) => {
              this.ligandRegistrationEntityLoaded(ligandRegistrationData)
              resolve()
            }).catch((error) => {
            this.setBatchIdError(error)
            reject()
          })
        })
      },

      setConceptIdError (error) {
        this.errors.batchId = null

        if (!_.isObject(error)) {
          this.errors.conceptId = error
        } else if (error.conceptId) {
          this.errors.conceptId = 'Invalid ' + this.registrationConfig.conceptIdLabel
          this.conceptIdValidationState = 'invalid'
        } else if (error.message) { // Server error.
          this.conceptIdValidationState = 'unknown'
          this.errors.conceptId = _.isObject(error.message) ? error.message.errorMessage : error.message
        }
      },

      setBatchIdError (error) {
        this.errors.conceptId = null

        if (!_.isObject(error)) {
          this.errors.batchId = error
        } else if (error.batchId) {
          this.errors.batchId = 'Invalid ' + this.registrationConfig.batchIdLabel
          this.batchIdValidationState = 'invalid'
        } else if (error.message || error.errorMessage) { // Server error.
          this.batchIdValidationState = 'unknown'
          this.errors.batchId = error.message || error.errorMessage
        }
      },

      ligandRegistrationEntityLoaded (ligandRegistrationData) {
        this.conceptIdValidationState = this.conceptIdValidationState ? 'correct' : this.conceptIdValidationState
        this.batchIdValidationState = this.batchIdValidationState ? 'correct' : this.batchIdValidationState
        this.ligand.batchId = ligandRegistrationData.batchId
        this.ligand.conceptId = ligandRegistrationData.conceptId
        this.batchIdforLigandInfo = this.ligand.batchId

        this.validConceptIds[this.ligand.conceptId] = this.ligand.batchId
        this.validBatchIds[this.ligand.batchId] = this.ligand.conceptId

        this.setConceptIdError(null)
        this.setBatchIdError(null)
      },

      async displayNoMolecularWeightInCONCEPTDialog () {
        await DialogsService.showConfirmDialog(
          'Please update ' + this.registrationConfig.batchIdLabel +
          ' in ' + this.registrationConfig.systemName + ' by entering the Compound Molecular Weight.'
          + ' Then get back to this screen and save this form once again.',
          'Molecular weight not found', {
            showCancelButton: false
          })

        this.showMolecularWeightError()
      },

      showMolecularWeightError () {
        if (this.ligand.conceptId) {
          this.conceptIdValidationState = 'invalid'
          this.setConceptIdError('Molecular weight for ' + this.ligand.conceptId + ' not found')
        } else {
          this.batchIdValidationState = 'invalid'
          this.setBatchIdError('Molecular weight for ' + this.ligand.batchId + ' not found')
        }
      },

      // Methods for "save with existing CONCEPT ID" popup.

      getInProjectsMessage: function (projects) {
        if (_.find(projects, {slug: this.projectId})) {
          return ''
        } else if (projects.length === 1) {
          return ' (in project "' + projects[0].label + '")'
        } else {
          return ' (in projects: ' + projects.map(project => '"' + project.label + '"').join(', ') + ')'
        }
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
          if (this.originalConceptId && this.originalConceptId === conceptId) {
            resolve()
            return
          }

          this._ligandService.findRecentAvailableLigand(conceptId).then((response) => {
              if (_.isEmpty(response.data)) {
                resolve()
              } else {
                this.shouldSaveWithExistingConceptId(response.data).then(() => {
                  resolve()
                }).catch(() => reject())
              }
            }
          )
        })
      },

      // Listeners.

      enableConceptId () {
        this.ligand.ligandEnabledFields = LigandEnabledFields.CONCEPT
      },

      enableBatchId () {
        this.ligand.ligandEnabledFields = LigandEnabledFields.BATCH
      },

      conceptIdChanged () {
        this.setConceptIdError(null)

        if (this.isNotEmpty(this.ligand.conceptId)) {
          this.ligand.ligandEnabledFields = LigandEnabledFields.CONCEPT
        } else {
          this.ligand.ligandEnabledFields = LigandEnabledFields.ALL
          this.ligand.batchId = ''
        }

        if (this.previousConceptId !== this.ligand.conceptId) {
          this.validateConceptId(this.ligand.conceptId).catch(error => {
          })
          this.previousConceptId = this.ligand.conceptId
          this.previousBatchId = null
          this.batchIdforLigandInfo = null
        }
      },

      batchIdChanged () {
        this.setBatchIdError(null)
        if (this.isNotEmpty(this.ligand.batchId)) {
          this.ligand.ligandEnabledFields = LigandEnabledFields.BATCH
        } else {
          this.ligand.ligandEnabledFields = LigandEnabledFields.ALL
          this.ligand.conceptId = ''
        }
        if (this.previousBatchId !== this.ligand.batchId) {
          this.batchIdforLigandInfo = null

          this.validateBatchId(this.ligand.batchId).catch(error => {
          })
          this.previousBatchId = this.ligand.batchId
          this.previousConceptId = null
        }
      }
    },

    mounted () {
      this._ligandService = new LigandService()
    }
  }
</script>

<style lang="scss" scoped>
  @import '~loadme/dist/style/loadme.css';
  @import "~styles/globals/colors";

  .el-icon-check:before {
    content: none;
  }

  .el-icon-check {
    width: 1em;
  }

  .small-content {
    font-size: 0.846rem;
    color: $text-header-color--disabled;
    line-height: initial;
    margin-top: 0.8rem;
  }
</style>
<style lang="scss">
  .usage-error .el-form-item__error {
    margin-top: 0;
    position: relative;
    width: 200%; /* for long error so that it is not folded */
  }
</style>
