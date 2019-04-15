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
    <el-row class="app-header">
      <el-col :span="2" class="em-app__menu-indicator__box">
        <div v-if="$isLoggedIn" class="em-app__menu-indicator" @click="$events.emit('menuIndicator')"></div>
      </el-col>
      <el-col :span="2" />
      <el-col :span="6" class="em-app__left-header">
        <router-link :to="defaultSampleRoute">
          <div class="em-app__left-header__logo">
            <img src="~@/assets/images/logo.svg"/>
            <h2 class="em-app__title-part1">g</h2>
            <h2 class="em-app__title-part2">P2S</h2>
            <h2 v-if="env === 'local'" class="em-app__title-part3">LOCAL</h2>
            <h2 v-else-if="env === 'development'" class="em-app__title-part3">DEV</h2>
            <h2 v-else-if="env === 'testing'" class="em-app__title-part3">TST</h2>
          </div>
        </router-link>
      </el-col>
      <el-col :span="10" class="em-app__center-header">
      </el-col>
      <el-col v-if="isUserInfo" class="em-app__right-header" :span="5">
        <user-info></user-info>
      </el-col>
    </el-row> <!-- /header -->
</template>

<script>
  import ProjectCommons from '@/components/mixins/ProjectCommons'
  import UserInfo from '@/components/App/UserInfo'

  export default {
    name: 'app-header',

    mixins: [ProjectCommons],

    components: {
      UserInfo
    },

    data() {
      return {
        env: process.env.NODE_ENV,
      };
    },

    computed: {
      isUserInfo: function() {
        return process.env.SECURITY_ENABLED && this.$isLoggedIn
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import '~styles/globals/constants';

  .app-header {
    height: $header-height;

    .em-app__menu-indicator__box {
      display: flex;
      align-items: center;
      height: $header-height;
      margin-left: 2rem;
    }

    .em-app__center-header {
      height: $header-height;
    }

    .em-app__left-header {
      height: $header-height;
      align-items: center;
      display: flex;
      a {
        text-decoration: none;
      }
      .em-app__left-header__logo {
        white-space: nowrap;
        display: flex;
        align-items: center;
      }
      .em-app__left-header__logo img{
        height: 2.462rem;
        width: 2.077rem;
      }
      .em-app__title-part1, .em-app__title-part2 {
        font-size: 1.846rem;
        font-weight: 300;
        letter-spacing: -.1px;
      }
      .em-app__title-part1 {
        color: #ffffff;
        padding-left: 0.6154rem;
      }
      .em-app__title-part2 {
        color: #649de5;
        padding-left: 0.3077rem;
      }
      .em-app__title-part3 {
        color: #fff;
        padding-left: 0.6154rem;
        font-size: .8rem;
        margin-top: 0.6rem;
        text-shadow: 2px 2px 2px rgba(0, 0, 0, 0.2),
        0px 0px 5px rgba(0, 0, 0, 0.3);
      }
    }

    .em-app__right-header {
      height: $header-height;
      align-items: center;
      justify-content: flex-end;
      display: flex;

    }
  }

  .em-app__menu-indicator {
    width: 1.462rem;
    height: 1.154rem;
    cursor: pointer;
    content: url('~@/assets/images/menu_indicator.svg');
  }
  .em-app__menu-indicator:hover{
    content: url('~@/assets/images/menu_indicator_active.svg');
  }
  /*workaround to have it working on firefox*/
  .em-app__menu-indicator::before {
    content: url('~@/assets/images/menu_indicator.svg');
  }
  /*workaround to have it working on firefox*/
  .em-app__menu-indicator:hover::before{
    content: url('~@/assets/images/menu_indicator_active.svg');
  }

</style>
