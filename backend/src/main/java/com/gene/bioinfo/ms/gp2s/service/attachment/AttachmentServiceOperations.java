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

import com.gene.bioinfo.ms.gp2s.domain.base.EntityType;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.NoSuchElementException;

@Service
public abstract class AttachmentServiceOperations<T> {
    private final FileRepository fileRepository;

    public AttachmentServiceOperations(@NonNull final FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public UploadResponse createFile(@NonNull final MultipartFile file,
                                     @NonNull final String entityType,
                                     @NonNull final Long entityId) {
        final EntityType entityTypeEnum = EntityType.valueOf(entityType.toUpperCase());

        final T entity = findEntity(entityId);
        if (entity == null) {
            throw new NoSuchElementException("Entity with id = " + entityId + " not found");
        }

        try {
            final MongoFileMetadata metadata = MongoFileMetadata.builder()
                    .entityType(entityTypeEnum.name())
                    .entityId(entityId)
                    .build();
            final File attachmentFile = File.builder()
                    .fileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileMetadata(metadata)
                    .contentStream(file.getInputStream())
                    .build();
            final String mongoId = fileRepository.store(attachmentFile);

            // After deployment to mongo is successful, create entity.
            final Long attachmentId = createAttachment(entity, attachmentFile.getFileName(), mongoId);

            return UploadResponse.builder()
                    .success(true)
                    .mongoId(mongoId)
                    .id(attachmentId)
                    .build();
        } catch (IOException e) {
            return UploadResponse.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    public File getFile(@Null final String fileId) {
        if (fileId == null) {
            return null;
        }
        return fileRepository.findOne(fileId);
    }

    @NonNull
    public abstract Long createAttachment(final T entity,
                                          final String fileName,
                                          final String mongoId);

    @Null
    protected abstract T findEntity(final Long id);

    public void deleteFile(final String mongoId) {
        fileRepository.delete(mongoId);
    }
}
