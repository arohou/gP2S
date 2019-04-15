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

import com.gene.bioinfo.ms.gp2s.service.attachment.UploadResponse;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Przemysław Stankowski on 12.04.2018
 */
public interface ISingleAttachmentRestController {

    @NonNull
    @PostMapping("/attachment/upload")
    ResponseEntity<UploadResponse> uploadFile(@RequestParam("qqfile") final MultipartFile file);

    @RequestMapping(value = "/attachment/download/{fileId:.+}", method = RequestMethod.GET)
    @ResponseBody
    void downloadFileById(@PathVariable("fileId") final String fileId,
                          final HttpServletResponse response) throws IOException;

    @DeleteMapping(value = "/attachment/{fileId}")
    void deleteFile(@PathVariable("fileId") final String fileId);

    @DeleteMapping(value = "/attachment")
    void deleteFile(@RequestBody final List<String> fileIds);
}
