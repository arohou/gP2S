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
  <div class="action-buttons">
    <el-button id="action-buttons__print"
               @click="print()" title="Print">
      <img src="~@/assets/images/print.svg" alt="Print"/>
    </el-button>
    <el-button id="action-buttons__edit"
               @click="$events.emit('actionEdit')" title="Edit">
      <img src="~@/assets/images/edit.svg" alt="Edit"/>
    </el-button>
    <el-button id="action-buttons__copy"
               :disabled="disableCopy"
               @click="$events.emit('actionCopy')" title="Copy">
      <img src="~@/assets/images/copy.svg" alt="Copy"/>
    </el-button>
    <el-button disabled id="action-buttons__delete"
               @click="$events.emit('actionDelete')" title="Delete">
      <img src="~@/assets/images/delete.svg" alt="Delete"/>
    </el-button>
  </div>
</template>

<script>
  export default {
    name: 'action-buttons',

    props: {
      disableCopy: {
        type: Boolean,
        default: false
      }
    },
    methods: {
      print () {
        window.print()
      }
    }
  }
</script>

<style lang="scss" scoped>
  .action-buttons {
    text-align: end;
  }
</style>
