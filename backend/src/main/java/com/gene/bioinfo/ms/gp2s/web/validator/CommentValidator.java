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

import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;

@Component
public class CommentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz) || ArrayList.class.isAssignableFrom(clazz);
    }

    @Override
    public final void validate(@NonNull final Object target, @NonNull final Errors error) {
        final String comment = (String) target;
        if (StringUtils.isBlank(comment)) {
            error.reject("Comment can't be empty");
        } else if (comment.length() > DomainConstants.SHORT_TEXT_LENGTH) {
            error.reject("Comment shouldn't be longer than " + DomainConstants.SHORT_TEXT_LENGTH + " characters");
        }
    }
}
