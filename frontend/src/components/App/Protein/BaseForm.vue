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
            <el-input id="purificationId" v-model="protein.purificationId">
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
    </el-row>

  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import ConcentrationComponent from '@/components/App/ConcentrationComponent'
  import ProteinService from '@/services/ProteinService'
  import DialogsService from '@/services/DialogsService'

  export default {
    name: 'protein-base-form',

    props: {
      protein: {},
      projectId: String
    },

    mixins: [
      CommonValidations
    ],

    components: {
      ConcentrationComponent
    },

    methods: {
      getInProjectsMessage: function (projects) {
        if (_.find(projects, {slug: this.projectId})) {
          return ''
        } else if (projects.length === 1) {
          return ' (in project "' + projects[0].label + '")'
        } else {
          return ' (in projects: ' + projects.map(project => '"' + project.label + '"').join(', ') + ')'
        }
      },

      shouldSaveWithExistingPurId (protein) {
        return DialogsService.showConfirmDialog(
          'Protein "' + protein.label + '" is available'
          + this.getInProjectsMessage(protein.projects)
          + ' and has the same ID. Is this really a new aliquot?',
          'Protein already exists.', {
            confirmButtonText: 'Yes',
            cancelButtonText: 'No'
          })
      },

      shouldProceedWithSaving (purId) {
        return new Promise((resolve, reject) => {
          if (!purId || (this.originalPurId && this.originalPurId === purId)) {
            resolve()
            return
          }

          this._proteinService.findRecentAvailableProtein(purId).then((response) => {
              if (_.isEmpty(response.data)) {
                resolve()
              } else {
                this.shouldSaveWithExistingPurId(response.data).then(() => {
                  resolve()
                }).catch(() => reject())
              }
            }
          )
        })
      },

      // Events.

      validateAndSaveProtein (purificationId, saveCallback) {
        this.$events.$emit('formSavingStarted')

        this.shouldProceedWithSaving(purificationId)
          .then(saveCallback)
          .catch(() => {
            this.$events.$emit('formSavingFinished')
          })
      },

      initialDataLoad (purificationId) {
        this.originalPurId = purificationId
      },

      initEvents () {
        this.$events.on('initialDataLoad', this.initialDataLoad)
        this.$events.on('validateAndSaveProtein', this.validateAndSaveProtein)
      },

      removeEvents () {
        this.$events.off('initialDataLoad', this.initialDataLoad)
        this.$events.off('validateAndSaveProtein', this.validateAndSaveProtein)
      }
    },

    created () {
      this._proteinService = new ProteinService()
      this.initEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    },

    data () {
      return {
        usageSumError: false,
        originalPurId: null,
        rules: {
          label: [
            {required: true, message: 'Protein label can\'t be empty', trigger: 'blur'}
          ],
          concentration: [
            {validator: this.checkConcentration, trigger: 'blur'}
          ]
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>
<style lang="scss">
  .usage-error .el-form-item__error {
    margin-top: 0;
    position: relative;
    width: 200%; /* for long error so that it is not folded */
  }
</style>
