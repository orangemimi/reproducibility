<!-- multiply input -->
<template>
  <div>
    <el-input size="mini" v-model="inputObject.key" style="width:70%" placeholder=""></el-input>
    <el-input size="mini" v-model="inputObject.value" style="width:70%" placeholder=""></el-input>
  </div>
</template>

<script>
export default {
  components: {},

  watch: {
    inputObject: {
      handler(val) {
        console.log(val);
      },
      deep: true
    }
  },

  computed: {},

  data() {
    return {
      inputObject: {
        key: '',
        value: ''
      }
    };
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped></style>
