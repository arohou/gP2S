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

import com.gene.bioinfo.ms.gp2s.domain.Concentration;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class ConcentrationValidator {

    public void validate(final Concentration concentration, @NonNull final String prefix, @NonNull final Errors e) {
        if (concentration == null) {
            e.rejectValue(prefix, "Concentration should be specified");
            return;
        }
        isNotNull(concentration.getIsDilutedOrConcentrated(), prefix + ".isDilutedOrConcentrated",
                "Is diluted or concentrated", e);

        if (Boolean.TRUE.equals(concentration.getIsDilutedOrConcentrated())) {
            isNotNull(concentration.getConcentrationType(), prefix + ".concentrationType",
                    "Concentration type", e);
            if (Concentration.ConcentrationType.Dilution.equals(concentration.getConcentrationType())) {
                validateDiluted(concentration, prefix, e);
            } else if (Concentration.ConcentrationType.Concentration.equals(concentration.getConcentrationType())) {
                validateConcentrated(concentration, prefix, e);
            }
        } else {
            isNull(concentration.getConcentrationFactor(), prefix + ".concentrationFactor",
                    "Concentration factor", e);
            isNull(concentration.getDilutionFactor(), prefix + ".dilutionFactor",
                    "Dilution factor", e);
        }
    }

    protected void validateConcentrated(@NonNull final Concentration concentration, @NonNull final String prefix, @NonNull final Errors e) {
        isGreaterThan(concentration.getConcentrationFactor(), prefix + ".concentrationFactor",
                "Concentration factor", 1.0, e);
        isNull(concentration.getDilutionFactor(), prefix + ".dilutionFactor",
                "Dilution factor", e);
    }

    protected void validateDiluted(@NonNull final Concentration concentration, @NonNull final String prefix, @NonNull final Errors e) {
        isGreaterThan(concentration.getDilutionFactor(), prefix + ".dilutionFactor", "Dilution factor",
                1.0, e);
        isNull(concentration.getConcentrationFactor(), prefix + ".concentrationFactor",
                "Concentration factor", e);
    }

}
