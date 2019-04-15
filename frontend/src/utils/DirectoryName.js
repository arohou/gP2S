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
 * Replaces path tokens
 *
 * Created by Cezary Krzyżanowski on 21.03.2018.
 */

import _ from 'lodash'
import sanitize from 'sanitize-filename'

export default class DirectoryName {

  constructor (tokenString, tokenToSubstitutionPairs) {
    if (_.isNil(tokenString)) {
      throw new Error('tokenString can\'t be empty')
    } else if (_.isNil(tokenToSubstitutionPairs)) {
      throw new Error('tokenToSubstitutionPairs can\'t be empty')
    }

    this.value = this.substituteTokens(tokenString, tokenToSubstitutionPairs)
  }

  get value () {
    return this._value
  }

  set value (value) {
    this._value = sanitize(this._underscoreInsteadOfWhitespace(value))
  }

  static fromTokenString (tokenString, tokenToSubstitutionPairs) {
    return new DirectoryName(tokenString, tokenToSubstitutionPairs)
  }

  substituteTokens (string, tokenToSubstitutionPairs) {
    let result = '' + string
    for (let [regExp, substitution] of this._regExpSubstitutionPairs(tokenToSubstitutionPairs)) {
      if (!_.isNil(substitution)) {
        result = result.replace(regExp, substitution)
      }
    }

    return result
  }

  * _regExpSubstitutionPairs (tokenToSubstitutionPairs) {
    for (let [token, substitution] of tokenToSubstitutionPairs) {
      let regExp = _.get(token, 'regExp')
      if (!regExp) { continue }

      yield [regExp, substitution]
    }
  }

  _underscoreInsteadOfWhitespace (value) {
    return value.replace(/\s/g, '_')
  }

  toString () {
    return this.value.toString()
  }
}
