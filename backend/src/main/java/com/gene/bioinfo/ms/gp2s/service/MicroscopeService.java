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

package com.gene.bioinfo.ms.gp2s.service;

import com.gene.bioinfo.ms.gp2s.domain.ElectronDetector;
import com.gene.bioinfo.ms.gp2s.domain.Microscope;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.ElectronDetectorRepository;
import com.gene.bioinfo.ms.gp2s.repository.MicroscopeRepository;
import com.gene.bioinfo.ms.gp2s.service.base.BaseRestService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@Service
public class MicroscopeService extends BaseRestService<Microscope> {

    private final ElectronDetectorRepository electronDetectorRepository;

    MicroscopeService(@NotNull final MicroscopeRepository microscopeRepository,
                      @NonNull final ElectronDetectorRepository electronDetectorRepository) {
        super(microscopeRepository);
        this.electronDetectorRepository = electronDetectorRepository;
    }

    @NonNull
    public Collection<ElectronDetector> getElectronDetectors(@NonNull final String slug) {
        return Optional.ofNullable(electronDetectorRepository.findByMicroscope_Slug(slug))
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found for slug: " + slug));
    }

    @NonNull
    public Collection<ElectronDetector> getElectronDetectors(@NonNull final Long id) {
        return Optional.ofNullable(electronDetectorRepository.findByMicroscope_Id(id))
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found for id: " + id));
    }
}
