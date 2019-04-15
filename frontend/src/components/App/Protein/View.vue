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
  <view-comment-base entityType="protein" :entity="protein" :icon="icon">
    <div slot="component-protocol-tag-wrapper">
      <div class="component-tag-protein">Protein</div>
    </div>
    <div v-if="protein.availableForSampleMaking" slot="component-protocol-tag-wrapper" class="component-tag-available">
      Available
    </div>
    <el-row class="view__metadata__row">
      <el-col :sm="6">
        <label class="view__metadata__label">PUR ID</label>
        <div class="view__metadata__value" id="protein-incubationTime">
          {{ protein.purificationId | formatValue }}
        </div>
      </el-col>
      <el-col :sm="6">
        <concentration-view :concentration="protein.concentration"/>
      </el-col>
      <el-col :sm="6">
        <label class="view__metadata__label">Tube labeling</label>
        <div class="view__metadata__value" id="protein-tube-labeling">{{protein.tubeLabel | formatUnit}}</div>
      </el-col>
      <el-col :sm="3">
        <label class="view__metadata__label">&nbsp;</label>
        <div class="view__metadata__value" id="protein-solubization-matrix">&nbsp;</div>
      </el-col>
      <el-col :sm="3">
        <label class="view__metadata__label">&nbsp;</label>
        <div class="view__metadata__value" id="protein-cryorepresentative">&nbsp;</div>
      </el-col>
    </el-row>
  </view-comment-base>
</template>

<script>
  import Commons from '@/components/App/Protein/Commons'
  import ProteinRegistrationSystemCommons from '@/components/App/Protein/registration_system_ext/ProteinRegistrationSystemCommons'
  import ProjectResidentCommons from '@/components/mixins/ProjectResidentCommons'
  import ViewCommentBase from '@/components/App/ViewCommentBase'
  import ConcentrationView from '@/components/App/ConcentrationView'

  export default {
    name: 'protein-view',

    props: ['id'],

    mixins: [
      Commons, ProteinRegistrationSystemCommons, ProjectResidentCommons
    ],

    components: {
      ViewCommentBase, ConcentrationView
    },

    watch: {
      'id' (id) {
        this.loadData(id)
      }
    },

    methods: {
      initEvents () {
        this.$events.on('actionEdit', () => this.actionEdit('protein', this.id))
        this.$events.on('actionCopy', () => this.actionCopy('protein', this.id))
      },

      removeEvents () {
        this.$events.off('actionEdit')
        this.$events.off('actionCopy')
      },

      loadData (id) {
        this.fetchProtein(id)
      }
    },

    mounted () {
      this.$log.debug('props.id: ', this.id)
      this.loadData(this.id)
      this.initEvents()
    },

    beforeDestroy () {
      this.removeEvents()
    }
  }
</script>

<style scoped>
  .component-tag-available {
    margin-left: 0.662rem;
  }
</style>
