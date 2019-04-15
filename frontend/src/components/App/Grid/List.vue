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
  <div class="grid-list-view list-view scrollable-container">
    <div class="list-view__header">
      <el-button class="list-view__action-buttons__submit" @click="$router.push({ name: 'grid-new' })">
        Create new grid
      </el-button>
    </div>
    <el-col class="grid-view__components scrollable" :xs="15">

      <grid-list-item v-for="(grid, index) in grids"
                      :key="index"
                      :entity="grid"
                      :index="index">
      </grid-list-item>
    </el-col>
  </div>
</template>

<script>
  import Service from '@/services/GridService'
  import Commons from '@/components/App/Grid/GridCommons'
  import GridListItem from '@/components/App/Grid/ListItem'

  const service = new Service()

  export default {
    name: 'grid',

    data () {
      return {
        grids: []
      }
    },

    mixins: [
      Commons
    ],

    components: {
      GridListItem
    },

    watch: {
      projectId: function () {
        this.loadGrids()
      }
    },

    methods: {
      loadGrids () {
        service.listEntitiesByProject(this.projectId)
          .then(result => {
            this.grids = result.data
          })
          .catch(error => this.$log.error(error))
      }
    },

    mounted () {
      this.loadGrids()
    }
  }

</script>
