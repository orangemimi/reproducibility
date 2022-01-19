<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <el-tabs type="border-card" style="min-height: calc(100vh - 275px)">
          <el-tab-pane>
            <span slot="label">
              <i class="el-icon-document"></i>
              Context
            </span>
            
            <step-card :cardInfo="{ btnType: 'Context Definition' }"></step-card>
          </el-tab-pane>
          <el-tab-pane>
            <span slot="label">
              <i class="iconfont icon-resource-manage"></i>
              Resource
            </span>
            <el-row>
              <el-col :span="24">
                <step-card :cardInfo="{ btnType: 'Resource Definition' }"></step-card>
              </el-col>
              <el-col :span="24">
                <step-card :cardInfo="{ btnType: 'Resource Collection' }" style="height: 450px; width: 100%; margin-top: 20px"></step-card>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane>
            <span slot="label">
              <i class="iconfont icon-changjingguanli"></i>
              Scenario
            </span>
            <el-col :span="24">
              <step-card :cardInfo="{ btnType: 'Simulation Scenario' }" style="height: 950px; width: 100%" v-if="forkingProjectId == undefined"></step-card>
              <step-card :cardInfo="{ btnType: 'forkScenario' }" v-else></step-card>
            </el-col>
          </el-tab-pane>
          <el-tab-pane>
            <span slot="label">
              <i class="iconfont icon-result"></i>
              Result analysis
            </span>
            <div style="padding-top: 100px">
              <wang-editor />
            </div>
          </el-tab-pane>
          <el-tab-pane>
            <span slot="label">
              <i class="iconfont icon-comparison-analysis"></i>
              Comparision analysis
            </span>
            定时任务补偿
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="record">
        <div class="block">
          <el-timeline>
            <el-timeline-item
              :icon="completion.context.icon"
              :type="completion.context.type"
              size="large"
              :timestamp="dateFormat(completion.context.updateTime)"
            >
              {{ completion.context.content }}
            </el-timeline-item>

            <el-timeline-item
              :icon="completion.resource.icon"
              :type="completion.resource.type"
              size="large"
              :timestamp="dateFormat(completion.resource.updateTime)"
            >
              {{ completion.resource.content }}
            </el-timeline-item>

            <el-timeline-item
              :icon="completion.scenario.icon"
              :type="completion.scenario.type"
              size="large"
              :timestamp="dateFormat(completion.scenario.updateTime)"
            >
              {{ completion.scenario.content }}
            </el-timeline-item>

            <el-timeline-item
              :icon="completion.results.icon"
              :type="completion.results.type"
              size="large"
              :timestamp="dateFormat(completion.results.updateTime)"
            >
              {{ completion.results.content }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-col>
  </div>
</template>

<script>
import stepCard from '_com/StepCard';
// import tinymceEdit from '_com/TinymceEdit/TinymceEdit.vue';
import wangEditor from '_com/WangEditor/WangEditor.vue';
// import tinymce from '_com/Tinymce/Tinymce.vue'
import { getPerformanceByProjectId } from '@/api/request';
import { dateFormat } from '@/utils/utils';

export default {
  components: {
    stepCard,
    // tinymceEdit,
    wangEditor,
    // tinymce
  },
  data() {
    return {
      projectId: this.$route.params.id,
      forkingProjectId: this.$route.query.forkingProjectId,
      completion: {
        context: { icon: '', type: '', updateTime: '' },
        resource: { icon: '', type: '', updateTime: '' },
        scenario: { icon: '', type: '', updateTime: '' },
        results: { icon: '', type: '', updateTime: '' },
      },
      dialogVisible: false,
    };
  },
  methods: {
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
    init() {
      this.getPerformance();
      console.log(this.$route.query.forkingProjectId)
    },
  },
  mounted() {
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