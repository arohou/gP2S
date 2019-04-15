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
 * Created by Przemyslaw Stankowski on 14.02.2018.
 */

import { HTTP } from '@/utils/http-common'
import AttachmentService from '@/services/AttachmentService'

const URI = 'comment/'

export default class CommentService extends AttachmentService {

  constructor () {
    super(URI)
  }

  listComments (entityType, entityId) {
    return HTTP.get(URI + entityType + '/' + entityId)
  }

  countComments (entityType, entityId) {
    return HTTP.get(URI + entityType + '/' + entityId + '/count')
  }

  addComment (entityType, entityId, content) {
    return HTTP.post(URI + entityType + '/' + entityId, content)
  }

  updateComment (comment, attachmentsModified) {
    return HTTP.put(URI + comment.id + (attachmentsModified ? '?modified=true' : ''), comment.content)
  }

  deleteComment (commentId) {
    return HTTP.delete(URI + commentId)
  }
}
