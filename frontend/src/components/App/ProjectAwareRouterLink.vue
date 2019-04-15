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
  <div class="em-nav__tab">
    <router-link :to="route" v-if="projectId" active-class="em-nav__tab_active" class="em-nav__link em-nav__link-enabled">
        <slot></slot>
    </router-link>
    <a :disabled="true" v-else class="em-nav__link em-nav__link-disabled">
      <slot></slot>
    </a>
  </div>
</template>

<script>
  import ProjectCommons from '@/components/mixins/ProjectCommons'

  export default {
    name: 'project-aware-router-link',
    mixins: [ProjectCommons],
    props: ['route']
  }

</script>

<style scoped>
  a:focus, a:active, button,
  input[type="reset"]::-moz-focus-inner,
  input[type="button"]::-moz-focus-inner,
  input[type="submit"]::-moz-focus-inner,
  select::-moz-focus-inner,
  input[type="file"] > input[type="button"]::-moz-focus-inner {
      outline: none !important;
  }
  .em-nav__link-disabled{
    color: #7f7f7f;
    cursor: not-allowed;
  }
  .em-nav__link-enabled{
    color: #ffffff;
  }
  .em-nav__link{
    text-decoration: none;
    width: 10rem;
    display: flex;
    align-items: center;
    justify-content: space-around;
    height: 100%;
  }
  .em-nav__tab_active {
    border-bottom: solid 5px #649de5;
    font-weight: bold;
    color: #649de5;
  }
  .em-nav__tab {
    background-color: #242642;
    font-family: Helvetica;
    font-size: 16px;
    letter-spacing: -0.1px;
    text-align: center;
    display: inline-block;
    height: 100%;
  }

  .nav .el-button + .el-button {
    margin-left: 0;
  }
</style>
