<template>
  <div>
    <el-row>
      <el-col :span="10">
        <el-input v-model="from" :disabled="disabled"></el-input>
      </el-col>
      <el-col :span="4">-</el-col>
      <el-col :span="10">
        <el-input v-model="to" :disabled="disabled"></el-input>
      </el-col>
    </el-row>
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
    from: {
      get() {
        return this.initParameter.value.split("-")[0];
      },
      set(newValue) {
        let toValue = this.initParameter.value.split("-")[1];
        this.initParameter.value = `${newValue}-${toValue}`;
        return newValue;
      }
    },
    to: {
      get() {
        return this.initParameter.value.split("-")[1];
      },
      set(newValue) {
        let fromValue = this.initParameter.value.split("-")[0];
        this.initParameter.value = `${fromValue}-${newValue}`;
        return newValue;
      }
    }
  },
  methods: {}
};
</script>

<style></style>
