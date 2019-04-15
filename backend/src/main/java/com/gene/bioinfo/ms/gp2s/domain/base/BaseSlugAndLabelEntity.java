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

package com.gene.bioinfo.ms.gp2s.domain.base;

import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * A base {@link javax.persistence.Entity} with fields for audit, slug and label.
 *
 * @author Przemysław Stankowski on 28.08.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of= {}, callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseSlugAndLabelEntity extends BaseSlugEntity {


    @Column(name = "label", length = DomainConstants.SHORT_STRING_LENGTH, nullable = false)
    private String label;

    public BaseSlugAndLabelEntity(Long id, String label, String slug, Integer version,
                               Date createdDate, Date modifiedDate,
                               String createdBy, String modifiedBy) {
        super(id, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.label = label;
    }

    public BaseSlugAndLabelEntity(final BaseSlugAndLabelEntity other) {
        super(other);
        this.label = other.label;
    }

}
