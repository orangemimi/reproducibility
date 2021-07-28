<template>
  <div class="main">
    <div class="mainContainer">
      <div class="modelbarTop">
        <el-button @click="runGraph" size="mini">Run Task</el-button>
      </div>
      <vue-scroll style="height: 800px; width: calc(100%)" :ops="ops">
        <div class="graphContainer" ref="container"></div>
      </vue-scroll>
    </div>

    <div class="rightBar">
      <div class="instances">
        <el-row class="add_ins">
          <el-card shadow="hover" class="box-card">
            <i class="el-icon-plus" />
          </el-card>
        </el-row>
        <el-row class="instance">
          <el-card shadow="hover" class="box-card">
            <!-- <i class="el-icon-plus" /> -->
            producible instance from builder

            <div class="task_view" @click="showInstanceStatus">
              View
            </div>
          </el-card>
        </el-row>
        <el-row>
          <div v-for="(item, index) in instanceList" :key="index">
            <instance-card :instanceItem="item" :taskItem="currentTask" @showInstanceStatus="showInstanceStatus"></instance-card>
          </div>
        </el-row>
      </div>
      <div class="page">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page.sync="instancePageFilter.page"
          :page-size="instancePageFilter.pageSize"
          background
          layout="prev, pager, next"
          :total="instanceList.length + 1"
        ></el-pagination>
      </div>
    </div>

    <div class="dialogs">
      <el-dialog :visible.sync="dataDoubleClick" width="50%" title="Configuration">
        <span>
          <!-- Configuration -->
          <data-cell-info :cell="dataNode"></data-cell-info>
        </span>
      </el-dialog>
    </div>
  </div>
  <!-- </div> -->
</template>

<script>
import { mapState } from 'vuex';
import mxgraph from '_com/MxGraph/index';
import { genGraph } from '_com/MxGraph/initMx';
import {
  saveFileItem,
  saveIntegrateTaskInstance,
  updateIntegrateTaskInstanceById,
  getScenarioByProjectId,
  runtask,
  checkTaskStatus,
  getIntegrateTaskByTaskId,
  getAllIntegrateTaskInstancesByTaskId
} from '@/api/request';
import { generateAction, generateXml, getCellStyle } from './components/configuration';
import { initSetTimeOut, hasProperty } from '@/utils/utils';

import dataCellInfo from '_com/MxGraphDialogs/DataCellInfo';

import instanceCard from '_com/Cards/InstanceCard';

const { mxEvent } = mxgraph;

export default {
  components: {
    dataCellInfo,
    instanceCard
  },

  computed: {
    ...mapState({
      role: state => state.permission.role
    })
  },

  data() {
    return {
      projectId: this.$route.params.id,

      graph: null,
      editCellVisible: false,

      selectionCells: [],
      deleteCellsVisible: false,
      checked: false,
      undoMng: null,

      cellForm: {
        name: ''
      },

      getXml: this.sendXml,
      //modelbar
      modelItemList: [],

      doi: '',
      cell: {}, //双击事件 cell
      state: {},
      inputItemList: [],
      outputItemList: [],

      dataNode: {},

      modelClick: false,
      modelDoubleClick: false,
      dataClick: false,
      dataDoubleClick: false,

      modelListInGraph: [],
      modelInputInGraph: [],
      modelLinkInGraph: [], //下一模型的输入数据
      modelOutputInGraph: [],
      linkEdgeList: [],

      stateList: [],
      // dataItemModelbarKey: 0,

      taskInfo: {
        taskName: '',
        taskDescription: ''
      },

      isNewTaskContainerShow: true,

      currentTask: {},

      //document
      nodeList: [],
      dataItemList: [],

      //task
      taskList: [],

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
        }
      },

      record: {}, //task -->get output record
      timer: {},
      container: null,

      //select task to show
      isSelectTaskInConsruction: true,

      //dom
      domFlag: 0,

      //currentInstance;
      currentTaskInstance: {},

      //INSTANCE LIST
      instancePageFilter: {
        pageSize: 8,
        page: 0
      },
      instanceList: []
    };
  },

  methods: {
    runInstanes() {},

    async getScenario() {
      let data = await getScenarioByProjectId(this.projectId);
      this.currentTask = await getIntegrateTaskByTaskId(data.selectTaskId);
      this.graph.importGraph(this.currentTask.mxgraph);

      await this.getAllIntegrateTaskInstances(0);
    },

    //instance list
    async getAllIntegrateTaskInstances(page) {
      let data = await getAllIntegrateTaskInstancesByTaskId(this.currentTask.id, page, this.instancePageFilter.pageSize);
      if (data == null) {
        this.instanceList = [];
        return;
      }
      this.instanceList = data.content;
      this.instancePageFilter.page++;
      // console.log('instances', data);
    },
    async handleCurrentChange(val) {
      await this.getAllIntegrateTaskInstances(val++);
    },

    //初始化mxgraph
    async init() {
      this.container = this.$refs.container;
      this.createGraph();
      this.listenGraphEvent();
      this.getScenario();
    },

    // Creates the graph inside the given container
    createGraph() {
      this.graph = genGraph(this.container);
    },

    listenGraphEvent() {
      // 监听双击事件
      this.graph.addListener(mxEvent.DOUBLE_CLICK, async (graph, evt) => {
        // DOUBLE_CLICK
        let cell = evt.properties.cell;
        let clickModelType = cell.type;
        if (clickModelType == 'model') {
          this.modelDoubleClick = true;
          this.domFlag++;
          await initSetTimeOut();
          this.cell = cell;
          this.dataDoubleClick = this.dataClick = this.modelClick = false;
        } else {
          this.modelDoubleClick = this.modelClick = this.dataClick = false;
          this.dataDoubleClick = true;
          // console.log(cell);
          this.dataNode = cell;
        }
      });
    },

    getCells() {
      let modelListInGraph = [];
      let modelOutputInGraph = [];
      let modelInputInGraph = [];
      let modelLinkInGraph = [];

      Object.values(this.graph.getModel().cells).forEach(cell => {
        if (cell.style != undefined) {
          if (cell.type == 'model') {
            modelListInGraph.push(cell);
          } else if (cell.type == 'output') {
            modelOutputInGraph.push(cell);
          } else if (cell.type == 'input') {
            modelInputInGraph.push(cell);
          } else if (cell.type == 'link') {
            modelLinkInGraph.push(cell);
          }
        }
      });
      let links = Object.values(this.graph.getModel().cells).filter(cell => Object.prototype.hasOwnProperty.call(cell, 'edge'));
      this.linkEdgeList = links;
      this.modelListInGraph = modelListInGraph;
      this.modelOutputInGraph = modelOutputInGraph;
      this.modelInputInGraph = modelInputInGraph;
      this.modelLinkInGraph = modelLinkInGraph;
    },

    judgeInputList() {
      let modelInputInGraph = this.modelInputInGraph;
      modelInputInGraph.forEach(input => {
        if (!hasProperty(input, 'value')) {
          this.$notify.error({
            title: 'Error',
            message: 'You have not bind the data'
          });
          return false;
        }
      });
      return true;
    },

    async showInstanceStatus(value) {
      this.currentTaskInstance = value;
      await this.getOutputs(value.tid);
      // }
    },

    getInstanceAction(action) {
      //
      //
      // 考虑dataitemcollection 改变
      let outputList = this.modelOutputInGraph;
      let dataItem = action.dataItemList;
      outputList.forEach(out => {
        let dataOutputValue = dataItem.filter(item => item.id == out.fileId);
        if (dataOutputValue.length != 0) {
          out.value = dataOutputValue.value;
        }
      });
    },

    async runGraph() {
      this.getCells();

      //是否有input中有空 无法run
      if (!this.judgeInputList()) {
        return;
      }

      let xml = generateXml(
        this.currentTask.taskName,
        this.modelListInGraph,
        this.modelInputInGraph,
        this.modelLinkInGraph,
        this.modelOutputInGraph,
        this.linkEdgeList
      );
      let file = new File([xml], this.currentTask.taskName + '.xml', {
        type: 'text/xml'
      });
      let formData = new FormData();
      formData.append('file', file);

      //get tid from manager server
      let tid = await runtask(formData);

      await this.addTaskInstance(tid);

      await this.getOutputs(tid);

      //save to  the instance
    },

    async getOutputs(tid) {
      //获得结果

      this.record = {};
      this.timer = setInterval(async () => {
        if (this.record.status == 1) {
          clearInterval(this.timer);
          await this.putOutputToCell();

          return;
        } else {
          let data = await checkTaskStatus(tid);
          this.record = data;
          this.changeCellColor(data);
        }
      }, 3000);
    },

    changeCellColor(data) {
      let modelActions = data.taskInfo.modelActionList;
      let runningTask = modelActions.running; //yellow
      let failedTask = modelActions.failed; //red
      let completedTask = modelActions.completed;

      this.getCells();
      let modelListInGraph = this.modelListInGraph;

      modelListInGraph.forEach(modelCell => {
        //判断runnign里面是否有model
        if (runningTask.some(task => task.id == modelCell.id)) {
          //change color
          this.changeCellStyleByStatus(0, modelCell, false);
          this.changeDataCellByStatus(0, modelCell, false);
        }
        //判断completedTask里面是否有model
        if (completedTask.some(task => task.id == modelCell.id)) {
          //change color

          this.changeCellStyleByStatus(1, modelCell, false);

          this.changeDataCellByStatus(1, modelCell, false);
        }
        //判断failedTask里面是否有model
        if (failedTask.some(task => task.id == modelCell.id)) {
          //change color
          this.changeCellStyleByStatus(2, modelCell, false);

          this.changeDataCellByStatus(2, modelCell, false);
        }
      });
    },

    changeDataCellByStatus(status, modelCell) {
      let list = [...this.modelInputInGraph, ...this.modelLinkInGraph];
      let inputList = list.filter(event => event.md5 == modelCell.md5);
      let outputList = this.modelOutputInGraph.filter(event => event.md5 == modelCell.md5);
      inputList.forEach(item => {
        this.changeCellStyleByStatus(status, item, true);
      });
      outputList.forEach(item => {
        this.changeCellStyleByStatus(status, item, true);
      });
    },

    changeCellStyleByStatus(status, item, isData) {
      //runnning
      let style;
      if (status == 0) {
        style = {
          fontColor: '#f6f6f6',
          fillColor: '#E6A23C'
        };
      }
      //finish
      if (status == 1) {
        style = {
          fillColor: '#67C23A',
          fontColor: '#24292E'
        };
      } //error
      if (status == 2) {
        style = {
          fillColor: '#ce1212',
          fontColor: '#f6f6f6'
        };
      }
      if (isData) {
        style.shape = 'parallelogram';
        style.fixedSize = 1;
      }
      let styleIn = getCellStyle(style, item);
      this.graph.getModel().setStyle(item, styleIn);
    },

    async putOutputToCell() {
      let actionResponse = this.record.taskInfo.modelActionList.completed;
      // console.log(this.modelOutputInGraph);
      this.modelOutputInGraph.forEach(async eventCell => {
        let outputActionList = actionResponse.filter(response => eventCell.linkModelCellId == response.id);
        let outputValueJson = outputActionList[0].outputData.outputs.filter(out => out.dataId == eventCell.eventId && out.state == eventCell.stateName);
        let content = outputValueJson[0].dataContent;

        //upload
        let dataJson = {
          name: content.fileName,
          address: content.value,
          suffix: content.suffix,
          description: '',
          userUpload: false //--false是中间数据
        };

        let data = await saveFileItem(dataJson);
        eventCell.fileId = data.id;
        eventCell.value = data.address;
        eventCell.fileName = data.name;
      });

      await this.updateTaskInstances();
    },

    async addTaskInstance(tid) {
      let postJson = {
        name: this.currentTask.taskName + '-Instance',
        taskId: this.currentTask.id,
        action: generateAction(
          this.currentTask.id,
          this.modelListInGraph,
          this.modelInputInGraph,
          this.modelLinkInGraph,
          this.modelOutputInGraph,
          this.linkEdgeList,
          'taskInstance'
        ),
        status: 0,
        tid: tid
      };
      let data = await saveIntegrateTaskInstance(postJson);
      this.instanceList.push(data);
    },

    async updateTaskInstances() {
      let postJson = {
        action: generateAction(
          this.currentTask.id,
          this.modelListInGraph,
          this.modelInputInGraph,
          this.modelLinkInGraph,
          this.modelOutputInGraph,
          this.linkEdgeList,
          'taskInstance'
        ),
        status: 1
      };
      // console.log(postJson, this.currentTaskInstance.id);
      await updateIntegrateTaskInstanceById(this.currentTaskInstance.id, postJson);
    }
  },

  async mounted() {
    await initSetTimeOut();
    // debugger;

    this.init();
  }
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
  height: 100%;
  display: flex;
  position: relative;

  .mainContainer {
    position: relative;
    // top: 0;
    // left: 300px;
    width: calc(100%);
    height: 100%;
    .modelbarTop {
      background: rgb(251, 251, 251);
      padding-left: 10px;
      border-radius: 4px;
      margin-bottom: 5px;

      .fade-enter-active,
      .fade-leave-active {
        transition: opacity 0.3s ease;
      }
      .fade-enter, .fade-leave-to
/* .component-fade-leave-active for below version 2.1.8 */ {
        opacity: 0;
      }

      .bell {
        float: left;
        color: rgb(224, 75, 75);
        margin: 0 8px;
        font-size: 20px;
      }

      .bell:hover {
        cursor: pointer;
        transition: all 0.2s ease-in-out;
      }
    }
    .graphContainer {
      position: relative;
      overflow: hidden;
      height: 100%;
      width: 100%;
      min-width: calc(100%);
      min-height: 800px;
      background: rgb(251, 251, 251) url('./../../assets/images/mxgraph/point.gif') 0 0 repeat;
      border-radius: 4px;
    }
  }

  .rightBar {
    position: relative;
    right: 0;
    width: 200px;
    margin-left: 5px;
    padding: 0 5px;
    box-shadow: 0px 0px 5px rgb(207, 207, 207);
    background-color: rgba(243, 243, 243, 0.9);
    .instances {
      width: 100%;
      .add_ins {
        font-size: 30px;
        margin-bottom: 10px;
        text-align: center;
        /deep/.el-card {
          background-color: rgba($color: #ffffff, $alpha: 0.8);
        }
      }
      .add_ins:hover {
        cursor: pointer;
      }
      .instance {
        margin-bottom: 10px;
        text-align: center;
        /deep/.el-card {
          background-color: rgba($color: #b87171, $alpha: 0.8);
        }
      }
    }
    .page {
      position: absolute;
      bottom: 0;
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
}
</style>
