<!-- selec card vue -->
<template>
  <div class="select_data">
    <div class="select-data select-data-line">
      <div class="data-name">{{ currentItem.name }}</div>
      <i class="el-icon-close" @click="remove"></i>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    currentItem: {
      type: Object
    }
  },
  components: {},

  watch: {},

  computed: {},

  data() {
    return {};
  },

  methods: {
    remove() {
      this.$emit('removeItem', this.currentItem);
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.select_data {
  .select-data-line {
    margin: 15px 0 0 0;
    line-height: 30px;
    .el-button {
      width: 138px;
    }
  }
  .select-data {
    background-color: #f0f9eb;
    display: inline-block;
    height: 32px;
    padding: 0 10px;

    font-size: 12px;
    color: #67c23a;
    border: 1px solid #e1f3d8;
    border-radius: 4px;
    box-sizing: border-box;
    white-space: nowrap;

    .data-name {
      float: left;
      width: 200px;
      font-weight: 600;
      font-size: 16px;
      // padding-left: 5px;
    }
  }

  .select-data:hover {
    // width: 250px;
    cursor: pointer;
    transition: all 0.2s ease-out;
    color: #60a83c;
    background-color: #c8f3d3;
  }
}
</style>
