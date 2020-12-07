<template>
  <div>
    <el-input
      :disabled="disabled"
      type="textarea"
      :rows="3"
      :placeholder="columnsFeilds"
      v-model="initParameter.value"
    >
    </el-input>
  </div>
</template>
<script>
//TODO 后面考虑用Table来做
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
    columnsFeilds() {
      let str = 'columns must be like "';
      this.initParameter.fields.forEach(element => {
        str += `${element},`;
      });
      str += '"';
      return str;
    }
  }
};
</script>

<style></style>
