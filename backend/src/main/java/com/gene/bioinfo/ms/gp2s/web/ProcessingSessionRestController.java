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

import com.gene.bioinfo.ms.gp2s.domain.ProcessingSession;
import com.gene.bioinfo.ms.gp2s.service.ProcessingSessionService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestController;
import com.gene.bioinfo.ms.gp2s.web.validator.ProcessingSessionValidator;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Api(description = "processing session management", value = "processingSession")
@RequestMapping(value = API_V1 + "/processing-session", produces = APPLICATION_JSON_UTF8_VALUE)
public class ProcessingSessionRestController extends BaseProjectRestController<ProcessingSession, ProcessingSessionService> {

    ProcessingSessionRestController(@NotNull final ProcessingSessionService service,
                                    @NotNull final ProcessingSessionValidator validator) {
        super(service, validator);
    }
}
