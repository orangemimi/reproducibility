<!--  -->
<template>
  <div class="rightBar">
    <div class="instances">
      <el-row class="add_ins">
        <el-card shadow="hover" class="box-card">
          <i class="el-icon-plus" />
        </el-card>
      </el-row>
      <el-row>
        <div v-for="(item, index) in instanceList" :key="index">
          <instance-card :instanceItem="item" :taskItem="currentTask" @showInstanceStatus="showInstanceStatus"></instance-card>
        </div>
      </el-row>
    </div>
    <div class="page">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page.sync="instancePageFilter.page"
        :page-size="instancePageFilter.pageSize"
        background
        layout="prev, pager, next"
        :total="instanceList.length + 1"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import instanceCard from '_com/Cards/InstanceCard';
export default {
  components: { instanceCard },

  watch: {},

  computed: {},

  data() {
    return {
      //currentInstance;
      currentTaskInstance: {}
    };
  },

  methods: {
    async showInstanceStatus(value) {
      this.currentTaskInstance = value;
      await this.getOutputs(value.tid);
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.rightBar {
  // position: relative;
  float: left;
  // right: 0;
  width: 200px;
  margin-left: 5px;
  padding: 0 5px;
  box-shadow: 0px 0px 5px rgb(207, 207, 207);
  background-color: rgba(243, 243, 243, 0.9);
  .instances {
    width: 100%;
    .add_ins {
      font-size: 30px;
      margin-bottom: 10px;
      text-align: center;
      /deep/.el-card {
        background-color: rgba($color: #ffffff, $alpha: 0.8);
      }
    }
    .add_ins:hover {
      cursor: pointer;
    }
  }
  .page {
    position: absolute;
    bottom: 0;
  }
}
</style>
