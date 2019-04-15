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

package com.gene.bioinfo.ms.gp2s.repository;

import com.gene.bioinfo.ms.gp2s.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminEntitiesCounterRepository extends JpaRepository<Project, Long> {
    @Query("select count(e.id) from SurfaceTreatmentProtocol e")
    Integer surfaceTreatmentProtocolsCount();

    @Query("select count(e.id) from NegativeStainProtocol e")
    Integer negativeStainProtocolsCount();

    @Query("select count(e.id) from VitrificationProtocol e")
    Integer vitrificationProtocolsCount();

    @Query("select count(e.id) from SurfaceTreatmentMachine e")
    Integer surfaceTreatmentMachinesCount();

    @Query("select count(e.id) from VitrificationMachine e")
    Integer vitrificationMachinesCount();

    @Query("select count(e.id) from CryoStorageDevice e")
    Integer cryoStorageDevicesCount();

    @Query("select count(e.id) from Microscope e")
    Integer microscopesCount();

    @Query("select count(e.id) from ElectronDetector e")
    Integer electronDetectorsCount();

    @Query("select count(e.id) from SampleHolder e")
    Integer sampleHoldersCount();

    @Query("select count(e.id) from GridType e")
    Integer gridTypesCount();

    @Query("select count(e.id) from BlottingPaper e")
    Integer blottingPapersCount();

    @Query("select count(e.id) from ImageProcessingSoftware e")
    Integer imageProcessingSoftwareCount();

    @Query("select count(e.id) from Project e")
    Integer projectsCount();
}
