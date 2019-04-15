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

import com.gene.bioinfo.ms.gp2s.service.AdminEntitiesCounterService;
import com.gene.bioinfo.ms.gp2s.web.dto.AdminPanelCounters;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@CrossOrigin
@RestController
@Api(description = "Admin panel statistics", value = "adminPanel")
@RequestMapping(value = API_V1 + "/admin", produces = APPLICATION_JSON_UTF8_VALUE)
public class AdminEntitiesCounterRestController {

    @Autowired
    private AdminEntitiesCounterService service;

    @NonNull
    @GetMapping(value = "/counters")
    public AdminPanelCounters counters() {
        return this.service.calculateAdminCounters();
    }
}
