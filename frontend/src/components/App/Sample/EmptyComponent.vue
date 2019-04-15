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
  <el-row :key="index"
          class="sample-base-form__components__component-row"
          :gutter="20">
    <el-col :span="14">
      <el-form-item class="sample-base-form__components__component"
                    label="Component"
                    :rules="{
                          required: true,
                          message: 'Please select a component',
                          trigger: 'change'
                        }"
                    :prop="'components.' + index + '.aliquot.label'">
        <el-col :span="20">
          <aliquot-selector :projectId="projectId" :component="component"></aliquot-selector>
        </el-col>
      </el-form-item>
    </el-col>

    <el-col :span="4">
      <el-form-item class="sample-base-form__components__concentration"
                    label="Final conc. (μM)"
                    :rules="{
                            required: true,
                            validator: isGreaterThan('Concentration', 0),
                            trigger: 'blur'
                          }"
                    :prop="'components.' + index + '.finalConcentration'">
        <el-input v-model="component.finalConcentration"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="3">
      <el-form-item class="no-label">
        <el-checkbox v-model="lastDrop">last drop</el-checkbox>
      </el-form-item>
    </el-col>
    <el-col :span="3">
      <el-form-item class="no-label">
        <el-button @click="removeComponent(index)" type="danger">Remove</el-button>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script>
  import ComponentType from '@/components/App/Sample/ComponentType'
  import AliquotSelector from '@/components/Selectors/AliquotSelector'
  import CommonValidations from '@/components/mixins/CommonValidations'

  export default {
    name: 'EmptyComponent',

    components: {
      AliquotSelector
    },

    mixins: [CommonValidations],

    props: {
      component: {
        type: Object,
        required: true
      },
      index: {
        type: Number,
        required: true
      },
      projectId: {
        type: String,
        required: true
      }
    },

    data () {
      return {
        ComponentType: ComponentType
      }
    },

    methods: {
      removeComponent (index) {
        this.$events.$emit('componentRemoved', index)
      },
    },

    computed: {
      lastDrop: {
        get: function () {
          return !this._.get(this, 'component.aliquot.availableForSampleMaking', true)
        },

        set: function (value) {
          this.component.aliquot.availableForSampleMaking = !value
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .no-label {
    margin-top: 2.231rem;
  }
</style>
