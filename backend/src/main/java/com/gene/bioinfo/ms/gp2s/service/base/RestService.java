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

import com.gene.bioinfo.ms.gp2s.domain.base.BaseAuditableEntity;

import java.util.Collection;

/**
 * Base class for REST entity services
 *
 * @author Cezary Krzyżanowski on 10.08.2017.
 */
public interface RestService<T extends BaseAuditableEntity> {

    T createItem(final T input);

    Collection<T> getItems();

    T getItem(final String slug);

    T getItem(final Long id);

    T updateItem(final T input);

    void deleteItem(final String slug);

    void deleteItem(final Long id);
}
