<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <el-row :gutter="20">
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
        </el-row>
      </div>
      <div class="record">
        <div class="block">
          <el-timeline>
            <el-timeline-item v-for="(activity, index) in activities" :key="index" :icon="activity.icon" :type="activity.type" :color="activity.color" :size="activity.size" :timestamp="activity.timestamp">
              {{ activity.content }}
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
import { getProjectAndUsers } from '@/api/request';
export default {
  components: {
    stepCard
    // recordList
  },
  data() {
    return {
      projectId: this.$route.params.id,
      projectInfo: {},
      activities: [
        {
          content: 'Context Definition',
          timestamp: '2018-04-12 20:46',
          size: 'large',
          type: 'primary',
          icon: 'el-icon-more'
        },
        {
          content: 'Resource Collection',
          timestamp: '2018-04-03 20:46',
          color: '#0bbd87'
        },
        {
          content: 'Simulation Scenario',
          timestamp: '2018-04-03 20:46',
          size: 'large'
        },
        {
          content: 'Expected Results',
          timestamp: '2018-04-03 20:46'
        }
      ]
      // cardInfos: [{ btnType: 'Context Definition' }, { btnType: 'Resource Collection' }]
    };
  },

  methods: {
    async init() {
      await this.getProjectInfo();
      // await this.judgeRole(this.projectInfo);
    },

    async getProjectInfo() {
      let data = await getProjectAndUsers(this.projectId);
      console.log(data);
      this.projectInfo = data.project;
      this.creator = data.creator;
      this.members = data.members;
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
    right: 50px;
    bottom: 75px;
    z-index: 100;
  }
}
</style>
