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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefault;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@GP2SDefaultType(DefaultValue.DefaultValueType.GRID)
@Table(name = "grid")
@Entity
public class Grid extends BaseSlugAndLabelEntity {

    @GP2SDefault
    @Column(name = "incubation_time")
    private Float incubationTime;

    @GP2SDefault
    private Float volume;

    private Concentration concentration;

    @GP2SDefault
    @Column(name = "protocol_type")
    private ProtocolType protocolType;

    @GP2SDefault
    @ManyToOne
    @JoinColumn(name = "grid_type_id", foreignKey = @ForeignKey(name = "grid_to_grid_type_fk"))
    private GridType gridType;

    @GP2SDefault
    @ManyToOne
    @JoinColumn(name = "surface_treatment_protocol_id", foreignKey = @ForeignKey(name = "grid_to_surface_treatment_protocol_fk"))
    private SurfaceTreatmentProtocol surfaceTreatmentProtocol;

    @GP2SDefault
    @ManyToOne
    @JoinColumn(name = "negative_stain_protocol_id", foreignKey = @ForeignKey(name = "grid_to_negative_stain_protocol_fk"))
    private NegativeStainProtocol negativeStainProtocol;

    @GP2SDefault
    @ManyToOne
    @JoinColumn(name = "vitrification_protocol_id", foreignKey = @ForeignKey(name = "grid_to_vitrification_protocol_fk"))
    private VitrificationProtocol vitrificationProtocol;

    @GP2SDefault
    @ManyToOne
    @JoinColumn(name = "sample_id", foreignKey = @ForeignKey(name = "grid_to_sample_fk"))
    private Sample sample;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean isAvailable;

    @Column(name = "storage_box_label_number", length = 16)
    private String storageBoxLabelNumber;

    @Column(name = "position_within_box", length = 4)
    private String positionWithinBox;

    @ManyToOne
    @JoinColumn(name = "cryo_storage_device_id", foreignKey = @ForeignKey(name = "grid_to_cryo_storage_device_fk"))
    private CryoStorageDevice cryoStorageDevice;

    @GP2SDefault
    @Column(name = "cylinder_number_label", length = 24)
    private String cylinderNumberLabel;

    @GP2SDefault
    @Column(name = "tube_number_label", length = 24)
    private String tubeNumberLabel;

    @Column(name = "box_number_label", length = 24)
    private String boxNumberLabel;

    @Builder
    public Grid(Long id, String label, String slug, Integer version, Date createdDate, Date modifiedDate,
                String createdBy, String modifiedBy, Float incubationTime, Concentration concentration,
                ProtocolType protocolType, GridType gridType, SurfaceTreatmentProtocol surfaceTreatmentProtocol,
                NegativeStainProtocol negativeStainProtocol, VitrificationProtocol vitrificationProtocol, Sample sample,
                String storageBoxLabelNumber, String positionWithinBox, CryoStorageDevice cryoStorageDevice, String cylinderNumberLabel,
                String tubeNumberLabel, String boxNumberLabel) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.incubationTime = incubationTime;
        this.concentration = concentration;
        this.protocolType = protocolType;
        this.gridType = gridType;
        this.surfaceTreatmentProtocol = surfaceTreatmentProtocol;
        this.negativeStainProtocol = negativeStainProtocol;
        this.vitrificationProtocol = vitrificationProtocol;
        this.sample = sample;
        this.storageBoxLabelNumber = storageBoxLabelNumber;
        this.positionWithinBox = positionWithinBox;
        this.cryoStorageDevice = cryoStorageDevice;
        this.cylinderNumberLabel = cylinderNumberLabel;
        this.tubeNumberLabel = tubeNumberLabel;
        this.boxNumberLabel = boxNumberLabel;


    }

    public Grid(final Grid other) {
        super(other);
        this.incubationTime = other.incubationTime;
        this.concentration = other.concentration;
        this.protocolType = other.protocolType;
        this.gridType = other.gridType;
        this.surfaceTreatmentProtocol = other.surfaceTreatmentProtocol;
        this.negativeStainProtocol = other.negativeStainProtocol;
        this.vitrificationProtocol = other.vitrificationProtocol;
        this.sample = other.sample;
        this.isAvailable = other.isAvailable;
        this.storageBoxLabelNumber = other.storageBoxLabelNumber;
        this.positionWithinBox = other.positionWithinBox;
        this.cryoStorageDevice = other.cryoStorageDevice;
        this.cylinderNumberLabel = other.cylinderNumberLabel;
        this.tubeNumberLabel = other.tubeNumberLabel;
        this.boxNumberLabel = other.boxNumberLabel;
    }

    public enum ProtocolType {
        Cryo,
        Stain
    }
}
