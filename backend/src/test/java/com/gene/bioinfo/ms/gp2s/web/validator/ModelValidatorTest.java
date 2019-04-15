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

package com.gene.bioinfo.ms.gp2s.web.validator;

import com.gene.bioinfo.ms.gp2s.domain.Model;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import com.gene.bioinfo.ms.gp2s.service.attachment.File;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Cezary Krzyżanowski on 22.06.2018
 */
@RunWith(MockitoJUnitRunner.class)
public class ModelValidatorTest {

    @Mock
    private Errors errors;

    @Mock
    private FileRepository fileRepository;

    private ModelValidator tested;

    @Before
    public void setup() {
        tested = new ModelValidator(fileRepository);
    }

    @Test
    public void shouldRequiteAttachmentMongoId() {
        //given
        final Model.ModelBuilder builder = Model.builder();
        //when
        tested.validate(builder.build(), errors);
        //then
        verify(errors).rejectValue(eq("attachmentMongoId"), anyString());
    }

    @Test
    public void shouldAcceptAttachmentMongoIdAndFileName() {
        //given
        final String mongoId = "file_id";
        final Model.ModelBuilder builder = Model.builder()
                .attachmentMongoId(mongoId)
                .attachmentFileName("file_name");
        final File file = Mockito.mock(File.class);
        Mockito.when(fileRepository.findOne(mongoId)).thenReturn(file);
        //when
        tested.validate(builder.build(), errors);
        //then
        verify(errors, times(0)).rejectValue(eq("attachmentMongoId"), anyString());
        verify(errors, times(0)).rejectValue(eq("attachmentFileName"), anyString());
    }

    @Test
    public void shouldRequiteAttachmentFileName() {
        //given
        final Model.ModelBuilder builder = Model.builder();
        //when
        tested.validate(builder.build(), errors);
        //then
        verify(errors).rejectValue(eq("attachmentFileName"), anyString());
    }

    @Test
    public void shouldRequiteFileToBeAlreadyStored() {
        //given
        final Model.ModelBuilder builder = Model.builder()
                .attachmentMongoId("not_existing_file_id")
                .attachmentFileName("file_name");
        //when
        tested.validate(builder.build(), errors);
        //then
        verify(errors).rejectValue(eq("attachmentMongoId"), anyString());
    }
}
