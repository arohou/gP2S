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

import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.service.ProteinService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestController;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(description = "Protein aliquot management", value = "protein")
@RequestMapping(value = API_V1 + "/protein", produces = APPLICATION_JSON_UTF8_VALUE)
public class ProteinRestController extends BaseProjectRestController<Protein, ProteinService> {

    @Autowired
    public ProteinRestController(ProteinService proteinService,
                          ProteinValidator[] validators) {
        super(proteinService, validators);
    }

    @NonNull
    @GetMapping("/findRecentAvailableProtein/{purId:.+}")
    Protein findRecentAvailableProtein(@PathVariable("purId") final String purId) {
        return service.findRecentAvailableProtein(purId);
    }
}
