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

package com.gene.bioinfo.ms.gp2s.web.base;

import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.service.base.RestService;
import lombok.NonNull;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Adds a {@link org.springframework.validation.Validator} to the {@link BaseRestController}
 *
 * @author Cezary Krzyżanowski on 10.08.2017.
 */
public abstract class BaseValidatingRestController<T extends BaseSlugAndLabelEntity, Service extends RestService<T>>
        extends BaseRestController<T, Service> {

    protected final Validator[] validators;

    protected BaseValidatingRestController(final Service service,
                                           @NonNull final Validator... validators) {
        super(service);
        this.validators = validators;
    }

    @InitBinder
    @SuppressWarnings("unused")
    protected void initBinder(@NonNull final WebDataBinder binder) {
        binder.addValidators(this.validators);
    }
}
