<template>
  <div>
    <el-slider
      :disabled="disabled"
      v-model="initParameter.value"
      :step="new Number(initParameter.step)"
      :max="new Number(initParameter.max)"
      :min="new Number(initParameter.min)"
    ></el-slider>
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
  created() {
    this.initParameter.value = new Number(this.initParameter.value).valueOf();
  }
};
</script>

<style></style>
