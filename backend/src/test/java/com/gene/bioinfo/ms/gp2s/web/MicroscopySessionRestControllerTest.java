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
import com.gene.bioinfo.ms.gp2s.domain.MicroscopySession;
import com.gene.bioinfo.ms.gp2s.service.MicroscopySessionService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestTCase;
import com.gene.bioinfo.ms.gp2s.web.validator.MicroscopySessionValidator;
import org.junit.Before;
import org.mockito.Mock;

import javax.annotation.Nullable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MicroscopySessionRestControllerTest extends BaseProjectRestTCase<MicroscopySession> {
    @Mock
    private MicroscopySessionService service;

    @Mock
    private MicroscopySessionValidator validator;

    @Override
    public String getUri() {
        return "/api/v1/microscopy-session/";
    }

    @Before
    public void setup() throws Exception {
        when(validator.supports(any())).thenReturn(true);
        initProjectBaseRestTests(new MicroscopySessionRestController(service, validator), service);
    }

    public void createMicroscopySession() throws Exception {
        final MicroscopySession microscopySession = MicroscopySession.builder().build();

        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc().perform(post(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isCreated());

        verify(service, times(1)).createItem(microscopySession);
    }

    @Override
    public MicroscopySession createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return MicroscopySession.builder().id(id).label(label).slug(slug).build();
    }
}
