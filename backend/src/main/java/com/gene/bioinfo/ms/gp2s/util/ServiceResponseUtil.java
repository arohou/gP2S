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

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ServiceResponseUtil {

    @NonNull
    public static ResponseEntity<String> interpretResult(@NonNull final ResourceAccessException exception,
                                                         @NonNull final String serviceName) {
        Class<? extends Throwable> rootCauseClass = exception.getRootCause().getClass();

        String errorMessage = "Cannot validate as " + serviceName + " is not responding";
        if (rootCauseClass == SocketTimeoutException.class) {
            errorMessage = "Timeout while communicating with " + serviceName;
        } else if (rootCauseClass == UnknownHostException.class) {
            errorMessage = "Error while communicating with " + serviceName + " (unknown host)";
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorMessage);
    }
}
