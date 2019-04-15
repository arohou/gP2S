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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
  A base {@link javax.persistence.Entity} with fields for audit, slug.
 *
 * @author Przemysław Stankowski on 28.08.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of= {}, callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseSlugEntity extends BaseAuditableEntity implements PersistableSlug<Long>{

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    public BaseSlugEntity(Long id, String slug, Integer version,
                               Date createdDate, Date modifiedDate,
                               String createdBy, String modifiedBy) {
        super(id, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.slug = slug;
    }

    public BaseSlugEntity(final BaseSlugEntity other) {
        super(other);
        this.slug = other.slug;
    }

}
