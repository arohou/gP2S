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

package com.gene.bioinfo.ms.gp2s.service.base;

import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import com.netflix.config.validation.ValidationException;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRestServiceTest {

    class MockService extends BaseRestService<MockEntity> {

        protected MockService(BaseSlugRepository<MockEntity> repository) {
            super(repository);
        }
    }

    @Mock
    BaseSlugRepository<MockEntity> mockRepository;

    private MockService put;

    // The test label should end with a number in order to test if name collisions are properly handled.
    private static final String TEST_LABEL = "A Test Label 123";
    private static final String TEST_SLUG = "a-test-label-123";
    private static final String TEST_LABEL_SPECIAL_CHARACTERS = "''()+.,/ $|-=?'''  ";
    private static final String TEST_LABEL_NUMERIC = "123";
    private static final String TEST_LABEL_NUMERIC_AND_SPACES = TEST_LABEL_NUMERIC + "    ";

    @Before
    public void setUp() throws Exception {
        this.put = new MockService(mockRepository);
    }

    @Test(expected = ValidationException.class)
    public void forbidsCreatingEntityWithId() throws Exception {
        MockEntity mockEntity = MockEntity.builder().id(1L).build();
        when(mockRepository.save(mockEntity)).thenReturn(mockEntity);
        assertSame(this.put.createItem(mockEntity), mockEntity);
    }

    @Test(expected = ValidationException.class)
    public void forbidsCreatingEntityWithSlug() throws Exception {
        MockEntity mockEntity = MockEntity.builder().slug(TEST_SLUG).build();
        when(mockRepository.save(mockEntity)).thenReturn(mockEntity);
        assertSame(mockEntity, this.put.createItem(mockEntity));
    }

    @Test
    public void fillsSlugWhenCreating() {
        MockEntity mockEntity = MockEntity.builder().label(TEST_LABEL).build();
        when(mockRepository.save(mockEntity)).thenReturn(mockEntity);

        final MockEntity savedEntity = this.put.createItem(mockEntity);
        assertEquals(TEST_LABEL, savedEntity.getLabel());
        assertEquals(TEST_SLUG, savedEntity.getSlug());
    }

    @Test
    public void createSpecialSlugWhenLabelIsANumber() {
        //given
        final String LABEL = "123";
        final String SLUG = "item-123";
        MockEntity mockEntity = MockEntity.builder().label(LABEL).build();
        when(mockRepository.save(mockEntity)).thenReturn(mockEntity);

        //when
        final MockEntity savedEntity = this.put.createItem(mockEntity);

        //then
        assertEquals(SLUG, savedEntity.getSlug());
    }

    @Test
    public void handlesCollidingSlugsWhenCreating() {
        final MockEntity mockEntity = MockEntity.builder()
                                                .label(TEST_LABEL)
                                                .build();
        when(mockRepository.save(mockEntity)).thenReturn(mockEntity);

        when(mockRepository.findOneBySlug(TEST_SLUG)).thenReturn(
                MockEntity.builder()
                          .label(TEST_LABEL)
                          .slug(TEST_SLUG)
                          .build());

        final MockEntity savedEntity = this.put.createItem(mockEntity);
        assertEquals(TEST_LABEL, savedEntity.getLabel());
        assertEquals(TEST_SLUG + "-1", savedEntity.getSlug());
    }

    @Test
    public void handlesSecondSlugCollisionWhenCreating() {
        final MockEntity mockEntity = MockEntity.builder()
                                                .label(TEST_LABEL)
                                                .build();
        when(mockRepository.findOneBySlug(TEST_SLUG)).thenReturn(
                MockEntity.builder()
                          .label(TEST_LABEL)
                          .slug(TEST_SLUG)
                          .build());

        final MockEntity firstConflictedEntity =
                MockEntity.builder()
                          .id(2L)
                          .label(TEST_LABEL)
                          .slug(TEST_SLUG + "-1")
                          .build();
        when(mockRepository.findOneBySlug(firstConflictedEntity.getSlug())).thenReturn(
                firstConflictedEntity);

        when(mockRepository.save(mockEntity)).thenReturn(mockEntity);

        final MockEntity savedEntity = this.put.createItem(mockEntity);
        assertEquals(TEST_LABEL, savedEntity.getLabel());
        assertEquals(TEST_SLUG + "-2", savedEntity.getSlug());
    }

    @Test
    public void createsSlugForGivenLabel() {
        MockEntity savedEntity = put.createSlug(MockEntity.builder().label(TEST_LABEL).build());
        assertEquals(savedEntity.getSlug(), TEST_SLUG);

        savedEntity = put.createSlug(MockEntity.builder().label(TEST_LABEL_SPECIAL_CHARACTERS).build());
        assertEquals(savedEntity.getSlug(), BaseRestService.GENERIC_PREFIX);

        savedEntity = put.createSlug(MockEntity.builder().label(TEST_LABEL_NUMERIC).build());
        assertEquals(savedEntity.getSlug(), BaseRestService.GENERIC_PREFIX + "-" + TEST_LABEL_NUMERIC);

        savedEntity = put.createSlug(MockEntity.builder().label(TEST_LABEL_NUMERIC_AND_SPACES).build());
        assertEquals(savedEntity.getSlug(), BaseRestService.GENERIC_PREFIX + "-" + TEST_LABEL_NUMERIC);
    }
}

@Data
@EqualsAndHashCode(callSuper = true)
class MockEntity extends BaseSlugAndLabelEntity {
    @Builder
    public MockEntity(Long id, String label, String slug, Integer version,
                      Date createdDate, Date modifiedDate, String createdBy, String modifiedBy) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
    }
}
