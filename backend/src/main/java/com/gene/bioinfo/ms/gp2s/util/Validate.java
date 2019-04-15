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


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.netflix.config.validation.ValidationException;

import java.util.List;

public class Validate {

	public static <T> T notNull(T object, String message, Object ...messageArgs) {
		try {
			return org.apache.commons.lang3.Validate.notNull(object, message, messageArgs);
		} catch (NullPointerException e) {
			throw new ValidationException(e.getMessage()); 
		}
	}
	
	public static <T extends CharSequence> T notBlank(T object, String message, Object ...messageArgs) {
		try {
			return org.apache.commons.lang3.Validate.notBlank(object, message, messageArgs);
		} catch (NullPointerException | IllegalArgumentException e) {
			throw new ValidationException(e.getMessage()); 
		}
	}
	
	public static <T> List<T> notEmpty(List<T> list, String message, Object ...messageArgs) {
		try {
			org.apache.commons.lang3.Validate.notEmpty(list.toArray(), message, messageArgs);
			return list;
		} catch (NullPointerException | IllegalArgumentException e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	public static <T> T isNull(T object, String message, Object ...messageArgs) {
		try {
			Preconditions.checkState(object == null, message, messageArgs);
		} catch (IllegalStateException e) {
			throw new ValidationException(e.getMessage());
		}
		return object;
	}
	
	public static String isNullOrEmpty(String object, String message, Object ...messageArgs) {
		try {
			Preconditions.checkState(Strings.isNullOrEmpty(object), message, messageArgs);
    	} catch (IllegalStateException e) {
    		throw new ValidationException(e.getMessage());
    	}
		return object;
	}
	
}
