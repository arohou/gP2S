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
  <div v-if="state === 'correct'"
       class="view__metadata"
       :class="{ 'project-management-system-box': listType === 'box', 'project-management-system-flat': listType === 'flat' }">
    <el-row>
      <label class="view__metadata__label">Title</label>
      <div class="view__metadata__value-title" id="management-system-info__title">
        {{ projectInfo.title | formatValue }}
      </div>
    </el-row>

    <h2 class="view__metadata__section-header view__metadata__section-line">Structural Biology</h2>
    <div>
      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">SB Lead</label>
          <div class="view__metadata__value" id="management-system-info__sb-lead">
            {{ projectInfo.structuralBiologyLeader | formatValue }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">SB Associates</label>
          <div class="view__metadata__value" id="management-system-info__sb-associates">
            {{ projectInfo.structuralBiologyAssociates | formatArray }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">SB Kick-Off Date</label>
          <div class="view__metadata__value" id="management-system-info__sb-kick-off-date">
            {{ projectInfo.structuralBiologyKickOffDate | formatArray(formatDate) }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">SB Attrition Date</label>
          <div class="view__metadata__value" id="management-system-info__sb-attrition-date">
            {{ projectInfo.structuralBiologyAttritionDate | formatArray(formatDate) }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="6">
          <label class="view__metadata__label">Protein Production Effort?</label>
          <div class="view__metadata__value" id="management-system-info__protein-production-effort">
            {{ projectInfo.structuralBiologyProteinProductionEffort | formatBoolean }}
          </div>
        </el-col>
        <el-col :sm="6">
          <label class="view__metadata__label">Crystallography Effort?</label>
          <div class="view__metadata__value" id="management-system-info__crystallography-effort">
            {{ projectInfo.structuralBiologyCrystallographyEffort | formatBoolean }}
          </div>
        </el-col>
        <el-col :sm="6">
          <label class="view__metadata__label">Cryo-EM Effort?</label>
          <div class="view__metadata__value" id="management-system-info__cryo-em-effort">
            {{ projectInfo.structuralBiologyCryoEMEffort | formatBoolean }}
          </div>
        </el-col>
        <el-col :sm="6">
          <label class="view__metadata__label">NMR Effort?</label>
          <div class="view__metadata__value" id="management-system-info__nmr-effort">
            {{ projectInfo.structuralBiologyNMREffort | formatBoolean }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col>
          <label class="view__metadata__label">SB Objective</label>
          <div class="view__metadata__value" id="management-system-info__sb-objective">
            {{ projectInfo.structuralBiologyObjective | formatValue }}
          </div>
        </el-col>
      </el-row>
    </div>

    <h2 class="view__metadata__section-header view__metadata__section-line">Research Portfolio Information</h2>
    <div>
      <el-row>
        <el-col>
          <label class="view__metadata__label">Portfolio Project</label>
          <div class="view__metadata__value" id="management-system-info__portfolio-project">
            {{ projectInfo.researchPortfolioProject | formatValue }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">Portfolio Project Created</label>
          <div class="view__metadata__value" id="management-system-info__portfolio-project-created">
            {{ projectInfo.researchPortfolioProjectCreated | formatDate }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">Portfolio Project Modified</label>
          <div class="view__metadata__value" id="management-system-info__portfolio-project-modified">
            {{ projectInfo.researchPortfolioProjectModified | formatDate }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">Project TA</label>
          <div class="view__metadata__value" id="management-system-info__project-ta">
            {{ projectInfo.researchPortfolioProjectTA | formatValue }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">Project Lead</label>
          <div class="view__metadata__value" id="management-system-info__portfolio-project-lead">
            {{ projectInfo.researchPortfolioProjectLeader | formatValue }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">Project Status</label>
          <div class="view__metadata__value" id="management-system-info__project-status">
            {{ projectInfo.researchPortfolioProjectStatus | formatValue }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">Project Stage</label>
          <div class="view__metadata__value" id="management-system-info__project-stage">
            {{ projectInfo.researchPortfolioProjectStage | formatValue }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col>
          <label class="view__metadata__label">Molecule</label>
          <div class="view__metadata__value" id="management-system-info__molecule">
            {{ projectInfo.researchPortfolioMolecule | formatValue }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">Molecule Type</label>
          <div class="view__metadata__value" id="management-system-info__molecule-type">
            {{ projectInfo.researchPortfolioMoleculeType | formatValue }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">Molecule Type Category</label>
          <div class="view__metadata__value" id="management-system-info__molecule-type-category">
            {{ projectInfo.researchPortfolioMoleculeTypeCategory | formatValue }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col>
          <label class="view__metadata__label">Targets</label>
          <div class="view__metadata__value" id="management-system-info__targets">
            {{ projectInfo.researchPortfolioTargets | formatArray }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col>
          <label class="view__metadata__label">Objective</label>
          <div class="view__metadata__value" id="management-system-info__objective">
            {{ projectInfo.researchPortfolioObjective | formatValue }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col>
          <label class="view__metadata__label">Indication</label>
          <div class="view__metadata__value" id="management-system-info__indication">
            {{ projectInfo.researchPortfolioIndication | formatValue }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">Vice President</label>
          <div class="view__metadata__value" id="management-system-info__vice-president">
            {{ projectInfo.researchPortfolioVicePresident | formatValue }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">Legal Representative</label>
          <div class="view__metadata__value" id="management-system-info__legal-representative">
            {{ projectInfo.researchPortfolioLegalRepresentative | formatValue }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="6">
          <label class="view__metadata__label">Biology Lead</label>
          <div class="view__metadata__value" id="management-system-info__biology-lead">
            {{ projectInfo.researchPortfolioBiologyLeader | formatValue }}
          </div>
        </el-col>
        <el-col :sm="6">
          <label class="view__metadata__label">DMPK Lead</label>
          <div class="view__metadata__value" id="management-system-info__dmpk-lead">
            {{ projectInfo.researchPortfolioDMPKLeader | formatValue }}
          </div>
        </el-col>
        <el-col :sm="6">
          <label class="view__metadata__label">Toxicology Lead</label>
          <div class="view__metadata__value" id="management-system-info__toxicology-lead">
            {{ projectInfo.researchPortfolioToxicologyLeader | formatValue }}
          </div>
        </el-col>
        <el-col :sm="6">
          <label class="view__metadata__label">Chemistry Lead</label>
          <div class="view__metadata__value" id="management-system-info__chemistry-lead">
            {{ projectInfo.researchPortfolioChemistryLeader | formatValue }}
          </div>
        </el-col>
      </el-row>
    </div>

    <h2 class="view__metadata__section-header view__metadata__section-line">Research Portfolio Timeline</h2>
    <div>
      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">ESR Go Date</label>
          <div class="view__metadata__value" id="management-system-info__esr-go-date">
            {{ projectInfo.researchPortfolioESRGoDate | formatDate }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">ESR Goal Achieving Date</label>
          <div class="view__metadata__value" id="management-system-info__esr-goal-achieving-date">
            {{ projectInfo.researchPortfolioESRGoalAchievingDate | formatDate }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">LSR Go Date</label>
          <div class="view__metadata__value" id="management-system-info__lsr-go-date">
            {{ projectInfo.researchPortfolioLSRGoDate | formatDate }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">LSR Goal Achieving Date</label>
          <div class="view__metadata__value" id="management-system-info__lsr-goal-achieving-date">
            {{ projectInfo.researchPortfolioLSRGoalAchievingDate | formatDate }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">ED Ready Date</label>
          <div class="view__metadata__value" id="management-system-info__ed-ready-date">
            {{ projectInfo.researchPortfolioEDReadyDate | formatDate }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">ED Go Date</label>
          <div class="view__metadata__value" id="management-system-info__ed-go-date">
            {{ projectInfo.researchPortfolioEDGoDate | formatDate }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :sm="12">
          <label class="view__metadata__label">Attrited Date</label>
          <div class="view__metadata__value" id="management-system-info__attrited-date">
            {{ projectInfo.researchPortfolioAttritionDate | formatDate }}
          </div>
        </el-col>
        <el-col :sm="12">
          <label class="view__metadata__label">On Hold Date</label>
          <div class="view__metadata__value" id="management-system-info__on-hol-date">
            {{ projectInfo.researchPortfolioOnHoldDate | formatDate }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col>
          <label class="view__metadata__label">Attrition Comment</label>
          <div class="view__metadata__value" id="management-system-info__attrition-comment">
            {{ projectInfo.researchPortfolioAttritionComment | formatValue }}
          </div>
        </el-col>
      </el-row>
    </div>
  </div>

  <div v-else class="view__metadata before-data-loaded">
    <el-row>
      <div class="view__metadata__value state-indicator-processing" v-if="state === 'processing'">
        <state-indicator :state="state"></state-indicator>&nbsp;
      </div>
      <span v-if="state === null">&nbsp;</span>
    </el-row>
  </div>
</template>

<script>
  import StateIndicator from '@/components/App/StateIndicatorComponent'
  import ProjectService from '@/services/ProjectService'
  import FilterCommons from '@/components/mixins/FilterCommons'

  export default {
    name: 'project-management-system-info',

    components: {
      StateIndicator
    },

    mixins: [
      FilterCommons
    ],

    props: {
      listType: {
        type: String,
        default: 'box'
      },
      projectManagementSystemSlug: {
        type: String
      }
    },

    data () {
      return {
        state: 'processing',
        projectInfo: {
          title: null,
          projectManagementSystemSlug: null,

          //region STRUCTURAL BIOLOGY
          structuralBiologyLeader: null,
          structuralBiologyAssociates: null,
          structuralBiologyKickOffDate: null,
          structuralBiologyAttritionDate: null,
          structuralBiologyProteinProductionEffort: null,
          structuralBiologyCrystallographyEffort: null,
          structuralBiologyCryoEMEffort: null,
          structuralBiologyNMREffort: null,
          structuralBiologyObjective: null,
          //endregion

          //region RESEARCH PORTFOLIO INFORMATION
          researchPortfolioProject: null,
          researchPortfolioProjectCreated: null,
          researchPortfolioProjectModified: null,
          researchPortfolioProjectTA: null,
          researchPortfolioProjectLeader: null,
          researchPortfolioProjectStatus: null,
          researchPortfolioProjectStage: null,
          researchPortfolioMolecule: null,
          researchPortfolioMoleculeType: null,
          researchPortfolioMoleculeTypeCategory: null,
          researchPortfolioTargets: null,
          researchPortfolioObjective: null,
          researchPortfolioIndication: null,
          researchPortfolioVicePresident: null,
          researchPortfolioLegalRepresentative: null,
          researchPortfolioBiologyLeader: null,
          researchPortfolioDMPKLeader: null,
          researchPortfolioToxicologyLeader: null,
          researchPortfolioChemistryLeader: null,
          //endregion

          //region RESEARCH PORTFOLIO TIMELINE
          researchPortfolioESRGoDate: null,
          researchPortfolioESRGoalAchievingDate: null,
          researchPortfolioLSRGoDate: null,
          researchPortfolioLSRGoalAchievingDate: null,
          researchPortfolioEDReadyDate: null,
          researchPortfolioEDGoDate: null,
          researchPortfolioAttritionDate: null,
          researchPortfolioOnHoldDate: null,
          researchPortfolioAttritionComment: null
          //endregion
        }
      }
    },

    watch: {
      async projectManagementSystemSlug () {
        await this.loadProjectManagementSystemData(this.projectManagementSystemSlug)
      }
    },

    methods: {
      handleProjectManagementSystemError (error) {
        this.state = 'error'
      },

      async handleProjectInfoDownloaded (projectInfo) {
        this.projectInfo = projectInfo
        this.state = 'correct'
      },

      async loadProjectManagementSystemData (projectManagementSystemSlug) {
        if (!projectManagementSystemSlug) {
          return
        }

        this.state = 'processing'

        try {
          const result = await this._projectService.findProjectManagementSystemEntity(projectManagementSystemSlug)
          this.handleProjectInfoDownloaded(result.data)
          this.$events.$emit('projectInfoUrl', result.data.url)
        } catch (error) {
          this.handleProjectManagementSystemError(error)
        }
      },

      formatDate (value) {
        if (!value) {
          return ''
        }

        return value.split(', ')
                    .map(val => this.$moment(val)
                                    .format('YYYY.MM.DD'))
                    .join(', ')
      }
    },

    created () {
      this._projectService = new ProjectService()
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";
  @import "~styles/globals/constants";
  @import "~styles/globals/overrides.element";

  .view__metadata {
    &__value-title {
      @extend .view__metadata__value;
      font-size: 200% !important;
    }
  }

  .project-management-system-box {
    width: 100%;
    background-color: $list-border-color;
    margin: 1.154rem 0;
    padding: 1.154rem;
  }

  .project-management-system-flat {
    width: $page-content;
  }

  .state-indicator {
    margin-left: auto;
    margin-right: auto;
    width: 1.846rem;
    height: 1.846rem;
    margin-top: -1.3rem;
  }

  .state-indicator-processing {
    margin-top: 0;
  }

  .view__metadata.before-data-loaded .el-row {
    margin-bottom: 0 !important;
  }

</style>

