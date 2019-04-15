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
  <div class="vitrification-protocol-base-form base-form">
    <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" label-width="120px" labelPosition="top">
      <div class="base-form__metadata">
        <el-row>
          <el-col>
            <el-form-item label="Vitrification Protocol label" prop="label" :error="getError('label')">
              <el-input v-model="entity.label"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="Vitrification machine" prop="vitrificationMachine"
                          :error="getError('vitrificationMachine')">
              <el-select v-model="entity.vitrificationMachine" filterable placeholder="Select vitrification machine"
                         value-key="id">
                <el-option
                  v-for="item in vitrificationMachines"
                  :key="item.id"
                  :label="item.label"
                  :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Relative humidity (%)" prop="relativeHumidity" :error="getError('relativeHumidity')">
              <el-input v-model="entity.relativeHumidity"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Temperature (°C)" prop="temperature" :error="getError('temperature')">
              <el-input v-model="entity.temperature"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="Blot paper" prop="blottingPaper" :error="getError('blottingPaper')">
              <el-select v-model="entity.blottingPaper" filterable placeholder="Select blotting paper" value-key="id">
                <el-option
                  v-for="item in blottingPapers"
                  :key="item.id"
                  :label="item.label"
                  :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Blot force" prop="blotForce" :error="getError('blotForce')">
              <el-input v-model="entity.blotForce"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Number of blots" prop="numberOfBlots" :error="getError('numberOfBlots')">
              <el-input v-model="entity.numberOfBlots"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="Blot time (s)" prop="blotTime" :error="getError('blotTime')">
              <el-input v-model="entity.blotTime"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Wait time (s)" prop="waitTime" :error="getError('waitTime')">
              <el-input v-model="entity.waitTime"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Drain time (s)" prop="drainTime" :error="getError('drainTime')">
              <el-input v-model="entity.drainTime"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Number of sample applications" prop="numberOfSampleApplications"
                          :error="getError('numberOfSampleApplications')">
              <el-input v-model="entity.numberOfSampleApplications"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="Description" prop="description" :error="getError('description')">
            <el-input type="textarea" :rows="10" v-model="entity.description"></el-input>
          </el-form-item>
        </el-row>
      </div>
    </el-form>

  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import FormCommons from '@/components/mixins/FormCommons'
  import MachineService from '@/services/VitrificationMachineService'
  import PaperService from '@/services/BlottingPaperService'

  const machineService = new MachineService()
  const paperService = new PaperService()

  export default {
    name: 'vitrification-protocol-base-form',

    props: {
      entity: {}
    },

    mixins: [
      CommonValidations, FormCommons
    ],

    data () {
      return {
        vitrificationMachines: [],
        blottingPapers: [],

        rules: {
          label: [
            {
              required: true,
              message: 'Vitrification Protocol label can\'t be empty',
              trigger: 'blur',
            }
          ],
          vitrificationMachine: [{
            required: true,
            validator: this.isDefined('Vitrification Machine'),
            trigger: 'change'
          }],
          relativeHumidity: [{
            required: true,
            validator: this.isIntegerBetweenInclusive('Relative humidity', 0, 100),
            trigger: 'blur'
          }],
          temperature: [{
            required: true,
            validator: this.isGreaterThan('Temperature', -273.15),
            trigger: 'blur'
          }],
          blottingPaper: [{
            required: true,
            validator: this.isDefined('Blot paper'),
            trigger: 'change'
          }],
          blotForce: [{
            required: true,
            validator: this.checkIsInteger('Blotting force'),
            trigger: 'blur'
          }],
          numberOfBlots: [{
            required: true,
            validator: this.isIntegerGreaterThan('Number of blots', 0),
            trigger: 'blur'
          }],
          blotTime: [{
            required: true,
            validator: this.isGreaterThanOrEqualTo('Blot time', 0),
            trigger: 'blur'
          }],
          waitTime: [{
            required: true,
            validator: this.isGreaterThanOrEqualTo('Wait time', 0),
            trigger: 'blur'
          }],
          drainTime: [{
            required: true,
            validator: this.isGreaterThanOrEqualTo('Drain time', 0),
            trigger: 'blur'
          }],
          numberOfSampleApplications: [{
            required: true,
            validator: this.isIntegerGreaterThan('Number of sample applications', 0),
            trigger: 'blur'
          }],
          description: [{
            required: true,
            message: 'Description can\'t be empty',
            trigger: 'blur'
          }]
        }
      }
    },

    created () {
      this.initOriginalEntity('entity')
    },

    mounted () {
      machineService.listEntities()
        .then(result => {
          this.vitrificationMachines = this.options(result.data, _.get(this, 'entity.vitrificationMachine'), 'id')
        })
        .catch(error => this.$log.error(error))
      paperService.listEntities()
        .then(result => {
          this.blottingPapers = this.options(result.data, _.get(this, 'entity.blottingPaper'), 'id')
        })
        .catch(error => this.$log.error(error))
    }
  }
</script>
