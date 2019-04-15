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

import com.gene.bioinfo.ms.gp2s.domain.Map;
import com.gene.bioinfo.ms.gp2s.domain.MapLink;
import com.gene.bioinfo.ms.gp2s.domain.MapRelationshipType;
import com.gene.bioinfo.ms.gp2s.repository.MapLinkRepository;
import com.gene.bioinfo.ms.gp2s.repository.MapRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MapLinkListValidatorTest {

    @Mock
    private MapRepository mapRepository;

    @Mock
    private MapLinkRepository mapLinkRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private Errors errors;

    private MapLinkListValidator tested;

    @Before
    public void setup() {
        tested = Mockito.spy(new MapLinkListValidator(projectRepository, mapRepository, mapLinkRepository));
    }

    @Test
    public void shouldAllowEmptyList() {
        //when
        tested.validate(new ArrayList<MapLink>(), errors);
        //then
        verify(errors, times(0)).reject(anyString());
    }

    @Test
    public void shouldNotAllowEmptyParentMap() {
        //given
        MapLink link = MapLink.builder().build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].parentMap"), anyString());
    }

    @Test
    public void shouldNotAllowEmptyParentMapId() {
        //given
        Map map = Map.builder().build();
        MapLink link = MapLink.builder().parentMap(map).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].parentMap.id"), anyString());
    }

    @Test
    public void shouldNotAllowNonExistingParentMap() {
        //given
        Long nonExistingId = 1L;
        Map map = Map.builder().id(nonExistingId).build();
        MapLink link = MapLink.builder().parentMap(map).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].parentMap.id"), anyString());
    }

    @Test
    public void shouldNotAllowEmptyChildMap() {
        //given
        Map map = Map.builder().id(1L).build();
        when(this.mapRepository.exists(1L)).thenReturn(true);
        when(this.mapRepository.findOne(1L)).thenReturn(map);
        MapLink link = MapLink.builder().parentMap(map).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].childMap"), anyString());
    }

    @Test
    public void shouldNotAllowEmptyChildMapId() {
        //given
        Long parentId = 1L;
        Map parent = Map.builder().id(parentId).build();
        when(this.mapRepository.exists(parentId)).thenReturn(true);
        when(this.mapRepository.findOne(parentId)).thenReturn(parent);
        Map map = Map.builder().build();
        MapLink link = MapLink.builder()
                .parentMap(parent)
                .childMap(map)
                .build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].childMap.id"), anyString());
    }

    @Test
    public void shouldNotAllowNonExistingChildMap() {
        //given
        Long nonExistingId = 1L;
        Map map = Map.builder().id(nonExistingId).build();
        MapLink link = MapLink.builder().childMap(map).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].childMap.id"), anyString());
    }

    @Test
    public void shouldNotAllowLinkMapToItself() {
        //given
        Map map = Map.builder().id(1L).build();
        when(this.mapRepository.exists(1L)).thenReturn(true);
        when(this.mapRepository.findOne(1L)).thenReturn(map);
        MapLink link = MapLink.builder().parentMap(map).childMap(map).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].childMap"), anyString());
    }

    @Test
    public void shouldNotAllowEmptyRelationshipType() {
        //given
        MapLink link = MapLink.builder().build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].relationshipType"), anyString());
    }

    @Test
    public void shouldCommentBeNotLongerThan255() {
        //given
        String comment = StringUtils.repeat("*", 256);
        MapLink link = MapLink.builder().comment(comment).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].comment"), anyString());
    }

    @Test
    public void shouldAcceptEmptyComment() {
        //given
        MapLink link = MapLink.builder().build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors, times(0)).rejectValue(eq("[0].comment"), anyString());
    }

    @Test
    public void shouldAcceptMapLink() {
        //given
        Long parentId = 1L;
        Long childId = 2L;
        Map parent = Map.builder().id(parentId).build();
        Map child = Map.builder().id(childId).build();
        when(this.mapRepository.exists(parentId)).thenReturn(true);
        when(this.mapRepository.findOne(parentId)).thenReturn(parent);
        when(this.mapRepository.exists(childId)).thenReturn(true);
        when(this.mapRepository.findOne(childId)).thenReturn(child);
        MapLink link = MapLink.builder()
                .parentMap(parent)
                .childMap(child)
                .relationshipType(MapRelationshipType.MASKED)
                .build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors, times(0)).rejectValue(anyString(), anyString());
        verify(errors, times(0)).reject(anyString());
    }

}
