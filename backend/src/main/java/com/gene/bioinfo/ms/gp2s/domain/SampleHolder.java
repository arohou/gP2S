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

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "sample_holder")
@Entity
public class SampleHolder extends BaseSlugAndLabelEntity {
    @Column(length = DomainConstants.SHORT_STRING_LENGTH, nullable = false)
    private String manufacturer;
    @Column(length = DomainConstants.SHORT_STRING_LENGTH, nullable = false)
    private String model;
    @Column(name = "location", length = DomainConstants.SHORT_STRING_LENGTH, nullable = false)
    private String location;
    @Column(name = "cryo_capable", nullable = false)
    private Boolean cryoCapable = false;
    @Column(name = "maximum_tilt", nullable = false)
    private Float maximumTilt;
    @Column(name = "double_tilt")
    private Boolean doubleTilt = false;

    @OrderBy("id DESC")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name = "sample_holder_microscope",
            joinColumns = {@JoinColumn(name = "sample_holder_id")},
            foreignKey = @ForeignKey(name = "sample_holder_to_microscope_fk"),
            inverseJoinColumns = {@JoinColumn(name = "microscope_id")},
            inverseForeignKey = @ForeignKey(name = "microscope_to_sample_holder_fk"),
            uniqueConstraints = {@UniqueConstraint(name = "sample_holder_microscope_unique_constraint",
                    columnNames = {"sample_holder_id", "microscope_id"})})
    private List<Microscope> microscopes; // Microscopes it can be used with.

    @Builder
    public SampleHolder(Long id, String label, String slug, Integer version,
                        Date createdDate, Date modifiedDate, String createdBy,
                        String modifiedBy, String manufacturer, String model, String location,
                        List<Microscope> microscopes, Boolean cryoCapable, Float maximumTilt, Boolean doubleTilt) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.manufacturer = manufacturer;
        this.model = model;
        this.location = location;
        this.microscopes = microscopes;
        this.cryoCapable = cryoCapable == null ? Boolean.FALSE : cryoCapable;
        this.maximumTilt = maximumTilt;
        this.doubleTilt = doubleTilt == null ? Boolean.FALSE : doubleTilt;
    }
}
