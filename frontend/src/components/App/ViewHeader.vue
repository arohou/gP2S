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
  <el-row class="view-header">
    <el-col :xs="24" :sm="16">
      <div class="view-header__title">
        <div class="view-header__title__icon">
          <img v-if="icon" :alt="iconAlt" :src="icon"/>
          <slot>
            <div class="view-header__content__title">{{ title }}</div>
            <div class="view-header__content__title__link"><a :href="viewLink">{{ title }}</a></div>
          </slot>
        </div>
      </div>
      <div class="view-header__content">
        <div class="view-header__content__metadata">
          <span id="view-header__content__metadata__id" v-if="!_.isEmpty(hashId)">{{ hashId }}, </span>
          <span id="view-header__content__metadata__created-date">&nbsp;created on {{ createdDate | moment('YYYY.MM.DD, HH:mm:ss')}}</span>
          <span>, by&nbsp;</span>
          <user-full-name :userId='createdBy'/>
        </div>
        <slot name="component-protocol-tag"></slot>
      </div>
    </el-col>
    <el-col :xs="24" :sm="8" class="view-header__actions">
      <slot name="comment">
        <div class="view-header__comments">&nbsp;</div>
      </slot>
      <action-buttons :disableCopy="disableCopy"></action-buttons>
    </el-col>
  </el-row>
</template>

<script>
  import ActionButtons from '@/components/App/ActionButtons'
  import UserFullName from '@/components/App/UserFullName'

  export default {
    name: 'view-header',

    props: {
      title: {
        type: String,
        default: 'Unknown Title'
      },

      slug: {
        type: String,
        default: ''
      },

      id: {
        type: [Number, String]
      },

      projects: {
        type: Array,
        default: () => []
      },

      createdDate: {
        type: [Date, Number],
        default: null
      },

      createdBy: {
        type: String,
        default: null
      },

      icon: {
        type: String
      },

      iconAlt: {
        Type: String,
        default: 'Icon'
      },

      disableCopy: {type: Boolean}
    },

    computed: {
      hashId: function () {
        return !!this.id ? '#' + this.id : ''
      },

      viewLink: function () {
        let viewName =
          _.get(this.$parent, '$parent.$options.name', 'unknown-slug')
            .replace('-view', '').replace('-print', '').replace('-', '_')
        let projectId = _.get(this.$parent, '$parent.projectId', 'unknown-project')

        return window.location.href.split('#', 1)[0] + '\\#/' + projectId + '/' + viewName + '/' + this.slug
      }
    },

    components: {ActionButtons, UserFullName}
  }
</script>

<style lang="scss">
  @import "~styles/globals/colors";

  .view-header {
    border-bottom: 1px solid $text-header-color--disabled;
    margin-bottom: 1rem;

    .view-header__title {
      .view-header__title__icon {
        display: inline-flex;
        margin-right: 0.692rem;

        .view-header__content__title, .view-header__content__title__link {
          font-size: 1.846rem;
          color: $text-color;
          margin-top: 0.15rem;
          margin-left: 1rem;
          width: 100%;
          padding-right: 1rem;
        }
        .view-header__content__title__link {
          display: none;
        }
        .view-header__content__title__link a {
          color: $text-action-color;
          text-decoration: none;
        }
      }
      img {
        width: 2.462rem;
        height: 2.462rem;
      }
    }
    .view-header__content {
      display: inline-block;

      .view-header__content__metadata {
        font-size: 1rem;
        color: $text-header-color--disabled;
        margin-bottom: 0.231rem;
        display: flex;
      }
    }
    .view-header__actions {
      margin-bottom: 0.692rem;
    }
  }
</style>
