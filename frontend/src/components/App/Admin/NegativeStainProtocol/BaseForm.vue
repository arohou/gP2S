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
  <div class="negative-stain-protocol-base-form base-form">
    <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" labelPosition="top">
      <div class="base-form__metadata">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="Negative Stain Protocol label" prop="label" :error="getError('label')">
              <el-input v-model="entity.label"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Name of stain" prop="name" :error="getError('name')">
              <el-input v-model="entity.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="pH of stain" prop="ph" :error="getError('ph')">
              <el-input v-model="entity.ph"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="Concentration of stain (%)" prop="concentration" :error="getError('concentration')">
              <el-input v-model="entity.concentration"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Temperature (C)" prop="temperature" :error="getError('temperature')">
              <el-input v-model="entity.temperature"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Incubation time of stain before blotting (s)" prop="incubationTime"
                          :error="getError('incubationTime')">
              <el-input v-model="entity.incubationTime"></el-input>
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

  export default {
    name: 'negative-stain-protocol-base-form',

    props: {
      entity: {}
    },

    mixins: [
      CommonValidations
    ],

    data () {
      return {
        rules: {
          label: [
            {
              required: true,
              message: 'Negative Stain Protocol label can\'t be empty',
              trigger: 'blur'
            }
          ],
          name: [{
            required: true,
            message: 'Please enter name of stain',
            trigger: 'blur'
          }],
          ph: [{
            required: true,
            validator: this.isGreaterThan('pH', 0),
            trigger: 'blur'
          }],
          concentration: [{
            required: true,
            validator: this.isBetweenExclusive('Concentration', 0, 100),
            trigger: 'blur'
          }],
          temperature: [{
            required: false,
            validator: this.isGreaterThan('Temperature', -273.15),
            trigger: 'blur'
          }],
          incubationTime: [{
            required: true,
            validator: this.isIntegerGreaterThan('Incubation time', 0),
            trigger: 'blur'
          }],
          description: [{
            required: true,
            message: 'Please enter description',
            trigger: 'blur'
          }]
        }
      }
    }
  }
</script>
