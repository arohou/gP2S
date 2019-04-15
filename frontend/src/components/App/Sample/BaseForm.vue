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
  <div class="sample-base-form base-form">
    <el-form class="sample-base-form__form" ref="sample" labelPosition="top" :model="sample" :rules="rules">
      <div class="sample-base-form__metadata">
        <el-row :gutter="24">
          <el-col :span="11">
            <el-form-item label="Sample label" prop="label" :error="getError('label')">
              <el-input class="sample-base-form__form__label-input" v-model="sample.label"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="Inc. Time (min)" prop="incubationTime" :error="getError('incubationTime')">
              <el-input id="incubation-time" v-model="sample.incubationTime"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="Inc. Temp (C)" prop="incubationTemperature" :error="getError('incubationTemperature')">
              <el-input id="incubation-temperature" v-model="sample.incubationTemperature"></el-input>
            </el-form-item>
          </el-col>
          <el-col :sm="5">
            <el-form-item label="Available for grid-making?" prop="availableForGridMaking"
                          :error="getError('availableForGridMaking')">
              <el-radio-group id="available-for-grid-making" v-model="sample.availableForGridMaking">
                <el-radio-button :label="true">{{ 'Yes' }}</el-radio-button>
                <el-radio-button :label="false">{{ 'No' }}</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="Buffer" :error="getError('otherBufferComponents')">
            <el-input type="textarea" v-model="sample.otherBufferComponents"></el-input>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="Protocol description" :error="getError('protocolDescription')">
            <el-input class="sample-base-form__metadata__protocol-description" type="textarea"
                      v-model="sample.protocolDescription"></el-input>
          </el-form-item>
        </el-row>
      </div>
      <el-row class="sample-base-form__components">
        <el-form-item label="Components" prop="components" :error="getError('components')"
                      :rules="{
                            required: true,
                            validator: isEmptyArray('Components list'),
                            message: 'At least one component needs to be added'}"
                      class="el-form-item-as-label">
        </el-form-item>
        <template v-for="(component, index) in sample.components">
          <ligand-with-registration-system-component v-if="isLigand(component) && proteinRegistrationSystemExists"
                                                     :component="component" :index="index" :projectId="projectId">
          </ligand-with-registration-system-component>
          <ligand-component v-if="isLigand(component) && !proteinRegistrationSystemExists" :component="component"
                            :index="index" :projectId="projectId">
          </ligand-component>
          <protein-with-registration-system-component v-if="isProtein(component) && proteinRegistrationSystemExists"
                                                      :component="component" :index="index" :projectId="projectId">
          </protein-with-registration-system-component>
          <protein-component v-if="isProtein(component) && !proteinRegistrationSystemExists" :component="component"
                             :index="index" :projectId="projectId"></protein-component>
          <empty-component v-if="isEmpty(component)" :component="component" :index="index"
                           :projectId="projectId"></empty-component>
        </template>

        <el-row class="sample-base-form__components__actions">
          <el-button class="sample-base-form__add-existing"
                     @click="addComponent(template)">Add existing
          </el-button>
          <el-button @click="addProteinDialog = true" id="addProteinToSample">New protein</el-button>
          <el-button @click="addLigandDialog = true" id="addLigandToSample">New ligand</el-button>
        </el-row>
      </el-row>
    </el-form>

    <overlay-component :visible.sync="addProteinDialog">
      <protein-with-registration-system-new :projectId="projectId" :isDialog="true" id="newProtein"
                                            v-if="addProteinDialog && proteinRegistrationSystemExists">
      </protein-with-registration-system-new>
      <protein-new :projectId="projectId" :isDialog="true" id="newProteinWithRegistrationSystem"
                   v-else-if="addProteinDialog"></protein-new>
    </overlay-component>
    <overlay-component :visible.sync="addLigandDialog">
      <ligand-with-registration-system-new v-if="addLigandDialog && ligandRegistrationSystemExists"
                                           :projectId="projectId"
                                           :isDialog="true"></ligand-with-registration-system-new>
      <ligand-new v-else-if="addLigandDialog" :projectId="projectId" :isDialog="true"></ligand-new>
    </overlay-component>
    <overlay-component :visible.sync="editProteinDialog">
      <protein-with-registration-system-edit :projectId="projectId" :id="proteinId" :isDialog="true"
                                             v-if="editProteinDialog && proteinRegistrationSystemExists">
      </protein-with-registration-system-edit>
      <protein-edit v-else-if="editProteinDialog" :projectId="projectId" :id="proteinId"
                    :isDialog="true"></protein-edit>
    </overlay-component>
    <overlay-component :visible.sync="editLigandDialog">
      <ligand-with-registration-system-edit v-if="editLigandDialog && ligandRegistrationSystemExists":projectId="projectId" :id="ligandId" :isDialog="true" ></ligand-with-registration-system-edit>
      <ligand-edit v-else-if="editLigandDialog" :projectId="projectId" :id="ligandId" :isDialog="true"></ligand-edit>
    </overlay-component>
  </div>
</template>

<script>
  import ComponentType from '@/components/App/Sample/ComponentType'
  import CommonValidations from '@/components/mixins/CommonValidations'
  import ProteinNew from '@/components/App/Protein/New'
  import ProteinWithRegistrationSystemNew from '@/components/App/Protein/registration_system_ext/New'
  import ProteinEdit from '@/components/App/Protein/Edit'
  import ProteinWithRegistrationSystemEdit from '@/components/App/Protein/registration_system_ext/Edit'
  import LigandNew from '@/components/App/Ligand/New'
  import LigandWithRegistrationSystemNew from '@/components/App/Ligand/registration_system_ext/New'
  import LigandEdit from '@/components/App/Ligand/Edit'
  import LigandWithRegistrationSystemEdit from '@/components/App/Ligand/registration_system_ext/Edit'
  import OverlayComponent from '@/components/App/OverlayComponent'
  import LigandWithRegistrationSystemComponent from '@/components/App/Sample/registration_system_ext/LigandComponent'
  import LigandComponent from '@/components/App/Sample/LigandComponent'
  import ProteinComponent from '@/components/App/Sample/ProteinComponent'
  import ProteinWithRegistrationSystemComponent
    from '@/components/App/Sample/registration_system_ext/ProteinComponent'
  import EmptyComponent from '@/components/App/Sample/EmptyComponent'
  import { proteinRegistrationSystemExists } from '@/utils/ExternalSystemUtils'
  import { ligandRegistrationSystemExists } from '@/utils/ExternalSystemUtils'

  export default {
    name: 'sample-base-form',

    props: ['sample', 'projectId', 'template'],

    mixins: [CommonValidations],

    components: {
      ProteinNew, ProteinWithRegistrationSystemNew, ProteinEdit, ProteinWithRegistrationSystemEdit,
      LigandNew, LigandWithRegistrationSystemNew, LigandEdit, LigandWithRegistrationSystemEdit, OverlayComponent, LigandComponent,
      LigandWithRegistrationSystemComponent,
      ProteinComponent,
      ProteinWithRegistrationSystemComponent, EmptyComponent
    },

    data () {
      return {
        rules: {
          label: [
            {
              required: true,
              message: 'Please provide a sample label',
              trigger: 'blur'
            }
          ],

          incubationTime: [{
            required: false,
            validator: this.isGreaterThan('Incubation time', 0),
            trigger: 'blur'
          }],

          incubationTemperature: [{
            required: false,
            validator: this.isGreaterThanOrEqualTo('Incubation temperature', -273.15),
            trigger: 'blur'
          }]
        },
        addProteinDialog: false,
        editProteinDialog: false,
        addLigandDialog: false,
        editLigandDialog: false,
        proteinId: null,
        ligandId: null,
        showAddExistingSelector: true
      }
    },

    events: {
      componentAdded (aliquot, type) {
        this.addProteinDialog = false
        this.addLigandDialog = false
        this.editLigandDialog = false
        this.editProteinDialog = false

        if (!aliquot) {
          return
        }

        let component = Object.assign(Object.assign({}, this.template), {
          type: type,
          aliquot: aliquot
        })

        if (this.sample.components && this.sample.components.length > 0) {
          for (let i = 0; i < this.sample.components.length; i++) {
            // Check if such component exists - if it does, modify it.
            if (this.sample.components[i].type === type && this.sample.components[i].aliquot.id === aliquot.id) {
              component.finalConcentration = this.sample.components[i].finalConcentration
              component.finalDrop = this.sample.components[i].finalDrop
              this.sample.components[i] = component
              return
            }
          }
        }

        this.addComponent(component)
      }
    },

    computed: {
      proteinRegistrationSystemExists: function () {
        return proteinRegistrationSystemExists()
      },

      ligandRegistrationSystemExists(){
        return ligandRegistrationSystemExists()
      }
    },

    methods: {
      addComponent (component) {
        if (!this.sample.components) {
          this.sample.components = []
        }
        this.sample.components.push(Object.assign({}, component))
        this.$refs['sample'].validateField('components')
      },

      removeComponent (index) {
        if (!this.sample.components) {
          return
        }
        this.sample.components.splice(index, 1)
        this.$refs['sample'].validateField('components')
      },

      showEditDialogForLigand (ligandId) {
        this.ligandId = ligandId
        this.editLigandDialog = true
      },

      showEditDialogForProtein (proteinId) {
        this.proteinId = proteinId
        this.editProteinDialog = true
      },

      isLigand (component) {
        return component.type === ComponentType.G
      },

      isProtein (component) {
        return component.type === ComponentType.PUR
      },

      isEmpty (component) {
        return !component.type
      }
    },

    mounted () {
      this.$events.on('editOfLigandComponent', this.showEditDialogForLigand)
      this.$events.on('editOfProteinComponent', this.showEditDialogForProtein)
      this.$events.on('componentRemoved', this.removeComponent)
    },

    beforeDestroy () {
      this.$events.off('editOfLigandComponent')
      this.$events.off('editOfProteinComponent')
      this.$events.off('componentRemoved')
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .sample-base-form {
    .sample-base-form__metadata {
      margin-bottom: 3.462rem;

      .sample-base-form__metadata__protocol-description {
        /deep/ .el-textarea__inner {
          min-height: 10rem !important;
        }
      }
    }

    .sample-base-form__components {
      background-color: $list-border-color;
      padding: 1.538rem;

      .el-form-item__error {
        padding-top: 1.231rem;
      }

      .el-form-item-as-label {
        margin-bottom: 1rem;
      }
    }
  }

  /* Dialog */
  .el-dialog {
    width: 45rem;

    .el-col-18 {
      width: 97%;
    }
    .el-col-16 {
      width: 100%;
    }
    .actions-header__buttons {
      margin-right: 12% !important;
    }
  }
</style>

