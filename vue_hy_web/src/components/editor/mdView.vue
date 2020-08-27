<template>
  <div>
      <mavon-editor
            :value="content"
            defaultOpen="preview"
            :boxShadow="false"
            style="z-index:1;min-height:200px;max-width:700px"
            :editable="false"
            :subfield="false"
            :toolbarsFlag="false"
          >
          </mavon-editor>
  </div>
</template>

<script>

import {mavonEditor} from "mavon-editor";
import "mavon-editor/dist/css/index.css"

export default {
  name: "home",
  components: {
    mavonEditor:mavonEditor
  },
  props:{
    content:{
      type:String
    }
  }
};
</script>
<style scoped>
 .v-note-navigation-wrapper transition{
   margin-right: 500px !important;
 }
</style>