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

import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.service.ProjectService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import com.gene.bioinfo.ms.gp2s.web.base.BaseValidatingRestController;
import com.gene.bioinfo.ms.gp2s.web.validator.ProjectValidator;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(description = "project management", value = "project")
@RequestMapping(value = API_V1 + "/project", produces = APPLICATION_JSON_UTF8_VALUE)
public class ProjectRestController extends BaseValidatingRestController<Project, ProjectService> {

    @Autowired
    ProjectRestController(ProjectService service, ProjectValidator validator) {
        super(service, validator);
    }

    @NonNull
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createItem(@NonNull @Valid @RequestBody Project input) {
        return this.service.createItem(input);
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/protein")
    public List<Protein> getProteins(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.getProteins(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.getProteins(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/protein/count")
    public Integer countProteins(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.countProteins(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.countProteins(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/ligand")
    public List<Ligand> getLigands(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.getLigands(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.getLigands(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/ligand/count")
    public Integer countLigands(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.countLigands(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.countLigands(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/sample")
    public List<Sample> getSamples(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.getSamples(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.getSamples(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/sampleWithProjectLabels")
    public List<Sample> getSamplesWithProjectLabels(
            @NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.getSamplesWithProjectLabels(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.getSamplesWithProjectLabels(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/sample/count")
    public Integer countSamples(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.countSamples(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.countSamples(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/grid")
    public List<Grid> getGrids(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.getGrids(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.getGrids(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/grid/count")
    public Integer countGrids(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.countGrids(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.countGrids(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/microscopy-session")
    public List<MicroscopySession> getMicroscopySessions(
            @NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.getMicroscopySessions(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.getMicroscopySessions(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/microscopy-session/count")
    public Integer countMicroscopySessions(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.countMicroscopySessions(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.countMicroscopySessions(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/processing-session")
    public List<ProcessingSession> getProcessingSessions(
            @NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.getProcessingSessions(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.getProcessingSessions(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/processing-session/count")
    public Integer countProcessingSessions(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.countProcessingSessions(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.countProcessingSessions(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/map")
    public List<Map> getMaps(
            @NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.getMaps(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.getMaps(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/map/count")
    public Integer countMaps(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.countMaps(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.countMaps(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/model")
    public List<Model> getModels(
            @NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.getModels(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.getModels(projectSlugOrId);
        }
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/model/count")
    public Integer countModels(@NonNull @PathVariable("projectSlugOrId") final String projectSlugOrId) {
        if (ValidationUtils.isAnId(projectSlugOrId)) {
            return this.service.countModels(Long.parseLong(projectSlugOrId));
        } else {
            return this.service.countModels(projectSlugOrId);
        }
    }
}
