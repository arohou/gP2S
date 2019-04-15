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

export default {

  methods: {

    submitBaseFormBy (formName) {
      this.submitForm(this.$refs['baseForm'].$refs[formName])
    },

    submitForm (form) {
      this.formSavingStarted()

      form.validate((valid) => {
        if (valid) {
          // Form errors need to be removed because error that has the same value doesn't appear again.
          this.$refs['baseForm'].errors = {}
          let formSavingPromise = this.saveForm()
          if (formSavingPromise) {
            formSavingPromise.then(() => this.$events.emit('entitiesCountChanged'))
              .catch(error => this.handleFormSubmitError(error))
              .finally(() => this.formSavingFinished())
          }
          return true
        }
        this.$events.$emit('formValidationFailed')
        this.$log.warn('Form validation failed')
        this.formSavingFinished()
        return false
      })
    },

    handleFormSubmitError (error) {
      this.$log.error(error)
      this.$events.$emit('saveFormFailed')
    },

    initForm (slugOrId) {
      this.formLoadingStarted()
      let loadDataPromise = this.loadFormData(slugOrId)
      if (loadDataPromise) {
        loadDataPromise.catch(error => this.$log.error(error))
          .finally(() => this.formLoadingFinished())
      } else {
        this.formLoadingFinished()
      }
    },

    formLoadingStarted: function () {
      this.$events.$emit('formLoadingStarted')
    },

    formLoadingFinished: function () {
      this.$events.$emit('formLoadingFinished')
    },

    formSavingStarted: function () {
      this.$events.$emit('formSavingStarted')
    },

    formSavingFinished: function () {
      this.$events.$emit('formSavingFinished')
    }
  },

  mounted () {
    this.initForm(this.id)
  },

  watch: {
    'id' (id) {
      this.initForm(id)
    }
  }

}
