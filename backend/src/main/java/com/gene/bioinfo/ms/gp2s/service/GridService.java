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

import com.gene.bioinfo.ms.gp2s.domain.Grid;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.repository.*;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.service.base.BaseProjectRestService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GridService extends BaseProjectRestService<Grid> {

    private final GridTypeRepository gridTypeRepository;
    private final SurfaceTreatmentProtocolRepository surfaceTreatmentProtocolRepository;
    private final VitrificationProtocolRepository vitrificationProtocolRepository;
    private final NegativeStainProtocolRepository negativeStainProtocolRepository;
    private final SampleRepository sampleRepository;
    private final CryoStorageDeviceRepository cryoStorageDeviceRepository;
    private final DefaultValueService defaultValueService;

    @Autowired
    GridService(final GridRepository gridRepository,
                final ProjectRepository projectRepository,
                final @NonNull GridTypeRepository gridTypeRepository,
                final @NonNull SurfaceTreatmentProtocolRepository surfaceTreatmentProtocolRepository,
                final @NonNull VitrificationProtocolRepository vitrificationProtocolRepository,
                final @NonNull NegativeStainProtocolRepository negativeStainProtocolRepository,
                final @NonNull SampleRepository sampleRepository,
                final @NonNull CryoStorageDeviceRepository cryoStorageDeviceRepository,
                final @NonNull DefaultValueService defaultValueService
    ) {
        super(gridRepository, projectRepository);
        this.gridTypeRepository = gridTypeRepository;
        this.surfaceTreatmentProtocolRepository = surfaceTreatmentProtocolRepository;
        this.vitrificationProtocolRepository = vitrificationProtocolRepository;
        this.negativeStainProtocolRepository = negativeStainProtocolRepository;
        this.sampleRepository = sampleRepository;
        this.cryoStorageDeviceRepository = cryoStorageDeviceRepository;
        this.defaultValueService = defaultValueService;
    }

    @NonNull
    public Collection<Project> getItemProjects(@NonNull final String slug) {
        return Optional.ofNullable(this.projectRepository.findByGrid_Slug(slug))
                .orElse(Collections.emptySet());
    }

    @Override
    public Collection<Project> getItemProjects(@NonNull final Long id) {
        return Optional.ofNullable(this.projectRepository.findByGrid_Id(id))
                .orElse(Collections.emptySet());
    }

    @Transactional
    @NonNull
    public Grid createItem(@NonNull final Grid input, @NonNull final Long projectId) {
        loadEntityDependency(input);
        Project project = projectRepository.findOne(projectId);
        return commonCreateItem(project, input);
    }

    @Transactional
    @NonNull
    public Grid createItem(@NonNull final Grid input, @NonNull @ApiIgnore final String projectSlug) {
        loadEntityDependency(input);
        Project project = projectRepository.findOneBySlug(projectSlug);
        return commonCreateItem(project, input);
    }

    protected Grid commonCreateItem(@NonNull final Project project, @NonNull final Grid input) {
        super.commonCreateItemValidations(input);
        Grid result = super.createItem(input);
        this.defaultValueService.saveDefaultValues(project, input);
        return result;
    }

    @Transactional
    @NonNull
    @Override
    @Deprecated()
    public Grid updateItem(@NonNull final Grid input) {
        loadEntityDependency(input);
        Grid result = super.updateItem(input);
        return result;
    }

    @Transactional
    @NonNull
    public Grid updateItem(@NonNull final Grid input, @NonNull final Long projectId) {
        return updateItem(input, projectRepository.findOne(projectId));
    }

    @Transactional
    @NonNull
    public Grid updateItem(@NonNull final Grid input, @NonNull final String projectSlug) {
        return updateItem(input, projectRepository.findOneBySlug(projectSlug));
    }

    @Transactional
    @NonNull
    public Grid updateItem(@NonNull final Grid input, @NonNull final Project project) {
        loadEntityDependency(input);
        Grid result = super.updateItem(input);
        this.defaultValueService.saveDefaultValues(project, input);
        return result;
    }

    private void loadEntityDependency(@NonNull final Grid input) {
        loadGridType(input);
        loadSurfaceTreatmentProtocol(input);
        loadVitrificationProtocol(input);
        loadNegativeStainProtocol(input);
        loadSample(input);
        loadCryoStorageDevice(input);
    }

    @NonNull
    @Override
    protected Grid postLoadItem(@NonNull final Grid item) {
        final Grid result = super.postLoadItem(item);
        result.setIsAvailable(((GridRepository) repository).hasBeenReturnedToStorage(item.getId()));
        return result;
    }

    protected void loadGridType(@NonNull final Grid input) {
        input.setGridType(gridTypeRepository.findOne(input.getGridType().getId()));
    }

    protected void loadSurfaceTreatmentProtocol(@NonNull final Grid input) {
        input.setSurfaceTreatmentProtocol(surfaceTreatmentProtocolRepository.findOne(input.getSurfaceTreatmentProtocol().getId()));
    }

    protected void loadVitrificationProtocol(@NonNull final Grid input) {
        if (input.getVitrificationProtocol() != null) {
            input.setVitrificationProtocol(vitrificationProtocolRepository.findOne(input.getVitrificationProtocol().getId()));
        }
    }

    protected void loadNegativeStainProtocol(@NonNull final Grid input) {
        if (input.getNegativeStainProtocol() != null) {
            input.setNegativeStainProtocol(negativeStainProtocolRepository.findOne(input.getNegativeStainProtocol().getId()));
        }
    }

    protected void loadSample(@NonNull final Grid input) {
        input.setSample(sampleRepository.findOne(input.getSample().getId()));
    }

    protected void loadCryoStorageDevice(@NonNull final Grid input) {
        if (input.getCryoStorageDevice() != null) {
            input.setCryoStorageDevice(cryoStorageDeviceRepository.findOne(input.getCryoStorageDevice().getId()));
        }
    }

    public List<Grid> findAvailableGrids(final Long id) {
        return ((GridRepository) this.repository).findAvailableGrids(id);
    }

    public List<Grid> findAvailableGrids(final String slug) {
        return ((GridRepository) this.repository).findAvailableGrids(slug);
    }

    public List<Grid> findAvailableGrids(final String projectSlug, final Long gridId) {
        return ((GridRepository) this.repository).findAvailableGrids(projectSlug, gridId);
    }

    public List<Grid> findAvailableGrids(final Long projectId, final Long gridId) {
        return ((GridRepository) this.repository).findAvailableGrids(projectId, gridId);
    }
}
