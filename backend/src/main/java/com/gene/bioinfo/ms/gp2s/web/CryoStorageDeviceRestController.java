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

import com.gene.bioinfo.ms.gp2s.domain.CryoStorageDevice;
import com.gene.bioinfo.ms.gp2s.service.CryoStorageDeviceService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseValidatingRestController;
import com.gene.bioinfo.ms.gp2s.web.validator.CryoStorageDeviceValidator;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Api(description = "Cryo storage device management", value = "cryoStorageDevice")
@RequestMapping(value = API_V1 + "/cryo-storage-device", produces = APPLICATION_JSON_UTF8_VALUE)
public class CryoStorageDeviceRestController extends BaseValidatingRestController<CryoStorageDevice, CryoStorageDeviceService> {

    @Autowired
    public CryoStorageDeviceRestController(@NotNull final CryoStorageDeviceService service,
                                           @NotNull final CryoStorageDeviceValidator validator) {
        super(service, validator);
    }

    @NonNull
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CryoStorageDevice createItem(@NotNull @Valid @RequestBody final CryoStorageDevice device) {
        return this.service.createItem(device);
    }
}
