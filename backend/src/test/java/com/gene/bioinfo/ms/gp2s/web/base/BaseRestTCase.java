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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.service.base.RestService;

import lombok.NonNull;


/**
 * Base class to test {@link BaseRestController} based controllers
 *
 * @author Cezary Krzyżanowski on 11.08.2017.
 */
public abstract class BaseRestTCase<T extends BaseSlugAndLabelEntity> extends BaseTCase<T> {

    protected static final Long LOOKING_ID = 1L;
    protected static final Long ENTITY_ID_1 = 1L;
    protected static final Long ENTITY_ID_2 = 2L;
    protected static final String ENTITY_LABEL_1 = "entity 1";
    protected static final String ENTITY_LABEL_2 = "entity 2";

    protected RestService<T> service;

    protected final List<T> entities = new ArrayList<>();

    protected void initBaseRestTests(@NonNull final BaseRestController<T, ? extends RestService<T>> controller,
                                     @NonNull final RestService<T> service) {
        this.service = service;
        initMockMvc(controller);

        this.entities.add(createEntity(ENTITY_ID_1, ENTITY_LABEL_1, ENTITY_SLUG_1));
        this.entities.add(createEntity(ENTITY_ID_2, ENTITY_LABEL_2, ENTITY_SLUG_2));
    }

    //region COMMON TESTS
    @Test
    public void readEntities() throws Exception {
        when(service.getItems()).thenReturn(entities);

        getMockMvc()
                .perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(ENTITY_ID_1.intValue())))
                .andExpect(jsonPath("$[0].slug", is(ENTITY_SLUG_1)))
                .andExpect(jsonPath("$[0].label", is(ENTITY_LABEL_1)))
                .andExpect(jsonPath("$[1].id", is(ENTITY_ID_2.intValue())))
                .andExpect(jsonPath("$[1].slug", is(ENTITY_SLUG_2)))
                .andExpect(jsonPath("$[1].label", is(ENTITY_LABEL_2)));
    }
    //endregion

    //region ID-BASED TESTS

    @Test
    public void entityByIdNotFound() throws Exception {
        when(service.getItem(LOOKING_ID)).thenThrow(new ResourceNotFoundException());

        getMockMvc().perform(get(URI + LOOKING_ID).contentType(JSON_CONTENT_TYPE))
                    .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleEntityById() throws Exception {
        when(service.getItem(this.entities.get(0).getId())).thenReturn(this.entities.get(0));

        getMockMvc()
                .perform(get(URI + this.entities.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(this.entities.get(0).getId().intValue())))
                .andExpect(jsonPath("$.label", is(ENTITY_LABEL_1)))
                .andExpect(jsonPath("$.slug", is(this.entities.get(0).getSlug())));
    }

    @Test
    public void deleteEntityById() throws Exception {
        getMockMvc().perform(delete(URI + entities.get(1).getId()).contentType(JSON_CONTENT_TYPE))
                    .andExpect(status().isOk());

        verify(service, times(1)).deleteItem(entities.get(1).getId());
    }

    @Test
    public void updateEntityById() throws Exception {

        final T entity = createEntity(entities.get(0).getId(), "label update", null);
        when(service.updateItem(entity)).thenReturn(entity);

        final String json = new ObjectMapper().writeValueAsString(entity);

        getMockMvc().perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isOk())
                    .andExpect(
                            jsonPath("$.label", is("label update")));

        verify(service, times(1)).updateItem(entity);
    }
    //endregion

    //region SLUG-BASED TESTS
    protected static final String LOOKING_SLUG = "a-slug";
    protected static final String ENTITY_SLUG_1 = "entity-slug-1";
    protected static final String ENTITY_SLUG_2 = "entity-slug-2";

    @Test
    public void entityBySlugNotFound() throws Exception {
        when(service.getItem(LOOKING_SLUG)).thenThrow(new ResourceNotFoundException());

        getMockMvc().perform(get(URI + LOOKING_SLUG).contentType(JSON_CONTENT_TYPE))
                    .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleEntityBySlug() throws Exception {
        when(service.getItem(this.entities.get(0).getSlug())).thenReturn(this.entities.get(0));

        getMockMvc()
                .perform(get(URI + this.entities.get(0).getSlug()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(this.entities.get(0).getId().intValue())))
                .andExpect(jsonPath("$.slug", is(this.entities.get(0).getSlug())))
                .andExpect(jsonPath("$.label", is(ENTITY_LABEL_1)));
    }

    @Test
    public void deleteEntityBySlug() throws Exception {
        getMockMvc().perform(delete(URI + entities.get(1).getSlug()).contentType(JSON_CONTENT_TYPE))
                    .andExpect(status().isOk());

        verify(service, times(1)).deleteItem(entities.get(1).getSlug());
    }

    @Test
    public void updateEntityBySlug() throws Exception {

        final T entity = createEntity(null, "label update", entities.get(0).getSlug());
        when(service.updateItem(entity)).thenReturn(entity);

        final String json = new ObjectMapper().writeValueAsString(entity);

        getMockMvc().perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isOk())
                    .andExpect(
                            jsonPath("$.label", is("label update")));

        verify(service, times(1)).updateItem(entity);
    }
    //endregion
}
