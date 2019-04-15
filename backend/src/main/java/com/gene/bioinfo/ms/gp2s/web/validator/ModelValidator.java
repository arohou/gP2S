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

import com.gene.bioinfo.ms.gp2s.domain.Model;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isGreaterThan;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotEmpty;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotNull;

@Component
public class ModelValidator extends LabelValidator {

    private final FileRepository fileRepository;

    @Autowired
    public ModelValidator(@NonNull final FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return super.supports(aClass) && Model.class.isAssignableFrom(aClass);
    }

    @Override
    public void doValidate(Object target, Errors errors) {
        final Model model = (Model) target;

        isGreaterThan(model.getResolution(), "resolution", "Resolution", 0, errors);
        isNotEmpty(model.getMaps(), "maps", "At least one map needs to be associated", errors);

        this.validateAttachment(model, errors);
    }

    public void validateAttachment(@NonNull Model model, @NonNull Errors errors) {
        isNotNull(model.getAttachmentMongoId(), "attachmentMongoId", "Attachment mongo ID", errors);
        isNotNull(model.getAttachmentFileName(), "attachmentFileName", "Attachment file name", errors);
        if (model.getAttachmentMongoId() != null && fileRepository.findOne(model.getAttachmentMongoId()) == null) {
            errors.rejectValue("attachmentMongoId", "File doesn't exists: " + model.getAttachmentMongoId());
        }
    }
}
