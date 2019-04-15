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
  <div class="sample-holder-base-form base-form">
    <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" labelPosition="top">
      <div class="base-form__metadata">
        <el-row>
          <el-col>
            <el-form-item label="Sample Holder label" prop="label" :error="getError('label')">
              <el-input v-model="entity.label"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="Manufacturer" prop="manufacturer" :error="getError('manufacturer')">
              <el-input v-model="entity.manufacturer"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Model" prop="model" :error="getError('model')">
              <el-input v-model="entity.model"></el-input>
            </el-form-item>
          </el-col>
          <el-col :sm="8">
            <el-form-item label="Cryo-capable?" prop="cryoCapable" :error="getError('cryoCapable')">
              <el-radio-group v-model="entity.cryoCapable">
                <el-radio-button :label="true">Yes</el-radio-button>
                <el-radio-button :label="false">No</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :sm="8">
            <el-form-item label="Can be used with the following microscopes" prop="microscopes"
                          :error="getError('microscopes')">
              <el-select v-model="entity.microscopes" filterable multiple
                         placeholder="Select microscopes"
                         value-key="id">
                <el-option
                  v-for="item in microscopes"
                  :key="item.id"
                  :label="item.label"
                  :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="Location" prop="location" :error="getError('location')">
              <el-input v-model="entity.location"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Maximum tilt (°)" prop="maximumTilt" :error="getError('maximumTilt')">
              <el-input v-model="entity.maximumTilt"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :sm="8">
            <el-form-item label="Double tilt?" prop="doubleTilt" :error="getError('doubleTilt')">
              <el-radio-group v-model="entity.doubleTilt">
                <el-radio-button :label="true">Yes</el-radio-button>
                <el-radio-button :label="false">No</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import FormCommons from '@/components/mixins/FormCommons'
  import MicroscopeService from '@/services/MicroscopeService'

  const microscopeService = new MicroscopeService()

  export default {
    name: 'sample-holder-base-form',

    props: {
      entity: {}
    },

    mixins: [
      CommonValidations, FormCommons
    ],

    data () {
      return {
        microscopes: [],
        rules: {
          label: [
            {
              required: true,
              message: 'Sample Holder label can\'t be empty',
              trigger: 'blur'
            }
          ],
          manufacturer: [{
            required: true,
            message: 'Please enter manufacturer',
            trigger: 'blur'
          }],
          model: [{
            required: true,
            message: 'Please enter model',
            trigger: 'blur'
          }],
          location: [{
            required: true,
            message: 'Please enter location',
            trigger: 'blur'
          }],
          microscopes: [{
            required: true,
            validator: this.isEmptyArray('List of microscopes'),
            trigger: 'change'
          }],
          maximumTilt: [{
            required: true,
            validator: this.isGreaterThanOrEqualTo('Maximum tilt', 0),
            trigger: 'blur'
          }],
        }
      }
    },

    created () {
      this.initOriginalEntity('entity')
    },

    mounted () {
      microscopeService.listEntities()
        .then(result => {
          this.microscopes = this.options(result.data, _.get(this, 'entity.microscope'), 'id')
        }).catch(error => this.$log.error(error))
    }
  }
</script>
