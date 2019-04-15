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

import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.repository.*;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.service.base.BaseRestService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectService extends BaseRestService<Project> {

    private final SampleRepository sampleRepository;
    private final ProteinRepository proteinRepository;
    private final LigandRepository ligandRepository;
    private final GridRepository gridRepository;
    private final MicroscopySessionRepository microscopySessionRepository;
    private final ProcessingSessionRepository processingSessionRepository;
    private final MapRepository mapRepository;
    private final ModelRepository modelRepository;

    @Autowired
    ProjectService(final ProjectRepository projectRepository,
                   @NonNull final SampleRepository sampleRepository,
                   @NonNull final ProteinRepository proteinRepository,
                   @NonNull final LigandRepository ligandRepository,
                   @NonNull final GridRepository gridRepository,
                   @NonNull final MicroscopySessionRepository microscopySessionRepository,
                   @NonNull final ProcessingSessionRepository processingSessionRepository,
                   @NonNull final MapRepository mapRepository,
                   @NonNull final ModelRepository modelRepository) {
        super(projectRepository);
        this.proteinRepository = proteinRepository;
        this.ligandRepository = ligandRepository;
        this.sampleRepository = sampleRepository;
        this.gridRepository = gridRepository;
        this.microscopySessionRepository = microscopySessionRepository;
        this.processingSessionRepository = processingSessionRepository;
        this.mapRepository = mapRepository;
        this.modelRepository = modelRepository;
    }

    @NonNull
    public List<Protein> getProteins(@NonNull final String projectSlug) {
        return Optional.ofNullable(this.proteinRepository.findAllByProjects_SlugOrderByIdDesc(projectSlug))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public List<Protein> getProteins(@NonNull final Long id) {
        return Optional.ofNullable(this.proteinRepository.findAllByProjects_IdOrderByIdDesc(id))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Integer countProteins(@NonNull final String projectSlug) {
        return this.proteinRepository.countForProject(projectSlug);
    }

    @NonNull
    public Integer countProteins(@NonNull final Long id) {
        return this.proteinRepository.countForProject(id);
    }

    @NonNull
    public List<Ligand> getLigands(@NonNull final String projectSlug) {
        return Optional.ofNullable(this.ligandRepository.findAllByProjects_SlugOrderByIdDesc(projectSlug))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public List<Ligand> getLigands(@NonNull final Long id) {
        return Optional.ofNullable(this.ligandRepository.findAllByProjects_IdOrderByIdDesc(id))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Integer countLigands(@NonNull final String projectSlug) {
        return this.ligandRepository.countForProject(projectSlug);
    }

    @NonNull
    public Integer countLigands(@NonNull final Long id) {
        return this.ligandRepository.countForProject(id);
    }

    @NonNull
    public List<Sample> getSamples(@NonNull final String projectSlug) {
        return Optional.ofNullable(this.sampleRepository.findByProject_SlugOrderByIdDesc(projectSlug))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public List<Sample> getSamples(@NonNull final Long id) {
        return Optional.ofNullable(this.sampleRepository.findByProject_IdOrderByIdDesc(id))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Integer countSamples(@NonNull final String projectSlug) {
        return this.sampleRepository.countForProject(projectSlug);
    }

    @NonNull
    public Integer countSamples(@NonNull final Long id) {
        return this.sampleRepository.countForProject(id);
    }

    private List<Sample> decorateSamplesWithProjectLabels(List<Sample> samples) {
        List<Long> sampleIds = new ArrayList<>();
        for (Sample sample : samples) {
            sampleIds.add(sample.getId());
        }
        if (sampleIds.size() == 0) {
            return new ArrayList<>();
        }

        List<List<Object>> projectLabelsList = this.sampleRepository.findProjectLabels(sampleIds);
        Map<Long, String> projectLabelsMap = new HashMap<>();
        for (List<Object> labelMap : projectLabelsList) {
            if (labelMap == null || labelMap.size() != 2) {
                continue;
            }
            Long sampleId = (Long) labelMap.get(0);
            String projectLabels = projectLabelsMap.get(sampleId);
            if (projectLabels == null) {
                projectLabelsMap.put(sampleId, (String) labelMap.get(1));
            } else {
                projectLabelsMap.put(sampleId, projectLabels + ", " + labelMap.get(1));
            }
        }

        for (Sample sample : samples) {
            String projectLabels = projectLabelsMap.get(sample.getId());
            if (projectLabels != null) {
                sample.setProjectLabels(projectLabels);
            }
        }
        return samples;
    }

    @NonNull
    public List<Sample> getSamplesWithProjectLabels(@NonNull final String projectSlug) {
        return decorateSamplesWithProjectLabels(Optional.ofNullable(
                this.sampleRepository.findByProject_SlugOrderByIdDesc(projectSlug)).orElse(Collections.emptyList()));
    }

    @NonNull
    public List<Sample> getSamplesWithProjectLabels(@NonNull final Long id) {
        return decorateSamplesWithProjectLabels(Optional.ofNullable(
                this.sampleRepository.findByProject_IdOrderByIdDesc(id)).orElse(Collections.emptyList()));
    }

    @NonNull
    private Grid calculateAvailability(@NonNull final Grid grid) {
        grid.setIsAvailable(gridRepository.hasBeenReturnedToStorage(grid.getId()));
        return grid;
    }

    @NonNull
    private List<Grid> calculateAvailabilityOfGrids(@NonNull final List<Grid> grids) {
        return grids.stream().map(this::calculateAvailability).collect(Collectors.toList());
    }

    @NonNull
    public List<Grid> getGrids(@NonNull final String projectSlug) {
        return Optional.ofNullable(this.gridRepository.findByProject_SlugOrderByIdDesc(projectSlug))
                .map(this::calculateAvailabilityOfGrids)
                .orElse(Collections.emptyList());
    }

    @NonNull
    public List<Grid> getGrids(@NonNull final Long id) {
        return Optional.ofNullable(this.gridRepository.findByProject_IdOrderByIdDesc(id))
                .map(this::calculateAvailabilityOfGrids)
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Integer countGrids(@NonNull final String projectSlug) {
        return this.gridRepository.countForProject(projectSlug);
    }

    @NonNull
    public Integer countGrids(@NonNull final Long id) {
        return this.gridRepository.countForProject(id);
    }

    @NonNull
    public List<MicroscopySession> getMicroscopySessions(@NonNull final Long id) {
        return Optional.ofNullable(this.microscopySessionRepository.findByProject_IdOrderByIdDesc(id))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public List<MicroscopySession> getMicroscopySessions(@NonNull final String projectSlug) {
        return Optional.ofNullable(this.microscopySessionRepository.findByProject_SlugOrderByIdDesc(projectSlug))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Integer countMicroscopySessions(@NonNull final String projectSlug) {
        return this.microscopySessionRepository.countForProject(projectSlug);
    }

    @NonNull
    public Integer countMicroscopySessions(@NonNull final Long id) {
        return this.microscopySessionRepository.countForProject(id);
    }

    @NonNull
    public List<ProcessingSession> getProcessingSessions(@NonNull final Long id) {
        return Optional.ofNullable(this.processingSessionRepository.findByProject_IdOrderByIdDesc(id))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public List<ProcessingSession> getProcessingSessions(@NonNull final String projectSlug) {
        return Optional.ofNullable(this.processingSessionRepository.findByProject_SlugOrderByIdDesc(projectSlug))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Integer countProcessingSessions(@NonNull final String projectSlug) {
        return this.processingSessionRepository.countForProject(projectSlug);
    }

    @NonNull
    public Integer countProcessingSessions(@NonNull final Long id) {
        return this.processingSessionRepository.countForProject(id);
    }

    @NonNull
    public List<com.gene.bioinfo.ms.gp2s.domain.Map> getMaps(@NonNull final Long id) {
        return Optional.ofNullable(this.mapRepository.findByProject_IdOrderByIdDesc(id))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public List<com.gene.bioinfo.ms.gp2s.domain.Map> getMaps(@NonNull final String projectSlug) {
        return Optional.ofNullable(this.mapRepository.findByProject_SlugOrderByIdDesc(projectSlug))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Integer countMaps(@NonNull final String projectSlug) {
        return this.mapRepository.countForProject(projectSlug);
    }

    @NonNull
    public Integer countMaps(@NonNull final Long id) {
        return this.mapRepository.countForProject(id);
    }

    @NonNull
    public List<com.gene.bioinfo.ms.gp2s.domain.Model> getModels(@NonNull final Long id) {
        return Optional.ofNullable(this.modelRepository.findByProject_IdOrderByIdDesc(id))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public List<com.gene.bioinfo.ms.gp2s.domain.Model> getModels(@NonNull final String projectSlug) {
        return Optional.ofNullable(this.modelRepository.findByProject_SlugOrderByIdDesc(projectSlug))
                .orElse(Collections.emptyList());
    }

    @NonNull
    public Integer countModels(@NonNull final String projectSlug) {
        return this.modelRepository.countForProject(projectSlug);
    }

    @NonNull
    public Integer countModels(@NonNull final Long id) {
        return this.modelRepository.countForProject(id);
    }
}
