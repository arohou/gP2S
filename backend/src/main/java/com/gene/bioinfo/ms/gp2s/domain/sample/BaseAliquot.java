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

package com.gene.bioinfo.ms.gp2s.domain.sample;

import com.gene.bioinfo.ms.gp2s.domain.Ligand;
import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Common values between aliquots.
 *
 * @author Cezary Krzyżanowski on 07.08.2017.
 * @see Protein
 * @see Ligand
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@MappedSuperclass
public abstract class BaseAliquot extends BaseSlugAndLabelEntity {

    @Column(name = "tube_label", length = DomainConstants.SHORT_STRING_LENGTH)
    private String tubeLabel;

    @Column(name = "available_for_sample_making")
    private Boolean availableForSampleMaking;

    public BaseAliquot(Long id, String label, String slug, Integer version, Date createdDate, Date modifiedDate,
                       String createdBy, String modifiedBy, String tubeLabel, Boolean availableForSampleMaking) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.tubeLabel = tubeLabel;
        this.availableForSampleMaking = availableForSampleMaking;
    }

    public BaseAliquot(final BaseAliquot other) {
        super(other);
        this.tubeLabel = other.tubeLabel;
        this.availableForSampleMaking = other.availableForSampleMaking;
    }
}
