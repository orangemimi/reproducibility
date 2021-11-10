<!--reproduction-->
<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <!-- <el-timeline>
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
        </el-timeline> -->

        <el-form ref="context" :model="context" label-width="110px" @submit.native.prevent size="mini">
          <el-form-item label="Theme:">
            <div v-if="context != null">
              <el-tag :key="tagIndex" v-for="(tag, tagIndex) in context.themes" closable :disable-transitions="false" @close="delTags(tag)" style="margin: 0 2px">
                {{ tag }}
              </el-tag>
            </div>
          </el-form-item>
          <el-form-item label="Purpose:">
            <div>{{ context.purpose }}</div>
          </el-form-item>
          <el-form-item label="Object:">
            <div>{{ context.objects }}</div>
          </el-form-item>
          <el-form-item label="Temporal scale:">
            <div>{{ context.temporalScale }}</div>
          </el-form-item>
          <el-form-item label="Spatio scale:">
            <div>{{ context.spatialScale }}</div>
          </el-form-item>
          <el-form-item label="Method:">
            <div>{{ context.methods }}</div>
          </el-form-item>
          <el-form-item label="Discussion:">
            <div>{{ context.discussion }}</div>
          </el-form-item>
        </el-form>

        <div class="row-style">
          <!-- <div class="table"> -->
          Related Resource
          <el-table
            ref="multipleTable"
            :data="dataItemListFromResource"
            tooltip-effect="dark"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            max-height="350"
            :row-style="{ height: '0' }"
            :cell-style="{ padding: '4px' }"
          >
            <template slot="empty">
              Please upload a file
            </template>
            <el-table-column label="Name" show-overflow-tooltip>
              <template #default="scope">{{ scope.row.name }}</template>
            </el-table-column>
            <el-table-column label="Type" width="70">
              <template #default="scope">{{ scope.row.type }}</template>
            </el-table-column>
            <el-table-column label="File size" width="100">
              <template #default="scope">{{ scope.row.fileSize }}</template>
            </el-table-column>
            <el-table-column label="Upload time" width="180" show-overflow-tooltip>
              <template #default="scope">{{ scope.row.createTime }}</template>
            </el-table-column>
          </el-table>
          <!-- </div> -->
        </div>

        <div class="scenario">
          <re-scenario-content />
        </div>
      </div>
    </el-col>
  </div>
</template>

<script>
// import stepCard from '_com/StepCard';
// import recordList from '_com/RecordList';
import { getProjectAndUsers, getPerformanceByProjectId, getContextByProjectId, getFileItemByCreatorId } from '@/api/request';
import { dateFormat } from '@/utils/utils';

import ReScenarioContent from '_com/Scenario/reScenario';

export default {
  components: {
    ReScenarioContent
    // stepCard
    // recordList
  },
  data() {
    return {
      projectId: this.$route.params.id,
      projectInfo: {},
      completion: [],
      context: {},
      resource: {},
      dataItemListFromResource: [],
      scenario: {},
      results: {}
    };
  },

  methods: {
    async init() {
      await this.getProjectInfo();
      await this.getPerformance();
      await this.getContext();
      await this.getDataAsOperator();
      // await this.judgeRole(this.projectInfo);
    },

    async getProjectInfo() {
      let data = await getProjectAndUsers(this.projectId);
      console.log("ProjectInfo", data);
      this.projectInfo = data.project;
      this.creator = data.creator;
      this.members = data.members;
    },

    async getPerformance() {
      let data = await getPerformanceByProjectId(this.projectId);
      console.log("Performance", data)
      this.completion = data.completion;
    },
    async getContext() {
      // console.log(this.projectId);
      if (this.projectId == null) {
        this.$message({
          message: 'Get information error!',
          type: 'error'
        });
        return;
      }
      let data = await getContextByProjectId(this.projectId);
      console.log('context', data);
      this.context = data;
    },

    async getDataAsOperator() {
      let data = await getFileItemByCreatorId(this.projectId);
      console.log("DataAsOperator", data)
      this.dataItemListFromResource = data;
      this.$refs.multipleTable.toggleAllSelection();
      // console.log('DATA', data);
    },

    dateFormat(time) {
      if (time == null) {
        return 'You have not do any operation';
      }
      return dateFormat(time);
    },
    handleSelectionChange() {}
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
  .row-style {
    padding: 0 10px;
    height: 100%;
    width: 50%;
    // position: relative;
  }

  .scenario {
    width: 100%;
  }
}
</style>
