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
  <div class="form-loading-overlay-container scrollable-container">
    <slot name="action-header"></slot>
    <form-loading-overlay v-if="formIsLoading"></form-loading-overlay>
    <div class="scrollable">
      <slot name="base-form"></slot>
    </div>
  </div>
</template>

<script>
  import FormLoadingOverlay from '@/components/App/FormLoadingOverlay'

  export default {
    name: 'form-component',
    props: ['name'],
    data () {
      return {
        formIsLoading: false
      }
    },
    components: {
      FormLoadingOverlay
    },
    events: {
      formLoadingStarted () {
        this.formIsLoading = true
      },
      formLoadingFinished () {
        this.formIsLoading = false
      }
    }
  }
</script>

<style scoped>
  .form-loading-overlay-container {
    position: relative;
  }
</style>
