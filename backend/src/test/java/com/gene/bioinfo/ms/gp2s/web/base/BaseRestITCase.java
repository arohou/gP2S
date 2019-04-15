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

import com.gene.bioinfo.ms.gp2s.GP2SApplication;
import com.gene.bioinfo.ms.gp2s.domain.Concentration;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import lombok.NonNull;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Base integration test case class.
 * <p>
 * Sets up the whole spring context for running and provides some utils.
 *
 * @author Cezary Krzyżanowski on 25.07.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GP2SApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration-test")
public abstract class BaseRestITCase<T extends BaseSlugAndLabelEntity> extends BaseITCase<T> {

    protected static final Long LOOKING_ID = Long.MAX_VALUE;
    protected static final Long ENTITY_ID_1 = 1L;
    protected static final Long ENTITY_ID_2 = 2L;
    protected static final String ENTITY_LABEL_1 = "entity 1";
    protected static final String ENTITY_LABEL_2 = "entity 2";

    protected static final String LOOKING_SLUG = "a-slug";
    protected static final String ENTITY_SLUG_1 = "entity-slug-1";
    protected static final String ENTITY_SLUG_2 = "entity-slug-2";
    protected final List<T> entities = new ArrayList<>();
    protected BaseSlugRepository<T> entityRepository;

    protected abstract String createRequestBody(final String label) throws IOException;

    @After
    public void cleanupRepository() {
        this.entityRepository.deleteAll();
    }

    public void prepareEntities() {
        this.entities.clear();
        this.entities.add(this.entityRepository.save(
                this.createEntity(ENTITY_ID_1, ENTITY_LABEL_1, ENTITY_SLUG_1)));
        this.entities.add(this.entityRepository.save(
                this.createEntity(ENTITY_ID_2, ENTITY_LABEL_2, ENTITY_SLUG_2)));
    }

    protected void initBaseRestTests(@NonNull final BaseSlugRepository<T> entitiesRepository) {
        this.entityRepository = entitiesRepository;
        prepareEntities();
    }

    /**
     * Build default concentration for proteins and grids. Proteins are used in all
     * IT cases below Samples IT case
     *
     * @return
     */
    protected Concentration buildConcentration() {
        return Concentration.builder().isDilutedOrConcentrated(false)
                .concentrationType(Concentration.ConcentrationType.Concentration).build();
    }

    @Test
    public void entityByIdNotFound() throws Exception {
        getMockMvc().perform(get(URI + LOOKING_ID).contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void entityBySlugNotFound() throws Exception {
        getMockMvc().perform(get(URI + LOOKING_SLUG).contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleEntityById() throws Exception {
        final T entityToRead = entities.get(0);
        getMockMvc()
                .perform(get(URI + entityToRead.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(entityToRead.getId().intValue())))
                .andExpect(jsonPath("$.slug", is(entityToRead.getSlug())))
                .andExpect(jsonPath("$.label", is(entityToRead.getLabel())));
    }

    @Test
    public void readSingleEntityBySlug() throws Exception {
        final T entityToRead = entities.get(0);
        getMockMvc()
                .perform(get(URI + entityToRead.getSlug()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(entityToRead.getId().intValue())))
                .andExpect(jsonPath("$.slug", is(entityToRead.getSlug())))
                .andExpect(jsonPath("$.label", is(entityToRead.getLabel())));
    }

    @Test
    public void readEntities() throws Exception {
        final T entity1 = entities.get(0);
        final T entity2 = entities.get(1);
        getMockMvc()
                .perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(entity2.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(entity2.getSlug())))
                .andExpect(jsonPath("$[0].label", is(entity2.getLabel())))
                .andExpect(jsonPath("$[1].id", is(entity1.getId().intValue())))
                .andExpect(jsonPath("$[1].slug", is(entity1.getSlug())))
                .andExpect(jsonPath("$[1].label", is(entity1.getLabel())));
    }

    @Test
    public void createEntity() throws Exception {
        final String label = "A new entity";
        final String json = createRequestBody(label);

        getMockMvc().perform(post(getCreateUri()).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.slug", is(notNullValue())))
                .andExpect(jsonPath("$.label", is(label)))
                .andExpect(jsonPath("$.createdDate", is(not(empty()))))
                .andExpect(jsonPath("$.modifiedDate", is(not(empty()))))
                .andExpect(jsonPath("$[?(@.createdDate == @.modifiedDate)]", is(notNullValue())))
                .andExpect(jsonPath("$.createdBy", is(DEFAULT_USER)))
                .andExpect(jsonPath("$.modifiedBy", is(DEFAULT_USER)));
    }

    @Test
    public void createEntityLabelCanBeANumber() throws Exception {
        final String LABEL = "123";
        final String SLUG = "item-123";
        final String json = createRequestBody(LABEL);

        getMockMvc().perform(post(getCreateUri()).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.slug", is(SLUG)))
                .andExpect(jsonPath("$.label", is(LABEL)))
                .andExpect(jsonPath("$.createdDate", is(not(empty()))))
                .andExpect(jsonPath("$.modifiedDate", is(not(empty()))))
                .andExpect(jsonPath("$[?(@.createdDate == @.modifiedDate)]", is(notNullValue())))
                .andExpect(jsonPath("$.createdBy", is(DEFAULT_USER)))
                .andExpect(jsonPath("$.modifiedBy", is(DEFAULT_USER)));
    }

    @Test
    public void deleteEntityById() throws Exception {
        getMockMvc().perform(delete(URI + entities.get(1).getId()).contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEntityBySlug() throws Exception {
        getMockMvc().perform(delete(URI + entities.get(1).getSlug()).contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    public void updateEntity() throws Exception {
        final T entityToModify = entities.get(0);
        final String updatedLabel = "Updated label";
        entityToModify.setLabel(updatedLabel);
        entityToModify.setVersion(entityRepository.findOne(entityToModify.getId()).getVersion());

        getMockMvc().perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json(entityToModify)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label", is(updatedLabel)))
                .andExpect(jsonPath("$.id", is(entityToModify.getId().intValue())))
                // We've modified the label, so the slug has to change as well
                .andExpect(jsonPath("$.slug", is(not(entityToModify.getSlug()))))
                .andExpect(jsonPath("$[?(@.createdDate == @.modifiedDate)]", is(notNullValue())))
                .andExpect(jsonPath("$.modifiedBy", is(DEFAULT_USER)));
    }
}
