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

package com.gene.bioinfo.ms.gp2s.service.feign;

import com.gene.bioinfo.ms.gp2s.exception.ThirdPartyServiceException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ThirdPartyServiceExceptionErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        final HttpStatus statusCode = HttpStatus.valueOf(response.status());

        String responseMessage = "";
        try {
            if (response.body() != null) {
                responseMessage = Util.toString(response.body().asReader());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process response body.", e);
        }

        return new ThirdPartyServiceException(statusCode, responseMessage);
    }
}
