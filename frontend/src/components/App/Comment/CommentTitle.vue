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
  <el-row class="comment__title-bar" type="flex">
    <div class="comment__title-bar__close">
      <el-button type="text" @click="$events.emit('closeCommentDrawer')">
        <img :src="icon"/>
      </el-button>
    </div>
  </el-row>
</template>

<script>
  import CloseIcon from '@/assets/images/close.svg'

  export default {
    name: 'CommentTitle',
    props: {},
    data () {
      return {
        icon: CloseIcon
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .comment__title-bar {
    padding: 0rem 1rem 0rem 1rem;
    justify-content: flex-end;
    display: flex;
    background-color: $background-accent-color-comment-button-box;
    box-shadow: 0 0 16px 0 rgba(0, 0, 0, 0.18);
  }

</style>
