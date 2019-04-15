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
import com.gene.bioinfo.ms.gp2s.domain.ProcessingSession;
import com.gene.bioinfo.ms.gp2s.domain.UsedImageProcessingSoftware;
import com.gene.bioinfo.ms.gp2s.repository.ImageProcessingSoftwareRepository;
import com.gene.bioinfo.ms.gp2s.repository.UsedImageProcessingSoftwareRepository;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants.LONG_STRING_LENGTH;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class ProcessingSessionValidator extends LabelValidator {

    private ImageProcessingSoftwareRepository imageProcessingSoftwareRepository;
    private UsedImageProcessingSoftwareRepository usedImageProcessingSoftwareRepository;

    public ProcessingSessionValidator(@NonNull final ImageProcessingSoftwareRepository
                                              imageProcessingSoftwareRepository,
                                      @NonNull final UsedImageProcessingSoftwareRepository
                                              usedImageProcessingSoftwareRepository) {
        super();
        this.imageProcessingSoftwareRepository = imageProcessingSoftwareRepository;
        this.usedImageProcessingSoftwareRepository = usedImageProcessingSoftwareRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return super.supports(aClass) && ProcessingSession.class.isAssignableFrom(aClass);
    }

    @Override
    public void doValidate(Object target, Errors errors) {
        final ProcessingSession processingSession = (ProcessingSession) target;

        // BASIC INFORMATION
        isGreaterOrEqualTo(processingSession.getNumberOfMicrographs(), "numberOfMicrographs",
                "Number of micrographs", 1, errors);

        isGreaterOrEqualTo(processingSession.getNumberOfPickedParticles(), "numberOfPickedParticles",
                "Number of picked particles", 1, errors);

        isNotLongerThan(processingSession.getBasePathOfProcessingDirectory(), "basePathOfProcessingDirectory",
                "Base value of processing directory", LONG_STRING_LENGTH, errors);

        // MICROSCOPY SESSION
        isNotEmpty(processingSession.getMicroscopySessions(), "microscopySessions",
                "At least one microscopy session needs to be associated", errors);

        Optional.ofNullable(processingSession.getUsedImageProcessingSoftware()).orElse(Collections.emptyList())
                .forEach(u -> this.validateUsedImageProcessingSoftware(u, errors));
    }

    private void validateUsedImageProcessingSoftware(UsedImageProcessingSoftware usedImageProcessingSoftware, Errors errors) {
        isNotNull(usedImageProcessingSoftware.getImageProcessingSoftware(), "usedImageProcessingSoftware" +
                ".imageProcessingSoftware", "Image processing software", errors);

        isNotEmpty(usedImageProcessingSoftware.getSoftwareVersion(), "usedImageProcessingSoftware.softwareVersion",
                "Software version", 45, errors);

        if (usedImageProcessingSoftware.getImageProcessingSoftware() != null) {
            final Long imageProcessingSoftwareId = usedImageProcessingSoftware.getImageProcessingSoftware().getId();
            isNotNull(imageProcessingSoftwareId, "usedImageProcessingSoftware" +
                    ".imageProcessingSoftware.id", "Image processing software id", errors);

            if (imageProcessingSoftwareId != null) {
                final ImageProcessingSoftware software
                        = imageProcessingSoftwareRepository.findOne(imageProcessingSoftwareId);
                if (software == null) {
                    errors.rejectValue("usedImageProcessingSoftware.imageProcessingSoftware", "Invalid image " +
                            "processing software");
                } else {
                    validateSoftwareVersion(usedImageProcessingSoftware, software, errors);
                }
            }
        }
    }

    private void validateSoftwareVersion(UsedImageProcessingSoftware usedImageProcessingSoftware, ImageProcessingSoftware software, Errors errors) {
        final List versions = Optional.ofNullable(software.getSoftwareVersions())
                                      .orElse(Collections.emptyList());
        if (usedImageProcessingSoftware.getId() != null) { // update case
            final UsedImageProcessingSoftware original = usedImageProcessingSoftwareRepository.findOne
                    (usedImageProcessingSoftware.getId());
            if (original != null && original.getSoftwareVersion().equals(usedImageProcessingSoftware
                    .getSoftwareVersion())) {
                //no need to validate version, so it allow historical values to pass
                return;
            }
        }
        if (!versions.contains(usedImageProcessingSoftware.getSoftwareVersion())) {
            errors.rejectValue("usedImageProcessingSoftware.softwareVersion", "Software version is not valid");
        }
    }
}
