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
import com.gene.bioinfo.ms.gp2s.domain.base.EntityType;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Przemysław Stankowski on 13.02.2018
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
@Entity
@EqualsAndHashCode(of = {}, callSuper = true)
public class Comment extends BaseAuditableEntity {

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "entity_type", nullable = false, length = DomainConstants.SHORT_STRING_LENGTH)
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(name = "content", nullable = false, length = DomainConstants.SHORT_TEXT_LENGTH)
    private String content;

    @OrderBy("id DESC")
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "comment_to_attachment_fk"))
    private List<CommentAttachment> attachments;

    @Builder
    public Comment(Long id, Integer version, Date createdDate, Date modifiedDate,
                   String createdBy, String modifiedBy, Long entityId, EntityType entityType,
                   String content, List<CommentAttachment> attachments) {
        super(id, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.entityId = entityId;
        this.entityType = entityType;
        this.content = content;
        this.attachments = attachments;
    }
}
