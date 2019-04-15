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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.CryoStorageDevice;
import com.gene.bioinfo.ms.gp2s.service.CryoStorageDeviceService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestTCase;
import com.gene.bioinfo.ms.gp2s.web.validator.CryoStorageDeviceValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.annotation.Nullable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CryoStorageDeviceRestControllerTest extends BaseRestTCase<CryoStorageDevice> {

    @Mock
    private CryoStorageDeviceService service;

    @Mock
    private CryoStorageDeviceValidator validator;

    @Before
    public void setup() {
        when(validator.supports(any())).thenReturn(true);
        initBaseRestTests(new CryoStorageDeviceRestController(service, validator), service);
    }

    @Test
    public void createCryoStorageDevice() throws Exception {
        final CryoStorageDevice device = CryoStorageDevice.builder().label("My machine").build();
        final String json = new ObjectMapper().writeValueAsString(device);

        getMockMvc().perform(post(URI).contentType(JSON_CONTENT_TYPE).content(json)).andExpect(status().isCreated());

        verify(service, times(1)).createItem(device);
    }

    @Override
    public String getUri() {
        return "/api/v1/cryo-storage-device/";
    }

    @Override
    public CryoStorageDevice createEntity(@Nullable final Long id, @Nullable final String label,
                                          @Nullable final String slug) {
        return CryoStorageDevice.builder().id(id).label(label).slug(slug)
                .manufacturer("XYZ").location("Village in the north").model("XXL")
                .hasCylinders(true).hasTubes(true).hasBoxes(true).build();
    }
}
