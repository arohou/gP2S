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

import com.gene.bioinfo.ms.gp2s.domain.ModelLink;
import com.gene.bioinfo.ms.gp2s.service.ModelLinkService;
import com.gene.bioinfo.ms.gp2s.web.validator.ModelLinkListValidator;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(description = "model link management", value = "model-link")
@RequestMapping(value = API_V1 + "/model-link", produces = APPLICATION_JSON_UTF8_VALUE)
public class ModelLinkRestController {

    private final ModelLinkService modelLinkService;
    private final ModelLinkListValidator modelLinkListValidator;

    @Autowired
    ModelLinkRestController(@NonNull final ModelLinkService modelLinkService,
                            @NonNull final ModelLinkListValidator modelLinkListValidator) {
        this.modelLinkListValidator = modelLinkListValidator;
        this.modelLinkService = modelLinkService;
    }


    @NonNull
    @PostMapping("/{modelId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ModelLink> createModelLinksBatch(@NonNull @PathVariable final Long modelId,
                                             @NonNull @Valid @RequestBody List<ModelLink> inputList) {
        this.modelLinkService.deleteModelLinksByParentModel(modelId);
        this.modelLinkService.deleteModelLinksByChildModel(modelId);

        return inputList.stream().map(link -> this.modelLinkService.createModelLink(link)).collect(Collectors
                .toList());
    }

    @NonNull
    @GetMapping("/{modelId}")
    @ResponseBody
    public List<ModelLink> getModelLinks(@NonNull @PathVariable final Long modelId) {
        return this.modelLinkService.getModelLinks(modelId);
    }

    @DeleteMapping("/{modelLinkId}")
    public void deleteModelLink(@NonNull @PathVariable final Long modelLinkId) {
        this.modelLinkService.deleteModelLink(modelLinkId);
    }

    @InitBinder
    @SuppressWarnings("unused")
    protected void initBinder(@NonNull final WebDataBinder binder) {
        binder.setValidator(modelLinkListValidator);
    }
}
