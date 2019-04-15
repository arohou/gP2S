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
  <div class="surface-treatment-protocol-base-form base-form">
    <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" labelPosition="top">
      <div class="base-form__metadata">
        <el-row :gutter="24">
          <el-col :span="19">
            <el-form-item label="Protocol label" prop="label" :error="getError('label')">
              <el-input v-model="entity.label"></el-input>
            </el-form-item>
          </el-col>
          <el-col :sm="5">
            <el-form-item label="Polarity" prop="polarity"
                          :error="getError('polarity')">
              <el-radio-group id="polarity" v-model="entity.polarity">
                <el-radio-button :label="Polarity.Positive">{{ Polarity.Positive.name }}</el-radio-button>
                <el-radio-button :label="Polarity.Negative">{{ Polarity.Negative.name }}</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="9">
            <el-form-item label="Surface treatment machine" prop="machine" :error="getError('machine')">
              <el-select v-model="entity.machine" filterable placeholder="Select machine" value-key="id">
                <el-option
                  v-for="item in machines"
                  :key="item.id"
                  :label="item.label"
                  :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="Duration (s)" prop="duration" :error="getError('duration')">
              <el-input v-model="entity.duration"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="Pressure (mbar)" prop="pressure" :error="getError('pressure')">
              <el-input v-model="entity.pressure"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="Current (mA)" prop="current" :error="getError('current')">
              <el-input v-model="entity.current"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="Chemicals in Atmosphere" :error="getError('chemicalsInAtmosphere')">
            <el-input type="textarea" v-model="entity.chemicalsInAtmosphere"></el-input>
          </el-form-item>
        </el-row>
      </div>
    </el-form>

  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import FormCommons from '@/components/mixins/FormCommons'
  import Service from '@/services/SurfaceTreatmentMachineService'
  import Polarity from '@/components/App/Admin/SurfaceTreatmentProtocol/Polarity'

  const service = new Service()

  export default {
    name: 'surface-treatment-protocol-base-form',

    props: {
      entity: {}
    },

    mixins: [
      CommonValidations, FormCommons
    ],

    data () {
      return {
        Polarity: Polarity,
        machines: [],

        rules: {
          label: [
            {
              required: true,
              message: 'Surface Treatment Protocol label can\'t be empty',
              trigger: 'blur'
            }
          ],
          machine: [{
            required: true,
            validator: this.isDefined('Surface Treatment Machine'),
            trigger: 'change'
          }],
          duration: [{
            required: true,
            validator: this.isGreaterThan('Duration', 0),
            trigger: 'blur'
          }],
          pressure: [{
            required: true,
            validator: this.isGreaterThan('Pressure', 0),
            trigger: 'blur'
          }],
          current: [{
            required: true,
            validator: this.isGreaterThan('Current', 0),
            trigger: 'blur'
          }]
        }
      }
    },

    created () {
      this.initOriginalEntity('entity')
    },

    mounted () {
      service.listEntities()
        .then(result => {
          this.machines = this.options(result.data, _.get(this, 'originalEntity.machine'), 'id')
        })
        .catch(error => this.$log.error(error))
    }
  }
</script>
