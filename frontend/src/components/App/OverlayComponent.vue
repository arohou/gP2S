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
  <el-dialog class="overlay-component"
             size="large"
             :close-on-click-modal="false"
             :close-on-press-escape="false"
             :visible.sync="dialogVisible"
             :showClose="showClose"
             :before-close="handleClose">
    <slot>
      <p>Empty content</p>
    </slot>
  </el-dialog>
</template>

<script>

  export default {
    name: 'overlay-component',

    props: {
      visible: {
        type: Boolean,
        default: false
      },
      showClose: {
        type: Boolean,
        default: false
      }
    },

    computed: {
      dialogVisible: {
        get: function () {
          return this.visible
        },

        set: function (newValue) {
          this.$emit('update:visible', newValue)
          if (!newValue && this.visible) {
            this.$emit('update:visible', false)
          }
        }
      }
    },
    methods: {
      handleClose () {
        this.dialogVisible = false // For closing dialog with "X" icon.
      }
    },

    events: {
      hideDialog () {
        this.$emit('update:visible', false)
      }
    }
  }

</script>

<style lang="scss" scope>
  /* Cancel default padding, since we don't use any header. */
  .el-dialog__header {
    padding: 0;
  }

  .el-dialog__headerbtn {
    margin-top: 1em;
    margin-right: 1em;
  }

  .el-dialog__body .actions-header {
    margin-top: -1.0rem;
  }

  .el-dialog__body .actions-header .actions-header__content {
    margin-top: -1.95rem;
  }
</style>
