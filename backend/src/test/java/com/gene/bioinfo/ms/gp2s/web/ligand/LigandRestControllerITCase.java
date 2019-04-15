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

package com.gene.bioinfo.ms.gp2s.web.ligand;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.Ligand;
import com.gene.bioinfo.ms.gp2s.repository.LigandRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestITCase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LigandRestControllerITCase extends BaseProjectRestITCase<Ligand> {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LigandRepository ligandRepository;

    @Before
    public void setUp() throws Exception {
        this.initProjectBaseRestTests(ligandRepository, projectRepository);
    }

    private Ligand.LigandBuilder createLigandBuilder() {
        return Ligand.builder()
                     .label("A new label")
                     .conceptId("1")
                     .concentration(1.0f)
                     .availableForSampleMaking(true)
                     .projects(projects);
    }

    @Test
    public void createLigandById() throws Exception {
        final String json = json(createLigandBuilder().build());

        getMockMvc().perform(post(URI + projects.get(1).getId()).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdDate", is(Matchers.not(empty()))))
                .andExpect(jsonPath("$.modifiedDate", is(Matchers.not(empty()))))
                .andExpect(jsonPath("$[?(@.createdDate == @.modifiedDate)]", is(notNullValue())))
                .andExpect(jsonPath("$.createdBy", is(DEFAULT_USER)))
                .andExpect(jsonPath("$.modifiedBy", is(DEFAULT_USER)));
    }

    @Test
    public void createLigandBySlug() throws Exception {
        final String json = json(createLigandBuilder().build());

        getMockMvc().perform(post(URI + projects.get(1).getSlug()).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdDate", is(Matchers.not(empty()))))
                .andExpect(jsonPath("$.modifiedDate", is(Matchers.not(empty()))))
                .andExpect(jsonPath("$[?(@.createdDate == @.modifiedDate)]", is(notNullValue())))
                .andExpect(jsonPath("$.createdBy", is(DEFAULT_USER)))
                .andExpect(jsonPath("$.modifiedBy", is(DEFAULT_USER)));
    }

    @Test
    public void shouldValidationReturnMultipleErrors() throws Exception {
        final Ligand ligand = Ligand.builder().build();
        final String longString = "01234567890123456789012345678901234567890123456789";
        ligand.setSolvent(longString);
        ligand.setTubeLabel(longString);
        final String json = new ObjectMapper().writeValueAsString(ligand);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errors.label", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.tubeLabel", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.concentration", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.solvent", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void countLigands() throws Exception {
        getMockMvc().perform(get("/api/v1/project/" + PROJECT_SLUG_1 + "/ligand/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(entities.size())));
    }

    @Test
    public void findRecentAvailableLigand() throws Exception {
        getMockMvc().perform(get(URI + "findRecentAvailableLigand/" + entities.get(0).getConceptId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conceptId", is(entities.get(0).getConceptId())));
    }

    @Override
    public String getCreateUri() {
        return URI + "/" + PROJECT_SLUG_1;
    }

    @Override
    public String getUri() {
        return "/api/v1/ligand/";
    }

    @Override
    public String getProjectionFromProjectPath() {
        return "ligand";
    }

    @Override
    public Ligand createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return createLigandBuilder().id(id).label(label).slug(slug).build();
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(createEntity(null, label, null));
    }
}
