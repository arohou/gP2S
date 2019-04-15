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
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MicroscopySessionRestControllerITCase extends BaseRestITCase<MicroscopySession> {

    private static final String PROJECT_SLUG_1 = "project-slug-1";
    private static final String PROJECT_SLUG_2 = "project-slug-2";
    private static final String PROJECT_LABEL_1 = "project 1";
    private static final String PROJECT_LABEL_2 = "project 2";
    private static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_1 = "project-management-system-slug 1";
    private static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_2 = "project-management-system-slug 2";
    private static final String PROTEIN_SLUG_1 = "protein-slug-1";
    private static final String PROTEIN_SLUG_2 = "protein-slug-2";
    private static final String PROTEIN_LABEL_1 = "protein 1";
    private static final String PROTEIN_LABEL_2 = "protein 2";
    private static final String LIGAND_SLUG_1 = "ligand-slug-1";
    private static final String LIGAND_SLUG_2 = "ligand-slug-2";
    private static final String LIGAND_LABEL_1 = "ligand 1";
    private static final String LIGAND_LABEL_2 = "ligand 2";
    private static final String MICROSCOPE_SLUG = "microscope-slug";

    @Autowired
    private DefaultValueRepository defaultValueRepository;
    @Autowired
    private ProjectRepository projectRepository;
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
    private MicroscopySessionRepository repository;
    private Microscope microscope;
    private GridType gridType;
    private Sample sample;
    private SurfaceTreatmentProtocol surfaceTreatmentProtocol;
    private VitrificationProtocol vitrificationProtocol;
    private Grid grid;
    private ElectronDetector electronDetector;
    private List<Project> projects;

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
                .concentration(buildConcentration()).build());

        microscope = microscopeRepository.save(MicroscopeRestControllerITCase.createMicroscope(
                Microscope.builder()
                        .label("Some microscope")
                        .slug(MICROSCOPE_SLUG)));
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

        super.prepareEntities();
    }

    @Override
    public void cleanupRepository() {
        super.cleanupRepository();
        // Order is important, thus backwards then when creating.
        defaultValueRepository.deleteAll();

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

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(repository);
    }

    @Override
    public String getUri() {
        return "/api/v1/microscopy-session/";
    }

    @Override
    public String getCreateUri() {
        return URI + "/" + PROJECT_SLUG_1;
    }

    public MicroscopySession.MicroscopySessionBuilder createMicroscopySession(MicroscopySession.MicroscopySessionBuilder builder) {
        return builder
                .microscope(microscope)
                .grid(grid)
                .gridReturnedToStorage(false)
                .sessionStart(Date.from(ZonedDateTime.now().minusMinutes(1).toInstant()))
                .sessionFinish(null)
                .numberOfImagesAcquired(1337)
                .electronDetector(electronDetector)
                .nominalMagnification(1)
                .calibratedMagnification(1.0f)
                .pixelSize(1.0f)
                .exposureDuration(1.0f)
                .pixelBinning(1)
                .extractionVoltageKV(1.0f);
    }

    @Override
    public MicroscopySession createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return createMicroscopySession(MicroscopySession.builder().id(id).label(label).slug(slug)).build();
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(createMicroscopySession(MicroscopySession.builder().id(null).slug(null).label(label)).build());
    }

    @Test
    public void readMicroscopySessionProjectsById() throws Exception {
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
    public void readMicroscopySessionProjectsBySlug() throws Exception {
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
    public void readMicroscopySessionProjectionFromProjectById() throws Exception {
        final Long id = projects.get(0).getId();
        final MicroscopySession microscopySession = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + id + "/microscopy-session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(microscopySession.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(microscopySession.getSlug())))
                .andExpect(jsonPath("$[0].label", is(microscopySession.getLabel())));
    }

    @Test
    public void readMicroscopySessionProjectionFromProjectBySlug() throws Exception {
        final String slug = projects.get(0).getSlug();
        final MicroscopySession microscopySession = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + slug + "/microscopy-session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(microscopySession.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(microscopySession.getSlug())))
                .andExpect(jsonPath("$[0].label", is(microscopySession.getLabel())));
    }

    @Test
    public void shouldValidationReturnMultipleErrors() throws Exception {
        final MicroscopySession microscopySession = MicroscopySession.builder()
                .numberOfImagesAcquired(-1)
                .build();
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.label", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.microscope", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.grid", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.sessionStart", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.numberOfImagesAcquired", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.electronDetector", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.nominalMagnification", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.exposureDuration", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.pixelBinning", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.extractionVoltageKV", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationForFinishDate() throws Exception {
        final Date now = new Date();
        final MicroscopySession microscopySession = MicroscopySession.builder()
                .numberOfImagesAcquired(-1)
                .sessionStart(now)
                .sessionFinish(Date
                        .from(now.toInstant().minusSeconds(100)))
                .build();
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.sessionFinish", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationForExposureRate() throws Exception {
        final MicroscopySession microscopySession =
                createMicroscopySession(MicroscopySession.builder()
                        .countingMode(true).exposureRate(null)).build();
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.exposureRate", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationForSupersamplingFactor() throws Exception {
        final MicroscopySession microscopySession =
                createMicroscopySession(MicroscopySession.builder().superResolution(true)
                        .supersamplingFactor(null)).build();
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.supersamplingFactor", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationForPixelBinning() throws Exception {
        final MicroscopySession microscopySession =
                createMicroscopySession(MicroscopySession.builder().superResolution(false)).build();
        microscopySession.setPixelBinning(null);
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.pixelBinning", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailValidationForMicroscopeSettings() throws Exception {
        final MicroscopySession microscopySession = MicroscopySession.builder()
                .extractionVoltageKV(0.0f)
                .gunLensSetting(0)
                .condenser2ApertureDiameter(0)
                .energyFilter(true)
                .energyFilterSlitWidth(0.0f)
                .build();
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errors.extractionVoltageKV", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.gunLensSetting", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.condenser2ApertureDiameter", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.energyFilterSlitWidth", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldPassNullApertureDiametersCreation() throws Exception {
        final MicroscopySession microscopySession = createMicroscopySession(MicroscopySession.builder())
                .label("test")
                .condenser2ApertureDiameter(null)
                .objectiveAperture(null)
                .build();
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(JSON_CONTENT_TYPE));
    }

    @Test
    public void shouldFailOnObjectiveApertureWithPhasePlateAndDiameter() throws Exception {
        final Microscope.ObjectiveAperture objectiveAperture = new Microscope.ObjectiveAperture();
        objectiveAperture.setPhasePlate(true);
        objectiveAperture.setDiameter(13);
        final MicroscopySession microscopySession = createMicroscopySession(MicroscopySession.builder())
                .label("test")
                .objectiveAperture(objectiveAperture)
                .build();
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errors.objectiveAperture", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFailOnObjectiveApertureWithTooSmalDiameter() throws Exception {
        final Microscope.ObjectiveAperture objectiveAperture = new Microscope.ObjectiveAperture();
        objectiveAperture.setPhasePlate(true);
        objectiveAperture.setDiameter(0);
        final MicroscopySession microscopySession = createMicroscopySession(MicroscopySession.builder())
                .label("test")
                .objectiveAperture(objectiveAperture)
                .build();
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errors.objectiveAperture", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldCreateWithObjectiveApertureDiameterSet() throws Exception {
        final Microscope.ObjectiveAperture objectiveAperture = new Microscope.ObjectiveAperture();
        objectiveAperture.setPhasePlate(true);
        objectiveAperture.setDiameter(1);
        final MicroscopySession microscopySession = createMicroscopySession(MicroscopySession.builder())
                .label("test")
                .objectiveAperture(objectiveAperture)
                .build();
        final String json = new ObjectMapper().writeValueAsString(microscopySession);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errors.objectiveAperture", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void countMicroscopySessions() throws Exception {
        getMockMvc().perform(get("/api/v1/project/" + PROJECT_SLUG_1 + "/microscopy-session/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(entities.size())));
    }

    @Test
    public void getMicroscopySessionByMicroscopeId() throws Exception {
        getMockMvc().perform(get("/api/v1/microscopy-session/microscope/" + MICROSCOPE_SLUG))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug", is(ENTITY_SLUG_2)))
                .andExpect(jsonPath("$.microscope.slug", is(MICROSCOPE_SLUG)));
    }
}
