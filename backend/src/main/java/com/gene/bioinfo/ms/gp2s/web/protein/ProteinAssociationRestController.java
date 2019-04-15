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

package com.gene.bioinfo.ms.gp2s.web.protein;

import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.service.ProteinService;
import com.gene.bioinfo.ms.gp2s.web.dto.ProjectId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(description = "Protein to project association management")
public class ProteinAssociationRestController {

    private final ProteinService proteinService;

    @Autowired
    ProteinAssociationRestController(final @NonNull ProteinService proteinService) {
        this.proteinService = proteinService;
    }

    @ApiOperation(value = "disconnect protein and project", notes = "Removes project and protein association")
    @DeleteMapping(value = API_V1 + "/protein/{proteinId}/projects/{projectId}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Collection<Project> disconnectProteinAndProject(final @NonNull @PathVariable("proteinId") Long proteinId,
                                                           final @NonNull @PathVariable("projectId") Long projectId) {
        return proteinService.disconnectProject(proteinId, projectId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "connect protein and project", notes = "Creates project and protein association. Require " +
            "'proteinId' in request body")
    @PatchMapping(value = API_V1 + "/protein/{proteinId}/projects", produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Collection<Project> connectProteinAndProject(final @NonNull @PathVariable("proteinId") Long proteinId,
                                                        final @NonNull @Valid @RequestBody ProjectId body) {

        return proteinService.connectProject(proteinId, body.getProjectId());
    }
}
