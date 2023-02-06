<template>
  <div class="main">
    <el-tabs type="border-card">
      <el-tab-pane>
        <el-dropdown split-button type="text" slot="label">
          <i class="el-icon-document"></i>
          Context
          <el-dropdown-menu slot="dropdown" style="width: 150px">
            <el-tree :data="data" :props="defaultProps" @node-click="handleClick"></el-tree>
          </el-dropdown-menu>
        </el-dropdown>
        <div style="height: 320px">
          <el-carousel type="card" height="300px" :autoplay="false">
            <el-carousel-item>
              <info-card />
            </el-carousel-item>
            <el-carousel-item>
              <context-card :type="'description'" />
            </el-carousel-item>
            <el-carousel-item>
              <context-card :type="'other'" />
            </el-carousel-item>
            <!-- <el-carousel-item v-for="item in 0" :key="item"></el-carousel-item> -->
          </el-carousel>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">
          <i class="iconfont icon-resource-manage"></i>
          Resource
        </span>
        <vue-scroll style="height: 320px" :ops="ops">
          <div class="resource">
            <resource-card :type="'plus'" />
            <!-- :key="'info-'+ index"这种写法原因，这里写了两个for循环，尽管都加上了key值,然而又将key的值都写成index，会导致vue警告 -->
            <resource-card :type="'input'" :resourceItem="item" v-for="(item, index) in resourceInfo.inputs" :key="'info-' + index" />
            <resource-card :type="'parameter'" :resourceItem="item" v-for="(item, index) in resourceInfo.parameters" :key="'info1-' + index" />
            <resource-card :type="'output'" :resourceItem="item" v-for="(item, index) in resourceInfo.outputs" :key="'info2-' + index" />
          </div>
        </vue-scroll>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">
          <i class="iconfont icon-changjingguanli"></i>
          Scenario
        </span>
        <vue-scroll style="height: 320px" :ops="ops">
          <div class="scenario">
            <scenario-card :type="'plus'" />
            <scenario-card :type="'iconfont icon-text'" />
            <scenario-card :type="'iconfont icon-picture'" />
            <scenario-card :type="'iconfont icon-video'" />
            <scenario-card :type="'iconfont icon-biaoge'" />
            <scenario-card :type="'iconfont icon-map'" />
            <scenario-card :type="'iconfont icon-flow-chart'" />
          </div>
        </vue-scroll>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">
          <i class="iconfont icon-result"></i>
          Result analysis
        </span>
        <vue-scroll style="height: 320px" :ops="ops"></vue-scroll>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">
          <i class="iconfont icon-comparison-analysis"></i>
          Comparision analysis
        </span>
        <vue-scroll style="height: 320px" :ops="ops"></vue-scroll>
      </el-tab-pane>
    </el-tabs>

    <el-dialog :visible.sync="description" width="40%" class="descriptionDialog" title="Description">
      <description />
    </el-dialog>

    <el-dialog :visible.sync="other" width="40%" class="otherDialog" title="Other">
      <other />
    </el-dialog>
    <el-dialog :visible.sync="temporalInfo" width="40%" class="temporalInfoDialog" title="TemporalInfo" v-if="temporalInfo">
      <temporal-info />
    </el-dialog>
  </div>
</template>

<script>
import { getContextByProjectId, updateContexByProjectId, saveRecord, updatePerformanceById } from '@/api/request';
import { mapState } from 'vuex';
import { hasProperty } from '@/utils/utils';

import description from './components/description.vue';
import other from './components/other.vue';
import infoCard from './components/Cards/InfoCard.vue';
import contextCard from './components/Cards/ContextCard.vue';
import resourceCard from './components/Resource/ResourceCard.vue';
import scenarioCard from './components/Scenario/ScenarioCard.vue';
import temporalInfo from './components/temporalInfo.vue';
// import spatialInfo from './components/spatialInfo.vue'

import { getAllResource } from '@/api/request';
export default {
  computed: {
    ...mapState({
      userId: state => state.user.userId,
      userName: state => state.user.name,
      role: state => state.permission.role
    })
  },
  components: {
    infoCard,
    description,
    contextCard,
    other,
    resourceCard,
    scenarioCard,
    temporalInfo
  },

  data() {
    return {
      projectId: this.$route.params.id,
      contextForm: {
        themes: [],
        purpose: '',
        objects: '',
        spatialInfo: {},
        temporalInfo: {},
        methods: '',
        discussion: ''
      },

      themeTag: '',
      tagInputVisible: false,

      //spatial-temporal-info
      addSpatialInfodialogVisible: false,
      addTemporalInfodialogVisible: false,
      spatialInfoForm: {
        enable: false,
        spatialReference: {
          general: '',
          wkt: ''
        },
        spatialDimension: '',
        spatialScale: {
          type: '',
          description: ''
        },
        spatialExtentList: [],
        resolutionConstraintList: []
      },

      temporalInfoForm: {
        enable: false,
        temporalScale: {
          type: '',
          description: ''
        },
        temporalReference: {
          value: ''
        },
        temporalExtentList: [],
        stepConstraintList: []
      },

      // updateContext: true
      //mxgraph scrollbar
      ops: {
        bar: {
          onlyShowBarOnScroll: false,
          keepShow: true,
          background: '#c1c1c1',
          opacity: 1,
          hoverStyle: false,
          specifyBorderRadius: false,
          minSize: 0,
          size: '6px',
          disable: false
        },
        scrollPanel: {
          scrollingY: false,
          scrollingX: true
        }
      },

      defaultProps: {
        children: 'children',
        label: 'label'
      },
      data: [
        {
          label: 'Add',
          children: [
            {
              label: 'Description'
            },
            {
              label: 'TemporalInfo'
            },
            {
              label: 'SpatialInfo'
            },
            {
              label: 'Other'
            },
            {
              label: 'Boundary'
            }
          ]
        }
      ],
      description: false,
      temporalInfo: false,
      spatialInfo: false,
      boundary: false,
      other: false,
      resourceInfo: {}
    };
  },

  methods: {
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
      // console.log('context', data);
      this.contextForm = data;
      if (data.spatialInfo != null) {
        this.spatialInfoForm = data.spatialInfo;
      }
      if (data.temporalInfo != null) {
        this.temporalInfoForm = data.temporalInfo;
        // this.temporalInfoForm.temporalExtentList = data.temporalInfo.temporalExtentList[0];
      }
      this.$forceUpdate();
    },
    async submitContext() {
      // this.$refs.contextForm.validate(async valid => {
      //   if (valid) {
      this.contextForm.spatialInfo = this.spatialInfoForm;
      this.contextForm.temporalInfo = this.temporalInfoForm;

      await updateContexByProjectId(this.projectId, this.contextForm);
      await this.saveRecord();
      await this.updatePerformance();
      // }
      // });
      this.$emit('closeForm', false);
    },

    async saveRecord() {
      let record = {};
      record.projectId = this.projectId;
      await saveRecord(record);
    },

    async updatePerformance() {
      let content = { content: 'hahah' };

      await updatePerformanceById('context', this.projectId, content);
    },

    showTagInput() {
      this.tagInputVisible = true;

      this.$nextTick(() => {
        this.$refs.tagInput.$refs.input.focus();
        // console.log(_);
      });
    },
    addTags() {
      let tag = this.themeTag;
      if (!hasProperty(this.contextForm, 'themes') || this.contextForm.themes == null) {
        this.contextForm.themes = [];
      }
      if (tag != '') {
        // this.contextForm.themes = [];
        this.contextForm.themes.push(tag);
      }
      this.tagInputVisible = false;
      this.themeTag = '';
    },
    delTags(tagIndex) {
      this.contextForm.themes.splice(tagIndex, 1);
    },

    //设置表格行的样式
    // eslint-disable-next-line no-unused-vars
    tableRowStyle({ row, rowIndex }) {
      return 'background-color:pink;font-size:15px;';
    },

    //get spatial info back
    getSpatialInfoReturn(value) {
      console.log('spatialInfo', value);
      this.spatialInfoForm = value;
      // this.contextForm.spatialInfo = this.spatialInfoForm;
      this.addSpatialInfodialogVisible = false;
    },
    getTemporalInfoReturn(value) {
      this.temporalInfoForm = value;
      // this.contextForm.temporalInfo = this.temporalInfoForm;
      this.addTemporalInfodialogVisible = false;
    },

    init() {
      this.getAllResource();
    },
    async getAllResource() {
      let data = await getAllResource(this.projectId);
      console.log(data);
      this.resourceInfo = data;
    },
    handleClick(val) {
      if (val.label == 'Description') {
        this.description = true;
        this.temporalInfo = this.spatialInfo = this.boundary = this.other = false;
      } else if (val.label == 'TemporalInfo') {
        this.temporalInfo = true;
        this.description = this.spatialInfo = this.boundary = this.other = false;
      } else if (val.label == 'SpatialInfo') {
        this.spatialInfo = true;
        this.description = this.temporalInfo = this.boundary = this.other = false;
      } else if (val.label == 'Boundary') {
        this.boundary = true;
        this.description = this.temporalInfo = this.spatialInfo = this.other = false;
      } else if (val.label == 'Other') {
        this.other = true;
        this.temporalInfo = this.spatialInfo = this.boundary = this.description = false;
      }
    }
  },

  created() {
    this.getContext();
    this.init();
  }
};
</script>
<style lang="scss" scoped>
.main {
  margin: 0 10px;
  height: 100%;

  .context,
  .resource,
  .scenario {
    height: 100%;
    width: 100%;
    display: flex;
  }
  .el-carousel__item {
    color: #475669;
    font-size: 14px;
    margin: 0;
    border-radius: 4px;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
  }

  .spatioEdit {
    position: relative;
    left: 78%;
    // top: 30px;
  }
  .temporalEdit {
    position: relative;
    left: 75%;
  }
}
</style>
