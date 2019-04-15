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

package com.gene.bioinfo.ms.gp2s.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.ElectronDetector;
import com.gene.bioinfo.ms.gp2s.domain.Microscope;
import com.gene.bioinfo.ms.gp2s.service.ElectronDetectorService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestTCase;
import com.gene.bioinfo.ms.gp2s.web.validator.ElectronDetectorValidator;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.annotation.Nullable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ElectronDetectorRestControllerTest extends BaseRestTCase<ElectronDetector> {

    @Mock
    private ElectronDetectorService service;

    @Mock
    private ElectronDetectorValidator validator;

    @Before
    public void setup() throws Exception {
        when(validator.supports(any())).thenReturn(true);
        initBaseRestTests(new ElectronDetectorRestController(service, validator), service);
    }

    @NonNull
    private ElectronDetector.ElectronDetectorBuilder commonCreateDetector() {
        return ElectronDetector.builder()
                .manufacturer("A manufacturer")
                .model("The new model")
                .microscope(Microscope.builder().build())
                .countsPerElectronsFactor(13.37f)
                .countingModeAvailable(true)
                .pixelLinearDimensionUm(14.10f)
                .numberOfPixelColumns(13)
                .numberOfPixelRows(7)
                .doseFractionationAvailable(true)
                .superResolutionAvailable(true);
    }

    @Test
    public void createElectronDetector() throws Exception {
        final ElectronDetector electronDetector = commonCreateDetector()
                .label("label 3")
                .build();
        final String json = new ObjectMapper().writeValueAsString(electronDetector);

        getMockMvc().perform(post(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isCreated());

        verify(service, times(1)).createItem(electronDetector);
    }

    @Override
    public String getUri() {
        return "/api/v1/electron-detector/";
    }

    @Override
    public ElectronDetector createEntity(@Nullable final Long id,
                                         @Nullable final String label,
                                         @Nullable final String slug) {
        return commonCreateDetector()
                .id(id).label(label).slug(slug)
                .build();
    }
}
