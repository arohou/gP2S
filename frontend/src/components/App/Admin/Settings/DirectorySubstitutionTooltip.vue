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
  <div class="directory-substitution-tooltip">
    <h4 class="example">Example directory name: <code>{{ directory }}</code></h4>
    <p>Variable substitution will occur to generate a directory name, when microscopy sessions are created. Variables
      are denoted by "<code>${...}</code>" and the following variables are recognized:</p>
    <ul class="substitutions-list">
      <li><code>${ProjectLabel}</code>: project name, string, e.g. "MyProject"</li>
      <li><code>${GridID}</code>: grid record number, zero-padded 6-digit int, e.g. "000143"</li>
      <li><code>${GridLabel}</code>: grid label, string, e.g. "Mybestgrid"</li>
      <li><code>${MicroscopyLabel}</code>: microscopy session label, string, e.g. "MyMicroscopySession"</li>
      <li><code>${MicroscopySessionID}</code>: zero-padded 6-digit int, e.g. "000098"</li>
      <li><code>${MicroscopyStartDate}</code>: date of start of microscopy session in YYMMDD format, e.g. 171004</li>
      <li><code>${MicroscopyStartTime}</code>: time of start of microscopy session in HHMM format, e.g. 0314</li>
      <li><code>${MicroscopeLabel}</code>: label of the microscope used, string, e.g. "Krios2"</li>
    </ul>
  </div>
</template>

<script>
  import DirectoryName from '@/utils/DirectoryName'
  import Token from '@/utils/MicroscopySessionToken'

  export default {
    name: 'directory-substitution-tooltip',

    props: {
      templateString: String
    },

    computed: {
      directory: function () {
        return DirectoryName.fromTokenString(
          this.templateString,
          [
            [Token.PROJECT_LABEL, 'MyProject'],
            [Token.GRID_ID, '000143'],
            [Token.GRID_LABEL, 'Mybestgrid'],
            [Token.MICROSCOPY_LABEL, 'MyMicroscopySession'],
            [Token.MICROSCOPY_SESSION_ID, '000098'],
            [Token.MICROSCOPY_START_DATE, '171004'],
            [Token.MICROSCOPY_START_TIME, '031415'],
            [Token.MICROSCOPE_LABEL, 'Krios2']
          ]
        ).toString()
      }
    }
  }
</script>

<style scoped lang="scss">
  .example {
    margin-bottom: 1em;
  }

  .substitutions-list {
    margin-top: 1em;
  }
</style>
