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

import com.gene.bioinfo.ms.gp2s.domain.SurfaceTreatmentProtocol;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import com.gene.bioinfo.ms.gp2s.repository.SurfaceTreatmentMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isGreaterThan;

@Component
public class SurfaceTreatmentProtocolValidator extends LabelValidator {
	
	private final SurfaceTreatmentMachineRepository surfaceTreatmentMachineRepository;

	@Autowired
	public SurfaceTreatmentProtocolValidator(final SurfaceTreatmentMachineRepository surfaceTreatmentMachineRepository) {
		this.surfaceTreatmentMachineRepository = surfaceTreatmentMachineRepository;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return super.supports(clazz) && SurfaceTreatmentProtocol.class.isAssignableFrom(clazz);
	}
	
    @Override
    public void doValidate(Object target, Errors e) {
    	SurfaceTreatmentProtocol protocol = (SurfaceTreatmentProtocol)target;
		isGreaterThan(protocol.getPressure(), "pressure", "Pressure", 0, e);
		isGreaterThan(protocol.getDuration(), "duration", "Duration", 0, e);
		isGreaterThan(protocol.getCurrent(), "current", "Current", 0, e);

		if(protocol.getChemicalsInAtmosphere() != null && protocol.getChemicalsInAtmosphere().length() > DomainConstants.LONG_STRING_LENGTH) {
    		e.rejectValue("chemicalsInAtmosphere", "Text shouldn't be longer then "+DomainConstants.LONG_STRING_LENGTH+" characters");
    	}
    	if(protocol.getMachine() == null
    			|| protocol.getMachine().getId() == null
    			|| !surfaceTreatmentMachineRepository.exists(protocol.getMachine().getId())) {
    		e.rejectValue("machine", "Surface Treatment Machine can not be found");
    	}
    }
	
}
