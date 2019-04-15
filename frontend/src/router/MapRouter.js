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

import MapView from '@/components/App/Map/View'
import MapNew from '@/components/App/Map/New'
import MapEdit from '@/components/App/Map/Edit'
import MapCopy from '@/components/App/Map/Copy'
import MapList from '@/components/App/Map/List'

export default [
  {
    path: '',
    name: 'map',
    component: MapList,
    props: true
  },
  {
    path: 'new',
    name: 'map-new',
    component: MapNew,
    props: true,
  },
  {
    path: ':id',
    name: 'map-view',
    component: MapView,
    props: true,
  },
  {
    path: ':id/edit',
    name: 'map-edit',
    component: MapEdit,
    props: true
  },
  {
    path: 'copy/:id',
    name: 'map-copy',
    component: MapCopy,
    props: true
  }
]
