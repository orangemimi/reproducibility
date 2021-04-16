<!--  -->
<template>
  <div class="main">
    <el-card :class="instance.status == 1 ? 'successCard' : 'runningCard'" shadow="hover">
      <div class="task_name">
        {{ instance.name }}
      </div>
      <div class="task_createTime">
        <i class="el-icon-time" />
        {{ instance.createTime }}
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
  margin-bottom: 15px;
  text-align: center;
  // padding-right: 50px;
  /deep/ .el-card {
    // padding: 5px;
    width: 100%;
    height: 100%;
  }
  /deep/ .el-card__body {
    padding: 10px 5px;
  }
  .successCard {
    background-color: #f0f9eb;
    border: 1px solid #67c23a;
    border-top: 5px solid #67c23a;
  }
  .runningCard {
    background-color: #fdf6ec;
    border: 1px solid #e6a23c;
    border-top: 5px solid #e6a23c;
  }

  .el-card:hover {
    // background-color: #3067d61c;
    box-shadow: 0px 0px 5px 1px rgba(27, 94, 238, 0.2);
    cursor: pointer;
  }

  .task_name {
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 10px;
    // padding-left: 10px;
  }
  .task_createTime {
    font-size: 12px;
    text-align: right;
    color: #5c5c5c;
    // font-weight: 600;
  }
}
</style>
