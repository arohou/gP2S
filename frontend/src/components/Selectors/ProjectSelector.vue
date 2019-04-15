<!--
  - Copyright 2018 Genentech Inc.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <div class="project-selector">
    <el-select v-model="value" filterable noDataText="No projects available" @change="selectProject">
      <el-option v-for="item in projects" :key="item.slug" :value="item.slug" :label="item.label"></el-option>
    </el-select>
  </div>
</template>

<script>
  import Service from '@/services/ProjectService'

  const service = new Service()

  export default {
    name: 'project-selector',

    data () {
      return {
        projects: [],
        value: ''
      }
    },

    watch: {
      '$route' (to, from) {
        if (to.params.projectId !== from.params.projectId) {
          this.projectIdChanged(to)
        }
      }
    },

    methods: {
      projectIdChanged (to) {
        let projectId = to.params.projectId
        if (!projectId) {
          return
        }

        let projectFromPath = null
        if (!isNaN(Number.parseInt(projectId))) {
          projectId = Number.parseInt(projectId)
          projectFromPath = this.projects.find(project => project.id === projectId)
        } else {
          projectFromPath = this.projects.find(project => project.slug === projectId)
        }

        if (!projectFromPath) {
          this.$log.warn('Unknown project id in URL path: ', projectId)
          return
        }

        this.value = projectFromPath.slug
        this.$emit('projectSelected', this.value)
      },

      selectProject () {
        this.$log.debug('Project Selector event: [projectSelected], projectId: ', this.value)

        if (!!this.$route.name) {
          this.$router.push({
            name: this.$route.name,
            params: {projectId: this.value}
          })
        } else {
          this.$router.push({
            name: 'project',
            params: {projectId: this.value}
          })
        }
      },

      fetchProjects () {
        return service.listEntities()
          .then(result => { this.projects = result.data })
          .catch(error => this.$log.error(error))
      }
    },

    mounted () {
      this.fetchProjects().then(() => {
        this.projectIdChanged(this.$route)
      })
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .project-selector {
    float: left;
    margin-right: 0.5ex;

    /deep/ .el-input__icon {
      color: $text-header-color--disabled
    }
  }


</style>
