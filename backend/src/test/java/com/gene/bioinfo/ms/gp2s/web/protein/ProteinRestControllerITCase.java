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

package com.gene.bioinfo.ms.gp2s.web.protein;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProteinRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestITCase;
import lombok.NonNull;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProteinRestControllerITCase extends BaseProjectRestITCase<Protein> {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProteinRepository proteinRepository;

    @Before
    public void setUp() throws Exception {
        this.initProjectBaseRestTests(proteinRepository, projectRepository);
    }

    private Protein.ProteinBuilder createProteinBuilder() {
        return Protein.builder()
                      .label("A new label")
                      .purificationId("P12345678")
                      .concentration(buildConcentration())
                      .availableForSampleMaking(true)
                      .projects(projects);
    }

    @Test
    public void createProteinById() throws Exception {
        final String json = json(createProteinBuilder().build());

        getMockMvc().perform(post(URI + projects.get(1).getId()).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdDate", is(Matchers.not(empty()))))
                .andExpect(jsonPath("$.modifiedDate", is(Matchers.not(empty()))))
                .andExpect(jsonPath("$[?(@.createdDate == @.modifiedDate)]", is(notNullValue())))
                .andExpect(jsonPath("$.createdBy", is(DEFAULT_USER)))
                .andExpect(jsonPath("$.modifiedBy", is(DEFAULT_USER)));
    }

    @Test
    public void createProteinBySlug() throws Exception {
        final String json = json(createProteinBuilder().build());

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
        final Protein protein = Protein.builder().build();
        final String longString = "01234567890123456789012345678901234567890123456789";
        protein.setTubeLabel(longString);
        final String json = new ObjectMapper().writeValueAsString(protein);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errors.label", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.tubeLabel", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void countProteins() throws Exception {
        getMockMvc().perform(get("/api/v1/project/" + PROJECT_SLUG_1 + "/protein/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(entities.size())));
    }

    @Test
    public void findRecentAvailableProtein() throws Exception {
        getMockMvc().perform(get(URI + "findRecentAvailableProtein/" + entities.get(0).getPurificationId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purificationId", is(entities.get(0).getPurificationId())));
    }

    @Override
    public String getCreateUri() {
        return URI + "/" + PROJECT_SLUG_1;
    }

    @Override
    public String getUri() {
        return "/api/v1/protein/";
    }

    @Override
    public String getProjectionFromProjectPath() {
        return "protein";
    }

    @Override
    public Protein createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return createProteinBuilder().id(id).label(label).slug(slug).build();
    }

    @Override
    protected String createRequestBody(@NonNull String label) throws IOException {
        return json(createEntity(null, label, null));
    }
}
