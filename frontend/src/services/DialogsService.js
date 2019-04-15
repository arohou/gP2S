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
 * Facade for displaying global dialogs.
 *
 * Created by Cezary Krzyżanowski on 03.08.2017.
 */

import { Message, MessageBox } from 'element-ui'

export default {
  showError (message) {
    return Message({
      showClose: true,
      message: message,
      type: 'error'
    })
  },

  showConfirmDialog (message, title, options) {
    return MessageBox.confirm(
      message, title, {
        showClose: false,
        closeOnClickModal: false,
        closeOnPressEscape: false,
        ...options
      }
    )
  },

  optimisticLockingError () {
    const message = 'Entity was updated or deleted by another user.' +
      'Your changes were not applied. Click reload to fill the form again.'
    return MessageBox.alert(
      message,
      'Entity modified', {
        type: 'error',
        confirmButtonText: 'Reload',
        showClose: false
      }
    )
  }
}
