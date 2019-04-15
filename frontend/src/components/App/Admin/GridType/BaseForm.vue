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
  <div class="grid-type-base-form base-form">
    <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" labelPosition="top">
      <div class="base-form__metadata">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="Grid Type label" prop="label" :error="getError('label')">
              <el-input v-model="entity.label"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Manufacturer" prop="manufacturer" :error="getError('manufacturer')">
              <el-input v-model="entity.manufacturer"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="Description" prop="description" :error="getError('description')">
            <el-input type="textarea" v-model="entity.description"></el-input>
          </el-form-item>
        </el-row>
      </div>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'

  export default {
    name: 'grid-type-base-form',

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
              message: 'Grid Type label can\'t be empty',
              trigger: 'blur'
            }
          ],
          manufacturer: [{
            required: true,
            message: 'Please enter manufacturer',
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
