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

import com.gene.bioinfo.ms.gp2s.domain.SurfaceTreatmentMachine;
import com.gene.bioinfo.ms.gp2s.domain.SurfaceTreatmentProtocol;
import com.gene.bioinfo.ms.gp2s.repository.SurfaceTreatmentMachineRepository;
import com.gene.bioinfo.ms.gp2s.repository.SurfaceTreatmentProtocolRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import javax.annotation.Nullable;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class SurfaceTreatmentProtocolRestControllerITCase extends BaseRestITCase<SurfaceTreatmentProtocol> {

    @Autowired
    private SurfaceTreatmentMachineRepository repository;
    private SurfaceTreatmentMachine machine;
    @Autowired
    private SurfaceTreatmentProtocolRepository surfaceTreatmentProtocolRepository;

    @Override
    public String getUri() {
        return "/api/v1/surface-treatment-protocol/";
    }

    @Override
    public void prepareEntities() {
        machine = repository.save(SurfaceTreatmentMachine.builder()
                .slug("machine-1")
                .label("machine 1")
                .build());
        super.prepareEntities();
    }

    @Override
    public SurfaceTreatmentProtocol createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {

        return SurfaceTreatmentProtocol.builder().id(id).label(label).slug(slug).duration(1f).current(1f).pressure(1f)
                .machine(machine).build();
    }

    @Override
    public void cleanupRepository() {
        super.cleanupRepository();
        repository.deleteAll();
    }

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(surfaceTreatmentProtocolRepository);
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(SurfaceTreatmentProtocol.builder().label(label).duration(1f).current(1f).pressure(1f)
                .machine(machine).build());
    }
}
