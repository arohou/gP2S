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

import ProtocolsRouter from '@/router/admin/ProtocolsRouter'
import EquipmentRouter from '@/router/admin/EquipmentRouter'
import ConsumablesRouter from '@/router/admin/ConsumablesRouter'
import AdminRouter from '@/router/admin/AdminRouter'
import SoftwareRouter from '@/router/admin/SoftwareRouter'

Vue.use(Router)

export default {
  name: 'admin',
  path: '/admin',
  component: {template: '<router-view></router-view>'},
  children: [
    {
      name: 'admin-protocols',
      path: 'protocols',
      component: {template: '<router-view></router-view>'},
      children: ProtocolsRouter
    },
    {
      name: 'admin-equipment',
      path: 'equipment',
      component: {template: '<router-view></router-view>'},
      children: EquipmentRouter
    },
    {
      name: 'admin-consumables',
      path: 'consumables',
      component: {template: '<router-view></router-view>'},
      children: ConsumablesRouter
    },
    {
      name: 'admin-software',
      path: 'software',
      component: {template: '<router-view></router-view>'},
      children: SoftwareRouter
    },
    {
      name: 'admin-admin',
      path: 'admin',
      component: {template: '<router-view></router-view>'},
      children: AdminRouter
    },
  ]
}
