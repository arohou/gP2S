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
  <el-col class="selector-box">
    <slot></slot>
    <el-col>
      <el-row v-for="(entity, index) in selectedEntities"
              :key="index"
              class="sample-base-form__components__component-row"
              :gutter="20">
        <el-col :span="20">
          <el-form-item class="sample-base-form__components__component"
                        :label="entityLabel  + ' ' + (index + 1) + ':'"
                        :rules="{
                                  required: true,
                                  message: errorMessage || 'Please select an item',
                                  trigger: 'change'
                                }"
                        :prop="entitiesName + '.'+index+'.label'">
            <el-select :placeholder="placeholder || 'Choose an item'"
                       noDataText="No items available"
                       v-model="chosenEntitiesIds[index]" value-key="key"
                       class="input-with-search"
                       @change="updateEntities(chosenEntitiesIds[index], index)">
              <el-option v-for="item in createFilteredSelectionList(entity, index)"
                         :key="item.id" :value="item.id" :label="item.label">
                <div style="height: 3rem">
                  <div class="selector__item-title">{{item.label}}</div>
                  <div class="selector__item-datetime">created: {{ item.createdDate | moment('YYYY.MM.DD HH:mm') }}
                  </div>
                </div>
              </el-option>
            </el-select>

          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item class="no-label">
            <el-button @click="removeEntity(entity)" type="danger">Remove</el-button>
          </el-form-item>
        </el-col>
      </el-row>

      <el-button v-if="allEntities.length > selectedEntities.length"
                 class="sample-base-form__add-existing"
                 @click="addEntity()">Add new
      </el-button>
      <el-tooltip content="No items available" placement="right" v-else>
        <div class="left">
          <el-button class="sample-base-form__add-existing" disabled>Add new</el-button>
        </div>
      </el-tooltip>
    </el-col>
  </el-col>
</template>

<script>
  import CommonValidations from '@/components/mixins/CommonValidations'

  export default {
    name: 'multiple-select',

    props: {
      selectedEntities: {type: Array, required: true},
      entitiesName: {type: String, required: true},
      entityLabel: {type: String, required: true},
      allEntities: {type: Array, required: true}, // All entities to choose from.
      placeholder: {type: String, required: false},
      errorMessage: {type: String, required: false}
    },

    mixins: [CommonValidations],

    data () {
      return {
        chosenEntitiesIds: [], // IDs of entities that have been added.
      }
    },

    computed: {
      availableEntities () { // Entities that haven't been selected.
        return this.allEntities.filter(el => !this.chosenEntitiesIds.includes(el.id))
      }
    },

    methods: {
      addEntity () {
        this.selectedEntities.push({})
        this.updateAvailableEntities()
      },

      updateAvailableEntities () {
        this.chosenEntitiesIds = this.selectedEntities.map(entity => entity.id)
      },

      updateEntities (chosenEntityId, index) {
        const updatedEntity = this.allEntities.find(el => el.id === chosenEntityId)
        this.selectedEntities[index] = Object.assign({}, updatedEntity)

        this.updateAvailableEntities()
      },

      createFilteredSelectionList (entity) {
        const list = (entity ? [entity] : []).concat(this.availableEntities)
          .filter(el => {
            return el && el.id && el.label ? el : null
          })
        return _.sortBy(list, 'createdDate', 'id').reverse()
      },

      removeEntity (entity) {
        const indexToRemove = this.selectedEntities.findIndex(el => el.id === entity.id)
        this.selectedEntities.splice(indexToRemove, 1)

        this.updateAvailableEntities()
      }
    },

    watch: {
      allEntities: function () {
        this.updateAvailableEntities()
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .selector-box {
    background-color: $list-border-color;
    padding: 1.538rem;

    .el-form-item-as-label {
      margin-bottom: 1rem;
    }
  }

  .selector__item-title {
    font-size: 1.15rem;
    font-weight: bold;
    display: inline-flex;
  }

  .selector__item-datetime {
    color: $text-header-color--disabled;
    font-size: 0.9rem;
  }

  .el-select-dropdown__item {
    height: auto;
  }

  .no-label {
    margin-top: 2.231rem;
  }

  .el-select-dropdown__item.selected .selector__item-datetime {
    color: $text-gray-selected;
  }
</style>
