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

    /**
     * Display an general warning alert passing a message
     */
    alertWarnOK (message) {
      this.$alert(message, 'Warning', {
        confirmButtonText: 'OK'
      })
    },
    /**
     * Display an general error alert passing a message
     */
    alertErrorOK (message) {
      this.$alert(message, 'Error', {
        confirmButtonText: 'OK'
      })
    },
    /**
     * Display an general alert asking for reload
     */
    alertGeneralFormIssue () {
      this.$alert('Something went wrong. Please reload the form and try again.', 'Warning', {
        confirmButtonText: 'OK'
      })
    },

    /**
     * Display an alert when no project has been selected
     */
    alertNoProject () {
      this.$events.$emit('formSavingFinished')
      this.$alert('Project is not selected', 'Warning', {
        confirmButtonText: 'OK'
      })
    }
  }
}
