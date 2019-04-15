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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.GP2SApplication;
import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.repository.DefaultValueRepository;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.AdminEntities;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.CryoEntities;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.CryoEntitiesPreparer;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.MockMvcExecutor;
import lombok.NonNull;
import org.junit.After;
import org.junit.Assert;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GP2SApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration-test")
@TestExecutionListeners({SpringBootDependencyInjectionTestExecutionListener.class})
public class ModelLinkRestControllerITCase {


    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DefaultValueRepository defaultValueRepository;
    private MockMvc mockMvc;
    private CryoEntitiesPreparer preparer;
    private List<ModelLink> createdModelLink;

    @Before
    public void initMockMvc() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.preparer = new CryoEntitiesPreparer(this.mockMvc, this.defaultValueRepository);
    }

    @After
    public void tearDown() {
        this.createdModelLink.stream().forEach(this::performDelete);
        preparer().deleteAllCreated();
    }

    private CryoEntitiesPreparer preparer() {
        return this.preparer;
    }

    @Test
    public void shouldCreateLinkToOtherModel() throws Exception {
        //given
        final AdminEntities adminEntities = preparer().prepareAdminEntities();
        final CryoEntities cryoEntities = preparer().prepareCryoEntities(adminEntities);
        final Project project = cryoEntities.getProject();
        final ProcessingSession processingSession = cryoEntities.getProcessingSession();
        final Model parentModel = cryoEntities.getModel();
        final Model childModel = preparer().prepareModel(project,
                preparer().prepareMap(project, processingSession,
                        "some-file.txt"),
                "model-file.txt");
        final ModelLink link = ModelLink.builder()
                .relationshipType(ModelRelationshipType.REFINED)
                .parentModel(parentModel)
                .childModel(childModel)
                .build();

        //when
        final List<ModelLink> result = performPost(parentModel.getId(), link);
        //then
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(ModelRelationshipType.REFINED, result.get(0).getRelationshipType());
        Assert.assertNotNull(result.get(0).getParentModel());
        Assert.assertEquals(parentModel.getId(), result.get(0).getParentModel().getId());
        Assert.assertNotNull(result.get(0).getChildModel());
        Assert.assertEquals(childModel.getId(), result.get(0).getChildModel().getId());
    }

    @Test
    public void shouldDeleteModelLink() throws Exception {

        //given
        final AdminEntities adminEntities = preparer().prepareAdminEntities();
        final CryoEntities cryoEntities = preparer().prepareCryoEntities(adminEntities);
        final Project project = cryoEntities.getProject();
        final ProcessingSession processingSession = cryoEntities.getProcessingSession();
        final Model parentModel = cryoEntities.getModel();
        final Model childModel = preparer().prepareModel(project,
                preparer().prepareMap(project, processingSession,
                        "some-file.txt"),
                "model-file.txt");
        final ModelLink link = ModelLink.builder()
                .relationshipType(ModelRelationshipType.REFINED)
                .parentModel(parentModel)
                .childModel(childModel)
                .build();
        final List<ModelLink> result = performPost(parentModel.getId(), link);

        //when
        this.mockMvc.perform(delete(MockMvcExecutor.API_V1_MODEL_LINK + result.get(0).getId()))
                .andDo(print())
                .andExpect(status().isOk());
        //then
        this.mockMvc.perform(get(MockMvcExecutor.API_V1_MODEL_LINK + parentModel.getId()))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(0)));
        this.createdModelLink.clear();
    }

    @Test
    public void shouldDeleteModelLinksOnUpdate() throws Exception {
        //given
        final AdminEntities adminEntities = preparer().prepareAdminEntities();
        final CryoEntities cryoEntities = preparer().prepareCryoEntities(adminEntities);
        final Project project = cryoEntities.getProject();
        final ProcessingSession processingSession = cryoEntities.getProcessingSession();
        final Model parentModel = cryoEntities.getModel();
        final Model childModel = preparer().prepareModel(project,
                preparer().prepareMap(project, processingSession,
                        "some-file.txt"),
                "model-file.txt");
        final ModelLink link = ModelLink.builder()
                .relationshipType(ModelRelationshipType.REFINED)
                .parentModel(parentModel)
                .childModel(childModel)
                .build();

        final List<ModelLink> orgLinks = performPost(parentModel.getId(), link);

        //when
        final List<ModelLink> result = performPost(parentModel.getId(), link);
        //then
        Assert.assertEquals(1, result.size());
        Assert.assertNotEquals(orgLinks.get(0).getId(), result.get(0).getId());
    }

    private List<ModelLink> performPost(Long parentModelId, ModelLink link) throws Exception {
        final String json = new ObjectMapper().writeValueAsString(Collections.singletonList(link));

        final MvcResult res = this.mockMvc.perform(post(MockMvcExecutor.API_V1_MODEL_LINK + parentModelId)
                .contentType(MockMvcExecutor.JSON_CONTENT_TYPE)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andReturn();

        createdModelLink = new ObjectMapper().readValue(
                res.getResponse().getContentAsByteArray(),
                new TypeReference<List<ModelLink>>() {
                });
        return createdModelLink;
    }

    public void performDelete(@NonNull final ModelLink modelLink) {
        try {
            mockMvc.perform(delete(MockMvcExecutor.API_V1_MODEL_LINK + modelLink.getId())
                    .contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
