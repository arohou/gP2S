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
  import View from '@/components/App/Ligand/View'
  import ViewCommentBase from '@/components/App/PrintViewCommentBase'

  export default {
    name: 'ligand-print-view',

    mixins: [View],

    props: ['ligandData'],

    components: {
      ViewCommentBase // Comments view component has been replaced.
    },

    methods: {
      fetchLigand (id) {
        this.ligand = this.ligandData.aliquot
        return Promise.resolve()
      },

      initEvents () {},
      removeEvents () {}
    }
  }
</script>
