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

import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static org.mockito.AdditionalMatchers.and;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ValidationUtilsTest {


    private static final String property = "property";
    private static final String messagePrefix = "prefix";

    @Mock
    private Errors error;

    @Test
    public void shouldIsNotEmptyNotAcceptNullValue() throws Exception {
        ValidationUtils.isNotEmpty(null, property, messagePrefix, DomainConstants.SHORT_STRING_LENGTH, error);
        verify(error).rejectValue(eq(property), startsWith(messagePrefix));
    }

    @Test
    public void shouldIsNotEmptyNotAcceptEmptyValue() throws Exception {
        ValidationUtils.isNotEmpty("", property, messagePrefix, DomainConstants.SHORT_STRING_LENGTH, error);
        verify(error).rejectValue(eq(property), startsWith(messagePrefix));
    }

    @Test
    public void shouldIsNotEmptyNotAcceptBlankValue() throws Exception {
        ValidationUtils.isNotEmpty(" ", property, messagePrefix, DomainConstants.SHORT_STRING_LENGTH, error);
        verify(error).rejectValue(eq(property), startsWith(messagePrefix));
    }

    @Test
    public void shouldIsNotEmptyNotAcceptValueLongerThan() throws Exception {
        ValidationUtils.isNotEmpty("123456", property, messagePrefix, 5, error);
        verify(error).rejectValue(eq(property), startsWith(messagePrefix));
    }

    @Test
    public void shouldIsNotEmptyAcceptString() throws Exception {
        ValidationUtils.isNotEmpty("Valid value", property, messagePrefix, DomainConstants.SHORT_STRING_LENGTH, error);
        verify(error, times(0)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldIsGreaterThanNotAcceptNullValue() throws Exception {
        ValidationUtils.isGreaterThan(null, property, messagePrefix, 0, error);
        verify(error).rejectValue(eq(property), and(startsWith(messagePrefix), endsWith("0")));
    }

    @Test
    public void shouldIsGreaterThanNotAcceptSmallerValue() throws Exception {
        ValidationUtils.isGreaterThan(-1, property, messagePrefix, 0, error);
        verify(error).rejectValue(eq(property), and(startsWith(messagePrefix), endsWith("0")));
    }

    @Test
    public void shouldIsGreaterThanNotAcceptEqualValue() throws Exception {
        ValidationUtils.isGreaterThan(0, property, messagePrefix, 0, error);
        verify(error).rejectValue(eq(property), and(startsWith(messagePrefix), endsWith("0")));
    }

    @Test
    public void shouldIsGreaterThanAcceptValidValue() throws Exception {
        ValidationUtils.isGreaterThan(1, property, messagePrefix, 0, error);
        verify(error, times(0)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldIsBetweenNotAcceptNullValue() throws Exception {
        ValidationUtils.isBetween(null, property, messagePrefix, 0, 2.2, error);
        verify(error).rejectValue(eq(property), and(and(startsWith(messagePrefix), endsWith("2.2")), contains("0")));
    }

    @Test
    public void shouldIsBetweenNotAcceptValueSmallerThan() throws Exception {
        ValidationUtils.isBetween(-1, property, messagePrefix, 0, 2.2, error);
        verify(error).rejectValue(eq(property), and(and(startsWith(messagePrefix), endsWith("2.2")), contains("0")));
    }

    @Test
    public void shouldIsBetweenNotAcceptValueBiggerThan() throws Exception {
        ValidationUtils.isBetween(2.3, property, messagePrefix, 0, 2.2, error);
        verify(error).rejectValue(eq(property), and(and(startsWith(messagePrefix), endsWith("2.2")), contains("0")));
    }

    @Test
    public void shouldIsBetweenAcceptValidValue() throws Exception {
        ValidationUtils.isBetween(1, property, messagePrefix, 0, 2, error);
        verify(error, times(0)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldIsNullOrGreaterOrEqualToAcceptNullValue() throws Exception {
        ValidationUtils.isNullOrGreaterOrEqualTo(null, property, messagePrefix, 2, error);
        verify(error, times(0)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldIsNullOrGreaterOrEqualToNotAcceptSmallerValue() throws Exception {
        ValidationUtils.isNullOrGreaterOrEqualTo(-1, property, messagePrefix, 0, error);
        verify(error).rejectValue(eq(property), and(startsWith(messagePrefix), endsWith("0")));
    }

    @Test
    public void shouldIsNullOrGreaterOrEqualToAcceptEqualValue() throws Exception {
        ValidationUtils.isNullOrGreaterOrEqualTo(0, property, messagePrefix, 0, error);
        verify(error, times(0)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldIsNullOrGreaterOrEqualToAcceptGreaterValue() throws Exception {
        ValidationUtils.isNullOrGreaterOrEqualTo(1, property, messagePrefix, 0, error);
        verify(error, times(0)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldContainsNotAcceptNullValue() throws Exception {
        ValidationUtils.contains(null, "sequence", property, messagePrefix, error);
        verify(error, times(1)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldContainsAcceptWhenContainsSequence() throws Exception {
        ValidationUtils.contains("test_sequence_", "sequence", property, messagePrefix, error);
        verify(error, times(0)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldContainsAcceptWhenContainsBashVariableStyleSequence() throws Exception {
        ValidationUtils.contains("test_${sequence}", "${sequence}", property, messagePrefix, error);
        verify(error, times(0)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldContainsNotAcceptWhenMissingSequence() throws Exception {
        ValidationUtils.contains("", "missing", property, messagePrefix, error);
        verify(error, times(1)).rejectValue(anyString(), anyString());
    }

    @Test
    public void shouldContainsNotAcceptDuoToCaseSensitive() throws Exception {
        ValidationUtils.contains("sequence", "Sequence", property, messagePrefix, error);
        verify(error, times(1)).rejectValue(anyString(), anyString());
    }
}
