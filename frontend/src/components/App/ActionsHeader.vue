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
  <el-row class="actions-header">
    <el-row class="actions-header__content">
      <h3 class="actions-header__title">{{ title || 'Unknown title' }}</h3>
      <div class="actions-header__buttons" :span="16">
        <el-button class="actions-header__action-buttons__cancel"
                   @click="actionCancel()">{{ cancelTitle || 'Cancel' }}
        </el-button>
        <el-button class="actions-header__action-buttons__submit"
                   type="primary"
                   :disabled="formIsLoading || formIsSaving || initialLockSave"
                   @click="save()">
          <div>{{ confirmTitle || 'Save'}}</div>
          <state-indicator v-if="formIsSaving || initialLockSave" state="processing"
                           id="save-state-indicator"></state-indicator>
        </el-button>
      </div>
    </el-row>
  </el-row>
</template>

<script>
  import FormCommons from '@/components/mixins/FormCommons'
  import StateIndicator from '@/components/App/StateIndicatorComponent'

  export default {
    components: {StateIndicator},

    data () {
      return {
        initialLockSave: false, // For locking save button right after clicking "Save".
        formIsSaving: false, // For further locking save button e.g. if a validation takes place.
        formIsLoading: false
      }
    },

    name: 'actions-header',

    props: {
      title: String,
      confirmTitle: String,
      cancelTitle: String,
      isDialog: Boolean
    },

    mixins: [FormCommons],

    events: {
      formSavingStarted () {
        this.formIsSaving = true
      },
      formSavingFinished () {
        this.formIsSaving = false
      },
      formLoadingStarted () {
        this.formIsLoading = true
      },
      formLoadingFinished () {
        this.formIsLoading = false
      }
    },

    methods: {
      save () {
        let self = this
        if (!this.formIsSaving) {
          this.initialLockSave = true
          setTimeout(() => {
            self.initialLockSave = false // Unlock initial lock.
          }, 300) // Lock the button for a short time so that form can handle the click and not be disturbed.
        }
        this.$emit('actionConfirm')
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~styles/globals/colors";

  .actions-header {
    margin-top: -2.5rem;
    margin-bottom: 2.538rem;
    background-color: $background-accent-color;
    border: 1px solid $text-header-color--disabled;
    padding-top: 5.05rem; /* 4.08rem;*/
    height: 7.85rem;

    .actions-header__title {
      font-size: 1.385rem;
      color: $text-color;
      float: left;
      text-transform: uppercase;
    }

    .actions-header__buttons {
      float: right;
      margin-top: -1.05rem;
      margin-right: 24%;
    }

    .actions-header__action-buttons__submit {
      height: 3.692rem;
      float: right;
    }

    button {
      width: 7.692rem;
      height: 3.154rem;
    }

    .actions-header__content {
      margin-top: -1.45rem;
    }
  }

  #save-state-indicator {
    margin-top: -28%;
  }
</style>
