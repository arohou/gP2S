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
  <div>
    <!-- Related entities report - for printing -->
    <div v-if="showReport && entity" class="view__metadata__additional-content__print">
      <div v-if="maps">
        <el-row class="list-view__separator">
          <el-row class="entity-header">{{ this.createEntityLabel("Maps", maps) }}</el-row>
        </el-row>

        <div class="entity-body">
          <map-print-item v-for="(map, index) in maps"
                          :key="index"
                          :map="map"
                          :projectId="projectId">
          </map-print-item>
        </div>
        <div class="clear-both"/>
      </div>

      <div v-if="processingSessions">
        <el-row class="list-view__separator">
          <el-row class="entity-header">{{ this.createEntityLabel("Processing sessions", processingSessions) }}</el-row>
        </el-row>
        <div class="entity-body">
          <processing-session-print-item v-for="(processingSession, index) in processingSessions"
                                         :key="index"
                                         :processingSession="processingSession"
                                         :projectId="projectId" v-if="processingSession">
          </processing-session-print-item>
        </div>
        <div class="clear-both"/>
      </div>

      <div v-if="microscopySessions">
        <el-row class="list-view__separator">
          <el-row class="entity-header">{{ this.createEntityLabel("Microscopy sessions", microscopySessions) }}</el-row>
        </el-row>
        <microscopy-session-print-item v-for="(microscopySession, index) in microscopySessions"
                                       :key="index"
                                       :microscopySession="microscopySession"
                                       :projectId="projectId">
        </microscopy-session-print-item>
      </div>

      <div v-if="grids">
        <el-row class="list-view__separator">
          <el-row class="entity-header">{{ this.createEntityLabel("Grids", grids) }}</el-row>
        </el-row>
        <grid-print-item v-for="(grid, index) in grids"
                         :key="index"
                         :gridData="grid"
                         :projectId="projectId">
        </grid-print-item>
      </div>

      <div v-if="samples">
        <el-row class="list-view__separator">
          <el-row class="entity-header">{{ this.createEntityLabel("Samples", samples) }}</el-row>
        </el-row>
        <sample-print-item v-for="(sample, index) in samples"
                           :key="index"
                           :sampleData="sample"
                           :projectId="projectId">
        </sample-print-item>
      </div>

      <div v-if="!_.isEmpty(proteins)">
        <el-row class="list-view__separator">
          <el-row class="entity-header">{{ this.createEntityLabel("Proteins", _.uniqBy(proteins, 'aliquot.id'))
            }}</el-row>
        </el-row>
        <protein-print-item v-for="(protein, index) in _.uniqBy(proteins, 'aliquot.id')"
                            :key="index"
                            :proteinData="protein"
                            :projectId="projectId">
        </protein-print-item>
      </div>

      <div v-if="!_.isEmpty(ligands)">
        <el-row class="list-view__separator">
          <el-row class="entity-header">{{ this.createEntityLabel("Ligands", _.uniqBy(ligands, 'aliquot.id'))
            }}</el-row>
        </el-row>
        <ligand-print-item v-for="(ligand, index) in _.uniqBy(ligands, 'aliquot.id')"
                           :key="index"
                           :ligandData="ligand"
                           :projectId="projectId">
        </ligand-print-item>
      </div>
    </div>

    <div v-if="entity" class="dashboard accordion-form">
      <el-collapse v-if="entity" v-model="activeNames">

        <el-collapse-item :title="createEntityLabel('Proteins', proteins)"
                          name="proteins"
                          class="base-form__metadata"
                          id="proteins-section"
                          v-if="checkIsSampleView() && !_.isEmpty(proteins)">
          <div class="content-wrapper">
            <el-col class="view__components__protein view__metadata__additional-content__col" :xs="15">
              <el-row v-for="(protein, index) in proteins" :key="index">
                <protein-item-with-registration-system
                  concentrationLabel="Final concentration"
                  concentrationUnit="μM"
                  :entity="replaceConcentrationWithFinalConcentration(protein)"
                  :index="index"
                  v-if="proteinRegistrationSystemExists"></protein-item-with-registration-system>
                <protein-item
                  concentrationLabel="Final concentration"
                  concentrationUnit="μM"
                  :entity="replaceConcentrationWithFinalConcentration(protein)"
                  :index="index" v-else>
                </protein-item>
              </el-row>
            </el-col>
          </div>
          <div class="clear-both"/>
        </el-collapse-item>

        <el-collapse-item :title="createEntityLabel('Ligands', ligands)"
                          name="ligands"
                          class="base-form__metadata"
                          id="ligands-section"
                          v-if="checkIsSampleView() && !_.isEmpty(ligands)">
          <div class="content-wrapper">
            <el-col class="view__components__ligand view__metadata__additional-content__col" :xs="15">
              <el-row v-for="(ligand, index) in ligands" :key="index">
                <ligand-item
                  :entity="replaceConcentrationWithFinalConcentration(ligand)"
                  :index="index"
                  concentrationLabel="Final concentration">
                </ligand-item>
              </el-row>
            </el-col>
          </div>
          <div class="clear-both"/>
        </el-collapse-item>

        <el-collapse-item :title="createEntityLabel('Samples', samples)"
                          name="samples"
                          class="base-form__metadata"
                          id="samples-section"
                          v-if="!_.isEmpty(samples)">
          <div class="content-wrapper">
            <el-col class="view__components__sample view__metadata__additional-content__col" :xs="15">
              <el-row v-for="(sample, index) in samples" :key="index">
                <sample-item :entity="sample" :projectId="projectId">
                </sample-item>
              </el-row>
            </el-col>
          </div>
          <div class="clear-both"/>
        </el-collapse-item>

        <el-collapse-item :title="createEntityLabel('Grids', grids)"
                          name="grids"
                          class="base-form__metadata"
                          id="grids-section"
                          v-if="!_.isEmpty(grids)">
          <div class="content-wrapper">
            <el-col class="view__components__grid view__metadata__additional-content__col" :xs="15">
              <el-row v-for="(grid, index) in grids" :key="index">
                <grid-item :entity="grid" :projectId="projectId">
                </grid-item>
              </el-row>
            </el-col>
          </div>
          <div class="clear-both"/>
        </el-collapse-item>

        <el-collapse-item :title="createEntityLabel('Microscopy sessions', microscopySessions)"
                          name="microscopy-sessions"
                          class="base-form__metadata"
                          id="microscopy-sessions-section"
                          v-if="!_.isEmpty(microscopySessions)">
          <div class="content-wrapper">
            <el-col class="view__components__microscopy-session view__metadata__additional-content__col" :xs="15">
              <microscopy-session-item v-for="(microscopySession, index) in microscopySessions"
                                       :key="index"
                                       :entity="microscopySession"
                                       :index="index"
                                       :projectId="projectId">
              </microscopy-session-item>
            </el-col>
          </div>
          <div class="clear-both"/>
        </el-collapse-item>

        <el-collapse-item :title="createEntityLabel('Processing sessions', processingSessions)"
                          name="processing-sessions"
                          class="base-form__metadata"
                          id="processing-sessions-section"
                          v-if="!_.isEmpty(processingSessions)">
          <div class="content-wrapper">
            <el-col class="view__components__processing-session view__metadata__additional-content__col" :xs="15">
              <processing-session-item v-for="(processingSession, index) in processingSessions"
                                       :key="index"
                                       :entity="processingSession"
                                       :index="index"
                                       :projectId="projectId">
              </processing-session-item>
            </el-col>
          </div>
          <div class="clear-both"/>
        </el-collapse-item>

        <el-collapse-item :title="createEntityLabel('Maps', maps)"
                          name="maps"
                          class="base-form__metadata"
                          id="maps-section"
                          v-if="!_.isEmpty(maps)">
          <div class="content-wrapper">
            <el-col class="view__components__map view__metadata__additional-content__col" :xs="15">
              <el-row v-for="(map, index) in maps" :key="index">
                <map-item :entity="map" :projectId="projectId">
                </map-item>
              </el-row>
            </el-col>
          </div>
          <div class="clear-both"/>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>
  import ProteinItem from '@/components/App/Protein/ListItem'
  import ProteinItemWithRegistrationSystem from '@/components/App/Protein/registration_system_ext/ListItem'
  import LigandItem from '@/components/App/Ligand/ListItem'
  import SampleItem from '@/components/App/Sample/ListItem'
  import GridItem from '@/components/App/Grid/ListItem'
  import MicroscopySessionItem from '@/components/App/MicroscopySession/ListItem'
  import ProcessingSessionItem from '@/components/App/ProcessingSession/ListItem'
  import MapItem from '@/components/App/Map/ListItem'
  import { proteinRegistrationSystemExists } from '@/utils/ExternalSystemUtils'

  export default {
    name: 'entity-report-view',

    data () {
      return {
        maps: null,
        processingSessions: null,
        microscopySessions: null,
        grids: null,
        samples: null,
        proteins: null,
        ligands: null,
        activeNames: ['proteins', 'ligands', 'samples', 'grids', 'microscopy-sessions', 'processing-sessions', 'maps']
      }
    },

    props: ['entity', 'projectId', 'relatedEntities'],

    components: {
      ProteinItem, ProteinItemWithRegistrationSystem, LigandItem, SampleItem, GridItem, MicroscopySessionItem,
      ProcessingSessionItem, MapItem
    },

    computed: {
      showReport: function () { // Allow displaying only for current route.
        let routeName = this.$route.name

        return (routeName === 'model-view' && this.entity.maps
          || routeName === 'map-view' && this.entity.processingSession
          || routeName === 'processing_session-view' && this.entity.microscopySessions
          || routeName === 'microscopy_session-view' && this.entity.grid
          || routeName === 'grid-view' && this.entity.sample
          || this.checkIsSampleView())
      },

      proteinRegistrationSystemExists: function () {
        return proteinRegistrationSystemExists()
      }
    },

    methods: {
      createEntityLabel: function (entityName, entitiesList) {
        return entityName + ' (' + entitiesList.length + ')'
      },

      loadMaps: function () {
        if (this.entity.maps) {
          this.maps = _.uniqBy(this.entity.maps, 'id')
        }
      },

      loadProcessingSessions: function () {
        if (this.maps) {
          this.processingSessions = []
          this.maps.forEach((map) => {
            if (map.processingSession) {
              this.processingSessions.push(map.processingSession)
            }
          })
          this.processingSessions = _.uniqBy(this.processingSessions, 'id')
        } else if (this.entity.processingSession) {
          this.processingSessions = [this.entity.processingSession]
        }
      },

      loadMicroscopySessions: function () {
        if (this.processingSessions) {
          this.microscopySessions = []
          this.processingSessions.forEach((processingSession) => {
            if (processingSession.microscopySessions) {
              this.microscopySessions = this.microscopySessions.concat(this.microscopySessions,
                processingSession.microscopySessions)
            }
          })
          this.microscopySessions = _.uniqBy(this.microscopySessions, 'id')
        } else if (this.entity.microscopySessions) {
          this.microscopySessions = _.uniqBy(this.entity.microscopySessions, 'id')
        }
      },

      loadGrids: function () {
        if (this.microscopySessions) {
          this.grids = []
          this.microscopySessions.forEach((microscopySession) => {
            if (microscopySession.grid) {
              this.grids.push(microscopySession.grid)
            }
          })
          this.grids = _.uniqBy(this.grids, 'id')

          this.updateGridsAvailability(this.microscopySessions)
        } else if (this.entity.grid) {
          this.grids = [this.entity.grid]
          //TODO: find microscopy sessions for grid and call: this.updateGridsAvailability(grid.microscopySessions)
        }
      },

      loadSamples: function () {
        if (this.grids) {
          this.samples = []
          this.grids.forEach((grid) => {
            if (grid.sample) {
              this.samples.push(grid.sample)
            }
          })
          this.samples = _.uniqBy(this.samples, 'id')
        } else if (this.entity.sample) {
          this.samples = [this.entity.sample]
        }
      },

      loadProteins: function () {
        if (this.samples) {
          this.proteins = []
          this.samples.forEach((sample) => {
            if (sample.proteinComponent) {
              this.proteins = this.proteins.concat(this.proteins, sample.proteinComponent)
            }
          })
          this.proteins = _.uniqBy(this.proteins, 'id')
        } else if (this.entity.proteinComponent) {
          this.proteins = _.uniqBy(this.entity.proteinComponent, 'id')
        }
      },

      loadLigands: function () {
        if (this.samples) {
          this.ligands = []
          this.samples.forEach((sample) => {
            if (sample.ligandComponent) {
              this.ligands = this.ligands.concat(this.ligands, sample.ligandComponent)
            }
          })
          this.ligands = _.uniqBy(this.ligands, 'id')
        } else if (this.entity.ligandComponent) {
          this.ligands = _.uniqBy(this.entity.ligandComponent, 'id')
        }
      },

      updateGridsAvailability: function (microscopySessions) {
        if (_.isEmpty(microscopySessions) || _.isEmpty(this.grids)) {
          return
        }

        this.grids.forEach(grid => {
          if (grid.isAvailable === undefined) {
            let microscopySessionWithGridNotRetunred = microscopySessions.find(microscopySession =>
              microscopySession.grid.id === grid.id && !microscopySession.gridReturnedToStorage)

            grid.isAvailable = !microscopySessionWithGridNotRetunred
          }
        })
      },

      replaceConcentrationWithFinalConcentration (component) {
        return {
          ...component.aliquot,
          concentration: component.finalConcentration
        }
      },

      checkIsSampleView: function () {
        return this.$route.name === 'sample-view' && (this.entity.proteinComponent || this.entity.proteinComponent)
      }
    },

    watch: {
      'entity': function () {
        this.loadMaps()

        this.loadProcessingSessions()

        this.loadMicroscopySessions()

        this.loadGrids()

        this.loadSamples()

        this.loadProteins()

        this.loadLigands()
      }
    }
  }
</script>
<style scoped lang="scss">
  @import "~styles/globals/colors";

  .entity-header {
    margin-left: 2.05rem;
    font-weight: bold;
    font-size: 1.19rem;
    color: $text-color;
  }

  /deep/ .accordion-form {
    .el-collapse-item__content {
      line-height: unset;
    }

    .el-collapse-item__header {
      padding-top: 0.92rem;
      padding-bottom: 0.92rem;
      padding-left: 2.05rem;
      font-size: 1.19rem;
      margin-bottom: 1.23rem;

      &__arrow:before {
        content: "\E604";
        color: $text-header-color--disabled;
      }
    }

    .is-active .el-collapse-item__header {
      &__arrow:before {
        content: "\E605";
      }
    }

    .el-collapse-item__header:after {
      content: '';
      float: right;
      width: 0;
      margin-top: 0;
      margin-right: 0;
      border-top: 0;
    }

    .el-collapse-item__content {
      padding: 0;
      padding-top: 1.08rem;
    }
  }
</style>
