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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugEntity;
import com.gene.bioinfo.ms.gp2s.domain.sample.LigandComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.ProteinComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.repository.DefaultValueRepository;
import com.gene.bioinfo.ms.gp2s.service.attachment.UploadResponse;
import lombok.NonNull;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CryoEntitiesPreparer extends AdminEntitiesPreparer {

    public static final String MULTIPART_FILE_NAME = "qqfile";

    private final DefaultValueRepository defaultValueRepository;

    public CryoEntitiesPreparer(
            @NonNull final MockMvc mockMvc,
            @NonNull final DefaultValueRepository defaultValueRepository) {
        super(mockMvc);
        this.defaultValueRepository = defaultValueRepository;
    }

    @Override
    public void deleteAllCreated() {
        this.defaultValueRepository.deleteAll();
        super.deleteAllCreated();
    }

    /**
     * @return all cryo entities, exactly one instance of every type.
     */
    public CryoEntities prepareCryoEntities() {
        return prepareCryoEntities(prepareAdminEntities());
    }

    public CryoEntities prepareCryoEntities(@NonNull final AdminEntities adminEntities) {
        final CryoEntities.CryoEntitiesBuilder cryoEntitiesBuilder = CryoEntities.builder();

        final Project project = prepareProject();
        final Protein protein = prepareProtein(project);
        final Ligand ligand = prepareLigand(project);
        final Sample sample = prepareSample(project, protein, ligand);
        final Grid grid = prepareGrid(project,
                sample,
                adminEntities.getGridType(),
                adminEntities.getSurfaceTreatmentProtocol(),
                adminEntities.getVitrificationProtocol(),
                adminEntities.getCryoStorageDevice());
        final MicroscopySession microscopySession = prepareMicroscopySession(project,
                grid,
                adminEntities.getMicroscope(),
                adminEntities.getElectronDetector(),
                null);
        final ProcessingSession processingSession = prepareProcessingSession(project,
                microscopySession,
                adminEntities.getImageProcessingSoftware(),
                adminEntities.getImageProcessingSoftware()
                        .getSoftwareVersions()
                        .iterator().next());

        final Map map = prepareMap(project, processingSession, "map-file.txt");
        final Model model = prepareModel(project, map, "model-file.txt");

        cryoEntitiesBuilder.project(project)
                .protein(protein)
                .ligand(ligand)
                .sample(sample)
                .grid(grid)
                .microscopySession(microscopySession)
                .processingSession(processingSession)
                .map(map)
                .model(model);

        return cryoEntitiesBuilder.build();
    }

    @NonNull
    public Map prepareMap(Project project, ProcessingSession processingSession, String fileName) {
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, fileName, "text/plain", "some text"
                .getBytes());
        try {
            UploadResponse result = uploadFile(file, mockMvcExecutor.API_V1_MAP + "attachment/upload");
            return post(Map.builder()
                            .label("My map")
                            .pixelSize(2.0)
                            .isolevelForSurfaceRendering(1.0)
                            .processingSession(processingSession)
                            .numberOfImages(1024)
                            .symmetryApplied("C4")
                            .estimatedResolutionInBestParts(2.0)
                            .estimatedResolutionInWorstParts(5.0)
                            .estimatedResolutionInAverageParts(3.0)
                            .attachmentFileName(fileName)
                            .attachmentMongoId(result.getMongoId())
                            .build(),
                    project.getSlug());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public Model prepareModel(Project project, Map map, String fileName) {
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, fileName, "text/plain", "some text"
                .getBytes());
        try {
            UploadResponse result = uploadFile(file, mockMvcExecutor.API_V1_MODEL + "attachment/upload");
            List<Map> maps = new ArrayList<>();
            maps.add(map);
            return post(Model.builder()
                            .label("My model")
                            .resolution(1.0)
                            .maps(maps)
                            .attachmentFileName(fileName)
                            .attachmentMongoId(result.getMongoId())
                            .build(),
                    project.getSlug());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public Protein prepareProtein(@NonNull final Project project) {
        return post(Protein.builder()
                           .label("My protein")
                           .tubeLabel("T-1234")
                           .purificationId("P123456")
                           .concentration(Concentration.builder()
                                                       .isDilutedOrConcentrated(false)
                                                       .concentrationType
                                                               (Concentration.ConcentrationType.Concentration)
                                                       .build())
                           .availableForSampleMaking(true)
                           .build(),
                project.getSlug());
    }

    @NonNull
    public Ligand prepareLigand(@NonNull final Project project) {
        return post(Ligand.builder()
                          .label("My ligand")
                          .tubeLabel("T-4321")
                          .conceptId("6")
                          .batchId("G99999999.9-9")
                          .concentration(1.1f)
                          .availableForSampleMaking(true)
                          .build(),
                project.getSlug());
    }

    @NonNull
    public Sample prepareSample(
            @NonNull final Project project,
            @NonNull final Protein protein,
            @NonNull final Ligand ligand) {
        ProteinComponent proteinComponent = ProteinComponent.builder()
                                                            .aliquot(protein)
                                                            .finalConcentration(1.0f)
                                                            .build();
        LigandComponent ligandComponent = LigandComponent.builder()
                                                         .aliquot(ligand)
                                                         .finalConcentration(1.0f)
                                                         .build();
        return post(Sample.builder()
                        .label("My sample")
                        .availableForGridMaking(Boolean.TRUE)
                        .proteinComponent(Collections.singletonList(proteinComponent))
                        .ligandComponent(Collections.singletonList(ligandComponent))
                        .build(),
                project.getSlug());
    }

    private Grid prepareGrid(
            @NonNull final Project project,
            @NonNull final Sample sample,
            @NonNull final GridType gridType,
            @NonNull final SurfaceTreatmentProtocol surfaceTreatmentProtocol,
            @NonNull final VitrificationProtocol vitrificationProtocol,
            @NonNull final CryoStorageDevice cryoStorageDevice) {
        return post(Grid.builder()
                        .label("My grid")
                        .protocolType(Grid.ProtocolType.Cryo)
                        .gridType(gridType)
                        .surfaceTreatmentProtocol(surfaceTreatmentProtocol)
                        .vitrificationProtocol(vitrificationProtocol)
                        .sample(sample)
                        .concentration(Concentration.builder()
                                .isDilutedOrConcentrated(false)
                                .concentrationType(
                                        Concentration.ConcentrationType.Concentration)
                                .build())
                        .cryoStorageDevice(cryoStorageDevice)
                        .boxNumberLabel("B001")
                        .cylinderNumberLabel("C001")
                        .tubeNumberLabel("T001")
                        .build(),
                project.getSlug());
    }

    private MicroscopySession prepareMicroscopySession(
            @NonNull final Project project,
            @NonNull final Grid grid,
            @NonNull final Microscope microscope,
            @NonNull final ElectronDetector electronDetector,
            final SampleHolder sampleHolder) {
        return post(MicroscopySession.builder()
                        .label("My microscopy session")
                        .sessionStart(new Date())
                        .grid(grid)
                        .gridReturnedToStorage(false)
                        .microscope(microscope)
                        .electronDetector(electronDetector)
                        .sampleHolder(sampleHolder)
                        .extractionVoltageKV(3.8f)
                        .nominalMagnification(10000)
                        .exposureDuration(5.0f)
                        .pixelBinning(5)
                        .build(),
                project.getSlug());
    }

    @NonNull
    public ProcessingSession prepareProcessingSession(
            @NonNull final Project project,
            @NonNull final MicroscopySession ms,
            @NonNull final ImageProcessingSoftware ips,
            @NonNull final String softwareVersion) {
        UsedImageProcessingSoftware uips = UsedImageProcessingSoftware.builder()
                .imageProcessingSoftware(ips)
                .softwareVersion(softwareVersion)
                .build();
        return post(ProcessingSession.builder()
                        .label("My processing session")
                        .numberOfPickedParticles(11)
                        .numberOfMicrographs(17)
                        .microscopySessions(Collections.singleton(ms))
                        .usedImageProcessingSoftware(Collections.singletonList(uips))
                        .build(),
                project.getSlug());
    }

    @NonNull
    public <T extends BaseSlugEntity> T post(@NonNull final T entity, @NonNull final String projectSlug) {
        final String url = MockMvcExecutor.url(entity.getClass());
        @NonNull final T createdEntity = this.mockMvcExecutor.post(url, entity, projectSlug);
        allItemsCreated.add(createdEntity);
        return createdEntity;
    }

    @NonNull
    public <T extends BaseSlugEntity> T put(@NonNull final T entity, @NonNull final String projectSlug) {
        final String url = MockMvcExecutor.url(entity.getClass());
        @NonNull final T updatedEntity = this.mockMvcExecutor.put(url, entity, projectSlug);
        //update entity on list
        Collections.replaceAll(this.allItemsCreated, entity, entity);
        return updatedEntity;
    }

    @NonNull
    public UploadResponse uploadFile(@NonNull final MockMultipartFile file, String url) throws
            Exception {
        final MvcResult result = mockMvc.perform(fileUpload(url)
                .file(file))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readValue(result.getResponse().getContentAsByteArray(), UploadResponse.class);
    }

}
