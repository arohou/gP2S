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

package com.gene.bioinfo.ms.gp2s.web.sample;

import com.gene.bioinfo.ms.gp2s.GP2SApplication;
import com.gene.bioinfo.ms.gp2s.domain.Ligand;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.repository.DefaultValueRepository;
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

import java.util.stream.IntStream;

import static com.gene.bioinfo.ms.gp2s.domain.base.EntityType.entityType;
import static com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.MockMvcExecutor.API_V1_PROJECT;
import static com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.MockMvcExecutor.JSON_CONTENT_TYPE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GP2SApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration-test")
@TestExecutionListeners({SpringBootDependencyInjectionTestExecutionListener.class})
public class AliquotSearchRestITCase {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DefaultValueRepository defaultValueRepository;

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

    private CryoEntitiesPreparer preparer() {
        return this.preparer;
    }

    private MockMvc getMockMvc() {
        return this.mockMvc;
    }

    @Test
    public void noAliquotsFoundForEmptyProject() throws Exception {
        //given
        Project project = preparer().prepareProject();

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("query", "not found"))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(JSON_CONTENT_TYPE))
                    .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void noAliquotsFoundForEmptyProjectBySlug() throws Exception {
        //given
        Project project = preparer().prepareProject();

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getSlug() + "/aliquots")
                .param("query", "not found"))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(JSON_CONTENT_TYPE))
                    .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void notFoundWhenReferringToWrongProject() throws Exception {
        //when
        getMockMvc().perform(get(API_V1_PROJECT + "99999999/aliquots")
                .param("query", "some query"))
                    //then
                    .andExpect(status().isNotFound());
    }

    @Test
    public void bothProteinAndLigandAliquotsFoundSortedByLabel() throws Exception {
        //given
        Project project = preparer().prepareProject();
        Protein protein = preparer().prepareProtein(project);
        Ligand ligand = preparer().prepareLigand(project);

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", ""))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].label", is(ligand.getLabel())))
                    .andExpect(jsonPath("$[1].label", is(protein.getLabel())));
    }

    @Test
    public void resultsAreLimitedTo25() throws Exception {
        //given
        Project project = preparer().prepareProject();
        IntStream.range(0, 30)
                 .forEach(idx -> preparer().prepareProtein(project));

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", ""))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(25)));
    }

    @Test
    public void ligandAliquotsFound() throws Exception {
        //given
        Project project = preparer().prepareProject();
        Ligand ligand = preparer().prepareLigand(project);

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", ""))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(ligand.getId()
                                                            .intValue())))
                    .andExpect(jsonPath("$[0].availableForSampleMaking", is(ligand.getAvailableForSampleMaking())))
                    .andExpect(jsonPath("$[0].label", is(ligand.getLabel())))
                    .andExpect(jsonPath("$[0].reference", is(ligand.getBatchId())))
                    .andExpect(jsonPath("$[0].type", is(entityType(Ligand.class))));
    }

    @Test
    public void proteinAliquotFound() throws Exception {
        //given
        Project project = preparer().prepareProject();
        Protein protein = preparer().prepareProtein(project);
        preparer().prepareLigand(project);

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", protein.getLabel()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(protein.getId()
                                                             .intValue())))
                    .andExpect(jsonPath("$[0].availableForSampleMaking", is(protein.getAvailableForSampleMaking())))
                    .andExpect(jsonPath("$[0].label", is(protein.getLabel())))
                    .andExpect(jsonPath("$[0].reference", is(protein.getPurificationId())))
                    .andExpect(jsonPath("$[0].type", is(entityType(Protein.class))));
    }

    @Test
    public void proteinAliquotFoundByLabelCaseInsensitive() throws Exception {
        //given
        Project project = preparer().prepareProject();
        Protein protein = preparer().prepareProtein(project);
        preparer().prepareLigand(project);

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", protein.getLabel()
                                       .toLowerCase()
                                       .substring(0, 5)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(protein.getId()
                                                             .intValue())));
    }

    @Test
    public void proteinAliquotFoundByReferenceCaseInsensitive() throws Exception {
        //given
        Project project = preparer().prepareProject();
        Protein protein = preparer().prepareProtein(project);
        preparer().prepareLigand(project);

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", protein.getPurificationId()
                                       .toLowerCase()
                                       .substring(0, 5)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(protein.getId()
                                                             .intValue())));
    }

    @Test
    public void proteinAliquotsFoundByLabel() throws Exception {
        //given
        Project project = preparer().prepareProject();
        Protein protein = preparer().prepareProtein(project);

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", protein.getLabel()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(protein.getId()
                                                             .intValue())));
    }

    @Test
    public void ligandAliquotsFoundByLabel() throws Exception {
        //given
        Project project = preparer().prepareProject();
        Ligand ligand = preparer().prepareLigand(project);

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", ligand.getLabel()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(ligand.getId()
                                                            .intValue())));
    }

    @Test
    public void ligandAliquotsFoundByReference() throws Exception {
        //given
        Project project = preparer().prepareProject();
        Ligand ligand = preparer().prepareLigand(project);

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", ligand.getBatchId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(ligand.getId()
                                                            .intValue())));
    }

    @Test
    public void proteinAliquotsFoundByReference() throws Exception {
        //given
        Project project = preparer().prepareProject();
        Protein protein = preparer().prepareProtein(project);

        //when
        getMockMvc().perform(get(API_V1_PROJECT + project.getId() + "/aliquots")
                .param("project", project.getId()
                                         .toString())
                .param("query", protein.getPurificationId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(protein.getId()
                                                             .intValue())));
    }
}
