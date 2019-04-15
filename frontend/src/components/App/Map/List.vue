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
  <div class="map-list-view list-view scrollable-container">
    <div class="list-view__header">
      <el-button class="list-view__action-buttons__submit" @click="$router.push({ name: 'map-new' })">
        Create new map
      </el-button>
    </div>

    <el-col class="map-view__components scrollable" :xs="15">
      <el-row v-for="(entity, index) in entities"
              :key="index">
        <list-item
          :entity="entity"
          :index="index"
          :projectId="projectId"
          entityType="MAP">
        </list-item>
      </el-row>
    </el-col>
  </div>
</template>

<script>
  import Service from '@/services/MapService'
  import Commons from '@/components/App/Map/Commons'
  import ListItem from '@/components/App/Map/ListItem'

  const service = new Service()

  export default {
    name: 'map',

    data () {
      return {
        entities: []
      }
    },

    mixins: [
      Commons
    ],

    components: {
      ListItem
    },

    watch: {
      projectId: function () {
        this.loadEntities()
      }
    },

    methods: {
      loadEntities () {
        service.listEntitiesByProject(this.projectId)
          .then(result => {
            this.entities = result.data
          })
          .catch(error => this.$log.error(error))
      }
    },

    mounted () {
      this.loadEntities()
    }
  }

</script>

