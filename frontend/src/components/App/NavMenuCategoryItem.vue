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
    <a :disabled="true" class="em-nav__link" :class="{'nav-menu__sub-item': isSubItem, 'em-nav__link-disabled': disabled, 'em-nav__link-enabled': !disabled}">
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
      }
    }
  }

</script>

<style lang="scss" scoped>
  a:focus, a:active,
  input[type="reset"]::-moz-focus-inner,
  input[type="button"]::-moz-focus-inner,
  input[type="submit"]::-moz-focus-inner,
  select::-moz-focus-inner,
  input[type="file"] > input[type="button"]::-moz-focus-inner {
    outline: none !important;

  }

  .nav-menu__item, a {
    background-color: #f2f2f2;
    font-family: Helvetica;
    font-size: 1.231rem;
    letter-spacing: -0.1px;
    text-align: center;
    display: inline-block;
    height: 4rem;
    width: 100%;

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
  }


</style>
