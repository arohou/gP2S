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

// Polyfill for older browsers (IE11)
import 'babel-polyfill'
import promiseFinally from 'promise.prototype.finally'
import automapper from 'automapper-ts'
import Vue from 'vue'
import VueEvents from 'vue-events'
import VueMoment from 'vue-moment'
import lodash from 'lodash'
import VueLodash from 'vue-lodash'
import i18n from 'vue-i18n'
import AsyncComputed from 'vue-async-computed'
import auth from '@/utils/auth'

import locale from 'element-ui/lib/locale/lang/en'
import ElementUI from 'element-ui'
import Vuelidate from 'vuelidate'

import App from '@/App'
import router from '@/router'
import VueLogger from 'vuejs-logger'

//Logger configuration
//TODO move it to configuration files
const options = {
  // required ['debug', 'info', 'warn', 'error', 'fatal']
  logLevel: 'info',
  // optional : defaults to false if not specified
  stringifyArguments: false,
  // optional : defaults to false if not specified
  showLogLevel: false,
  // optional : defaults to false if not specified
  showMethodName: true,
  // optional : defaults to '|' if not specified
  separator: '|',
  // optional : defaults to false if not specified
  showConsoleColors: true
}

promiseFinally.shim() // will be a no-op if not needed

automapper.initialize(() => {})

Vue.use(VueLogger, options)
Vue.use(VueEvents)
Vue.use(i18n)
Vue.use(ElementUI, {locale})
Vue.use(Vuelidate)
Vue.use(VueMoment)
Vue.use(VueLodash, lodash)
Vue.use(AsyncComputed)
Vue.use(auth)

Vue.config.productionTip = false

router.error = null

router.onError(function (error) {
  router.error = error
  router.push({name: 'error', params: {errorCode: error.response.status}})
})

router.beforeEach(async (to, from, next) => {
  if (to.name === 'error' || to.path.startsWith('/error')) {
    next()
    return
  }
  try {
    router.error = null
    next()
  } catch (e) {
    next(e)
  }
})

export { Vue, App, router }
