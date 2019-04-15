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

import com.gene.bioinfo.ms.gp2s.domain.ElectronDetector;
import com.gene.bioinfo.ms.gp2s.domain.Magnification;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;

import java.util.List;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class ElectronDetectorValidator extends LabelValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return super.supports(clazz) && ElectronDetector.class.isAssignableFrom(clazz);
    }

    @Override
    public void doValidate(Object target, Errors e) {
        ElectronDetector electronDetector = (ElectronDetector) target;

        isNotEmpty(electronDetector.getManufacturer(), "manufacturer", "Manufacturer", DomainConstants.SHORT_STRING_LENGTH, e);
        isNotEmpty(electronDetector.getModel(), "model", "Model", DomainConstants.SHORT_STRING_LENGTH, e);
        isNotNull(electronDetector.getMicroscope(), "microscope", "Microscope", e);
        isGreaterThan(electronDetector.getCountsPerElectronsFactor(), "countsPerElectronsFactor", "Counts-per-electrons-factor", 0.0f, e);
        isNotNull(electronDetector.getCountingModeAvailable(), "countingModeAvailable", "Counting mode", e);
        isGreaterThan(electronDetector.getPixelLinearDimensionUm(), "pixelLinearDimensionUm", "Pixel linear dimension", 0, e);
        isGreaterThan(electronDetector.getNumberOfPixelColumns(), "numberOfPixelColumns", "Number of pixel columns", 0, e);
        isGreaterThan(electronDetector.getNumberOfPixelRows(), "numberOfPixelRows", "Number of pixel rows", 0, e);
        isNotNull(electronDetector.getDoseFractionationAvailable(), "doseFractionationAvailable", "Dose fractionation available", e);
        isNotNull(electronDetector.getSuperResolutionAvailable(), "superResolutionAvailable", "Super resolution available", e);

        List<Magnification> availableMagnifications = electronDetector.getAvailableMagnifications();

        if (!CollectionUtils.isEmpty(availableMagnifications)) {
            for (Magnification magnification : availableMagnifications) {
                isGreaterThan(magnification.getNominalMagnification(), "availableMagnifications", "Nominal magnification", 0, e);
                isGreaterThan(magnification.getCalibratedMagnification(), "availableMagnifications", "Calibrated magnification", 0, e);
            }
        }
    }

}
