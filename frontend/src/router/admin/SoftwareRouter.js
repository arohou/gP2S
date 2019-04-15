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

import ImageProcessingSoftwareList from '@/components/App/Admin/ImageProcessingSoftware/List'
import ImageProcessingSoftwareView from '@/components/App/Admin/ImageProcessingSoftware/View'
import ImageProcessingSoftwareNew from '@/components/App/Admin/ImageProcessingSoftware/New'
import ImageProcessingSoftwareEdit from '@/components/App/Admin/ImageProcessingSoftware/Edit'
import ImageProcessingSoftwareCopy from '@/components/App/Admin/ImageProcessingSoftware/Copy'

export default [

  //region IMAGE PROCESSING
  {
    path: 'image_processing',
    name: 'admin-software-image_processing',
    component: ImageProcessingSoftwareList
  },
  {
    path: 'image_processing/new',
    name: 'admin-software-image_processing-new',
    component: ImageProcessingSoftwareNew,
    props: true
  },
  {
    path: 'image_processing/:id',
    name: 'admin-software-image_processing-view',
    component: ImageProcessingSoftwareView,
    props: true
  },
  {
    path: 'image_processing/:id/edit',
    name: 'admin-software-image_processing-edit',
    component: ImageProcessingSoftwareEdit,
    props: true
  },
  {
    path: 'image_processing/copy/:id',
    name: 'admin-software-image_processing-copy',
    component: ImageProcessingSoftwareCopy,
    props: true
  },
  //endregion
]
