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
import lombok.NonNull;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

public class AdminEntitiesPreparer extends DataPreparer {

    public AdminEntitiesPreparer(@NonNull final MockMvc mockMvc) {
        super(mockMvc);
    }

    /**
     * @return all admin entities, exactly one instance of every type.
     */
    public AdminEntities prepareAdminEntities() {
        final AdminEntities.AdminEntitiesBuilder adminEntitiesBuilder = AdminEntities.builder();

        final SurfaceTreatmentMachine surfaceTreatmentMachine = prepareSurfaceTreatmentMachine();
        adminEntitiesBuilder.surfaceTreatmentMachine(surfaceTreatmentMachine);
        adminEntitiesBuilder.surfaceTreatmentProtocol(prepareSurfaceTreatmentProtocol(surfaceTreatmentMachine));

        adminEntitiesBuilder.negativeStainProtocol(prepareNegativeStainProtocol());

        final BlottingPaper blottingPaper = prepareBlottingPaper();
        adminEntitiesBuilder.blottingPaper(blottingPaper);
        final VitrificationMachine vitrificationMachine = prepareVitrificationMachine();
        adminEntitiesBuilder.vitrificationMachine(vitrificationMachine);
        adminEntitiesBuilder.vitrificationProtocol(prepareVitrificationProtocol(blottingPaper, vitrificationMachine));

        adminEntitiesBuilder.cryoStorageDevice(prepareCryoStorageDevice());

        final Microscope microscope = prepareMicroscope();
        adminEntitiesBuilder.microscope(microscope);
        adminEntitiesBuilder.electronDetector(prepareElectronDetector(microscope));
        adminEntitiesBuilder.sampleHolder(prepareSampleHolder(microscope));

        adminEntitiesBuilder.gridType(prepareGridType());
        adminEntitiesBuilder.project(prepareProject());
        adminEntitiesBuilder.imageProcessingSoftware(prepareImageProcessingSoftware());

        return adminEntitiesBuilder.build();
    }

    @NonNull
    public CryoStorageDevice prepareCryoStorageDevice() {
        return post(CryoStorageDevice.builder()
                .label("My machine")
                .manufacturer("Atari")
                .model("ZX Spectrum")
                .location("Living room")
                .hasCylinders(true)
                .hasBoxes(true)
                .hasTubes(true)
                .build());
    }

    @NonNull
    public VitrificationMachine prepareVitrificationMachine() {
        return post(VitrificationMachine.builder()
                .label("label 3")
                .manufacturer("A manufacturer")
                .model("The new model")
                .location("In the basement")
                .build());
    }

    @NonNull
    public VitrificationProtocol prepareVitrificationProtocol(@NonNull final BlottingPaper blottingPaper,
                                                              @NonNull final VitrificationMachine machine) {
        return post(VitrificationProtocol.builder()
                .label("Vitrification Protocol Label")
                .vitrificationMachine(machine)
                .relativeHumidity(1)
                .temperature(36.6f)
                .blottingPaper(blottingPaper)
                .blotForce(1)
                .numberOfBlots(1)
                .blotTime(10.0f)
                .waitTime(1f)
                .drainTime(1f)
                .numberOfSampleApplications(1)
                .description("Vitrification protocol description")
                .build());
    }

    @NonNull
    public BlottingPaper prepareBlottingPaper() {
        return post(BlottingPaper.builder()
                .label("label 3")
                .manufacturer("A manufacturer")
                .model("The new model")
                .build());
    }

    @NonNull
    public NegativeStainProtocol prepareNegativeStainProtocol() {
        return post(NegativeStainProtocol.builder()
                .label("label 3")
                .name("stain name")
                .ph(2.2f)
                .concentration(50f)
                .temperature(-100f)
                .incubationTime(10)
                .description("Lorem Ipsum")
                .build());
    }

    @NonNull
    public Microscope prepareMicroscope() {
        return post(Microscope.builder()
                .label("Some microscope")
                .manufacturer("CRL")
                .model("My model")
                .location("my location")
                .defaultExtractionVoltageKV(1.5f)
                .defaultGunLensSetting(5)
                .defaultVoltageKV(400f)
                .condenser1ApertureDiameter(1)
                .condenser2ApertureDiameter(2)
                .condenser3ApertureDiameter(3)
                .condenser4ApertureDiameter(4)
                .defaultCondenserApertureIndex(2)
                .defaultObjectiveApertureIndex(2)
                .sampleInsertionMechanism(Microscope.InsertionMechanismType.AUTO_LOADER)
                .energyFilter(true)
                .defaultEnergyFilterSlitWidth(5f)
                .defaultSpotSize(5)
                .availableVoltagesKV(singleton(400f))
                .build());
    }

    @NonNull
    public SurfaceTreatmentProtocol prepareSurfaceTreatmentProtocol(final @NonNull SurfaceTreatmentMachine machine) {
        return post(SurfaceTreatmentProtocol.builder()
                .label("Surface Treatment Protocol Label")
                .machine(machine)
                .duration(1.0f)
                .pressure(1.0f)
                .current(1.0f)
                .build());
    }

    @NonNull
    public SurfaceTreatmentMachine prepareSurfaceTreatmentMachine() {
        return post(SurfaceTreatmentMachine.builder()
                .label("label 3")
                .manufacturer("manufacturer")
                .model("model")
                .location("location")
                .build());
    }


    @NonNull
    public ElectronDetector prepareElectronDetector(@NonNull final Microscope microscope) {
        return post(ElectronDetector.builder()
                .label("Fast and cheap electron detector")
                .manufacturer("A manufacturer")
                .model("The new model")
                .microscope(microscope)
                .countsPerElectronsFactor(13.37f)
                .countingModeAvailable(true)
                .pixelLinearDimensionUm(14.10f)
                .numberOfPixelColumns(13)
                .numberOfPixelRows(7)
                .doseFractionationAvailable(true)
                .superResolutionAvailable(true)
                .availableMagnifications(new ArrayList<>())
                .build());
    }

    @NonNull
    public GridType prepareGridType() {
        return post(GridType.builder()
                .label("Specific Grid Type")
                .manufacturer("Simens")
                .description("Lorem Ipsum")
                .build());
    }

    public SampleHolder prepareSampleHolder(Microscope... microscope) {
        return post(SampleHolder.builder()
                .label("A label")
                .manufacturer("A manufacturer")
                .model("A model")
                .location("New York")
                .cryoCapable(true)
                .maximumTilt(5f)
                .doubleTilt(false)
                .microscopes(asList(microscope))
                .build());
    }

    @NonNull
    public Project prepareProject() {
        return post(Project.builder()
                .label("Specific project label")
                .projectManagementSystemSlug("XYZ0000001")
                .build());
    }

    public ImageProcessingSoftware prepareImageProcessingSoftware() {
        return post(ImageProcessingSoftware.builder()
                .label("XYZ")
                .softwareVersions(Collections.singletonList("0.1"))
                .build());
    }
}
