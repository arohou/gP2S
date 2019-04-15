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

import axios from 'axios'
import DialogsService from '@/services/DialogsService'
import OptimisticLockingError from '@/errors/OptimisticLockingError'
import EntityNotFoundError from '@/errors/EntityNotFoundError'
import { Vue } from '@/global-imports'
import auth from '@/utils/auth'
import AuthenticationError from '@/errors/AuthenticationError'

const ERROR_MESSAGES = {
  400: 'Corrupted request',
  404: 'Entity not found',
  500: 'Server was unable to handle the request properly',
  503: 'Service unavailable'
}

export const HTTP = axios.create({
  baseURL: process.env.API_URL,
  headers: {
    'Content-type': 'application/json',
    'Accept': 'application/json'
  }
})

function logResponse (response) {
  return {
    REQUEST: {
      headers: response.config.headers,
      data: response.config.data
    },
    RESPONSE: {
      headers: response.headers,
      data: response.data
    }
  }
}

// Log responses and errors, emit errors for status code 400.
HTTP.interceptors.response.use(response => {
  // Logging response - that may help with debugging
  Vue.prototype.$log.debug(
    response.status + ' ' + response.statusText,
    '(' + response.config.method.toUpperCase() + ' ' + response.config.url + ')', logResponse(response))

  return response
}, error => {
  if (error.config) {
    Vue.prototype.$log.error(error.name + ': ' + error.message,
      '(' + error.config.method.toUpperCase() + ' ' + error.config.url + ')', logResponse(error))
  } else {
    Vue.prototype.$log.debug(error.name + ': ' + error.message)
  }
  // General error messaging
  if (error.response.status === 400) {
    Vue.prototype.$events.$emit('validationError', error.response.data && error.response.data.errors ? error.response.data.errors : {})
  } else if (error.response.status === 401 || error.response.status === 403) {
    if(auth.isLoggedIn == true){
      window.console.warn(`Error 401 while user logged in, so user is going to be logged out`)
      auth.logout()
      window.location.reload()
    }
    auth.logout()
    throw new AuthenticationError()
  } else if (error.response.status === 404) {
    throw new EntityNotFoundError()
  } else if (error.response.status === 409) {
    return DialogsService.optimisticLockingError()
      .then(() => { throw new OptimisticLockingError() })
  } else if (error.response.status !== 503 && error.response.status !== 502) { // Unknown host is handled in UI errors.
    DialogsService.showError(
      ERROR_MESSAGES[error.response.status] || 'Unknown server error: ' + error.response.status)
  }

  return Promise.reject(error)
})
