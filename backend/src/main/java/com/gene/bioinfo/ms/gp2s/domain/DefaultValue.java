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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "default_value",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"project_id", "entity_type", "field_name"})})
@Entity
public class DefaultValue extends BaseAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "default_value_to_project_fk"))
    private Project project;

    @Column(name = "entity_type", length = DomainConstants.SHORT_STRING_LENGTH)
    @Enumerated(EnumType.STRING)
    private DefaultValueType entityType;

    @Column(name = "field_name")
    private String fieldName;

    private String value;

    @Builder
    public DefaultValue(Long id, Integer version,
                        Date createdDate, Date modifiedDate, String createdBy,
                        String modifiedBy, Project project, DefaultValueType entityType,
                        String fieldName, String value) {
        super(id, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.project = project;
        this.entityType = entityType;
        this.fieldName = fieldName;
        this.value = value;

    }

    public enum DefaultValueType {
        GRID,
        MICROSCOPY_SESSION,
        PROCESSING_SESSION
    }
}
