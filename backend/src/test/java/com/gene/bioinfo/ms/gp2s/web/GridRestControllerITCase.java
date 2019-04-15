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
import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.repository.GridRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestExecutionListeners({SpringBootDependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup(value = "GridRestControllerITCaseData.xml")
@DatabaseTearDown(value = "ClearDB.xml")
public class GridRestControllerITCase extends BaseRestITCase<Grid> {

    private static final String PROJECT_SLUG_1 = "project-slug-1";
    private static final String PROJECT_LABEL_1 = "project 1";
    private static final String PROJECT_LABEL_2 = "project 2";
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private GridRepository gridRepository;
    private List<Project> projects;

    @Before
    public void setUp() throws Exception {
        this.initBaseRestTests(gridRepository);
    }

    @Test
    public void readGridProjectsById() throws Exception {
        //check if new sample is associated to two projects
        getMockMvc()
                .perform(get(URI + entities.get(1).getId() + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(projects.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(projects.get(0).getSlug())))
                .andExpect(jsonPath("$[0].label", is(PROJECT_LABEL_1)))
                .andExpect(jsonPath("$[1].id", is(projects.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].slug", is(projects.get(1).getSlug())))
                .andExpect(jsonPath("$[1].label", is(PROJECT_LABEL_2)));
    }

    @Test
    public void readGridProjectsBySlug() throws Exception {
        //check if new sample is associated to two projects
        getMockMvc()
                .perform(get(URI + entities.get(1).getSlug() + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(projects.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(projects.get(0).getSlug())))
                .andExpect(jsonPath("$[0].label", is(PROJECT_LABEL_1)))
                .andExpect(jsonPath("$[1].id", is(projects.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].slug", is(projects.get(1).getSlug())))
                .andExpect(jsonPath("$[1].label", is(PROJECT_LABEL_2)));
    }

    @Test
    public void readGridProjectionFromProjectById() throws Exception {
        final Long id = projects.get(0).getId();
        final Grid grid = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + id + "/grid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(grid.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(grid.getSlug())))
                .andExpect(jsonPath("$[0].label", is(grid.getLabel())));
    }

    @Test
    public void readGridProjectionFromProjectBySlug() throws Exception {
        final String slug = projects.get(0).getSlug();
        final Grid grid = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + slug + "/grid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(grid.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(grid.getSlug())))
                .andExpect(jsonPath("$[0].label", is(grid.getLabel())));
    }

    @Test
    public void shouldValidationReturnMultipleErrors() throws Exception {
        final Grid grid = Grid.builder().incubationTime(-2f).build();
        final String json = new ObjectMapper().writeValueAsString(grid);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.label", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.incubationTime", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.sample", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.gridType", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.surfaceTreatmentProtocol", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.vitrificationProtocol", is(not(isEmptyOrNullString()))))
        ;

    }

    @Test
    public void countGrids() throws Exception {
        getMockMvc().perform(get("/api/v1/project/" + PROJECT_SLUG_1 + "/grid/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(entities.size())));
    }

    @Override
    public String getUri() {
        return "/api/v1/grid/";
    }

    @Override
    public Grid createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        // db unit will create below dependent entities
        return Grid.builder().id(id).label(label).slug(slug)
                .gridType(GridType.builder().id(1L).build())
                .protocolType(Grid.ProtocolType.Cryo)
                .surfaceTreatmentProtocol(SurfaceTreatmentProtocol.builder().id(1L).build())
                .sample(Sample.builder().id(1L).build())
                .vitrificationProtocol(VitrificationProtocol.builder().id(1L).build())
                .cryoStorageDevice(CryoStorageDevice.builder().id(1L).build())
                .concentration(buildConcentration())
                .build();
    }

    @Override
    public void cleanupRepository() {

    }

    @Override
    public void prepareEntities() {
        entities.clear();
        entities.addAll(gridRepository.findAll());
        projects = projectRepository.findAll();
        entities.forEach(e -> e.setSample(Sample.builder().id(e.getSample().getId()).build())); //to prevent issue with lazy loading
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        Grid grid = createEntity(null, label, null);
        return json(grid);
    }

    @Override
    public String getCreateUri() {
        return URI + "/" + PROJECT_SLUG_1;
    }
}
