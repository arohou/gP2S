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

package com.gene.bioinfo.ms.gp2s.util;

import com.gene.bioinfo.ms.gp2s.domain.base.Persistable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.Errors;

import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern ID_PATTERN = Pattern.compile("(-)?\\d+");

    public static void isNotEmpty(String value, String property, String messagePrefix, int length, Errors error) {
        if (StringUtils.isBlank(value)) {
            error.rejectValue(property, messagePrefix + " can't be empty");
        } else if (value.length() > length) {
            error.rejectValue(property, messagePrefix + " shouldn't be longer than " + length + " characters");
        }
    }

    public static void isEmpty(String value, String property, String messagePrefix, Errors error) {
        if (StringUtils.isNotBlank(value)) {
            error.rejectValue(property, messagePrefix + " shouldn't be set");
        }
    }

    public static void isNotLongerThan(String value, String property, String messagePrefix, int length, Errors error) {
        if (StringUtils.isNotBlank(value) && value.length() > length) {
            error.rejectValue(property, messagePrefix + " shouldn't be longer than " + length + " characters");
        }
    }

    public static void isNotEmpty(Collection collection, String property, String errorMessage, Errors error) {
        if (collection == null || collection.isEmpty()) {
            error.rejectValue(property, errorMessage);
        }
    }

    public static void isNotNull(Object value, String property, String messagePrefix, Errors error) {
        if (value == null) {
            error.rejectValue(property, messagePrefix + " should be specified");
        }
    }

    public static void isNull(Object value, String property, String messagePrefix, Errors error) {
        if (value != null) {
            error.rejectValue(property, messagePrefix + " shouldn't be specified");
        }
    }

    public static void contains(String sequence, String searchSequence, String property, String messagePrefix, Errors error) {
        if (!StringUtils.contains(sequence, searchSequence)) {
            error.rejectValue(property, messagePrefix + " should contain sequence \"" + searchSequence + "\"");
        }
    }

    public static void isGreaterThan(Number value, String property, String messagePrefix, Number min, Errors error) {
        if (value == null || value.doubleValue() <= min.doubleValue()) {
            error.rejectValue(property, messagePrefix + " should be greater than " + min);
        }
    }

    public static void isGreaterOrEqualTo(Number value, String property, String messagePrefix, Number min, Errors error) {
        if (value == null || value.doubleValue() < min.doubleValue()) {
            error.rejectValue(property, messagePrefix + " should be greater or equal to " + min);
        }
    }

    public static void isBetween(Number value, String property, String messagePrefix, Number min, Number max, Errors error) {
        if (value == null || value.doubleValue() <= min.doubleValue() || value.doubleValue() > max.doubleValue()) {
            error.rejectValue(property, messagePrefix + " should be between " + min + " and " + max);
        }
    }

    public static void isBetweenInclusive(Number value, String property, String messagePrefix, Number min, Number max, Errors error) {
        if (value == null || value.doubleValue() < min.doubleValue() || value.doubleValue() > max.doubleValue()) {
            error.rejectValue(property, messagePrefix + " should be between " + min + " and " + max + " inclusive");
        }
    }

    public static void isNullOrGreaterOrEqualTo(Number value, String property, String messagePrefix, Number min, Errors error) {
        if (value != null && value.doubleValue() < min.doubleValue()) {
            error.rejectValue(property, messagePrefix + " should be a number greater than or equal to " + min);
        }
    }

    public static void isNullOrGreaterThan(Number value, String property, String messagePrefix, Number min, Errors error) {
        if (value != null && value.doubleValue() <= min.doubleValue()) {
            error.rejectValue(property, messagePrefix + " should be a number greater than or equal to " + min);
        }
    }

    public static <P extends Persistable<Long>, R extends JpaRepository<P, Long>> void isDefined(P entity, R repository, String property, String messagePrefix, Errors error) {
        if (entity == null
                || entity.getId() == null
                || !repository.exists(entity.getId())) {
            error.rejectValue(property, messagePrefix + " can not be found");
        }
    }

    public static void isAfter(Date value, String property, String messagePrefix, Date otherDate, Errors error) {
        if (value != null && !value.after(otherDate)) {
            error.rejectValue(property, messagePrefix + " should be a date after " + otherDate);
        }
    }

    public static boolean isAnId(@Null final String string) {
        return string != null && ID_PATTERN.matcher(string).matches();
    }

    public static void entityExists(Long id, CrudRepository repository, String property, String messagePrefix, Errors
            error) {
        if (!repository.exists(id)) {
            error.rejectValue(property, messagePrefix + " doesn't exist");
        }
    }
}
