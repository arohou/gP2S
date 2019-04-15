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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "microscope")
@Entity
public class Microscope extends BaseSlugAndLabelEntity {
    @Column(length = DomainConstants.SHORT_STRING_LENGTH, nullable = false)
    private String manufacturer;
    @Column(length = DomainConstants.SHORT_STRING_LENGTH, nullable = false)
    private String model;
    @Column(length = DomainConstants.SHORT_STRING_LENGTH, nullable = false)
    private String location;
    @Column(name = "default_extraction_voltage_kv")
    private Float defaultExtractionVoltageKV; // Unit: kV
    @Column(name = "default_gun_lens_setting")
    private Integer defaultGunLensSetting;
    @ElementCollection
    @CollectionTable(name = "microscope_voltage", joinColumns = @JoinColumn(name = "microscope_id"))
    @Column(name = "voltage_kv", nullable = false)
    private Set<Float> availableVoltagesKV;
    @Column(name = "default_voltage_kv")
    private Float defaultVoltageKV;
    @Column(name = "condenser_1_aperture_diameter")
    private Integer condenser1ApertureDiameter;
    @Column(name = "condenser_2_aperture_diameter")
    private Integer condenser2ApertureDiameter;
    @Column(name = "condenser_3_aperture_diameter")
    private Integer condenser3ApertureDiameter;
    @Column(name = "condenser_4_aperture_diameter")
    private Integer condenser4ApertureDiameter;
    @Column(name = "default_condenser_aperture_index")
    private Integer defaultCondenserApertureIndex; // 1 or 2 or 3 or 4
    @AttributeOverrides({
            @AttributeOverride(name = "phasePlate", column = @Column(name = "objective_1_aperture_phase_plate")),
            @AttributeOverride(name = "diameter", column = @Column(name = "objective_1_aperture_diameter"))
    })
    private ObjectiveAperture objectiveAperture1;
    @AttributeOverrides({
            @AttributeOverride(name = "phasePlate", column = @Column(name = "objective_2_aperture_phase_plate")),
            @AttributeOverride(name = "diameter", column = @Column(name = "objective_2_aperture_diameter"))
    })
    private ObjectiveAperture objectiveAperture2;
    @AttributeOverrides({
            @AttributeOverride(name = "phasePlate", column = @Column(name = "objective_3_aperture_phase_plate")),
            @AttributeOverride(name = "diameter", column = @Column(name = "objective_3_aperture_diameter"))
    })
    private ObjectiveAperture objectiveAperture3;
    @AttributeOverrides({
            @AttributeOverride(name = "phasePlate", column = @Column(name = "objective_4_aperture_phase_plate")),
            @AttributeOverride(name = "diameter", column = @Column(name = "objective_4_aperture_diameter"))
    })
    private ObjectiveAperture objectiveAperture4;
    @Column(name = "default_objective_aperture_index")
    private Integer defaultObjectiveApertureIndex; // 1 or 2 or 3 or 4
    @Column(name = "sample_insertion_mechanism")
    @Enumerated(EnumType.STRING)
    private InsertionMechanismType sampleInsertionMechanism;
    @Column(name = "energy_filter")
    private Boolean energyFilter;
    @Column(name = "default_energy_filter_slit_width")
    private Float defaultEnergyFilterSlitWidth; // Unit: eV
    @Column(name = "default_spot_size", nullable = false)
    private Integer defaultSpotSize;
    @OrderBy("id DESC")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "microscopes")
    @JsonIgnore
    private List<SampleHolder> sampleHolders;

    @Builder
    public Microscope(Long id, String label, String slug, Integer version, Date createdDate, Date modifiedDate,
                      String createdBy, String modifiedBy, String manufacturer, String model, String location,
                      Float defaultExtractionVoltageKV, Integer defaultGunLensSetting, Set<Float> availableVoltagesKV,
                      Float defaultVoltageKV, Integer condenser1ApertureDiameter, Integer condenser2ApertureDiameter,
                      Integer condenser3ApertureDiameter, Integer condenser4ApertureDiameter,
                      Integer defaultCondenserApertureIndex, ObjectiveAperture objectiveAperture1,
                      ObjectiveAperture objectiveAperture2, ObjectiveAperture objectiveAperture3,
                      ObjectiveAperture objectiveAperture4, Integer defaultObjectiveApertureIndex,
                      InsertionMechanismType sampleInsertionMechanism, Boolean energyFilter,
                      Float defaultEnergyFilterSlitWidth, Integer defaultSpotSize) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.manufacturer = manufacturer;
        this.model = model;
        this.location = location;
        this.defaultExtractionVoltageKV = defaultExtractionVoltageKV;
        this.defaultGunLensSetting = defaultGunLensSetting;
        this.availableVoltagesKV = availableVoltagesKV;
        this.defaultVoltageKV = defaultVoltageKV;
        this.condenser1ApertureDiameter = condenser1ApertureDiameter;
        this.condenser2ApertureDiameter = condenser2ApertureDiameter;
        this.condenser3ApertureDiameter = condenser3ApertureDiameter;
        this.condenser4ApertureDiameter = condenser4ApertureDiameter;
        this.defaultCondenserApertureIndex = defaultCondenserApertureIndex;
        this.objectiveAperture1 = objectiveAperture1;
        this.objectiveAperture2 = objectiveAperture2;
        this.objectiveAperture3 = objectiveAperture3;
        this.objectiveAperture4 = objectiveAperture4;
        this.defaultObjectiveApertureIndex = defaultObjectiveApertureIndex;
        this.sampleInsertionMechanism = sampleInsertionMechanism;
        this.energyFilter = energyFilter;
        this.defaultEnergyFilterSlitWidth = defaultEnergyFilterSlitWidth;
        this.defaultSpotSize = defaultSpotSize;
    }

    @SuppressWarnings("unused")
    public enum InsertionMechanismType {
        SIDE_ENTRY_HOLDER, AUTO_LOADER
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    public static class ObjectiveAperture {
        private Boolean phasePlate;
        private Integer diameter; // Unit: μm

        @Builder
        @SuppressWarnings("unused")
        public ObjectiveAperture(Boolean phasePlate, Integer diameter) {
            this.phasePlate = phasePlate;
            this.diameter = diameter;
        }
    }
}
