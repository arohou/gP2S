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
 * Created by Cezary Krzyżanowski on 06.07.2018.
 */
import Big from 'big.js'

export default class ConcentrationConversionService {

  uMToMgPerMl (concentrationInUm, molecularWeightDa) {
    const concentration = new Big(concentrationInUm)
    if (concentration.lte(new Big(0))) {
      throw new TypeError('Concentration has to be > 0 but is ' + concentrationInUm)
    }
    const mw = new Big(molecularWeightDa)
    if (mw.lte(new Big(0))) {
      throw new TypeError('Molecular weight has to be > 0 but is ' + molecularWeightDa)
    }

    return concentration
      .times(mw)
      .div(new Big(1000000))
      .toFixed(3)
  }

  mgPerMlToUm (concentrationInMgPerMl, molecularWeightDa) {
    const concentration = new Big(concentrationInMgPerMl)
    if (concentration.lte(new Big(0))) {
      throw new TypeError('Concentration has to be > 0 but is ' + concentrationInMgPerMl)
    }

    const mw = new Big(molecularWeightDa)
    if (mw.lte(new Big(0))) {
      throw new TypeError('Molecular weight has to be > 0 but is ' + molecularWeightDa)
    }

    return concentration
      .times(new Big(1000000))
      .div(mw)
      .toFixed(3)
  }
}
