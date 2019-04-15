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

package com.gene.bioinfo.ms.gp2s.web.validator;

import com.gene.bioinfo.ms.gp2s.domain.ModelLink;
import com.gene.bioinfo.ms.gp2s.repository.ModelLinkRepository;
import com.gene.bioinfo.ms.gp2s.repository.ModelRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.entityExists;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotNull;

@Component
public class ModelLinkListValidator implements Validator {

    private final ModelLinkRepository modelLinkRepository;
    private final ModelRepository modelRepository;

    @Autowired
    public ModelLinkListValidator(@NonNull final ModelLinkRepository modelLinkRepository,
                                  @NonNull final ModelRepository modelRepository) {
        this.modelLinkRepository = modelLinkRepository;
        this.modelRepository = modelRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        List list = (List) target;
        for (int i = 0; i < list.size(); i++) {
            ModelLink link = (ModelLink) list.get(i);
            validateParentModel(link, i, errors);
            validateChildModel(link, i, errors);

            if (link.getParentModel() != null && link.getChildModel() != null) {
                if (link.getParentModel().getId() == link.getChildModel().getId()) {
                    errors.rejectValue(String.format("[%s].childModel", i), "Model can not link to itself");
                }
            }

            isNotNull(link.getRelationshipType(), String.format("[%s].relationshipType", i), "Relationship Type", errors);

            if (link.getComment() != null && link.getComment().length() > 255) {
                errors.rejectValue(String.format("[%s].comment", i), "Comment can't be longer than 255 characters");
            }
        }
    }

    private void validateChildModel(ModelLink link, int idx, Errors errors) {
        isNotNull(link.getChildModel(), String.format("[%s].childModel", idx), "Child model", errors);
        if (link.getChildModel() != null) {
            isNotNull(link.getChildModel().getId(), String.format("[%s].childModel.id", idx), "Child model id", errors);
            if (link.getChildModel().getId() != null) {
                entityExists(link.getChildModel().getId(), this.modelRepository, String.format("[%s].childModel.id", idx),
                        "Child model ", errors);
            }
        }
    }

    private void validateParentModel(ModelLink link, int idx, Errors errors) {
        isNotNull(link.getParentModel(), String.format("[%s].parentModel", idx), "Parent model", errors);
        if (link.getParentModel() != null) {
            isNotNull(link.getParentModel().getId(), String.format("[%s].parentModel.id", idx), "Parent model id", errors);
            if (link.getParentModel().getId() != null) {
                entityExists(link.getParentModel().getId(), this.modelRepository, String.format("[%s].parentModel.id", idx),
                        "Parent model ", errors);
            }
        }
    }

}
