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

package com.gene.bioinfo.ms.gp2s.service;

import com.gene.bioinfo.ms.gp2s.repository.AdminEntitiesCounterRepository;
import com.gene.bioinfo.ms.gp2s.web.dto.AdminPanelCounters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AdminEntitiesCounterServiceTest {

    public static final int SURFACE_TREATMENT_PROTOCOL_COUNT = 0;
    public static final int NEGATIVE_STAIN_PROTOCOL_COUNT = 1;
    public static final int VITRIFICATION_PROTOCOL_COUNT = 2;
    public static final int SURFACE_TREATMENT_MACHINES_COUNT = 3;
    public static final int VITRIFICATION_MACHINES_COUNT = 4;
    public static final int CRYO_DEVICE_COUNT = 5;
    public static final int MICROSCOPES_COUNT = 6;
    public static final int ELECTRON_DETECTORS_COUNT = 7;
    public static final int SAMPLE_HOLDERS_COUNT = 8;
    public static final int GRID_TYPES_COUNT = 9;
    public static final int BLOTTING_COUNT = 10;
    public static final int PROJECTS_COUNT = 11;
    public static final int IMAGE_PROCESSING_SOFTWARE_COUNT = 12;

    AdminEntitiesCounterService underTest;

    @Mock
    private AdminEntitiesCounterRepository countersRepo;

    @Before
    public void before() {
        this.underTest = new AdminEntitiesCounterService(countersRepo);
    }

    @Test
    public void shouldReturnAdminCounters() throws IOException {
        //given
        given(countersRepo.surfaceTreatmentProtocolsCount()).willReturn(SURFACE_TREATMENT_PROTOCOL_COUNT);
        given(countersRepo.negativeStainProtocolsCount()).willReturn(NEGATIVE_STAIN_PROTOCOL_COUNT);
        given(countersRepo.vitrificationProtocolsCount()).willReturn(VITRIFICATION_PROTOCOL_COUNT);
        given(countersRepo.surfaceTreatmentMachinesCount()).willReturn(SURFACE_TREATMENT_MACHINES_COUNT);
        given(countersRepo.vitrificationMachinesCount()).willReturn(VITRIFICATION_MACHINES_COUNT);
        given(countersRepo.cryoStorageDevicesCount()).willReturn(CRYO_DEVICE_COUNT);
        given(countersRepo.microscopesCount()).willReturn(MICROSCOPES_COUNT);
        given(countersRepo.electronDetectorsCount()).willReturn(ELECTRON_DETECTORS_COUNT);
        given(countersRepo.sampleHoldersCount()).willReturn(SAMPLE_HOLDERS_COUNT);
        given(countersRepo.gridTypesCount()).willReturn(GRID_TYPES_COUNT);
        given(countersRepo.blottingPapersCount()).willReturn(BLOTTING_COUNT);
        given(countersRepo.projectsCount()).willReturn(PROJECTS_COUNT);
        given(countersRepo.imageProcessingSoftwareCount()).willReturn(IMAGE_PROCESSING_SOFTWARE_COUNT);

        //when
        final AdminPanelCounters adminPanelCounters = this.underTest.calculateAdminCounters();

        //then
        assertEquals(SURFACE_TREATMENT_PROTOCOL_COUNT, adminPanelCounters.getSurfaceTreatmentProtocolsCount()
                .intValue());
        assertEquals(NEGATIVE_STAIN_PROTOCOL_COUNT, adminPanelCounters.getNegativeStainProtocolsCount()
                .intValue());
        assertEquals(VITRIFICATION_PROTOCOL_COUNT, adminPanelCounters.getVitrificationProtocolsCount()
                .intValue());
        assertEquals(SURFACE_TREATMENT_MACHINES_COUNT, adminPanelCounters.getSurfaceTreatmentMachinesCount()
                .intValue());
        assertEquals(VITRIFICATION_MACHINES_COUNT, adminPanelCounters.getVitrificationMachinesCount()
                .intValue());
        assertEquals(CRYO_DEVICE_COUNT, adminPanelCounters.getCryoStorageDevicesCount()
                .intValue());
        assertEquals(MICROSCOPES_COUNT, adminPanelCounters.getMicroscopesCount()
                .intValue());
        assertEquals(ELECTRON_DETECTORS_COUNT, adminPanelCounters.getElectronDetectorsCount()
                .intValue());
        assertEquals(SAMPLE_HOLDERS_COUNT, adminPanelCounters.getSampleHoldersCount()
                .intValue());
        assertEquals(GRID_TYPES_COUNT, adminPanelCounters.getGridTypesCount()
                .intValue());
        assertEquals(BLOTTING_COUNT, adminPanelCounters.getBlottingPapersCount()
                .intValue());
        assertEquals(PROJECTS_COUNT, adminPanelCounters.getProjectsCount()
                .intValue());
        assertEquals(IMAGE_PROCESSING_SOFTWARE_COUNT, adminPanelCounters.getImageProcessingSoftwareCount()
                .intValue());
    }

}
