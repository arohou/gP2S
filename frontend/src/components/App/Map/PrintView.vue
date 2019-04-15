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
  import View from '@/components/App/Map/View'
  import ViewCommentBase from '@/components/App/PrintViewCommentBase'

  export default {
    name: 'map-print-view',

    mixins: [View],

    props: ['map'],

    components: {
      ViewCommentBase // Comments view component has been replaced.
    },

    methods: {
      loadEntity: function (id) {
        this.entity = this.map

        this._service.getMapLinks(this.entity.id).then(result => {
          this.createGroupedRelationships(result.data)
        }).catch(error => this.$log.error(error))
      },
      initEvents () {},
      removeEvents () {}
    }
  }
</script>
