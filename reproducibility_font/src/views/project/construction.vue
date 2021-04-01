<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <el-row :gutter="20">
          <el-col :span="12">
            <step-card
              :cardInfo="{ btnType: 'Context Definition' }"
              :projectInfo="projectInfo"
              style="height:450px;width:100%"
            ></step-card>
          </el-col>
          <el-col :span="12">
            <step-card
              :cardInfo="{ btnType: 'Resource Collection' }"
              :projectInfo="projectInfo"
              style="height:450px;width:100%"
            ></step-card>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <step-card
              :cardInfo="{ btnType: 'Simulation Construction' }"
              :projectInfo="projectInfo"
              style="height:750px;width:100%"
            ></step-card>
          </el-col>
        </el-row>
      </div>
      <div class="record">
        <el-popover placement="top" width="460" trigger="click">
          <div><record-list :projectInfo="projectInfo"></record-list></div>
          <el-button type="success" class="folder_collect" slot="reference" circle>
            <i class="el-icon-folder-opened"></i>
          </el-button>
        </el-popover>
      </div>
    </el-col>
  </div>
</template>

<script>
import stepCard from './components/StepCard';
import recordList from './components/RecordList';
import { get } from '@/axios';
export default {
  components: {
    stepCard,
    recordList
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
  .record {
    position: fixed;
    right: 50px;
    bottom: 75px;
    z-index: 100;
  }
}
</style>
