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

import SurfaceTreatmentProtocolList from '@/components/App/Admin/SurfaceTreatmentProtocol/List'
import SurfaceTreatmentProtocolView from '@/components/App/Admin/SurfaceTreatmentProtocol/View'
import SurfaceTreatmentProtocolNew from  '@/components/App/Admin/SurfaceTreatmentProtocol/New'
import SurfaceTreatmentProtocolEdit from '@/components/App/Admin/SurfaceTreatmentProtocol/Edit'
import SurfaceTreatmentProtocolCopy from '@/components/App/Admin/SurfaceTreatmentProtocol/Copy'

import NegativeStainProtocolList from '@/components/App/Admin/NegativeStainProtocol/List'
import NegativeStainProtocolView from '@/components/App/Admin/NegativeStainProtocol/View'
import NegativeStainProtocolNew from  '@/components/App/Admin/NegativeStainProtocol/New'
import NegativeStainProtocolEdit from '@/components/App/Admin/NegativeStainProtocol/Edit'
import NegativeStainProtocolCopy from '@/components/App/Admin/NegativeStainProtocol/Copy'

import VitrificationProtocolList from '@/components/App/Admin/VitrificationProtocol/List'
import VitrificationProtocolView from '@/components/App/Admin/VitrificationProtocol/View'
import VitrificationProtocolNew from  '@/components/App/Admin/VitrificationProtocol/New'
import VitrificationProtocolEdit from '@/components/App/Admin/VitrificationProtocol/Edit'
import VitrificationProtocolCopy from '@/components/App/Admin/VitrificationProtocol/Copy'

export default [
  /***************** SURFACE TREATMENT ********************/
  {
    path: 'surface_treatment',
    name: 'admin-protocols-surface_treatment',
    component: SurfaceTreatmentProtocolList
  },
  {
    path: 'surface_treatment/new',
    name: 'admin-protocols-surface_treatment-new',
    component: SurfaceTreatmentProtocolNew,
    props: true
  },
  {
    path: 'surface_treatment/:id',
    name: 'admin-protocols-surface_treatment-view',
    component: SurfaceTreatmentProtocolView,
    props: true
  },
  {
    path: 'surface_treatment/:id/edit',
    name: 'admin-protocols-surface_treatment-edit',
    component: SurfaceTreatmentProtocolEdit,
    props: true
  },
  {
    path: 'surface_treatment/copy/:id',
    name: 'admin-protocols-surface_treatment-copy',
    component: SurfaceTreatmentProtocolCopy,
    props: true
  },
  /***************** NEGATIVE STAIN ********************/
  {
    path: 'negative_stain',
    name: 'admin-protocols-negative_stain',
    component: NegativeStainProtocolList
  },
  {
    path: 'negative_stain/new',
    name: 'admin-protocols-negative_stain-new',
    component: NegativeStainProtocolNew,
    props: true
  },
  {
    path: 'negative_stain/:id',
    name: 'admin-protocols-negative_stain-view',
    component: NegativeStainProtocolView,
    props: true
  },
  {
    path: 'negative_stain/:id/edit',
    name: 'admin-protocols-negative_stain-edit',
    component: NegativeStainProtocolEdit,
    props: true
  },
  {
    path: 'negative_stain/copy/:id',
    name: 'admin-protocols-negative_stain-copy',
    component: NegativeStainProtocolCopy,
    props: true
  },
  /***************** VITRIFICATION ********************/
  {
    path: 'vitrification',
    name: 'admin-protocols-vitrification',
    component: VitrificationProtocolList
  },
  {
    path: 'vitrification/new',
    name: 'admin-protocols-vitrification-new',
    component: VitrificationProtocolNew,
    props: true
  },
  {
    path: 'vitrification/:id',
    name: 'admin-protocols-vitrification-view',
    component: VitrificationProtocolView,
    props: true
  },
  {
    path: 'vitrification/:id/edit',
    name: 'admin-protocols-vitrification-edit',
    component: VitrificationProtocolEdit,
    props: true
  },
  {
    path: 'vitrification/copy/:id',
    name: 'admin-protocols-vitrification-copy',
    component: VitrificationProtocolCopy,
    props: true
  }
]
