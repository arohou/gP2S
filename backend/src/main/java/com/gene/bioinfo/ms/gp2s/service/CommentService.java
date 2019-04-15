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

package com.gene.bioinfo.ms.gp2s.service;

import com.gene.bioinfo.ms.gp2s.domain.Attachment;
import com.gene.bioinfo.ms.gp2s.domain.Comment;
import com.gene.bioinfo.ms.gp2s.domain.base.EntityType;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.CommentRepository;
import com.gene.bioinfo.ms.gp2s.service.attachment.CommentAttachmentService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Przemysław Stankowski on 14.02.2018
 */
@Service
public class CommentService {

    protected final CommentRepository commentRepository;
    private final CommentAttachmentService attachmentService;

    protected CommentService(@NonNull final CommentRepository commentRepository,
                             @NotNull final CommentAttachmentService attachmentService) {
        this.commentRepository = commentRepository;
        this.attachmentService = attachmentService;
    }

    public List<Comment> getComments(@NonNull final Long entityId,
                                     @NonNull final EntityType entityType) {
        return this.commentRepository.findByEntityIdAndEntityTypeOrderByIdAsc(entityId, entityType);
    }

    public Integer countComments(@NonNull final Long entityId,
                                 @NonNull final EntityType entityType) {
        return this.commentRepository.countByEntityIdAndEntityType(entityId, entityType);
    }

    @NonNull
    public Comment addComment(@NonNull final Long entityId,
                              @NonNull final EntityType entityType,
                              @NonNull final String content) {
        return this.commentRepository.save(Comment.builder().entityId(entityId)
                .entityType(entityType).content(content).build());
    }

    @NonNull
    private Comment removeAttachmentsFromMongo(@NonNull final Comment comment) {
        if (comment.getAttachments() == null) {
            return comment;
        }

        for (Attachment attachment : comment.getAttachments()) {
            attachmentService.deleteFile(attachment.getMongoId());
        }

        return comment;
    }

    public void deleteComment(@NonNull final Long commentId) {
        Optional.ofNullable(this.commentRepository.findOne(commentId))
                .map(this::removeAttachmentsFromMongo)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found for id: " + commentId));

        this.commentRepository.delete(commentId);
    }

    @NonNull
    public Comment updateComment(@NonNull final Long id,
                                 @NonNull final String content,
                                 final Boolean forceSetModified) {
        final Comment comment = Optional.ofNullable(this.commentRepository.findOne(id))
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found for id: " + id));
        if (forceSetModified != null && forceSetModified) { // Attachments changes are not automatically reflected
            comment.setModifiedDate(new java.util.Date());  // in modifiedDate so date change needs to be forced.
        }
        comment.setContent(content);
        return this.commentRepository.save(comment);
    }

    @NonNull
    public Comment getComment(@NonNull final Long id) {
        return Optional.ofNullable(this.commentRepository.findOne(id))
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found for id: " + id));
    }
}
