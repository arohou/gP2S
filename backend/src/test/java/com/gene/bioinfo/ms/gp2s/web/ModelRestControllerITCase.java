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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.Model;
import com.gene.bioinfo.ms.gp2s.service.attachment.UploadResponse;
import com.gene.bioinfo.ms.gp2s.web.base.BaseITCaseWithRepositories;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.Nullable;
import java.io.IOException;

import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ModelRestControllerITCase extends BaseITCaseWithRepositories<Model> {
    public static final String MULTIPART_FILE_NAME = "qqfile";
    public static final String MODEL_ATTACHMENTS_URL = "/api/v1/model/attachment/";

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(getModelRepository());
    }

    @Override
    public String getUri() {
        return "/api/v1/model/";
    }

    @Override
    public String getCreateUri() {
        return URI + "/" + PROJECT_SLUG_1;
    }

    @NonNull
    private Model.ModelBuilder createModel(@NonNull final Model.ModelBuilder builder) {
        try {
            final MockMultipartFile file =
                    new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                            .getBytes());
            final UploadResponse uploadResponse = uploadFile(file);

            return builder
                    .resolution(1.5)
                    .maps(getMaps())
                    .attachmentFileName("some_file.txt")
                    .attachmentMongoId(uploadResponse.getMongoId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Model createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return createModel(createModel(Model.builder().id(id).label(label).slug(slug))).build();
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(createModel(Model.builder().id(null).slug(null).label(label)).build());
    }

    @Test
    public void readModelProjectsById() throws Exception {
        //check if new model is associated to two projects
        getMockMvc()
                .perform(get(URI + entities.get(1).getId() + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(getProjects().get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(getProjects().get(0).getSlug())))
                .andExpect(jsonPath("$[0].label", is(PROJECT_LABEL_1)))
                .andExpect(jsonPath("$[1].id", is(getProjects().get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].slug", is(getProjects().get(1).getSlug())))
                .andExpect(jsonPath("$[1].label", is(PROJECT_LABEL_2)));
    }

    @Test
    public void readModelProjectsBySlug() throws Exception {
        //check if new model is associated to two projects
        getMockMvc()
                .perform(get(URI + entities.get(0).getSlug() + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(getProjects().get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(getProjects().get(0).getSlug())))
                .andExpect(jsonPath("$[0].label", is(PROJECT_LABEL_1)))
                .andExpect(jsonPath("$[1].id", is(getProjects().get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].slug", is(getProjects().get(1).getSlug())))
                .andExpect(jsonPath("$[1].label", is(PROJECT_LABEL_2)));
    }

    @Test
    public void readModelProjectionFromProjectById() throws Exception {
        final Long id = getProjects().get(0).getId();
        final Model model = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + id + "/model"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(JSON_CONTENT_TYPE))
                    .andExpect(jsonPath("$[0].id", is(model.getId().intValue())))
                    .andExpect(jsonPath("$[0].slug", is(model.getSlug())))
                    .andExpect(jsonPath("$[0].label", is(model.getLabel())));
    }

    @Test
    public void readModelProjectionFromProjectBySlug() throws Exception {
        final String slug = getProjects().get(0).getSlug();
        final Model model = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + slug + "/model"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(JSON_CONTENT_TYPE))
                    .andExpect(jsonPath("$[0].id", is(model.getId().intValue())))
                    .andExpect(jsonPath("$[0].slug", is(model.getSlug())))
                    .andExpect(jsonPath("$[0].label", is(model.getLabel())));
    }

    @Test
    public void shouldValidationReturnMultipleErrors() throws Exception {
        final Model model = Model.builder().resolution(null).build();
        final String json = new ObjectMapper().writeValueAsString(model);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.label", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.resolution", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.maps", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.attachmentMongoId", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.attachmentFileName", is(not(isEmptyOrNullString()))));
    }

    @Test
    public void countMaps() throws Exception {
        getMockMvc().perform(get("/api/v1/project/" + PROJECT_SLUG_1 + "/model/count"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(entities.size())));
    }

    @NonNull
    public UploadResponse uploadFile(@NonNull final MockMultipartFile file) throws
            Exception
    {
        final MvcResult result = getMockMvc().perform(
                fileUpload(MODEL_ATTACHMENTS_URL + "upload").file(file))
                                             .andDo(print())
                                             //then
                                             .andExpect(status().isOk())
                                             .andReturn();
        return new ObjectMapper().readValue(result.getResponse().getContentAsByteArray(), UploadResponse.class);
    }

    @Test
    public void shouldUploadOrphanFile() throws Exception {
        //given
        final MockMultipartFile file =
                new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                        .getBytes());
        //when
        final UploadResponse uploadResponse = uploadFile(file);
        //then
        assertThat(uploadResponse.getMongoId(), is(not(isEmptyOrNullString())));
    }

    @Test
    public void shouldDeleteFile() throws Exception {
        //given
        final MockMultipartFile file =
                new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                        .getBytes());
        final UploadResponse result = uploadFile(file);

        //when
        getMockMvc().perform(delete(MODEL_ATTACHMENTS_URL + result.getMongoId()))
                    .andExpect(status().isOk());

        //then
        getMockMvc().perform(get(format(MODEL_ATTACHMENTS_URL + "download/%s", result.getMongoId())))
                    .andExpect(status().isNotFound());
    }

    @Test
    public void shouldRemoveFileAssociationFromMapOnFileDelete() throws Exception {
        //given
        final Model model = entities.get(0);
        final MockMultipartFile file =
                new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                        .getBytes());
        final UploadResponse uploadResponse = uploadFile(file);

        model.setAttachmentMongoId(uploadResponse.getMongoId());
        getMockMvc().perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json(model)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.attachmentMongoId", is(not(isEmptyOrNullString()))));

        //when
        getMockMvc().perform(delete(MODEL_ATTACHMENTS_URL + uploadResponse.getMongoId()))
                    .andExpect(status().isOk());

        //then
        getMockMvc()
                .perform(get(URI + model.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.attachmentMongoId", isEmptyOrNullString()));
    }

    @Test
    public void shouldAssociateFileWithModel() throws Exception {
        //given
        final Model model = entities.get(0);
        final String originalFilename = "filename.txt";
        final MockMultipartFile file =
                new MockMultipartFile(MULTIPART_FILE_NAME, originalFilename, "text/plain", "some text"
                        .getBytes());
        final UploadResponse uploadResponse = uploadFile(file);

        //when
        model.setAttachmentMongoId(uploadResponse.getMongoId());
        final MvcResult result = getMockMvc().perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json(model)))
                                             .andDo(print())
                                             .andExpect(status().isOk())
                                             .andReturn();

        final ObjectMapper mapper = new ObjectMapper();
        final Model resultModel = mapper.readValue(result.getResponse()
                                                         .getContentAsByteArray(), mapper.getTypeFactory()
                                                                                         .constructType(Model.class));

        //then
        assertThat(resultModel.getAttachmentMongoId(), is(uploadResponse.getMongoId()));
    }

    @Test
    public void shouldDownloadFile() throws Exception {
        //given
        final String originalFilename = "filename.txt";
        final String fileContent = "some text";
        final String contentType = "text/plain";
        final MockMultipartFile file =
                new MockMultipartFile(MULTIPART_FILE_NAME, originalFilename, contentType, fileContent
                        .getBytes());
        UploadResponse result = uploadFile(file);

        //when
        final String url = format(MODEL_ATTACHMENTS_URL + "download/%s", result.getMongoId());
        getMockMvc().perform(get(url))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", contentType))
                    .andExpect(header().longValue("Content-Length", fileContent.length()))
                    .andExpect(header().string("Content-Disposition", "attachment; filename=" + originalFilename))
                    .andExpect(content().string(fileContent));
    }
}
