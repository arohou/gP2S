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
  <el-col :sm="24" class="selector-box">
    <slot></slot>
    <el-col v-if="allEntities">
      <el-row v-for="(entity, index) in selectedEntities" :key="index" :gutter="20">
        <el-col :span="20">
          <el-row type="flex" justify="space-between" :gutter="20">
            <el-col v-for="configItem in config" :gutter="20" :key="configItem.propertyName">
              <el-form-item
                class="sample-base-form__components__component"
                :label="configItem.label"
                :rules="Object.assign({
                            required: configItem.required === false ? false : true,
                            message: configItem.validator ? undefined : configItem.errorMessage},
                            {validator : configItem.validator ? configItem.validator(entity) : undefined})"
                :prop="entitiesName + '.'+index+'.' + configItem.propertyName
                            + (configItem.type === 'select-with-date' ? '.label' : '')">
                <el-select v-if="configItem.type === 'select-with-date'"
                           :placeholder="configItem.placeholder" noDataText="No items available"
                           v-model="entity[configItem.propertyName]" value-key="id"
                           class="input-with-search"
                           @change="configItem.onchange
                           ? configItem.onchange(entity, index) :updateSubSelectors(entity)">
                  <el-option
                    v-for="item in createFilteredSelectionList(entity[configItem.propertyName], configItem)"
                    :key="item.id" :value="item" :label="item.label">
                    <div style="height: 3rem">
                      <div class="selector__item-title">{{item.label}}</div>
                      <div class="selector__item-datetime" v-if="item.createdDate">
                        created: {{ item.createdDate | moment('YYYY.MM.DD HH:mm') }}
                      </div>
                    </div>
                  </el-option>
                </el-select>
                <el-select v-if="configItem.type === 'select'"
                           :placeholder="configItem.placeholder" noDataText="No items available"
                           v-model="entity[configItem.propertyName]"
                           class="input-with-search"
                           @change="configItem.onchange ? configItem.onchange() : '' ">
                  <el-option v-if="!!configItem.listItemsName"
                             v-for="item in createOptions(entity, configItem)"
                             :key="item" :value="item"
                             :label="item">
                  </el-option>
                  <el-option v-if="!!configItem.listItems"
                             v-for="item in entity, configItem.listItems"
                             :key="item.value" :value="item.value"
                             :label="item.label">
                  </el-option>
                </el-select>
                <el-input type="textarea" v-if="configItem.type === 'textarea'"
                          v-model="entity[configItem.propertyName]"
                          :placeholder="configItem.placeholder">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>

        </el-col>
        <el-col :span="4">
          <el-form-item class="no-label">
            <el-button @click="removeEntity(index)" type="danger">Remove</el-button>
          </el-form-item>
        </el-col>
      </el-row>

      <el-button v-if="allEntities.length > selectedEntities.length || unlimitedRecordsNumber()"
                 class="sample-base-form__add-existing"
                 @click="addEntity()">{{ this.addNewLabel ? this.addNewLabel : 'Add new' }}
      </el-button>
      <el-tooltip content="No items available" placement="right" v-else>
        <div class="left">
          <el-button class="sample-base-form__add-existing" disabled>
            {{ this.addNewLabel ? this.addNewLabel : 'Add new' }}
          </el-button>
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
      config: {type: Array, required: true},
      entitiesName: {type: String, required: true},
      selectedEntities: {type: Array, required: true},
      allEntities: {type: Array, required: true}, // All entities to choose from.
      onRemove: null,
      defaultValues: null,
      addNewLabel: String,
      rules: {}
    },

    mixins: [CommonValidations],

    data () {
      return {
        historicalValues: null,
        mainSelectorPropertyName: null
      }
    },

    methods: {
      addEntity () {
        let newEntity = {}
        let defaultEntityValue = this.defaultValues ? this.defaultValues[this.mainSelectorPropertyName] : null

        newEntity[this.mainSelectorPropertyName] =
          this.selectedEntities.length === 0 && defaultEntityValue
            ? this.allEntities.find((el) => parseInt(el.id) === parseInt(defaultEntityValue)) : {}
        this.config.forEach((configItem) => {
          if (configItem.propertyName === this.mainSelectorPropertyName) {
            return
          } else if (configItem.type === 'select') {
            newEntity[configItem.propertyName] = ''
          }

          this.setupDefaultValue(newEntity, configItem)
        })

        this.selectedEntities.push(newEntity)
      },

      createFilteredSelectionList (entity, configItem) {
        if (configItem.notUnique) {
          return this.allEntities
        }

        const list = (entity ? [entity] : []).concat(
          this.allEntities.filter(el => {
            return el && el.id && !this.selectedEntities.map(elem => elem[configItem.propertyName].id).includes(el.id)
              ? el : null
          }))
          .filter(el => {
            return el && el.id && el.label ? el : null
          })

        return _.sortBy(list, 'createdDate', 'id').reverse()
      },

      removeEntity (index) {
        if (index >= this.selectedEntities.length) {
          return
        }

        this.selectedEntities.splice(index, 1)
        if (this.onRemove) {
          this.onRemove()
        }
      },

      setupDefaultValue (entity, configItem) {
        if (!this.defaultValues) {
          return false
        }

        const configListItemsName = configItem.listItemsName
        const configPropertyName = configItem.propertyName
        const defaultEntityValues = this.defaultValues[configPropertyName]
        const currentEntityId = entity[this.mainSelectorPropertyName].id
        const configItems = _.get(entity, configListItemsName) || []

        if (!defaultEntityValues || !currentEntityId) {
          return false
        }

        let defaultValue = defaultEntityValues[currentEntityId]

        if (!configItems.includes(defaultValue)) {
          return false
        }

        entity[configItem.propertyName] = defaultValue

        return true
      },

      updateSubSelectors (entity) {
        this.config.forEach((configItem) => {
            let configListItemsName = configItem['listItemsName']
            let configPropertyName = configItem['propertyName']

            if (!configPropertyName || !configListItemsName) {
              return
            }

            if (configListItemsName.startsWith(this.mainSelectorPropertyName + '.') && entity[configPropertyName]
              && !_.get(entity, configListItemsName).includes(entity[configPropertyName])) {
              if (!this.setupDefaultValue(entity, configItem)) {
                entity[configPropertyName] = undefined
              }
            } else if (!entity[configPropertyName]) { // Set up default value if it is empty.
              this.setupDefaultValue(entity, configItem)
            }
          }
        )
      },

      createOptions (entity, configItem) {
        let previousObject = (this.historicalValues || [])
          .find((el) => _.get(el, this.mainSelectorPropertyName + '.id')
            === _.get(entity, this.mainSelectorPropertyName + '.id')
          )
        let previousValue = _.get(previousObject, configItem.propertyName)

        let options = _.get(entity, configItem.listItemsName) || []

        return !!previousValue && options.includes(previousValue) || !previousValue
          ? options : [previousValue].concat(options)
      },

      setupMainSelectorPropertyName () {
        this.mainSelectorPropertyName = (this.config.find((el) => el.type === 'select-with-date') || {}).propertyName
      },

      unlimitedRecordsNumber () {
        return (this.config.find(el => el.propertyName === this.mainSelectorPropertyName) || {}).notUnique
      }
    },

    watch: {
      'selectedEntities': function (selectedEntities) {
        if (!!this.historicalValues) {
          return
        }

        this.historicalValues = JSON.parse(JSON.stringify(selectedEntities)) // deep clone
      }
    },

    created () {
      this.setupMainSelectorPropertyName()
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  /deep/ .el-textarea textarea {
    height: 2.769rem;
  }

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
