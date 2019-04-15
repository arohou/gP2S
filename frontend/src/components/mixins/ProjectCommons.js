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

export default {

  data () {
    return {
      projectId: null,
      defaultProject: null
    }
  },

  watch: {
    '$route.params.projectId' (projectId) {
      this.projectId = projectId
      if(this.projectId){
        this.defaultProject = this.projectId
      }
    }
  },

  computed: {
    defaultSampleRoute: function () {
      if(this.projectId){
        return { name: 'sample', params: {projectId: this.projectId} }
      } else if(this.defaultProject){
        return { name: 'sample', params: {projectId: this.defaultProject} }
      } else {
        return { path: '/' }
      }
    }
  },

  created () {
    this.projectId = this.$route.params.projectId || null
  }
}
