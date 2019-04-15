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
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "image_processing_software")
@Entity
public class ImageProcessingSoftware extends BaseSlugAndLabelEntity {

    @ElementCollection
    @CollectionTable(name = "image_processing_software_version", joinColumns = @JoinColumn(name =
            "image_processing_software_id"))
    @Column(name = "software_version", nullable = false)
    @OrderColumn(name = "order_idx")
    private List<String> softwareVersions;

    @Builder
    public ImageProcessingSoftware(Long id, String label, String slug, Integer version,
                                   Date createdDate, Date modifiedDate,
                                   String createdBy, String modifiedBy,
                                   List<String> softwareVersions) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.softwareVersions = softwareVersions;
    }
}
