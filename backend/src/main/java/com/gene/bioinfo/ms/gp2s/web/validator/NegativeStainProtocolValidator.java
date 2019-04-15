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

import com.gene.bioinfo.ms.gp2s.domain.NegativeStainProtocol;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.PhysicalConstants.ABSOLUTE_ZERO_IN_CELSIUS;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class NegativeStainProtocolValidator extends LabelValidator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return super.supports(clazz) && NegativeStainProtocol.class.isAssignableFrom(clazz);
	}
	
    @Override
    public void doValidate(Object target, Errors e) {
    	NegativeStainProtocol protocol = (NegativeStainProtocol)target;

    	isNotEmpty(protocol.getName(), "name", "Name of stain", DomainConstants.SHORT_STRING_LENGTH, e);
    	isNotEmpty(protocol.getDescription(), "description", "Description", DomainConstants.LONG_STRING_LENGTH, e);
    	isGreaterThan(protocol.getPh(), "ph", "pH of stain", 0, e);
    	isGreaterThan(protocol.getIncubationTime(), "incubationTime", "Incubation time of stain before blotting", 0, e);
    	isNullOrGreaterOrEqualTo(protocol.getTemperature(), "temperature", "Temperature", ABSOLUTE_ZERO_IN_CELSIUS, e);
    	isBetween(protocol.getConcentration(), "concentration", "Concentration of stain", 0, 100, e);
    }
	
}
