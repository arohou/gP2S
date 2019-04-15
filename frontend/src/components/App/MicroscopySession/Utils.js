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

export default {
  methods: {
    /**
     * Image pixel size is computed from the binning or super-resolution factor, the detector's physical pixel size, the post-mag (of the detector), the calibrated mag (of the microscope):
     * - if super-resolution was used: image_pixel_size_in_A = detector_physical_pixel_size_in_um * 10000.0
     *   / calibrated_magnification_unitless / post_magnification_factor_for_detector / supersampling_factor
     * - if super-resolution was not used: image_pixel_size_A = detector_physical_pixel_size_in_um * 10000.0
     *   / calibrated_magnification_unitless / post_magnification_factor_for_detector * binning_factor.
     * @param microscopySession Session to get the calculation from.
     * @returns Calculated image pixel size.
     */
    calculateImagePixelSize (microscopySession) {
      let imagePixelSize = null
      if (microscopySession.electronDetector && microscopySession.electronDetector.pixelLinearDimensionUm
        && microscopySession.calibratedMagnification) {
        if (microscopySession.superResolution && microscopySession.supersamplingFactor && microscopySession.supersamplingFactor > 0) {
          imagePixelSize = microscopySession.electronDetector.pixelLinearDimensionUm * 10000
            / (microscopySession.calibratedMagnification * microscopySession.supersamplingFactor)
        } else if (!microscopySession.superResolution && microscopySession.pixelBinning && microscopySession.pixelBinning > 0) {
          imagePixelSize = microscopySession.electronDetector.pixelLinearDimensionUm * 10000 * microscopySession.pixelBinning
            / microscopySession.calibratedMagnification
        }
      }
      return imagePixelSize
    },

    /**
     * The exposure rate is calculated:
     * - if counting_mode == false, compute exposure rate as follows: Exposure_rate_eA2s
     *   = exposure_rate_cps / detector_counts_per_electron_factor / (image_pixel_size_A)^2
     * - if counting_mode == true, compute exposure rate as follows: Exposure_rate_eA2s
     *   = exposure_rate_eps / (image_pixel_size_A)^2.
     * @param microscopySession Session to get the calculation from.
     * @returns Calculated exposure rate.
     */
    calculateExposureRate (microscopySession) {
      let imagePixelSize = this.calculateImagePixelSize(microscopySession)
      let microscopeExposureRate = parseFloat(microscopySession.exposureRate)
      return !imagePixelSize || imagePixelSize <= 0 || !microscopeExposureRate || microscopeExposureRate <= 0 || !microscopySession.electronDetector
      || (microscopySession.countingMode && !microscopySession.electronDetector.countsPerElectronsFactor) ? null
        : microscopySession.exposureRate / ((microscopySession.countingMode ? 1 : microscopySession.electronDetector.countsPerElectronsFactor) * Math.pow(imagePixelSize, 2))
    },

    /**
     * Total exposure is calculated:
     * total_exposure = exposure_duration [seconds, given by user] * exposure_rate [calculated above, in electrons/Å ^2^ /s].
     * @param microscopySession Session to get the calculation from.
     * @returns Calculated total exposure.
     */
    calculateTotalExposure (microscopySession) {
      let exposureRate = this.calculateExposureRate(microscopySession)
      let exposureDuration = parseFloat(microscopySession.exposureDuration)

      return !(exposureDuration > 0) || !exposureRate ? null : microscopySession.exposureDuration * exposureRate
    },

    /**
     * Frame duration is calculated:
     * frame_duration = exposure_duration / number_of_frames.
     * @param microscopySession Session to get the calculation from.
     * @returns Calculated frame duration.
     */
    calculateFrameDuration (microscopySession) {
      let exposureDuration = parseFloat(microscopySession.exposureDuration)
      let numberOfFrames = parseInt(microscopySession.numberOfFrames)

      return !(exposureDuration > 0) || !(numberOfFrames > 0) ? null
        : exposureDuration / microscopySession.numberOfFrames
    },

    /**
     * Exposure per frame is calculated:
     * exposure_per_frame = total_exposure [calculated above] / number of frames.
     * @param microscopySession Session to get the calculation from.
     * @returns Calculated exposure per frame.
     */
    calculateExposurePerFrame (microscopySession) {
      let totalExposure = this.calculateTotalExposure(microscopySession)
      let numberOfFrames = parseInt(microscopySession.numberOfFrames)

      return !totalExposure || !(numberOfFrames > 0) ? null : totalExposure / numberOfFrames
    },

    /**
     * Check if objectiveAperture can be choose for microscope
     * @param microscope
     * @param objectiveAperture
     * @returns {boolean}
     */
    isApplicableObjectiveAperture: function (microscope, objectiveAperture) {
      const availableApertures = [microscope.objectiveAperture1,
        microscope.objectiveAperture2, microscope.objectiveAperture3,
        microscope.objectiveAperture4]

      if (objectiveAperture) {
        for (let i = 0; i < availableApertures.length; i++) {
          if (!!availableApertures[i].phasePlate === !!objectiveAperture.phasePlate
            && (availableApertures[i].diameter === objectiveAperture.diameter
              || objectiveAperture.diameter !== 0 && !objectiveAperture.diameter && !availableApertures[i].diameter)) {
            return true
          }
        }
      }
      return false
    },

    /**
     * Check if accelerationVoltageKV is on microscope list
     * @param microscope
     * @param accelerationVoltageKV
     * @returns {boolean}
     */
    isApplicableAccelerationVoltageKV: function (microscope, accelerationVoltageKV) {
      return microscope.availableVoltagesKV.indexOf(accelerationVoltageKV) >= 0
    },

    /**
     * Check if condenser2ApertureDiameter can be choose for microscope
     * @param microscope
     * @param condenser2ApertureDiameter
     * @returns {boolean}
     */
    isApplicableCondenser2ApertureDiameter: function (microscope, condenser2ApertureDiameter) {
      return [microscope.condenser1ApertureDiameter,
        microscope.condenser2ApertureDiameter,
        microscope.condenser3ApertureDiameter,
        microscope.condenser4ApertureDiameter]
        .indexOf(condenser2ApertureDiameter) >= 0
    },

    isApplicableMagnification: function (electronDetector, magnification) {
      return electronDetector.availableMagnifications.map(m => m.nominalMagnification).indexOf(magnification) >= 0
    },

    /**
     * Returns default objectiveAperture from microscope
     * @param microscope
     * @returns {ObjectiveAperture}
     */
    getDefaultObjectiveAperture: function (microscope) {
      return [microscope.objectiveAperture1,
        microscope.objectiveAperture2, microscope.objectiveAperture3,
        microscope.objectiveAperture4][(microscope.defaultObjectiveApertureIndex || 1) - 1]
    },

    /**
     * Returns default value for condenser2ApertureDiameter from microscope
     * @param microscope
     * @returns {*}
     */
    getDefaultCondenser2ApertureDiameter: function (microscope) {
      return [microscope.condenser1ApertureDiameter,
        microscope.condenser2ApertureDiameter, microscope.condenser3ApertureDiameter,
        microscope.condenser4ApertureDiameter][(microscope.defaultCondenserApertureIndex || 1) - 1]
    }
  }
}
