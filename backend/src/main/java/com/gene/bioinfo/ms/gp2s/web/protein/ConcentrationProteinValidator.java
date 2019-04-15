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

package com.gene.bioinfo.ms.gp2s.web.protein;

import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.web.validator.ConcentrationValidator;
import com.gene.bioinfo.ms.gp2s.web.validator.LabelValidator;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants.SHORT_STRING_LENGTH;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class ConcentrationProteinValidator extends LabelValidator implements ProteinValidator {

    private final ConcentrationValidator concentrationValidator;

    public ConcentrationProteinValidator(final @NonNull ConcentrationValidator concentrationValidator) {
        this.concentrationValidator = concentrationValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return super.supports(clazz) && Protein.class.isAssignableFrom(clazz);
    }

    @Override
    public void doValidate(Object target, Errors errors) {
        final Protein protein = (Protein) target;

        this.concentrationValidator.validate(protein.getConcentration(), "concentration", errors);

        if (protein.getTubeLabel() != null) {
            isNotLongerThan(protein.getTubeLabel(), "tubeLabel", "Tube label", SHORT_STRING_LENGTH, errors);
        }
    }
}
