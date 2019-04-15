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

import com.gene.bioinfo.ms.gp2s.domain.Microscope;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class MicroscopeValidator extends LabelValidator {
    @Override
    public boolean supports(Class<?> aClass) {
        return super.supports(aClass) && Microscope.class.isAssignableFrom(aClass);
    }

    @Override
    public void doValidate(Object target, Errors errors) {
        Microscope microscope = (Microscope) target;

        isNotEmpty(microscope.getManufacturer(), "manufacturer", "Manufacturer", DomainConstants.SHORT_STRING_LENGTH, errors);
        isNotEmpty(microscope.getModel(), "model", "Model", DomainConstants.SHORT_STRING_LENGTH, errors);
        isNotEmpty(microscope.getLocation(), "location", "Location", DomainConstants.SHORT_STRING_LENGTH, errors);
        if (microscope.getDefaultExtractionVoltageKV() != null) {
            isGreaterThan(microscope.getDefaultExtractionVoltageKV(), "defaultExtractionVoltageKV", "Default extraction voltage", 0, errors);
        }
        if (microscope.getDefaultGunLensSetting() != null) {
            isGreaterThan(microscope.getDefaultGunLensSetting(), "defaultGunLensSetting", "Default gun lens setting", 0, errors);
        }
        if (microscope.getAvailableVoltagesKV() == null || microscope.getAvailableVoltagesKV().isEmpty() || microscope.getDefaultVoltageKV() == null) {
            errors.rejectValue("availableVoltagesKV", "At least one available voltage needs to be selected");
        }

        if (microscope.getAvailableVoltagesKV() != null && !ArrayUtils.contains(microscope.getAvailableVoltagesKV().toArray(), microscope.getDefaultVoltageKV())) {
            errors.rejectValue("availableVoltagesKV", "Selected default voltage is not on the list of available voltages");
        }

        isNullOrGreaterThan(microscope.getCondenser1ApertureDiameter(), "condenser1ApertureDiameter",
                "Condenser 1 aperture diameter", 0, errors);
        isNullOrGreaterThan(microscope.getCondenser2ApertureDiameter(), "condenser2ApertureDiameter",
                "Condenser 2 aperture diameter", 0, errors);
        isNullOrGreaterThan(microscope.getCondenser3ApertureDiameter(), "condenser3ApertureDiameter",
                "Condenser 3 aperture diameter", 0, errors);
        isNullOrGreaterThan(microscope.getCondenser4ApertureDiameter(), "condenser4ApertureDiameter",
                "Condenser 4 aperture diameter", 0, errors);

        if (microscope.getEnergyFilter() != null && microscope.getEnergyFilter()) {
            isGreaterThan(microscope.getDefaultEnergyFilterSlitWidth(), "defaultEnergyFilterSlitWidth", "Default energy filter slit width", 0, errors);
        }

        Integer[] correctIndexes = {1, 2, 3, 4};
        isGreaterThan(microscope.getDefaultObjectiveApertureIndex(), "defaultObjectiveApertureIndex", "Default objective aperture", 0, errors);
        isGreaterThan(microscope.getDefaultCondenserApertureIndex(), "defaultCondenserApertureIndex", "Default condenser aperture index", 0, errors);
        if (!ArrayUtils.contains(correctIndexes, microscope.getDefaultObjectiveApertureIndex())) {
            errors.rejectValue("defaultObjectiveApertureIndex", "Incorrect default objective aperture index");
        }
        if (!ArrayUtils.contains(correctIndexes, microscope.getDefaultCondenserApertureIndex())) {
            errors.rejectValue("defaultCondenserApertureIndex", "Incorrect condenser aperture index");
        }

        isNotNull(microscope.getSampleInsertionMechanism(), "sampleInsertionMechanism", "Sample insertion mechanism", errors); // If mapped from a correct value, it should not be null.

        isGreaterThan(microscope.getDefaultSpotSize(), "defaultSpotSize", "Default spot size", 0, errors);
    }
}
