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

import com.gene.bioinfo.ms.gp2s.GP2SApplication;
import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.repository.DefaultValueRepository;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.AdminEntities;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.CryoEntities;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.CryoEntitiesPreparer;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.MockMvcExecutor;
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

import static com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.MockMvcExecutor.API_V1_DEFAULT_VALUE;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GP2SApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration-test")
@TestExecutionListeners({SpringBootDependencyInjectionTestExecutionListener.class})
public class ProcessingSessionRestControllerDefaultValuesITCase {

    @Autowired
    private DefaultValueRepository defaultValueRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;
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

    private String prepareUrl(Project project) {
        return API_V1_DEFAULT_VALUE + DefaultValue.DefaultValueType.PROCESSING_SESSION + "/" + project.getSlug();
    }

    @Test
    public void shouldCreateProcessingSessionRecordLastUsedImageProcessingSoftware() throws Exception {
        //given
        final AdminEntities adminEntities = preparer().prepareAdminEntities();
        final Project project = preparer().prepareCryoEntities(adminEntities).getProject();
        final ImageProcessingSoftware ips = adminEntities.getImageProcessingSoftware();
        //then
        this.mockMvc.perform(get(prepareUrl(project)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.imageProcessingSoftware", notNullValue()))
                .andExpect(jsonPath("$.imageProcessingSoftware", is(ips.getId().toString())));
    }

    @Test
    public void shouldUpdateProcessingSessionRecordLastUsedImageProcessingSoftware() throws Exception {
        //given
        final AdminEntities adminEntities = preparer().prepareAdminEntities();
        final Project project = adminEntities.getProject();
        final ImageProcessingSoftware ips = adminEntities.getImageProcessingSoftware();

        final CryoEntities cryoEntities = preparer().prepareCryoEntities(adminEntities);
        final ProcessingSession ps = cryoEntities.getProcessingSession();

        //when
        defaultValueRepository.deleteAll();
        preparer().put(ps, project.getSlug());

        //then
        this.mockMvc.perform(get(prepareUrl(project)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.imageProcessingSoftware", notNullValue()))
                .andExpect(jsonPath("$.imageProcessingSoftware", is(ips.getId().toString())));
    }

    @Test
    public void shouldCreateProcessingSessionOverrideLastUsedImageProcessingSoftware() throws Exception {
        //given
        final CryoEntities cryoEntities = preparer().prepareCryoEntities();
        final Project project = cryoEntities.getProject();
        final MicroscopySession ms = cryoEntities.getMicroscopySession();
        final String sv = "v1.1";
        final ImageProcessingSoftware ips = preparer().post(ImageProcessingSoftware.builder()
                .label("Paint")
                .softwareVersions(Collections.singletonList(sv))
                .build());

        //when
        //create entirely new processing session with new software
        final ProcessingSession ps = preparer().prepareProcessingSession(project, ms, ips, sv);

        //then
        this.mockMvc.perform(get(prepareUrl(project)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.imageProcessingSoftware", notNullValue()))
                .andExpect(jsonPath("$.imageProcessingSoftware", is(ips.getId().toString())));
    }

    @Test
    public void shouldUpdateProcessingSessionOverrideLastUsedImageProcessingSoftware() throws Exception {
        //given
        final ImageProcessingSoftware ips = preparer().prepareImageProcessingSoftware();
        final CryoEntities cryoEntities = preparer().prepareCryoEntities();
        final Project project = cryoEntities.getProject();
        final ProcessingSession ps = cryoEntities.getProcessingSession();

        //when
        ps.getUsedImageProcessingSoftware().get(0).setImageProcessingSoftware(ips);
        ps.getUsedImageProcessingSoftware().get(0).setSoftwareVersion(ips.getSoftwareVersions().iterator().next());
        preparer().put(ps, project.getSlug());

        //then
        this.mockMvc.perform(get(prepareUrl(project)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.imageProcessingSoftware", notNullValue()))
                .andExpect(jsonPath("$.imageProcessingSoftware", is(ips.getId().toString())));
    }

    @Test
    public void shouldCreateProcessingSessionRecordSoftwareVersionForLastUsedImageProcessingSoftware() throws Exception {
        //given
        final CryoEntities cryoEntities = preparer().prepareCryoEntities();
        final Project project = cryoEntities.getProject();
        final ProcessingSession ps = cryoEntities.getProcessingSession();
        final UsedImageProcessingSoftware lastUsed = ps.getUsedImageProcessingSoftware().get(0);

        //then
        this.mockMvc.perform(get(prepareUrl(project)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.softwareVersion." + lastUsed.getImageProcessingSoftware().getId(), notNullValue()))
                .andExpect(jsonPath("$.softwareVersion." + lastUsed.getImageProcessingSoftware().getId(), is(lastUsed.getSoftwareVersion())));
    }

    @Test
    public void shouldUpdateProcessingSessionRecordSoftwareVersionForLastUsedImageProcessingSoftware() throws Exception {
        //given
        final CryoEntities cryoEntities = preparer().prepareCryoEntities();
        final Project project = cryoEntities.getProject();
        final ProcessingSession ps = cryoEntities.getProcessingSession();
        final UsedImageProcessingSoftware lastUsed = ps.getUsedImageProcessingSoftware().get(0);
        //when
        preparer().put(ps, project.getSlug());
        //then
        this.mockMvc.perform(get(prepareUrl(project)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.softwareVersion." + lastUsed.getImageProcessingSoftware().getId(), notNullValue()))
                .andExpect(jsonPath("$.softwareVersion." + lastUsed.getImageProcessingSoftware().getId(), is(lastUsed.getSoftwareVersion())));
    }

    @Test
    public void shouldCreateProcessingSessionOverrideSoftwareVersionForLastUsedImageProcessingSoftware() throws Exception {
        //given
        final CryoEntities cryoEntities = preparer().prepareCryoEntities();
        final Project project = cryoEntities.getProject();
        final MicroscopySession ms = cryoEntities.getMicroscopySession();
        final String sv = "v1.1";
        final ImageProcessingSoftware ips = preparer().post(ImageProcessingSoftware.builder()
                .label("Paint")
                .softwareVersions(Collections.singletonList(sv))
                .build());

        //when
        //create entirely new processing session with new software
        final ProcessingSession ps = preparer().prepareProcessingSession(project, ms, ips, sv);

        //then
        this.mockMvc.perform(get(prepareUrl(project)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.softwareVersion." + ips.getId(), notNullValue()))
                .andExpect(jsonPath("$.softwareVersion." + ips.getId(), is(sv)));
    }

    @Test
    public void shouldUpdateProcessingSessionOverrideSoftwareVersionForLastUsedImageProcessingSoftware() throws Exception {
        //given
        final String sv = "v1.1";
        final ImageProcessingSoftware ips = preparer().post(ImageProcessingSoftware.builder()
                .label("Paint")
                .softwareVersions(Collections.singletonList(sv))
                .build());
        final CryoEntities cryoEntities = preparer().prepareCryoEntities();
        final Project project = cryoEntities.getProject();
        final ProcessingSession ps = cryoEntities.getProcessingSession();
        final UsedImageProcessingSoftware lastUsed = ps.getUsedImageProcessingSoftware().get(0);
        //when
        ps.getUsedImageProcessingSoftware().get(0).setImageProcessingSoftware(ips);
        ps.getUsedImageProcessingSoftware().get(0).setSoftwareVersion(sv);
        preparer().put(ps, project.getSlug());

        //then
        this.mockMvc.perform(get(prepareUrl(project)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MockMvcExecutor.JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.softwareVersion." + ips.getId(), notNullValue()))
                .andExpect(jsonPath("$.softwareVersion." + ips.getId(), is(sv)));
    }

}
