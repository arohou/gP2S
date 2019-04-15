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

import com.gene.bioinfo.ms.gp2s.domain.Settings;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.contains;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotLongerThan;

/**
 * @author Przemysław Stankowski on 21.03.2018
 */
@Component
public class SettingsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Settings.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, Errors errors) {
        Settings settings = (Settings) target;
        contains(settings.getPatternForDataStorageDirectoryName(), "${MicroscopySessionID}",
                "patternForDataStorageDirectoryName", "Pattern for data storage directory name",
                errors);
        isNotLongerThan(settings.getPatternForDataStorageDirectoryName(), "patternForDataStorageDirectoryName",
                "Pattern for data storage directory name", DomainConstants.LONG_STRING_LENGTH, errors);
    }
}
