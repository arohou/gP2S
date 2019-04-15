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

package com.gene.bioinfo.ms.gp2s.domain.base;

import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import lombok.Getter;
import lombok.NonNull;

@Getter
public enum EntityType {

    LIGAND(Ligand.class),
    PROTEIN(Protein.class),
    SAMPLE(Sample.class),
    GRID(Grid.class),
    MICROSCOPY_SESSION(MicroscopySession.class),
    PROCESSING_SESSION(ProcessingSession.class),
    MAP(Map.class),
    MODEL(Model.class),
    BLOTTING_PAPER(BlottingPaper.class),
    STORAGE_DEVICE(CryoStorageDevice.class),
    GRID_TYPE(GridType.class),
    MICROSCOPE(Microscope.class),
    NEGATIVE_STAIN_PROTOCOL(NegativeStainProtocol.class),
    PROJECT(Project.class),
    SAMPLE_HOLDER(SampleHolder.class),
    SURFACE_TREATMENT_MACHINE(SurfaceTreatmentMachine.class),
    SURFACE_TREATMENT_PROTOCOL(SurfaceTreatmentMachine.class),
    VITRIFICATION_MACHINE(VitrificationMachine.class),
    VITRIFICATION_PROTOCOL(VitrificationProtocol.class),
    ELECTRON_DETECTOR(ElectronDetector.class),
    COMMENT(Comment.class),
    IMAGE_PROCESSING_SOFTWARE(ImageProcessingSoftware.class);

    private Class clazz;

    EntityType(@NonNull final Class clazz) {
        this.clazz = clazz;
    }

    public static String entityType(@NonNull final Class clazz) {
        for (EntityType type : EntityType.values()) {
            if (type.getClazz() == clazz) {
                return type.name();
            }
        }
        return null;
    }
}
