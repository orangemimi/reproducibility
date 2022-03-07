<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <el-row :gutter="20">
          <el-col :span="24">
            <step-card :cardInfo="{ btnType: 'Content Definition' }" style="height: 450px; width: 100%"></step-card>
          </el-col>
          <el-col :span="24">
            <step-card :cardInfo="{ btnType: 'Resource Collection' }" :projectInfo="projectInfo" style="height: 450px; width: 100%"></step-card>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <step-card :cardInfo="{ btnType: 'Simulation Scenario' }" style="height: 950px; width: 100%"></step-card>
          </el-col>
        </el-row>
      </div>
      <div class="record">
        <div class="block">
          <el-timeline>
            <el-timeline-item
              icon="el-icon-edit"
              :type="completion.context.length > 0 ? 'success' : 'warning'"
              size="large"
              :timestamp="dateFormat(completion.context.updateTime)"
            >
              Context Definition
            </el-timeline-item>

            <el-timeline-item
              icon="el-icon-folder"
              :type="completion.resource.length > 0 ? 'success' : 'warning'"
              size="large"
              :timestamp="dateFormat(completion.resource.updateTime)"
            >
              Resource Collection
            </el-timeline-item>

            <el-timeline-item
              icon="el-icon-sunny"
              :type="completion.scenario.length > 0 ? 'success' : 'warning'"
              size="large"
              :timestamp="dateFormat(completion.scenario.updateTime)"
            >
              Simulation Scenario
            </el-timeline-item>

            <el-timeline-item
              icon="el-icon-document"
              :type="completion.results.length > 0 ? 'success' : 'warning'"
              size="large"
              :timestamp="dateFormat(completion.results.updateTime)"
            >
              Excepted Results
            </el-timeline-item>
          </el-timeline>
        </div>
        <!-- <el-popover placement="top" width="460" trigger="click">
          <record-list></record-list>
          <el-button type="success" class="folder_collect" slot="reference" circle>
            <i class="el-icon-folder-opened"></i>
          </el-button>
        </el-popover> -->
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
    stepCard,
    // recordList
  },
  data() {
    return {
      projectId: this.$route.params.id,
      projectInfo: {},
      completion: {
        context: [],
        resource: [],
        scenario: [],
        results: [],
      },
      context: {},
      resource: {},
      scenario: {},
      results: {},
      // cardInfos: [{ btnType: 'Context Definition' }, { btnType: 'Resource Collection' }]
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
    },
  },
  created() {
    this.init();
  },
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
