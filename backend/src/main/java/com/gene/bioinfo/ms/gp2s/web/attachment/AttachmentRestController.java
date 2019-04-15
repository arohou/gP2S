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

package com.gene.bioinfo.ms.gp2s.web.attachment;

import com.gene.bioinfo.ms.gp2s.service.attachment.File;
import com.gene.bioinfo.ms.gp2s.service.attachment.IAttachmentService;
import com.gene.bioinfo.ms.gp2s.service.attachment.UploadResponse;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class AttachmentRestController {
    private final IAttachmentService service;

    @Autowired
    public AttachmentRestController(@NonNull final IAttachmentService service) {
        this.service = service;
    }

    @PostMapping("/attachment/upload")
    public ResponseEntity<UploadResponse> upload(
            @NonNull @RequestParam("qqfile") final MultipartFile file,
            @NonNull @RequestParam("entityType") final String entityType,
            @Null @RequestParam(value = "entityId", required = false) final Long entityId) {
        try {
            UploadResponse response = service.createFile(file, entityType, entityId);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            int errorCode = 400;
            if (e instanceof NoSuchElementException) {
                errorCode = 404;
            }
            return ResponseEntity.status(errorCode)
                    .body(UploadResponse.builder().errorMessage(e.getMessage()).build());
        }
    }

    @RequestMapping(value = "/attachment/download/{fileId:.+}", method = RequestMethod.GET)
    public @ResponseBody
    void downloadFileById(@NonNull @PathVariable("fileId") final String fileId,
                          @NonNull final HttpServletResponse response) throws IOException {

        final File file = service.getFile(fileId);

        if (file == null) {
            response.setStatus(404);
            return;
        }

        try {
            response.setContentType(file.getContentType());
            response.setContentLength((file.getLength()
                    .intValue()));
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getFileName());

            // Copy file InputStream to response's OutputStream.
            IOUtils.copyLarge(file.getContentStream(), response.getOutputStream());
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @DeleteMapping(value = "/attachment/{fileId}")
    public void deleteItem(@NonNull @PathVariable("fileId") final Long id) {
        this.service.deleteFile(id);
    }

    @DeleteMapping(value = "/attachment")
    public void deleteItem(@NonNull @RequestBody final List<Long> fileIds) {
        for (Long id : fileIds) {
            if (id != null) {
                this.service.deleteFile(id);
            }
        }
    }
}