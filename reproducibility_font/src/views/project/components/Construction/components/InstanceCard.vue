<!--  -->
<template>
  <div class="main">
    <el-card>
      <div class="task_name">
        {{ instance.id }}
      </div>
      <div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  props: {
    instanceItem: {
      type: Object
    }
  },

  watch: {
    instanceItem: {
      handler(val) {
        this.instance = val;
        this.init();
      },
      deep: true
    }
  },

  computed: {},

  data() {
    return {
      instance: this.instanceItem
    };
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  height: 60px;
  width: 100%;

  /deep/ .el-card {
    box-shadow: 0 1px 5px 0 rgba(161, 161, 161, 0.1);
    
    .el-card__body {
      padding: 5px;
      width: 100%;
      height: 80px;
    }
  }

  .el-card:hover {
    // background-color: #3067d61c;
    border: 1px solid #25252534;
    cursor: pointer;
  }

  .task_name {
    font-size: 14px;
    font-weight: 600;
    padding-left: 10px;
  }
}
</style>
