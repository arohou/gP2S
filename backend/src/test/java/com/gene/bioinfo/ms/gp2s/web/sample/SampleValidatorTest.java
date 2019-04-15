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

package com.gene.bioinfo.ms.gp2s.web.sample;

import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.domain.sample.ProteinComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.repository.LigandRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProteinRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SampleValidatorTest {

    @Mock
    private ProteinRepository proteinRepository;

    @Mock
    private LigandRepository ligandRepository;

    @Mock
    private Errors errors;

    private SampleValidator tested;

    @Before
    public void setup() {
        tested = Mockito.spy(new SampleValidator(proteinRepository, ligandRepository));
    }

    @Test
    public void shouldNotAllowEmptyLabel() {
        when(errors.getFieldValue(any())).thenReturn(null);
        tested.validate(Sample.builder().build(), errors);
        verify(errors).rejectValue(eq("label"), any());
    }

    @Test
    public void shouldNotAllowIncubationTimeLessThan0() {
        when(errors.getFieldValue(any())).thenReturn(null);
        tested.validate(Sample.builder().incubationTime(-1f).build(), errors);
        verify(errors).rejectValue(eq("incubationTime"), any());
    }

	@Test
	public void shouldNotAllowIncubationTempLessThan273_15() {
		tested.validate(Sample.builder().incubationTemperature(-273.16f).build(), errors);
		verify(errors).rejectValue(eq("incubationTemperature"), any());
	}

	@Test
	public void shouldNotAllowWithoutComponents() {
		tested.validate(Sample.builder().build(), errors);
		verify(errors).rejectValue(eq("proteinComponent"), any());
	}

	@Test
	public void shouldNotAllowEmptyFinalConcentrationOfComponent() {
		Sample sample = Sample.builder().label("label").build();
		Protein aliquot = Protein.builder().id(1L).build();
		when(proteinRepository.findOne(1L)).thenReturn(aliquot);
		sample.setProteinComponent(Collections.singletonList(ProteinComponent.builder()
                                                                             .aliquot(aliquot)
                                                                             .build()));
		tested.validate(sample, errors);
		verify(errors).rejectValue(eq("proteinComponent[0].finalConcentration"), any());
	}

	@Test
	public void shouldNotAllow0AsFinalConcentrationOfComponent() {
		Sample sample = Sample.builder().label("label").build();
		Protein aliquot = Protein.builder().id(1L).build();
		when(proteinRepository.findOne(1L)).thenReturn(aliquot);
		sample.setProteinComponent(Collections.singletonList(ProteinComponent.builder()
                                                                             .finalConcentration(0f)
                                                                             .aliquot(aliquot)
                                                                             .build()));
		tested.validate(sample, errors);
		verify(errors).rejectValue(eq("proteinComponent[0].finalConcentration"), any());
	}

    @Test
    public void shouldNotAllowLessThan0ConcentrationOfComponent() {
        Sample sample = Sample.builder().build();
        sample.setProteinComponent(Collections.singletonList(ProteinComponent.builder().finalConcentration(-1f).build()));
        tested.validate(sample, errors);
        verify(errors).rejectValue(eq("proteinComponent[0].finalConcentration"), any());
    }

    @Test
    public void shouldRequireAliquot() {
        Sample sample = Sample.builder().build();
        sample.setProteinComponent(Collections.singletonList(ProteinComponent.builder().aliquot(null).build()));
        tested.validate(sample, errors);
        verify(errors).rejectValue(eq("proteinComponent[0].aliquot"), any());
    }

    @Test
    public void shouldRequireValidAliquot() {
        Sample sample = Sample.builder().build();
        Protein aliquot = Protein.builder().build();
        sample.setProteinComponent(Collections.singletonList(ProteinComponent.builder().aliquot(aliquot).build()));
        tested.validate(sample, errors);
        verify(errors).rejectValue(eq("proteinComponent[0].aliquot"), any());
    }

    @Test
    public void shouldNotAllowNonExistingAliquot() {
        Sample sample = Sample.builder().build();
        Protein aliquot = Protein.builder().id(1L).build();
        sample.setProteinComponent(Collections.singletonList(ProteinComponent.builder()
                .aliquot(aliquot)
                .build()));
        tested.validate(sample, errors);
        verify(errors).rejectValue(eq("proteinComponent[0].aliquot"), any());
    }

    @Test
    public void shouldNotReturnAnyError() {
        Sample sample = Sample.builder().label("label").incubationTime(1f).incubationTemperature(-100f).availableForGridMaking(true).build();
        Protein aliquot = Protein.builder().id(1L).build();
        when(proteinRepository.findOne(1L)).thenReturn(aliquot);
        sample.setProteinComponent(Collections.singletonList(ProteinComponent.builder()
                .aliquot(aliquot)
                .finalConcentration(1f)
                .build()));
        tested.validate(sample, errors);
        verify(errors, times(0)).rejectValue(any(), any());
    }
}
