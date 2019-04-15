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
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "model_link")
@Entity
public class ModelLink extends BaseAuditableEntity {

    @OrderBy("id DESC")
    @ManyToOne
    @JoinColumn(name = "parent_model_id", foreignKey = @ForeignKey(name =
            "model_link_to_parent_model_fk"), nullable = false)
    private Model parentModel;

    @OrderBy("id DESC")
    @ManyToOne
    @JoinColumn(name = "child_model_id", foreignKey = @ForeignKey(name =
            "model_link_to_child_model_fk"), nullable = false)
    private Model childModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false)
    private ModelRelationshipType relationshipType;

    @Column(name = "comment")
    private String comment;

    public ModelLink(Model parentModel, Model childModel, ModelRelationshipType relationshipType,
                     String comment, Long id, Integer version,
                     Date createdDate, Date modifiedDate, String createdBy,
                     String modifiedBy) {
        super(id, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.parentModel = parentModel;
        this.childModel = childModel;
        this.relationshipType = relationshipType;
        this.comment = comment;
    }

}