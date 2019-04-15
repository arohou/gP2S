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
  <div class="comment-indicator view-header__comments">
    <el-button id="comment-indicator__open-comment-drawer" @click="$events.emit('openCommentDrawer')" type="text">
      <div class="comment-indicator__box">
        <div class="comment-indicator__icon">
          <img :src="icon"/>
        </div>
        <div class="comment-indicator__title">
          Comments&nbsp;({{commentsCount}})
        </div>
      </div>
    </el-button>
  </div>
</template>

<script>
  import CommentIcon from '@/assets/images/comment.svg'
  import CommentService from '@/services/CommentService'

  const service = new CommentService()

  export default {
    name: 'CommentIndicator',
    props: {
      entityType: {type: String, required: true},
      entityId: {type: Number}
    },
    data () {
      return {
        icon: CommentIcon,
        commentsCount: 0
      }
    },
    methods: {
      loadData () {
        if (!this.entityType || !this.entityId) {
          return
        }

        service.countComments(this.entityType, this.entityId)
          .then(result => {
            this.commentsCount = result.data
          })
          .catch(error => {
            this.commentsCount = 'Error fetching'
            this.$log.error(error)
          })
      }
    },

    watch: {
      'entityId' () {
        this.loadData()
      },
    },

    mounted () {
      this.$events.on('commentAdded', this.loadData)
      this.$events.on('commentDeleted', this.loadData)
      this.loadData()
    },

    beforeDestroy () {
      this.$events.off('commentAdded', this.loadData)
      this.$events.off('commentDeleted', this.loadData)
    }
  }
</script>

<style lang="scss">
  @import "~styles/globals/colors";

  .comment-indicator {
    align-items: center;
    justify-content: flex-end;
    display: flex;
  }

  .comment-indicator__box {
    text-transform: none;
    font-size: 1rem;
    align-items: center;
    justify-content: flex-end;
    display: flex;
    .comment-indicator__icon {
      margin-right: 0.5rem;
    }
    .comment-indicator__title {
      color: $text-action-color;
    }

  }
</style>
