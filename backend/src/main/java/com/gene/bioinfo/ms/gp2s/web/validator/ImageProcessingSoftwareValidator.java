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

import com.gene.bioinfo.ms.gp2s.domain.ImageProcessingSoftware;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotEmpty;

@Component
public class ImageProcessingSoftwareValidator extends LabelValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return super.supports(clazz) && ImageProcessingSoftware.class.isAssignableFrom(clazz);
    }

    @Override
    public void doValidate(Object target, Errors e) {
        final ImageProcessingSoftware imageProcessingSoftware = (ImageProcessingSoftware) target;
        isNotEmpty(imageProcessingSoftware.getSoftwareVersions(), "softwareVersions",
                "At least one software version needs to be provided", e);
    }
}
