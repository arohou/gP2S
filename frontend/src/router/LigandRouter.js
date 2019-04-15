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

import LigandView from '@/components/App/Ligand/View'
import LigandNew from '@/components/App/Ligand/New'
import LigandEdit from '@/components/App/Ligand/Edit'
import LigandCopy from '@/components/App/Ligand/Copy'
import LigandList from '@/components/App/Ligand/List'

import LigandWithRegistrationSystemView from '@/components/App/Ligand/registration_system_ext/View'
import LigandWithRegistrationSystemNew from '@/components/App/Ligand/registration_system_ext/New'
import LigandWithRegistrationSystemEdit from '@/components/App/Ligand/registration_system_ext/Edit'
import LigandWithRegistrationSystemCopy from '@/components/App/Ligand/registration_system_ext/Copy'
import LigandWithRegistrationSystemList from '@/components/App/Ligand/registration_system_ext/List'

import { ligandRegistrationSystemExists } from '@/utils/ExternalSystemUtils'

export default [
  {
    path: '',
    name: 'ligand',
    component: ligandRegistrationSystemExists() ? LigandWithRegistrationSystemList : LigandList,
    props: true
  },
  {
    path: 'new',
    name: 'ligand-new',
    component: ligandRegistrationSystemExists() ? LigandWithRegistrationSystemNew : LigandNew,
    props: true,
  },
  {
    path: ':id',
    name: 'ligand-view',
    component: ligandRegistrationSystemExists() ? LigandWithRegistrationSystemView : LigandView,
    props: true,
  },
  {
    path: ':id/edit',
    name: 'ligand-edit',
    component: ligandRegistrationSystemExists() ? LigandWithRegistrationSystemEdit : LigandEdit,
    props: true
  },
  {
    path: 'copy/:id',
    name: 'ligand-copy',
    component: ligandRegistrationSystemExists() ? LigandWithRegistrationSystemCopy : LigandCopy,
    props: true
  }
]
