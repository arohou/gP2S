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

import com.gene.bioinfo.ms.gp2s.domain.Ligand;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.LigandRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
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
public class LigandService extends BaseProjectRestService<Ligand> {

    private final SampleRepository sampleRepository;

    @Autowired
    LigandService(final LigandRepository ligandRepository,
                  final ProjectRepository projectRepository,
                  @NonNull final SampleRepository sampleRepository) {
        super(ligandRepository, projectRepository);
        this.sampleRepository = sampleRepository;
    }

    @NonNull
    public Collection<Project> getItemProjects(@NonNull final String ligandSlug) {
        return Optional.ofNullable(this.repository.findOneBySlug(ligandSlug)).map(Ligand::getProjects)
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Collection<Project> getItemProjects(@NonNull final Long id) {
        return Optional.ofNullable(this.repository.findOne(id)).map(Ligand::getProjects)
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Ligand createItem(@NonNull final Ligand input, @NonNull final Long projectId) {
        if (!projectRepository.exists(projectId)) {
            throw new ResourceNotFoundException("Project with specified id doesn't exists: " + projectId);
        }

        final Project project = projectRepository.findOne(projectId);
        return commonCreateItem(input, project);
    }

    public Ligand findRecentAvailableLigand(@NonNull final String conceptId) {
        return Optional.ofNullable(((LigandRepository) this.repository)
                .findAvailableLigandsByConceptId(conceptId))
                .orElse(Collections.emptyList())
                .stream().findFirst().orElse(null);
    }

    @NonNull
    public Ligand createItem(@NonNull final Ligand input, @NonNull final String projectSlug) {
        if (!projectRepository.existsBySlug(projectSlug)) {
            throw new ResourceNotFoundException("Project with specified slug doesn't exists: " + projectSlug);
        }

        final Project project = projectRepository.findOneBySlug(projectSlug);
        return commonCreateItem(input, project);
    }

    @NonNull
    protected Ligand commonCreateItem(@NonNull final Ligand input, @NonNull final Project project) {
        input.setProjects(Collections.singletonList(project));
        return super.createItem(input);
    }

    public Collection<Project> disconnectProject(@NonNull final Long ligandId, @NonNull final Long projectId) {
        final Ligand ligand = Optional.ofNullable(repository.findOne(ligandId)).orElseThrow(() ->
                new ResourceNotFoundException("Ligand not found"));

        final Project project = ligand.getProjects().stream().filter(p -> p.getId().equals(projectId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Association to project not found"));

        ligand.getProjects().remove(project);
        repository.save(ligand);

        return this.getItemProjects(ligandId);
    }

    public Collection<Project> connectProject(@NonNull final Long ligandId,
                                              @NonNull final Long projectId) {
        final Ligand ligand = Optional.ofNullable(repository.findOne(ligandId)).orElseThrow
                (ResourceNotFoundException::new);
        final Project project = Optional.ofNullable(projectRepository.findOne(projectId)).orElseThrow
                (ResourceNotFoundException::new);
        if (ligand.getProjects().contains(project)) {
            throw new ValidationException("Association already exists");
        }

        ligand.getProjects().add(project);
        repository.save(ligand);

        return this.getItemProjects(ligandId);
    }
}
