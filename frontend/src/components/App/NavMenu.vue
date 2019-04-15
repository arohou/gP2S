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
  <div class="nav-menu">
    <div class="nav-menu__project" v-if="!isAdminMenu">
      <el-row class="nav-menu__project-box">
        <el-row class="nav-menu__project-label">
          <span>Project</span>
        </el-row>
        <el-row>
          <project-selector class="nav-menu__project-selector"></project-selector>
        </el-row>
      </el-row>
      <el-row>
        <nav-menu-item id="sample" :icon="icons.sample" :disabled="!projectId"
                       :route="{ name: 'sample', params: {projectId: this.projectId}}">Samples {{ samplesCount |
          wrapInParentheses }}
        </nav-menu-item>
        <nav-menu-item id="protein" :icon="icons.protein" :disabled="!projectId" isSubItem
                       :route="{ name: 'protein', params: {projectId: this.projectId}}">Proteins {{ proteinsCount |
          wrapInParentheses }}
        </nav-menu-item>
        <nav-menu-item id="ligand" :icon="icons.ligand" :disabled="!projectId" isSubItem
                       :route="{ name: 'ligand', params: {projectId: this.projectId}}">Ligands {{ ligandsCount |
          wrapInParentheses }}
        </nav-menu-item>
        <nav-menu-item id="grid" :icon="icons.grid" :disabled="!projectId"
                       :route="{ name: 'grid', params: {projectId: this.projectId}}">Grids {{ gridsCount |
          wrapInParentheses }}
        </nav-menu-item>
        <nav-menu-item id="microscopySession" :icon="icons.microscopySession" :disabled="!projectId"
                       :route="{ name: 'microscopy_session', params: {projectId: this.projectId}}">
          Microscopy sessions {{ microscopySessionsCount | wrapInParentheses }}
        </nav-menu-item>
        <nav-menu-item id="processingSession" :icon="icons.processingSession" :disabled="!projectId"
                       :route="{ name: 'processing_session', params: {projectId: this.projectId}}">
          Processing sessions {{ processingSessionsCount | wrapInParentheses }}
        </nav-menu-item>
        <nav-menu-item id="map" :icon="icons.map" :disabled="!projectId"
                       :route="{ name: 'map', params: {projectId: this.projectId}}">Maps {{ mapsCount |
          wrapInParentheses
          }}
        </nav-menu-item>
        <nav-menu-item id="model" :icon="icons.model" :disabled="!projectId"
                       :route="{ name: 'model', params: {projectId: this.projectId}}">
          Models {{ modelsCount | wrapInParentheses }}
        </nav-menu-item>
        <hr/>
        <nav-menu-item id="settings" :icon="icons.settings" :route="{name: 'admin'}">Settings</nav-menu-item>
      </el-row>
    </div>
    <div class="nav-menu__admin" v-else>
      <el-row>
        <nav-menu-item id="settings-close" :icon="icons.settings" selected :route="defaultSampleRoute">
          Settings<img class="nav-menu__admin-close" :src="icons.close"/>
        </nav-menu-item>
        <nav-menu-category-item :icon="icons.protocols">Protocols</nav-menu-category-item>
        <nav-menu-item isSubItem id="surfaceTreatmentProtocol" :icon="icons.surfaceTreatmentProtocol"
                       :route="{name: 'admin-protocols-surface_treatment'}">Surface Treatment
          {{adminCounters.surfaceTreatmentProtocolsCount | wrapInParentheses}}
        </nav-menu-item>
        <nav-menu-item isSubItem id="negativeStainProtocol" :icon="icons.negativeStainProtocol"
                       :route="{name: 'admin-protocols-negative_stain'}">Negative Stain
          {{adminCounters.negativeStainProtocolsCount | wrapInParentheses}}
        </nav-menu-item>
        <nav-menu-item isSubItem id="vitrificationProtocol" :icon="icons.vitrificationProtocol"
                       :route="{name: 'admin-protocols-vitrification'}">Vitrification
          {{adminCounters.vitrificationProtocolsCount | wrapInParentheses}}
        </nav-menu-item>

        <nav-menu-category-item :icon="icons.equipment">Equipment</nav-menu-category-item>
        <nav-menu-item isSubItem id="surfaceTreatmentMachine" :icon="icons.surfaceTreatmentMachine"
                       :route="{name: 'admin-equipment-surface_treatment_machines'}">Surface Treatment Machine
          {{adminCounters.surfaceTreatmentMachinesCount | wrapInParentheses}}
        </nav-menu-item>
        <nav-menu-item isSubItem id="vitrificationMachine" :icon="icons.vitrificationMachine"
                       :route="{name: 'admin-equipment-vitrification_machines'}">Vitrification Machine
          {{adminCounters.vitrificationMachinesCount | wrapInParentheses}}
        </nav-menu-item>
        <nav-menu-item isSubItem id="cryoStorageDevice" :icon="icons.cryoStorageDevice"
                       :route="{name: 'admin-equipment-cryo_storage_device'}">Cryo Storage Device
          {{adminCounters.cryoStorageDevicesCount | wrapInParentheses}}
        </nav-menu-item>
        <nav-menu-item isSubItem id="microscope" :icon="icons.microscope"
                       :route="{name: 'admin-equipment-microscopes'}">Microscope {{adminCounters.microscopesCount |
          wrapInParentheses}}
        </nav-menu-item>
        <nav-menu-item isSubItem id="electronDetector" :icon="icons.electronDetector"
                       :route="{name: 'admin-equipment-electron_detectors'}">Electron Detector
          {{adminCounters.electronDetectorsCount | wrapInParentheses}}
        </nav-menu-item>
        <nav-menu-item isSubItem id="sampleHolder" :icon="icons.sampleHolder"
                       :route="{name: 'admin-equipment-sample_holders'}">Sample Holder
          {{adminCounters.sampleHoldersCount | wrapInParentheses}}
        </nav-menu-item>

        <nav-menu-category-item :icon="icons.consumable">Consumables</nav-menu-category-item>
        <nav-menu-item isSubItem id="gridType" :icon="icons.gridType" :route="{name: 'admin-consumables-grid_type'}">
          Grid Type {{adminCounters.gridTypesCount | wrapInParentheses}}
        </nav-menu-item>
        <nav-menu-item isSubItem id="blottingPaper" :icon="icons.blottingPaper"
                       :route="{name: 'admin-consumables-blot_paper_types'}">Blotting Paper
          {{adminCounters.blottingPapersCount | wrapInParentheses}}
        </nav-menu-item>

        <nav-menu-category-item :icon="icons.software">Software</nav-menu-category-item>
        <nav-menu-item isSubItem id="image_processing" :icon="icons.imageProcessingSoftware"
                       :route="{name: 'admin-software-image_processing'}">Image Processing
          {{adminCounters.imageProcessingSoftwareCount | wrapInParentheses}}
        </nav-menu-item>

        <nav-menu-category-item :icon="icons.admin">Admin</nav-menu-category-item>
        <nav-menu-item isSubItem id="project" :icon="icons.admin" :route="{name: 'admin-admin-project'}">Projects
          {{adminCounters.projectsCount | wrapInParentheses}}
        </nav-menu-item>

        <nav-menu-item isSubItem id="admin-settings" :icon="icons.settings"
                       :route="{name: 'admin-admin-settings-view'}">Settings
        </nav-menu-item>
      </el-row>
    </div>
  </div>
</template>

<script>
  import ProjectCommons from '@/components/mixins/ProjectCommons'
  import IconCommons from '@/components/mixins/IconCommons'
  import ProjectSelector from '@/components/Selectors/ProjectSelector'
  import NavMenuItem from '@/components/App/NavMenuItem'
  import NavMenuCategoryItem from '@/components/App/NavMenuCategoryItem'
  import SampleService from '@/services/SampleService'
  import ProteinService from '@/services/ProteinService'
  import LigandService from '@/services/LigandService'
  import GridService from '@/services/GridService'
  import MicroscopySessionService from '@/services/MicroscopySessionService'
  import ProcessingSessionService from '@/services/ProcessingSessionService'
  import MapService from '@/services/MapService'
  import ModelService from '@/services/ModelService'
  import CountersService from '@/services/CountersService'

  const countersService = new CountersService()

  export default {
    name: 'nav-menu',

    mixins: [ProjectCommons, IconCommons],

    components: {
      NavMenuItem,
      NavMenuCategoryItem,
      ProjectSelector
    },

    data () {
      return {
        samplesCount: '',
        proteinsCount: '',
        ligandsCount: '',
        gridsCount: '',
        microscopySessionsCount: '',
        processingSessionsCount: '',
        mapsCount: '',
        modelsCount: '',
        adminCounters: {},
        countersMap: new Map([
          [new SampleService(), 'samplesCount'],
          [new ProteinService(), 'proteinsCount'],
          [new LigandService(), 'ligandsCount'],
          [new GridService(), 'gridsCount'],
          [new MicroscopySessionService(), 'microscopySessionsCount'],
          [new ProcessingSessionService(), 'processingSessionsCount'],
          [new MapService(), 'mapsCount'],
          [new ModelService(), 'modelsCount']
        ])
      }
    },

    computed: {
      isAdminMenu: function () {
        return this.$route.name && this.$route.name.startsWith('admin')
      }
    },

    watch: {
      'projectId' (newProjectId, oldProjectId) {
        /** check if project was changed from undefined to defined and if non route is currently active */
        if (!oldProjectId && !!newProjectId && (!this.$route.name || 'project' === this.$route.name)) {
          this.$router.replace({name: 'sample', params: {projectId: newProjectId}})
        }
        this.$events.emit('entitiesCountChanged')
      }
    },

    filters: {
      wrapInParentheses: (count) => count ? '(' + count + ')' : ''
    },

    methods: {
      refreshCounters: function () {
        this.refreshCountersForAdminPanel()
        if (this.projectId) {
          this.refreshCountersForProject()
        }
      },

      refreshCountersForAdminPanel: function () {
        countersService.countAdminEntities()
          .then(counters => this.adminCounters = counters.data)
          .catch(error => this.$log.error(error))
      },

      refreshCountersForProject () {
        this.countersMap.forEach((prop, service) => this.refreshSingleCounter(prop, service))
      },

      refreshSingleCounter: function (prop, service) {
        service.countEntitiesByProject(this.projectId)
          .then(count => this[prop] = count.data)
          .catch(error => this.$log.error(error))
      }
    },
    mounted () {
      this.$events.on('entitiesCountChanged', () => this.refreshCounters())
      this.$events.emit('entitiesCountChanged')
    },

    beforeDestroy () {
      this.$events.off('entitiesCountChanged')
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .nav-menu {
    background-color: $background-accent-color;

    .nav-menu__item {
      /deep/ .em-nav__link {
        text-align: left;
      }
    }

    .nav-menu__project-selector {
      width: 100%;
    }
    .nav-menu__project-label {
      margin-bottom: 0.8rem;
      color: #9b9b9b;
    }
    .nav-menu__project-box {
      padding-left: 1.8rem;
      padding-right: 1.8rem;
      padding-bottom: 2rem;
      padding-top: 2.5rem;
    }
    .nav-menu__admin-close {
      height: 2rem;
      width: 2rem;
      position: absolute;
      right: 1rem;
      top: 1rem;
    }
    hr {
      margin-left: 2rem;
      margin-right: 2rem;
    }
  }

</style>
