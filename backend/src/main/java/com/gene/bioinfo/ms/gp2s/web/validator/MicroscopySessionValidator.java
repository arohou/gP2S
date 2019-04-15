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

package com.gene.bioinfo.ms.gp2s.web.validator;

import com.gene.bioinfo.ms.gp2s.domain.Grid;
import com.gene.bioinfo.ms.gp2s.domain.Microscope;
import com.gene.bioinfo.ms.gp2s.domain.MicroscopySession;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class MicroscopySessionValidator extends LabelValidator {
    @Override
    public boolean supports(Class<?> aClass) {
        return super.supports(aClass) && MicroscopySession.class.isAssignableFrom(aClass);
    }

    @Override
    public void doValidate(Object target, Errors errors) {
        final MicroscopySession microscopySession = (MicroscopySession) target;

        validateBasicInformation(microscopySession, errors);
        validateMicroscopeSettings(microscopySession, errors);
        validateExposureSettings(microscopySession, errors);
    }


    private void validateBasicInformation(@NonNull final MicroscopySession microscopySession, @NonNull final Errors
            errors) {
        final Microscope microscope = microscopySession.getMicroscope();
        final Grid grid = microscopySession.getGrid();

        isNotNull(microscope, "microscope", "Microscope", errors);
        isNotNull(grid, "grid", "Grid", errors);
        isNotNull(microscopySession.getGridReturnedToStorage(), "gridReturnedToStorage", "Grid returned to storage", errors);
        isNotNull(microscopySession.getSessionStart(), "sessionStart", "Session start", errors);
        isAfter(microscopySession.getSessionFinish(), "sessionFinish", "Session finish", microscopySession.getSessionStart(), errors);
        if (microscopySession.getNumberOfImagesAcquired() != null) {
            isGreaterOrEqualTo(microscopySession.getNumberOfImagesAcquired(), "numberOfImagesAcquired", "Number of images acquired", 0, errors);
        }

        if (microscope != null) {
            if (microscope.getSampleInsertionMechanism() == Microscope.InsertionMechanismType.SIDE_ENTRY_HOLDER) {
                isNotNull(microscopySession.getSampleHolder(), "sampleHolder", "Sample holder", errors);

                if (microscopySession.getSampleHolder() != null && grid != null && grid.getProtocolType() == Grid.ProtocolType.Cryo
                        && !microscopySession.getSampleHolder().getCryoCapable()) {
                    errors.rejectValue("sampleHolder", "Sample holder should be a cryo-capable holder");
                }
            } else if (microscope.getSampleInsertionMechanism() == Microscope.InsertionMechanismType.AUTO_LOADER && microscopySession.getSampleHolder() != null) {
                errors.rejectValue("sampleHolder", "Sample holder should be empty if the microscope's sample insertion mechanism is \"side-entry holder\"");
            }

        }
        isNotNull(microscopySession.getElectronDetector(), "electronDetector", "Electron detector", errors);
    }

    private void validateMicroscopeSettings(@NonNull final MicroscopySession microscopySession,
                                            @NonNull final Errors
                                                    errors) {
        final Microscope microscope = microscopySession.getMicroscope();

        isNotNull(microscopySession.getExtractionVoltageKV(), "extractionVoltageKV", "Extraction voltage", errors);
        if (microscopySession.getExtractionVoltageKV() != null) {
            isGreaterThan(microscopySession.getExtractionVoltageKV(), "extractionVoltageKV", "Extraction voltage", 0, errors);
        }
        if (microscopySession.getGunLensSetting() != null) {
            isGreaterThan(microscopySession.getGunLensSetting(), "gunLensSetting", "Gun lens setting", 0, errors);
        }
        isNullOrGreaterThan(microscopySession.getCondenser2ApertureDiameter(), "condenser2ApertureDiameter", "Condenser 2 " +
                "aperture diameter", 0, errors);
        if (microscopySession.getObjectiveAperture() != null) {
            if (microscopySession.getObjectiveAperture().getPhasePlate() != null && microscopySession
                    .getObjectiveAperture().getPhasePlate()) {
                isNull(microscopySession.getObjectiveAperture().getDiameter(), "objectiveAperture",
                        "Objective aperture diameter", errors);
            } else if ((microscopySession.getObjectiveAperture().getPhasePlate() == null || !microscopySession.getObjectiveAperture().getPhasePlate())
                    && microscopySession.getObjectiveAperture().getDiameter() != null) {
                isGreaterThan(microscopySession.getObjectiveAperture().getDiameter(), "objectiveAperture",
                        "Objective aperture diameter", 0, errors);
            }
        }
        if (microscope != null && (microscope.getEnergyFilter() == null || !microscope.getEnergyFilter()) && microscopySession.getEnergyFilter() != null && microscopySession.getEnergyFilter()) {
            errors.rejectValue("energyFilter", "Microscope does not have an energy filter");
        }
        if (microscopySession.getEnergyFilter() != null && microscopySession.getEnergyFilter()) {
            isGreaterThan(microscopySession.getEnergyFilterSlitWidth(), "energyFilterSlitWidth", "Default energy " +
                    "filter slit width", 0, errors);
        }
    }

    private void validateExposureSettings(@NonNull final MicroscopySession microscopySession,
                                          @NonNull final Errors
                                                  errors) {
        final Microscope microscope = microscopySession.getMicroscope();

        isNotNull(microscopySession.getNominalMagnification(), "nominalMagnification", "Nominal magnification", errors);

        if (microscopySession.getSpotSize() != null) {
            isGreaterThan(microscopySession.getSpotSize(), "spotSize", "Spot size", 0, errors);
        }
        if (microscopySession.getDiameterOfIlluminatedArea() != null) {
            isGreaterThan(microscopySession.getDiameterOfIlluminatedArea(), "diameterOfIlluminatedArea", "Diameter of illuminated area", 0, errors);
        }
        if (microscopySession.getElectronDetector() != null && !microscopySession.getElectronDetector().getCountingModeAvailable() && microscopySession.getCountingMode()) {
            errors.rejectValue("countingMode", "Counting mode is not available on selected detector");
        }
        if (microscopySession.getCountingMode() != null && microscopySession.getCountingMode()) {
            isNotNull(microscopySession.getExposureRate(), "exposureRate", "Exposure rate", errors);
        }
        if (microscopySession.getExposureRate() != null) {
            isGreaterThan(microscopySession.getExposureRate(), "exposureRate", "Exposure rate", 0, errors);
        }

        isNotNull(microscope, "exposureDuration", "Exposure duration", errors);

        if (microscopySession.getExposureDuration() != null) {
            isGreaterThan(microscopySession.getExposureDuration(), "exposureDuration", "Exposure duration", 0, errors);
        }
        if (microscopySession.getElectronDetector() != null && !microscopySession.getElectronDetector().getDoseFractionationAvailable() && microscopySession.getDoseFractionation()) {
            errors.rejectValue("doseFractionation", "Dose fraction is not available on selected detector");
        }
        if (microscopySession.getNumberOfFrames() != null) {
            isGreaterThan(microscopySession.getNumberOfFrames(), "numberOfFrames", "Number of frames", 0, errors);
        }
        if (microscopySession.getElectronDetector() != null && !microscopySession.getElectronDetector().getSuperResolutionAvailable() && microscopySession.getSuperResolution()) {
            errors.rejectValue("superResolution", "Super resolution is not available on selected detector");
        }
        if (microscopySession.getSuperResolution() != null && microscopySession.getSuperResolution()) {
            isNotNull(microscopySession.getSupersamplingFactor(), "supersamplingFactor", "Supersampling factor", errors);
        } else {
            isNotNull(microscopySession.getPixelBinning(), "pixelBinning", "Pixel binning", errors);
        }
        if (microscopySession.getSupersamplingFactor() != null) {
            isGreaterOrEqualTo(microscopySession.getSupersamplingFactor(), "supersamplingFactor", "Supersampling factor", 2, errors);
        }
        if (microscopySession.getPixelBinning() != null) {
            isGreaterThan(microscopySession.getPixelBinning(), "pixelBinning", "Pixel binning", 0, errors);
        }

        if (microscopySession.getTargetUnderfocusMin() != null) {
            isGreaterThan(microscopySession.getTargetUnderfocusMin(), "targetUnderfocusMin", "Target underfocus min", 0, errors);
        }
        if (microscopySession.getTargetUnderfocusMax() != null) {
            isGreaterThan(microscopySession.getTargetUnderfocusMax(), "targetUnderfocusMax", "Target underfocus max", 0, errors);
        }
        if (microscopySession.getExposuresPerHole() != null) {
            isGreaterThan(microscopySession.getExposuresPerHole(), "exposuresPerHole", "Exposures per hole", 0, errors);
        }
    }
}
