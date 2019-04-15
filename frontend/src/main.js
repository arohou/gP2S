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

import { App, router, Vue } from '@/global-imports'

// Uncomment if you wan'to to use the vue-devtools
// https://github.com/vuejs/vue-devtools
// if (process.env.NODE_ENV === 'local') {
//   devtools.connect('localhost', 8098)
// }

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {App},
  mounted: function () {
    // Attach event listener to the root vue element
    this.$el.addEventListener('click', this.onClick)
  },
  beforeDestroy: function () {
    this.$el.removeEventListener('click', this.onClick)
  },
  methods: {
    /**
     * iPad click away fix for selectors.
     *
     * It seems that iPad changes the behaviour of a HTML selector in a way, that the user can't
     * click away from it (click anywhere outside of the bounds of a selector). The user needs to click another
     * selector, leading to an unwanted behaviour, where the users jumps from a field to a field in order to just
     * loose focus on a selector.
     *
     * This hack fetches all clicks on the app itself and forces all selector dropdowns to hide, jus as they're
     * hidden as part of their normal behaviour.
     * @param ev
     */
    onClick: function (ev) {
      const selectorDropdowns = document.querySelectorAll('.el-select-dropdown')
      for (let dropdown in selectorDropdowns) {
        if (dropdown.style) {
          dropdown.style.display = 'none'
        }
      }
    }
  }
})
