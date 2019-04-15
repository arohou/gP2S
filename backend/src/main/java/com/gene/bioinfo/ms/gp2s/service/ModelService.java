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

import com.gene.bioinfo.ms.gp2s.domain.Model;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.repository.ModelRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.service.base.BaseProjectRestService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class ModelService extends BaseProjectRestService<Model> {


    ModelService(final ModelRepository ModelRepository,
                 final ProjectRepository projectRepository) {
        super(ModelRepository, projectRepository);
    }

    @Override
    public Collection<Project> getItemProjects(@NonNull final String slug) {
        return Optional.ofNullable(this.projectRepository.findByModel_Slug(slug))
                .orElse(Collections.emptySet());
    }

    @Override
    public Collection<Project> getItemProjects(@NonNull final Long id) {
        return Optional.ofNullable(this.projectRepository.findByModel_Id(id))
                .orElse(Collections.emptySet());
    }
}
