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

import SurfaceTreatmentMachineList from '@/components/App/Admin/SurfaceTreatmentMachine/List'
import SurfaceTreatmentMachineView from '@/components/App/Admin/SurfaceTreatmentMachine/View'
import SurfaceTreatmentMachineNew from '@/components/App/Admin/SurfaceTreatmentMachine/New'
import SurfaceTreatmentMachineEdit from '@/components/App/Admin/SurfaceTreatmentMachine/Edit'
import SurfaceTreatmentMachineCopy from '@/components/App/Admin/SurfaceTreatmentMachine/Copy'

import VitrificationMachineList from '@/components/App/Admin/VitrificationMachine/List'
import VitrificationMachineView from '@/components/App/Admin/VitrificationMachine/View'
import VitrificationMachineNew from '@/components/App/Admin/VitrificationMachine/New'
import VitrificationMachineEdit from '@/components/App/Admin/VitrificationMachine/Edit'
import VitrificationMachineCopy from '@/components/App/Admin/VitrificationMachine/Copy'

import CryoStorageDeviceList from '@/components/App/Admin/CryoStorageDevice/List'
import CryoStorageDeviceView from '@/components/App/Admin/CryoStorageDevice/View'
import CryoStorageDeviceNew from '@/components/App/Admin/CryoStorageDevice/New'
import CryoStorageDeviceEdit from '@/components/App/Admin/CryoStorageDevice/Edit'
import CryoStorageDeviceCopy from '@/components/App/Admin/CryoStorageDevice/Copy'

import MicroscopeList from '@/components/App/Admin/Microscope/List'
import MicroscopeView from '@/components/App/Admin/Microscope/View'
import MicroscopeNew from '@/components/App/Admin/Microscope/New'
import MicroscopeEdit from '@/components/App/Admin/Microscope/Edit'
import MicroscopeCopy from '@/components/App/Admin/Microscope/Copy'

import ElectronDetectorList from '@/components/App/Admin/ElectronDetector/List'
import ElectronDetectorView from '@/components/App/Admin/ElectronDetector/View'
import ElectronDetectorNew from '@/components/App/Admin/ElectronDetector/New'
import ElectronDetectorEdit from '@/components/App/Admin/ElectronDetector/Edit'
import ElectronDetectorCopy from '@/components/App/Admin/ElectronDetector/Copy'

import SampleHolderList from '@/components/App/Admin/SampleHolder/List'
import SampleHolderView from '@/components/App/Admin/SampleHolder/View'
import SampleHolderNew from '@/components/App/Admin/SampleHolder/New'
import SampleHolderEdit from '@/components/App/Admin/SampleHolder/Edit'
import SampleHolderCopy from '@/components/App/Admin/SampleHolder/Copy'

export default [
  /***************** SURFACE TREATMENT MACHINES ********************/
  {
    path: 'surface_treatment_machines',
    name: 'admin-equipment-surface_treatment_machines',
    component: SurfaceTreatmentMachineList
  },
  {
    path: 'surface_treatment_machines/new',
    name: 'admin-equipment-surface_treatment_machines-new',
    component: SurfaceTreatmentMachineNew,
    props: true
  },
  {
    path: 'surface_treatment_machines/:id',
    name: 'admin-equipment-surface_treatment_machines-view',
    component: SurfaceTreatmentMachineView,
    props: true
  },
  {
    path: 'surface_treatment_machines/:id/edit',
    name: 'admin-equipment-surface_treatment_machines-edit',
    component: SurfaceTreatmentMachineEdit,
    props: true
  },
  {
    path: 'surface_treatment_machines/copy/:id',
    name: 'admin-equipment-surface_treatment_machines-copy',
    component: SurfaceTreatmentMachineCopy,
    props: true
  },
  /***************** VITRIFICATION MACHINES ********************/
  {
    path: 'vitrification_machines',
    name: 'admin-equipment-vitrification_machines',
    component: VitrificationMachineList
  },
  {
    path: 'vitrification_machines/new',
    name: 'admin-equipment-vitrification_machines-new',
    component: VitrificationMachineNew,
    props: true
  },
  {
    path: 'vitrification_machines/:id',
    name: 'admin-equipment-vitrification_machines-view',
    component: VitrificationMachineView,
    props: true
  },
  {
    path: 'vitrification_machines/:id/edit',
    name: 'admin-equipment-vitrification_machines-edit',
    component: VitrificationMachineEdit,
    props: true
  },
  {
    path: 'vitrification_machines/copy/:id',
    name: 'admin-equipment-vitrification_machines-copy',
    component: VitrificationMachineCopy,
    props: true
  },
  /***************** CRYO STORAGE DEVICE ********************/
  {
    path: 'cryo-storage-device',
    name: 'admin-equipment-cryo_storage_device',
    component: CryoStorageDeviceList
  },
  {
    path: 'cryo-storage-device/new',
    name: 'admin-equipment-cryo_storage_device-new',
    component: CryoStorageDeviceNew,
    props: true
  },
  {
    path: 'cryo-storage-device/:id',
    name: 'admin-equipment-cryo_storage_device-view',
    component: CryoStorageDeviceView,
    props: true
  },
  {
    path: 'cryo-storage-device/:id/edit',
    name: 'admin-equipment-cryo_storage_device-edit',
    component: CryoStorageDeviceEdit,
    props: true
  },
  {
    path: 'cryo-storage-device/copy/:id',
    name: 'admin-equipment-cryo_storage_device-copy',
    component: CryoStorageDeviceCopy,
    props: true
  },
  /********************** MICROSCOPE ************************/
  {
    path: 'microscopes',
    name: 'admin-equipment-microscopes',
    component: MicroscopeList
  },
  {
    path: 'microscopes/new',
    name: 'admin-equipment-microscopes-new',
    component: MicroscopeNew,
    props: true
  },
  {
    path: 'microscopes/:id',
    name: 'admin-equipment-microscopes-view',
    component: MicroscopeView,
    props: true
  },
  {
    path: 'microscopes/:id/edit',
    name: 'admin-equipment-microscopes-edit',
    component: MicroscopeEdit,
    props: true
  },
  {
    path: 'microscopes/copy/:id',
    name: 'admin-equipment-microscopes-copy',
    component: MicroscopeCopy,
    props: true
  },
  /********************** ELECTRON DETECTOR ************************/
  {
    path: 'electron-detectors',
    name: 'admin-equipment-electron_detectors',
    component: ElectronDetectorList
  },
  {
    path: 'electron-detectors/new',
    name: 'admin-equipment-electron_detectors-new',
    component: ElectronDetectorNew,
    props: true
  },
  {
    path: 'electron-detectors/:id',
    name: 'admin-equipment-electron_detectors-view',
    component: ElectronDetectorView,
    props: true
  },
  {
    path: 'electron-detectors/:id/edit',
    name: 'admin-equipment-electron_detectors-edit',
    component: ElectronDetectorEdit,
    props: true
  },
  {
    path: 'electron-detectors/copy/:id',
    name: 'admin-equipment-electron_detectors-copy',
    component: ElectronDetectorCopy,
    props: true
  },
  /***************** SAMPLE HOLDER ********************/
  {
    path: 'sample_holders',
    name: 'admin-equipment-sample_holders',
    component: SampleHolderList
  },
  {
    path: 'sample_holders/new',
    name: 'admin-equipment-sample_holders-new',
    component: SampleHolderNew,
    props: true
  },
  {
    path: 'sample_holders/:id',
    name: 'admin-equipment-sample_holders-view',
    component: SampleHolderView,
    props: true
  },
  {
    path: 'sample_holders/:id/edit',
    name: 'admin-equipment-sample_holders-edit',
    component: SampleHolderEdit,
    props: true
  },
  {
    path: 'sample_holders/copy/:id',
    name: 'admin-equipment-sample_holders-copy',
    component: SampleHolderCopy,
    props: true
  },
]
