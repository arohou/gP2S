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

import com.gene.bioinfo.ms.gp2s.domain.Grid;
import com.gene.bioinfo.ms.gp2s.service.GridService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestController;
import com.gene.bioinfo.ms.gp2s.web.validator.GridValidator;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(description = "grid management", value = "grid")
@RequestMapping(value = API_V1 + "/grid", produces = APPLICATION_JSON_UTF8_VALUE)
public class GridRestController extends BaseProjectRestController<Grid, GridService> {

    @Autowired
    GridRestController(final GridService gridService,
                       final GridValidator validator) {
        super(gridService, validator);
    }

    @NonNull
    @PutMapping("/{projectSlugOrId}")
    @ResponseBody
    public Grid updateItem(@NonNull @Valid @RequestBody Grid input, @NonNull @PathVariable final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.updateItem(input, Long.parseLong(projectSlugOrId));
        } else {
            return this.service.updateItem(input, projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/findAvailableGrids/{projectSlugOrId}")
    public List<Grid> findAvailableGrids(@NonNull final @PathVariable("projectSlugOrId") String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.findAvailableGrids(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.findAvailableGrids(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/findAvailableGrids/{projectSlugOrId}/{gridId}")
    public List<Grid> findAvailableGrids(@NonNull final @PathVariable("projectSlugOrId") String projectSlugOrId,
                                         @NonNull final @PathVariable("gridId") Long gridId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.findAvailableGrids(Long.parseLong(projectSlugOrId), gridId);
        } else {
            return this.service.findAvailableGrids(projectSlugOrId, gridId);
        }
    }
}
