<template>
  <div>
    <mavon-editor
      v-model="value"
      @save="saveDoc"
    ></mavon-editor>
  </div>
</template>
<script>
import { mavonEditor } from "mavon-editor";
import "mavon-editor/dist/css/index.css";
export default {
  props: [],
  data() {
    return { value: "" };
  },
  methods: {
    saveDoc(markdown, html) {
      console.log(html);
    }
  },
  components: { mavonEditor }
};
</script>

<style scoped>
</style>