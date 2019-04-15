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
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultComposite;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultDefinition;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultType;
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
@GP2SDefaultType(DefaultValue.DefaultValueType.PROCESSING_SESSION)
@Table(name = "processing_session")
@Entity
public class ProcessingSession extends BaseSlugAndLabelEntity {

    //region Basic Information
    @Column(name = "number_of_micrographs")
    private Integer numberOfMicrographs;

    @Column(name = "number_of_picked_particles")
    private Integer numberOfPickedParticles;

    @Column(name = "base_path_of_processing_directory")
    private String basePathOfProcessingDirectory;
    //endregion

    @OrderBy("id DESC")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name = "processing_session_microscopy_session",
            joinColumns = {@JoinColumn(name = "processing_session_id")},
            foreignKey = @ForeignKey(name = "processing_session_to_microscopy_session_fk"),
            inverseJoinColumns = {@JoinColumn(name = "microscopy_session_id")},
            inverseForeignKey = @ForeignKey(name = "microscopy_session_to_processing_session_fk"),
            uniqueConstraints = {@UniqueConstraint(name = "processing_session_microscopy_session_unique_constraint",
                    columnNames = {"processing_session_id", "microscopy_session_id"})})
    private Set<MicroscopySession> microscopySessions;

    @GP2SDefaultComposite(definition = {
            @GP2SDefaultDefinition(key = "imageProcessingSoftware",
                    value = "${usedImageProcessingSoftware[0].imageProcessingSoftware.id}"),
            @GP2SDefaultDefinition(key = "softwareVersion.${usedImageProcessingSoftware[0].imageProcessingSoftware.id}",
                    value = "${usedImageProcessingSoftware[0].softwareVersion}",
                    project = false)
    })
    @OrderColumn(name = "order_idx")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "processing_session_id", foreignKey = @ForeignKey(name =
            "used_image_processing_software_to_processing_session_fk"))
    private List<UsedImageProcessingSoftware> usedImageProcessingSoftware;

    @Builder
    public ProcessingSession(Long id, String label, String slug, Integer version, Date createdDate, Date modifiedDate,
                             String createdBy, String modifiedBy,
                             Integer numberOfMicrographs, Integer numberOfPickedParticles,
                             String basePathOfProcessingDirectory, Set<MicroscopySession> microscopySessions,
                             List<UsedImageProcessingSoftware> usedImageProcessingSoftware) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.numberOfMicrographs = numberOfMicrographs;
        this.numberOfPickedParticles = numberOfPickedParticles;
        this.basePathOfProcessingDirectory = basePathOfProcessingDirectory;

        this.microscopySessions = microscopySessions;
        this.usedImageProcessingSoftware = usedImageProcessingSoftware;
    }

}
