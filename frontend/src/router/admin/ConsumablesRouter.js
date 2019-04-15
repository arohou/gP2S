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

import GridTypeView from '@/components/App/Admin/GridType/View'
import GridTypeList from '@/components/App/Admin/GridType/List'
import GridTypeNew from '@/components/App/Admin/GridType/New'
import GridTypeEdit from '@/components/App/Admin/GridType/Edit'
import GridTypeCopy from '@/components/App/Admin/GridType/Copy'

import BlottingPaperList from '@/components/App/Admin/BlottingPaper/List'
import BlottingPaperView from '@/components/App/Admin/BlottingPaper/View'
import BlottingPaperNew from '@/components/App/Admin/BlottingPaper/New'
import BlottingPaperEdit from '@/components/App/Admin/BlottingPaper/Edit'
import BlottingPaperCopy from '@/components/App/Admin/BlottingPaper/Copy'

export default [
  /**************** GRID TYPE *****************/
  {
    path: 'grid_type',
    name: 'admin-consumables-grid_type',
    component: GridTypeList
  },
  {
    path: 'grid_type/new',
    name: 'admin-consumables-grid_type-new',
    component: GridTypeNew,
    props: true
  },
  {
    path: 'grid_type/:id',
    name: 'admin-consumables-grid_type-view',
    component: GridTypeView,
    props: true
  },
  {
    path: 'grid_type/:id/edit',
    name: 'admin-consumables-grid_type-edit',
    component: GridTypeEdit,
    props: true
  },
  {
    path: 'grid_type/copy/:id',
    name: 'admin-consumables-grid_type-copy',
    component: GridTypeCopy,
    props: true
  },
  /**************** BLOTTING PAPER *****************/
  {
    path: 'blot_paper_types',
    name: 'admin-consumables-blot_paper_types',
    component: BlottingPaperList
  },
  {
    path: 'blot_paper_types/new',
    name: 'admin-consumables-blot_paper_types-new',
    component: BlottingPaperNew,
    props: true
  },
  {
    path: 'blot_paper_types/:id',
    name: 'admin-consumables-blot_paper_types-view',
    component: BlottingPaperView,
    props: true
  },
  {
    path: 'blot_paper_types/:id/edit',
    name: 'admin-consumables-blot_paper_types-edit',
    component: BlottingPaperEdit,
    props: true
  },
  {
    path: 'blot_paper_types/copy/:id',
    name: 'admin-consumables-blot_paper_types-copy',
    component: BlottingPaperCopy,
    props: true
  }
]
