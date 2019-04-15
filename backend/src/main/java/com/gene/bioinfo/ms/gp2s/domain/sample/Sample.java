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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants.LONG_TEXT_LENGTH;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "sample")
@Entity
public class Sample extends BaseSlugAndLabelEntity {

    @Column(name = "incubation_time")
    private Float incubationTime;

    @Column(name = "incubation_temperature")
    private Float incubationTemperature;

    @Column(name = "other_buffer_components")
    private String otherBufferComponents;

    @Column(name = "available_for_grid_making", nullable = false)
    private Boolean availableForGridMaking;

    @Column(name = "protocol_description", length = LONG_TEXT_LENGTH)
    private String protocolDescription;

    @OrderBy("id DESC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sample_id", foreignKey = @ForeignKey(name = "ligand_component_to_sample_fk"))
    private List<LigandComponent> ligandComponent;

    @OrderBy("id DESC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sample_id", foreignKey = @ForeignKey(name = "protein_component_to_sample_fk"))
    private List<ProteinComponent> proteinComponent;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String projectLabels;

    @Builder
    public Sample(Long id, String label, String slug, Integer version, Date createdDate, Date modifiedDate,
                  String createdBy, String modifiedBy, Float incubationTime, Float incubationTemperature,
                  String otherBufferComponents, Boolean availableForGridMaking,
                  List<LigandComponent> ligandComponent, List<ProteinComponent> proteinComponent) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.incubationTime = incubationTime;
        this.incubationTemperature = incubationTemperature;
        this.otherBufferComponents = otherBufferComponents;
        this.availableForGridMaking = getAvailableForGridMaking(availableForGridMaking);
        this.ligandComponent = ligandComponent;
        this.proteinComponent = proteinComponent;
    }

    public Sample(final Sample other) {
        super(other);
        this.incubationTime = other.incubationTime;
        this.incubationTemperature = other.incubationTemperature;
        this.otherBufferComponents = other.otherBufferComponents;
        this.availableForGridMaking = getAvailableForGridMaking(other.availableForGridMaking);
        this.projectLabels = other.projectLabels;
        this.ligandComponent = other.ligandComponent;
        this.proteinComponent = other.proteinComponent;
    }

    @NonNull
    private Boolean getAvailableForGridMaking(Boolean availableForGridMaking) {
        return availableForGridMaking == null ? Boolean.TRUE : availableForGridMaking;
    }

}
