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

import com.gene.bioinfo.ms.gp2s.domain.MicroscopySession;
import com.gene.bioinfo.ms.gp2s.exception.responses.BaseErrorResponse;
import com.gene.bioinfo.ms.gp2s.service.MicroscopySessionService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestController;
import com.gene.bioinfo.ms.gp2s.web.validator.MicroscopySessionValidator;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Api(description = "microscopy session management", value = "microscopySession")
@RequestMapping(value = API_V1 + "/microscopy-session", produces = APPLICATION_JSON_UTF8_VALUE)
public class MicroscopySessionRestController extends BaseProjectRestController<MicroscopySession, MicroscopySessionService> {

    MicroscopySessionRestController(@NotNull final MicroscopySessionService service,
                                    @NotNull final MicroscopySessionValidator validator) {
        super(service, validator);
    }

    @NonNull
    @PutMapping("/{projectSlugOrId}")
    @ResponseBody
    public MicroscopySession updateItem(@NonNull @Valid @RequestBody final MicroscopySession input,
                                        @NonNull @PathVariable final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.updateItem(input, Long.parseLong(projectSlugOrId));
        } else {
            return this.service.updateItem(input, projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping("/microscope/{microscopeSlugOrId}")
    @ResponseBody
    public ResponseEntity<Object> getItemByMicroscope(@NonNull @PathVariable final String microscopeSlugOrId) {
        try {
            return ResponseEntity.ok().body(service.getItemByMicroscopeSlugOrId(microscopeSlugOrId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(BaseErrorResponse.builder().errorMessage(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(BaseErrorResponse.builder().errorMessage(e.getMessage()).build());
        }
    }
}
