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

import ProjectList from '@/components/App/Admin/Project/List'
import ProjectWithManagementSystemList from '@/components/App/Admin/Project/management_system_ext/List'
import ProjectView from '@/components/App/Admin/Project/View'
import ProjectWithManagementSystemView from '@/components/App/Admin/Project/management_system_ext/View'
import ProjectNew from '@/components/App/Admin/Project/New'
import ProjectWithManagementSystemNew from '@/components/App/Admin/Project/management_system_ext/New'
import ProjectEdit from '@/components/App/Admin/Project/Edit'
import ProjectWithManagementSystemEdit from '@/components/App/Admin/Project/management_system_ext/Edit'
import ProjectCopy from '@/components/App/Admin/Project/Copy'
import ProjectWithManagementSystemCopy from '@/components/App/Admin/Project/management_system_ext/Copy'
import SettingsView from '@/components/App/Admin/Settings/View'
import SettingsEdit from '@/components/App/Admin/Settings/Edit'
import { projectManagementSystemExists } from '@/utils/ExternalSystemUtils'

export default [
  //region PROJECT
  {
    path: 'project',
    name: 'admin-admin-project',
    component: projectManagementSystemExists() ? ProjectWithManagementSystemList : ProjectList,
  },
  {
    path: 'project/new',
    name: 'admin-admin-project-new',
    component: projectManagementSystemExists() ? ProjectWithManagementSystemNew : ProjectNew,
    props: true
  },
  {
    path: 'project/:id',
    name: 'admin-admin-project-view',
    component: projectManagementSystemExists() ? ProjectWithManagementSystemView : ProjectView,
    props: true
  },
  {
    path: 'project/:id/edit',
    name: 'admin-admin-project-edit',
    component: projectManagementSystemExists() ? ProjectWithManagementSystemEdit : ProjectEdit,
    props: true
  },
  {
    path: 'project/copy/:id',
    name: 'admin-admin-project-copy',
    component: projectManagementSystemExists() ? ProjectWithManagementSystemCopy : ProjectCopy,
    props: true
  },
  //endregion

  //region SETTINGS
  {
    path: 'settings',
    name: 'admin-admin-settings-view',
    component: SettingsView
  },
  {
    path: 'settings/edit',
    name: 'admin-admin-settings-edit',
    component: SettingsEdit
  },
  //endregion
]
