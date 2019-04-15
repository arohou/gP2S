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

import com.gene.bioinfo.ms.gp2s.domain.ElectronDetector;
import com.gene.bioinfo.ms.gp2s.domain.Microscope;
import com.gene.bioinfo.ms.gp2s.service.MicroscopeService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import com.gene.bioinfo.ms.gp2s.web.base.BaseValidatingRestController;
import com.gene.bioinfo.ms.gp2s.web.validator.MicroscopeValidator;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Api(description = "microscope management", value = "microscope")
@RequestMapping(value = API_V1 + "/microscope", produces = APPLICATION_JSON_UTF8_VALUE)
public class MicroscopeRestController extends BaseValidatingRestController<Microscope, MicroscopeService> {

    MicroscopeRestController(@NotNull final MicroscopeService service,
                             @NotNull final MicroscopeValidator validator) {
        super(service, validator);
    }

    @NonNull
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Microscope createItem(@NonNull @Valid @RequestBody Microscope microscope) {
        return this.service.createItem(microscope);
    }

    @NonNull
    @GetMapping("/{slugOrId}/electron-detector")
    public Collection<ElectronDetector> getElectronDetectorsForMicroscope(
            @NonNull @PathVariable("slugOrId") final String slugOrId) {
        if (ValidationUtils.isAnId(slugOrId)) {
            return this.service.getElectronDetectors(Long.parseLong(slugOrId));
        } else {
            return this.service.getElectronDetectors(slugOrId);
        }
    }
}
