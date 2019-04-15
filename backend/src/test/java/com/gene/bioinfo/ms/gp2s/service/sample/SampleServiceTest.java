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

package com.gene.bioinfo.ms.gp2s.service.sample;

import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.*;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.service.sample.SampleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.any;

public class SampleServiceTest {

	private static final String SLUG = "slug";

	private SampleService tested;
	private ProteinRepository proteinRepository;
	private LigandRepository ligandRepository;
	private SampleRepository sampleRepository;

	@Before
	public void setup() {
		sampleRepository = mock(SampleRepository.class);
		ProjectRepository projectRepository = mock(ProjectRepository.class);
		proteinRepository = mock(ProteinRepository.class);
		ligandRepository = mock(LigandRepository.class);

		tested = new SampleService(sampleRepository,
				projectRepository,
				proteinRepository,
				ligandRepository);
	}

	@Test
	public void shouldThrowErrorWhenSampleIsNotAvailable() {
		//given
		final Long INVALID_SAMPLE_ID = -10L;
		given(sampleRepository.findOne(INVALID_SAMPLE_ID)).willReturn(null);

		//when
		try {
			tested.updateAvailableForGridMaking(INVALID_SAMPLE_ID, false);
			//then
			fail("It failed to throw exception when sample is not available.");
		} catch (ResourceNotFoundException e) {
			//pass
		}
	}

	@Test
	public void shouldMakeSampleUnavailableForGridMaking() {
		//given
		final Long SAMPLE_ID = 1L;
		final Sample sample = Sample.builder()
									.id(SAMPLE_ID)
									.availableForGridMaking(true)
									.build();
		given(sampleRepository.findOne(sample.getId())).willReturn(sample);

		//when
		tested.updateAvailableForGridMaking(sample.getId(), false);

		//then
		final ArgumentCaptor<Sample> savedSample = ArgumentCaptor.forClass(Sample.class);
		then(sampleRepository).should(times(1))
				.save(savedSample.capture());
		assertEquals(SAMPLE_ID, savedSample.getValue()
				.getId());
		assertEquals(false, savedSample.getValue()
				.getAvailableForGridMaking());
	}

	@Test
	public void shouldMakeSampleAvailableForGridMaking() {
		//given
		final Long SAMPLE_ID = 1L;
		final Sample sample = Sample.builder()
				.id(SAMPLE_ID)
				.availableForGridMaking(false)
				.build();
		given(sampleRepository.findOne(sample.getId())).willReturn(sample);

		//when
		tested.updateAvailableForGridMaking(sample.getId(), true);

		//then
		final ArgumentCaptor<Sample> savedSample = ArgumentCaptor.forClass(Sample.class);
		then(sampleRepository).should(times(1))
				.save(savedSample.capture());
		assertEquals(SAMPLE_ID, savedSample.getValue()
				.getId());
		assertEquals(true, savedSample.getValue()
				.getAvailableForGridMaking());
	}

	@Test
	public void shouldLeaveSampleAvailableForGridMakingAsItWasBefore() {
		//given
		final long SAMPLE_ID = 1L;
		final Sample sample = Sample.builder()
				.id(SAMPLE_ID)
				.availableForGridMaking(true)
				.build();
		given(sampleRepository.findOne(sample.getId())).willReturn(sample);

		//when
		tested.updateAvailableForGridMaking(sample.getId(), true);

		//then
		then(sampleRepository).should(times(0))
				.save(any(Sample.class));
	}
}
