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

import com.gene.bioinfo.ms.gp2s.domain.ElectronDetector;
import com.gene.bioinfo.ms.gp2s.domain.Microscope;
import com.gene.bioinfo.ms.gp2s.repository.ElectronDetectorRepository;
import com.gene.bioinfo.ms.gp2s.repository.MicroscopeRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import lombok.NonNull;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ElectronDetectorRestControllerITCase extends BaseRestITCase<ElectronDetector> {

    @Autowired
    private ElectronDetectorRepository repository;

    @Autowired
    private MicroscopeRepository microscopeRepository;

    private Microscope microscope;

    @NonNull
    private Microscope buildMicroscope() {
        Set<Float> availableVoltagesKV = new HashSet<>();
        availableVoltagesKV.add(400f);
        return Microscope.builder()
                .label("Test microscope")
                .slug("test-microscope")
                .manufacturer("CRL").model("My model").location("my location").defaultExtractionVoltageKV(1.5f)
                .defaultGunLensSetting(5).defaultVoltageKV(400f).condenser1ApertureDiameter(1)
                .condenser2ApertureDiameter(2).condenser3ApertureDiameter(3).condenser4ApertureDiameter(4)
                .defaultCondenserApertureIndex(2).objectiveAperture1(Microscope.ObjectiveAperture.builder().phasePlate(true).diameter(3).build())
                .objectiveAperture2(Microscope.ObjectiveAperture.builder().phasePlate(true).diameter(5).build())
                .objectiveAperture3(Microscope.ObjectiveAperture.builder().phasePlate(true).diameter(8).build())
                .objectiveAperture4(Microscope.ObjectiveAperture.builder().phasePlate(true).diameter(12).build())
                .defaultObjectiveApertureIndex(2).sampleInsertionMechanism(Microscope.InsertionMechanismType.AUTO_LOADER)
                .energyFilter(true).defaultEnergyFilterSlitWidth(5f).defaultSpotSize(5)
                .availableVoltagesKV(availableVoltagesKV)
                .build();
    }

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(repository);
    }

    @Override
    public void prepareEntities() {
        microscope = microscopeRepository.saveAndFlush(buildMicroscope());
        super.prepareEntities();
    }

    @Override
    public void cleanupRepository() {
        super.cleanupRepository();
        microscopeRepository.deleteAll();
    }

    @Override
    public String getUri() {
        return "/api/v1/electron-detector/";
    }

    @NonNull
    private ElectronDetector.ElectronDetectorBuilder commonCreateDetector() {
        return ElectronDetector.builder()
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
                .availableMagnifications(new ArrayList<>());
    }

    @Override
    public ElectronDetector createEntity(@Nullable final Long id,
                                         @Nullable final String label,
                                         @Nullable final String slug) {
        return commonCreateDetector()
                .id(id).label(label).slug(slug)
                .build();
    }

    @Override
    protected String createRequestBody(@Nullable final String label) throws IOException {
        return json(commonCreateDetector()
                .label(label)
                .build());
    }
}
