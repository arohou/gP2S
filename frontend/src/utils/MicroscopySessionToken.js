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
 * String tokens for directory path substitution in the Microscopy Session.
 *
 * Created by Cezary Krzyżanowski on 21.03.2018.
 */
import { Enum } from 'enumify'
import escapeStringRegexp from 'escape-string-regexp'

class MicroscopySessionToken extends Enum {
  static createToken (name) {
    const result = {
      string: `\$\{${name}\}`
    }
    result.regExp = new RegExp(escapeStringRegexp(result.string), 'g')

    return result
  }

  toString () {
    return this.string
  }
}

MicroscopySessionToken.initEnum({
  PROJECT_LABEL: MicroscopySessionToken.createToken('ProjectLabel'),
  GRID_ID: MicroscopySessionToken.createToken('GridID'),
  GRID_LABEL: MicroscopySessionToken.createToken('GridLabel'),
  MICROSCOPY_LABEL: MicroscopySessionToken.createToken('MicroscopyLabel'),
  MICROSCOPY_SESSION_ID: MicroscopySessionToken.createToken('MicroscopySessionID'),
  MICROSCOPY_START_DATE: MicroscopySessionToken.createToken('MicroscopyStartDate'),
  MICROSCOPY_START_TIME: MicroscopySessionToken.createToken('MicroscopyStartTime'),
  MICROSCOPE_LABEL: MicroscopySessionToken.createToken('MicroscopeLabel')
})

export default MicroscopySessionToken
