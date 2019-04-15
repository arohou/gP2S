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

/**
 * @author Przemysław Stankowski on 19.01.2018.
 */

import ProcessingSessionView from '@/components/App/ProcessingSession/View'
import ProcessingSessionNew from '@/components/App/ProcessingSession/New'
import ProcessingSessionEdit from '@/components/App/ProcessingSession/Edit'
import ProcessingSessionCopy from '@/components/App/ProcessingSession/Copy'
import ProcessingSessionList from '@/components/App/ProcessingSession/List'

export default [
  {
    path: '',
    name: 'processing_session',
    component: ProcessingSessionList,
    props: true
  },
  {
    path: 'new',
    name: 'processing_session-new',
    component: ProcessingSessionNew,
    props: true,
  },
  {
    path: ':id',
    name: 'processing_session-view',
    component: ProcessingSessionView,
    props: true,
  },
  {
    path: ':id/edit',
    name: 'processing_session-edit',
    component: ProcessingSessionEdit,
    props: true
  },
  {
    path: 'copy/:id',
    name: 'processing_session-copy',
    component: ProcessingSessionCopy,
    props: true
  }
]
