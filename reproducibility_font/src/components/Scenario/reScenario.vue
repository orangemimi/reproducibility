<template>
  <div class="main">
    <div class="mainContainer">
      <div class="modelbarTop">
        <el-button @click="runGraph" size="mini">Run Task</el-button>
        <div class="annotate model"></div>
        model
        <div class="annotate dataService"></div>
        dataService
        <div class="annotate input"></div>
        input
        <div class="annotate parameter"></div>
        parameter
        <div class="annotate output"></div>
        output
      </div>
      <vue-scroll style="height: 850px; width: calc(100%)" :ops="ops">
        <div class="graphContainer" ref="container" @contextmenu.prevent></div>
      </vue-scroll>
    </div>

    <div class="rightBar">
      <div class="barBottom">
        <el-empty description="No Data" v-if="allMyTaskList.length == 0"></el-empty>
        <div class="instances" v-else>
          <div v-for="(item, index) in allMyTaskList" :key="index" class="card">
            <instance-card
              :instanceItem="item"
              :taskItem="currentTask"
              :role="'reproductioner-builder'"
              @check="check"
              @authority="authority($event, index)"
            ></instance-card>
          </div>
        </div>
        <div class="page">
          <el-pagination @current-change="handleCurrentChange" :page-size="7" background layout="prev, pager, next" :total="total" small></el-pagination>
        </div>
      </div>
    </div>

    <el-dialog title="Detail" :visible.sync="outputDialogVisible" width="50%" :modal="false">
      <el-tabs>
        <el-tab-pane>
          <template #label>
            <i class="el-icon-s-order"></i>
            Data
          </template>
          <el-row class="dataInfo">
            <div class="data">
              <div class="dataTitle">name:</div>
              <div class="dataDetail">{{ clickedCell.name }}</div>
            </div>
            <div class="data">
              <div class="dataTitle">description:</div>
              <div class="dataDetail">{{ clickedCell.description }}</div>
            </div>

            <div class="data" v-if="clickedCell.value != '' && clickedCell.upload == 1">
              <div class="dataTitle">value:</div>
              <div class="dataDetail"><el-button type="primary" @click="download(clickedCell.value)">DownLoad</el-button></div>
            </div>
            <div class="data" v-if="clickedCell.value != '' && clickedCell.upload == 0">
              <div class="dataTitle">value:</div>
              <div class="dataDetail">{{ clickedCell.value }}</div>
            </div>
            <div class="data" v-if="clickedCell.value == ''">
              <div class="dataTitle">value:</div>
              <div class="dataDetail">Please wait for the calculation result!</div>
            </div>
          </el-row>
        </el-tab-pane>
        <el-tab-pane>
          <template #label>
            <i class="el-icon-s-platform"></i>
            View
          </template>
          <el-empty description="This data does not support visualization temporarily"></el-empty>
        </el-tab-pane>
      </el-tabs>

      <el-divider></el-divider>
    </el-dialog>
    <el-dialog title="Detail" :visible.sync="inputDialogVisible" width="50%" :modal="false">
      <el-tabs>
        <el-tab-pane>
          <template #label>
            <i class="el-icon-s-order"></i>
            Data
          </template>
          <el-row class="dataInfo">
            <div class="data">
              <div class="dataTitle">Name:</div>
              <div class="dataDetail">{{ clickedCell.name }}</div>
            </div>
            <div class="data">
              <div class="dataTitle">Description:</div>
              <div class="dataDetail">{{ clickedCell.description }}</div>
            </div>
            <div class="data">
              <div class="dataTitle">Type:</div>
              <div class="dataDetail" v-if="clickedCell.isParameter">Parameter</div>
              <div class="dataDetail" v-else>File</div>
            </div>
          </el-row>
        </el-tab-pane>
        <el-tab-pane>
          <template #label>
            <i class="el-icon-s-platform"></i>
            View
          </template>
          <el-empty description="This data does not support visualization temporarily"></el-empty>
        </el-tab-pane>
      </el-tabs>

      <el-divider></el-divider>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import mxgraph from '_com/MxGraph/index';
import { genGraph } from '_com/MxGraph/initMx';
import { saveIntegrateTaskInstance, runtask, getSelectedTaskByProjectId, getSelectedInstancesByProjectId, getAllInstances } from '@/api/request';
import {
  // generateAction,
  generateXml1,
  differCellStyle,
  getCellStyle
  // getCellStyle
} from './components/configuration';
import instanceCard from '_com/Cards/InstanceCard';

const { mxEvent, mxGraphHandler, mxConstants } = mxgraph;

export default {
  components: {
    // dataCellInfo,
    instanceCard,
  },
  props: {
    id: {
      type: String,
    },
  },
  watch: {
    id: {
      handler(val, oldval) {
        console.log(oldval);
        console.log(val);
        this.changeStyle(val, oldval)
      },
    },
  },

  computed: {
    ...mapState({
      role: (state) => state.permission.role,
    }),
  },

  data() {
    return {
      projectId: this.$route.params.id,
      total: 0,
      graph: null,
      currentTask: null,
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
          disable: false,
        },
      },

      container: null,

      selectedInstance: {},
      allMyTaskList: [],
      outputDialogVisible: false,
      inputDialogVisible: false,

      clickedCell: '',

      linkEdgeList: '',
      modelListInGraph: '',
      modelOutputInGraph: '',
      modelInputInGraph: '',
      modelLinkInGraph: '',
      dataServiceListInGraph: '',
      dataServiceInputInGraph: '',
      dataServiceOutputListInGraph: '',
      dataServiceLinkInGraph: '',
    };
  },

  methods: {
    async handleCurrentChange(val) {
      let data = await getAllInstances(this.currentTask.id, val - 1, 7);
      this.allMyTaskList = data.content;
    },

    //初始化mxgraph
    async init() {
      this.container = this.$refs.container;
      await this.getSelectedTaskByProjectId();
      await this.getSelectedInstances();

      this.createGraph();
      this.createCell();
      this.listenGraphEvent();
      this.getCells();
      let cells = {
        linkEdgeList: this.linkEdgeList,
        modelListInGraph: this.modelListInGraph,
        modelOutputInGraph: this.modelOutputInGraph,
        modelInputInGraph: this.modelInputInGraph,
        modelLinkInGraph: this.modelLinkInGraph,
        dataServiceListInGraph: this.dataServiceListInGraph,
        dataServiceInputInGraph: this.dataServiceInputInGraph,
        dataServiceOutputListInGraph: this.dataServiceOutputListInGraph,
        dataServiceLinkInGraph: this.dataServiceLinkInGraph,
      };
      this.$emit('getCells', cells);
    },

    changeStyle(id, oldid) {
      if(oldid != '') {
        this.changeOldStyly(oldid)
      }
      if(id != '') {
        this.changeNewStyle(id)
      }
    },

    changeNewStyle(id) {
      let style = {};
      if (this.graph.model.cells[id].type == 'modelServiceInput' || this.graph.model.cells[id].type == 'dataServiceInput') {
        if (this.graph.model.cells[id].nodeAttribute.isParameter == 'true') {
          style = differCellStyle('parameter');
        } else {
          style = differCellStyle(this.graph.model.cells[id].type);
        }
      } else {
        style = differCellStyle(this.graph.model.cells[id].type);
      }
      let styleObj = {
        ...style,
        strokeWidth: '3',
        align: mxConstants.ALIGN_CENTER,
        imageAlign: mxConstants.ALIGN_CENTER,
        imageVerticalAlign: mxConstants.ALIGN_TOP,
        strokeColor: '#15FE16',
      };
      const styleResult = Object.keys(styleObj)
        .map((attr) => `${attr}=${styleObj[attr]}`)
        .join(';');
      this.graph.getModel().setStyle(this.graph.model.cells[id], styleResult);
    },

    changeOldStyly(oldId) {
      let style = {};
      if (this.graph.model.cells[oldId].type == 'modelServiceInput' || this.graph.model.cells[oldId].type == 'dataServiceInput') {
        if (this.graph.model.cells[oldId].nodeAttribute.isParameter == 'true') {
          style = differCellStyle('parameter');
        } else {
          style = differCellStyle(this.graph.model.cells[oldId].type);
        }
      } else {
        style = differCellStyle(this.graph.model.cells[oldId].type);
      }
      let styleResult = getCellStyle(style, this.graph.model.cells[oldId])
      this.graph.getModel().setStyle(this.graph.model.cells[oldId], styleResult);
    },

    async getSelectedTaskByProjectId() {
      this.currentTask = await getSelectedTaskByProjectId(this.projectId);
    },

    // Creates the graph inside the given container
    createGraph() {
      this.graph = genGraph(this.container);
      this.graph.setPanning(true);
    },

    createCell() {
      if(this.selectedInstance != null) {
        this.graph.importGraph(this.selectedInstance.taskContent);
      }
      this.graph.getModel().beginUpdate();
      try {
        this.graph.setCellsResizable(false);
        mxGraphHandler.prototype.setMoveEnabled(false);
      } finally {
        this.graph.getModel().endUpdate();
      }
    },

    async getSelectedInstances() {
      let data1 = await getSelectedInstancesByProjectId(this.projectId);
      let data2 = await getAllInstances(this.currentTask.id, 0, 7);
      this.selectedInstance = data1;
      this.allMyTaskList = data2.content;
      this.total = data2.total;
      console.log(this.allMyTaskList);
      // this.getInstanceList();
    },

    listenGraphEvent() {
      this.graph.addListener(mxEvent.CLICK, (sender, evt) => {
        let cell = evt.getProperty('cell'); // cell may be null
        if (cell != undefined && cell.type != undefined) {
          this.type = cell.type;
          this.clickedCell = cell;
          if (cell.type == 'dataServiceInput' || cell.type == 'modelServiceInput') {
            console.log(cell);
            this.inputDialogVisible = true;
          } else if (cell.type == 'dataServiceOutput' || cell.type == 'modelServiceOutput') {
            console.log(cell);
            this.outputDialogVisible = true;
          } else if (cell.type == 'dataServiceLink' || cell.type == 'modelServiceLink') {
            console.log(cell);
            // this.dialogVisible = true;
          } else if (cell.type == 'dataService' || cell.type == 'modelService') {
            console.log(cell);
            // this.dialogVisible = true;
          }
        }
      });
    },

    getCells() {
      let modelListInGraph = [];
      let modelOutputInGraph = [];
      let modelInputInGraph = [];
      let modelLinkInGraph = [];

      let dataServiceListInGraph = [];
      let dataServiceInputInGraph = [];
      let dataServiceLinkInGraph = [];
      let dataServiceOutputListInGraph = [];

      Object.values(this.graph.getModel().cells).forEach((cell) => {
        if (cell.style != undefined) {
          if (cell.type == 'modelService') {
            modelListInGraph.push(cell);
          } else if (cell.type == 'modelServiceOutput') {
            modelOutputInGraph.push(cell);
          } else if (cell.type == 'modelServiceInput') {
            modelInputInGraph.push(cell);
          } else if (cell.type == 'modelServiceLink') {
            modelLinkInGraph.push(cell);
          } else if (cell.type == 'dataService') {
            dataServiceListInGraph.push(cell);
          } else if (cell.type == 'dataServiceOutput') {
            cell.value = '';
            dataServiceOutputListInGraph.push(cell);
          } else if (cell.type == 'dataServiceInput') {
            dataServiceInputInGraph.push(cell);
          } else if (cell.type == 'dataServiceLink') {
            dataServiceLinkInGraph.push(cell);
          }
        }
      });
      let links = Object.values(this.graph.getModel().cells).filter((cell) => Object.prototype.hasOwnProperty.call(cell, 'edge'));

      this.linkEdgeList = links;
      this.modelListInGraph = modelListInGraph;
      this.modelOutputInGraph = modelOutputInGraph;
      this.modelInputInGraph = modelInputInGraph;
      this.modelLinkInGraph = modelLinkInGraph;

      this.dataServiceListInGraph = dataServiceListInGraph;
      this.dataServiceInputInGraph = dataServiceInputInGraph;
      this.dataServiceOutputListInGraph = dataServiceOutputListInGraph;
      this.dataServiceLinkInGraph = dataServiceLinkInGraph;
    },

    async runGraph() {
      this.getCells();

      //是否有input中有空 无法run
      // if (!this.judgeInputList()) {
      //   return;
      // }

      let xml = generateXml1(
        this.currentTask.taskName,
        this.modelListInGraph,
        this.modelInputInGraph,
        this.modelLinkInGraph,
        this.modelOutputInGraph,
        this.linkEdgeList,
        this.dataServiceListInGraph,
        this.dataServiceInputInGraph,
        this.dataServiceLinkInGraph,
        this.dataServiceOutputListInGraph
      );

      let file = new File([xml], this.currentTask.taskName + '.xml', {
        type: 'text/xml',
      });
      let formData = new FormData();
      formData.append('file', file);

      //get tid from manager server
      let tid = await runtask(formData);

      await this.addTaskInstance(tid);

      //save to  the instance
    },

    async addTaskInstance(tid) {
      let taskInfo = {
        modelActionList: {
          completed: [],
          running: [],
          waiting: [],
          failed: [],
        },
        dataProcessingList: {
          completed: [],
          running: [],
          waiting: [],
          failed: [],
        },
      };
      this.modelListInGraph.forEach((cell) => {
        taskInfo.modelActionList.waiting.push({
          processId: cell.id,
          processName: cell.name,
        });
      });
      this.dataServiceListInGraph.forEach((cell) => {
        taskInfo.dataProcessingList.waiting.push({
          processId: cell.id,
          processName: cell.name,
        });
      });
      // let graphXml = this.graph.getGraphXml();
      let postJson = {
        name: this.currentTask.taskName + '-Instance',
        taskId: this.currentTask.id,
        authority: 'public',
        taskInfo: taskInfo,
        taskContent: this.currentTask.taskContent,
        status: 0,
        tid: tid,
      };
      let data = await saveIntegrateTaskInstance(postJson);
      console.log(data);
      // this.currentTaskInstance = data;
      if (this.allMyTaskList.length == 7) {
        this.allMyTaskList.pop();
        this.total = this.total + 1;
      }
      this.allMyTaskList.splice(0, 0, data);
    },

    authority(val, index) {
      console.log(val);
      console.log(index);
      this.allMyTaskList.forEach((task, index) => {
        if (task.id == val.id) {
          this.allMyTaskList.splice(index, 1, val);
        }
      });
    },
    check(val) {
      this.allMyTaskList.forEach((item, index) => {
        if (val.id == item.id) {
          this.allMyTaskList.splice(index, 1, val);
        }
      });
    },
  },

  mounted() {
    this.init();
  },
};
</script>

<style lang="scss" scoped>
.main {
  width: calc(100%);
  height: 100%;
  display: flex;
  position: relative;

  .mainContainer {
    position: relative;
    // top: 0;
    // left: 300px;
    width: 80%;
    height: 100%;
    .modelbarTop {
      background: rgb(251, 251, 251);
      padding-left: 10px;
      border-radius: 4px;
      margin-bottom: 5px;
      display: flex;
      .annotate {
        height: 10px;
        width: 10px;
        margin-right: 5px;
        margin-left: 10px;
        margin-top: 8px;
      }
      .model {
        background: #07689f;
      }
      .dataService {
        background: #00fff8;
      }
      .input {
        background: #fff0f0;
      }
      .parameter {
        background: #f497e8;
      }
      .output {
        background: #b9e6d3;
      }
    }
    .graphContainer {
      position: relative;
      overflow: hidden;
      // height: 100%;
      // width: 100%;
      // min-width: calc(100%);
      min-height: 850px;
      background: rgb(251, 251, 251) url('./../../assets/images/mxgraph/point.gif') 0 0 repeat;
      border-radius: 4px;
    }
  }

  .rightBar {
    position: relative;
    right: 0;
    width: 20%;
    margin-left: 5px;
    padding: 0 5px;
    box-shadow: 0px 0px 5px rgb(207, 207, 207);
    background-color: rgba(243, 243, 243, 0.9);
    .barBottom {
      margin-top: 10px;
      .instances {
        width: 100%;

        .card {
          box-shadow: 0px 0px 5px 1px rgba(27, 94, 238, 0.2);
        }
      }
      .page {
        position: absolute;
        bottom: 0;
      }
    }
  }

  .outline-wrapper {
    border: 1px solid #dedede;
    background: #fff;
    position: fixed;
    right: 30px;
    bottom: 40px;
    border-radius: 4px;
    z-index: 10;
    > h4 {
      padding: 6px;
      font-size: 12px;
      border-bottom: 1px solid #e6e6e6;
    }
    #graphOutline {
      width: 200px;
    }
  }
  .dialogs {
    /deep/ .el-dialog__body {
      padding: 20px;
    }
  }
  .dataInfo {
    .data {
      font-size: 16px;
      line-height: 30px;

      .dataTitle {
        clear: both;
        font-weight: 600;
        width: 150px;
        float: left;
      }
      .dataDetail {
        float: left;
        // width: 700px;
      }
    }
  }
}
</style>
