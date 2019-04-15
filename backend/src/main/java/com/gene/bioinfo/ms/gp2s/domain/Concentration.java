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

package com.gene.bioinfo.ms.gp2s.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Przemysław Stankowski on 09.03.2018
 */
@Data
@NoArgsConstructor
@Embeddable
public class Concentration {

    @Column(name = "is_diluted_or_concentrated", nullable = false)
    private Boolean isDilutedOrConcentrated;

    @Enumerated(EnumType.STRING)
    @Column(name = "concentration_type", nullable = false)
    private ConcentrationType concentrationType;

    @Column(name = "dilution_factor")
    private Float dilutionFactor;

    @Column(name = "concentration_factor")
    private Float concentrationFactor;

    @Builder
    public Concentration(Boolean isDilutedOrConcentrated, ConcentrationType concentrationType,
                         Float dilutionFactor, Float concentrationFactor) {
        this.isDilutedOrConcentrated = isDilutedOrConcentrated;
        this.concentrationType = concentrationType;
        this.dilutionFactor = dilutionFactor;
        this.concentrationFactor = concentrationFactor;
    }

    public enum ConcentrationType {
        Dilution, Concentration
    }
}
