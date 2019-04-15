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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "model")
@Entity
public class Model extends BaseSlugAndLabelEntity {
    @Column(name = "resolution")
    private Double resolution;
    @Column(name = "attachment_mongo_id")
    private String attachmentMongoId;
    @Column(name = "attachment_file_name")
    private String attachmentFileName;

    @OrderBy("id DESC")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(name = "model_map",
            joinColumns = {@JoinColumn(name = "model_id")},
            foreignKey = @ForeignKey(name = "model_to_map_fk"),
            inverseJoinColumns = {@JoinColumn(name = "map_id")},
            inverseForeignKey = @ForeignKey(name = "map_to_model_fk"),
            uniqueConstraints = {@UniqueConstraint(name = "model_map_unique_constraint",
                    columnNames = {"model_id", "map_id"})})
    private List<Map> maps;

    @Builder
    public Model(Long id, String label, String slug, Integer version, Date createdDate, Date modifiedDate,
                 String createdBy, String modifiedBy, Double resolution, List<Map> maps,
                 String attachmentMongoId, String attachmentFileName) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.resolution = resolution;
        this.attachmentMongoId = attachmentMongoId;
        this.attachmentFileName = attachmentFileName;
        this.maps = maps;
    }
}
