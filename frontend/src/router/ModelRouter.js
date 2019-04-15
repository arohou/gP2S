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

import ModelView from '@/components/App/Model/View'
import ModelNew from '@/components/App/Model/New'
import ModelEdit from '@/components/App/Model/Edit'
import ModelCopy from '@/components/App/Model/Copy'
import ModelList from '@/components/App/Model/List'

export default [
  {
    path: '',
    name: 'model',
    component: ModelList,
    props: true
  },
  {
    path: 'new',
    name: 'model-new',
    component: ModelNew,
    props: true,
  },
  {
    path: ':id',
    name: 'model-view',
    component: ModelView,
    props: true,
  },
  {
    path: ':id/edit',
    name: 'model-edit',
    component: ModelEdit,
    props: true
  },
  {
    path: 'copy/:id',
    name: 'model-copy',
    component: ModelCopy,
    props: true
  }
]
