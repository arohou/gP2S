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

import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.Optional;

@Service
public class SingleAttachmentService implements ISingleAttachmentService {
    private final FileRepository fileRepository;

    public SingleAttachmentService(@NonNull final FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public UploadResponse createFile(@NonNull final MultipartFile file) {
        try {
            final File attachmentFile = File.builder()
                    .multipartFile(file)
                    .build();
            final String mongoId = fileRepository.store(attachmentFile);

            return UploadResponse.builder()
                    .successResponse(mongoId)
                    .build();
        } catch (IOException e) {
            return UploadResponse.builder()
                    .failResponse(e.getMessage())
                    .build();
        }
    }

    public File getFile(@Null final String fileId) {
        if (fileId == null) {
            return null;
        }
        return fileRepository.findOne(fileId);
    }

    public void deleteFile(@NonNull final String fileId) {
        Optional.ofNullable(fileRepository.findOne(fileId)).orElseThrow(ResourceNotFoundException::new);
        fileRepository.delete(fileId);
    }
}
