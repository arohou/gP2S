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

package com.gene.bioinfo.ms.gp2s.service.base;

import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * A {@link BaseRestService} utilizing {@link com.gene.bioinfo.ms.gp2s.service.ProjectService}
 *
 * @author Cezary Krzyżanowski on 10.08.2017.
 */
public abstract class BaseProjectRestService<T extends BaseSlugAndLabelEntity> extends BaseRestService<T>
        implements RestService<T> {

    protected final ProjectRepository projectRepository;

    protected BaseProjectRestService(final BaseSlugRepository<T> repository,
                                     @NonNull final ProjectRepository projectRepository) {
        super(repository);
        this.projectRepository = projectRepository;
    }

    @NonNull
    protected Project getProjectById(@NonNull final Long id) {
        return Optional.ofNullable(projectRepository.findOne(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project with specified id doesn't exist: " + id));
    }

    @NonNull
    protected Project getProjectBySlug(@NonNull final String slug) {
        return Optional.ofNullable(projectRepository.findOneBySlug(slug))
                .orElseThrow(() -> new ResourceNotFoundException("Project with specified slug doesn't exist: " + slug));
    }

    @NonNull
    public abstract Collection<Project> getItemProjects(final String slug);

    @NonNull
    public abstract Collection<Project> getItemProjects(final Long id);

    @NonNull
    @Transactional
    public T createItem(@NonNull final T input, @NonNull final Long projectId) {
        return createItem(input, getProjectById(projectId));
    }

    @NonNull
    @Transactional
    public T createItem(@NonNull final T input, @NonNull final String projectSlug) {
        return createItem(input, getProjectBySlug(projectSlug));
    }

    @NonNull
    @Transactional
    public T createItem(@NonNull final T input, @NonNull final Project project) {
        return super.createItem(input);
    }

    @NonNull
    @Transactional
    public T updateItem(@NonNull final T input, @NonNull final Project project) {
        return super.updateItem(input);
    }

    @NonNull
    @Transactional
    public T updateItem(@NonNull final T input, @NonNull final Long projectId) {
        return updateItem(input, getProjectById(projectId));
    }

    @NonNull
    @Transactional
    public T updateItem(@NonNull final T input, @NonNull final String projectSlug) {
        return updateItem(input, getProjectBySlug(projectSlug));
    }
}
