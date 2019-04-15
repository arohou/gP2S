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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.GridType;
import com.gene.bioinfo.ms.gp2s.service.GridTypeService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestTCase;
import com.gene.bioinfo.ms.gp2s.web.validator.GridTypeValidator;

public class GridTypeRestControllerTest extends BaseRestTCase<GridType> {

    @Mock
    private GridTypeService service;
    
    @Mock
	private GridTypeValidator validator;

    @Before
    public void setup() throws Exception {
    	when(validator.supports(any())).thenReturn(true);
        initBaseRestTests(new GridTypeRestController(service, validator), service);
    }

    @Test
    public void createGridType() throws Exception {
        final GridType gridType = GridType.builder()
        		.label("label 3")
        		.manufacturer("Simens")
        		.description("Lorem Ipsum")
        		.build();
        final String json = new ObjectMapper().writeValueAsString(gridType);

        getMockMvc().perform(post(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isCreated());

        verify(service, times(1)).createItem(gridType);
    }

    @Override
    public String getUri() {
        return "/api/v1/grid-type/";
    }

    @Override
    public GridType createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return GridType.builder().id(id).label(label).slug(slug).manufacturer("Simens")
        		.description("Lorem Ipsum").build();
    }
}
