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
    <div v-if="errorStatusCode" class="error">
      <div class="app__header-background"></div>
      <el-row class="app__header">
        <app-header></app-header>
      </el-row>
      <div class="cat-my_error-box">
        <img :src="'https://http.cat/' + errorStatusCode"/>
      </div>
    </div>
    <div  class="app">
      <div class="app__header-background"></div>
      <el-row class="app__header">
        <app-header></app-header>
      </el-row>
      <el-row v-if="isLoginScreen" type="flex" justify="center" class="app__box">
        <el-col :span="6" class="app__login-column">
          <login />
        </el-col>
      </el-row>
      <el-row v-else type="flex" class="app__box">
        <el-col class="app__nav-menu-wrapper" :span="5" v-if="isMenuOpen">
          <nav-menu></nav-menu>
        </el-col>
        <el-col  :span="contentSpan" class="app__content">
          <transition name="transition-fade">
            <router-view :useSlug="useSlug"></router-view>
          </transition>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import NavMenu from '@/components/App/NavMenu'
  import AppHeader from '@/components/App/AppHeader'
  import CommonValidations from '@/components/mixins/CommonValidations'
  import Login from '@/components/App/Login'
  import auth from '@/utils/auth'

  export default {
    name: 'app',

    data () {
      return {
        useSlug: true,
        isMenuOpen: true,
        isMenuOpenedByHamburger: true,
        isCommentsSectionOpem : false
      }
    },

    mixins: [CommonValidations],

    components: {
      Login,
      AppHeader, NavMenu
    },

    methods: {
      isSlug (key) {
        return !this.isNumeric(key)
      },

      checkAndUpdateUseSlug (key) {
        if (key) {
          let res = this.isSlug(key)
          if (res !== this.useSlug) {
            this.useSlug = res
          }
        }
      },

      menuIndicatorListener () {
        this.isMenuOpen = !this.isMenuOpen
        this.isMenuOpenedByHamburger = this.isMenuOpen
        this.isCommentsSectionOpen = false
      },

      openCommentDrawerListener () {
        this.isCommentsSectionOpen = !this.isCommentsSectionOpen
        this.isMenuOpen = this.isCommentsSectionOpen ? false : this.isMenuOpenedByHamburger
      },

      closeCommentDrawerListener () {
        this.isMenuOpen = this.isMenuOpenedByHamburger
        this.isCommentsSectionOpen = false
      }
    },

    computed: {
      errorStatusCode: function () {
        return _.get(this.$router, 'error.response.status') || _.get(this.$route, 'params.errorCode')
      },
      contentSpan: function () {
        return this.isMenuOpen ? 19 : 24
      },
      isLoginScreen: function() {
        return process.env.SECURITY_ENABLED && !this.$isLoggedIn
      }
    },

    mounted () {
      /* Fix for loading data over url with object id. Without seting useSlug flag
       * element loaded by id will not be found on list and default selection will
       * be executed */
      this.checkAndUpdateUseSlug(this.$route.params.id)
      this.$events.on('menuIndicator', this.menuIndicatorListener)
      this.$events.on('openCommentDrawer', this.openCommentDrawerListener)
      this.$events.on('closeCommentDrawer', this.closeCommentDrawerListener)
    },

    beforeDestroy () {
      this.$events.off('menuIndicator', this.menuIndicatorListener)
      this.$events.off('openCommentDrawer', this.openCommentDrawerListener)
      this.$events.off('closeCommentDrawer', this.closeCommentDrawerListener)
    },

    watch: {
      '$route.params.id' (key) {
        this.checkAndUpdateUseSlug(key)
      }
    }
  }
</script>

<style lang="scss">
  @import '~element-ui/lib/theme-default/index.css';
  @import 'styles/globals/base';
  @import 'styles/globals/transitions.css';
  @import 'styles/globals/overrides.element';
</style>

<style lang="scss" scoped>
  @import 'styles/globals/constants';
  @import 'styles/globals/colors';

  .app__header {
    height: $header-height;
    min-width: $application-width;
    width: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
  }

  .app__header-background {
    height: $header-height;
    background-color: $header-background-color;
    box-shadow: 0 0.3077rem 0.3077rem 0 rgba(0, 0, 0, 0.5);
    min-width: $application-width;
    width: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
  }

  .app__content {
    padding-top: 1.5rem;
  }

  .app {
    min-height: 100vh;
    margin-left: auto;
    margin-right: auto;
    min-width: $application-width;
    width: 100%;

    &__nav-menu-wrapper {
      display: flex;
      align-items: stretch;
    }

    .app__box {
      display: flex;
      align-items: stretch;
      height: 100vh;
      padding-top: $header-height;
      .el-col-5 {
        width: 19.30555%;
      }
      .el-col-19 {
        width: 80.69444%;
      }
    }

    &__login-column {
      padding-top: 4em;
    }

    #save-state-indicator {
      margin-top: -28%;
    }
  }

  .cat-my_error-box {
    display: flex;
    align-items: center;
    justify-content: center;
    background: black;
    height: 100vh;
  }
</style>
