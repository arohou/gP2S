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

import com.gene.bioinfo.ms.gp2s.domain.Map;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import com.gene.bioinfo.ms.gp2s.repository.MapRepository;
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
 * @author Przemysław Stankowski on 17.04.2018
 */
@RunWith(MockitoJUnitRunner.class)
public class MapAttachmentServiceTest {

    final String MONGO_ID = "XYZ123";
    private MapAttachmentService tested;
    @Mock
    private MapRepository mapRepository;
    @Mock
    private FileRepository fileRepository;
    private List<Map> maps;

    @Before
    public void setup() {
        tested = new MapAttachmentService(fileRepository, mapRepository);

        //common given
        final Map.MapBuilder builder = Map.builder()
                .id(1L)
                .label("label")
                .attachmentMongoId(MONGO_ID)
                .attachmentFileName("file_name");
        maps = Stream.of(builder.build(), builder.build()).collect(Collectors.toList());
        Mockito.when(mapRepository.findAllByAttachmentMongoId(MONGO_ID)).thenReturn(maps);
    }

    @Test
    public void shouldRemoveAttachmentInfoFromMapsSetNullAsMongoId() {
        //when
        tested.removeAttachmentInfoFromMaps(mapRepository, MONGO_ID);
        //then
        maps.forEach(map -> Assert.assertNull(map.getAttachmentMongoId()));
    }

    @Test
    public void shouldRemoveAttachmentInfoFromMapsSetNullAsFileName() {
        //when
        tested.removeAttachmentInfoFromMaps(mapRepository, MONGO_ID);
        //then
        maps.forEach(map -> Assert.assertNull(map.getAttachmentFileName()));
    }

    @Test
    public void shouldNotRemoveAttachmentInfoFromMaps() {
        //when
        tested.removeAttachmentInfoFromMaps(mapRepository, "DIFFERENT_MONGO_ID");
        //then
        maps.forEach(map -> Assert.assertNotNull(map.getAttachmentFileName()));
        maps.forEach(map -> Assert.assertNotNull(map.getAttachmentMongoId()));
    }
}
