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

import com.gene.bioinfo.ms.gp2s.domain.BlottingPaper;
import com.gene.bioinfo.ms.gp2s.repository.BlottingPaperRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.IOException;

public class BlottingPaperRestControllerITCase extends BaseRestITCase<BlottingPaper> {

    @Autowired
    private BlottingPaperRepository repository;

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(repository);
    }

    @Override
    public String getUri() {
        return "/api/v1/blotting-paper/";
    }

    @Override
    public BlottingPaper createEntity(@Nullable final Long id,
                                      @Nullable final String label,
                                      @Nullable final String slug) {
        return BlottingPaper.builder().id(id).label(label).slug(slug)
                .manufacturer("A manufacturer")
                .model("The new model")
                .build();
    }


    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(BlottingPaper.builder().label(label)
                .manufacturer("A manufacturer")
                .model("The new model")
                .build());
    }
}
