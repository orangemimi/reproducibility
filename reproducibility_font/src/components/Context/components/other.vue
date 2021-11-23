<template>
  <div class="main">
    <el-input type="textarea" :rows="6" placeholder="Please enter" v-model="textarea" style="width: 100%; margin-top: 5px"></el-input>
    <el-button type="primary" style="margin-top: 10px">Submit</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      textarea: '',
    };
  },
};
</script>

<style lang="scss" scoped>
.main {
  text-align: center;
}
</style>