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
  <el-dropdown trigger="click" @command="handleCommand">
    <div class="em-app__user-info-box">
      <h2 class="em-app__user">{{ user.displayName }}</h2>
      <img :src='user.photoUrl'/>
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="logout">Logout</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>

</template>

<script>
  import SettingsIcon from '@/assets/images/user.svg'
  import auth from '@/utils/auth'
  import UserInfoUtil from '@/utils/UserInfoUtil'

  export default {
    name: 'app-header',

    data () {
      return {
        user: {
          displayName: '',
          photoUrl: SettingsIcon
        }
      }
    },

    methods: {
      handleCommand(command) {
        if( command === 'logout' ){
          auth.logout()
          this.$router.go(0)
        }
      }
    },

    async mounted () {
      this.user.displayName = await UserInfoUtil.getUsername()
      if(!this.user.photoUrl){
        this.user.photoUrl = SettingsIcon
      }
    }

  }
</script>

<style lang="scss" scoped>

  .em-app__user-info-box {
    align-items: center;
    display: flex;
    cursor: pointer;

    :not(:last-child) {
      padding-right: 1em;
    }
  }
  .em-app__user-info-box img {
    width: 2.462rem;
    height: 2.462rem;
    border: solid 1px transparent;
    border-radius: 100%;
  }
  .em-app__user {
    font-size: 1rem;
    letter-spacing: -0px;
    color: white;
    font-weight: normal;
    }

</style>
