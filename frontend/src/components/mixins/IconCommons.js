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

import SampleIcon from '@/assets/images/sample_icon.svg'
import ProteinIcon from '@/assets/images/protein_icon.svg'
import LigandIcon from '@/assets/images/ligand_icon.svg'
import GridIcon from '@/assets/images/grid.svg'
import MicroscopySessionIcon from '@/assets/images/microscopy_session.svg'
import ProcessingSessionIcon from '@/assets/images/processing_session.svg'
import MapIcon from '@/assets/images/map.svg'
import ModelIcon from '@/assets/images/model.svg'

import SurfaceTreatmentProtocolIcon from '@/assets/images/surface-treatment-protocol.svg'
import VitrificationProtocolIcon from '@/assets/images/vitrification-protocol.svg'
import NegativeStainProtocolIcon from '@/assets/images/negative-stain-protocol.svg'

import SurfaceTreatmentMachineIcon from '@/assets/images/surface-treatment-machine.svg'
import VitrificationMachineIcon from '@/assets/images/vitrification-machine.svg'
import CryoStorageDeviceIcon from '@/assets/images/cryo-storage-device.svg'
import MicroscopeIcon from '@/assets/images/microscope.svg'
import ElectronDetectorIcon from '@/assets/images/electron-detector.svg'
import SampleHolderIcon from '@/assets/images/sample-holder.svg'

import GridTypeIcon from '@/assets/images/grid_type.svg'
import BlottingPaperIcon from '@/assets/images/blotting-paper.svg'

import SettingsIcon from '@/assets/images/gear_active.svg'
import EquipmentIcon from '@/assets/images/equipment.svg'
import ProtocolsIcon from '@/assets/images/protocols.svg'
import ConsumableIcon from '@/assets/images/consumable.svg'
import AdminIcon from '@/assets/images/admin.svg'
import CloseIcon from '@/assets/images/close.svg'
import Software from '@/assets/images/software.svg'
import ImageProcessingSoftware from '@/assets/images/image_processing_software.svg'

export default {

  name: 'icon-commons',

  data () {
    return {
      icons: {
        sample: SampleIcon,
        protein: ProteinIcon,
        ligand: LigandIcon,
        grid: GridIcon,
        microscopySession: MicroscopySessionIcon,
        processingSession: ProcessingSessionIcon,
        map: MapIcon,
        model: ModelIcon,
        surfaceTreatmentProtocol: SurfaceTreatmentProtocolIcon,
        vitrificationProtocol: VitrificationProtocolIcon,
        negativeStainProtocol: NegativeStainProtocolIcon,
        surfaceTreatmentMachine: SurfaceTreatmentMachineIcon,
        vitrificationMachine: VitrificationMachineIcon,
        cryoStorageDevice: CryoStorageDeviceIcon,
        microscope: MicroscopeIcon,
        electronDetector: ElectronDetectorIcon,
        sampleHolder: SampleHolderIcon,
        gridType: GridTypeIcon,
        blottingPaper: BlottingPaperIcon,
        settings: SettingsIcon,
        equipment: EquipmentIcon,
        protocols: ProtocolsIcon,
        consumable: ConsumableIcon,
        admin: AdminIcon,
        close: CloseIcon,
        software: Software,
        imageProcessingSoftware: ImageProcessingSoftware
      }
    }
  }
}
