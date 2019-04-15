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

import com.gene.bioinfo.ms.gp2s.domain.VitrificationProtocol;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import com.gene.bioinfo.ms.gp2s.repository.BlottingPaperRepository;
import com.gene.bioinfo.ms.gp2s.repository.VitrificationMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.PhysicalConstants.ABSOLUTE_ZERO_IN_CELSIUS;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class VitrificationProtocolValidator extends LabelValidator {

	private final VitrificationMachineRepository vitrificationMachineRepository;
	private final BlottingPaperRepository blottingPaperRepository;

	@Autowired
	public VitrificationProtocolValidator(final VitrificationMachineRepository vitrificationMachineRepository,
										  final BlottingPaperRepository blottingPaperRepository) {
		this.vitrificationMachineRepository = vitrificationMachineRepository;
		this.blottingPaperRepository = blottingPaperRepository;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return super.supports(clazz) && VitrificationProtocol.class.isAssignableFrom(clazz);
	}
	
    @Override
    public void doValidate(Object target, Errors e) {
		VitrificationProtocol protocol = (VitrificationProtocol)target;

		isBetweenInclusive(protocol.getRelativeHumidity(), "relativeHumidity", "Relative humidity", 0, 100, e);

		isGreaterOrEqualTo(protocol.getBlotTime(), "blotTime", "Blot time", 0, e);
		isGreaterOrEqualTo(protocol.getWaitTime(), "waitTime", "Wait time", 0, e);
		isGreaterOrEqualTo(protocol.getDrainTime(), "drainTime", "Drain time", 0, e);
		isNotNull(protocol.getBlotForce(), "blotForce", "Blot force", e);

		isNotEmpty(protocol.getDescription(), "description", "Description", DomainConstants.LONG_STRING_LENGTH, e);

		isGreaterThan(protocol.getNumberOfBlots(), "numberOfBlots", "Number of blots", 0, e);
		isGreaterThan(protocol.getNumberOfSampleApplications(), "numberOfSampleApplications", "Number of sample applications", 0, e);

		if(protocol.getTemperature() == null || protocol.getTemperature() < ABSOLUTE_ZERO_IN_CELSIUS) {
			e.rejectValue("temperature", "Temperature should be a number greater than or equal to " + ABSOLUTE_ZERO_IN_CELSIUS);
		}

    	if(protocol.getBlottingPaper() == null
    			|| protocol.getBlottingPaper().getId() == null
    			|| !blottingPaperRepository.exists(protocol.getBlottingPaper().getId())) {
    		e.rejectValue("blottingPaper", "Blotting paper can not be found");
    	}

		if(protocol.getVitrificationMachine() == null
				|| protocol.getVitrificationMachine().getId() == null
				|| !vitrificationMachineRepository.exists(protocol.getVitrificationMachine().getId())) {
			e.rejectValue("VitrificationMachine", "Vitrification machine can not be found");
		}
    }
	
}
