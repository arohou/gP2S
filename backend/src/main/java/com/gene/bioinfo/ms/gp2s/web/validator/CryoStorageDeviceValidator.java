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

import com.gene.bioinfo.ms.gp2s.domain.CryoStorageDevice;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotEmpty;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotNull;

@Component
public class CryoStorageDeviceValidator extends LabelValidator {
    @Override
    public boolean supports(Class<?> aClass) {
        return super.supports(aClass) && CryoStorageDevice.class.isAssignableFrom(aClass);
    }

    @Override
    public void doValidate(Object target, Errors errors) {
        CryoStorageDevice device = (CryoStorageDevice) target;

        isNotEmpty(device.getManufacturer(), "manufacturer", "Manufacturer", DomainConstants.SHORT_STRING_LENGTH, errors);
        isNotEmpty(device.getModel(), "model", "Model", DomainConstants.SHORT_STRING_LENGTH, errors);
        isNotEmpty(device.getLocation(), "location", "Location", DomainConstants.SHORT_STRING_LENGTH, errors);
        isNotNull(device.getHasCylinders(), "hasCylinders", "Has cylinders", errors);
        isNotNull(device.getHasTubes(), "hasTubes", "Has tubes", errors);
        isNotNull(device.getHasBoxes(), "hasBoxes", "Has boxes", errors);
    }
}
