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

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "electron_detector")
@Entity
public class ElectronDetector extends BaseSlugAndLabelEntity {

    @Column(length = DomainConstants.SHORT_STRING_LENGTH, nullable = false)
    private String manufacturer;

    @Column(length = DomainConstants.SHORT_STRING_LENGTH, nullable = false)
    private String model;

    @ManyToOne(optional = false)
    @JoinColumn(name = "microscope_id", foreignKey = @ForeignKey(name = "electron_detector_to_microscope_fk"))
    private Microscope microscope;

    @Column(name = "counts_per_electrons_factor")
    private Float countsPerElectronsFactor;

    @Column(name = "counting_mode_available")
    private Boolean countingModeAvailable;

    @Column(name = "pixel_linear_dimension_um")
    private Float pixelLinearDimensionUm;

    @Column(name = "number_of_pixel_columns")
    private Integer numberOfPixelColumns;

    @Column(name = "number_of_pixel_rows")
    private Integer numberOfPixelRows;

    @Column(name = "dose_fractionation_available")
    private Boolean doseFractionationAvailable;

    @Column(name = "super_resolution_available")
    private Boolean superResolutionAvailable;

    @OrderBy("id DESC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "electron_detector_id", foreignKey = @ForeignKey(name = "magnification_to_electron_detector_fk"))
    private List<Magnification> availableMagnifications; // It should be a List, not a Set because a Set can
    // extract only 1 value for a given microscope id.

    @Builder
    public ElectronDetector(Long id, String label, String slug, Integer version, Date createdDate, Date modifiedDate,
                            String createdBy, String modifiedBy, String manufacturer, String model,
                            Microscope microscope, Float countsPerElectronsFactor,
                            Boolean countingModeAvailable, Float pixelLinearDimensionUm,
                            Integer numberOfPixelColumns, Integer numberOfPixelRows,
                            Boolean doseFractionationAvailable, Boolean superResolutionAvailable,
                            List<Magnification> availableMagnifications) {

        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.manufacturer = manufacturer;
        this.model = model;
        this.microscope = microscope;
        this.countsPerElectronsFactor = countsPerElectronsFactor;
        this.countingModeAvailable = countingModeAvailable;
        this.pixelLinearDimensionUm = pixelLinearDimensionUm;
        this.numberOfPixelColumns = numberOfPixelColumns;
        this.numberOfPixelRows = numberOfPixelRows;
        this.doseFractionationAvailable = doseFractionationAvailable;
        this.superResolutionAvailable = superResolutionAvailable;
        // Preventing the error:
        // 'A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance'
        this.availableMagnifications = Optional.ofNullable(availableMagnifications).orElse(Collections.emptyList());
    }
}
