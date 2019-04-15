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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "cryo_storage_device")
@Entity
public class CryoStorageDevice extends BaseSlugAndLabelEntity {
    @Column(length = DomainConstants.SHORT_STRING_LENGTH)
    private String manufacturer;
    @Column(length = DomainConstants.SHORT_STRING_LENGTH)
    private String location;
    @Column(length = DomainConstants.SHORT_STRING_LENGTH)
    private String model;
    @Column(name = "has_cylinders", nullable = false)
    private Boolean hasCylinders;
    @Column(name = "has_tubes", nullable = false)
    private Boolean hasTubes;
    @Column(name = "has_boxes", nullable = false)
    private Boolean hasBoxes;

    @Builder
    public CryoStorageDevice(Long id, String label, String slug, Integer version, Date createdDate, Date modifiedDate,
                             String createdBy, String modifiedBy, String manufacturer, String location,
                             String model, Boolean hasCylinders, Boolean hasTubes, Boolean hasBoxes) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.manufacturer = manufacturer;
        this.location = location;
        this.model = model;
        this.hasCylinders = hasCylinders;
        this.hasTubes = hasTubes;
        this.hasBoxes = hasBoxes;
    }
}
