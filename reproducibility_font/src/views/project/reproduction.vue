<!--reproduction-->
<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <el-row :gutter="20">
          <el-col :span="17">
            <div class="scenario">
              <div ref="scenario"><re-scenario-content /></div>
              <div class="md" v-if="content.context != undefined" ref="abstract">
                <div class="head">
                  <i class="iconfont icon-xiangqing" style="margin-left: 15px; margin-right: 15px"></i>
                  ABSTRACT.md
                </div>
                <el-divider></el-divider>
                <mavon-editor v-html="content.context.essentialInformation.abstractRender" style="box-shadow: none" ></mavon-editor>
              </div>
              <div class="md" v-for="(item, index) in resourceMD" :key="index">
                <div v-if="item.markDownHtml != ''">
                  <div class="head">
                    <i class="iconfont icon-xiangqing" style="margin-left: 15px; margin-right: 15px"></i>
                    {{ item.name + '.md' }}
                  </div>
                  <el-divider></el-divider>
                  <mavon-editor v-html="item.markDownHtml" style="box-shadow: none"></mavon-editor>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="7">
            <div v-if="content.context != undefined">
              <el-divider content-position="left">Name</el-divider>
              <h4>{{ content.context.essentialInformation.name }}</h4>
              <el-tag v-for="(item, index) in content.context.essentialInformation.keyWords" :key="index" style="margin-right: 5px; margin-top: 10px">
                {{ item }}
              </el-tag>
              <el-divider content-position="left">Purpose</el-divider>
              <h4>{{ content.context.essentialInformation.purpose }}</h4>
              <el-divider></el-divider>
              <div @click="toAbstract">
                <i class="iconfont icon-read" style="margin-right: 10px"></i>
                Abstract
              </div>
              <div @click="toScenario" style="margin-top: 10px">
                <i class="el-icon-data-board" style="margin-right: 10px"></i>
                Scenario
              </div>
              <el-divider content-position="left">
                Resource
                <div class="divider-text">{{ resourceCount }}</div>
              </el-divider>
              <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick" @node-collapse="handleNodeCollapse"></el-tree>
              <el-descriptions direction="vertical" :column="3" border v-if="resourceFlag" style="margin-top: 20px">
                <el-descriptions-item label="Name">{{ selectedNode.label }}</el-descriptions-item>
                <el-descriptions-item label="Event Name">{{ selectedNode.eventName }}</el-descriptions-item>
                <el-descriptions-item label="Type">
                  <el-tag size="small" :type="getFormat">{{ selectedNode.format }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="Data Type" v-if="selectedNode.format == 'parameter' || selectedNode.format == 'output'">
                  <el-tag size="small" :type="getType" effect="dark">{{ selectedNode.type }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="Description">
                  {{ selectedNode.description }}
                </el-descriptions-item>
              </el-descriptions>
              <el-divider></el-divider>
            </div>
          </el-col>
        </el-row>
        <el-divider></el-divider>
      </div>
    </el-col>
  </div>
</template>

<script>
import { getContent } from '@/api/request';

import ReScenarioContent from '_com/Scenario/reScenario';

export default {
  components: {
    ReScenarioContent,
  },
  data() {
    return {
      projectId: this.$route.params.id,
      content: {},
      resourceCount: 0,
      data: [],
      resourceFlag: false,
      defaultProps: {
        children: 'children',
        label: 'label',
      },
      selectedNode: {},
      resourceMD: [],
    };
  },
  computed: {
    getFormat() {
      if (this.selectedNode.format == 'input') {
        return '';
      } else if (this.selectedNode.format == 'parameter') {
        return 'info';
      } else if (this.selectedNode.format == 'output') {
        return 'success';
      } else if (this.selectedNode.format == 'model') {
        return 'danger';
      } else {
        return 'warning';
      }
    },
    getType() {
      if (this.selectedNode.type == 'INT') {
        return '';
      } else if (this.selectedNode.type == 'STRING') {
        return 'success';
      } else {
        return 'warning';
      }
    },
  },

  methods: {
    click() {
      console.log(this.$refs.md);
    },
    handleNodeClick(data) {
      if (data.flag) {
        this.resourceFlag = true;
        this.selectedNode = data;
      } else {
        this.resourceFlag = false;
      }
    },
    handleNodeCollapse() {
      this.resourceFlag = false;
    },

    async getContent() {
      this.content = await getContent(this.projectId);
      if (this.content.resource != undefined) {
        if (this.content.resource.inputs != undefined && this.content.resource.inputs.length != 0) {
          this.resourceCount = this.resourceCount + this.content.resource.inputs.length;
          this.data.push({
            label: 'inputs',
            children: [],
          });
          this.content.resource.inputs.forEach((item) => {
            this.data[this.data.length - 1].children.push({
              label: item.name,
              eventName: item.eventName,
              description: item.description,
              format: 'input',
              flag: true,
            });
            this.resourceMD.push({
              name: item.name,
              markDownHtml: item.markDownHtml
            });
          });
        }
        if (this.content.resource.parameters != undefined) {
          this.resourceCount = this.resourceCount + this.content.resource.parameters.length;
          this.data.push({
            label: 'parameters',
            children: [],
          });
          this.content.resource.parameters.forEach((item) => {
            this.data[this.data.length - 1].children.push({
              label: item.name,
              eventName: item.eventName,
              description: item.description,
              format: 'parameter',
              type: item.type,
              flag: true,
            });
            this.resourceMD.push({
              name: item.name,
              markDownHtml: item.markDownHtml
            });
          });
        }
        if (this.content.resource.outputs != undefined) {
          this.resourceCount = this.resourceCount + this.content.resource.outputs.length;
          this.data.push({
            label: 'output',
            children: [],
          });
          this.content.resource.outputs.forEach((item) => {
            this.data[this.data.length - 1].children.push({
              label: item.name,
              eventName: item.eventName,
              description: item.description,
              format: 'output',
              type: item.type,
              flag: true,
            });
            this.resourceMD.push({
              name: item.name,
              markDownHtml: item.markDownHtml
            });
          });
        }
      }
    },

    toAbstract() {
      this.$refs.abstract.scrollIntoView();
    },
    toScenario() {
      this.$refs.scenario.scrollIntoView();
    },
    async init() {
      await this.getContent();
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
  .row-style {
    padding: 0 10px;
    height: 100%;
    width: 50%;
  }

  .scenario {
    width: 100%;

    .md {
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      border-radius: 4px;
      margin-top: 20px;
      .head {
        height: 45px;
        line-height: 45px;
        font-weight: bolder;
      }
      .el-divider {
        margin-top: 0px;
      }
      .markdown-body {
        padding: 30px 30px;
      }
    }
  }
  .el-divider__text {
    display: flex;
    .divider-text {
      border-radius: 50%;
      background-color: #343942;
      // width: 15px;
      height: 15px;
      min-width: 15px;
      text-align: center;
      margin-top: 3px;
      margin-left: 3px;
      line-height: 15px;
      color: white;
      font-size: 8px;
    }
  }
}
</style>
