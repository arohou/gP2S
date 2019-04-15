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

package com.gene.bioinfo.ms.gp2s.web.ligand;

import com.gene.bioinfo.ms.gp2s.domain.Ligand;
import com.gene.bioinfo.ms.gp2s.web.validator.LabelValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants.LONG_STRING_LENGTH;
import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants.SHORT_STRING_LENGTH;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isGreaterThan;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.isNotLongerThan;

@Component
public class FieldLengthLigandValidator extends LabelValidator implements LigandValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return super.supports(clazz) && Ligand.class.isAssignableFrom(clazz);
    }

    @Override
    public void doValidate(Object target, Errors errors) {
        final Ligand ligand = (Ligand) target;
        if (ligand.getConceptId() != null) {
            isNotLongerThan(ligand.getConceptId(), "conceptId", "Concept ID", LONG_STRING_LENGTH, errors);
        }
        if (ligand.getBatchId() != null) {
            isNotLongerThan(ligand.getBatchId(), "batchId", "Batch ID", SHORT_STRING_LENGTH, errors);
        }
        if (ligand.getTubeLabel() != null) {
            isNotLongerThan(ligand.getTubeLabel(), "tubeLabel", "Tube label", SHORT_STRING_LENGTH, errors);
        }
        isGreaterThan(ligand.getConcentration(), "concentration", "Concentration", 0, errors);
        if (ligand.getSolvent() != null) {
            isNotLongerThan(ligand.getSolvent(), "solvent", "Solvent", SHORT_STRING_LENGTH, errors);
        }
    }
}
