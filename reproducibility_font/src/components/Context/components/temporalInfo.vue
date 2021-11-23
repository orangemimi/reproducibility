<template>
  <div>
    <div class="block">
      <el-date-picker v-model="value" type="datetimerange" range-separator="-" start-placeholder="Start date" end-placeholder="End date" @change="change"></el-date-picker>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      value: [],
    };
  },
  methods: {
    getDate() {
      let date = new Date()
      let year = date.getFullYear()
      let month = date.getMonth()
      let day = date.getDate()
      this.value = [new Date(year, month + 1, day, 0, 0), new Date(year, month + 1, day + 1, 0, 0)]
    },
    change(val) {
      console.log(val)
    }
  },
  mounted() {
    this.getDate()
  }
};
</script>

<style lang="scss" scoped>
.block {
  text-align: center;
}
</style>