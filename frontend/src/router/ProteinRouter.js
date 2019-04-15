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

import ProteinList from '@/components/App/Protein/List'
import ProteinView from '@/components/App/Protein/View'
import ProteinWithRegistrationSystemView from '@/components/App/Protein/registration_system_ext/View'
import ProteinNew from '@/components/App/Protein/New'
import ProteinWithRegistrationSystemNew from '@/components/App/Protein/registration_system_ext/New'
import ProteinEdit from '@/components/App/Protein/Edit'
import ProteinWithRegistrationSystemEdit from '@/components/App/Protein/registration_system_ext/Edit'
import ProteinCopy from '@/components/App/Protein/Copy'
import ProteinWithRegistrationSystemCopy from '@/components/App/Protein/registration_system_ext/Copy'
import { proteinRegistrationSystemExists } from '@/utils/ExternalSystemUtils'

export default [
  {
    path: '',
    name: 'protein',
    component: ProteinList,
    props: true
  },
  {
    path: 'new',
    name: 'protein-new',
    component: proteinRegistrationSystemExists() ? ProteinWithRegistrationSystemNew : ProteinNew,
    props: true
  },
  {
    path: ':id',
    name: 'protein-view',
    component: proteinRegistrationSystemExists() ? ProteinWithRegistrationSystemView : ProteinView,
    props: true
  },
  {
    path: ':id/edit',
    name: 'protein-edit',
    component: proteinRegistrationSystemExists() ? ProteinWithRegistrationSystemEdit : ProteinEdit,
    props: true
  },
  {
    path: 'copy/:id',
    name: 'protein-copy',
    component: proteinRegistrationSystemExists() ? ProteinWithRegistrationSystemCopy : ProteinCopy,
    props: true
  }
]
