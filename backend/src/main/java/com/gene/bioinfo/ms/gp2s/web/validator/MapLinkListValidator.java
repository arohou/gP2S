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

import com.gene.bioinfo.ms.gp2s.domain.MapLink;
import com.gene.bioinfo.ms.gp2s.repository.MapLinkRepository;
import com.gene.bioinfo.ms.gp2s.repository.MapRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.entityExists;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotNull;

@Component
public class MapLinkListValidator implements Validator {

    private final MapLinkRepository mapLinkRepository;
    private final MapRepository mapRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public MapLinkListValidator(@NonNull final ProjectRepository projectRepository,
                                @NonNull final MapRepository mapRepository,
                                @NonNull final MapLinkRepository mapLinkRepository) {
        this.mapLinkRepository = mapLinkRepository;
        this.mapRepository = mapRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        List list = (List) target;
        for (int i = 0; i < list.size(); i++) {
            MapLink link = (MapLink) list.get(i);
            validateParentMap(link, i, errors);
            validateChildMap(link, i, errors);

            if (link.getParentMap() != null && link.getChildMap() != null) {
                if (link.getParentMap().getId() == link.getChildMap().getId()) {
                    errors.rejectValue(String.format("[%s].childMap", i), "Map can not link to itself");
                }
            }

            isNotNull(link.getRelationshipType(), String.format("[%s].relationshipType", i), "Relationship Type", errors);

            if (link.getComment() != null && link.getComment().length() > 255) {
                errors.rejectValue(String.format("[%s].comment", i), "Comment can't be longer than 255 characters");
            }
        }

    }

    private void validateChildMap(MapLink link, int idx, Errors errors) {
        isNotNull(link.getChildMap(), String.format("[%s].childMap", idx), "Child map", errors);
        if (link.getChildMap() != null) {
            isNotNull(link.getChildMap().getId(), String.format("[%s].childMap.id", idx), "Child map id", errors);
            if (link.getChildMap().getId() != null) {
                entityExists(link.getChildMap().getId(), this.mapRepository, String.format("[%s].childMap.id", idx),
                        "Child map ", errors);
            }
        }
    }

    private void validateParentMap(MapLink link, int idx, Errors errors) {
        isNotNull(link.getParentMap(), String.format("[%s].parentMap", idx), "Parent map", errors);
        if (link.getParentMap() != null) {
            isNotNull(link.getParentMap().getId(), String.format("[%s].parentMap.id", idx), "Parent map id", errors);
            if (link.getParentMap().getId() != null) {
                entityExists(link.getParentMap().getId(), this.mapRepository, String.format("[%s].parentMap.id", idx),
                        "Parent map ", errors);
            }
        }
    }

}
