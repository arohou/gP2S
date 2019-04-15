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
  <div class="nav-menu__item">
    <router-link :to="route" v-if="!disabled" active-class="nav-menu__item_active" class="em-nav__link em-nav__link-enabled"
                 :class="{'nav-menu__sub-item': isSubItem, 'nav-menu__item_active': selected}">
      <div class="state-indicator">&nbsp;</div>
      <div class="nav-menu__item__icon-box">
        <img v-if="icon" :src="icon"/>
      </div>
      <slot></slot>
    </router-link>
    <a v-else :disabled="true" class="em-nav__link em-nav__link-disabled" :class="{'nav-menu__sub-item': isSubItem}">
      <div class="nav-menu__item__icon-box">
        <img v-if="icon" :src="icon"/>
      </div>
      <slot></slot>
    </a>
  </div>
</template>

<script>
  export default {
    name: 'nav-menu-item',
    props: {
      route: {
        type: Object,
        required: true
      },
      icon: {
        type: String,
        required: true
      },
      disabled: {
        type: Boolean,
        default: false
      },
      isSubItem: {
        type: Boolean,
        default: false
      },
      selected: {
        type: Boolean,
        default: false
      }
    }
  }

</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  a:focus, a:active,
  input[type="reset"]::-moz-focus-inner,
  input[type="button"]::-moz-focus-inner,
  input[type="submit"]::-moz-focus-inner,
  select::-moz-focus-inner,
  input[type="file"] > input[type="button"]::-moz-focus-inner {
    outline: none !important;

  }
  .state-indicator {
    width: 1.231rem;
    float: left;
    margin-right: -1.231rem;
    height: 100%;
  }
  .em-nav__link.em-nav__link-enabled.nav-menu__sub-item .state-indicator {
    margin-left: -2rem;
    margin-right: 2rem;
  }
  .em-nav__link.em-nav__link-enabled.router-link-exact-active.nav-menu__item_active,
  .em-nav__link.em-nav__link-enabled.nav-menu__item_active .state-indicator {
    background-color: $icon-button-border;
  }
  .em-nav__link.em-nav__link-enabled.router-link-exact-active.nav-menu__item_active .state-indicator,
  .em-nav__link.em-nav__link-enabled.nav-menu__item_active, #settings-close .state-indicator {
    background-color: transparent;
  }

  .nav-menu__item, a {
    background-color: $background-accent-color;
    font-family: Helvetica;
    font-size: 1.231rem;
    letter-spacing: -0.1px;
    text-align: center;
    display: inline-block;
    height: 4rem;
    width: 100%;

    a:hover {
      // same as $icon-button-border but using rgba to have opacity
      background-color: rgba(196, 196, 196, 0.5);
    }
    .nav-menu__sub-item {
      font-size: 1.077rem;
      padding-left: 2rem;
    }

    .em-nav__link {
      text-decoration: none;
      display: flex;
      align-items: center;
      /*justify-content: space-around;*/
    }

    .nav-menu__item__icon-box{
      height: 2rem;
      width: 2rem;
      margin-left: 2rem;
      margin-right: 2rem;
    }

    img {
      -webkit-filter: grayscale(100%);
      -moz-filter: grayscale(100%);
      -o-filter: grayscale(100%);
      -ms-filter: grayscale(100%);
      filter: grayscale(100%);
      height: 100%;
    }

    .em-nav__link-enabled {
      color: #303030;
    }
    .em-nav__link-disabled {
      color: #7f7f7f;
      cursor: not-allowed;
    }

    .nav-menu__item_active {
      // same as $icon-button-border but using rgba to have opacity
      background-color: $icon-button-border;
      font-weight: bold;
      color: #3f3f3f;

      img {
        filter: grayscale(0%);
        -moz-filter: grayscale(0%);
        -ms-filter: grayscale(0%);
        -o-filter: grayscale(0%);
        filter: none; /* IE6-9 */
        zoom: 1; /* needed to trigger "hasLayout" in IE if no width or height is set */
        -webkit-filter: grayscale(0%); /* Chrome 19+, Safari 6+, Safari 6+ iOS */
      }
    }
  }


</style>
