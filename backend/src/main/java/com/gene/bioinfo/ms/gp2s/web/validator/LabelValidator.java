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

import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import lombok.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotEmpty;

public abstract class LabelValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BaseSlugAndLabelEntity.class.isAssignableFrom(clazz);
    }

    @Override
    public final void validate(@NonNull final Object target, @NonNull final Errors e) {
        validateLabel((BaseSlugAndLabelEntity) target, e);
        doValidate(target, e);
    }

    protected void validateLabel(@NonNull final BaseSlugAndLabelEntity baseSlugAndLabelEntity,
                                 @NonNull final Errors e) {
        final String label = baseSlugAndLabelEntity.getLabel();
        isNotEmpty(label, "label", "Label",
                DomainConstants.SHORT_STRING_LENGTH, e);
    }

    protected abstract void doValidate(final Object target, final Errors e);
}
