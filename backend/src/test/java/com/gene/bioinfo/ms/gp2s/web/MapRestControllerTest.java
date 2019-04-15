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

import com.gene.bioinfo.ms.gp2s.domain.Map;
import com.gene.bioinfo.ms.gp2s.service.MapService;
import com.gene.bioinfo.ms.gp2s.web.attachment.ISingleAttachmentRestController;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestTCase;
import com.gene.bioinfo.ms.gp2s.web.validator.MapValidator;
import org.junit.Before;
import org.mockito.Mock;

import javax.annotation.Nullable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MapRestControllerTest extends BaseProjectRestTCase<Map> {
    @Mock
    private MapService service;

    @Mock
    private MapValidator validator;

    @Mock
    private ISingleAttachmentRestController attachmentController;

    @Override
    public String getUri() {
        return "/api/v1/map/";
    }

    @Before
    public void setup() throws Exception {
        when(validator.supports(any())).thenReturn(true);
        initProjectBaseRestTests(new MapRestController(attachmentController, service, validator), service);
    }

    @Override
    public Map createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return Map.builder().id(id).label(label).slug(slug).build();
    }
}
