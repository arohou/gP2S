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

import com.gene.bioinfo.ms.gp2s.domain.base.BaseAuditableEntity;
import com.gene.bioinfo.ms.gp2s.service.base.RestService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.Collection;

/**
 * Base class for REST controllers
 *
 * @author Cezary Krzyżanowski on 10.08.2017.
 */
public abstract class BaseRestController<T extends BaseAuditableEntity, Service extends RestService<T>>
        implements RestController<T> {

    protected final Service service;

    protected BaseRestController(@NonNull final Service service) {
        this.service = service;
    }

    @NonNull
    @GetMapping
    public Collection<T> getItems() {
        return this.service.getItems();
    }

    @Null
    @GetMapping(value = "/{slugOrId}")
    public T getItem(@NonNull @PathVariable("slugOrId") final String slugOrId) {
        if (ValidationUtils.isAnId(slugOrId)) {
            return this.service.getItem(Long.parseLong(slugOrId));
        } else {
            return this.service.getItem(slugOrId);
        }
    }


    @DeleteMapping(value = "/{slugOrId}")
    public void deleteItem(@NonNull @PathVariable("slugOrId") final String slugOrId) {
        if (ValidationUtils.isAnId(slugOrId)) {
            this.service.deleteItem(Long.parseLong(slugOrId));
        } else {
            this.service.deleteItem(slugOrId);
        }
    }

    @NonNull
    @PutMapping
    public T updateItem(@NonNull @Valid @RequestBody final T input) {
        return this.service.updateItem(input);
    }
}
