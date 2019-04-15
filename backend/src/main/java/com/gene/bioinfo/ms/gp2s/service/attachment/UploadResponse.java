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

package com.gene.bioinfo.ms.gp2s.service.attachment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Przemysław Stankowski on 19.04.2018
 */
@Data
@Builder
@AllArgsConstructor
public class UploadResponse {
    private boolean success;

    private String mongoId;
    private Long id;

    @JsonProperty("error")
    private String errorMessage;

    public static class UploadResponseBuilder {
        public UploadResponseBuilder successResponse(String mongoId) {
            this.success = true;
            this.mongoId = mongoId;
            return this;
        }

        public UploadResponseBuilder failResponse(String errorMessage) {
            this.success = false;
            this.errorMessage = errorMessage;
            return this;
        }
    }
}
