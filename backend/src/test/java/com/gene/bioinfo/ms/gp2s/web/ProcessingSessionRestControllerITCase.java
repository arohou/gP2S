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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.domain.sample.LigandComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.ProteinComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.repository.*;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants.LONG_STRING_LENGTH;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProcessingSessionRestControllerITCase extends BaseRestITCase<ProcessingSession> {

    private static final String PROTEIN_SLUG_1 = "protein-slug-1";
    private static final String PROTEIN_SLUG_2 = "protein-slug-2";
    private static final String PROTEIN_LABEL_1 = "protein 1";
    private static final String PROTEIN_LABEL_2 = "protein 2";
    private static final String LIGAND_SLUG_1 = "ligand-slug-1";
    private static final String LIGAND_SLUG_2 = "ligand-slug-2";
    private static final String LIGAND_LABEL_1 = "ligand 1";
    private static final String LIGAND_LABEL_2 = "ligand 2";
    private static final String MICROSCOPY_SESSION_LABEL_1 = "Microscopy Session 1";
    private static final String MICROSCOPY_SESSION_LABEL_2 = "Microscopy Session 2";
    private static final String MICROSCOPY_SESSION_SLUG_1 = "microscopy-session-1";
    private static final String MICROSCOPY_SESSION_SLUG_2 = "microscopy-session-2";
    private static final String PROJECT_SLUG_1 = "project-slug-1";
    private static final String PROJECT_SLUG_2 = "project-slug-2";
    private static final String PROJECT_LABEL_1 = "project 1";
    private static final String PROJECT_LABEL_2 = "project 2";
    private static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_1 = "project-management-system-slug-1";
    private static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_2 = "project-management-system-slug-2";
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
    private ProcessingSessionRepository repository;
    private Microscope microscope;
    private GridType gridType;
    private Sample sample;
    private SurfaceTreatmentProtocol surfaceTreatmentProtocol;
    private VitrificationProtocol vitrificationProtocol;
    private Grid grid;
    private ElectronDetector electronDetector;
    private Set<MicroscopySession> microscopySessions;
    private List<Project> projects;

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(repository);
    }

    @Override
    public void prepareEntities() {
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

        final Protein protein1 = proteinRepository.save(Protein.builder().label(PROTEIN_LABEL_1).slug(PROTEIN_SLUG_1)
                .concentration(buildConcentration())
                .projects(Collections.singletonList(p1)).build());
        final Protein protein2 = proteinRepository.save(Protein.builder().label(PROTEIN_LABEL_2).slug(PROTEIN_SLUG_2)
                .concentration(buildConcentration())
                .projects(Collections.singletonList(p2)).build());

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

        super.prepareEntities();
    }

    @Override
    public void cleanupRepository() {
        super.cleanupRepository();

        // Order is important, thus backwards then when creating.
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

    @Override
    public String getUri() {
        return "/api/v1/processing-session/";
    }


    @Override
    public String getCreateUri() {
        return URI + "/" + PROJECT_SLUG_1;
    }

    public ProcessingSession.ProcessingSessionBuilder createProcessingSession(ProcessingSession.ProcessingSessionBuilder builder) {
        return builder
                .numberOfMicrographs(1)
                .numberOfPickedParticles(2)
                .microscopySessions(microscopySessions);
    }

    @Override
    public ProcessingSession createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return createProcessingSession(ProcessingSession.builder().id(id).label(label).slug(slug)
                .usedImageProcessingSoftware(new ArrayList<>())).build();
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(createProcessingSession(ProcessingSession.builder().id(null).slug(null).label(label)
                .usedImageProcessingSoftware(new ArrayList<>())).build());
    }

    @Test
    public void readProcessingSessionProjectsById() throws Exception {
        //check if new sample is associated to two projects
        getMockMvc()
                .perform(get(URI + entities.get(1).getId() + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(projects.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(projects.get(0).getSlug())))
                .andExpect(jsonPath("$[0].label", is(PROJECT_LABEL_1)))
                .andExpect(jsonPath("$[1].id", is(projects.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].slug", is(projects.get(1).getSlug())))
                .andExpect(jsonPath("$[1].label", is(PROJECT_LABEL_2)));
    }

    @Test
    public void readProcessingSessionProjectsBySlug() throws Exception {
        //check if new sample is associated to two projects
        getMockMvc()
                .perform(get(URI + entities.get(1).getSlug() + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(projects.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(projects.get(0).getSlug())))
                .andExpect(jsonPath("$[0].label", is(PROJECT_LABEL_1)))
                .andExpect(jsonPath("$[1].id", is(projects.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].slug", is(projects.get(1).getSlug())))
                .andExpect(jsonPath("$[1].label", is(PROJECT_LABEL_2)));
    }

    @Test
    public void readProcessingSessionProjectionFromProjectById() throws Exception {
        final Long id = projects.get(0).getId();
        final ProcessingSession processingSession = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + id + "/processing-session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(processingSession.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(processingSession.getSlug())))
                .andExpect(jsonPath("$[0].label", is(processingSession.getLabel())));
    }

    @Test
    public void readProcessingSessionProjectionFromProjectBySlug() throws Exception {
        final String slug = projects.get(0).getSlug();
        final ProcessingSession processingSession = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + slug + "/processing-session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(processingSession.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(processingSession.getSlug())))
                .andExpect(jsonPath("$[0].label", is(processingSession.getLabel())));
    }

    @Test
    public void shouldValidationReturnMultipleErrors() throws Exception {
        final ProcessingSession processingSession = ProcessingSession.builder()
                .build();
        final String json = new ObjectMapper().writeValueAsString(processingSession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.label", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.numberOfMicrographs", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.numberOfPickedParticles", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.microscopySessions", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationOnTooSmallNumberOfMicrographs() throws Exception {
        final ProcessingSession processingSession = createProcessingSession(ProcessingSession.builder())
                .numberOfMicrographs(0)
                .build();
        final String json = new ObjectMapper().writeValueAsString(processingSession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.numberOfMicrographs", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationOnTooSmallNumberOfPickedParticles() throws Exception {
        final ProcessingSession processingSession = createProcessingSession(ProcessingSession.builder())
                .numberOfPickedParticles(0)
                .build();
        final String json = new ObjectMapper().writeValueAsString(processingSession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.numberOfPickedParticles", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationOnNoMicroscopySessionsAssociated() throws Exception {
        final ProcessingSession processingSession = createProcessingSession(ProcessingSession.builder())
                .microscopySessions(null)
                .build();
        final String json = new ObjectMapper().writeValueAsString(processingSession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.microscopySessions", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationOnBasePathOfProcessingDirectoryTooLong() throws Exception {
        final ProcessingSession processingSession = createProcessingSession(ProcessingSession.builder())
                .basePathOfProcessingDirectory(StringUtils.repeat("*", LONG_STRING_LENGTH + 1))
                .build();
        final String json = new ObjectMapper().writeValueAsString(processingSession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.basePathOfProcessingDirectory", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationOnNullMicroscopySessions() throws Exception {
        final ProcessingSession processingSession = createProcessingSession(ProcessingSession.builder())
                .microscopySessions(null)
                .build();
        final String json = new ObjectMapper().writeValueAsString(processingSession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.microscopySessions", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationOnEmptyMicroscopySessions() throws Exception {
        final ProcessingSession processingSession = createProcessingSession(ProcessingSession.builder())
                .microscopySessions(new HashSet<>())
                .build();
        final String json = new ObjectMapper().writeValueAsString(processingSession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.microscopySessions", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void countProcessingSessions() throws Exception {
        getMockMvc().perform(get("/api/v1/project/" + PROJECT_SLUG_1 + "/processing-session/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(entities.size())));
    }
}
