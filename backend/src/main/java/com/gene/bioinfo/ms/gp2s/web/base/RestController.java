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
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.util.Collection;

/**
 * A REST controller interface
 *
 * @author Cezary Krzyżanowski on 10.08.2017.
 */
@CrossOrigin
public interface RestController<T extends BaseAuditableEntity> {

    @NonNull
    @GetMapping
    Collection<T> getItems();

    @Null
    @GetMapping(value = "/{slug}")
    T getItem(@PathVariable("slug") final String slug);


    @DeleteMapping(value = "/{slug}")
    void deleteItem(@PathVariable("slug") final String slug);


    @NonNull
    @PutMapping
    T updateItem(@RequestBody final T input);
}
