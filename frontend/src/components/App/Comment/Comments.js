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

import _ from 'lodash'

class Comments {

  replaceUpdatedComment (comments, updatedComment) {
    if(_.isNil(_.get(updatedComment, 'id'))){
      throw new Error('Updated comment or it\'s ID is undefined!')
    }
    return comments.map(existing => this._replaceIfCommentWasUpdated(existing, updatedComment))
  }

  _replaceIfCommentWasUpdated (existing, updated) {
    if (existing.id === updated.id) {
      return updated
    }
    return existing
  }

  wasEdited (comment) {
    return comment.createdDate !== comment.modifiedDate
  }
}

export default new Comments()
