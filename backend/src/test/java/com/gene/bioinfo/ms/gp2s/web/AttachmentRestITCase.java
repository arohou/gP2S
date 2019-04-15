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

import com.gene.bioinfo.ms.gp2s.GP2SApplication;
import com.gene.bioinfo.ms.gp2s.domain.Attachment;
import com.gene.bioinfo.ms.gp2s.domain.Comment;
import com.gene.bioinfo.ms.gp2s.domain.CommentAttachment;
import com.gene.bioinfo.ms.gp2s.domain.GridType;
import com.gene.bioinfo.ms.gp2s.domain.base.EntityType;
import com.gene.bioinfo.ms.gp2s.service.attachment.UploadResponse;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.AdminEntitiesPreparer;
import com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.CommentPreparer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.gene.bioinfo.ms.gp2s.web.base.integration.preparers.CommentPreparer.COMMENT_ATTACHMENTS_URL;
import static com.google.common.collect.Iterables.getOnlyElement;
import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GP2SApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration-test")
@TestExecutionListeners({SpringBootDependencyInjectionTestExecutionListener.class})
public class AttachmentRestITCase {

    public static final String MULTIPART_FILE_NAME = "qqfile";

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private AdminEntitiesPreparer adminPreparer;
    private CommentPreparer commentPreparer;

    @Before
    public void initMockMvc() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.adminPreparer = new AdminEntitiesPreparer(this.mockMvc);
        this.commentPreparer = new CommentPreparer(this.mockMvc);
    }

    @After
    public void deleteAllCreated() {
        this.commentPreparer.deleteAllCreated();
        this.adminPreparer.deleteAllCreated();
    }

    @Test
    public void shouldUploadNewFiles() throws Exception {
        //given
        final GridType gridType = adminPreparer.prepareGridType();
        final Comment comment = commentPreparer.prepareComment(gridType);
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                .getBytes());

        //when
        final UploadResponse uploadResponse = commentPreparer.addAttachmentToComment(comment, file);

        //then
        assertThat(uploadResponse.getId(), is(not(nullValue())));
        assertThat(uploadResponse.getMongoId(), is(not(isEmptyOrNullString())));
    }

    @Test
    public void shouldAssociateFileWithComment() throws Exception {
        //given
        final GridType gridType = adminPreparer.prepareGridType();
        final Comment comment = commentPreparer.prepareComment(gridType);
        final String originalFilename = "filename.txt";
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, originalFilename, "text/plain", "some text"
                .getBytes());
        final UploadResponse uploadResponse = commentPreparer.addAttachmentToComment(comment, file);

        //when
        final Comment commentAfterAddingAttachment = getOnlyElement(commentPreparer.getCommentsFor(gridType));

        //then
        CommentAttachment attachment = getOnlyElement(commentAfterAddingAttachment.getAttachments());
        assertThat(attachment.getId(), is(uploadResponse.getId()));
        assertThat(attachment.getMongoId(), is(uploadResponse.getMongoId()));
        assertThat(attachment.getFileName(), is(originalFilename));
    }

    @Test
    public void shouldAssociateTwoFileWithComment() throws Exception {
        //given
        final GridType gridType = adminPreparer.prepareGridType();
        final Comment comment = commentPreparer.prepareComment(gridType);
        final String firstFileName = "first_file.txt";
        final String secondFileName = "second_file.txt";
        final MockMultipartFile firstFile = new MockMultipartFile(MULTIPART_FILE_NAME, firstFileName,
                "text/plain", ("someext").getBytes());
        final MockMultipartFile secondFile = new MockMultipartFile(MULTIPART_FILE_NAME, secondFileName,
                "text/plain", ("someext").getBytes());
        commentPreparer.addAttachmentToComment(comment, firstFile);
        commentPreparer.addAttachmentToComment(comment, secondFile);

        //when
        final Comment commentAfterAddingAttachments = getOnlyElement(commentPreparer.getCommentsFor(gridType));

        //then
        assertThat(commentAfterAddingAttachments.getAttachments(), hasSize(2));

        // Newest attachment should be first on the list.
        final Attachment firstAttachment = commentAfterAddingAttachments.getAttachments().get(1);
        final Attachment secondAttachment = commentAfterAddingAttachments.getAttachments().get(0);

        assertThat(firstAttachment.getFileName(), is(firstFileName));
        assertThat(secondAttachment.getFileName(), is(secondFileName));
    }

    @Test
    public void shouldDownloadFile() throws Exception {
        //given
        final GridType gridType = adminPreparer.prepareGridType();
        final Comment comment = commentPreparer.prepareComment(gridType);
        final String originalFilename = "filename.txt";
        final String fileContent = "some text";
        final String contentType = "text/plain";
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, originalFilename, contentType, fileContent
                .getBytes());
        commentPreparer.addAttachmentToComment(comment, file);
        final List<Comment> comments = commentPreparer.getCommentsFor(gridType);
        final Attachment attachment = getOnlyElement(getOnlyElement(comments).getAttachments());

        final String url = format(COMMENT_ATTACHMENTS_URL + "download/%s", attachment.getMongoId());
        //when
        mockMvc.perform(get(url))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", contentType))
                .andExpect(header().longValue("Content-Length", fileContent.length()))
                .andExpect(header().string("Content-Disposition", "attachment; filename=" + originalFilename))
                .andExpect(content().string(fileContent));
    }

    @Test
    public void shouldRecognizeMsDocContentType() throws Exception {
        //given
        final GridType gridType = adminPreparer.prepareGridType();
        final Comment comment = commentPreparer.prepareComment(gridType);
        final String msDocFileName = "test_doc.docx";
        final String msDocMimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        final byte[] msDocFileContent = getBytes(msDocFileName);

        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, msDocFileName, msDocMimeType, msDocFileContent);
        commentPreparer.addAttachmentToComment(comment, file);

        final List<Comment> comments = commentPreparer.getCommentsFor(gridType);
        final Attachment attachment = getOnlyElement(getOnlyElement(comments).getAttachments());

        final String url = format(COMMENT_ATTACHMENTS_URL + "download/%s", attachment.getMongoId());

        //when
        mockMvc.perform(get(url))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", msDocMimeType))
                .andExpect(header().longValue("Content-Length", msDocFileContent.length))
                .andExpect(header().string("Content-Disposition", "attachment; filename=" + msDocFileName))
                .andExpect(content().bytes(msDocFileContent));
    }

    private byte[] getBytes(final String msDocFileName) throws IOException {
        final File msDocFile = new File(getClass().getClassLoader()
                .getResource(msDocFileName)
                .getFile());
        return Files.readAllBytes(Paths.get(msDocFile.toURI()));
    }

    @Test
    public void shouldThrowNotFoundWhenEntityIDisInvalid() throws Exception {
        //given
        final String fakeCommentId = String.valueOf(Integer.MAX_VALUE);
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                .getBytes());

        //when
        mockMvc.perform(fileUpload(COMMENT_ATTACHMENTS_URL + "upload")
                .file(file)
                .param("entityType", EntityType.COMMENT.name())
                .param("entityId", fakeCommentId))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldThrowBadRequestWhenEntityTypeIsInvalid() throws Exception {
        //given
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                .getBytes());

        //when
        mockMvc.perform(fileUpload(COMMENT_ATTACHMENTS_URL + "upload")
                .file(file)
                .param("entityType", "INVALID_ENTITY_TYPE")
                .param("entityId", "1"))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldDeleteFile() throws Exception {
        //given
        final GridType gridType = adminPreparer.prepareGridType();
        final Comment comment = commentPreparer.prepareComment(gridType);
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                .getBytes());
        commentPreparer.addAttachmentToComment(comment, file);
        final List<Comment> comments = commentPreparer.getCommentsFor(gridType);
        final Attachment attachment = getOnlyElement(getOnlyElement(comments).getAttachments());

        //when
        mockMvc.perform(delete(COMMENT_ATTACHMENTS_URL + attachment.getId()))
                .andExpect(status().isOk());

        //then
        final String url = format(COMMENT_ATTACHMENTS_URL + "download/%s", attachment.getMongoId());
        mockMvc.perform(get(url))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteAttachmentFromComment() throws Exception {
        //given
        final GridType gridType = adminPreparer.prepareGridType();
        final Comment comment = commentPreparer.prepareComment(gridType);
        final MockMultipartFile file = new MockMultipartFile(MULTIPART_FILE_NAME, "filename.txt", "text/plain", "some text"
                .getBytes());
        final UploadResponse uploadResponse = commentPreparer.addAttachmentToComment(comment, file);
        mockMvc.perform(delete(COMMENT_ATTACHMENTS_URL + uploadResponse.getId()))
                .andExpect(status().isOk());

        //when
        final Comment commentFetchedAfterDeletingAttachment = getOnlyElement(commentPreparer.getCommentsFor(gridType));

        //then
        assertThat(commentFetchedAfterDeletingAttachment, is(not(nullValue())));
        assertThat(commentFetchedAfterDeletingAttachment.getAttachments(), is(empty()));
    }

    @Test
    public void shouldReturnNotFoundForNonExistingFile() throws Exception {
        //given

        //when
        final String url = format(COMMENT_ATTACHMENTS_URL + "download/%s", "FAKE_MONGODB_ID");
        mockMvc.perform(get(url))
                //then
                .andExpect(status().isNotFound());
    }
}
