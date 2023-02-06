<template>
  <div class="temporalInfo">
    <p style="margin-bottom: 10px">TemporalInfo</p>
    <el-date-picker v-model="value" type="datetimerange" range-separator="-" start-placeholder="Start" end-placeholder="End" @change="change"></el-date-picker>
    <!-- <el-button icon="el-icon-close" circle size="mini" style="float: right" @click="close"></el-button> -->
  </div>
</template>

<script>
export default {
  data() {
    return {
      value: JSON.parse(JSON.stringify(this.temporalInfo))
    };
  },
  watch: {
    temporalInfo: {
      handler(val) {
        this.value = JSON.parse(JSON.stringify(val));
      },
      deep: true
    }
  },
  props: {
    temporalInfo: {
      type: Array
    }
  },
  methods: {
    // close() {
    //   console.log(this.value);
    //   this.$emit('close');
    // },
    change(val) {
      console.log(val);
      this.$emit('date', val);
    }
  }
};
</script>

<style lang="scss" scoped>
.temporalInfo {
  color: #606266;
}
</style>
