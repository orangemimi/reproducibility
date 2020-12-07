<template>
  <div>
    <el-select v-model="initParameter.value" :disabled="disabled">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      >
      </el-option>
    </el-select>
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
  computed: {
    options() {
      let options = [];
      Object.entries(this.initParameter.keyValue).forEach(element => {
        options.push({
          label: element[1],
          value: element[0]
        });
      });
      return options;
    }
  }
};
</script>

<style></style>
