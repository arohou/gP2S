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

const URI = 'attachment/'

import { HTTP } from '@/utils/http-common'

export default class AttachmentService {
  constructor (parentURI) {
    this.parentURI = parentURI
  }

  getDownloadURL (fileId) {
    return HTTP.defaults.baseURL + this.parentURI + URI + 'download/' + fileId
  }

  getUploadURL () {
    return HTTP.defaults.baseURL + this.parentURI + URI + 'upload'
  }

  deleteAttachments (attachmentIds) {
    return HTTP.delete(this.parentURI + 'attachment', {data: attachmentIds})
  }

  deleteAttachment (fileId) {
    return HTTP.delete(this.parentURI + 'attachment/' + fileId)
  }
}
