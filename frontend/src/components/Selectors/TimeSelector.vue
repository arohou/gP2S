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
  <div class="time-selector">
    <div v-if="this.isMobile"
         class="el-date-editor el-input el-date-editor--time">
      <i class="el-input__icon el-icon-time"></i>
      <input autocomplete="off" type="time" rows="2"
             class="el-input__inner"
             :value="mobileTime"
             @input="updateValueFromMobile($event.target.value)">
    </div>

    <el-time-picker v-else
                    v-model="desktopTime"
                    :format="'HH:mm'"
                    @input="updateValueFromDesktop">
    </el-time-picker>
  </div>
</template>

<script>
  import MobileDetect from 'mobile-detect'
  import moment from 'moment'

  export default {
    name: 'time-selector',

    props: ['value'],

    data () {
      return {
        md: new MobileDetect(window.navigator.userAgent),
        mobileTime: '',
        desktopTime: '',
      }
    },

    computed: {
      isMobile: function () {
        return this.md.mobile() !== null
      }
    },

    watch: {
      'value': function (newValue) {
        this.$log.debug('New time value:', newValue)
        // noinspection EqualityComparisonWithCoercionJS
        if (newValue == 'Invalid Date') {
          this.clearTime()
          return
        }

        this.setDesktopTime(newValue)
        this.setMobileTime(newValue)
      },

    },

    methods: {
      clearTime () {
        this.desktopTime = this.mobileTime = ''
      },

      setDesktopTime (value) {
        this.desktopTime = moment(value).toDate() || ''
      },

      setMobileTime (value) {
        this.mobileTime = moment(value).format(this.MOBILE_TIME_FORMAT) || ''
      },

      updateValueFromMobile (time) {
        this.$log.debug('Time selected: ', time)
        this.$emit('input', moment(time, this.MOBILE_TIME_FORMAT).toDate())
      },

      updateValueFromDesktop (time) {
        this.$log.debug('Time selected: ', time)
        this.$emit('input', moment(time).toDate())
      }
    },

    created () {
      this.MOBILE_TIME_FORMAT = 'HH:mm'
    }
  }
</script>

<style lang="scss" scoped>
  .el-date-editor {
    width: 100% !important;
  }
</style>
