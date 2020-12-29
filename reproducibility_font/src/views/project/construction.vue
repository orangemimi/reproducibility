<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <el-col :span="12">
          <step-card
            :cardInfo="{ btnType: 'Context Definition' }"
            :projectInfo="projectInfo"
          ></step-card>
        </el-col>
      </div>
      <!-- <el-row>
        <el-popover placement="top" width="460" trigger="click">
          <div><history-record></history-record></div>
          <el-button type="success" class="folder_collect" slot="reference" circle>
            <i class="el-icon-folder-opened"></i>
          </el-button>
        </el-popover>
        <el-col :span="4"></el-col>
        <el-col :span="20"></el-col>
      </el-row> -->
    </el-col>
  </div>
</template>

<script>
import stepCard from './components/StepCard';
// import historyRecord from './components/Record';
import { get } from '@/axios';
export default {
  components: {
    stepCard
    // historyRecord
  },
  data() {
    return {
      projectId: this.$route.params.id,
      projectInfo: {}
      // cardInfos: [{ btnType: 'Context Definition' }, { btnType: 'Resource Collection' }]
    };
  },

  methods: {
    async init() {
      await this.getProjectInfo();
      // await this.judgeRole(this.projectInfo);
    },

    async getProjectInfo() {
      let data = await get(`/projects/user/${this.projectId}`);
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
}
</style>
