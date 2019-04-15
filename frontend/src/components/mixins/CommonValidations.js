/*
 * Copyright 2018 Genentech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import ValidationError from '@/errors/ValidationError'
import ComponentType from '@/components/App/Sample/ComponentType'
import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

const MAX_INTEGER = 999999999
const MAX_NUMBER = 9999999999999999999.999999

export default {

  name: 'common-validations',

  data () {
    return {
      errors: {}
    }
  },

  methods: {
    isNotEmpty (input) {
      return input && 0 !== input.length
    },

    isNumeric (input) {
      if (/[a-zA-Z]+/.test(input)) {
        return false
      }
      return !isNaN(parseFloat(input)) && isFinite(input)
    },

    isHigherThanMaxInteger (input) {
      return input > MAX_INTEGER
    },

    isHigherThanMaxNumber (input) {
      return input > MAX_NUMBER
    },

    isInteger (input) {
      if (/^(-)?\d+$/.test(input)) {
        return true
      }
      return false
    },

    precision (number) {
      let n = number.toString().split('.')
      return n.length > 1 ? n[1].length : 0
    },

    checkIsInteger (name) {
      return (rule, value, callback) => {
        if (!rule.required && value !== 0 && !value) {
          callback()
        } else if (!this.isInteger(value)) {
          return callback(new ValidationError(
            name + ' should be an integer'))
        } else if (this.isHigherThanMaxInteger(value)) {
          return callback(new ValidationError(name + ' should not be higher than ' + MAX_INTEGER.toLocaleString()))
        } else {
          return callback()
        }
      }
    },

    isIntegerGreaterThan (name, input) {
      return (rule, value, callback) => {
        if (!rule.required && value !== 0 && !value) {
          callback()
        } else if (!this.isInteger(value) || value <= input) {
          return callback(new ValidationError(
            name + ' should be an integer greater than ' + input))
        } else if (this.isHigherThanMaxInteger(value)) {
          return callback(new ValidationError(name + ' should not be higher than ' + MAX_INTEGER.toLocaleString()))
        } else {
          return callback()
        }
      }
    },

    isIntegerGreaterThanOrEqualTo (name, input) {
      return (rule, value, callback) => {
        if (!rule.required && value !== 0 && !value) {
          callback()
        } else if (!this.isInteger(value) || value < input) {
          return callback(new ValidationError(
            name + ' should be an integer greater than or equal to ' + input))
        } else if (this.isHigherThanMaxInteger(value)) {
          return callback(new ValidationError(name + ' should not be higher than ' + MAX_INTEGER.toLocaleString()))
        } else {
          return callback()
        }
      }
    },

    isGreaterThan (name, input) {
      return (rule, value, callback) => {
        if (!rule.required && value !== 0 && !value) {
          callback()
        } else if (!this.isNumeric(value) || value <= input) {
          return callback(new ValidationError(
            name + ' should be a number greater than ' + input))
        } else if (this.isHigherThanMaxNumber(value)) {
          return callback(new ValidationError(name + ' should not be higher than ' + MAX_NUMBER.toLocaleString()))
        } else {
          return callback()
        }
      }
    },

    isNumber (name) {
      return (rule, value, callback) => {
        if (!rule.required && value !== 0 && !value) {
          callback()
        } else if (!this.isNumeric(value)) {
          return callback(new ValidationError(
            name + ' should be a number'))
        } else if (this.isHigherThanMaxNumber(value)) {
          return callback(new ValidationError(name + ' should not be higher than ' + MAX_NUMBER.toLocaleString()))
        } else {
          return callback()
        }
      }
    },

    isPrecisionNotGreaterThan (input) {
      return (rule, value, callback) => {
        if (!rule.required && value !== 0 && !value) {
          callback()
        } else if (!this.isNumeric(value) || this.precision(value) > input) {
          return callback(new ValidationError(
            'Only ' + input + ' decimal places are allowed'))
        } else if (this.isHigherThanMaxNumber(value)) {
          return callback(new ValidationError(name + ' should not be higher than ' + MAX_NUMBER.toLocaleString()))
        } else {
          return callback()
        }
      }
    },

    isGreaterThanOrEqualTo (name, input) {
      return (rule, value, callback) => {
        if (!rule.required && value !== 0 && !value) {
          callback()
        } else if (!this.isNumeric(value) || value < input) {
          return callback(new ValidationError(
            name + ' should be a number greater than or equal to ' + input))
        } else if (this.isHigherThanMaxNumber(value)) {
          return callback(new ValidationError(name + ' should not be higher than ' + MAX_NUMBER.toLocaleString()))
        } else {
          return callback()
        }
      }
    },

    isDifferentFrom (name, input) {
      return (rule, value, callback) => {
        if (!rule.required && value !== 0 && !value) {
          callback()
        } else if (!this.isNumeric(value) || value == input) {
          return callback(new ValidationError(
            name + ' should be a number different from ' + input))
        } else if (this.isHigherThanMaxNumber(value)) {
          return callback(new ValidationError(name + ' should not be higher than ' + MAX_NUMBER.toLocaleString()))
        } else {
          return callback()
        }
      }
    },

    isBetween (name, min, max) {
      return (rule, value, callback) => {
        if (!rule.required && !value) {
          callback()
        } else if (!this.isNumeric(value) || value < min || value > max) {
          return callback(new ValidationError(
            name + ' should be a number between ' + min + ' and ' + max))
        } else {
          return callback()
        }
      }
    },

    isIntegerBetweenInclusive (name, min, max) {
      return (rule, value, callback) => {
        if (!rule.required && !value) {
          callback()
        } else if (!this.isInteger(value) || value < min || value > max) {
          return callback(new ValidationError(
            name + ' should be a integer between ' + min + ' and ' + max + ' inclusive'))
        } else {
          return callback()
        }
      }
    },

    isBetweenExclusive (name, min, max) {
      return (rule, value, callback) => {
        if (!rule.required && !value) {
          callback()
        } else if (!this.isNumeric(value) || value <= min || value >= max) {
          return callback(new ValidationError(
            name + ' should be a number between ' + min + ' and ' + max))
        } else {
          return callback()
        }
      }
    },

    isDefined (name) {
      return (rule, value, callback) => {
        if (!rule.required && !value) {
          callback()
        } else if (!value) {
          return callback(new ValidationError(
            name + ' can\'t be empty'))
        } else {
          return callback()
        }
      }
    },

    checkBatchIdFormat: function (rule, value, callback) {
      let registrationConfig = ligandRegistrationConfig()

      // Pattern G[8d].[1-10d]-[1-10d]
      if (/^G\d{8}\.\d{1,10}-\d{1,10}$/.test(value)) {
        callback()
      } else {
        return callback(new ValidationError(
          registrationConfig.batchIdLabel + ' should be in format G[8d].[d]-[d]'))
      }
    },

    checkConcentration: function (rule, value, callback) {
      if (!value && value !== 0) {
        callback()
      } else if (!this.isNumeric(value)) {
        return callback(new ValidationError('Please input a valid concentration or dilution value'))
      } else if (this.isHigherThanMaxNumber(value)) {
        return callback(new ValidationError('Value should not be higher than ' + MAX_NUMBER.toLocaleString()))
      } else if (value <= 0) {
        callback(new ValidationError('Value needs to be greater than 0'))
      } else {
        callback()
      }
    },

    isEmptyArray (name) {
      return (rule, value, callback) => {
        if (!rule.required && value !== 0 && !value) {
          callback()
        } else if (!Array.isArray(value) || value.length === 0) {
          return callback(new ValidationError(
            name + ' should have at least one item'))
        } else {
          return callback()
        }
      }
    },

    getUsageSum () {
      if (this.protein) {
        return this.protein.usageSum
      }
      if (this.ligand) {
        return this.ligand.usageSum
      }
      return null
    },

    getError (label, componentId) {
      if (!label || !this.errors) {
        return null
      }
      if (!componentId) {
        return this.errors[label]
      } else if (!this.errors[componentId]) {
        return null
      }

      return this.errors[componentId][label]
    },

    validateRelationshipTypes () {
      for (let i = 0; i < this.entity.relationships.length; i++) {
        this.$refs['mainForm'].validateField('relationships.' + i + '.relationshipType')
      }
    },

    validateCommentLength () {
      return (rule, value, callback) => {
        if ((value || '').length > 255) {
          return callback(new ValidationError('Comment shouldn\'t be longer than 255 characters'))
        }
        return callback()
      }
    }
  },

  events: {
    validationError (errors) {
      if (!errors) {
        errors = {}
        return
      }

      // Parsing error message - removing lists of components and changing to format key: value.
      if (errors.proteinComponent && errors.proteinComponent.length > 0) {
        for (let component of errors.proteinComponent) {
          errors[ComponentType.PUR.name + component.id] = component.errors
        }
        errors.proteinComponent = null
      }
      if (errors.ligandComponent && errors.ligandComponent.length > 0) {
        for (let component of errors.ligandComponent) {
          errors[ComponentType.G.name + component.id] = component.errors
        }
        errors.ligandComponent = null
      }

      if (this.$refs['baseForm']) {
        this.$refs['baseForm'].errors = errors
      }
    }
  }
}
