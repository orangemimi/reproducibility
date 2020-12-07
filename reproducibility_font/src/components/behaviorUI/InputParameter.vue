<template>
  <div>
    <el-input v-model="initParameter.value" :disabled="disabled"></el-input>
  </div>
</template>
<script>
export default {
  props: {
    disabled: {
      type: Boolean,
      default: false
    },
    initParameter: {
      type: Object
    }
  },

  methods: {}
};
</script>

<style></style>
