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

package com.gene.bioinfo.ms.gp2s.service;

import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProteinRepository;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.service.base.BaseProjectRestService;
import com.netflix.config.validation.ValidationException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class ProteinService extends BaseProjectRestService<Protein> {

    private final SampleRepository sampleRepository;

    @Autowired
    ProteinService(final ProteinRepository proteinRepository,
                   final ProjectRepository projectRepository,
                   @NonNull final SampleRepository sampleRepository) {
        super(proteinRepository, projectRepository);
        this.sampleRepository = sampleRepository;
    }

    @NonNull
    public Collection<Project> getItemProjects(@NonNull final String proteinSlug) {
        return Optional.ofNullable(this.repository.findOneBySlug(proteinSlug)).map(Protein::getProjects)
                .orElse(Collections.emptyList());
    }

    @Override
    public Collection<Project> getItemProjects(@NonNull final Long id) {
        return Optional.ofNullable(this.repository.findOne(id)).map(Protein::getProjects)
                .orElse(Collections.emptyList());
    }

    public Protein findRecentAvailableProtein(@NonNull final String purificationId) {
        return Optional.ofNullable(((ProteinRepository) this.repository).findAvailableProteins(purificationId))
                .orElse(Collections.emptyList())
                .stream().findFirst().orElse(null);
    }

    @NonNull
    public Protein createItem(@NonNull final Protein input, @NonNull final Long projectId) {
        if (!projectRepository.exists(projectId)) {
            throw new ResourceNotFoundException("Project with specified id doesn't exist: " + projectId);
        }

        final Project project = projectRepository.findOne(projectId);
        return commonCreateItem(input, project);
    }

    @NonNull
    public Protein createItem(@NonNull final Protein input, @NonNull final String projectSlug) {
        if (!projectRepository.existsBySlug(projectSlug)) {
            throw new ResourceNotFoundException("Project with specified slug doesn't exist: " + projectSlug);
        }

        final Project project = projectRepository.findOneBySlug(projectSlug);
        return commonCreateItem(input, project);
    }

    @NonNull
    protected Protein commonCreateItem(@NonNull final Protein input, @NonNull final Project project) {
        input.setProjects(Collections.singletonList(project));
        return super.createItem(input);
    }

    public Collection<Project> disconnectProject(@NonNull final Long proteinId, @NonNull final Long projectId) {
        final Protein protein = Optional.ofNullable(repository.findOne(proteinId)).orElseThrow(() ->
                new ResourceNotFoundException("Protein not found"));

        final Project project = protein.getProjects().stream().filter(p -> p.getId().equals(projectId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Association to project not found"));

        protein.getProjects().remove(project);
        repository.save(protein);

        return this.getItemProjects(proteinId);
    }

    public Collection<Project> connectProject(@NonNull final Long proteinId,
                                              @NonNull final Long projectId) {
        final Protein protein = Optional.ofNullable(repository.findOne(proteinId)).orElseThrow
                (ResourceNotFoundException::new);
        final Project project = Optional.ofNullable(projectRepository.findOne(projectId)).orElseThrow
                (ResourceNotFoundException::new);
        if (protein.getProjects().contains(project)) {
            throw new ValidationException("Association already exists");
        }

        protein.getProjects().add(project);
        repository.save(protein);

        return this.getItemProjects(proteinId);
    }
}
