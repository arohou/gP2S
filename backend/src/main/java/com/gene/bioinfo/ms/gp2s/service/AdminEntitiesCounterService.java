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
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AdminEntitiesCounterService {

    private AdminEntitiesCounterRepository repo;

    protected AdminEntitiesCounterService(@NonNull final AdminEntitiesCounterRepository repo) {
        this.repo = repo;
    }

    public AdminPanelCounters calculateAdminCounters() {
        return AdminPanelCounters.builder()
                .surfaceTreatmentProtocolsCount(this.repo.surfaceTreatmentProtocolsCount())
                .negativeStainProtocolsCount(this.repo.negativeStainProtocolsCount())
                .vitrificationProtocolsCount(this.repo.vitrificationProtocolsCount())
                .surfaceTreatmentMachinesCount(this.repo.surfaceTreatmentMachinesCount())
                .vitrificationMachinesCount(this.repo.vitrificationMachinesCount())
                .cryoStorageDevicesCount(this.repo.cryoStorageDevicesCount())
                .microscopesCount(this.repo.microscopesCount())
                .electronDetectorsCount(this.repo.electronDetectorsCount())
                .sampleHoldersCount(this.repo.sampleHoldersCount())
                .gridTypesCount(this.repo.gridTypesCount())
                .blottingPapersCount(this.repo.blottingPapersCount())
                .imageProcessingSoftwareCount(this.repo.imageProcessingSoftwareCount())
                .projectsCount(this.repo.projectsCount())
                .build();
    }
}
