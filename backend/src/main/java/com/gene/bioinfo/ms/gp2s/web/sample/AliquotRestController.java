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

import com.gene.bioinfo.ms.gp2s.domain.sample.Aliquot;
import com.gene.bioinfo.ms.gp2s.service.sample.AliquotService;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(
        description = "Fetching aliquots of ligands & proteins available in the lab for particular project",
        value = "aliquot"
)
@RequestMapping(value = API_V1 + "/project", produces = APPLICATION_JSON_UTF8_VALUE)
public class AliquotRestController {

    private AliquotService aliquotService;

    @Autowired
    AliquotRestController(AliquotService aliquotService) {
        this.aliquotService = aliquotService;
    }

    @NonNull
    @GetMapping(value = "/{projectSlugOrId}/aliquots")
    public List<Aliquot> findAliquots(@PathVariable("projectSlugOrId") String projectSlugOrId,
                                      @RequestParam("query") String queryString) {
        return this.aliquotService.find(projectSlugOrId, queryString);
    }
}
