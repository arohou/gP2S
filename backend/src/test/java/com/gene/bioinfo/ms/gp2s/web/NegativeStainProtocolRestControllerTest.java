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
import com.gene.bioinfo.ms.gp2s.domain.NegativeStainProtocol;
import com.gene.bioinfo.ms.gp2s.service.NegativeStainProtocolService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestTCase;
import com.gene.bioinfo.ms.gp2s.web.validator.NegativeStainProtocolValidator;
import javax.annotation.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NegativeStainProtocolRestControllerTest extends BaseRestTCase<NegativeStainProtocol> {

    @Mock
    private NegativeStainProtocolService service;

    @Mock
    private NegativeStainProtocolValidator validator;

    @Before
    public void setup() throws Exception {
        when(validator.supports(any())).thenReturn(true);
        initBaseRestTests(new NegativeStainProtocolRestController(service, validator), service);
    }

    @Test
    public void createNegativeStainProtocol() throws Exception {
        final NegativeStainProtocol gridType = NegativeStainProtocol.builder()
                .label("label 3")
                .name("stain name")
                .ph(2.2f)
                .concentration(50f)
                .temperature(-100f)
                .incubationTime(10)
                .description("Lorem Ipsum")
                .build();
        final String json = new ObjectMapper().writeValueAsString(gridType);

        getMockMvc().perform(post(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isCreated());

        verify(service, times(1)).createItem(gridType);
    }

    @Override
    public String getUri() {
        return "/api/v1/negative-stain-protocol/";
    }

    @Override
    public NegativeStainProtocol createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return NegativeStainProtocol.builder().id(id).label(label).slug(slug).name("stain name")
                .ph(2.2f)
                .concentration(50f)
                .temperature(-100f)
                .incubationTime(10)
                .description("Lorem Ipsum").build();
    }
}
