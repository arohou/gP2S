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

package com.gene.bioinfo.ms.gp2s.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminPanelCounters {
    private Integer surfaceTreatmentProtocolsCount;
    private Integer negativeStainProtocolsCount;
    private Integer vitrificationProtocolsCount;
    private Integer surfaceTreatmentMachinesCount;
    private Integer vitrificationMachinesCount;
    private Integer cryoStorageDevicesCount;
    private Integer microscopesCount;
    private Integer electronDetectorsCount;
    private Integer sampleHoldersCount;
    private Integer gridTypesCount;
    private Integer blottingPapersCount;
    private Integer imageProcessingSoftwareCount;
    private Integer projectsCount;

    @Builder
    public AdminPanelCounters(
            Integer surfaceTreatmentProtocolsCount,
            Integer negativeStainProtocolsCount,
            Integer vitrificationProtocolsCount,
            Integer surfaceTreatmentMachinesCount,
            Integer vitrificationMachinesCount,
            Integer cryoStorageDevicesCount,
            Integer microscopesCount,
            Integer electronDetectorsCount,
            Integer sampleHoldersCount,
            Integer gridTypesCount,
            Integer blottingPapersCount,
            Integer imageProcessingSoftwareCount,
            Integer projectsCount) {
        this.surfaceTreatmentProtocolsCount = surfaceTreatmentProtocolsCount;
        this.negativeStainProtocolsCount = negativeStainProtocolsCount;
        this.vitrificationProtocolsCount = vitrificationProtocolsCount;
        this.surfaceTreatmentMachinesCount = surfaceTreatmentMachinesCount;
        this.vitrificationMachinesCount = vitrificationMachinesCount;
        this.cryoStorageDevicesCount = cryoStorageDevicesCount;
        this.microscopesCount = microscopesCount;
        this.electronDetectorsCount = electronDetectorsCount;
        this.sampleHoldersCount = sampleHoldersCount;
        this.gridTypesCount = gridTypesCount;
        this.blottingPapersCount = blottingPapersCount;
        this.imageProcessingSoftwareCount = imageProcessingSoftwareCount;
        this.projectsCount = projectsCount;
    }
}
