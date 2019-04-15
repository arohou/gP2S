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
    <div v-if="isConcentrated">
      <label v-bind:class="labelClass">Concentration factor</label>
      <div id="concentration-factor" v-bind:class="valueClass">
        {{ concentration.concentrationFactor | formatUnit('×') }}
      </div>
    </div>
    <div v-else-if="isDiluted">
      <label v-bind:class="labelClass">Dilution factor</label>
      <div id="dilution-factor" v-bind:class="valueClass">{{ '1:' + concentration.dilutionFactor }}</div>
    </div>
    <div v-else>
      <label v-bind:class="labelClass">Concentration factor</label>
      <div id="concentration-factor-fallback" v-bind:class="valueClass">{{ 1 | formatUnit('×') }}</div>
    </div>
  </div>
</template>

<script>
  import ConcentrationType from '@/components/App/ConcentrationType'
  import FilterCommons from '@/components/mixins/FilterCommons'

  export default {
    name: 'concentration-view',

    props: {
      concentration: Object,
      listItem: {
        type: Boolean,
        default: false
      }
    },

    mixins: [
      FilterCommons
    ],

    computed: {
      labelClass () {
        if (this.listItem) {
          return 'list-view__label'
        }
        return 'view__metadata__label'
      },
      valueClass () {
        if (this.listItem) {
          return 'list-view__value'
        }
        return 'view__metadata__value'
      },
      isConcentrated () {
        if (!_.get(this.concentration, 'isDilutedOrConcentrated')) { return false }
        return ConcentrationType.Concentration === this.concentration.concentrationType ||
          ConcentrationType.Concentration.name === this.concentration.concentrationType
      },
      isDiluted () {
        if (!_.get(this.concentration, 'isDilutedOrConcentrated')) { return false }
        return ConcentrationType.Dilution === this.concentration.concentrationType ||
          ConcentrationType.Dilution.name === this.concentration.concentrationType
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .dilution-factor {
    display: flex;

    .dilution-factor__const {
      width: 2.5rem;
      min-width: 2.5rem;
    }

    span {
      margin-left: 0.5rem;
      margin-right: 0.5rem;
    }
  }
</style>
