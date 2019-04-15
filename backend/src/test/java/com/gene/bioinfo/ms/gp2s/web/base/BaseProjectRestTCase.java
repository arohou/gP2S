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

package com.gene.bioinfo.ms.gp2s.web.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.service.base.BaseProjectRestService;
import com.gene.bioinfo.ms.gp2s.service.base.RestService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Base class to test {@link Project} related {@link BaseRestController}s
 *
 * @author Cezary Krzyżanowski on 11.08.2017.
 */
public abstract class BaseProjectRestTCase<T extends BaseSlugAndLabelEntity> extends BaseRestTCase<T> {
    protected static final Long PROJECT_ID_1 = 1L;
    protected static final Long PROJECT_ID_2 = 2L;
    protected static final String PROJECT_SLUG_1 = "project-1-slug";
    protected static final String PROJECT_SLUG_2 = "project-2-slug";
    protected static final String PROJECT_LABEL_1 = "project 1";
    protected static final String PROJECT_LABEL_2 = "project 2";
    protected static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_2 = "project-management-system-slug 2";

    protected BaseProjectRestService<T> service;

    protected List<Project> projects;

    protected void initProjectBaseRestTests(BaseRestController<T, ? extends RestService<T>> controller,
                                            BaseProjectRestService<T> service) {
        this.initBaseRestTests(controller, service);
        this.service = service;

        projects = new ArrayList<>(2);
        projects.add(Project.builder()
                .id(PROJECT_ID_1)
                .slug(PROJECT_SLUG_1)
                .label(PROJECT_LABEL_1)
                .projectManagementSystemSlug(PROJECT_MANAGEMENT_SYSTEM_SLUG_2)
                .build());
        projects.add(Project.builder()
                .id(PROJECT_ID_2)
                .slug(PROJECT_SLUG_2)
                .label(PROJECT_LABEL_2)
                .projectManagementSystemSlug(PROJECT_MANAGEMENT_SYSTEM_SLUG_2)
                .build());
    }

    @Test
    public void readEntityProjectsById() throws Exception {
        when(service.getItemProjects(LOOKING_ID)).thenReturn(projects);

        getMockMvc()
                .perform(get(URI + LOOKING_ID + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(PROJECT_ID_1.intValue())))
                .andExpect(jsonPath("$[0].slug", is(PROJECT_SLUG_1)))
                .andExpect(jsonPath("$[0].label", is(PROJECT_LABEL_1)))
                .andExpect(jsonPath("$[1].id", is(PROJECT_ID_2.intValue())))
                .andExpect(jsonPath("$[1].slug", is(PROJECT_SLUG_2)))
                .andExpect(jsonPath("$[1].label", is(PROJECT_LABEL_2)));
    }

    @Test
    public void readEntityProjectsBySlug() throws Exception {
        when(service.getItemProjects(LOOKING_SLUG)).thenReturn(projects);

        getMockMvc()
                .perform(get(URI + LOOKING_SLUG + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(PROJECT_ID_1.intValue())))
                .andExpect(jsonPath("$[0].slug", is(PROJECT_SLUG_1)))
                .andExpect(jsonPath("$[0].label", is(PROJECT_LABEL_1)))
                .andExpect(jsonPath("$[1].id", is(PROJECT_ID_2.intValue())))
                .andExpect(jsonPath("$[1].slug", is(PROJECT_SLUG_2)))
                .andExpect(jsonPath("$[1].label", is(PROJECT_LABEL_2)));
    }

    @Test
    public void createEntityByProjectId() throws Exception {
        final T entity = createEntity(null, ENTITY_LABEL_1, null);
        final String json = new ObjectMapper().writeValueAsString(entity);

        getMockMvc().perform(post(URI + PROJECT_ID_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated());

        verify(service, times(1)).createItem(entity, PROJECT_ID_1);
    }

    @Test
    public void createEntityByProjectSlug() throws Exception {
        final T entity = createEntity(null, ENTITY_LABEL_1, null);
        final String json = new ObjectMapper().writeValueAsString(entity);

        getMockMvc().perform(post(URI + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated());

        verify(service, times(1)).createItem(entity, PROJECT_SLUG_1);
    }
}
