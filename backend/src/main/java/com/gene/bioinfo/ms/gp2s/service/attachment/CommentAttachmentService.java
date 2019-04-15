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

package com.gene.bioinfo.ms.gp2s.service.attachment;

import com.gene.bioinfo.ms.gp2s.domain.Attachment;
import com.gene.bioinfo.ms.gp2s.domain.Comment;
import com.gene.bioinfo.ms.gp2s.domain.CommentAttachment;
import com.gene.bioinfo.ms.gp2s.repository.CommentAttachmentRepository;
import com.gene.bioinfo.ms.gp2s.repository.CommentRepository;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentAttachmentService extends AttachmentServiceOperations<Comment> implements IAttachmentService {
    private final CommentAttachmentRepository commentAttachmentRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentAttachmentService(FileRepository fileRepository,
                                    @NonNull final CommentAttachmentRepository commentAttachmentRepository,
                                    @NonNull final CommentRepository commentRepository) {
        super(fileRepository);
        this.commentAttachmentRepository = commentAttachmentRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Long createAttachment(Comment comment, String fileName, String mongoId) {
        final CommentAttachment attachment = CommentAttachment.builder()
                .comment(comment)
                .fileName(fileName)
                .mongoId(mongoId)
                .build();
        return commentAttachmentRepository.save(attachment)
                .getId();
    }

    @Override
    public void deleteFile(Long id) {
        final Attachment attachment = commentAttachmentRepository.findOne(id);

        if (attachment == null || attachment.getMongoId() == null) {
            return;
        }

        deleteFile(attachment.getMongoId());
        commentAttachmentRepository.delete(id);
    }

    @Override
    public Comment findEntity(Long id) {
        return commentRepository.findOne(id);
    }
}
