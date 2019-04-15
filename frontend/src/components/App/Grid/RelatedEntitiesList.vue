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

<script>
  import SamplePrintItem from '@/components/App/Sample/PrintView'
  import ProteinPrintItem from '@/components/App/Protein/PrintView'
  import LigandPrintItem from '@/components/App/Ligand/PrintView'

  import RelatedEntitiesList from '@/components/App/RelatedEntitiesList'

  export default {
    name: 'processing-session-report-view',

    mixins: [RelatedEntitiesList],

    components: {
      SamplePrintItem, ProteinPrintItem, LigandPrintItem
    },
  }
</script>
