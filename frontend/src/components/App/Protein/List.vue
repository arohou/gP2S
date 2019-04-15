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
  <div class="protein-list-view list-view scrollable-container">
    <div class="list-view__header">
      <el-button class="list-view__action-buttons__submit" @click="$router.push({ name: 'protein-new' })">
        Create new protein
      </el-button>
    </div>

    <el-col class="protein-view__components scrollable" :xs="15">
      <el-row v-for="(protein, index) in proteins"
              :key="index">
        <protein-item concentrationLabel="Dilution or concentration factor"
                      concentrationUnit="×"
                      :entity="protein"
                      :index="index">
          <concentration-view slot="concentration" :concentration="protein.concentration" listItem/>
        </protein-item>
      </el-row>
    </el-col>
  </div>
</template>

<script>
  import Service from '@/services/ProteinService'
  import Commons from '@/components/App/Protein/Commons'
  import ProteinItem from '@/components/App/Protein/ListItem'
  import ConcentrationView from '@/components/App/ConcentrationView'

  const service = new Service()

  export default {
    name: 'protein',

    data () {
      return {
        proteins: []
      }
    },

    mixins: [
      Commons
    ],

    components: {ProteinItem, ConcentrationView},

    watch: {
      projectId: function () {
        this.loadProteins()
      }
    },

    methods: {
      loadProteins () {
        service.listEntitiesByProject(this.projectId)
          .then(result => {
            this.proteins = result.data
          })
          .catch(error => this.$log.error(error))
      }
    },

    mounted () {
      this.loadProteins()
    }
  }
</script>
