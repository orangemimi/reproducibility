<!--reproduction-->
<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <!-- <el-row :gutter="20">
          <el-col :span="12">
            <step-card :cardInfo="{ btnType: 'Context Definition' }" style="height:450px;width:100%"></step-card>
          </el-col>
          <el-col :span="12">
            <step-card :cardInfo="{ btnType: 'Resource Collection' }" style="height:450px;width:100%"></step-card>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <step-card :cardInfo="{ btnType: 'Simulation Scenario' }" style="height:900px;width:100%"></step-card>
          </el-col>
        </el-row> -->

        <el-timeline>
          <el-timeline-item timestamp="2018/4/12" placement="top">
            <el-row>
              <el-col :span="12">
                <step-card :cardInfo="{ btnType: 'Context Definition' }" style="height:450px;width:100%"></step-card>
              </el-col>
            </el-row>
          </el-timeline-item>
          <el-timeline-item timestamp="2018/4/3" placement="top">
            <el-row>
              <el-col :span="12">
                <step-card :cardInfo="{ btnType: 'Resource Collection' }" style="height:450px;width:100%"></step-card>
              </el-col>
            </el-row>
          </el-timeline-item>
          <el-timeline-item timestamp="2018/4/3" placement="top">
            <el-row>
              <el-col :span="12">
                <step-card :cardInfo="{ btnType: 'Expected Results' }" style="height:450px;width:100%"></step-card>
              </el-col>
            </el-row>
          </el-timeline-item>
          <el-timeline-item timestamp="2018/4/2" placement="top">
            <el-row>
              <el-col :span="24">
                <step-card :cardInfo="{ btnType: 'Reproducible Simulation Scenario' }" style="height:900px;width:100%"></step-card>
              </el-col>
            </el-row>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-col>
  </div>
</template>

<script>
import stepCard from '_com/StepCard';
// import recordList from '_com/RecordList';
import { getProjectAndUsers, getPerformanceByProjectId } from '@/api/request';
import { dateFormat } from '@/utils/utils';
export default {
  components: {
    stepCard
    // recordList
  },
  data() {
    return {
      projectId: this.$route.params.id,
      projectInfo: {},
      completion: [],
      context: {},
      resource: {},
      scenario: {},
      results: {}
    };
  },

  methods: {
    async init() {
      await this.getProjectInfo();
      await this.getPerformance();
      // await this.judgeRole(this.projectInfo);
    },

    async getProjectInfo() {
      let data = await getProjectAndUsers(this.projectId);
      // console.log(data);
      this.projectInfo = data.project;
      this.creator = data.creator;
      this.members = data.members;
    },

    async getPerformance() {
      let data = await getPerformanceByProjectId(this.projectId);
      this.completion = data.completion;
    },
    dateFormat(time) {
      if (time == null) {
        return 'You have not do any operation';
      }
      return dateFormat(time);
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
  min-height: calc(100vh - 240px);
  .main-card {
    background-color: white;
    min-height: calc(100vh - 242px);
    padding: 20px 32px;
  }
  .record {
    position: fixed;
    right: 30px;
    bottom: 75px;
    z-index: 100;
    .block {
      /deep/ .el-timeline-item__tail {
        left: 8px;
      }
      /deep/.el-timeline-item__wrapper {
        padding-left: 35px;
      }
      /deep/ .el-timeline-item__node--large {
        height: 25px;
        width: 25px;
      }
    }
  }
}
</style>
