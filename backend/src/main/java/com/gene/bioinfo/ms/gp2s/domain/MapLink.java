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
@Table(name = "map_link")
@Entity
public class MapLink extends BaseAuditableEntity {

    @OrderBy("id DESC")
    @ManyToOne
    @JoinColumn(name = "parent_map_id", foreignKey = @ForeignKey(name =
            "map_link_to_parent_map_fk"), nullable = false)
    private Map parentMap;

    @OrderBy("id DESC")
    @ManyToOne
    @JoinColumn(name = "child_map_id", foreignKey = @ForeignKey(name =
            "map_link_to_child_map_fk"), nullable = false)
    private Map childMap;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false)
    private MapRelationshipType relationshipType;

    @Column(name = "comment")
    private String comment;

    public MapLink(Map parentMap, Map childMap, MapRelationshipType relationshipType,
                   String comment, Long id, Integer version,
                   Date createdDate, Date modifiedDate, String createdBy,
                   String modifiedBy) {
        super(id, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.parentMap = parentMap;
        this.childMap = childMap;
        this.relationshipType = relationshipType;
        this.comment = comment;
    }

}