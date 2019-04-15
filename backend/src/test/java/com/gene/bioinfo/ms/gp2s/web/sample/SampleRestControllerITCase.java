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

package com.gene.bioinfo.ms.gp2s.web.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.Ligand;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.domain.sample.LigandComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.ProteinComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.repository.LigandRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProteinRepository;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SampleRestControllerITCase extends BaseRestITCase<Sample> {

    protected static final String PROJECT_SLUG_1 = "project-slug-1";
    protected static final String PROJECT_SLUG_2 = "project-slug-2";
    protected static final String PROJECT_LABEL_1 = "project 1";
    protected static final String PROJECT_LABEL_2 = "project 2";
    protected static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_1 = "project-management-system-slug-1";
    protected static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_2 = "project-management-system-slug-2";
    protected static final String PROTEIN_SLUG_1 = "protein-slug-1";
    protected static final String PROTEIN_SLUG_2 = "protein-slug-2";
    protected static final String PROTEIN_LABEL_1 = "protein 1";
    protected static final String PROTEIN_LABEL_2 = "protein 2";
    protected static final String LIGAND_SLUG_1 = "ligand-slug-1";
    protected static final String LIGAND_SLUG_2 = "ligand-slug-2";
    protected static final String LIGAND_LABEL_1 = "ligand 1";
    protected static final String LIGAND_LABEL_2 = "ligand 2";
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private ProteinRepository proteinRepository;
    @Autowired
    private LigandRepository ligandRepository;
    private List<Project> projects;
    private Protein protein1;
    private Protein protein2;
    private Ligand ligand1;
    private Ligand ligand2;

    @Before
    public void setUp() throws Exception {
        this.initBaseRestTests(sampleRepository);
    }

    @Test
    public void readSampleProjectsById() throws Exception {
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
    public void shouldFailWhenInvalidIDProvidedForSample() throws Exception {
        //given

        //when
        getMockMvc()
                .perform(patch(URI + -10L).contentType(JSON_CONTENT_TYPE)
                        .content("{\"availableForGridMaking\": false}"))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateAvailableForGridMaking() throws Exception {
        //given
        final Sample sample = sampleRepository.findOne(entities.get(1)
                .getId());
        sample.setAvailableForGridMaking(true);
        sampleRepository.save(sample);

        final String json = "{\"availableForGridMaking\": false}";
        final String samplePath = URI + sample.getId();

        //when
        getMockMvc()
                .perform(patch(samplePath).contentType(JSON_CONTENT_TYPE)
                        .content(json))
                .andExpect(status().isOk());

        //then
        getMockMvc()
                .perform(get(samplePath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.availableForGridMaking", is(false)));
    }

    @Test
    public void shouldFailWhenAvailableForGridMakingIsNotPassed() throws Exception {
        //given
        final String json = "{\"wrong_property_name\": false}";
        final String samplePath = URI + entities.get(1)
                .getId();

        //when
        getMockMvc()
                .perform(patch(samplePath).contentType(JSON_CONTENT_TYPE)
                        .content(json))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findSamplesAvailableForGridMaking() throws Exception {
        getMockMvc()
                .perform(get(URI + "/findSamplesAvailableForGridMaking/" + PROJECT_SLUG_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void readSampleProjectsBySlug() throws Exception {
        //check if new sample is associated to two projects
        getMockMvc()
                .perform(get(URI + ENTITY_SLUG_2 + "/projects"))
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
    public void readSamplesProjectionFromProjectById() throws Exception {
        final Long id = projects.get(0).getId();
        final Sample sample = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + id + "/sample"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(sample.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(sample.getSlug())))
                .andExpect(jsonPath("$[0].label", is(sample.getLabel())));
    }

    @Test
    public void readSamplesProjectionFromProjectBySlug() throws Exception {
        final String slug = projects.get(0).getSlug();
        final Sample sample = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + slug + "/sample"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(sample.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(sample.getSlug())))
                .andExpect(jsonPath("$[0].label", is(sample.getLabel())));
    }

    @Test
    public void readProteinComponentsAndProteinFromSample() throws Exception {
        getMockMvc()
                .perform(get(URI + ENTITY_SLUG_2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.proteinComponent", hasSize(2)))
                .andExpect(jsonPath("$.proteinComponent[0].finalConcentration", is(2.1)))
                .andExpect(jsonPath("$.proteinComponent[1].finalConcentration", is(1.1)))

                .andExpect(jsonPath("$.proteinComponent[1].aliquot.id", is(protein1.getId().intValue())))
                .andExpect(jsonPath("$.proteinComponent[0].aliquot.id", is(protein2.getId().intValue())))
                .andExpect(jsonPath("$.proteinComponent[1].aliquot.label", is(PROTEIN_LABEL_1)))
                .andExpect(jsonPath("$.proteinComponent[0].aliquot.label", is(PROTEIN_LABEL_2)));
    }

    @Test
    public void deleteProteinComponent() throws Exception {
        final Sample sample = sampleRepository.findOne(entities.get(1).getId());
        final ProteinComponent component = ProteinComponent.builder()
                                                           .id(entities.get(1)
                        .getProteinComponent()
                        .get(1)
                        .getId())
                                                           .finalConcentration(1.1f)
                                                           .aliquot(protein1)
                                                           .version(0)
                                                           .build();
        sample.setProteinComponent(Collections.singletonList(component));
        sample.setLigandComponent(Collections.emptyList());
        final String json = new ObjectMapper().writeValueAsString(sample);

        getMockMvc()
                .perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.proteinComponent", hasSize(1)))
                .andExpect(jsonPath("$.proteinComponent[0].finalConcentration", is(1.1)))

                .andExpect(jsonPath("$.proteinComponent[0].aliquot.id", is(protein1.getId().intValue())))
                .andExpect(jsonPath("$.proteinComponent[0].aliquot.label", is(PROTEIN_LABEL_1)));
    }

    @Test
    public void updateProteinComponent() throws Exception {
        final Sample sample = sampleRepository.findOne(entities.get(1).getId());
        final ProteinComponent component = ProteinComponent.builder()
                .id(entities.get(1)
                        .getProteinComponent()
                        .get(0)
                        .getId())
                .version(entities.get(1)
                        .getProteinComponent()
                        .get(0)
                        .getVersion())
                .finalConcentration(7.1f)
                .aliquot(protein1)
                .build();
        sample.setProteinComponent(Collections.singletonList(component));
        sample.setLigandComponent(Collections.emptyList());
        final String json = new ObjectMapper().writeValueAsString(sample);

        getMockMvc()
                .perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.proteinComponent", hasSize(1)))
                .andExpect(jsonPath("$.proteinComponent[0].finalConcentration", is(7.1)))
                .andExpect(jsonPath("$.proteinComponent[0].aliquot.availableForSampleMaking", is(true)));

    }

    @Test
    public void createSampleForProteinAndLigandRequireOnlyIdOrSlug() throws Exception {
        List<ProteinComponent> proteinComponents = new ArrayList<>();
        proteinComponents.add(ProteinComponent.builder()
                .finalConcentration(1f)
                .aliquot(Protein.builder().id(protein1.getId()).build())
                .build());
        proteinComponents.add(ProteinComponent.builder()
                .finalConcentration(1f)
                .aliquot(Protein.builder().slug(protein2.getSlug()).build())
                .build());

        List<LigandComponent> ligandComponents = new ArrayList<>();
        ligandComponents.add(LigandComponent.builder()
                .finalConcentration(1f)
                .aliquot(Ligand.builder().id(ligand1.getId()).build())
                .build());
        ligandComponents.add(LigandComponent.builder()
                .finalConcentration(1f)
                .aliquot(Ligand.builder().slug(ligand2.getSlug()).build())
                .build());

        final String label = "A new entity";
        Sample sample = Sample.builder().label(label).build();
        sample.setProteinComponent(proteinComponents);
        sample.setLigandComponent(ligandComponents);

        final String json = json(sample);

        getMockMvc().perform(post(URI + "/project-slug").contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.proteinComponent[0].aliquot.id", is(protein1.getId().intValue())))
                .andExpect(jsonPath("$.proteinComponent[0].aliquot.slug", is(protein1.getSlug())))
                .andExpect(jsonPath("$.proteinComponent[1].aliquot.id", is(protein2.getId().intValue())))
                .andExpect(jsonPath("$.proteinComponent[1].aliquot.slug", is(protein2.getSlug())))

                .andExpect(jsonPath("$.ligandComponent[0].aliquot.id", is(ligand1.getId().intValue())))
                .andExpect(jsonPath("$.ligandComponent[0].aliquot.slug", is(ligand1.getSlug())))
                .andExpect(jsonPath("$.ligandComponent[1].aliquot.id", is(ligand2.getId().intValue())))
                .andExpect(jsonPath("$.ligandComponent[1].aliquot.slug", is(ligand2.getSlug())));

    }


    @Test
    public void shouldValidationReturnMultipleErrors() throws Exception {
        final Sample sample = Sample.builder().incubationTemperature(-300f).incubationTime(-2f).build();
        sample.setProteinComponent(Arrays.asList(ProteinComponent.builder().build(),
                ProteinComponent.builder().aliquot(Protein.builder().build()).build()));
        final String json = new ObjectMapper().writeValueAsString(sample);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.label", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.incubationTime", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.incubationTemperature", is(not(isEmptyOrNullString()))));

    }

    @Test
    public void readLigandComponentsAndLigandFromComponent() throws Exception {
        getMockMvc()
                .perform(get(URI + ENTITY_SLUG_2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.ligandComponent", hasSize(2)))
                .andExpect(jsonPath("$.ligandComponent[0].finalConcentration", is(2.1)))
                .andExpect(jsonPath("$.ligandComponent[1].finalConcentration", is(1.1)))

                .andExpect(jsonPath("$.ligandComponent[1].aliquot.id", is(ligand1.getId().intValue())))
                .andExpect(jsonPath("$.ligandComponent[0].aliquot.id", is(ligand2.getId().intValue())))
                .andExpect(jsonPath("$.ligandComponent[1].aliquot.label", is(LIGAND_LABEL_1)))
                .andExpect(jsonPath("$.ligandComponent[0].aliquot.label", is(LIGAND_LABEL_2)));
    }

    @Test
    public void deleteLigandComponents() throws Exception {
        final Sample sample = sampleRepository.findOne(entities.get(1).getId());
        sample.setLigandComponent(Collections.emptyList());
        //Satisfying at least one protein component requirement
        sample.setProteinComponent(entities.get(1).getProteinComponent());
        final String json = new ObjectMapper().writeValueAsString(sample);

        getMockMvc()
                .perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.ligandComponent", hasSize(0)));
    }

    @Test
    public void updateLigandComponents() throws Exception {
        final Sample sample = sampleRepository.findOne(entities.get(1).getId());
        LigandComponent component = LigandComponent.builder()
                .id(entities.get(1).getLigandComponent().get(0).getId())
                .finalConcentration(7.1f)
                .aliquot(ligand1)
                .version(0)
                .build();
        sample.setLigandComponent(Collections.singletonList(component));
        //Satisfying at least one protein component requirement
        sample.setProteinComponent(entities.get(1).getProteinComponent());
        final String json = new ObjectMapper().writeValueAsString(sample);

        getMockMvc()
                .perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.ligandComponent", hasSize(1)))
                .andExpect(jsonPath("$.ligandComponent[0].id", is(entities.get(1).getLigandComponent().get(0).getId()
                        .intValue())))
                .andExpect(jsonPath("$.ligandComponent[0].finalConcentration", is(7.1)))
                .andExpect(jsonPath("$.ligandComponent[0].version", is(1)))
                .andExpect(jsonPath("$.ligandComponent[0].aliquot.availableForSampleMaking", is(true)));
    }

    @Test
    public void updateBothProteinAndLigandComponent() throws Exception {
        final Sample sample = sampleRepository.findOne(entities.get(1).getId());
        final ProteinComponent proteinComponent = ProteinComponent.builder()
                .id(entities.get(1)
                        .getProteinComponent()
                        .get(0)
                        .getId())
                .version(entities.get(1)
                        .getProteinComponent()
                        .get(0)
                        .getVersion())
                .finalConcentration(7.1f)
                .aliquot(protein1)
                .build();
        sample.setProteinComponent(Collections.singletonList(proteinComponent));
        LigandComponent ligandComponent = LigandComponent.builder()
                .finalConcentration(7.1f)
                .aliquot(ligand1)
                .version(0)
                .build();
        sample.setLigandComponent(Collections.singletonList(ligandComponent));
        final String json = new ObjectMapper().writeValueAsString(sample);

        getMockMvc()
                .perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.proteinComponent", hasSize(1)))
                .andExpect(jsonPath("$.proteinComponent[0].finalConcentration", is(7.1)))
                .andExpect(jsonPath("$.proteinComponent[0].aliquot.availableForSampleMaking", is(true)))
                .andExpect(jsonPath("$.ligandComponent", hasSize(1)))
                .andExpect(jsonPath("$.ligandComponent[0].finalConcentration", is(7.1)))
                .andExpect(jsonPath("$.ligandComponent[0].version", is(0)))
                .andExpect(jsonPath("$.ligandComponent[0].aliquot.availableForSampleMaking", is(true)));
    }

    @Test
    public void countSamples() throws Exception {
        getMockMvc().perform(get("/api/v1/project/" + PROJECT_SLUG_1 + "/sample/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(entities.size())));
    }

    @Override
    public String getUri() {
        return "/api/v1/sample/";
    }

    @Override
    public Sample createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        List<ProteinComponent> proteinComponents = new ArrayList<>();
        proteinComponents.add(ProteinComponent.builder()
                .finalConcentration(1.1f)
                .aliquot(protein1)
                .build());
        proteinComponents.add(ProteinComponent.builder()
                .finalConcentration(2.1f)
                .aliquot(protein2)
                .build());

        List<LigandComponent> ligandComponents = new ArrayList<>();
        ligandComponents.add(LigandComponent.builder()
                .finalConcentration(1.1f)
                .aliquot(ligand1)
                .build());
        ligandComponents.add(LigandComponent.builder()
                .finalConcentration(2.1f)
                .aliquot(ligand2)
                .build());

        Sample sample = Sample.builder().id(id).label(label).slug(slug).build();
        sample.setProteinComponent(proteinComponents);
        sample.setLigandComponent(ligandComponents);
        return sample;
    }

    @Override
    public void cleanupRepository() {
        super.cleanupRepository();
        ligandRepository.deleteAll();
        proteinRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Override
    public void prepareEntities() {
        Project p1 = projectRepository.save(Project.builder()
                .label(PROJECT_LABEL_1)
                .slug(PROJECT_SLUG_1)
                .projectManagementSystemSlug(PROJECT_MANAGEMENT_SYSTEM_SLUG_1)
                .build());
        Project p2 = projectRepository.save(Project.builder()
                .label(PROJECT_LABEL_2)
                .slug(PROJECT_SLUG_2)
                .projectManagementSystemSlug(PROJECT_MANAGEMENT_SYSTEM_SLUG_2)
                .build());

        protein1 = proteinRepository.save(Protein.builder()
                .label(PROTEIN_LABEL_1)
                .slug(PROTEIN_SLUG_1)
                .concentration(buildConcentration())
                .availableForSampleMaking(true)
                .projects(Collections.singletonList(p1))
                .build());
        protein2 = proteinRepository.save(Protein.builder()
                .label(PROTEIN_LABEL_2)
                .slug(PROTEIN_SLUG_2)
                .concentration(buildConcentration())
                .availableForSampleMaking(true)
                .projects(Collections.singletonList(p2))
                .build());

        ligand1 = ligandRepository.save(Ligand.builder()
                .label(LIGAND_LABEL_1)
                .slug(LIGAND_SLUG_1)
                .availableForSampleMaking(true)
                .projects(Collections.singletonList(p1))
                .build());
        ligand2 = ligandRepository.save(Ligand.builder()
                .label(LIGAND_LABEL_2)
                .slug(LIGAND_SLUG_2)
                .availableForSampleMaking(true)
                .projects(Collections.singletonList(p2))
                .build());

        projects = Arrays.asList(p1, p2);

        super.prepareEntities();
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        Sample sample = createEntity(null, label, null);
        return json(sample);
    }

    @Override
    public String getCreateUri() {
        return URI + "/" + PROJECT_SLUG_1;
    }
}
