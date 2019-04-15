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

package com.gene.bioinfo.ms.gp2s.web;

import com.gene.bioinfo.ms.gp2s.domain.DefaultValue;
import com.gene.bioinfo.ms.gp2s.service.DefaultValueService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import com.netflix.config.validation.ValidationException;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(description = "default value endpoint", value = "defaultValue")
@RequestMapping(value = API_V1 + "/default-value", produces = APPLICATION_JSON_UTF8_VALUE)
public class DefaultValueRestController {

    private final DefaultValueService service;

    @Autowired
    DefaultValueRestController(final @NonNull DefaultValueService service){
        this.service = service;
    }

    @NonNull
    @GetMapping(value = "/{type}/{project}")
    public Map<String, Object> getDefaultValues(@NonNull @PathVariable("type") String entityType,
                                                               @NonNull @PathVariable("project") String project) {
        if(EnumUtils.isValidEnum(DefaultValue.DefaultValueType.class, entityType.toUpperCase())){
            if (ValidationUtils.isAnId(project)) {
                return this.service.getValues(DefaultValue.DefaultValueType.valueOf(entityType.toUpperCase()), Long.parseLong(project));
            } else {
                return this.service.getValues(DefaultValue.DefaultValueType.valueOf(entityType.toUpperCase()), project);
            }
        }
        throw new ValidationException(String.format("Invalid entity type: '%s'", entityType));
    }
}
