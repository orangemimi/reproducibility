<template>
  <div class="main">
    <div class="mainContainer">
      <div class="modelbarTop">
        <el-button @click="runGraph" size="mini">Run Task</el-button>
        
      </div>
      <vue-scroll style="height: 850px; width: calc(100%)" :ops="ops">
        <div class="graphContainer" ref="container" @contextmenu.prevent></div>
      </vue-scroll>
    </div>

    <div class="rightBar">
      <el-switch
        style="display: block"
        v-model="switchValue"
        inactive-color="#13ce66"
        active-color="#ff4949"
        active-text="My Tasks"
        inactive-text="Tasks"
      ></el-switch>
      <div class="barBottom">
        <div v-if="!switchValue">
          <el-empty description="No Data" v-if="selectedInstances.length == 0"></el-empty>
          <div class="instances">
            <el-row v-if="instanceList.length != 0 && currentTask != null">
              <div v-for="(item, index) in instanceList" :key="index" class="card" @click="clickCard(index)">
                <instance-card :instanceItem="item" :taskItem="currentTask" :role="'reproductioner'" @showInstanceStatus="showInstanceStatus"></instance-card>
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
              :total="selectedInstances.length"
            ></el-pagination>
          </div>
        </div>
        <div v-else-if="switchValue">
          <el-empty description="No Data" v-if="allMyTaskList.length == 0"></el-empty>
          <div class="instances">
            <el-row v-if="myTaskList != [] && currentTask != null">
              <div v-for="(item, index) in myTaskList" :key="index" class="card">
                <instance-card :instanceItem="item" :taskItem="currentTask" :role="'reproductioner-builder'" @showInstanceStatus="showInstanceStatus" @instance="instance"></instance-card>
              </div>
            </el-row>
          </div>
          <div class="page">
            <el-pagination
              @current-change="handleCurrentChange"
              :current-page.sync="instancePageFilter.myTaskPage"
              :page-size="instancePageFilter.pageSize"
              background
              layout="prev, pager, next"
              :total="allMyTaskList.length"
            ></el-pagination>
          </div>
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
import X2js from 'x2js'
import {
  saveIntegrateTaskInstance,

  runtask,

  getSelectedTaskByProjectId,
  getSelectedInstancesByProjectId,
  getAllInstances
} from '@/api/request';
import {
  // generateAction,
  generateXml1,
  // getCellStyle
} from './components/configuration';


import instanceCard from '_com/Cards/InstanceCard';

const { mxEvent, mxGraphHandler } = mxgraph;

export default {
  components: {
    // dataCellInfo,
    instanceCard,
  },

  computed: {
    ...mapState({
      role: (state) => state.permission.role,
    }),
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
        name: '',
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



      stateList: [],
      // dataItemModelbarKey: 0,

      taskInfo: {
        taskName: '',
        taskDescription: '',
      },

      isNewTaskContainerShow: true,

      currentTask: null,

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
          disable: false,
        },
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
        pageSize: 7,
        page: 1,
        myTaskPage: 1,
      },
      instanceList: [],
      selectedInstances: [],
      myTaskList: [],
      allMyTaskList: [],
      switchValue: false,
      outputDialogVisible: false,
      inputDialogVisible: false,
      type: '',
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
    handleCurrentChange(val) {
      console.log(val);
    },

    //初始化mxgraph
    async init() {
      this.container = this.$refs.container;
      await this.getSelectedTaskByProjectId();
      await this.getSelectedInstances();
      
      this.createGraph();
      this.createCell(0);
      this.listenGraphEvent();
    },

    async getSelectedTaskByProjectId() {
      this.currentTask = await getSelectedTaskByProjectId(this.projectId);
    },

    // Creates the graph inside the given container
    createGraph() {
      this.graph = genGraph(this.container);
      this.graph.setPanning(true);
    },

    createCell(index) {
      this.graph.removeCells(this.graph.getChildVertices(this.graph.getDefaultParent()));
      this.graph.importGraph(this.instanceList[index].taskContent);
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
      let data2 = await getAllInstances(this.currentTask.id)
      this.selectedInstances = data1;
      this.allMyTaskList = data2
      this.getInstanceList();
    },

    getInstanceList() {
      this.instanceList = [];
      this.myTaskList = []
      let index = 0;
      while (
        index < this.instancePageFilter.pageSize &&
        (this.instancePageFilter.page - 1) * this.instancePageFilter.pageSize + index < this.selectedInstances.length
      ) {
        this.instanceList.push(this.selectedInstances[(this.instancePageFilter.page - 1) * this.instancePageFilter.pageSize + index]);
        index++;
      }
      index = 0
      while(index < this.instancePageFilter.pageSize && (this.instancePageFilter.page - 1) * this.instancePageFilter.pageSize + index < this.allMyTaskList.length) {
        this.myTaskList.push(this.allMyTaskList[(this.instancePageFilter.page - 1) * this.instancePageFilter.pageSize + index]);
        index++;
      }
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

    clickCard(index) {
      this.createCell(index);
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
            cell.value = ''
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
      console.log(xml)
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
        // action: generateAction(
        //   this.currentTask.id,
        //   this.modelListInGraph,
        //   this.modelInputInGraph,
        //   this.modelLinkInGraph,
        //   this.modelOutputInGraph,
        //   this.linkEdgeList,
        //   'taskInstance'
        // ),
        authority: 'public',
        taskInfo: taskInfo,
        taskContent: this.changeXML(),
        status: 0,
        tid: tid,
      };
      let data = await saveIntegrateTaskInstance(postJson);
      console.log(data);
      // this.currentTaskInstance = data;
      this.allMyTaskList.splice(0, 0, data);
      if (this.myTaskList.length == this.instancePageFilter.pageSize) {
        this.myTaskList.pop();
      }
      this.myTaskList.splice(0, 0, data);
    },

    instance(val) {
      console.log(val);
      this.allMyTaskList.forEach((instance) => {
        if (instance.id == val.id) {
          instance.status = val.status;
        }
      });
    },

    changeXML() {
      let xml = this.graph.getGraphXml()
      let x2js = new X2js()
      let json = x2js.xml2js(xml)
      json.mxGraphModel.root.mxCell.forEach(cell => {
        if(cell._type == 'dataService') {
          // console.log('dataService', cell)
          cell._style = "fontColor=#f6f6f6;fillColor=#07689f;strokeColor=;shape=rectangle;strokeWidth=1.5;align=center;imageAlign=center;imageVerticalAlign=top"
        } else if(cell._type == 'dataServiceOutput') {
          // console.log('dataServiceOutput', cell)
          cell._value = ''
        }
      })
      return x2js.js2xml(json)
    },

    showInstanceStatus() {},
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
    width: 85%;
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
    width: 15%;
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
