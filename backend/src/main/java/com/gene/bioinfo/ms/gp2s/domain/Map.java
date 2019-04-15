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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "map")
@Entity
public class Map extends BaseSlugAndLabelEntity {
    @Column(name = "pixel_size")
    private Double pixelSize;
    @Column(name = "isolevel_for_surface_rendering")
    private Double isolevelForSurfaceRendering;
    @ManyToOne(optional = false)
    @JoinColumn(name = "processing_session_id", foreignKey = @ForeignKey(name = "map_to_processing_session_fk"))
    private ProcessingSession processingSession;
    @Column(name = "number_of_images")
    private Integer numberOfImages;
    @Column(name = "estimated_resolution_in_best_parts")
    private Double estimatedResolutionInBestParts;
    @Column(name = "estimated_resolution_in_average_parts")
    private Double estimatedResolutionInAverageParts;
    @Column(name = "estimated_resolution_in_worst_parts")
    private Double estimatedResolutionInWorstParts;
    @Column(name = "symmetry_applied")
    private String symmetryApplied;
    @Column(name = "description")
    private String description;
    @Column(name = "attachment_mongo_id")
    private String attachmentMongoId;
    @Column(name = "attachment_file_name")
    private String attachmentFileName;

    @Builder
    public Map(Long id, String label, String slug, Integer version, Date createdDate, Date modifiedDate,
               String createdBy, String modifiedBy, Double pixelSize, Double isolevelForSurfaceRendering,
               ProcessingSession processingSession, Integer numberOfImages, Double estimatedResolutionInBestParts,
               Double estimatedResolutionInAverageParts, Double estimatedResolutionInWorstParts, String symmetryApplied,
               String description, String attachmentMongoId, String attachmentFileName) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.pixelSize = pixelSize;
        this.isolevelForSurfaceRendering = isolevelForSurfaceRendering;
        this.processingSession = processingSession;
        this.numberOfImages = numberOfImages;
        this.estimatedResolutionInBestParts = estimatedResolutionInBestParts;
        this.estimatedResolutionInAverageParts = estimatedResolutionInAverageParts;
        this.estimatedResolutionInWorstParts = estimatedResolutionInWorstParts;
        this.symmetryApplied = symmetryApplied;
        this.description = description;
        this.attachmentMongoId = attachmentMongoId;
        this.attachmentFileName = attachmentFileName;
    }
}
