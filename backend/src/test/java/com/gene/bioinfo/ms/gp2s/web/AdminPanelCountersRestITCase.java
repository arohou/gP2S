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
import com.gene.bioinfo.ms.gp2s.domain.BlottingPaper;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.AdminEntities;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.AdminEntitiesPreparer;
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

import static com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.MockMvcExecutor.API_V1_BLOTTING_PAPER;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GP2SApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration-test")
@TestExecutionListeners({SpringBootDependencyInjectionTestExecutionListener.class})
public class AdminPanelCountersRestITCase {

    static final String ADMIN_COUNTERS_URL = "/api/v1/admin/counters";

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private AdminEntitiesPreparer preparer;

    @Before
    public void initMockMvc() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.preparer = new AdminEntitiesPreparer(this.mockMvc);
    }

    @After
    public void deleteAllEntitiesCreated() {
        preparer().deleteAllCreated();
    }

    @Test
    public void shouldCreateAdminEntity() throws Exception {
        //when
        final BlottingPaper paper = preparer().prepareBlottingPaper();

        //then
        this.mockMvc.perform(get(API_V1_BLOTTING_PAPER + paper.getSlug()))
                    .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteAdminEntity() throws Exception {
        //given
        final BlottingPaper paper = preparer().prepareBlottingPaper();

        //when
        preparer().delete(paper);

        //then
        this.mockMvc.perform(get(API_V1_BLOTTING_PAPER + paper.getSlug()))
                    .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteAllAdminEntitiesCreated() throws Exception {
        //given
        AdminEntities adminEntities = preparer().prepareAdminEntities();

        //when
        preparer().deleteAllCreated();

        //then
        this.mockMvc.perform(get(API_V1_BLOTTING_PAPER + adminEntities.getBlottingPaper()
                                                                      .getSlug()))
                    .andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturnZerosForEmptyDB() throws Exception {
        //given

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.surfaceTreatmentProtocolsCount", is(0)))
                    .andExpect(jsonPath("$.negativeStainProtocolsCount", is(0)))
                    .andExpect(jsonPath("$.vitrificationProtocolsCount", is(0)))
                    .andExpect(jsonPath("$.surfaceTreatmentMachinesCount", is(0)))
                    .andExpect(jsonPath("$.vitrificationMachinesCount", is(0)))
                    .andExpect(jsonPath("$.cryoStorageDevicesCount", is(0)))
                    .andExpect(jsonPath("$.microscopesCount", is(0)))
                    .andExpect(jsonPath("$.electronDetectorsCount", is(0)))
                    .andExpect(jsonPath("$.sampleHoldersCount", is(0)))
                    .andExpect(jsonPath("$.gridTypesCount", is(0)))
                    .andExpect(jsonPath("$.blottingPapersCount", is(0)))
                    .andExpect(jsonPath("$.projectsCount", is(0)));
    }

    @Test
    public void shouldReturnCountersForAdminPanel() throws Exception {
        //given
        preparer().prepareAdminEntities();

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.surfaceTreatmentProtocolsCount", is(1)))
                    .andExpect(jsonPath("$.negativeStainProtocolsCount", is(1)))
                    .andExpect(jsonPath("$.vitrificationProtocolsCount", is(1)))
                    .andExpect(jsonPath("$.surfaceTreatmentMachinesCount", is(1)))
                    .andExpect(jsonPath("$.vitrificationMachinesCount", is(1)))
                    .andExpect(jsonPath("$.cryoStorageDevicesCount", is(1)))
                    .andExpect(jsonPath("$.microscopesCount", is(1)))
                    .andExpect(jsonPath("$.electronDetectorsCount", is(1)))
                    .andExpect(jsonPath("$.sampleHoldersCount", is(1)))
                    .andExpect(jsonPath("$.gridTypesCount", is(1)))
                    .andExpect(jsonPath("$.blottingPapersCount", is(1)))
                    .andExpect(jsonPath("$.projectsCount", is(1)));
    }

    @Test
    public void shouldIncrementSingleCounterAfterAddingNewEquipment() throws Exception {
        //given
        preparer().prepareAdminEntities();
        preparer().prepareBlottingPaper();

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.surfaceTreatmentProtocolsCount", is(1)))
                    .andExpect(jsonPath("$.negativeStainProtocolsCount", is(1)))
                    .andExpect(jsonPath("$.vitrificationProtocolsCount", is(1)))
                    .andExpect(jsonPath("$.surfaceTreatmentMachinesCount", is(1)))
                    .andExpect(jsonPath("$.vitrificationMachinesCount", is(1)))
                    .andExpect(jsonPath("$.cryoStorageDevicesCount", is(1)))
                    .andExpect(jsonPath("$.microscopesCount", is(1)))
                    .andExpect(jsonPath("$.electronDetectorsCount", is(1)))
                    .andExpect(jsonPath("$.sampleHoldersCount", is(1)))
                    .andExpect(jsonPath("$.gridTypesCount", is(1)))
                    .andExpect(jsonPath("$.blottingPapersCount", is(2)))
                    .andExpect(jsonPath("$.projectsCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForSurfaceTreatmentProtocol() throws Exception {
        //given
        preparer().prepareSurfaceTreatmentProtocol(preparer().prepareSurfaceTreatmentMachine());

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.surfaceTreatmentProtocolsCount", is(1)))
                    .andExpect(jsonPath("$.surfaceTreatmentMachinesCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForNegativeStainProtocol() throws Exception {
        //given
        preparer().prepareNegativeStainProtocol();

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.negativeStainProtocolsCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForVitrificationProtocol() throws Exception {
        //given
        preparer().prepareVitrificationProtocol(preparer().prepareBlottingPaper(), preparer().prepareVitrificationMachine());

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.vitrificationProtocolsCount", is(1)))
                    .andExpect(jsonPath("$.vitrificationMachinesCount", is(1)))
                    .andExpect(jsonPath("$.blottingPapersCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForCryoStorageDevice() throws Exception {
        //given
        preparer().prepareCryoStorageDevice();

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cryoStorageDevicesCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForMicroscopes() throws Exception {
        //given
        preparer().prepareMicroscope();

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.microscopesCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForElectronDetectors() throws Exception {
        //given
        preparer().prepareElectronDetector(preparer().prepareMicroscope());

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.microscopesCount", is(1)))
                    .andExpect(jsonPath("$.electronDetectorsCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForSampleHolder() throws Exception {
        //given
        preparer().prepareSampleHolder(preparer().prepareMicroscope());

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.microscopesCount", is(1)))
                    .andExpect(jsonPath("$.sampleHoldersCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForGridType() throws Exception {
        //given
        preparer().prepareGridType();

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.gridTypesCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForBlottingPaper() throws Exception {
        //given
        preparer().prepareBlottingPaper();

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.blottingPapersCount", is(1)));
    }

    @Test
    public void shouldIncrementCounterForProject() throws Exception {
        //given
        preparer().prepareProject();

        //when
        this.mockMvc.perform(get(ADMIN_COUNTERS_URL))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.projectsCount", is(1)));
    }

    private AdminEntitiesPreparer preparer() {
        return this.preparer;
    }
}
