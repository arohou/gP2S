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

package com.gene.bioinfo.ms.gp2s.service.sample;

import com.gene.bioinfo.ms.gp2s.domain.Ligand;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.domain.sample.LigandComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.ProteinComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.LigandRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProteinRepository;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.service.base.BaseProjectRestService;
import com.gene.bioinfo.ms.gp2s.util.Validate;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SampleService extends BaseProjectRestService<Sample> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleService.class);

    private final ProteinRepository proteinRepository;
    private final LigandRepository ligandRepository;

    @Autowired
    SampleService(final SampleRepository sampleRepository,
                  final ProjectRepository projectRepository,
                  @NotNull final ProteinRepository proteinRepository,
                  @NotNull final LigandRepository ligandRepository
    ) {
        super(sampleRepository, projectRepository);
        this.ligandRepository = ligandRepository;
        this.proteinRepository = proteinRepository;
    }

    @NonNull
    public Collection<Project> getItemProjects(@NonNull final String slug) {
        return Optional.ofNullable(this.projectRepository.findBySamples_Slug(slug))
                .orElse(Collections.emptySet());
    }

    @Override
    public Collection<Project> getItemProjects(@NonNull final Long id) {
        return Optional.ofNullable(this.projectRepository.findBySamples_Id(id))
                .orElse(Collections.emptySet());
    }

    @Transactional
    @NonNull
    public Sample createItem(@NonNull final Sample input, @NonNull final Long projectId) {
        return commonCreateItem(input);
    }

    @Transactional
    @NonNull
    public Sample createItem(@NonNull final Sample input,
                             @NonNull @ApiIgnore final String projectSlug) {
        return commonCreateItem(input);
    }

    public Collection<Sample> findSamplesAvailableForGridMaking(@NonNull final Long projectId) {
        return Optional.ofNullable(((SampleRepository) this.repository).findSamplesAvailableForGridMaking(projectId))
                .orElse(Collections.emptyList());
    }

    public Collection<Sample> findSamplesAvailableForGridMaking(@NonNull final String projectSlug) {
        return Optional.ofNullable(((SampleRepository) this.repository).findSamplesAvailableForGridMaking(projectSlug))
                .orElse(Collections.emptyList());
    }

    protected Sample commonCreateItem(@NonNull final Sample input) {
        super.commonCreateItemValidations(input);
        loadProteins(input.getProteinComponent());
        loadLigands(input.getLigandComponent());
        input.getProteinComponent().forEach(component ->
                component.setAliquot(proteinRepository.save(component.getAliquot())));
        input.getLigandComponent().forEach(component ->
                component.setAliquot(ligandRepository.save(component.getAliquot())));

        return super.createItem(input);
    }

    protected void loadProteins(Collection<ProteinComponent> components) {
        Optional.ofNullable(components).orElse(Collections.emptyList()).forEach(c -> {
            Validate.notNull(c.getAliquot(), "Missing protein");
            final Protein protein = c.getAliquot();
            Protein toBeSaved;
            if (protein.getId() != null) {
                toBeSaved = proteinRepository.findOne(protein.getId());
            } else if (protein.getSlug() != null) {
                toBeSaved = proteinRepository.findOneBySlug(protein.getSlug());
            } else {
                Validate.notNull(protein, "Protein not found {%s}", protein);
                return;
            }

            toBeSaved.setAvailableForSampleMaking(protein.getAvailableForSampleMaking());
            c.setAliquot(toBeSaved);
        });
    }

    protected void loadLigands(Collection<LigandComponent> components) {
        Optional.ofNullable(components).orElse(Collections.emptyList()).forEach(c -> {
            Validate.notNull(c.getAliquot(), "Missing ligand");
            final Ligand ligand = c.getAliquot();
            Ligand toBeSaved;
            if (ligand.getId() != null) {
                toBeSaved = ligandRepository.findOne(ligand.getId());
            } else if (ligand.getSlug() != null) {
                toBeSaved = ligandRepository.findOneBySlug(ligand.getSlug());
            } else {
                Validate.notNull(ligand, "Ligand not found {%s}", ligand);
                return;
            }

            toBeSaved.setAvailableForSampleMaking(ligand.getAvailableForSampleMaking());
            c.setAliquot(toBeSaved);
        });
    }

    protected List<Protein> extractProteins(Sample sample) {
        return Optional.ofNullable(sample.getProteinComponent())
                .orElse(Collections.emptyList()).stream()
                .map(ProteinComponent::getAliquot)
                .collect(Collectors.toList());
    }

    protected List<Ligand> extractLigands(Sample sample) {
        return Optional.ofNullable(sample.getLigandComponent())
                .orElse(Collections.emptyList()).stream()
                .map(LigandComponent::getAliquot)
                .collect(Collectors.toList());
    }

    @Transactional
    @NonNull
    @Override
    public Sample updateItem(@NonNull final Sample input) {
        loadProteins(input.getProteinComponent());
        loadLigands(input.getLigandComponent());

        // Take into account previously state of sample when calculating remaining volume.
        Sample originalSample = repository.findOneBySlug(input.getSlug());

        List<Protein> sampleProteins = extractProteins(originalSample);
        List<Ligand> sampleLigands = extractLigands(originalSample);

        Sample result = super.updateItem(input);

        List<Ligand> currentAndPreviousLigands = Stream.concat(extractLigands(result)
                .stream(), sampleLigands.stream()).distinct().collect(Collectors.toList());
        ligandRepository.save(currentAndPreviousLigands);
        List<Protein> currentAndPreviousProteins = Stream.concat(extractProteins(result)
                .stream(), sampleProteins.stream()).distinct().collect(Collectors.toList());
        proteinRepository.save(currentAndPreviousProteins);

        return result;
    }

    public void updateAvailableForGridMaking(@NonNull final Long sampleId, @NonNull final Boolean availabilityForGridMaking) {
        final Sample sample = repository.findOne(sampleId);
        if (sample == null) {
            LOGGER.info("[updateAvailableForGridMaking] Sample not found for ID: " + sampleId);
            throw new ResourceNotFoundException("Sample not found for ID: " + sampleId);
        }
        if (sample.getAvailableForGridMaking() == availabilityForGridMaking) {
            return;
        }
        sample.setAvailableForGridMaking(availabilityForGridMaking);
        repository.save(sample);
    }
}
