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
  <div class="protein-base-form base-form">
    <el-row>
      <el-col :span="15">
        <el-form class="protein__form" ref="protein" :model="protein" :rules="rules" labelPosition="top">
          <el-form-item class="protein__form__label" label="Protein label" prop="label" :error="getError('label')">
            <el-input id="label" class="protein__form__label-input" v-model="protein.label"></el-input>
          </el-form-item>
          <el-form-item label="PUR ID" prop="purificationId" :error="getError('purificationId')">
            <el-input id="purificationId" v-model="protein.purificationId"
                      @blur="purificationIdChanged()">
              <i class="el-icon-check" slot="append">
                <state-indicator :state="proteinIdValidationState"></state-indicator>
              </i>
            </el-input>
          </el-form-item>
          <el-form-item label="Tube labeling" :error="getError('tubeLabel')">
            <el-input id="tubeLabel" v-model="protein.tubeLabel"></el-input>
          </el-form-item>

          <el-row :gutter="20" type="flex">
            <el-col>
              <concentration-component :concentration="protein.concentration"/>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
      <el-col :sm="8" v-if="showProteinRegistrationSystemInfo" class="protein-registration-system-info-box">
        <protein-registration-system-info :purId="protein.purificationId"></protein-registration-system-info>
      </el-col>
    </el-row>

  </div>
</template>

<script>
  import ProteinRegistrationSystemInfo from './ProteinRegistrationSystemInfo'
  import StateIndicator from '@/components/App/StateIndicatorComponent'
  import ProteinRegistrationSystemCommons from '@/components/App/Protein/registration_system_ext/ProteinRegistrationSystemCommons'
  import BaseForm from '@/components/App/Protein/BaseForm'
  import DialogsService from '@/services/DialogsService'

  export default {
    name: 'protein-base-form',

    props: {
      showProteinRegistrationSystemInfo: {
        type: Boolean,
        default: true
      }
    },

    mixins: [
      BaseForm, ProteinRegistrationSystemCommons
    ],

    components: {
      ProteinRegistrationSystemInfo, StateIndicator
    },

    methods: {
      validatePurificationId (proteinId) {
        this.setProteinRegistrationSystemError('')

        return new Promise((resolve, reject) => {
          if (!proteinId) {
            this.handleEmptyPurId()
            reject()
            return
          }

          if (this.validPurificationIds.includes(proteinId)) { // Don't validate PUR id if it has been validated.
            this.proteinIdValidationState = 'correct'
            resolve()
          } else {
            this.proteinIdValidationState = 'processing'
          }

          this.callProteinRegistrationSystem(proteinId, this.protein)
            .then(() => {
              this.proteinRegistrationSystemPurLoaded(proteinId)
              resolve()
            })
            .catch((error) => {
              this.handleProteinRegistrationSystemError(error)
              reject()
            })
        })
      },

      handleProteinRegistrationSystemError (error) {
        if (!error) {
          return
        }

        if (error.purId !== undefined) {
          this.proteinIdValidationState = 'invalid'
          this.setProteinRegistrationSystemError('Invalid PUR ID')
        } else { // Server error.
          this.proteinIdValidationState = 'unknown'
          this.setProteinRegistrationSystemError(error.message)
        }
      },

      setProteinRegistrationSystemError (error) {
        this.errors.purificationId = error
      },

      purificationIdChanged () {
        this.validatePurificationId(this.protein.purificationId).catch(error => {})
      },

      proteinRegistrationSystemPurLoaded: function (purificationId) {
        this.validPurificationIds.push(purificationId)
        this.setProteinRegistrationSystemError(null)
        this.proteinIdValidationState = 'correct'
      },

      handleEmptyPurId () {
        this.proteinIdValidationState = 'invalid'
        this.setProteinRegistrationSystemError('Please enter pur id')
        this.$events.emit('proteinRegistrationSystemPurInvalid', _.get(this.protein, 'purificationId'))
      },

      // Events.

      initialProteinRegistrationSystemCall (purificationId) {
        this.originalPurId = purificationId

        if (purificationId) {
          this.proteinIdValidationState = 'correct'
          this.validPurificationIds.push(purificationId)

          this.callProteinRegistrationSystem(purificationId, this.protein).catch(error => {})
        }
      },

      displayNoMolecularWeightInRegistrationSystemDialog () {
        return DialogsService.showConfirmDialog(
          'Please update molecular weight for this protein. Then get back to this screen and save this form once again.',
          'Molecular weight for ' + this.protein.purificationId + ' not found', {
            confirmButtonText: 'OK',
            showCancelButton: false
          }).then(this.showMolecularWeightError)
      },

      showMolecularWeightError () {
        this.proteinIdValidationState = 'invalid'
        this.setProteinRegistrationSystemError('Molecular weight for ' + this.protein.purificationId + ' not found')
      },

      initEvents () {
        this.$events.on('initialProteinRegistrationSystemCall', this.initialProteinRegistrationSystemCall)
        this.$events.on('validateAndSaveProtein', this.validateAndSaveProtein)
        this.$events.on('purHasNoMolecularWeight', (purificationId) => {
          if (!_.get(this.protein, 'purificationId') || this.protein.purificationId !== purificationId) {
            return
          }

          this.displayNoMolecularWeightInRegistrationSystemDialog()
        })
      },

      removeEvents () {
        this.$events.off('initialProteinRegistrationSystemCall', this.initialProteinRegistrationSystemCall)
        this.$events.off('validateAndSaveProtein', this.validateAndSaveProtein)
        this.$events.off('purHasNoMolecularWeight')
      }
    },

    data () {
      return {
        proteinIdValidationState: null,
        validPurificationIds: [],
        rules: {
          purificationId: [
            {required: true, message: 'Please enter pur id', trigger: 'blur'}
          ]
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import '~loadme/dist/style/loadme.css';

  .el-icon-check:before {
    content: none;
  }

  .protein-registration-system-info-box {
    float: right;
  }

  .el-icon-check {
    width: 1em;
  }
</style>
