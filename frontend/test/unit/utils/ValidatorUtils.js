import Validator from 'async-validator'
import _ from 'lodash'

/**
 * Creates an async-validator for a given field
 * @param vue {Vue} The Vue instance
 * @param field {string} Name of the prop
 */
export function validatorForField (vue, field) {
  return new Validator({[field]: _.get(vue, 'rules.' + field)})
}

/**
 * Validates a given field name with a given value and validator.
 * @param field {string} The name of the field
 * @param value {*} Value for that field
 * @param validator {Validator} The validator to call
 * @returns {Promise<[*]>} A list of errors
 */
export async function validateField (field, value, validator) {
  return new Promise((resolve, reject) => {
    validator.validate({[field]: value}, (errors, fields) =>
      resolve(_.get(fields, field, [])))
  })
}

/**
 * Validates a given property withing a given Vue object
 * @param vue {Vue} The Vue instance
 * @param field {string} The prop name to validate
 * @param value {*} Value of the prop to be validated
 * @returns {Promise<[*]>} A list of errors for the validation
 */
export async function validateVueProp (vue, field, value) {
  const validator = validatorForField(vue, field)
  return validateField(field, value, validator)
}
