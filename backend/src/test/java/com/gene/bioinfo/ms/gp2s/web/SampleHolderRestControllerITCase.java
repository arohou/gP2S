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

import com.gene.bioinfo.ms.gp2s.domain.Microscope;
import com.gene.bioinfo.ms.gp2s.domain.SampleHolder;
import com.gene.bioinfo.ms.gp2s.repository.MicroscopeRepository;
import com.gene.bioinfo.ms.gp2s.repository.SampleHolderRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import javax.annotation.Nullable;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SampleHolderRestControllerITCase extends BaseRestITCase<SampleHolder> {

    @Autowired
    MicroscopeRepository microscopeRepository;
    @Autowired
    private SampleHolderRepository repository;
    private List<Microscope> microscopes;

    @Override
    public void prepareEntities() {
        Microscope microscope = microscopeRepository.save(MicroscopeRestControllerITCase.createMicroscope(Microscope.builder()
                .slug("microscope-1")
                .label("Microscope 1")));
        microscopes = new ArrayList<>();
        microscopes.add(microscope);

        super.prepareEntities();
    }

    @Override
    public void cleanupRepository() {
        super.cleanupRepository();
        microscopeRepository.deleteAll();
    }

    @After
    public void tearDown() {
        repository.deleteAll();
        microscopeRepository.deleteAll();
    }

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(repository);
    }

    @Override
    public String getUri() {
        return "/api/v1/sample-holder/";
    }

    @Override
    public SampleHolder createEntity(@Nullable final Long id,
                                     @Nullable final String label,
                                     @Nullable final String slug) {

        SampleHolder sampleHolder = SampleHolder.builder().id(id).label(label).slug(slug)
                .label("A label")
                .manufacturer("A manufacturer")
                .model("A model")
                .location("New York")
                .cryoCapable(true)
                .maximumTilt(5f)
                .doubleTilt(false)
                .microscopes(this.microscopes)
                .build();

        return sampleHolder;
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(SampleHolder.builder().label(label)
                .manufacturer("A manufacturer")
                .model("A model")
                .location("New York")
                .cryoCapable(true)
                .maximumTilt(5f)
                .doubleTilt(false)
                .microscopes(this.microscopes)
                .build());
    }
}
