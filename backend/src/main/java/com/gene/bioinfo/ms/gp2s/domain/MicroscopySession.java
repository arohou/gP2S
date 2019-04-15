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
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefault;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultType;
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
@GP2SDefaultType(DefaultValue.DefaultValueType.MICROSCOPY_SESSION)
@Table(name = "microscopy_session")
@Entity
public class MicroscopySession extends BaseSlugAndLabelEntity {

    // <editor-fold> Basic information.

    @GP2SDefault
    @ManyToOne(optional = false)
    @JoinColumn(name = "microscope_id", foreignKey = @ForeignKey(name = "microscopy_session_to_microscope_fk"))
    private Microscope microscope;

    @ManyToOne(optional = false)
    @JoinColumn(name = "grid_id", foreignKey = @ForeignKey(name = "microscopy_session_to_grid_fk"))
    private Grid grid;

    @Column(name = "grid_returned_to_storage")
    private Boolean gridReturnedToStorage;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "session_start", nullable = false)
    private Date sessionStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "session_finish")
    private Date sessionFinish;

    @Column(name = "number_of_images_acquired")
    private Integer numberOfImagesAcquired;

    @ManyToOne(optional = false)
    @JoinColumn(name = "electron_detector_id", foreignKey = @ForeignKey(name = "microscopy_session_to_electron_detector_fk"))
    private ElectronDetector electronDetector;

    @ManyToOne
    @JoinColumn(name = "sample_holder_id", foreignKey = @ForeignKey(name = "microscopy_session_to_sample_holder_fk"))
    private SampleHolder sampleHolder;

    @Column(name = "data_storage_directory_name", length = DomainConstants.LONG_STRING_LENGTH)
    private String dataStorageDirectoryName;

    // </editor-fold>

    // <editor-fold> Microscope settings.

    @Column(name = "extraction_voltage_kv")
    private Float extractionVoltageKV;

    @Column(name = "acceleration_voltage")
    private Float accelerationVoltageKV;

    @Column(name = "gun_lens_setting")
    private Integer gunLensSetting;

    @Column(name = "condenser2_aperture_diameter")
    private Integer condenser2ApertureDiameter;

    @AttributeOverrides({
            @AttributeOverride(name = "phasePlate", column = @Column(name = "objective_aperture_phase_plate")),
            @AttributeOverride(name = "diameter", column = @Column(name = "objective_aperture_diameter"))
    })
    private Microscope.ObjectiveAperture objectiveAperture;

    @Column(name = "energy_filter")
    private Boolean energyFilter;

    @Column(name = "energy_filter_slit_width")
    private Float energyFilterSlitWidth; // Unit: eV
    // </editor-fold>

    // <editor-fold> Exposure settings.

    @Column(name = "nominal_magnification")
    private Integer nominalMagnification;

    @Column(name = "calibrated_magnification")
    private Float calibratedMagnification;

    @Column(name = "pixel_size")
    private Float pixelSize;

    private Boolean nanoprobe;

    @Column(name = "spot_size")
    private Integer spotSize;

    @Column(name = "diameter_of_illuminated_area")
    private Float diameterOfIlluminatedArea;

    @Column(name = "counting_mode")
    private Boolean countingMode;

    @Column(name = "exposure_rate")
    private Float exposureRate;

    @Column(name = "exposure_duration")
    private Float exposureDuration;

    @Column(name = "dose_fractionation")
    private Boolean doseFractionation;

    @Column(name = "number_of_frames")
    private Integer numberOfFrames;

    @Column(name = "super_resolution")
    private Boolean superResolution;

    @Column(name = "supersampling_factor")
    private Integer supersamplingFactor;

    @Column(name = "pixel_binning")
    private Integer pixelBinning;
    // </editor-fold>

    // <editor-fold> Microscope control

    @Column(name = "target_underfocus_min")
    private Float targetUnderfocusMin;

    @Column(name = "target_underfocus_max")
    private Float targetUnderfocusMax;

    @Column(name = "exposures_per_hole")
    private Integer exposuresPerHole;
    // </editor-fold>

    @OrderBy("id DESC")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "microscopySessions")
    @JsonIgnore
    private List<ProcessingSession> processingSessions;

    @Builder
    public MicroscopySession(Long id, String label, String slug, Integer version,
                             Date createdDate, Date modifiedDate, String createdBy, String modifiedBy,
                             Microscope microscope, Grid grid, Boolean gridReturnedToStorage,
                             Date sessionStart, Date sessionFinish,
                             Integer numberOfImagesAcquired, ElectronDetector electronDetector,
                             SampleHolder sampleHolder,
                             String dataStorageDirectoryName,
                             Integer nominalMagnification, Float calibratedMagnification, Float pixelSize,
                             Boolean nanoprobe, Integer spotSize,
                             Float diameterOfIlluminatedArea, Boolean countingMode, Float exposureRate,
                             Float exposureDuration, Boolean doseFractionation, Integer numberOfFrames,
                             Boolean superResolution, Integer supersamplingFactor, Integer pixelBinning,
                             Float targetUnderfocusMin, Float targetUnderfocusMax, Integer exposuresPerHole,
                             Float extractionVoltageKV, Float accelerationVoltageKV, Integer gunLensSetting,
                             Integer condenser2ApertureDiameter, Microscope.ObjectiveAperture objectiveAperture,
                             Boolean energyFilter, Float energyFilterSlitWidth) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.microscope = microscope;
        this.grid = grid;
        this.gridReturnedToStorage = gridReturnedToStorage;
        this.sessionStart = sessionStart;
        this.sessionFinish = sessionFinish;
        this.numberOfImagesAcquired = numberOfImagesAcquired;
        this.electronDetector = electronDetector;
        this.sampleHolder = sampleHolder;
        this.dataStorageDirectoryName = dataStorageDirectoryName;

        this.extractionVoltageKV = extractionVoltageKV;
        this.accelerationVoltageKV = accelerationVoltageKV;
        this.gunLensSetting = gunLensSetting;
        this.condenser2ApertureDiameter = condenser2ApertureDiameter;
        this.objectiveAperture = objectiveAperture;
        this.energyFilter = energyFilter;
        this.energyFilterSlitWidth = energyFilter != null && energyFilter ? energyFilterSlitWidth : null;

        this.nominalMagnification = nominalMagnification;
        this.calibratedMagnification = calibratedMagnification;
        this.pixelSize = pixelSize;
        this.nanoprobe = nanoprobe;
        this.spotSize = spotSize;
        this.diameterOfIlluminatedArea = diameterOfIlluminatedArea;
        this.countingMode = countingMode;
        this.exposureRate = exposureRate;
        this.exposureDuration = exposureDuration;
        this.doseFractionation = doseFractionation;
        this.numberOfFrames = numberOfFrames;
        this.superResolution = superResolution;
        this.supersamplingFactor = supersamplingFactor;
        this.pixelBinning = pixelBinning;

        this.targetUnderfocusMin = targetUnderfocusMin;
        this.targetUnderfocusMax = targetUnderfocusMax;
        this.exposuresPerHole = exposuresPerHole;
    }
}
