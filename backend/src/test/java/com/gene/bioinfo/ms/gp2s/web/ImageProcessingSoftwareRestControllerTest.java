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
import com.gene.bioinfo.ms.gp2s.domain.ImageProcessingSoftware;
import com.gene.bioinfo.ms.gp2s.service.ImageProcessingSoftwareService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestTCase;
import com.gene.bioinfo.ms.gp2s.web.validator.ImageProcessingSoftwareValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.annotation.Nullable;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageProcessingSoftwareRestControllerTest extends BaseRestTCase<ImageProcessingSoftware> {

    @Mock
    private ImageProcessingSoftwareService service;

    @Mock
    private ImageProcessingSoftwareValidator validator;

    @Before
    public void setup() throws Exception {
        when(validator.supports(any())).thenReturn(true);
        initBaseRestTests(new ImageProcessingSoftwareRestController(service, validator), service);
    }

    @Test
    public void createImageProcessingSoftware() throws Exception {
        final ImageProcessingSoftware imageProcessingSoftware = ImageProcessingSoftware.builder()
                .label("label 3")
                .softwareVersions(Collections.singletonList("7.0"))
                .build();
        final String json = new ObjectMapper().writeValueAsString(imageProcessingSoftware);

        getMockMvc().perform(post(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isCreated());

        verify(service, times(1)).createItem(imageProcessingSoftware);
    }

    @Override
    public String getUri() {
        return "/api/v1/image-processing-software/";
    }

    @Override
    public ImageProcessingSoftware createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return ImageProcessingSoftware.builder().id(id).label(label).slug(slug)
                .softwareVersions(Collections.singletonList("1.337"))
                .build();
    }
}
