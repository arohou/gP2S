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
  <div class="comment__info-bar">
    <el-row type="flex">
      <div class="comment__info-bar__user">
        <el-row class="user_name">
          <user-full-name :userId='comment.createdBy'/>
        </el-row>
        <el-row v-if="wasEdited(comment)" class="edited_indicator"
                :title="comment.modifiedDate | moment('YYYY.MM.DD HH:mm:ss')">(edited)
        </el-row>
      </div>
      <div class="comment__info-bar__date-time">
        <el-row class="list-view__date">{{ comment.createdDate | moment('YYYY.MM.DD') }}</el-row>
        <el-row class="list-view__time">{{ comment.createdDate | moment('HH:mm:ss') }}</el-row>
      </div>
    </el-row>
  </div>
</template>

<script>
  import comments from './Comments'
  import UserFullName from '@/components/App/UserFullName'

  export default {
    name: 'CommentText',
    props: {
      comment: {
        type: Object,
        required: true
      }
    },
    components: {UserFullName},
    methods: {
      wasEdited (comment) {
        return comments.wasEdited(comment)
      }
    }
  }
</script>

<style lang="scss" scoped>

  @import '~styles/globals/overrides.element';

  .user_name {
    @extend .list-view__value;
    margin-bottom: 0rem;
  }

  .edited_indicator {
    @extend .list-view__time;
    text-align: left;
  }

  .comment__info-bar__user {
    float: left;
    flex: 1;
  }

  .comment__info-bar__date-time {
    float: right
  }
</style>
