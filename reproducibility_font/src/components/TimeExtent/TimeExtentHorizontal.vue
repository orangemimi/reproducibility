<!--a time extent -->
<template>
  <div>
    <el-date-picker v-model="timeExtents" type="datetimerange" range-separator="To" start-placeholder="Start time" end-placeholder="End time"></el-date-picker>
  </div>
</template>

<script>
export default {
  watch: {
    timeExtents: {
      handler(val) {
        console.log('dwd', val);
        this.$emit('getReturnTime', val, this.count);
        this.count++;
      },
      deep: true //true 深度监听
    }
  },

  computed: {},

  data() {
    return { timeExtents: '', count: 0 };
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped></style>
