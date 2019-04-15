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
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.service.ProjectService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestTCase;
import com.gene.bioinfo.ms.gp2s.web.validator.ProjectValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.annotation.Nullable;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProjectRestControllerTest extends BaseRestTCase<Project> {

    @Mock
    private ProjectService projectService;

    @Mock
    private ProjectValidator validator;

    @Before
    public void setup() throws Exception {
        when(validator.supports(any())).thenReturn(true);
        initBaseRestTests(new ProjectRestController(projectService, validator), projectService);
    }

    @Test
    public void projectNotFound() throws Exception {
        final String projectSlug = "test-project-slug";
        when(service.getItem(projectSlug)).thenThrow(new ResourceNotFoundException());

        getMockMvc().perform(get(URI + projectSlug).contentType(JSON_CONTENT_TYPE)).andExpect(status().isNotFound());
    }

    @Test
    public void readSingleProject() throws Exception {
        when(service.getItem(ENTITY_SLUG_1)).thenReturn(this.entities.get(0));

        getMockMvc()
                .perform(get(URI + ENTITY_SLUG_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.slug", is(ENTITY_SLUG_1)))
                .andExpect(jsonPath("$.label", is(ENTITY_LABEL_1)));
    }

    @Test
    public void readProjects() throws Exception {
        when(service.getItems()).thenReturn(entities);

        getMockMvc()
                .perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].slug", is(ENTITY_SLUG_1)))
                .andExpect(jsonPath("$[0].label", is(ENTITY_LABEL_1)))
                .andExpect(jsonPath("$[1].slug", is(ENTITY_SLUG_2)))
                .andExpect(jsonPath("$[1].label", is(ENTITY_LABEL_2)));
    }

    @Test
    public void createProjects() throws Exception {
        final Project project = Project.builder().label("label 3").build();
        final String json = new ObjectMapper().writeValueAsString(project);

        getMockMvc().perform(post(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isCreated());

        verify(service, times(1)).createItem(project);
    }

    @Test
    public void deleteProjects() throws Exception {
        getMockMvc().perform(delete(URI + ENTITY_SLUG_2).contentType(JSON_CONTENT_TYPE)).andExpect(
                status().isOk());

        verify(service, times(1)).deleteItem(ENTITY_SLUG_2);
    }

    @Test
    public void updateProjects() throws Exception {

        final Project project = Project.builder().label("label update").slug(ENTITY_SLUG_1).build();
        when(service.updateItem(project)).thenReturn(project);

        final String json = new ObjectMapper().writeValueAsString(project);

        getMockMvc().perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.label", is("label update")));

        verify(service, times(1)).updateItem(project);
    }

    @Override
    public String getUri() {
        return "/api/v1/project/";
    }

    @Override
    public Project createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return Project.builder().id(id).label(label).slug(slug).build();
    }
}
