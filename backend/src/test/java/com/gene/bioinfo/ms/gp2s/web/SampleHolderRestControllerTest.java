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
import com.gene.bioinfo.ms.gp2s.domain.SampleHolder;
import com.gene.bioinfo.ms.gp2s.service.SampleHolderService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestTCase;
import com.gene.bioinfo.ms.gp2s.web.validator.SampleHolderValidator;
import javax.annotation.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SampleHolderRestControllerTest extends BaseRestTCase<SampleHolder> {

    @Mock
    private SampleHolderService service;

    @Mock
    private SampleHolderValidator validator;

    @Before
    public void setup() throws Exception {
        when(validator.supports(any())).thenReturn(true);
        initBaseRestTests(new SampleHolderRestController(service, validator), service);
    }

    @Test
    public void createSampleHolder() throws Exception {
        final SampleHolder sampleHolder = SampleHolder.builder()
                .label("A label")
                .manufacturer("A manufacturer")
                .model("A model")
                .location("New York")
                .cryoCapable(true)
                .maximumTilt(5f)
                .doubleTilt(false)
                .build();
        final String json = new ObjectMapper().writeValueAsString(sampleHolder);

        getMockMvc().perform(post(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isCreated());

        verify(service, times(1)).createItem(sampleHolder);
    }

    @Override
    public String getUri() {
        return "/api/v1/sample-holder/";
    }

    @Override
    public SampleHolder createEntity(@Nullable final Long id,
                                     @Nullable final String label,
                                     @Nullable final String slug) {
        return SampleHolder.builder().id(id).label(label).slug(slug)
                .manufacturer("A manufacturer")
                .model("A model")
                .location("New York")
                .cryoCapable(true)
                .maximumTilt(5f)
                .doubleTilt(false)
                .build();
    }
}
