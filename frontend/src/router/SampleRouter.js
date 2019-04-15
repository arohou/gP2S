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

import SampleNew from '@/components/App/Sample/New'
import SampleView from '@/components/App/Sample/View'
import SampleEdit from '@/components/App/Sample/Edit'
import SampleCopy from '@/components/App/Sample/Copy'
import SampleList from '@/components/App/Sample/List'

export default [
  {
    path: '',
    name: 'sample',
    component: SampleList,
    props: true
  },
  {
    path: 'new',
    name: 'sample-new',
    component: SampleNew,
    props: true
  },
  {
    path: ':id',
    name: 'sample-view',
    component: SampleView,
    props: true
  },
  {
    path: ':id/edit',
    name: 'sample-edit',
    component: SampleEdit,
    props: true
  },
  {
    path: 'copy/:id',
    name: 'sample-copy',
    component: SampleCopy,
    props: true
  }
]
