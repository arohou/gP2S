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
import com.gene.bioinfo.ms.gp2s.domain.VitrificationMachine;
import com.gene.bioinfo.ms.gp2s.domain.VitrificationProtocol;
import com.gene.bioinfo.ms.gp2s.repository.BlottingPaperRepository;
import com.gene.bioinfo.ms.gp2s.repository.VitrificationMachineRepository;
import com.gene.bioinfo.ms.gp2s.repository.VitrificationProtocolRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import lombok.NonNull;
import javax.annotation.Nullable;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class VitrificationProtocolRestControllerITCase extends BaseRestITCase<VitrificationProtocol> {

    @Autowired
    private VitrificationMachineRepository vitrificationMachineRepository;
    @Autowired
    private BlottingPaperRepository blottingPaperRepository;
    private VitrificationMachine vitrificationMachine;
    private BlottingPaper blottingPaper;
    @Autowired
    private VitrificationProtocolRepository repository;

    @Override
    public void prepareEntities() {
        vitrificationMachine = vitrificationMachineRepository.save(VitrificationMachine.builder()
                .slug("machine-1")
                .label("Machine 1")
                .build());
        blottingPaper = blottingPaperRepository.save(BlottingPaper.builder()
                .slug("blotting-paper-1")
                .label("Blotting paper 1")
                .build());

        super.prepareEntities();
    }

    @Override
    public void cleanupRepository() {
        super.cleanupRepository();
        blottingPaperRepository.deleteAll();
        vitrificationMachineRepository.deleteAll();
    }

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(repository);
    }

    @After
    public void tearDown() {
        repository.deleteAll();
        blottingPaperRepository.deleteAll();
        vitrificationMachineRepository.deleteAll();
    }

    @Override
    public String getUri() {
        return "/api/v1/vitrification-protocol/";
    }

    @Override
    public VitrificationProtocol createEntity(@Nullable final Long id,
                                              @Nullable final String label,
                                              @Nullable final String slug) {
        return VitrificationProtocol.builder().id(id).label(label).slug(slug)
                .relativeHumidity(10)
                .temperature(10f)
                .blotTime(10f)
                .waitTime(10f)
                .drainTime(10f)
                .blotForce(10)
                .description("Lorem Ipsum")
                .numberOfBlots(10)
                .numberOfSampleApplications(10)
                .vitrificationMachine(vitrificationMachine)
                .blottingPaper(blottingPaper)
                .build();
    }


    @Override
    protected String createRequestBody(@NonNull final String label) throws IOException {
        return json(VitrificationProtocol.builder().label(label)
                .relativeHumidity(10)
                .temperature(10f)
                .blotTime(10f)
                .waitTime(10f)
                .drainTime(10f)
                .blotForce(10)
                .description("Lorem Ipsum")
                .numberOfBlots(10)
                .numberOfSampleApplications(10)
                .vitrificationMachine(vitrificationMachine)
                .blottingPaper(blottingPaper)
                .build());
    }
}
