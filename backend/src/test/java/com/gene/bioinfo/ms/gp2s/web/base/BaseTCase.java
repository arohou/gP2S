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

package com.gene.bioinfo.ms.gp2s.web.base;

import com.gene.bioinfo.ms.gp2s.domain.base.BaseAuditableEntity;
import com.gene.bioinfo.ms.gp2s.service.base.RestService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Base unit test class.
 *
 * @author Cezary Krzyżanowski on 25.07.2017.
 */
@RunWith(MockitoJUnitRunner.class)
@Getter(AccessLevel.PROTECTED)
public abstract class BaseTCase<T extends BaseAuditableEntity> extends BaseMockMvcTCase<T> {

    protected BaseRestController<T, ? extends RestService<T>> controller;

    protected void initMockMvc(@NonNull final BaseRestController<T, ? extends RestService<T>> controller) {
        this.controller = controller;

        setMockMvc(MockMvcBuilders
                .standaloneSetup(this.controller)
                .alwaysDo(MockMvcResultHandlers.print())
                .build());
    }

}
