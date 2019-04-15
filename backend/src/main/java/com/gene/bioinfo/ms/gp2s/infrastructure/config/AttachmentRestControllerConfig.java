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

package com.gene.bioinfo.ms.gp2s.infrastructure.config;

import com.gene.bioinfo.ms.gp2s.service.attachment.MapAttachmentService;
import com.gene.bioinfo.ms.gp2s.service.attachment.ModelAttachmentService;
import com.gene.bioinfo.ms.gp2s.web.attachment.ISingleAttachmentRestController;
import com.gene.bioinfo.ms.gp2s.web.attachment.SingleAttachmentRestController;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Przemysław Stankowski on 16.04.2018
 */
@Configuration
public class AttachmentRestControllerConfig {

    @Bean
    public ISingleAttachmentRestController mapAttachmentRestController(@NonNull final MapAttachmentService service) {
        return new SingleAttachmentRestController(service);
    }

    @Bean
    public ISingleAttachmentRestController modelAttachmentRestController(
            @NonNull final ModelAttachmentService service) {
        return new SingleAttachmentRestController(service);
    }
}
