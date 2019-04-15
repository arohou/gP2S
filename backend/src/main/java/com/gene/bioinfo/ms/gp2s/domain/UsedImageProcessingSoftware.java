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

import com.gene.bioinfo.ms.gp2s.domain.base.BaseAuditableEntity;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Przemysław Stankowski on 13.02.2018
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "used_image_processing_software")
@Entity
@EqualsAndHashCode(of = {}, callSuper = true)
public class UsedImageProcessingSoftware extends BaseAuditableEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "image_processing_software_id", foreignKey = @ForeignKey(name =
            "used_image_processing_software_to_image_processing_software_fk"))
    private ImageProcessingSoftware imageProcessingSoftware;

    @Column(name = "software_version", nullable = false, length = DomainConstants.SHORT_STRING_LENGTH)
    private String softwareVersion;

    @Column(name = "notes", length = DomainConstants.SHORT_TEXT_LENGTH)
    private String notes;

    @Builder
    public UsedImageProcessingSoftware(Long id, Integer version, Date createdDate, Date modifiedDate,
                                       String createdBy, String modifiedBy, ImageProcessingSoftware
                                               imageProcessingSoftware, String softwareVersion, String notes) {
        super(id, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.imageProcessingSoftware = imageProcessingSoftware;
        this.softwareVersion = softwareVersion;
        this.notes = notes;
    }
}
