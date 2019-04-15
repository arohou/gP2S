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
import com.gene.bioinfo.ms.gp2s.domain.Map;
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

public class MapRestControllerITCase extends BaseITCaseWithRepositories<Map> {
    public static final String MULTIPART_FILE_NAME = "qqfile";
    public static final String MAP_ATTACHMENTS_URL = "/api/v1/map/attachment/";

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(getMapRepository());
    }

    @Override
    public String getUri() {
        return "/api/v1/map/";
    }

    @Override
    public String getCreateUri() {
        return URI + "/" + PROJECT_SLUG_1;
    }

    public Map.MapBuilder createMap(Map.MapBuilder builder) {
        try {
            final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                    .getBytes());
            final UploadResponse uploadResponse = uploadFile(file);
            
            return builder
                    .pixelSize(1.0)
                    .isolevelForSurfaceRendering(1.0)
                    .processingSession(getProcessingSession())
                    .numberOfImages(5)
                    .estimatedResolutionInBestParts(1.0)
                    .estimatedResolutionInWorstParts(1.1)
                    .attachmentFileName("some_file.txt")
                    .attachmentMongoId(uploadResponse.getMongoId())
                    .symmetryApplied("C15");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return createMap(Map.builder().id(id).label(label).slug(slug)).build();
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(createMap(Map.builder().id(null).slug(null).label(label)).build());
    }

    @Test
    public void readMapProjectsById() throws Exception {
        //check if new sample is associated to two projects
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
    public void readMapProjectsBySlug() throws Exception {
        //check if new sample is associated to two projects
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
    public void readMapProjectionFromProjectById() throws Exception {
        final Long id = getProjects().get(0).getId();
        final Map map = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + id + "/map"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(map.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(map.getSlug())))
                .andExpect(jsonPath("$[0].label", is(map.getLabel())));
    }

    @Test
    public void readMapProjectionFromProjectBySlug() throws Exception {
        final String slug = getProjects().get(0).getSlug();
        final Map map = entities.get(1);
        getMockMvc().perform(get("/api/v1/project/" + slug + "/map"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id", is(map.getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(map.getSlug())))
                .andExpect(jsonPath("$[0].label", is(map.getLabel())));
    }

    @Test
    public void shouldValidationReturnMultipleErrors() throws Exception {
        final Map map = Map.builder().build();
        final String json = new ObjectMapper().writeValueAsString(map);

        getMockMvc()
                .perform(post(URI + '/' + PROJECT_SLUG_1).contentType(JSON_CONTENT_TYPE).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.errorMessage", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.label", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.pixelSize", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.isolevelForSurfaceRendering", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.processingSession", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.numberOfImages", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.estimatedResolutionInBestParts", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.estimatedResolutionInWorstParts", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.errors.symmetryApplied", is(not(isEmptyOrNullString()))))
        ;
    }

    @Test
    public void countMaps() throws Exception {
        getMockMvc().perform(get("/api/v1/project/" + PROJECT_SLUG_1 + "/map/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(entities.size())));
    }

    @NonNull
    public UploadResponse uploadFile(@NonNull final MockMultipartFile file) throws
            Exception {
        final MvcResult result = getMockMvc().perform(fileUpload(MAP_ATTACHMENTS_URL + "upload")
                .file(file))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readValue(result.getResponse().getContentAsByteArray(), UploadResponse.class);
    }

    @Test
    public void shouldUploadOrphanFile() throws Exception {
        //given
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                .getBytes());
        //when
        final UploadResponse uploadResponse = uploadFile(file);
        //then
        assertThat(uploadResponse.getMongoId(), is(not(isEmptyOrNullString())));
    }

    @Test
    public void shouldDeleteFile() throws Exception {
        //given
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                .getBytes());
        final UploadResponse result = uploadFile(file);

        //when
        getMockMvc().perform(delete(MAP_ATTACHMENTS_URL + result.getMongoId()))
                .andExpect(status().isOk());

        //then
        getMockMvc().perform(get(format(MAP_ATTACHMENTS_URL + "download/%s", result.getMongoId())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldRemoveFileAssociationFromMapOnFileDelete() throws Exception {
        //given
        final Map map = entities.get(0);
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                .getBytes());
        final UploadResponse uploadResponse = uploadFile(file);

        map.setAttachmentMongoId(uploadResponse.getMongoId());
        getMockMvc().perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json(map)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attachmentMongoId", is(not(isEmptyOrNullString()))));

        //when
        getMockMvc().perform(delete(MAP_ATTACHMENTS_URL + uploadResponse.getMongoId()))
                .andExpect(status().isOk());

        //then
        getMockMvc()
                .perform(get(URI + map.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.attachmentMongoId", isEmptyOrNullString()));
    }

    @Test
    public void shouldAssociateFileWithMap() throws Exception {
        //given
        final Map map = entities.get(0);
        final String originalFilename = "filename.txt";
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, originalFilename, "text/plain", "some text"
                .getBytes());
        final UploadResponse uploadResponse = uploadFile(file);

        //when
        map.setAttachmentMongoId(uploadResponse.getMongoId());
        final MvcResult result = getMockMvc().perform(put(URI).contentType(JSON_CONTENT_TYPE).content(json(map)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final ObjectMapper mapper = new ObjectMapper();
        final Map resultMap = mapper.readValue(result.getResponse()
                .getContentAsByteArray(), mapper.getTypeFactory()
                .constructType(Map.class));

        //then
        assertThat(resultMap.getAttachmentMongoId(), is(uploadResponse.getMongoId()));
    }

    @Test
    public void shouldDownloadFile() throws Exception {
        //given
        final String originalFilename = "filename.txt";
        final String fileContent = "some text";
        final String contentType = "text/plain";
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, originalFilename, contentType, fileContent
                .getBytes());
        UploadResponse result = uploadFile(file);

        //when
        final String url = format(MAP_ATTACHMENTS_URL + "download/%s", result.getMongoId());
        getMockMvc().perform(get(url))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", contentType))
                .andExpect(header().longValue("Content-Length", fileContent.length()))
                .andExpect(header().string("Content-Disposition", "attachment; filename=" + originalFilename))
                .andExpect(content().string(fileContent));
    }
}
