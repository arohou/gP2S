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

package com.gene.bioinfo.ms.gp2s.service;

import com.gene.bioinfo.ms.gp2s.domain.DefaultValue;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.base.Persistable;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefault;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultComposite;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultDefinition;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultType;
import com.gene.bioinfo.ms.gp2s.repository.DefaultValueRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class DefaultValueServiceTest {

    @Mock
    private DefaultValueRepository repository;

    @Mock
    private ProjectRepository projectRepository;

    private DefaultValueService tested;

    private Project project;

    @Before
    public void setup() {
        tested = new DefaultValueService(repository, projectRepository);
        project = Project.builder().id(1L).build();
        Mockito.when(projectRepository.existsBySlug(Mockito.anyString())).thenReturn(true);
        Mockito.when(projectRepository.exists(Mockito.any(Long.class))).thenReturn(true);
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotAcceptWhenMissingTypeAnnotation() {
        tested.saveDefaultValues(Project.builder().build(), new Foo());
    }

    @Test
    public void shouldNotSaveNotAnnotatedFields() {
        tested.saveDefaultValues(Project.builder().build(), new Bar(1L, null, null, null, null, "test", null, null));
        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any(DefaultValue.class));
    }

    @Test
    public void shouldSaveValuesAsStrings() {
        tested.saveDefaultValues(project, new Bar(null, "val1", 2L, 3.0f, null, null, null, null));

        Mockito.verify(repository, Mockito.times(1))
                .save(Matchers.refEq(DefaultValue.builder()
                        .entityType(DefaultValue.DefaultValueType.GRID)
                        .project(project)
                        .fieldName("test1")
                        .value("val1")
                        .build()));
        Mockito.verify(repository, Mockito.times(1))
                .save(Matchers.refEq(DefaultValue.builder()
                        .entityType(DefaultValue.DefaultValueType.GRID)
                        .project(project)
                        .fieldName("test2")
                        .value("2")
                        .build()));
        Mockito.verify(repository, Mockito.times(1))
                .save(Matchers.refEq(DefaultValue.builder()
                        .entityType(DefaultValue.DefaultValueType.GRID)
                        .project(project)
                        .fieldName("test3")
                        .value("3.0")
                        .build()));
    }

    @Test
    public void shouldSaveOnlyIdOfObjectAtField() {
        tested.saveDefaultValues(project, new Bar(null, null, null, null, new Foo(5L), null, null, null));
        Mockito.verify(repository, Mockito.times(1))
                .save(Matchers.refEq(DefaultValue.builder()
                        .entityType(DefaultValue.DefaultValueType.GRID)
                        .project(project)
                        .fieldName("test4")
                        .value("5")
                        .build()));
    }

    @Test
    public void shouldUpdateDefaultValueIfExists() {
        DefaultValue dv = Mockito.mock(DefaultValue.class);
        Mockito.when(repository.findByEntityTypeAndProject_IdAndFieldName(DefaultValue.DefaultValueType.GRID, project.getId(), "test1")).thenReturn(dv);
        tested.saveDefaultValues(project, new Bar(null, "val1", null, null, null, null, null, null));

        Mockito.verify(dv, Mockito.times(1)).setValue("val1");

        Mockito.verify(repository, Mockito.times(1)).save(dv);
    }

    @Test
    public void shouldCreateAndSaveCompositeDefaultValuesBeExecutedForAllCompositeDefinition() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //given
        Project project = Mockito.mock(Project.class);
        DefaultValue.DefaultValueType type = DefaultValue.DefaultValueType.PROCESSING_SESSION;
        Bar entity = new Bar(null, null, null, null, null, null, new Foo[]{new Foo(1L)}, null);
        Field field = Bar.class.getDeclaredField("array");

        //when
        tested.createAndSaveCompositeDefaultValues(project, type, entity, field);

        //then
        Mockito.verify(repository, Mockito.times(2)).save(Mockito.any(DefaultValue.class));
    }

    @Test
    public void shouldProcessBuildValue1() throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        //given
        Bar bar = new Bar(
                null,
                null,
                null,
                null,
                new Foo(1L),
                null,
                null,
                null);
        //when
        String res = tested.process(bar, "${id}_${test4.id}_test");
        //then
        Assert.assertEquals("null_1_test", res);
    }

    @Test
    public void shouldProcessBuildValue2() throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        //given
        Bar bar = new Bar(
                null,
                null,
                null,
                null,
                null,
                null,
                new Foo[]{new Foo(1L)},
                null);
        //when
        String res = tested.process(bar, "${id}_${array[0].id}_test");
        //then
        Assert.assertEquals("null_1_test", res);
    }

    @Test
    public void shouldProcessBuildValue3() throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        //given
        Bar bar = new Bar(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Collections.singletonList(new Foo(1L)));
        //when
        String res = tested.process(bar, "${id}_${list[0].id}_test");
        //then
        Assert.assertEquals("null_1_test", res);

    }

    @Test
    public void shouldGroupByPropertyMakeSubLevelByDot() {
        //given
        Map<String, Object> map = new HashMap<>();
        map.put("test.key1", "value1");
        map.put("test.key2", "value2");

        //when
        Map<String, Object> res = tested.groupByProperty(map);

        //then
        Assert.assertTrue(res.containsKey("test"));
        Map<String, Object> subLevel = (Map<String, Object>) res.get("test");
        Assert.assertTrue(subLevel.containsKey("key1"));
        Assert.assertTrue(subLevel.containsKey("key2"));

        Assert.assertEquals("value1", subLevel.get("key1"));
        Assert.assertEquals("value2", subLevel.get("key2"));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Foo implements Persistable<Long> {
        private Long id;
    }

    @Data
    @AllArgsConstructor
    @GP2SDefaultType(DefaultValue.DefaultValueType.GRID)
    public class Bar implements Persistable<Long> {
        private Long id;
        @GP2SDefault
        private String test1;
        @GP2SDefault
        private Long test2;
        @GP2SDefault
        private Float test3;
        @GP2SDefault
        private Foo test4;
        private String nutOfTest;
        @GP2SDefaultComposite(definition = {
                @GP2SDefaultDefinition(key = "array_test1", value = "${array[0].id}"),
                @GP2SDefaultDefinition(key = "array_test2", value = "${array[0].id}")
        })
        private Foo[] array;
        private List<Foo> list;
    }
}
