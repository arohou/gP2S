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
  <div class="state-indicator">
    <img src='~@/assets/images/check-correct.svg' v-if="state == 'correct'" />
    <img src='~@/assets/images/check-invalid.svg' v-if="state == 'invalid'" />
    <img src='~@/assets/images/check-question-mark.svg' v-if="state == 'unknown'" />
    <div class="loadme-circular" v-if="state == 'processing'"></div>
  </div>
</template>

<script>
  export default {
    name: 'state-indicator',

    props: ['state'],
  }
</script>

<style lang="scss" scoped>
  @import '~loadme/dist/style/loadme.css';
  @import '~styles/globals/colors';

  .loadme-circular {
    position: relative;
    padding: 0;
    height: 1.5em;
    width: 1.5em;
    margin-top: 0.35em;
    margin-left: -0.65em;
    border-top: 0.2em solid $spinner-color;
    border-right: 0.2em solid $spinner-color;
    border-bottom: 0.2em solid $spinner-color;
    border-left: 0.2em solid $spinner-background-color;
  }

  .state-indicator img {
    width: 1.5em;
    margin-left: -0.25em;
    margin-top: 0.20em;
  }
</style>
