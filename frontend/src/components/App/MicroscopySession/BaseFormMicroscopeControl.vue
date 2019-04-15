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
  <div class="base-form">
    <el-row :gutter="24">
      <el-col :span="8">
        <microscope-label
          :microscope-label="_.get(entity, 'microscope.label')"></microscope-label>
      </el-col>
      <el-col :span="8">
        <detector-label
          :detector-label="_.get(entity, 'electronDetector.label')"></detector-label>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="8">
        <el-form-item label="Target underfocus min (μm)" prop="targetUnderfocusMin"
                      :error="getError('targetUnderfocusMin')" id="microscopy-session__target-underfocus-min">
          <el-input v-model="entity.targetUnderfocusMin"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Target underfocus max (μm)" prop="targetUnderfocusMax"
                      :error="getError('targetUnderfocusMax')" id="microscopy-session__target-underfocus-max">
          <el-input v-model="entity.targetUnderfocusMax"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="Number of exposures per hole" prop="exposuresPerHole"
                      :error="getError('exposuresPerHole')" id="microscopy-session__exposures-per-hole">
          <el-input v-model="entity.exposuresPerHole"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'
  import FilterCommons from '@/components/mixins/FilterCommons'
  import MicroscopeLabel from './MicroscopeLabel'
  import DetectorLabel from './DetectorLabel'

  export default {
    components: {
      DetectorLabel,
      MicroscopeLabel
    },
    props: {
      allRules: Object,
      mainForm: Object,
      projectId: String,
      entity: Object,
      allErrors: Object,
      originalEntityBaseForm: Object
    },

    data () {
      return {
        rules: {
          targetUnderfocusMin:
            [{
              required: false,
              validator: this.isGreaterThan('Target underfocus min', 0),
              trigger: 'blur'
            }],
          targetUnderfocusMax:
            [{
              required: false,
              validator: this.isGreaterThan('Target underfocus max', 0),
              trigger: 'blur'
            }],
          exposuresPerHole:
            [{
              required: false,
              validator: this.isIntegerGreaterThan('Number of exposures per hole', 0),
              trigger: 'blur'
            }],
        }
      }
    },

    mixins: [
      CommonValidations, FilterCommons
    ],

    watch: {
      // region Injecting form values that are applicable for all sections.
      'allErrors': function () {
        this.errors = this.allErrors // Inject updated error messages from main form.
      },

      'originalEntityBaseForm': function () {
        this.originalEntity = this.originalEntityBaseForm // Inject original entity.
      }
      // endregion
    },

    created () {
      Object.assign(this.allRules || {}, this.rules)
    }
  }
</script>
