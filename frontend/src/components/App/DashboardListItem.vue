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
  <el-row class="list-view__list-row" :id="entityType + '-' + entity.id">
    <el-col class="list-view__list-row__left-pane" :sm="6">
      <slot name="left-pane"></slot>
    </el-col>

    <el-col class="list-view__list-row__default-pane" :sm="15">
      <slot>&nbsp;</slot>
    </el-col>

    <el-col class="list-view__list-row__right-pane" :sm="3">
      <slot name="right-pane">
        <el-row class="list-view__date">{{ entity.createdDate | moment('YYYY.MM.DD') }}</el-row>
        <el-row class="list-view__time">{{ entity.createdDate | moment('HH:mm:ss') }}</el-row>
        <el-row class="list-view__id">
          <user-full-name :userId='entity.createdBy'/>
        </el-row>
        <el-row v-if="_.gt(commentsCount, 0)" class="list-view__comment-indicator">
          <dashboard-comment-indicator
            :commentsCount="commentsCount">
          </dashboard-comment-indicator>
        </el-row>
      </slot>
    </el-col>
  </el-row>
</template>

<script>
  import UserFullName from '@/components/App/UserFullName'
  import CommentService from '@/services/CommentService'
  import DashboardCommentIndicator from '@/components/App/Comment/DashboardCommentIndicator'

  export default {
    name: 'DashboardListItem',
    components: {DashboardCommentIndicator, UserFullName},

    props: {
      entity: {type: Object, required: true},
      entityType: {type: String, required: true}
    },

    asyncComputed: {
      commentsCount: {
        get () {
          if (_.isEmpty(this.entityType) || !_.get(this, 'entity.id')) {
            return 0
          }

          return this._commentService.countComments(this.entityType, this.entity.id)
            .then(result => {
              return result.data
            }).catch(error => {
              this.$log.error(error)
              return 0
            })
        },

        default: 0
      }
    },

    beforeCreate () {
      this._commentService = new CommentService()
    }

  }
</script>

<style scoped lang="scss">

  .list-view__list-row {
    &__right-pane {
      height: 100%;
    }
  }

  .list-view__id {
    padding-bottom: 1rem;
  }

  .list-view__comment-indicator {
    padding-bottom: 1rem;
  }
</style>
