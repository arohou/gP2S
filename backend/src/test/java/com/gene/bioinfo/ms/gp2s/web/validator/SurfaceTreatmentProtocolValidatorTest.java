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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import com.gene.bioinfo.ms.gp2s.domain.SurfaceTreatmentMachine;
import com.gene.bioinfo.ms.gp2s.domain.SurfaceTreatmentProtocol;
import com.gene.bioinfo.ms.gp2s.repository.SurfaceTreatmentMachineRepository;

@RunWith(MockitoJUnitRunner.class)
public class SurfaceTreatmentProtocolValidatorTest {
	
	@Mock
	private SurfaceTreatmentMachineRepository surfaceTreatmentMachineRepository;
	
	@Mock
	private Errors errors;
	
	private SurfaceTreatmentProtocolValidator tested;
	
	@Before
	public void setup() {
		tested = new SurfaceTreatmentProtocolValidator(surfaceTreatmentMachineRepository);
	}
	
	@Test
	public void shouldSupportSurfaceTreatmentProtocol() {
		assertTrue(tested.supports(SurfaceTreatmentProtocol.class));
	}
	
	@Test
	public void shouldRequireLabel() {
		tested.validate(SurfaceTreatmentProtocol.builder().label(null).build(), errors);
		verify(errors).rejectValue(eq("label"), anyString());
	}
	
	@Test
	public void shouldRequirePressure() {
		tested.validate(SurfaceTreatmentProtocol.builder().pressure(null).build(), errors);
		verify(errors).rejectValue(eq("pressure"), anyString());
	}

	@Test
	public void shouldRequirePressureGraterThan0() {
		tested.validate(SurfaceTreatmentProtocol.builder().pressure(0f).build(), errors);
		verify(errors).rejectValue(eq("pressure"), anyString());
	}
	
	@Test
	public void shouldAcceptPressureGraterThan0() {
		tested.validate(SurfaceTreatmentProtocol.builder().pressure(1f).build(), errors);
		verify(errors, times(0)).rejectValue(eq("pressure"), anyString());
	}
	
	@Test
	public void shouldRequireDuration() {
		tested.validate(SurfaceTreatmentProtocol.builder().duration(null).build(), errors);
		verify(errors).rejectValue(eq("duration"), anyString());
	}

	@Test
	public void shouldRequireDurationGraterThan0() {
		tested.validate(SurfaceTreatmentProtocol.builder().duration(0f).build(), errors);
		verify(errors).rejectValue(eq("duration"), anyString());
	}
	
	@Test
	public void shouldAcceptDurationGraterThan0() {
		tested.validate(SurfaceTreatmentProtocol.builder().duration(1f).build(), errors);
		verify(errors, times(0)).rejectValue(eq("duration"), anyString());
	}
	
	@Test
	public void shouldRequireCurrent() {
		tested.validate(SurfaceTreatmentProtocol.builder().current(null).build(), errors);
		verify(errors).rejectValue(eq("current"), anyString());
	}
	
	@Test
	public void shouldRequireCurrentGraterThan0() {
		tested.validate(SurfaceTreatmentProtocol.builder().current(0f).build(), errors);
		verify(errors).rejectValue(eq("current"), anyString());
	}
	
	@Test
	public void shouldAcceptCurrentGraterThan0() {
		tested.validate(SurfaceTreatmentProtocol.builder().current(1f).build(), errors);
		verify(errors, times(0)).rejectValue(eq("current"), anyString());
	}
	
	@Test
	public void shouldRequireMachine() {
		tested.validate(SurfaceTreatmentProtocol.builder().machine(null).build(), errors);
		verify(errors).rejectValue(eq("machine"), anyString());
	}
	
	@Test
	public void shouldRequireMachineId() {
		tested.validate(SurfaceTreatmentProtocol.builder().machine(SurfaceTreatmentMachine.builder().id(null).build()).build(), errors);
		verify(errors).rejectValue(eq("machine"), anyString());
	}
	
	@Test
	public void shouldRequireMachineToExistInDb() {
		tested.validate(SurfaceTreatmentProtocol.builder().machine(SurfaceTreatmentMachine.builder().id(1L).build()).build(), errors);
		verify(errors).rejectValue(eq("machine"), anyString());
	}
	
	@Test
	public void shouldAcceptMachineThatExistInDb() {
		when(surfaceTreatmentMachineRepository.exists(1L)).thenReturn(true);
		tested.validate(SurfaceTreatmentProtocol.builder().machine(SurfaceTreatmentMachine.builder().id(1L).build()).build(), errors);
		verify(errors, times(0)).rejectValue(eq("machine"), anyString());
	}
}
