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
import com.gene.bioinfo.ms.gp2s.domain.ModelLink;
import com.gene.bioinfo.ms.gp2s.domain.ModelRelationshipType;
import com.gene.bioinfo.ms.gp2s.repository.ModelLinkRepository;
import com.gene.bioinfo.ms.gp2s.repository.ModelRepository;
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
public class ModelLinkListValidatorTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private ModelLinkRepository modelLinkRepository;

    @Mock
    private Errors errors;

    private ModelLinkListValidator tested;

    @Before
    public void setup() {
        tested = Mockito.spy(new ModelLinkListValidator(modelLinkRepository, modelRepository));
    }

    @Test
    public void shouldAllowEmptyList() {
        //when
        tested.validate(new ArrayList<ModelLink>(), errors);
        //then
        verify(errors, times(0)).reject(anyString());
    }

    @Test
    public void shouldNotAllowEmptyParentModel() {
        //given
        ModelLink link = ModelLink.builder().build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].parentModel"), anyString());
    }

    @Test
    public void shouldNotAllowEmptyParentModelId() {
        //given
        Model model = Model.builder().build();
        ModelLink link = ModelLink.builder().parentModel(model).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].parentModel.id"), anyString());
    }

    @Test
    public void shouldNotAllowNonExistingParentModel() {
        //given
        Long nonExistingId = 1L;
        Model model = Model.builder().id(nonExistingId).build();
        ModelLink link = ModelLink.builder().parentModel(model).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].parentModel.id"), anyString());
    }

    @Test
    public void shouldNotAllowEmptyChildModel() {
        //given
        Model model = Model.builder().id(1L).build();
        when(this.modelRepository.exists(1L)).thenReturn(true);
        when(this.modelRepository.findOne(1L)).thenReturn(model);
        ModelLink link = ModelLink.builder().parentModel(model).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].childModel"), anyString());
    }

    @Test
    public void shouldNotAllowEmptyChildModelId() {
        //given
        Long parentId = 1L;
        Model parent = Model.builder().id(parentId).build();
        when(this.modelRepository.exists(parentId)).thenReturn(true);
        when(this.modelRepository.findOne(parentId)).thenReturn(parent);
        Model model = Model.builder().build();
        ModelLink link = ModelLink.builder()
                .parentModel(parent)
                .childModel(model)
                .build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].childModel.id"), anyString());
    }

    @Test
    public void shouldNotAllowNonExistingChildModel() {
        //given
        Long nonExistingId = 1L;
        Model model = Model.builder().id(nonExistingId).build();
        ModelLink link = ModelLink.builder().childModel(model).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].childModel.id"), anyString());
    }

    @Test
    public void shouldNotAllowLinkModelToItself() {
        //given
        Model model = Model.builder().id(1L).build();
        when(this.modelRepository.exists(1L)).thenReturn(true);
        when(this.modelRepository.findOne(1L)).thenReturn(model);
        ModelLink link = ModelLink.builder().parentModel(model).childModel(model).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].childModel"), anyString());
    }

    @Test
    public void shouldNotAllowEmptyRelationshipType() {
        //given
        ModelLink link = ModelLink.builder().build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].relationshipType"), anyString());
    }

    @Test
    public void shouldCommentBeNotLongerThan255() {
        //given
        String comment = StringUtils.repeat("*", 256);
        ModelLink link = ModelLink.builder().comment(comment).build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors).rejectValue(eq("[0].comment"), anyString());
    }

    @Test
    public void shouldAcceptEmptyComment() {
        //given
        ModelLink link = ModelLink.builder().build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors, never()).rejectValue(eq("[0].comment"), anyString());
    }

    @Test
    public void shouldAcceptModelLink() {
        //given
        Long parentId = 1L;
        Long childId = 2L;
        Model parent = Model.builder().id(parentId).build();
        Model child = Model.builder().id(childId).build();
        when(this.modelRepository.exists(parentId)).thenReturn(true);
        when(this.modelRepository.findOne(parentId)).thenReturn(parent);
        when(this.modelRepository.exists(childId)).thenReturn(true);
        when(this.modelRepository.findOne(childId)).thenReturn(child);
        ModelLink link = ModelLink.builder()
                .parentModel(parent)
                .childModel(child)
                .relationshipType(ModelRelationshipType.REFINED)
                .build();
        //when
        tested.validate(Collections.singletonList(link), errors);
        //then
        verify(errors, times(0)).rejectValue(anyString(), anyString());
        verify(errors, times(0)).reject(anyString());
    }

}
