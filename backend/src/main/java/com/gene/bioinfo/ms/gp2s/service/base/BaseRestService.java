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
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import com.gene.bioinfo.ms.gp2s.util.Validate;
import com.github.slugify.Slugify;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Base implementation of the {@link RestService}
 *
 * @author Cezary Krzyżanowski on 10.08.2017.
 */
public abstract class BaseRestService<T extends BaseSlugAndLabelEntity> implements RestService<T> {
    public static final String GENERIC_PREFIX = "item";
    private static final Slugify SLUGIFY = new Slugify();
    protected final BaseSlugRepository<T> repository;

    protected BaseRestService(@NonNull final BaseSlugRepository<T> repository) {
        this.repository = repository;
    }

    @NonNull
    public T createItem(@NonNull final T input) {
        commonCreateItemValidations(input);
        final T slugifiedInput = this.createSlug(input);
        return this.repository.save(slugifiedInput);
    }

    @NonNull
    public Collection<T> getItems() {
        // Sorting over primary index will be always more efficient and faster. It is related with internal data
        // structure. If table has primary index then table data are stored in that index. Sorting over
        // additional field will involve reading additional index, reference to data and data itself
        // (including primary index) and because every new created object should have higher id
        // (id id incremented automatically) we can use is as creation order as well
        return Optional.ofNullable(this.repository.findAllByOrderByIdDesc())
                .map(items -> {
                    items.forEach(this::postLoadItem);
                    return items;
                })
                .orElse(Collections.emptyList());
    }

    @NonNull
    protected T postLoadItem(@NonNull final T item) {
        return item;
    }

    @NonNull
    public T getItem(@NonNull final String slug) {
        return postLoadItem(Optional.ofNullable(this.repository.findOneBySlug(slug))
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found for slug: " + slug)));
    }

    @NonNull
    public T getItem(@NonNull final Long id) {
        return postLoadItem(Optional.ofNullable(this.repository.findOne(id))
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found for id: " + id)));
    }

    protected void commonCreateItemValidations(final T input) {
        Validate.isNull(input.getId(), "Can't create new object if ID is already initialized");
        Validate.isNullOrEmpty(input.getSlug(), "Can't create new object if Slug is already initialized");
    }

    protected void commonUpdateItemValidations(final T input) {
        Validate.notNull(input.getId(), "Looks like new object. ID is not initialized.");
        Validate.notBlank(input.getSlug(), "Looks like new object. Slug is not initialized.");
    }

    @NonNull
    public T updateItem(@NonNull final T input) {
        commonUpdateItemValidations(input);
        T itemToUpdate = this.repository.findOne(input.getId());
        if (itemToUpdate == null) {
            throw new ResourceNotFoundException(
                    "No item with that id in the DB: " + input.getId());
        } else if (!itemToUpdate.getLabel().equals(input.getLabel())) {
            // The label has changed. We need to update the slug.
            itemToUpdate = createSlug(input);
        } else {
            itemToUpdate = input;
        }

        return this.repository.save(itemToUpdate);
    }

    public void deleteItem(String slug) {
        // FIXME: Cascade deleting.
        this.repository.deleteBySlug(slug);
    }

    public void deleteItem(Long id) {
        // FIXME: Cascade deleting.
        this.repository.delete(id);
    }

    @NonNull
    protected T createSlug(@NonNull final T input) {
        final String baseSlug = basicSlugFromLabel(input.getLabel());
        final String slug = !isSlugAlreadyUsed(baseSlug) ? baseSlug : addSuffix(baseSlug, 1);
        input.setSlug(slug);
        return input;
    }

    @NonNull
    private String basicSlugFromLabel(@NonNull final String label) {
        @NonNull final String slug = SLUGIFY.slugify(label);

        if (slug.length() == 0) {
            return GENERIC_PREFIX;
        }
        if (slug.matches("^[0-9]+$")) {
            return GENERIC_PREFIX + '-' + slug;
        }
        if (slug.substring(0,1).matches("\\d")) {
            return GENERIC_PREFIX + "-" + slug;
        }
        return slug;
    }

    @NonNull
    private String addSuffix(@NonNull final String slug, final int suffix) {
        final String suffixedSlug = slug + "-" + suffix;
        if (!isSlugAlreadyUsed(suffixedSlug)) {
            return suffixedSlug;
        }

        return addSuffix(slug, suffix + 1);
    }

    private boolean isSlugAlreadyUsed(@NonNull String baseSlug) {
        return this.repository.findOneBySlug(baseSlug) != null;
    }

}
