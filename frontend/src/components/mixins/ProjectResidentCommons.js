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
  methods: {
    redirectToDashboard() { // Redirecting to list view e.g. sample-edit -> sample
      this.$router.push({name: (this.$route.name || '').split('-')[0]})
    },

    redirectToDashboardIfNotInProject() {
      let projectIdMatch = this.projectId.match('[0-9]+')
      let isNumericProjectId = projectIdMatch && projectIdMatch[0] == projectIdMatch.input

      if (this.projectsData && this.projectsData.length > 0 && this.projectId
        && !(this.projectsData.map(isNumericProjectId ? project => project.id : project => project.slug) || [])
          .includes(isNumericProjectId ? parseInt(this.projectId) : this.projectId)) {
        this.redirectToDashboard()
      }
    }
  },

  watch: {
    projectsData() {
      this.redirectToDashboardIfNotInProject();
    },
    projectId() {
      let currentRouteName = (this.$route.name || '').split('-')

      // Redirect changes when user switches to another project in 'copy' mode or if user edits/crates a sample
      if (currentRouteName && currentRouteName.length > 1 && (currentRouteName[0] === 'sample' || currentRouteName[1] === 'copy')) {
        this.redirectToDashboard()
      }
      this.redirectToDashboardIfNotInProject();
    }
  }
}
