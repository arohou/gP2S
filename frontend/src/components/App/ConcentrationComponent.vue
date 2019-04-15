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
  <el-row class="concentration-component" :gutter="20" type="flex">
    <el-col :span="8">
      <el-form-item label="Diluted/concentrated?" prop="concentration.isDilutedOrConcentrated"
                    :error="getError('concentration.isDilutedOrConcentrated')">
        <el-radio-group id="isDilutedOrConcentrated" v-model="concentration.isDilutedOrConcentrated">
          <el-radio-button :label="true">Yes</el-radio-button>
          <el-radio-button :label="false">No</el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-col>
    <el-col :span="8" v-if="concentration.isDilutedOrConcentrated">
      <el-form-item label="Type" prop="concentration.concentrationType"
                    :error="getError('concentration.concentrationType')">
        <el-radio-group id="concentrationType" v-model="concentration.concentrationType">
          <el-radio-button :label="ConcentrationType.Dilution">{{ ConcentrationType.Dilution.name }}</el-radio-button>
          <el-radio-button :label="ConcentrationType.Concentration">
            {{ ConcentrationType.Concentration.name }}
          </el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-col>
    <el-col :span="8" v-if="concentration.isDilutedOrConcentrated">
      <el-form-item label="Dilution factor" prop="concentration.dilutionFactor"
                    v-if="concentration.concentrationType === ConcentrationType.Dilution"
                    :error="getError('concentration.dilutionFactor')" :rules="[
                      { required: true, message: 'Dilution factor is required'},
                      { validator: isGreaterThan('Dilution factor', 1.0) }
                    ]">
        <div class="dilution-factor">
          <el-input disabled value="1" class="dilution-factor__const"></el-input>
          <span>:</span>
          <el-input v-model="concentration.dilutionFactor"></el-input>
        </div>
      </el-form-item>
      <el-form-item label="Concentration factor (x)" prop="concentration.concentrationFactor"
                    v-if="concentration.concentrationType === ConcentrationType.Concentration"
                    :error="getError('concentration.concentrationFactor')"
                    :rules="[
                      { required: true, message: 'Concentration factor is required'},
                      { validator: isGreaterThan('Concentration factor', 1.0) }
                    ]">
        <el-input v-model="concentration.concentrationFactor"></el-input>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script>
  import ConcentrationType from '@/components/App/ConcentrationType'
  import CommonValidations from '@/components/mixins/CommonValidations'

  export default {
    name: 'concentration-component',

    components: {},

    data () {
      return {
        ConcentrationType: ConcentrationType
      }
    },

    props: {
      concentration: Object
    },

    mixins: [
      CommonValidations
    ],

    events: {},

    methods: {}
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .dilution-factor {
    display: flex;

    .dilution-factor__const {
      width: 2.5rem;
      min-width: 2.5rem;
    }

    span {
      margin-left: 0.5rem;
      margin-right: 0.5rem;
    }
  }
</style>
