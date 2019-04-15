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

import com.gene.bioinfo.ms.gp2s.domain.Settings;
import com.gene.bioinfo.ms.gp2s.service.SettingsService;
import com.gene.bioinfo.ms.gp2s.web.validator.SettingsValidator;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(description = "application global settings endpoint", value = "defaultValue")
@RequestMapping(value = API_V1 + "/settings", produces = APPLICATION_JSON_UTF8_VALUE)
public class SettingsRestController {

    private final SettingsService service;
    private final SettingsValidator validator;

    @Autowired
    SettingsRestController(final @NonNull SettingsService service,
                           final @NonNull SettingsValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    @NonNull
    @GetMapping
    public Settings getSettings() {
        return this.service.getSettings();
    }

    @NonNull
    @PutMapping
    public Settings updateValues(@NonNull @Valid @RequestBody Settings input) {
        return this.service.updateSettings(input);
    }

    @InitBinder
    @SuppressWarnings("unused")
    protected void initBinder(@NonNull final WebDataBinder binder) {
        binder.setValidator(validator);
    }
}
