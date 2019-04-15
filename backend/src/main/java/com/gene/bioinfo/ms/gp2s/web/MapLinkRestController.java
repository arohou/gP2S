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

import com.gene.bioinfo.ms.gp2s.domain.MapLink;
import com.gene.bioinfo.ms.gp2s.service.MapLinkService;
import com.gene.bioinfo.ms.gp2s.util.ValidationUtils;
import com.gene.bioinfo.ms.gp2s.web.validator.MapLinkListValidator;
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
@Api(description = "map link management", value = "map-link")
@RequestMapping(value = API_V1 + "/map-link", produces = APPLICATION_JSON_UTF8_VALUE)
public class MapLinkRestController {

    private final MapLinkService mapLinkService;
    private final MapLinkListValidator mapLinkListValidator;

    @Autowired
    MapLinkRestController(@NonNull final MapLinkService mapLinkService,
                          @NonNull final MapLinkListValidator mapLinkListValidator) {
        this.mapLinkListValidator = mapLinkListValidator;
        this.mapLinkService = mapLinkService;
    }


    @NonNull
    @PostMapping("/{mapId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<MapLink> createMapLinksBatch(@NonNull @PathVariable final Long mapId,
                                             @NonNull @Valid @RequestBody List<MapLink> inputList) {
        this.mapLinkService.deleteMapLinksByParentMap(mapId);
        this.mapLinkService.deleteMapLinksByChildMap(mapId);

        return inputList.stream().map(link -> this.mapLinkService.createMapLink(link)).collect(Collectors
                .toList());
    }

    @NonNull
    @GetMapping("/{mapId}")
    @ResponseBody
    public List<MapLink> getMapLinks(@NonNull @PathVariable final Long mapId) {
        return this.mapLinkService.getMapLinks(mapId);
    }

    @DeleteMapping("/{mapLinkId}")
    public void deleteMapLink(@NonNull @PathVariable final Long mapLinkId) {
        this.mapLinkService.deleteMapLink(mapLinkId);
    }

    @InitBinder
    @SuppressWarnings("unused")
    protected void initBinder(@NonNull final WebDataBinder binder) {
        binder.setValidator(mapLinkListValidator);
    }
}
