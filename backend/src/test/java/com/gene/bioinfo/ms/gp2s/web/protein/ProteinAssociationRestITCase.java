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
import com.gene.bioinfo.ms.gp2s.GP2SApplication;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.repository.DefaultValueRepository;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseMockMvcTCase;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.CryoEntities;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.CryoEntitiesPreparer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.MockMvcExecutor.API_V1_PROTEIN;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GP2SApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration-test")
@TestExecutionListeners({SpringBootDependencyInjectionTestExecutionListener.class})
public class ProteinAssociationRestITCase {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DefaultValueRepository defaultValueRepository;
    @Autowired
    private FileRepository fileRepository;
    private MockMvc mockMvc;
    private CryoEntitiesPreparer preparer;

    @Before
    public void initMockMvc() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.preparer = new CryoEntitiesPreparer(this.mockMvc, this.defaultValueRepository);
    }

    @After
    public void deleteAllEntitiesCreated() {
        preparer().deleteAllCreated();
    }

    @Test
    public void shouldCreateCryoEntity() throws Exception {
        //when
        final Project project = preparer().prepareProject();
        final Protein protein = preparer().prepareProtein(project);

        //then
        this.mockMvc.perform(get(API_V1_PROTEIN + protein.getSlug()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteCryoEntity() throws Exception {
        //given
        final Project project = preparer().prepareProject();
        final Protein protein = preparer().prepareProtein(project);

        //when
        preparer().delete(protein);

        //then
        this.mockMvc.perform(get(API_V1_PROTEIN + protein.getSlug()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteAllCryoEntitiesCreated() throws Exception {
        //given
        final CryoEntities cryoEntities = preparer().prepareCryoEntities();

        //when
        preparer().deleteAllCreated();

        //then
        this.mockMvc.perform(get(API_V1_PROTEIN + cryoEntities.getProtein()
                .getSlug()))
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldProteinHaveOnlyOneProject() throws Exception {
        //given
        final Project project = preparer().prepareProject();
        final Protein protein = preparer().prepareProtein(project);

        //when
        this.mockMvc.perform(get(API_V1_PROTEIN + protein.getSlug() + "/projects"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldAllowToRemoveLastProject() throws Exception {
        //given
        final Project project = preparer().prepareProject();
        final Protein protein = preparer().prepareProtein(project);

        //when
        this.mockMvc.perform(delete(API_V1_PROTEIN + protein.getId() + "/projects/" + project.getId()))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldShow404ErrorOnAttemptToRemoveAssociationFromNotExistingProtein() throws Exception {
        //given
        final Integer nonExistingProteinId = Integer.MAX_VALUE;
        final Integer nonExistingProjectId = Integer.MAX_VALUE;

        //when
        this.mockMvc.perform(delete(API_V1_PROTEIN + nonExistingProteinId + "/projects/" + nonExistingProjectId))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldShow404ErrorOnAttemptToCreateAssociationForNotExistingProtein() throws Exception {
        //given
        final Project project = preparer().prepareProject();
        final Integer nonExistingProteinId = Integer.MAX_VALUE;

        final String json = new ObjectMapper().writeValueAsString(Collections
                .singletonMap("projectId", project.getId()));

        //when
        this.mockMvc.perform(patch(API_V1_PROTEIN + nonExistingProteinId + "/projects/")
                .contentType(BaseMockMvcTCase.JSON_CONTENT_TYPE).content(json))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldShowErrorOnAttemptToRemoveNotExistingAssociation() throws Exception {
        //given
        final Project project = preparer().prepareProject();
        final Protein protein = preparer().prepareProtein(project);
        final Integer nonExistingProjectId = Integer.MAX_VALUE;

        //when
        this.mockMvc.perform(delete(API_V1_PROTEIN + protein.getId() + "/projects/" + nonExistingProjectId))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldShow404ErrorOnAttemptToCreateAssociationToNonExistingProject() throws Exception {
        //given
        final Project project = preparer().prepareProject();
        final Protein protein = preparer().prepareProtein(project);
        final Integer nonExistingProjectId = Integer.MAX_VALUE;

        final String json = new ObjectMapper().writeValueAsString(Collections
                .singletonMap("projectId", nonExistingProjectId));

        //when
        this.mockMvc.perform(patch(API_V1_PROTEIN + protein.getId() + "/projects/")
                .contentType(BaseMockMvcTCase.JSON_CONTENT_TYPE).content(json))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateProjectAssociation() throws Exception {
        //given
        final Project project1 = preparer().prepareProject();
        final Project project2 = preparer().prepareProject();
        final Protein protein = preparer().prepareProtein(project1);

        final String json = new ObjectMapper().writeValueAsString(Collections
                .singletonMap("projectId", project2.getId()));

        //when
        this.mockMvc.perform(patch(API_V1_PROTEIN + protein.getId() + "/projects/")
                .contentType(BaseMockMvcTCase.JSON_CONTENT_TYPE).content(json))
                .andDo(print())
                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldDeleteProjectAssociation() throws Exception {
        //given
        final Project project1 = preparer().prepareProject();
        final Project project2 = preparer().prepareProject();
        final Protein protein = preparer().prepareProtein(project1);

        final String json = new ObjectMapper().writeValueAsString(Collections
                .singletonMap("projectId", project2.getId()));

        this.mockMvc.perform(patch(API_V1_PROTEIN + protein.getId() + "/projects/")
                .contentType(BaseMockMvcTCase.JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated());

        //when
        this.mockMvc.perform(delete(API_V1_PROTEIN + protein.getId() + "/projects/" + project1.getId()))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(project2.getId().intValue())));
    }

    private CryoEntitiesPreparer preparer() {
        return this.preparer;
    }
}
