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

import com.gene.bioinfo.ms.gp2s.domain.VitrificationMachine;
import com.gene.bioinfo.ms.gp2s.repository.VitrificationMachineRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import javax.annotation.Nullable;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class VitrificationMachineRestControllerITCase extends BaseRestITCase<VitrificationMachine> {

    @Autowired
    private VitrificationMachineRepository repository;

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(repository);
    }

    @Override
    public String getUri() {
        return "/api/v1/vitrification-machine/";
    }

    @Override
    public VitrificationMachine createEntity(@Nullable final Long id,
                                             @Nullable final String label,
                                             @Nullable final String slug) {
        return VitrificationMachine.builder().id(id).label(label).slug(slug)
                .manufacturer("A manufacturer")
                .model("The new model")
                .location("In the basement")
                .build();
    }


    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(VitrificationMachine.builder().label(label)
                .manufacturer("A manufacturer")
                .model("The new model")
                .location("In the basement")
                .build());
    }
}
