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

import com.gene.bioinfo.ms.gp2s.domain.Model;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import com.gene.bioinfo.ms.gp2s.repository.ModelRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Przemysław Stankowski on 24.04.2018
 */
@RunWith(MockitoJUnitRunner.class)
public class ModelAttachmentServiceTest {

    final String MONGO_ID = "XYZ123";
    private ModelAttachmentService tested;
    @Mock
    private ModelRepository modelRepository;
    @Mock
    private FileRepository fileRepository;
    private List<Model> models;

    @Before
    public void setup() {
        tested = new ModelAttachmentService(fileRepository, modelRepository);

        //common given
        final Model.ModelBuilder builder = Model.builder()
                .id(1L)
                .label("label")
                .attachmentMongoId(MONGO_ID)
                .attachmentFileName("file_name");
        models = Stream.of(builder.build(), builder.build()).collect(Collectors.toList());
        Mockito.when(modelRepository.findAllByAttachmentMongoId(MONGO_ID)).thenReturn(models);
    }

    @Test
    public void shouldRemoveAttachmentInfoFromModel_SetNullAsMongoId() {
        //when
        tested.removeAttachmentInfoFromMaps(modelRepository, MONGO_ID);
        //then
        models.forEach(map -> Assert.assertNull(map.getAttachmentMongoId()));
    }

    @Test
    public void shouldRemoveAttachmentInfoFromModel_SetNullAsFileName() {
        //when
        tested.removeAttachmentInfoFromMaps(modelRepository, MONGO_ID);
        //then
        models.forEach(map -> Assert.assertNull(map.getAttachmentFileName()));
    }

    @Test
    public void shouldNotRemoveAttachmentInfoFromModel() {
        //when
        tested.removeAttachmentInfoFromMaps(modelRepository, "DIFFERENT_MONGO_ID");
        //then
        models.forEach(map -> Assert.assertNotNull(map.getAttachmentFileName()));
        models.forEach(map -> Assert.assertNotNull(map.getAttachmentMongoId()));
    }
}
