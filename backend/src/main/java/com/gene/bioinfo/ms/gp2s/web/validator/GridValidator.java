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

import com.gene.bioinfo.ms.gp2s.domain.Grid;
import com.gene.bioinfo.ms.gp2s.repository.*;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.gene.bioinfo.ms.gp2s.util.ValidationUtils.*;

@Component
public class GridValidator extends LabelValidator {

    private final GridTypeRepository gridTypeRepository;
    private final SurfaceTreatmentProtocolRepository surfaceTreatmentProtocolRepository;
    private final VitrificationProtocolRepository vitrificationProtocolRepository;
    private final NegativeStainProtocolRepository negativeStainProtocolRepository;
    private final SampleRepository sampleRepository;
    private final CryoStorageDeviceRepository cryoStorageDeviceRepository;
    private final ConcentrationValidator concentrationValidator;

    @Autowired
    public GridValidator(final @NonNull GridTypeRepository gridTypeRepository,
                         final @NonNull SurfaceTreatmentProtocolRepository surfaceTreatmentProtocolRepository,
                         final @NonNull VitrificationProtocolRepository vitrificationProtocolRepository,
                         final @NonNull NegativeStainProtocolRepository negativeStainProtocolRepository,
                         final @NonNull SampleRepository sampleRepository,
                         final @NonNull CryoStorageDeviceRepository cryoStorageDeviceRepository,
                         final @NonNull ConcentrationValidator concentrationValidator) {

        this.gridTypeRepository = gridTypeRepository;
        this.surfaceTreatmentProtocolRepository = surfaceTreatmentProtocolRepository;
        this.vitrificationProtocolRepository = vitrificationProtocolRepository;
        this.negativeStainProtocolRepository = negativeStainProtocolRepository;
        this.sampleRepository = sampleRepository;
        this.cryoStorageDeviceRepository = cryoStorageDeviceRepository;
        this.concentrationValidator = concentrationValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return super.supports(clazz) && Grid.class.isAssignableFrom(clazz);
    }

    @Override
    public void doValidate(Object target, Errors e) {
        final Grid grid = (Grid) target;

        isNotNull(grid.getProtocolType(), "protocolType", "Protocol type", e);
        isNullOrGreaterOrEqualTo(grid.getIncubationTime(), "incubationTime", "Incubation time", 0, e);

        this.concentrationValidator.validate(grid.getConcentration(), "concentration", e);

        isDefined(grid.getGridType(), gridTypeRepository, "gridType", "Grid type", e);
        isDefined(grid.getSample(), sampleRepository, "sample", "Sample", e);
        isDefined(grid.getSurfaceTreatmentProtocol(), surfaceTreatmentProtocolRepository, "surfaceTreatmentProtocol", "Surface treatment protocol", e);

        validateProtocol(grid, e);
        if (Grid.ProtocolType.Stain.equals(grid.getProtocolType())) {
            validateStainGrid(grid, e);
        } else if (Grid.ProtocolType.Cryo.equals(grid.getProtocolType())) {
            validateCryoGrid(grid, e);
        }
    }

    private void validateCryoGrid(@NonNull final Grid grid, @NonNull final Errors e) {
        isDefined(grid.getCryoStorageDevice(), cryoStorageDeviceRepository, "cryoStorageDevice", "Storage device", e);
        if (grid.getCryoStorageDevice() != null) {
            validateCryoStorageDevice(grid, e);
        }

        isEmpty(grid.getStorageBoxLabelNumber(), "storageBoxLabelNumber", "Storage box label/number", e);
        isEmpty(grid.getPositionWithinBox(), "positionWithinBox", "Position within box", e);
    }

    private void validateCryoStorageDevice(@NonNull final Grid grid, @NonNull final Errors e) {
        if (Boolean.TRUE.equals(grid.getCryoStorageDevice().getHasCylinders())) {
            isNotEmpty(grid.getCylinderNumberLabel(), "cylinderNumberLabel", "Cylinder number/label", 24, e);
        } else {
            isEmpty(grid.getCylinderNumberLabel(), "cylinderNumberLabel", "Cylinder number/label", e);
        }
        if (Boolean.TRUE.equals(grid.getCryoStorageDevice().getHasTubes())) {
            isNotEmpty(grid.getTubeNumberLabel(), "tubeNumberLabel", "Tube number/label", 24, e);
        } else {
            isEmpty(grid.getTubeNumberLabel(), "tubeNumberLabel", "Tube number/label", e);
        }
        if (Boolean.TRUE.equals(grid.getCryoStorageDevice().getHasBoxes())) {
            isNotEmpty(grid.getBoxNumberLabel(), "boxNumberLabel", "Box number/label", 24, e);
        } else {
            isEmpty(grid.getBoxNumberLabel(), "boxNumberLabel", "Box number/label", e);
        }
    }

    private void validateStainGrid(@NonNull final Grid grid, @NonNull final Errors e) {
        isNotEmpty(grid.getStorageBoxLabelNumber(), "storageBoxLabelNumber", "Storage box label/number", 16, e);
        isNotEmpty(grid.getPositionWithinBox(), "positionWithinBox", "Position within box", 4, e);

        isNull(grid.getCryoStorageDevice(), "cryoStorageDevice", "Storage device", e);
        isEmpty(grid.getCylinderNumberLabel(), "cylinderNumberLabel", "Cylinder number/label", e);
        isEmpty(grid.getTubeNumberLabel(), "tubeNumberLabel", "Tube number/label", e);
        isEmpty(grid.getBoxNumberLabel(), "boxNumberLabel", "Box number/label", e);
    }

    private void validateProtocol(@NonNull final Grid grid, @NonNull final Errors e) {
        if (grid.getNegativeStainProtocol() == null && grid.getVitrificationProtocol() == null) {
            e.rejectValue("negativeStainProtocol", "Negative stain protocol should not be empty");
            e.rejectValue("vitrificationProtocol", "Vitrification protocol should not be empty");
        } else if (grid.getNegativeStainProtocol() != null && grid.getVitrificationProtocol() != null) {
            e.rejectValue("negativeStainProtocol", "Please use one of: negative stain or vitrification protocol");
            e.rejectValue("vitrificationProtocol", "Please use one of: negative stain or vitrification protocol");
        } else if (grid.getNegativeStainProtocol() != null) {
            isDefined(grid.getNegativeStainProtocol(), negativeStainProtocolRepository, "negativeStainProtocol", "negativeStainProtocol", e);
        } else if (grid.getVitrificationProtocol() != null) {
            isDefined(grid.getVitrificationProtocol(), vitrificationProtocolRepository, "vitrificationProtocol", "vitrificationProtocol", e);
        }
    }
}
