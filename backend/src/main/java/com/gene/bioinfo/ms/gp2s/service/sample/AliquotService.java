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
import com.gene.bioinfo.ms.gp2s.domain.sample.Aliquot;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.LigandRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProteinRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@Service
public class AliquotService {
    private static final long DEFAULT_ALIQUOT_PAGE_SIZE = 25;
    ProteinRepository proteinRepository;
    LigandRepository ligandRepository;
    ProjectRepository projectRepository;

    @Autowired
    public AliquotService(final ProteinRepository proteinRepository, final LigandRepository ligandRepository, final ProjectRepository projectRepository) {
        this.proteinRepository = proteinRepository;
        this.ligandRepository = ligandRepository;
        this.projectRepository = projectRepository;
    }

    public List<Aliquot> find(@NonNull String projectSlugOrId, @NonNull String queryString) {
        Project project = loadProject(projectSlugOrId).orElseThrow(() -> new ResourceNotFoundException("There is no " +
                "project for Slug/ID: " + projectSlugOrId));

        List<Protein> proteins = proteinRepository.findAllByProjects_IdOrderByIdDesc(project.getId());
        Stream<Aliquot> proteinAliquots = proteins.stream()
                                                  .map(Aliquot::fromProtein);

        List<Ligand> ligands = ligandRepository.findAllByProjects_IdOrderByIdDesc(project.getId());
        Stream<Aliquot> ligandAliquots = ligands.stream()
                                                .map(Aliquot::fromLigand);

        Stream<Aliquot> aliquots = Stream.concat(proteinAliquots, ligandAliquots)
                                         .filter(Aliquot::getAvailableForSampleMaking)
                                         .filter(aliquot -> aliquot.matchesQuery(queryString))
                                         .sorted(Comparator.comparing(Aliquot::getLabel))
                                         .limit(DEFAULT_ALIQUOT_PAGE_SIZE);
        return aliquots.collect(toList());
    }

    private Optional<Project> loadProject(final String projectSlugOrId) {
        if (isNumeric(projectSlugOrId)) {
            return ofNullable(projectRepository.findOne(parseLong(projectSlugOrId)));
        } else {
            return ofNullable(projectRepository.findOneBySlug(projectSlugOrId));
        }
    }
}
