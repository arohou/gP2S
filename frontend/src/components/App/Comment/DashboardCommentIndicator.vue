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
  <div class="dashboard-comment-indicator">
    <el-button disabled type="text">
      <div class="dashboard-comment-indicator__box">
        <div class="dashboard-comment-indicator__icon">
          <img :src="icon"/>
        </div>
        <div class="dashboard-comment-indicator__title">
          ({{commentsCount}})
        </div>
      </div>
    </el-button>
  </div>
</template>

<script>
  import CommentIcon from '@/assets/images/comment-grey.svg'

  export default {
    name: 'DashboardCommentIndicator',
    props: {
      commentsCount: {type: Number, required: true}
    },
    data () {
      return {
        icon: CommentIcon
      }
    }
  }
</script>

<style scoped lang="scss">
  @import "~styles/globals/colors";

  .dashboard-comment-indicator {
    align-items: end;
    justify-content: flex-end;
    display: flex;

    /deep/ .el-button {
      padding-bottom: 0;
      padding-top: 0;

      border: none !important;
    }

    &__box {
      text-transform: none;
      font-size: 1rem;
      align-items: center;
      justify-content: flex-end;
      display: flex;
    }

    &__icon {
      margin-right: 0.5rem;
    }

    &__title {
      color: $text-header-color--disabled;
    }
  }
</style>
