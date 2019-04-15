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

import com.gene.bioinfo.ms.gp2s.domain.Map;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants.LONG_STRING_LENGTH;
import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants.SHORT_STRING_LENGTH;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class MapValidator extends LabelValidator {
    private static final String SYMETRY_APPLIED_PATTERN = "C\\d+|D\\d+|O|T|I";

    private final FileRepository fileRepository;

    @Autowired
    public MapValidator(@NonNull final FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return super.supports(aClass) && Map.class.isAssignableFrom(aClass);
    }

    @Override
    public void doValidate(Object target, Errors errors) {
        final Map map = (Map) target;

        isNotNull(map.getPixelSize(), "pixelSize", "Pixel size", errors);
        isNullOrGreaterThan(map.getPixelSize(), "pixelSize", "Pixel size", 0, errors);
        isNotNull(map.getIsolevelForSurfaceRendering(), "isolevelForSurfaceRendering", "Isolevel for surface rendering", errors);
        isNotNull(map.getProcessingSession(), "processingSession", "Processing session", errors);
        isNotNull(map.getNumberOfImages(), "numberOfImages", "Number of images", errors);
        isNullOrGreaterThan(map.getNumberOfImages(), "numberOfImages", "Number of images", 0, errors);
        isNotNull(map.getEstimatedResolutionInBestParts(), "estimatedResolutionInBestParts", "Estimated resolution in best parts", errors);
        isNullOrGreaterThan(map.getEstimatedResolutionInBestParts(), "estimatedResolutionInBestParts", "Estimated resolution in best parts", 0, errors);
        isNotNull(map.getEstimatedResolutionInWorstParts(), "estimatedResolutionInWorstParts", "Estimated resolution in worst parts", errors);
        isNullOrGreaterThan(map.getEstimatedResolutionInWorstParts(), "estimatedResolutionInWorstParts", "Estimated resolution in worst parts", 0, errors);
        if (map.getEstimatedResolutionInBestParts() != null && map.getEstimatedResolutionInWorstParts() != null
                && map.getEstimatedResolutionInBestParts() > map.getEstimatedResolutionInWorstParts()) {
            errors.rejectValue("estimatedResolutionInWorstParts", "Estimated resolution in worst parts should be >= best resolution");
        }
        isNotNull(map.getSymmetryApplied(), "symmetryApplied", "Symmetry applied", errors);
        if (map.getSymmetryApplied() != null) {
            if (!map.getSymmetryApplied().matches(SYMETRY_APPLIED_PATTERN)) {
                errors.rejectValue("symmetryApplied", "Symmetry applied should match pattern C\\d+|D\\d+|O|T|I");
            } else {
                isNotLongerThan(map.getSymmetryApplied(), "symmetryApplied", "Symmetry applied", SHORT_STRING_LENGTH, errors);
            }
        }
        if (map.getDescription() != null) {
            isNotLongerThan(map.getDescription(), "description", "Description", LONG_STRING_LENGTH, errors);
        }

        this.doValidateAttachment(map, errors);
    }

    public void doValidateAttachment(@NonNull Map map, @NonNull Errors errors) {
        isNotNull(map.getAttachmentMongoId(), "attachmentMongoId", "Attachment mongo ID", errors);
        isNotNull(map.getAttachmentFileName(), "attachmentFileName", "Attachment file name", errors);
        if (map.getAttachmentMongoId() != null && fileRepository.findOne(map.getAttachmentMongoId()) == null) {
            errors.rejectValue("attachmentMongoId", "File doesn't exists: " + map.getAttachmentMongoId());
        }
    }
}
