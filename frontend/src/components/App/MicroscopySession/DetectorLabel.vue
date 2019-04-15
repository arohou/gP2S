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
  <div class="detector-label">
    <div class="label">Selected Detector</div>
    <div v-if="detectorLabel" class="value">{{ detectorLabel }}</div>
    <div v-else class="error">Please select detector in Basic information</div>
  </div>
</template>

<script>
  export default {
    name: 'detector-label',

    props: {
      detectorLabel: String
    }
  }
</script>

<style scoped lang="scss">

  @import "~styles/globals/colors";

  .detector-label {

    margin-bottom: 2em;

    .label {
      padding: 0 0 1.231rem;
      font-size: 1.077rem;
      float: none;
      text-align: left;
      line-height: 1;
      color: $text-header-color--disabled;
    }

    .value {
      @extend .label;
      color: $text-label-edit-color;
    }

    .error {
      @extend .label;
      color: $form-error-color;
    }

  }

</style>
