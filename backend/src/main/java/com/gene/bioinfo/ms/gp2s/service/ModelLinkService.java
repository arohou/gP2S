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

import com.gene.bioinfo.ms.gp2s.domain.ModelLink;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.ModelLinkRepository;
import com.gene.bioinfo.ms.gp2s.repository.ModelRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelLinkService {

    private final ModelLinkRepository modelLinkRepository;
    private final ModelRepository modelRepository;

    @Autowired
    ModelLinkService(@NonNull final ModelLinkRepository modelLinkRepository,
                     @NonNull final ModelRepository modelRepository) {
        this.modelLinkRepository = modelLinkRepository;
        this.modelRepository = modelRepository;
    }

    public List<ModelLink> getModelLinks(@NonNull final Long modelId) {
        if (!this.modelRepository.exists(modelId)) {
            throw new ResourceNotFoundException("Model with given id doesn't exist: " + modelId);
        }

        return this.modelLinkRepository.getAllRelationsForModel(modelId);
    }

    public void deleteModelLink(@NonNull final Long modelLinkId) {
        Optional.ofNullable(this.modelLinkRepository.findOne(modelLinkId))
                .orElseThrow(() -> new ResourceNotFoundException("ModelLink not found for id: " + modelLinkId));
        this.modelLinkRepository.delete(modelLinkId);
    }

    public ModelLink createModelLink(@NonNull final ModelLink input) {
        final ModelLink link = ModelLink.builder()
                .parentModel(this.modelRepository.findOne(input.getParentModel().getId()))
                .childModel(this.modelRepository.findOne(input.getChildModel().getId()))
                .comment(input.getComment())
                .relationshipType(input.getRelationshipType())
                .build();
        return this.modelLinkRepository.save(link);

    }

    public void deleteModelLinksByParentModel(Long modelId) {
        this.modelLinkRepository.deleteByParentModel_Id(modelId);
    }

    public void deleteModelLinksByChildModel(Long modelId) {
        this.modelLinkRepository.deleteByChildModel_Id(modelId);
    }

}
