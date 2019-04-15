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
  <div class="ligand-list-view list-view scrollable-container">
    <div class="list-view__header">
      <el-button class="list-view__action-buttons__submit" @click="$router.push({ name: 'ligand-new' })">
        Create new ligand
      </el-button>
    </div>

    <el-col class="ligand-view__components scrollable" :xs="15">
      <el-row v-for="(ligand, index) in ligands"
              :key="index">
        <ligand-item :entity="ligand"
                     :index="index"
                     concentrationLabel="Concentration">
        </ligand-item>
      </el-row>
    </el-col>
  </div>
</template>

<script>
  import Service from '@/services/LigandService'
  import Commons from '@/components/App/Ligand/Commons'
  import LigandItem from '@/components/App/Ligand/ListItem'

  const service = new Service()

  export default {
    name: 'ligand',

    data () {
      return {
        ligands: []
      }
    },

    mixins: [
      Commons
    ],

    components: {
      LigandItem
    },

    watch: {
      projectId: function () {
        this.loadLigands()
      }
    },

    methods: {
      loadLigands () {
        service.listEntitiesByProject(this.projectId)
          .then(result => {
            this.ligands = result.data
          })
          .catch(error => this.$log.error(error))
      }
    },

    mounted () {
      this.loadLigands()
    }
  }

</script>

