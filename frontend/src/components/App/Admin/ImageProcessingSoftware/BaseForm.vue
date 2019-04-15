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
  <div class="image-processing-base-form base-form">
    <el-form class="em-main-form" ref="mainForm" :model="entity" :rules="rules" labelPosition="top">
      <el-row>
        <el-form-item label="Image processing software label" prop="label" :error="getError('label')">
          <el-input v-model="entity.label"></el-input>
        </el-form-item>
      </el-row>
      <el-row class="base-form__metadata">
        <el-form-item label="Software version(s)" prop="softwareVersions"
                      :error="getError('softwareVersions')"
                      :rules="{
                              required: true,
                              validator: this.isEmptyArray('Software version list'),
                              message: 'At least one software version needs to be present'}">
          <el-row v-for="(version, index) in entity.softwareVersions"
                  :key="index"
                  :gutter="20">
            <el-col :span="20">
              <el-row>
                <el-form-item :rules="{
                                  required: true,
                                  message: 'Please input a version',
                                  trigger: 'change'
                                }"
                              :prop="'softwareVersions.' + index">
                  <el-input placeholder="Please enter a version"
                            v-model="entity.softwareVersions[index]"></el-input>
                </el-form-item>
              </el-row>
            </el-col>
            <el-col :span="4">
              <el-form-item>
                <el-button @click="removeVersion(index)" type="danger">Remove</el-button>
              </el-form-item>
            </el-col>
          </el-row>

          <el-button @click="addVersion">Add new</el-button>
        </el-form-item>
      </el-row>
    </el-form>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'

  export default {
    name: 'image-processing-base-form',

    props: {
      entity: {
        label: '',
        softwareVersions: []
      }
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
              message: 'Image processing software label can\'t be empty',
              trigger: 'blur'
            }
          ]
        }
      }
    },

    methods: {
      removeVersion (index) {
        this.entity.softwareVersions.splice(index, 1)
      },

      addVersion () {
        this.entity.softwareVersions.push('')
      }
    }
  }
</script>

<style scoped lang="scss">
  @import "~styles/globals/colors";

  .image_processing_software-base-form__software-version-selector-container {
    width: 100%
  }

  /deep/ .el-form-item.is-required .el-form-item__label:before {
    margin-right: 0px;
  }

  .base-form__metadata {
    background-color: $list-border-color;
    padding: 1.538rem;
  }

</style>
