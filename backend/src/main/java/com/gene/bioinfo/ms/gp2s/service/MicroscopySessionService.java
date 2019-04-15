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

import com.gene.bioinfo.ms.gp2s.domain.Microscope;
import com.gene.bioinfo.ms.gp2s.domain.MicroscopySession;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.repository.MicroscopeRepository;
import com.gene.bioinfo.ms.gp2s.repository.MicroscopySessionRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.service.base.BaseProjectRestService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.time.Instant;
import java.util.*;

@Service
public class MicroscopySessionService extends BaseProjectRestService<MicroscopySession> {

    private final DefaultValueService defaultValueService;
    private final MicroscopeRepository microscopeRepository;

    MicroscopySessionService(final MicroscopySessionRepository microscopySessionRepository,
                             final ProjectRepository projectRepository,
                             final MicroscopeRepository microscopeRepository,
                             @NonNull final DefaultValueService defaultValueService) {
        super(microscopySessionRepository, projectRepository);
        this.defaultValueService = defaultValueService;
        this.microscopeRepository = microscopeRepository;
    }

    /**
     * Microscopy session INSERT. When inserting a microscopy session, other sessions that have their start date
     * before the start date of the new session get a finish date = start date of the new session minus 1 second.
     *
     * @param microscopySession Session to persist.
     * @return Persisted session.
     */
    private void updateEarlierMicrosocpySessions(@NonNull final MicroscopySession microscopySession) {
        Date sessionStart = microscopySession.getSessionStart();
        if (sessionStart != null && microscopySession.getMicroscope() != null) {
            Instant oneSecondBeforeSessionStartDate = sessionStart.toInstant();
            oneSecondBeforeSessionStartDate = oneSecondBeforeSessionStartDate.minusSeconds(1);

            ((MicroscopySessionRepository) this.repository).updateSetFinishToEarlierSessions(sessionStart,
                    Date.from(oneSecondBeforeSessionStartDate), microscopySession.getMicroscope()); // Update entities.
        }
    }

    @Override
    public MicroscopySession createItem(@NonNull final MicroscopySession microscopySession) {
        updateEarlierMicrosocpySessions(microscopySession);
        return super.createItem(microscopySession);
    }

    @Override
    public Collection<Project> getItemProjects(String slug) {
        return Optional.ofNullable(this.projectRepository.findByMicroscopySession_Slug(slug))
                .orElse(Collections.emptySet());
    }

    @Override
    public Collection<Project> getItemProjects(Long id) {
        return Optional.ofNullable(this.projectRepository.findByMicroscopySession_Id(id))
                .orElse(Collections.emptySet());
    }

    public MicroscopySession getItemByMicroscopeSlugOrId(String microscopeSlugOrId) {
        Long microscopeId = ValidationUtils.isAnId(microscopeSlugOrId) ? Long.parseLong(microscopeSlugOrId) : null;

        // Check if session exists.
        List<MicroscopySession> microscopySessions = microscopeId != null ?
                ((MicroscopySessionRepository) this.repository).findByMicroscopeId(microscopeId)
                : ((MicroscopySessionRepository) this.repository).findByMicroscopeSlug(microscopeSlugOrId);

        if (microscopySessions.size() > 0) {
            return microscopySessions.get(0);
        }

        // Check if microscope exists.
        Microscope microscope = microscopeId != null ? microscopeRepository.findOne(microscopeId)
                : microscopeRepository.findOneBySlug(microscopeSlugOrId);
        if (microscope == null) {
            throw new IllegalArgumentException("Microscope does not exist");
        }

        throw new NoSuchElementException("Active microscopy session not found");
    }

    @Transactional
    @Override
    public MicroscopySession createItem(@NonNull final MicroscopySession input,
                                        @NonNull final Long projectId) {
        final Project project = projectRepository.findOne(projectId);
        return commonCreateItem(project, input);
    }

    @Transactional
    @Override
    public MicroscopySession createItem(@NonNull final MicroscopySession input,
                                        @NonNull @ApiIgnore final String projectSlug) {
        final Project project = projectRepository.findOneBySlug(projectSlug);
        return commonCreateItem(project, input);
    }

    protected MicroscopySession commonCreateItem(@NonNull final Project project,
                                                 @NonNull final MicroscopySession input) {
        super.commonCreateItemValidations(input);
        updateEarlierMicrosocpySessions(input);
        final MicroscopySession result = super.createItem(input);
        this.defaultValueService.saveDefaultValues(project, input);
        return result;
    }

    @Transactional
    @NonNull
    @Override
    @Deprecated()
    public MicroscopySession updateItem(@NonNull final MicroscopySession input) {
        final MicroscopySession result = super.updateItem(input);
        return result;
    }

    @Transactional
    @NonNull
    public MicroscopySession updateItem(@NonNull final MicroscopySession input, @NonNull final Long projectId) {
        return updateItem(input, projectRepository.findOne(projectId));
    }

    @Transactional
    @NonNull
    public MicroscopySession updateItem(@NonNull final MicroscopySession input, @NonNull final String projectSlug) {
        return updateItem(input, projectRepository.findOneBySlug(projectSlug));
    }

    @Transactional
    @NonNull
    public MicroscopySession updateItem(@NonNull final MicroscopySession input, @NonNull final Project project) {
        final MicroscopySession result = super.updateItem(input);
        this.defaultValueService.saveDefaultValues(project, input);
        return result;
    }
}
