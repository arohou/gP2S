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

package com.gene.bioinfo.ms.gp2s.web.base;

import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.service.base.BaseProjectRestService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Base class for REST controllers
 *
 * @author Cezary Krzyżanowski on 10.08.2017.
 */
public abstract class BaseProjectRestController<T extends BaseSlugAndLabelEntity, Service extends BaseProjectRestService<T>>
        extends BaseValidatingRestController<T, Service> {

    protected BaseProjectRestController(final Service service,
                                        final Validator... validators) {
        super(service, validators);
    }

    @NonNull
    @GetMapping(value = "/{slugOrId}/projects")
    public Collection<Project> getItemProjects(@NonNull @PathVariable("slugOrId") final String slugOrId) {
        if (ValidationUtils.isAnId(slugOrId)) {
            return this.service.getItemProjects(Long.parseLong(slugOrId));
        } else {
            return this.service.getItemProjects(slugOrId);
        }
    }

    @NonNull
    @PostMapping("/{projectSlugOrId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public T createItem(@NonNull @Valid @RequestBody T input, @NonNull @PathVariable final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.createItem(input, Long.parseLong(projectSlugOrId));
        } else {
            return this.service.createItem(input, projectSlugOrId);
        }
    }

    @NonNull
    @PutMapping("/{projectSlugOrId}")
    @ResponseBody
    public T updateItem(@NonNull @Valid @RequestBody final T input,
                        @NonNull @PathVariable final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.updateItem(input, Long.parseLong(projectSlugOrId));
        } else {
            return this.service.updateItem(input, projectSlugOrId);
        }
    }
}
