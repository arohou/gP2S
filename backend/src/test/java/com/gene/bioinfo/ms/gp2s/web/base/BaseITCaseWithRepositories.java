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

package com.gene.bioinfo.ms.gp2s.web.base;

import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.domain.Map;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.domain.sample.LigandComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.ProteinComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.repository.*;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.web.MicroscopeRestControllerITCase;
import com.gene.bioinfo.ms.gp2s.web.ModelRestControllerITCase;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.*;

@Getter(AccessLevel.PROTECTED)
public abstract class BaseITCaseWithRepositories<T extends BaseSlugAndLabelEntity> extends BaseRestITCase<T> {

    protected static final String PROTEIN_SLUG_1 = "protein-slug-1";
    protected static final String PROTEIN_SLUG_2 = "protein-slug-2";
    protected static final String PROTEIN_LABEL_1 = "protein 1";
    protected static final String PROTEIN_LABEL_2 = "protein 2";
    protected static final String LIGAND_SLUG_1 = "ligand-slug-1";
    protected static final String LIGAND_SLUG_2 = "ligand-slug-2";
    protected static final String LIGAND_LABEL_1 = "ligand 1";
    protected static final String LIGAND_LABEL_2 = "ligand 2";
    protected static final String MICROSCOPY_SESSION_LABEL_1 = "Microscopy Session 1";
    protected static final String MICROSCOPY_SESSION_LABEL_2 = "Microscopy Session 2";
    protected static final String MICROSCOPY_SESSION_SLUG_1 = "microscopy-session-1";
    protected static final String MICROSCOPY_SESSION_SLUG_2 = "microscopy-session-2";
    protected static final String PROJECT_SLUG_1 = "project-slug-1";
    protected static final String PROJECT_SLUG_2 = "project-slug-2";
    protected static final String PROJECT_LABEL_1 = "project 1";
    protected static final String PROJECT_LABEL_2 = "project 2";
    protected static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_1 = "project-management-system-slug 1";
    protected static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_2 = "project-management-system-slug 2";
    protected static final String PROCESSING_SESSION_LABEL = "Processing Session";
    protected static final String PROCESSING_SESSION_SLUG = "processing-session";
    protected static final String MAP_LABEL_1 = "Map 1";
    protected static final String MAP_SLUG_1 = "map-1";
    protected static final String MAP_LABEL_2 = "Map 2";
    protected static final String MAP_SLUG_2 = "map-2";
    protected static final String MODEL_LABEL = "Model";
    protected static final String MODEL_SLUG = "model";

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private DefaultValueRepository defaultValueRepository;
    @Autowired
    private ProteinRepository proteinRepository;
    @Autowired
    private LigandRepository ligandRepository;
    @Autowired
    private MicroscopeRepository microscopeRepository;
    @Autowired
    private GridTypeRepository gridTypeRepository;
    @Autowired
    private GridRepository gridRepository;
    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private SurfaceTreatmentProtocolRepository surfaceTreatmentProtocolRepository;
    @Autowired
    private VitrificationProtocolRepository vitrificationProtocolRepository;
    @Autowired
    private ElectronDetectorRepository electronDetectorRepository;
    @Autowired
    private MicroscopySessionRepository microscopySessionRepository;
    @Autowired
    private ProcessingSessionRepository processingSessionRepository;
    @Autowired
    private MapRepository mapRepository;
    @Autowired
    private ModelRepository modelRepository;
    private Microscope microscope;
    private GridType gridType;
    private Sample sample;
    private SurfaceTreatmentProtocol surfaceTreatmentProtocol;
    private VitrificationProtocol vitrificationProtocol;
    private Grid grid;
    private ElectronDetector electronDetector;
    private Set<MicroscopySession> microscopySessions;
    private List<Project> projects;
    private ProcessingSession processingSession;
    private List<Map> maps;

    @Override
    public void prepareEntities() {
        try {
            final Project p1 = projectRepository
                    .save(Project.builder()
                            .label(PROJECT_LABEL_1)
                            .slug(PROJECT_SLUG_1)
                            .projectManagementSystemSlug(PROJECT_MANAGEMENT_SYSTEM_SLUG_1)
                            .build());
            final Project p2 = projectRepository
                    .save(Project.builder()
                            .label(PROJECT_LABEL_2)
                            .slug(PROJECT_SLUG_2)
                            .projectManagementSystemSlug(PROJECT_MANAGEMENT_SYSTEM_SLUG_2)
                            .build());

            final Protein protein1 = proteinRepository
                    .save(Protein.builder().label(PROTEIN_LABEL_1).slug(PROTEIN_SLUG_1)
                            .concentration(buildConcentration()).projects(Collections.singletonList(p1)).build());
            final Protein protein2 = proteinRepository
                    .save(Protein.builder().label(PROTEIN_LABEL_2).slug(PROTEIN_SLUG_2)
                            .concentration(buildConcentration()).projects(Collections.singletonList(p2)).build());

            final Ligand ligand1 = ligandRepository.save(Ligand.builder().label(LIGAND_LABEL_1).slug(LIGAND_SLUG_1)
                                                               .projects(Collections.singletonList(p1)).build());
            final Ligand ligand2 = ligandRepository.save(Ligand.builder().label(LIGAND_LABEL_2).slug(LIGAND_SLUG_2)
                    .projects(Collections.singletonList(p2)).build());

            projects = projectRepository.save(Arrays.asList(p1, p2));

            final List<ProteinComponent> proteinComponents = new ArrayList<>();
            proteinComponents.add(ProteinComponent.builder()
                    .finalConcentration(1.1f)
                    .aliquot(protein1)
                    .build());
            proteinComponents.add(ProteinComponent.builder()
                    .finalConcentration(2.1f)
                    .aliquot(protein2)
                    .build());

            final List<LigandComponent> ligandComponents = new ArrayList<>();
            ligandComponents.add(LigandComponent.builder()
                    .finalConcentration(1.1f)
                    .aliquot(ligand1)
                    .build());
            ligandComponents.add(LigandComponent.builder()
                    .finalConcentration(2.1f)
                    .aliquot(ligand2)
                    .build());

            sample = Sample.builder()
                    .label("Sample Label")
                    .slug("sample-label")
                    .build();
            sample.setProteinComponent(proteinComponents);
            sample.setLigandComponent(ligandComponents);
            sample = sampleRepository.save(sample);

            grid = gridRepository.save(Grid.builder()
                    .label("Grid Label")
                    .slug("grid-label")
                    .gridType(gridType)
                    .protocolType(Grid.ProtocolType.Cryo)
                    .surfaceTreatmentProtocol(surfaceTreatmentProtocol)
                    .sample(sample)
                    .vitrificationProtocol(vitrificationProtocol)
                    .concentration(buildConcentration())
                    .build());

            microscope = microscopeRepository.save(MicroscopeRestControllerITCase.createMicroscope(
                    Microscope.builder()
                            .label("Some microscope")
                            .slug("some-microscope")));
            gridType = gridTypeRepository.save(GridType.builder()
                    .label("Grid Type Label")
                    .slug("grid-type-label")
                    .build());

            surfaceTreatmentProtocol = surfaceTreatmentProtocolRepository.save(
                    SurfaceTreatmentProtocol.builder()
                            .label("Surface Treatment Protocol Label")
                            .slug("surface-treatment-protocol-label")
                            .build());
            vitrificationProtocol = vitrificationProtocolRepository.save(
                    VitrificationProtocol.builder()
                            .label("Vitrification Protocol Label")
                            .slug("vitrification-protocol-label")
                            .build());

            electronDetector = electronDetectorRepository.save(ElectronDetector.builder()
                    .label("Electron Detector Label")
                    .slug("electron-detector-label")
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
                    .build());


            microscopySessions = new HashSet<>();
            microscopySessions.add(microscopySessionRepository.save(
                    MicroscopySession.builder()
                            .label(MICROSCOPY_SESSION_LABEL_1)
                            .slug(MICROSCOPY_SESSION_SLUG_1)
                            .microscope(microscope)
                            .grid(grid)
                            .gridReturnedToStorage(false)
                            .sessionStart(Date.from(ZonedDateTime.now().minusMinutes(1).toInstant()))
                            .sessionFinish(Date.from(ZonedDateTime.now().toInstant()))
                            .numberOfImagesAcquired(1337)
                            .electronDetector(electronDetector)
                            .nominalMagnification(1)
                            .calibratedMagnification(1.0f)
                            .pixelSize(1.0f)
                            .exposureDuration(1.0f)
                            .pixelBinning(1)
                            .extractionVoltageKV(1.0f)
                            .condenser2ApertureDiameter(1)
                            .build()
            ));

            microscopySessions.add(microscopySessionRepository.save(
                    MicroscopySession.builder()
                            .label(MICROSCOPY_SESSION_LABEL_2)
                            .slug(MICROSCOPY_SESSION_SLUG_2)
                            .microscope(microscope)
                            .grid(grid)
                            .gridReturnedToStorage(false)
                            .sessionStart(Date.from(ZonedDateTime.now().minusMinutes(1).toInstant()))
                            .sessionFinish(Date.from(ZonedDateTime.now().toInstant()))
                            .numberOfImagesAcquired(1337)
                            .electronDetector(electronDetector)
                            .nominalMagnification(1)
                            .calibratedMagnification(1.0f)
                            .pixelSize(1.0f)
                            .exposureDuration(1.0f)
                            .pixelBinning(1)
                            .extractionVoltageKV(1.0f)
                            .condenser2ApertureDiameter(1)
                            .build()
            ));

            processingSession = processingSessionRepository.save(
                    ProcessingSession.builder()
                            .label(PROCESSING_SESSION_LABEL)
                            .slug(PROCESSING_SESSION_SLUG)
                            .numberOfMicrographs(1)
                            .numberOfPickedParticles(1)
                            .basePathOfProcessingDirectory("/hello-world")
                            .microscopySessions(microscopySessions)
                            .build()
            );

            if (!(this.getClass() == ModelRestControllerITCase.class)) {
                return;
            }

            maps = new ArrayList<>();
            maps.add(getMapRepository().save(Map.builder()
                    .slug(MAP_SLUG_1)
                    .label(MAP_LABEL_1)
                    .pixelSize(1.0)
                    .isolevelForSurfaceRendering(1.0)
                    .processingSession(processingSession)
                    .numberOfImages(5)
                    .estimatedResolutionInBestParts(1.0)
                    .estimatedResolutionInWorstParts(1.1)
                    .symmetryApplied("C15").build()
            ));
            maps.add(getMapRepository().save(Map.builder()
                    .slug(MAP_SLUG_2)
                    .label(MAP_LABEL_2)
                    .pixelSize(2.0)
                    .isolevelForSurfaceRendering(2.0)
                    .processingSession(processingSession)
                    .numberOfImages(10)
                    .estimatedResolutionInBestParts(2.0)
                    .estimatedResolutionInWorstParts(2.2)
                    .symmetryApplied("D12").build()
            ));
        } finally {
            super.prepareEntities();
        }
    }

    @Override
    public void cleanupRepository() {
        // Order is important, thus backwards then when creating.
        modelRepository.deleteAll();
        mapRepository.deleteAll();
        processingSessionRepository.deleteAll();

        defaultValueRepository.deleteAll();

        microscopySessionRepository.deleteAll();
        gridRepository.deleteAll();
        sampleRepository.deleteAll();
        proteinRepository.deleteAll();
        ligandRepository.deleteAll();

        electronDetectorRepository.deleteAll();
        gridTypeRepository.deleteAll();
        surfaceTreatmentProtocolRepository.deleteAll();
        vitrificationProtocolRepository.deleteAll();
        microscopeRepository.deleteAll();
        projectRepository.deleteAll();
    }
}
