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
  <div class="form-loading-overlay">
    <div class="loader-n-text">
      <div class="loader"></div>
      <div>{{ overlayText || 'Loading data. Please wait...' }}</div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'form-loading-overlay',
    props: [
      'overlayText'
    ]
  }
</script>

<style lang="scss" scoped>
  @import '~styles/globals/colors';

  .form-loading-overlay {
    background-color: $form-overlay-color;
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 10;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .loader-n-text {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    color: $spinner-text-color;
  }

  .loader {
    border: 0.3rem solid $spinner-background-color; /* Light grey */
    border-top: 0.3rem solid $spinner-color; /* Blue */
    border-radius: 50%;
    height: 3em;
    width: 3em;
    animation: spin 1s linear infinite;
    margin-bottom: 2em;
  }

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
</style>
