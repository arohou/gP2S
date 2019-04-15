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

import com.gene.bioinfo.ms.gp2s.domain.VitrificationProtocol;
import com.gene.bioinfo.ms.gp2s.service.VitrificationProtocolService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseValidatingRestController;
import com.gene.bioinfo.ms.gp2s.web.validator.VitrificationProtocolValidator;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Api(description = "vitrification protocol management", value = "vitrificationProtocol")
@RequestMapping(value = API_V1 + "/vitrification-protocol", produces = APPLICATION_JSON_UTF8_VALUE)
public class VitrificationProtocolRestController extends BaseValidatingRestController<VitrificationProtocol, VitrificationProtocolService> {

    @Autowired
    VitrificationProtocolRestController(final VitrificationProtocolService service,
                                        final VitrificationProtocolValidator validator) {
        super(service, validator);
    }

    @NonNull
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VitrificationProtocol createItem(@NonNull @Valid @RequestBody VitrificationProtocol input) {
        return this.service.createItem(input);
    }
}
