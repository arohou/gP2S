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

package com.gene.bioinfo.ms.gp2s.web.base.integration.preparers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.Comment;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugEntity;
import com.gene.bioinfo.ms.gp2s.service.attachment.UploadResponse;
import lombok.NonNull;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.gene.bioinfo.ms.gp2s.domain.base.EntityType.entityType;
import static com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.MockMvcExecutor.API_V1_COMMENT;
import static com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.MockMvcExecutor.url;
import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentPreparer extends DataPreparer {
    public static final String COMMENT_ATTACHMENTS_URL = "/api/v1/comment/attachment/";

    public CommentPreparer(@NonNull final MockMvc mockMvc) {
        super(mockMvc);
    }

    @NonNull
    public <T extends BaseSlugEntity> Comment prepareComment(@NonNull final T entity) {
        return mockMvcExecutor.post(format(API_V1_COMMENT, entityType(entity.getClass()), entity.getId()),
                Comment.builder().content("Some comment text").build());
    }

    @NonNull
    public <T extends BaseSlugEntity> List<Comment> getCommentsFor(@NonNull final T entity) {
        final String url = String.format(url(Comment.class), entityType(entity.getClass()), entity.getId());
        return this.mockMvcExecutor.getList(url, Comment.class);
    }

    @NonNull
    public UploadResponse addAttachmentToComment(
            @NonNull final Comment comment, @NonNull final MockMultipartFile file) throws
            Exception {
        final MvcResult result = mockMvc.perform(fileUpload(COMMENT_ATTACHMENTS_URL + "upload")
                .file(file)
                .param("entityType", entityType(Comment.class))
                .param("entityId", comment.getId().toString()))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readValue(result.getResponse()
                .getContentAsByteArray(), UploadResponse.class);
    }
}
