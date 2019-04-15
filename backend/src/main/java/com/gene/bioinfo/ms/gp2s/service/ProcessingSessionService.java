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

import com.gene.bioinfo.ms.gp2s.domain.ProcessingSession;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.repository.ProcessingSessionRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.service.base.BaseProjectRestService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class ProcessingSessionService extends BaseProjectRestService<ProcessingSession> {

    private final DefaultValueService defaultValueService;

    ProcessingSessionService(final ProcessingSessionRepository processingSessionRepository,
                             final ProjectRepository projectRepository,
                             final @NonNull DefaultValueService defaultValueService) {
        super(processingSessionRepository, projectRepository);
        this.defaultValueService = defaultValueService;
    }

    @Override
    public Collection<Project> getItemProjects(@NonNull final String slug) {
        return Optional.ofNullable(this.projectRepository.findByProcessingSession_Slug(slug))
                .orElse(Collections.emptySet());
    }

    @Override
    public Collection<Project> getItemProjects(@NonNull final Long id) {
        return Optional.ofNullable(this.projectRepository.findByProcessingSession_Id(id))
                .orElse(Collections.emptySet());
    }

    @Override
    public ProcessingSession createItem(ProcessingSession input, Project project) {
        ProcessingSession result = super.createItem(input, project);
        this.defaultValueService.saveDefaultValues(project, result);
        return result;
    }

    @Override
    public ProcessingSession updateItem(ProcessingSession input, Project project) {
        ProcessingSession result = super.updateItem(input, project);
        this.defaultValueService.saveDefaultValues(project, result);
        return result;
    }
}
