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
import com.gene.bioinfo.ms.gp2s.repository.MicroscopeRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import javax.annotation.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MicroscopeRestControllerITCase extends BaseRestITCase<Microscope> {
    @Autowired
    private MicroscopeRepository repository;

    public static Microscope createMicroscope(Microscope.MicroscopeBuilder builder) {
        Set<Float> availableVoltagesKV = new HashSet<>();
        availableVoltagesKV.add(400f);
        return builder.manufacturer("CRL").model("My model").location("my location").defaultExtractionVoltageKV(1.5f)
                .defaultGunLensSetting(5).defaultVoltageKV(400f).condenser1ApertureDiameter(1)
                .condenser2ApertureDiameter(2).condenser3ApertureDiameter(3).condenser4ApertureDiameter(4)
                .defaultCondenserApertureIndex(2)
                .defaultObjectiveApertureIndex(2).sampleInsertionMechanism(Microscope.InsertionMechanismType.AUTO_LOADER)
                .energyFilter(true).defaultEnergyFilterSlitWidth(5f).defaultSpotSize(5)
                .availableVoltagesKV(availableVoltagesKV)
                .build();
    }

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(repository);
    }

    @Override
    public String getUri() {
        return "/api/v1/microscope/";
    }

    @Override
    public Microscope createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return createMicroscope(Microscope.builder().id(id).label(label).slug(slug));
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(createMicroscope(Microscope.builder().id(null).slug(null).label(label)));
    }

    @Test
    public void shouldProvidingNoObjectiveAperturesBeOk() throws Exception {
        final Microscope microscope = createMicroscope(Microscope.builder().label("test"));
        microscope.setObjectiveAperture1(null);
        microscope.setObjectiveAperture2(null);
        microscope.setObjectiveAperture3(null);
        microscope.setObjectiveAperture4(null);

        getMockMvc()
                .perform(post(URI + '/').contentType(JSON_CONTENT_TYPE).content(json(microscope)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(JSON_CONTENT_TYPE));
    }

    @Test
    public void shouldProvidingNoC2AperturesBeOk() throws Exception {
        final Microscope microscope = createMicroscope(Microscope.builder().label("test"));
        microscope.setCondenser1ApertureDiameter(null);
        microscope.setCondenser2ApertureDiameter(null);
        microscope.setCondenser3ApertureDiameter(null);
        microscope.setCondenser4ApertureDiameter(null);


        getMockMvc()
                .perform(post(URI + '/').contentType(JSON_CONTENT_TYPE).content(json(microscope)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(JSON_CONTENT_TYPE));
    }
}
