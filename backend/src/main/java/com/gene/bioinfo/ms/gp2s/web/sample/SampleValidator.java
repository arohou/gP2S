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

package com.gene.bioinfo.ms.gp2s.web.sample;

import com.gene.bioinfo.ms.gp2s.domain.sample.LigandComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.ProteinComponent;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.domain.sample.BaseAliquot;
import com.gene.bioinfo.ms.gp2s.domain.sample.BaseSampleComponent;
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
import com.gene.bioinfo.ms.gp2s.repository.LigandRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProteinRepository;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import com.gene.bioinfo.ms.gp2s.web.validator.LabelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.gene.bioinfo.ms.gp2s.infrastructure.constants.PhysicalConstants.ABSOLUTE_ZERO_IN_CELSIUS;
import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class SampleValidator extends LabelValidator {

    private final ProteinRepository proteinRepository;
    private final LigandRepository ligandRepository;

    @Autowired
    SampleValidator(
            @NotNull final ProteinRepository proteinRepository,
            @NotNull final LigandRepository ligandRepository
    ) {
        this.proteinRepository = proteinRepository;
        this.ligandRepository = ligandRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return super.supports(clazz) && Sample.class.isAssignableFrom(clazz);
    }

    @Override
    public void doValidate(Object target, Errors e) {
        validateSample((Sample) target, e);
    }

    protected void validateSample(Sample sample, Errors e) {
        this.validateSampleBaseInfo(sample, e);

        if (isNullOrEmpty(sample.getProteinComponent())
                && isNullOrEmpty(sample.getLigandComponent())) {
            e.rejectValue("proteinComponent", "Sample need to have at least one component");
        }

        int[] idx = {0};
        Optional.ofNullable(sample.getLigandComponent()).orElse(Collections.emptyList())
                .forEach(component -> validateLigand(component, idx[0]++, e));

        idx[0] = 0;
        Optional.ofNullable(sample.getProteinComponent()).orElse(Collections.emptyList())
                .forEach(component -> validateProtein(component, idx[0]++, e));

    }

    protected void validateSampleBaseInfo(Sample sample, Errors e) {
        isNullOrGreaterThan(sample.getIncubationTime(), "incubationTime", "Incubation time", 0, e);
        isNullOrGreaterOrEqualTo(sample.getIncubationTemperature(), "incubationTemperature", "Incubation temperature", ABSOLUTE_ZERO_IN_CELSIUS, e);
        isNotNull(sample.getAvailableForGridMaking(), "availableForGridMaking", "Available for grid making", e);
        isNotLongerThan(sample.getProtocolDescription(), "protocolDescription", "Protocol description", DomainConstants.LONG_TEXT_LENGTH, e);
        isNotLongerThan(sample.getOtherBufferComponents(), "otherBufferComponents", "Buffer", DomainConstants.LONG_STRING_LENGTH, e);
    }

    protected void validateProtein(ProteinComponent component, int index, Errors e) {
        validateComponent("proteinComponent[" + index + "]", component, e);
        validateAliquot("proteinComponent[" + index + "]", component.getAliquot(), proteinRepository, e);
    }

    protected void validateLigand(LigandComponent component, int index, Errors e) {
        validateComponent("ligandComponent[" + index + "]", component, e);
        validateAliquot("ligandComponent[" + index + "]", component.getAliquot(), ligandRepository, e);
    }

    protected void validateComponent(String prefix, BaseSampleComponent component, Errors e) {
        if (component.getFinalConcentration() == null || component.getFinalConcentration() <= 0f) {
            e.rejectValue(prefix + ".finalConcentration", "Concentration should be a number greater than 0");
        }
    }

    protected <A extends BaseAliquot, R extends BaseSlugRepository<A>>
    void validateAliquot(String prefix, A example, R repository, Errors e) {
        A aliquot = loadAliquotByExample(example, repository);
        if (aliquot == null) {
            e.rejectValue(prefix + ".aliquot", "Aliquot can not be found");
        }
    }

    protected <A extends BaseAliquot, R extends BaseSlugRepository<A>> A loadAliquotByExample(A example, R repository) {
        if (example != null) {
            if (example.getId() != null) {
                return repository.findOne(example.getId());
            } else {
                return repository.findOneBySlug(example.getSlug());
            }
        }
        return null;
    }

    protected <U> boolean isNullOrEmpty(Collection<U> collection) {
        if (collection == null) {
            return true;
        }
        return collection.isEmpty();
    }

}
