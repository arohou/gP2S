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
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CryoEntities {
    private Project project;
    private Protein protein;
    private Ligand ligand;
    private Sample sample;
    private Grid grid;
    private MicroscopySession microscopySession;
    private ProcessingSession processingSession;
    private Map map;
    private Model model;


    @Builder
    public CryoEntities(Project project,
                        Protein protein,
                        Ligand ligand,
                        Sample sample,
                        Grid grid,
                        MicroscopySession microscopySession,
                        ProcessingSession processingSession,
                        Map map,
                        Model model
    ) {
        this.project = project;
        this.protein = protein;
        this.ligand = ligand;
        this.sample = sample;
        this.grid = grid;
        this.microscopySession = microscopySession;
        this.processingSession = processingSession;
        this.map = map;
        this.model = model;
    }

}
