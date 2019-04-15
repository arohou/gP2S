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

package com.gene.bioinfo.ms.gp2s.web;

import com.gene.bioinfo.ms.gp2s.domain.Map;
import com.gene.bioinfo.ms.gp2s.service.MapService;
import com.gene.bioinfo.ms.gp2s.service.attachment.UploadResponse;
import com.gene.bioinfo.ms.gp2s.web.attachment.ISingleAttachmentRestController;
import com.gene.bioinfo.ms.gp2s.web.base.BaseProjectRestController;
import com.gene.bioinfo.ms.gp2s.web.validator.MapValidator;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Api(description = "map management", value = "map")
@RequestMapping(value = API_V1 + "/map", produces = APPLICATION_JSON_UTF8_VALUE)
public class MapRestController extends BaseProjectRestController<Map, MapService> implements ISingleAttachmentRestController {

    private final ISingleAttachmentRestController attachmentController;

    MapRestController(
            @NonNull final ISingleAttachmentRestController mapAttachmentRestController,
            @NotNull final MapService service,
            @NotNull final MapValidator validator) {
        super(service, validator);
        this.attachmentController = mapAttachmentRestController;
    }


    @PostMapping("/attachment/upload")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("qqfile") final MultipartFile file) {
        return attachmentController.uploadFile(file);
    }

    @Override
    @RequestMapping(value = "/attachment/download/{fileId:.+}", method = RequestMethod.GET)
    public @ResponseBody
    void downloadFileById(@NonNull @PathVariable("fileId") final String fileId,
                          @NonNull final HttpServletResponse response) throws IOException {
        attachmentController.downloadFileById(fileId, response);
    }

    @Override
    @DeleteMapping(value = "/attachment/{fileId}")
    public void deleteFile(@NonNull @PathVariable("fileId") final String fileId) {
        attachmentController.deleteFile(fileId);
    }

    @Override
    @DeleteMapping(value = "/attachment")
    public void deleteFile(@NonNull @RequestBody final List<String> fileIds) {
        attachmentController.deleteFile(fileIds);
    }
}
