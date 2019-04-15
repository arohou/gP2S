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

import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.repository.*;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GridValidatorTest {

    @Mock
    private GridTypeRepository gridTypeRepository;
    @Mock
    private SurfaceTreatmentProtocolRepository surfaceTreatmentProtocolRepository;
    @Mock
    private VitrificationProtocolRepository vitrificationProtocolRepository;
    @Mock
    private NegativeStainProtocolRepository negativeStainProtocolRepository;
    @Mock
    private SampleRepository sampleRepository;
    @Mock
    private CryoStorageDeviceRepository cryoStorageDeviceRepository;
    @Mock
    private ConcentrationValidator concentrationValidator;

    @Mock
    private Errors errors;

    private GridValidator tested;

    private Grid.GridBuilder builder;

    @Before
    public void setup() {
        tested = new GridValidator(gridTypeRepository, surfaceTreatmentProtocolRepository, vitrificationProtocolRepository, negativeStainProtocolRepository,
                sampleRepository, cryoStorageDeviceRepository, concentrationValidator);

        builder = Grid.builder()
                .label("Label")
                .gridType(GridType.builder().id(1L).build())
                .sample(Sample.builder().id(1L).build())
                .surfaceTreatmentProtocol(SurfaceTreatmentProtocol.builder().id(1L).build());

        when(gridTypeRepository.exists(anyLong())).thenReturn(true);
        when(sampleRepository.exists(anyLong())).thenReturn(true);
        when(surfaceTreatmentProtocolRepository.exists(anyLong())).thenReturn(true);
        when(vitrificationProtocolRepository.exists(anyLong())).thenReturn(true);
        when(negativeStainProtocolRepository.exists(anyLong())).thenReturn(true);
    }

    @Test
    public void shouldSupportGrid() {
        assertTrue(tested.supports(Grid.class));
    }

    @Test
    public void shouldProtocolTypeStainFieldBeRequired() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Stain)
                .negativeStainProtocol(NegativeStainProtocol.builder().id(1L).build())
                .build(), errors);

        verify(errors).rejectValue(eq("storageBoxLabelNumber"), anyString());
        verify(errors).rejectValue(eq("positionWithinBox"), anyString());
    }

    @Test
    public void shouldProtocolTypeStainRejectCryoFields() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Stain)
                .negativeStainProtocol(NegativeStainProtocol.builder().id(1L).build())
                .storageBoxLabelNumber("test")
                .positionWithinBox("test")
                .cryoStorageDevice(CryoStorageDevice.builder().build())
                .cylinderNumberLabel("cylinder")
                .tubeNumberLabel("tube")
                .boxNumberLabel("box")
                .build(), errors);

        verify(errors).rejectValue(eq("cryoStorageDevice"), anyString());
        verify(errors).rejectValue(eq("cylinderNumberLabel"), anyString());
        verify(errors).rejectValue(eq("tubeNumberLabel"), anyString());
        verify(errors).rejectValue(eq("boxNumberLabel"), anyString());
    }

    @Test
    public void shouldStorageBoxLabelNumberBeLessEqualTo16Char() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Stain)
                .negativeStainProtocol(NegativeStainProtocol.builder().id(1L).build())
                .storageBoxLabelNumber("01234567890123456")
                .positionWithinBox("test")
                .build(), errors);
        verify(errors).rejectValue(eq("storageBoxLabelNumber"), anyString());
    }

    @Test
    public void shouldPositionWithinBoxBeLessEqualTo4Char() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Stain)
                .negativeStainProtocol(NegativeStainProtocol.builder().id(1L).build())
                .storageBoxLabelNumber("test")
                .positionWithinBox("01234")
                .build(), errors);
        verify(errors).rejectValue(eq("positionWithinBox"), anyString());
    }

    @Test
    public void shouldProtocolTypeCryoFieldsBeRequired() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Cryo)
                .vitrificationProtocol(VitrificationProtocol.builder().id(1L).build())
                .cryoStorageDevice(CryoStorageDevice.builder().id(1L)
                        .hasCylinders(true)
                        .hasTubes(true)
                        .hasBoxes(true)
                        .build())
                .build(), errors);

        verify(errors).rejectValue(eq("cylinderNumberLabel"), anyString());
        verify(errors).rejectValue(eq("tubeNumberLabel"), anyString());
        verify(errors).rejectValue(eq("boxNumberLabel"), anyString());
    }

    @Test
    public void shouldProtocolTypeCryoFieldsBeRejectedWhenDeviceDoesntHaveThem() {
        when(cryoStorageDeviceRepository.exists(anyLong())).thenReturn(true);
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Cryo)
                .vitrificationProtocol(VitrificationProtocol.builder().id(1L).build())
                .cryoStorageDevice(CryoStorageDevice.builder().id(1L)
                        .hasCylinders(false)
                        .hasTubes(false)
                        .hasBoxes(false)
                        .build())
                .cylinderNumberLabel("test")
                .tubeNumberLabel("test")
                .boxNumberLabel("test")
                .build(), errors);

        verify(errors).rejectValue(eq("cylinderNumberLabel"), anyString());
        verify(errors).rejectValue(eq("tubeNumberLabel"), anyString());
        verify(errors).rejectValue(eq("boxNumberLabel"), anyString());
    }

    @Test
    public void shouldProtocolTypeCryoRejectStainFields() {
        when(cryoStorageDeviceRepository.exists(anyLong())).thenReturn(true);
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Cryo)
                .vitrificationProtocol(VitrificationProtocol.builder().id(1L).build())
                .cryoStorageDevice(CryoStorageDevice.builder().id(1L)
                        .hasCylinders(false)
                        .hasTubes(false)
                        .hasBoxes(false)
                        .build())
                .storageBoxLabelNumber("test")
                .positionWithinBox("test")
                .build(), errors);
        verify(errors).rejectValue(eq("storageBoxLabelNumber"), anyString());
        verify(errors).rejectValue(eq("positionWithinBox"), anyString());
    }

    @Test
    public void shouldCryoStorageDeviceBeRequired() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Cryo)
                .vitrificationProtocol(VitrificationProtocol.builder().id(1L).build())
                .build(), errors);

        verify(errors).rejectValue(eq("cryoStorageDevice"), anyString());
    }

    @Test
    public void shouldCryoStorageDeviceCheckIfExists() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Cryo)
                .vitrificationProtocol(VitrificationProtocol.builder().id(1L).build())
                .cryoStorageDevice(CryoStorageDevice.builder().id(2L)
                        .hasCylinders(false)
                        .hasTubes(false)
                        .hasBoxes(false)
                        .build())
                .build(), errors);
        verify(cryoStorageDeviceRepository, times(1)).exists(2L);
    }

    @Test
    public void shouldCylinderNumberLabelBeLessEqualTo24Char() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Cryo)
                .vitrificationProtocol(VitrificationProtocol.builder().id(1L).build())
                .cryoStorageDevice(CryoStorageDevice.builder().id(1L)
                        .hasCylinders(true)
                        .hasTubes(false)
                        .hasBoxes(false)
                        .build())
                .cylinderNumberLabel("0123456789012345678901234")
                .build(), errors);
        verify(errors).rejectValue(eq("cylinderNumberLabel"), anyString());
    }

    @Test
    public void shouldTubeNumberLabelBeLessEqualTo24Char() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Cryo)
                .vitrificationProtocol(VitrificationProtocol.builder().id(1L).build())
                .cryoStorageDevice(CryoStorageDevice.builder().id(1L)
                        .hasCylinders(false)
                        .hasTubes(true)
                        .hasBoxes(false)
                        .build())
                .tubeNumberLabel("0123456789012345678901234")
                .build(), errors);
        verify(errors).rejectValue(eq("tubeNumberLabel"), anyString());
    }

    @Test
    public void shouldBoxNumberLabelBeLessEqualTo24Char() {
        tested.validate(builder
                .protocolType(Grid.ProtocolType.Cryo)
                .vitrificationProtocol(VitrificationProtocol.builder().id(1L).build())
                .cryoStorageDevice(CryoStorageDevice.builder().id(1L)
                        .hasCylinders(false)
                        .hasTubes(false)
                        .hasBoxes(true)
                        .build())
                .boxNumberLabel("0123456789012345678901234")
                .build(), errors);
        verify(errors).rejectValue(eq("boxNumberLabel"), anyString());
    }

}
