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
  <div class="metadata__user__full-name">{{userFullName}}</div>
</template>

<script>
  import UserInfoUtil from '@/utils/UserInfoUtil'

  export default {
    name: 'user-full-name',

    props: {
      userId: {
        type: String
      }
    },

    data () {
      return {
        userFullName: ''
      }
    },

    methods: {
      loadUserFullName (userId) {
        UserInfoUtil.getUserFullName(userId)
          .then(res => {
            this.userFullName = res
          })
          .catch(error => {
            this.userFullName = this.userId
            this.$log.error(error)
          })
      }
    },

    watch: {
      'userId' (userId) {
        this.loadUserFullName(userId)
      }
    },

    mounted () {
      this.loadUserFullName(this.userId)
    }

  }
</script>
