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

package com.gene.bioinfo.ms.gp2s.web.sample;

import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.service.sample.SampleService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestController;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(description = "Sample management", value = "sample")
@RequestMapping(value = API_V1 + "/sample", produces = APPLICATION_JSON_UTF8_VALUE)
public class SampleRestController extends BaseProjectRestController<Sample, SampleService> {

    @Autowired
    SampleRestController(final SampleService sampleService,
                         final SampleValidator validator) {
        super(sampleService, validator);
    }

    @NonNull
    @GetMapping(value = "/findSamplesAvailableForGridMaking/{projectSlugOrId}")
    public Collection<Sample> findSamplesAvailableForGridMaking(@NotNull final @PathVariable("projectSlugOrId") String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.findSamplesAvailableForGridMaking(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.findSamplesAvailableForGridMaking(projectSlugOrId);
        }
    }

    /**
     * http://www.baeldung.com/http-put-patch-difference-spring
     */
    @PatchMapping(value = "/{sampleId}")
    public ResponseEntity<?> partialUpdateAvailableForGridMaking(@NotNull final @PathVariable("sampleId") Long sampleId,
                                                                 @NotNull final @RequestBody Sample availableForGridMakingOnly) {
        if (availableForGridMakingOnly.getAvailableForGridMaking() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        service.updateAvailableForGridMaking(sampleId, availableForGridMakingOnly.getAvailableForGridMaking());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
