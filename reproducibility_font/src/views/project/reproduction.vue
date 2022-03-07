<!--reproduction-->
<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <el-row :gutter="20">
          <el-col :span="17">
            <div class="scenario">
              <div ref="scenario"><re-scenario-content @getCells="getCells" :id="selectedNodeId"/></div>
              <div class="md" v-if="content.context != undefined" ref="abstract">
                <div class="head">
                  <i class="iconfont icon-xiangqing" style="margin-left: 15px; margin-right: 15px"></i>
                  ABSTRACT.md
                </div>
                <el-divider></el-divider>
                <mavon-editor v-html="content.context.essentialInformation.abstractRender" style="box-shadow: none"></mavon-editor>
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

              <el-divider content-position="left">
                Resource
                <div class="divider-text">{{ resourceCount }}</div>
              </el-divider>
              <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick" @node-collapse="handleNodeCollapse" highlight-current>
                <span class="custom-tree-node" slot-scope="{ node, data }">
                  <span v-if="data.classify == 'model'" class="tree-node-color">
                    <div class="tree-color models"></div>
                    {{ node.label }}
                  </span>
                  <span v-else-if="data.classify == 'dataService'" class="tree-node-color">
                    <div class="tree-color dataServices"></div>
                    {{ node.label }}
                  </span>
                  <span v-else-if="data.classify == 'input'" class="tree-node-color">
                    <div class="tree-color inputs"></div>
                    {{ node.label }}
                  </span>
                  <span v-else-if="data.classify == 'parameter'" class="tree-node-color">
                    <div class="tree-color parameters"></div>
                    {{ node.label }}
                  </span>
                </span>
              </el-tree>
              <el-descriptions direction="vertical" :column="3" border v-if="resourceFlag" style="margin-top: 20px">
                <el-descriptions-item label="Name">{{ selectedNode.label }}</el-descriptions-item>
                <!-- <el-descriptions-item label="Event Name">{{ selectedNode.eventName }}</el-descriptions-item>
                <el-descriptions-item label="Type">
                  <el-tag size="small" :type="getFormat">{{ selectedNode.format }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="Data Type" v-if="selectedNode.format == 'parameter' || selectedNode.format == 'output'">
                  <el-tag size="small" :type="getType" effect="dark">{{ selectedNode.type }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="Description">
                  {{ selectedNode.description }}
                </el-descriptions-item> -->
                <el-descriptions-item label="Type">
                  <el-tag size="small" :type="getFormat">{{ selectedNode.classify }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="Download" v-if="selectedNode.classify == 'input' && selectedNode.type == 'value'">
                  <el-button type="info" plain size="mini">view</el-button>
                  <el-button type="warning" plain size="mini" @click="download(selectedNode)">download</el-button>
                </el-descriptions-item>
              </el-descriptions>

              <el-divider></el-divider>
              <div @click="toAbstract" class="nav">
                <i class="iconfont icon-read" style="margin-right: 10px"></i>
                Abstract
              </div>
              <div @click="toScenario" style="margin-top: 10px" class="nav">
                <i class="el-icon-data-board" style="margin-right: 10px"></i>
                Scenario
              </div>

              <div v-if="content.context.spatialInfos.length > 0 || content.context.temporalInfo.start != null">
                <el-divider>Scale</el-divider>
                <div v-if="content.context.temporalInfo.start != null">
                  <p style="margin-bottom: 10px">TemporalInfo</p>
                  <el-date-picker v-model="temporalInfo" type="datetimerange" range-separator="-" :readonly="true"></el-date-picker>
                </div>
                <div v-if="content.context.spatialInfos.length > 0" class="">
                  <p style="margin-bottom: 10px; margin-top: 10px">SpatialInfo</p>
                  <el-select v-model="value1" placeholder="请选择" filterable @change="change1" size="mini">
                    <el-option v-for="(item, index) in options1" :key="index" :label="item.name" :value="index"></el-option>
                  </el-select>
                  <el-select v-model="value2" placeholder="请选择" v-if="flag2" @change="change2" size="mini">
                    <el-option v-for="(item, index) in options2" :key="index" :label="item.name" :value="index"></el-option>
                  </el-select>
                  <el-select v-model="value3" placeholder="请选择" v-if="flag3" @change="change3" size="mini">
                    <el-option v-for="(item, index) in options3" :key="index" :label="item.name" :value="index"></el-option>
                  </el-select>
                  <baidu-map class="map" :center="center" :scroll-wheel-zoom="true" @ready="handleMap">
                    <bm-control anchor="BMAP_ANCHOR_TOP_RIGHT">
                      <el-dropdown style="margin-top: 10px; margin-right: 5px" trigger="click" @command="selectCommand">
                        <span class="el-dropdown-link">
                          <el-button type="primary" size="mini">
                            Selected Data
                            <i class="el-icon-arrow-down el-icon--right"></i>
                          </el-button>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item v-for="(item, index) in selectData" :key="index" :command="item">
                            {{ item.name }}
                          </el-dropdown-item>
                        </el-dropdown-menu>
                      </el-dropdown>
                    </bm-control>

                    <bm-map-type :map-types="['BMAP_NORMAL_MAP', 'BMAP_HYBRID_MAP']" anchor="BMAP_ANCHOR_TOP_LEFT"></bm-map-type>
                    <bm-scale anchor="BMAP_ANCHOR_BOTTOM_LEFT"></bm-scale>
                    <bm-boundary :name="item" :strokeWeight="2" strokeColor="black" v-for="(item, index) in selectBoundary" :key="'index1' + index" />
                    <bm-polygon :path="path" v-for="(path, index) in paths" :key="'index2' + index" stroke-color="black" :stroke-weight="2" />
                  </baidu-map>
                </div>
              </div>
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
import axios from 'axios';
import ReScenarioContent from '_com/Scenario/reScenario';

export default {
  components: {
    ReScenarioContent,
  },
  data() {
    return {
      projectId: this.$route.params.id,
      selectedNodeId: '',
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
      map: '',
      BMap: '',
      temporalInfo: [],
      spatialInfos: [],
      center: '',
      selectData: [],
      selectBoundary: [],
      paths: [],

      options1: [],
      options2: [],
      options3: [],
      value1: '',
      value2: '',
      value3: '',
      flag2: false,
      flag3: false,
    };
  },
  computed: {
    getFormat() {
      if (this.selectedNode.classify == 'input') {
        return '';
      } else if (this.selectedNode.classify == 'parameter') {
        return 'info';
      } else if (this.selectedNode.classify == 'dataService') {
        return 'success';
      } else if (this.selectedNode.classify == 'model') {
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
    // test() {
    //   let size = 0;
    //   for (let item in localStorage) {
    //     if (localStorage.getItem(item) != null) {
    //       size += localStorage.getItem(item).length;
    //     }
    //   }
    //   console.log((size / 1024).toFixed(2));
    // },
    download(val) {
      location.href = val.value
    },
    async getCityList() {
      await axios.get('/json/Region.json').then((res) => {
        this.options1 = res.data.districts[0].districts;
      });
    },
    handleNodeClick(data) {
      console.log(data);
      if (data.flag) {
        this.resourceFlag = true;
        this.selectedNode = data;
        this.selectedNodeId = data.id
      } else {
        this.resourceFlag = false;
      }
    },
    handleNodeCollapse() {
      this.resourceFlag = false;
      this.selectedNodeId = ''
    },

    async getContent() {
      this.content = await getContent(this.projectId);
      console.log(this.content);
      this.temporalInfo.push(this.content.context.temporalInfo.start);
      this.temporalInfo.push(this.content.context.temporalInfo.end);
      this.selectData = this.content.context.spatialInfos;
      if (this.selectData.length > 0) {
        this.selectData.forEach((item) => {
          if (item.type == 'polygon') {
            this.paths.push(item.points);
          } else if (item.type == 'boundary') {
            this.selectBoundary.push(item.value);
          }
        });
      }
      // if (this.content.resource != undefined) {
      //   if (this.content.resource.inputs != undefined && this.content.resource.inputs.length != 0) {
      //     this.resourceCount = this.resourceCount + this.content.resource.inputs.length;
      //     this.data.push({
      //       label: 'inputs',
      //       children: [],
      //     });
      //     this.content.resource.inputs.forEach((item) => {
      //       this.data[this.data.length - 1].children.push({
      //         label: item.name,
      //         eventName: item.eventName,
      //         description: item.description,
      //         format: 'input',
      //         flag: true,
      //       });
      //       this.resourceMD.push({
      //         name: item.name,
      //         markDownHtml: item.markDownHtml,
      //       });
      //     });
      //   }
      //   if (this.content.resource.parameters != undefined) {
      //     this.resourceCount = this.resourceCount + this.content.resource.parameters.length;
      //     this.data.push({
      //       label: 'parameters',
      //       children: [],
      //     });
      //     this.content.resource.parameters.forEach((item) => {
      //       this.data[this.data.length - 1].children.push({
      //         label: item.name,
      //         eventName: item.eventName,
      //         description: item.description,
      //         format: 'parameter',
      //         type: item.type,
      //         flag: true,
      //       });
      //       this.resourceMD.push({
      //         name: item.name,
      //         markDownHtml: item.markDownHtml,
      //       });
      //     });
      //   }
      //   if (this.content.resource.outputs != undefined) {
      //     this.resourceCount = this.resourceCount + this.content.resource.outputs.length;
      //     this.data.push({
      //       label: 'output',
      //       children: [],
      //     });
      //     this.content.resource.outputs.forEach((item) => {
      //       this.data[this.data.length - 1].children.push({
      //         label: item.name,
      //         eventName: item.eventName,
      //         description: item.description,
      //         format: 'output',
      //         type: item.type,
      //         flag: true,
      //       });
      //       this.resourceMD.push({
      //         name: item.name,
      //         markDownHtml: item.markDownHtml,
      //       });
      //     });
      //   }
      // }
    },

    getCells(val) {
      console.log(val);
      this.data.push({
        label: 'models',
        children: [],
        classify: 'model',
      });
      this.data.push({
        label: 'dataServices',
        children: [],
        classify: 'dataService',
      });
      this.data.push({
        label: 'inputs',
        children: [],
        classify: 'input',
      });
      this.data.push({
        label: 'parameters',
        children: [],
        classify: 'parameter',
      });
      val.modelListInGraph.forEach((item) => {
        this.data[0].children.push({
          label: item.name,
          classify: 'model',
          flag: true,
          id: item.id
        });
        this.resourceCount = this.resourceCount + 1;
      });
      val.dataServiceListInGraph.forEach((item) => {
        this.data[1].children.push({
          label: item.name,
          classify: 'dataService',
          flag: true,
          id: item.id
        });
        this.resourceCount = this.resourceCount + 1;
      });
      val.modelInputInGraph.forEach((item) => {
        if (item.nodeAttribute.isParameter == 'true') {
          this.data[3].children.push({
            label: item.nodeAttribute.dataSelect.name,
            value: item.nodeAttribute.dataSelect.value,
            type: 'value',
            classify: 'parameter',
            flag: true,
            id: item.id
          });
          this.resourceCount = this.resourceCount + 1;
        } else {
          this.data[2].children.push({
            label: item.nodeAttribute.dataSelect.name,
            value: item.nodeAttribute.dataSelect.value,
            type: 'value',
            classify: 'input',
            flag: true,
            id: item.id
          });
          this.resourceCount = this.resourceCount + 1;
        }
      });
      val.dataServiceInputInGraph.forEach((item) => {
        if (item.nodeAttribute.isParameter == 'true') {
          this.data[3].children.push({
            label: item.nodeAttribute.dataSelect.name,
            value: item.nodeAttribute.dataSelect.value,
            type: 'value',
            classify: 'parameter',
            flag: true,
            id: item.id
          });
          this.resourceCount = this.resourceCount + 1;
        } else {
          this.data[2].children.push({
            label: item.nodeAttribute.dataSelect.name,
            value: item.nodeAttribute.dataSelect.value,
            type: 'value',
            classify: 'input',
            flag: true,
            id: item.id
          });
          this.resourceCount = this.resourceCount + 1;
        }
      });
      val.modelLinkInGraph.forEach((item) => {
        if (item.nodeAttribute.isParameter == 'true') {
          item.edges.forEach((edge) => {
            if (edge.target.id == item.id) {
              this.data[3].children.push({
                label: 'Intermediate result',
                type: 'link',
                from: edge.target,
                classify: 'parameter',
                flag: true,
                id: item.id
              });
            }
          });
          this.resourceCount = this.resourceCount + 1;
        } else {
          item.edges.forEach((edge) => {
            if (edge.target.id == item.id) {
              this.data[2].children.push({
                label: 'Intermediate result',
                type: 'link',
                from: edge.target,
                classify: 'input',
                flag: true,
                id: item.id
              });
            }
          });
          this.resourceCount = this.resourceCount + 1;
        }
      });
      val.dataServiceLinkInGraph.forEach((item) => {
        if (item.nodeAttribute.isParameter == 'true') {
          item.edges.forEach((edge) => {
            if (edge.target.id == item.id) {
              this.data[3].children.push({
                label: 'Intermediate result',
                type: 'link',
                from: edge.target,
                classify: 'parameter',
                flag: true,
                id: item.id
              });
            }
          });
          this.resourceCount = this.resourceCount + 1;
        } else {
          item.edges.forEach((edge) => {
            if (edge.target.id == item.id) {
              this.data[2].children.push({
                label: 'Intermediate result',
                type: 'link',
                from: edge.target,
                classify: 'input',
                flag: true,
                id: item.id
              });
            }
          });
          this.resourceCount = this.resourceCount + 1;
        }
      });
    },

    handleMap({ BMap, map }) {
      this.BMap = BMap;
      this.map = map;
      if (this.selectData.length > 0) {
        if (this.selectData[0].type == 'polygon') {
          this.map.centerAndZoom(new this.BMap.Point(this.selectData[0].points[0].lng, this.selectData[0].points[0].lat), 11);
        } else if (this.selectData[0].type == 'boundary') {
          this.map.centerAndZoom(this.selectData[0].value);
        }
      }
    },

    selectCommand(val) {
      if (val.type == 'boundary') {
        this.map.centerAndZoom(val.value);
      } else if (val.type == 'polygon') {
        this.map.panTo(new this.BMap.Point(val.points[0].lng, val.points[0].lat));
      }
    },
    change1(val) {
      if (
        this.options1[val].name == '北京市' ||
        this.options1[val].name == '天津市' ||
        this.options1[val].name == '上海市' ||
        this.options1[val].name == '重庆市'
      ) {
        this.options2 = this.options1[val].districts[0].districts;
      } else {
        this.options2 = this.options1[val].districts;
      }
      this.flag3 = false;
      this.value2 = '';
      if (this.options2.length > 0) {
        this.flag2 = true;
      } else {
        this.flag2 = false;
      }
      this.center = this.options1[val].name;
    },
    change2(val) {
      this.options3 = this.options2[val].districts;
      this.value3 = '';
      if (this.options3.length > 0) {
        this.flag3 = true;
      } else {
        this.flag3 = false;
      }
      this.center = this.options1[this.value1].name + this.options2[val].name;
    },
    change3(val) {
      this.center = this.options1[this.value1].name + this.options2[this.value2].name + this.options3[val].name;
    },

    toAbstract() {
      this.$refs.abstract.scrollIntoView();
    },
    toScenario() {
      this.$refs.scenario.scrollIntoView();
    },
    async init() {
      await this.getContent();
      await this.getCityList();
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
    .custom-tree-node {
      .tree-node-color {
        display: flex;
        .tree-color {
          height: 10px;
          width: 10px;
          margin-top: 8px;
          margin-right: 5px;
        }
        .models {
          background: #07689f;
        }
        .dataServices {
          background: #00fff8;
        }
        .inputs {
          background: #fff0f0;
        }
        .parameters {
          background: #f497e8;
        }
      }
    }
    .nav:hover {
      cursor: pointer;
      color: #27A5C4;
    }
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
        &:hover {
          cursor: pointer;
        }
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
.map {
  width: 100%;
  height: 400px;
  margin-top: 10px;
}
.el-select {
  width: 20%;
  margin-right: 7px;
}
</style>
