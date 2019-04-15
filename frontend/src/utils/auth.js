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

/**
 * @author Piotr Pikusa on 03.03.2019.
 */


import LoginService from '@/services/LoginService'
import AuthenticationError from '@/errors/AuthenticationError'

const _loginService = new LoginService()
const KEY_IS_LOGGED_IN = "is_logged_in"


export default {

  get isLoggedIn(){
    if(localStorage.getItem(KEY_IS_LOGGED_IN) == null)
      return false
    else return localStorage.getItem(KEY_IS_LOGGED_IN) == 'true'
  },

  set isLoggedIn(newValue){
    localStorage.setItem(KEY_IS_LOGGED_IN, newValue)
  },

  install(Vue) {
    Vue.$isLoggedIn = this.isLoggedIn
    Vue.prototype.$isLoggedIn = this.isLoggedIn
  },

  login(credentials) {
    return _loginService.login(credentials)
      .then((result) => {
        this.isLoggedIn = true
      }).catch((error) => {
        if (error instanceof AuthenticationError) {
          this.isLoggedIn = false
        }
        throw error
      })
  },

  logout() {
      this.isLoggedIn = false
  }
}
