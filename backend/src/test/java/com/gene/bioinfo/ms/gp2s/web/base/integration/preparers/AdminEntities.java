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

package com.gene.bioinfo.ms.gp2s.web.base.integration.preparers;

import com.gene.bioinfo.ms.gp2s.domain.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminEntities {
    private SurfaceTreatmentProtocol surfaceTreatmentProtocol;
    private VitrificationProtocol vitrificationProtocol;
    private NegativeStainProtocol negativeStainProtocol;
    private SurfaceTreatmentMachine surfaceTreatmentMachine;
    private VitrificationMachine vitrificationMachine;
    private CryoStorageDevice cryoStorageDevice;
    private Microscope microscope;
    private ElectronDetector electronDetector;
    private SampleHolder sampleHolder;
    private GridType gridType;
    private BlottingPaper blottingPaper;
    private ImageProcessingSoftware imageProcessingSoftware;
    private Project project;

    @Builder
    public AdminEntities(SurfaceTreatmentProtocol surfaceTreatmentProtocol,
                         VitrificationProtocol vitrificationProtocol,
                         NegativeStainProtocol negativeStainProtocol,
                         SurfaceTreatmentMachine surfaceTreatmentMachine,
                         VitrificationMachine vitrificationMachine,
                         CryoStorageDevice cryoStorageDevice,
                         Microscope microscope,
                         ElectronDetector electronDetector,
                         SampleHolder sampleHolder,
                         GridType gridType,
                         BlottingPaper blottingPaper,
                         ImageProcessingSoftware imageProcessingSoftware,
                         Project project) {
        this.surfaceTreatmentProtocol = surfaceTreatmentProtocol;
        this.vitrificationProtocol = vitrificationProtocol;
        this.negativeStainProtocol = negativeStainProtocol;
        this.surfaceTreatmentMachine = surfaceTreatmentMachine;
        this.vitrificationMachine = vitrificationMachine;
        this.cryoStorageDevice = cryoStorageDevice;
        this.microscope = microscope;
        this.electronDetector = electronDetector;
        this.sampleHolder = sampleHolder;
        this.gridType = gridType;
        this.blottingPaper = blottingPaper;
        this.imageProcessingSoftware = imageProcessingSoftware;
        this.project = project;
    }
}
