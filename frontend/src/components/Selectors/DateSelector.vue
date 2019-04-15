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
  <div class="date-selector">
    <div v-if="this.isMobile"
         class="el-date-editor el-input el-date-editor--date">
      <i class="el-input__icon el-icon-date"></i>
      <input autocomplete="off" type="date" rows="2"
             class="el-input__inner"
             :value="mobileDate"
             @input="updateValueFromMobile($event.target.value)">
    </div>

    <el-date-picker v-else
                    v-model="desktopDate"
                    :format="DESKTOP_DATE_FORMAT"
                    @input="updateValueFromDesktop">
    </el-date-picker>
  </div>
</template>

<script>
  import MobileDetect from 'mobile-detect'
  import moment from 'moment'

  export default {
    name: 'date-selector',

    props: ['value'],

    data () {
      return {
        md: new MobileDetect(window.navigator.userAgent),
        desktopDate: '',
        mobileDate: ''
      }
    },

    computed: {
      isMobile: function () {
        return this.md.mobile() !== null
      }
    },

    watch: {
      'value': function (newValue) {
        // noinspection EqualityComparisonWithCoercionJS
        if (newValue == 'Invalid Date') {
          this.clearDate()
          return
        }

        this.setDesktopDate(newValue)
        this.setMobileDate(newValue)
      }
    },

    methods: {
      clearDate () {
        this.desktopDate = this.mobileDate = ''
      },

      setDesktopDate (value) {
        this.desktopDate = moment(value).toDate() || ''
      },

      setMobileDate (value) {
        this.mobileDate = moment(value).format(this.MOBILE_DATE_FORMAT) || ''
      },

      updateValueFromDesktop (date) {
        this.$log.debug('Date selected: ', date)
        this.$emit('input', moment(date).toDate())
      },

      updateValueFromMobile (date) {
        this.$log.debug('Date selected: ', date)
        this.$emit('input', moment(date, this.MOBILE_DATE_FORMAT).toDate())
      }
    },

    created () {
      this.DESKTOP_DATE_FORMAT = 'yyyy-MM-dd'
      this.MOBILE_DATE_FORMAT = 'YYYY-MM-DD'
    }
  }
</script>

<style lang="scss" scoped>
  .el-date-editor {
    width: 100% !important;
  }
</style>
