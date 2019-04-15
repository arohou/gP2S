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

package com.gene.bioinfo.ms.gp2s.web.base.integration.preparers;

import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugEntity;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.reverse;

public class DataPreparer {
    protected MockMvc mockMvc;
    @Getter
    protected MockMvcExecutor mockMvcExecutor;
    @Getter
    protected List<BaseSlugEntity> allItemsCreated = new ArrayList<>();

    public DataPreparer(@NonNull final MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.mockMvcExecutor = new MockMvcExecutor(mockMvc);
    }

    /**
     * I created new list, since reverse method only creates a wrapper around the existing list.
     * So when iterating reversed list I access the original list. Since, in this case, delete method deletes items
     * from original list I had to make a new list to iterate over.
     */
    public void deleteAllCreated() {
        final List<BaseSlugEntity> reversedItems = new ArrayList<>(reverse(allItemsCreated));
        reversedItems.forEach(this::delete);
    }

    public <T> T get(@NonNull final String url, @NonNull final Class<T> clazz) {
        return this.mockMvcExecutor.get(url, clazz);
    }

    public <T extends BaseSlugEntity> T post(@NonNull final T entity) {
        @NonNull final T createdEntity = this.mockMvcExecutor.post(entity);
        this.allItemsCreated.add(createdEntity);
        return createdEntity;
    }

    public <T extends BaseSlugEntity> T put(@NonNull final T entity) {
        @NonNull final T updatedEntity = this.mockMvcExecutor.put(entity);
        //remove from list old version of entity
        this.allItemsCreated.remove(updatedEntity);
        //add to list new version of entity
        this.allItemsCreated.add(updatedEntity);
        return updatedEntity;
    }

    public <T extends BaseSlugEntity> void delete(@NonNull final T entity) {
        this.mockMvcExecutor.delete(entity);
        this.allItemsCreated.remove(entity);
    }
}
