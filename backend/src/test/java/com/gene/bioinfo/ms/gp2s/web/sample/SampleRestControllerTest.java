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

import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.service.sample.SampleService;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestTCase;

import javax.annotation.Nullable;
import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class SampleRestControllerTest extends BaseProjectRestTCase<Sample> {

    @Mock
    private SampleService service;

    @Mock
    private SampleValidator validator;

    @Before
    public void setUp() throws Exception {
        when(validator.supports(any())).thenReturn(true);
        initProjectBaseRestTests(new SampleRestController(service, validator), service);
    }

    @Override
    public String getUri() {
        return "/api/v1/sample/";
    }

    @Override
    public Sample createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return Sample.builder().id(id).label(label).slug(slug).build();
    }

}
