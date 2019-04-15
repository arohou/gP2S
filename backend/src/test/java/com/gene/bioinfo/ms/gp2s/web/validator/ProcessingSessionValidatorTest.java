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

import com.gene.bioinfo.ms.gp2s.domain.ImageProcessingSoftware;
import com.gene.bioinfo.ms.gp2s.domain.MicroscopySession;
import com.gene.bioinfo.ms.gp2s.domain.ProcessingSession;
import com.gene.bioinfo.ms.gp2s.domain.UsedImageProcessingSoftware;
import com.gene.bioinfo.ms.gp2s.repository.ImageProcessingSoftwareRepository;
import com.gene.bioinfo.ms.gp2s.repository.UsedImageProcessingSoftwareRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProcessingSessionValidatorTest {

    @Mock
    private ImageProcessingSoftwareRepository imageProcessingSoftwareRepository;

    @Mock
    private UsedImageProcessingSoftwareRepository usedImageProcessingSoftwareRepository;

    @Mock
    private Errors errors;

    private ProcessingSessionValidator tested;

    @Before
    public void setup() {
        tested = new ProcessingSessionValidator(imageProcessingSoftwareRepository, usedImageProcessingSoftwareRepository);
    }

    @Test
    public void shouldSupportSurfaceTreatmentProtocol() {
        assertTrue(tested.supports(ProcessingSession.class));
    }

    @Test
    public void shouldRequireLabel() {
        //when
        tested.validate(ProcessingSession.builder().label(null).build(), errors);
        //then
        verify(errors).rejectValue(eq("label"), anyString());
    }

    @Test
    public void shouldNotRequireUsedImageProcessingSoftware() {
        //when
        tested.validate(ProcessingSession.builder().build(), errors);
        //then
        verify(errors, times(0)).rejectValue(eq("usedImageProcessingSoftware"), anyString());
    }

    @Test
    public void shouldRequireImageProcessingSoftwareForUsedImageProcessingSoftware() {
        //given
        UsedImageProcessingSoftware uips = UsedImageProcessingSoftware.builder().build();
        //when
        tested.validate(ProcessingSession.builder().usedImageProcessingSoftware(Collections.singletonList(uips)).build(), errors);
        //then
        verify(errors).rejectValue(eq("usedImageProcessingSoftware.imageProcessingSoftware"), anyString());
    }

    @Test
    public void shouldRequireImageProcessingSoftwareIdOfUsedImageProcessingSoftware() {
        //given
        ImageProcessingSoftware ips = ImageProcessingSoftware.builder()
                .build();
        UsedImageProcessingSoftware uips = UsedImageProcessingSoftware.builder()
                .imageProcessingSoftware(ips)
                .build();
        //when
        tested.validate(ProcessingSession.builder().usedImageProcessingSoftware(Collections.singletonList(uips)).build(), errors);
        //then
        verify(errors).rejectValue(eq("usedImageProcessingSoftware.imageProcessingSoftware.id"), anyString());
    }

    @Test
    public void shouldRequireImageProcessingSoftwareOfUsedImageProcessingSoftwareToExistInDatabase() {
        //given
        ImageProcessingSoftware ips = ImageProcessingSoftware.builder()
                .id(1L)
                .label("software")
                .build();
        UsedImageProcessingSoftware uips = UsedImageProcessingSoftware.builder()
                .imageProcessingSoftware(ips)
                .build();
        //when
        tested.validate(ProcessingSession.builder().usedImageProcessingSoftware(Collections.singletonList(uips)).build(), errors);
        //then
        verify(errors).rejectValue(eq("usedImageProcessingSoftware.imageProcessingSoftware"), anyString());
    }

    @Test
    public void shouldRequireSoftwareVersionForUsedImageProcessingSoftware() {
        //given
        UsedImageProcessingSoftware uips = UsedImageProcessingSoftware.builder().build();
        //when
        tested.validate(ProcessingSession.builder().usedImageProcessingSoftware(Collections.singletonList(uips)).build(), errors);
        //then
        verify(errors).rejectValue(eq("usedImageProcessingSoftware.softwareVersion"), anyString());
    }

    @Test
    public void shouldRequireSoftwareVersionOfUsedImageProcessingSoftwareToBeDefined() {
        //given
        ImageProcessingSoftware ips = ImageProcessingSoftware.builder()
                .id(1L)
                .label("software")
                .build();
        UsedImageProcessingSoftware uips = UsedImageProcessingSoftware.builder()
                .imageProcessingSoftware(ips)
                .build();
        when(imageProcessingSoftwareRepository.getOne(1L)).thenReturn(ips);
        //when
        tested.validate(ProcessingSession.builder().usedImageProcessingSoftware(Collections.singletonList(uips)).build(), errors);
        //then
        verify(errors).rejectValue(eq("usedImageProcessingSoftware.softwareVersion"), anyString());
    }

    @Test
    public void shouldAcceptProcessingSessionWithoutUsedImageProcessingSoftware() {
        //given
        MicroscopySession microscopySession = MicroscopySession.builder().build();
        //when
        tested.validate(ProcessingSession.builder()
                .label("processing session")
                .numberOfMicrographs(1)
                .numberOfPickedParticles(1)
                .microscopySessions(Collections.singleton(microscopySession))
                .build(), errors);
        //then
        verify(errors, times(0)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldAcceptProcessingSessionWithUsedImageProcessingSoftware() {
        //given
        ImageProcessingSoftware ips = ImageProcessingSoftware.builder()
                .id(1L)
                .label("software")
                .softwareVersions(Collections.singletonList("v1.0"))
                .build();
        UsedImageProcessingSoftware uips = UsedImageProcessingSoftware.builder()
                .imageProcessingSoftware(ips)
                .softwareVersion("v1.0")
                .build();
        MicroscopySession microscopySession = MicroscopySession.builder().build();
        when(imageProcessingSoftwareRepository.findOne(1L)).thenReturn(ips);
        //when
        tested.validate(ProcessingSession.builder()
                .label("processing session")
                .numberOfMicrographs(1)
                .numberOfPickedParticles(1)
                .microscopySessions(Collections.singleton(microscopySession))
                .usedImageProcessingSoftware(Collections.singletonList(uips))
                .build(), errors);
        verify(errors, times(0)).rejectValue(anyString(), anyString());
    }
}
