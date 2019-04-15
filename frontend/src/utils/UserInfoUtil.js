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
 * Created by Przemyslaw Stankowski on 26.03.2018.
 */

import UserService from '@/services/UserService'

const _userService = new UserService()
const GP2S_USER = 'GP2S User'


export default {

  getUserFullName (userId) {
     if (process.env.SECURITY_ENABLED == false) {
      return Promise.resolve(GP2S_USER)
     }
     return Promise.resolve(userId)

  },

  async getUsername () {
    if (!process.env.SECURITY_ENABLED) {
      return GP2S_USER
    }

    let username
    await _userService.user()
      .then((result) => {
        username = result.data.name
        })
    return username
  },
}
