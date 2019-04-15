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

package com.gene.bioinfo.ms.gp2s.exception.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationError extends BaseErrorResponse {

    @Getter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors = new HashMap<>();

    public ValidationError(String errorMessage) {
        super(errorMessage);
    }

    public static ValidationError fromBindingErrors(@NonNull final Errors errors) {
        ValidationError error = new ValidationError(
                "Validation failed. " + errors.getErrorCount() + " error(s)");
        for (FieldError objectError : errors.getFieldErrors()) {
            error.addValidationError(
                    objectError.getField(),
                    objectError.getCode());
        }
        for (ObjectError objectError : errors.getGlobalErrors()) {
            error.addValidationError(
                    objectError.getObjectName(),
                    objectError.getCode());
        }
        return error;
    }

    public void addValidationError(
            @NonNull final String field, @NonNull final String message) {
        errors.put(field, message);
    }
}
