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

import Vue from 'vue'
import Router from 'vue-router'

import ProteinRouter from '@/router/ProteinRouter'
import LigandRouter from '@/router/LigandRouter'
import SampleRouter from '@/router/SampleRouter'
import GridRouter from '@/router/GridRouter'
import MicroscopySessionRouter from '@/router/MicroscopySessionRouter'
import ProcessingSessionRouter from '@/router/ProcessingSessionRouter'
import MapRouter from '@/router/MapRouter'
import ModelRouter from '@/router/ModelRouter'
import AdminRouter from '@/router/admin/index'

Vue.use(Router)

export default new Router({
  routes: [
    AdminRouter,
    {
      name: 'error',
      path: '/error/:errorCode',
      component: null,
      props: true
    },
    {
      name: 'project',
      path: '/:projectId',
      component: {template: '<router-view></router-view>'},
      children: [
        {
          path: 'protein',
          component: {template: '<router-view></router-view>'},
          children: ProteinRouter
        },
        {
          path: 'ligand',
          component: {template: '<router-view></router-view>'},
          children: LigandRouter
        },
        {
          path: 'sample',
          component: {template: '<router-view></router-view>'},
          children: SampleRouter
        },
        {
          path: 'grid',
          component: {template: '<router-view></router-view>'},
          children: GridRouter
        },
        {
          path: 'microscopy_session',
          component: {template: '<router-view></router-view>'},
          children: MicroscopySessionRouter
        },
        {
          path: 'processing_session',
          component: {template: '<router-view></router-view>'},
          children: ProcessingSessionRouter
        },
        {
          path: 'map',
          component: {template: '<router-view></router-view>'},
          children: MapRouter
        },
        {
          path: 'model',
          component: {template: '<router-view></router-view>'},
          children: ModelRouter
        }
      ]
    },
  ]
})

