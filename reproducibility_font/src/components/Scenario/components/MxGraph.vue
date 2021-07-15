<template>
  <div class="main">
    <div class="leftBar" v-if="role == 'builder'">
      <left-toolbar ref="leftToolbar"></left-toolbar>
    </div>
    <div class="mainContainer">
      <div class="modelbarTop">
        <!-- <el-button @click="generateAction" type="text" size="mini">Export as Picture</el-button> -->
        <!-- <el-button @click="exportGraph" type="text" size="mini">Export as XML</el-button>
        <input @change="readFile" ref="importInput" class="hide" type="file" />
        <el-button @click="importGraphFile" type="text" size="mini">Import mxGraph</el-button> -->
        <el-button @click="checked ? deleteCells() : deleteCellsConfirmDialog()" type="text" size="mini" :disabled="selectionCells.length == 0">
          Delete
        </el-button>
        <el-button @click="undo" type="text" size="mini">Undo</el-button>
        <el-button @click="redo" type="text" size="mini">Redo</el-button>
        <!-- 创建一个新的画布  均为create-->
        <el-button @click="showCreateNewTask" type="success" size="mini">New Task</el-button>
        <!-- 保存现有的画布 均为update-->
        <el-button @click="updateGraph" type="warning" size="mini">Save Task</el-button>
        <el-button @click="runGraph" size="mini">Run Task</el-button>
        <el-button @click="compareResult" size="mini">Compare Result</el-button>

        <transition name="fade" mode="out-in">
          <div class="bell" @click="setAsSelectTaskInConstruction">
            <i :class="isSelectTaskInConsruction ? 'el-icon-message-solid' : ' el-icon-bell'"></i>
          </div>
        </transition>

        <integrate-tasks @selectTask="selectTask" style="float:right"></integrate-tasks>
      </div>
      <vue-scroll style="height: 900px; width: calc(100%)" :ops="ops">
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
      <el-dialog :visible.sync="deleteCellsVisible" width="30%">
        <span>Are you sure to delete this node?</span>
        <span slot="footer" class="dialog-footer">
          <el-checkbox v-model="checked" label="1">Ignore this tip in this action</el-checkbox>
          <el-button @click="deleteCellsVisible = false">Cancel</el-button>
          <el-button type="primary" @click="deleteCells">Confirm</el-button>
        </span>
      </el-dialog>
    </div>

    <div class="dialogs">
      <el-dialog :visible.sync="comparisonDialogShow" width="80%" title="Comparison">
        <div style="height:500px">
          <!-- Configuration -->
          <comparison />
        </div>
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
  saveIntegrateTask,
  updateIntegrateTask,
  saveIntegrateTaskInstance,
  updateIntegrateTaskInstanceById,
  getScenarioByProjectId,
  getAllIntegrateTaskInstancesByTaskId,
  runtask,
  checkTaskStatus,
  updatePerformanceById,
  updateScenarioByProjectId
} from '@/api/request';
import { generateAction, generateXml, getCellStyle } from './configuration';
import { initSetTimeOut, hasProperty } from '@/utils/utils';

import integrateTasks from '_com/IntegrateTasks';
import instanceCard from '_com/Cards/InstanceCard';

import comparison from './Comparison';
import leftToolbar from './LeftToolbar.vue';

const {
  // mxGraph,
  // mxEvent,
  mxUtils,
  mxCodec
} = mxgraph;

export default {
  props: {
    taskInfoInit: {
      type: Object
    }
  },

  components: {
    integrateTasks,
    instanceCard,
    comparison,
    leftToolbar
  },

  watch: {
    taskInfoInit: {
      async handler(val, oldVal) {
        if (val.id != undefined && val.id != oldVal.id) {
          console.log('valval', val);
          await initSetTimeOut();
          // debugger;
          this.currentTask = val;

          this.init();
          this.$refs.leftToolbar.init();
          this.$refs.leftToolbar.listenGraphEvent();

          await this.getAllIntegrateTaskInstances(0);
          this.graph.importGraph(val.note);
        }
      },
      deep: true
    }
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

      doi: '',
      cell: {}, //双击事件 cell
      state: {},

      modelListInGraph: [],
      dataInputInGraph: [],
      dataLinkInGraph: [], //下一模型的输入数据
      dataOutputInGraph: [],
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

      //currentInstance;
      currentTaskInstance: {},

      //INSTANCE LIST
      instancePageFilter: {
        pageSize: 8,
        page: 0
      },
      instanceList: [],

      comparisonDialogShow: false
      // selectInstanceId:''
    };
  },

  methods: {
    compareResult() {
      this.comparisonDialogShow = true;
    },

    async setAsSelectTaskInConstruction() {
      this.isSelectTaskInConsruction = !this.isSelectTaskInConsruction;
      let taskId = '';
      //已经取消了选择的Task
      if (this.isSelectTaskInConsruction) {
        taskId = '';
      } else {
        taskId = this.currentTask.id;
      }

      await updateScenarioByProjectId(this.projectId, { selectTaskId: taskId });
      // console.log('!!!');
    },
    //--------------初始化 bar的modelItem的内容--由 AllModels组件返回

    // handleCollapseChange(val) {

    // },

    //初始化mxgraph
    async init() {
      this.container = this.$refs.container;
      this.createGraph();

      // this.initLeftBar('generalBar');
      // this.initLeftBar('codeBar');
      // this.initLeftBar('modelBar');
      this.getScenario();
    },

    async getScenario() {
      let data = await getScenarioByProjectId(this.projectId);
      // console.log(data.selectTaskId, this.currentTask.id);
      let flag = data.selectTaskId == this.currentTask.id;
      this.$set(this, 'isSelectTaskInConsruction', flag);
      // console.log(this.isSelectTaskInConsruction);
    },

    // Creates the graph inside the given container
    createGraph() {
      this.graph = genGraph(this.container);
    },

    getCells() {
      let modelListInGraph = [];
      let dataOutputInGraph = [];
      let dataInputInGraph = [];
      let dataLinkInGraph = [];

      Object.values(this.graph.getModel().cells).forEach(cell => {
        if (cell.style != undefined) {
          if (cell.type == 'model') {
            modelListInGraph.push(cell);
          } else if (cell.type == 'output') {
            dataOutputInGraph.push(cell);
          } else if (cell.type == 'input') {
            dataInputInGraph.push(cell);
          } else if (cell.type == 'link') {
            dataLinkInGraph.push(cell);
          }
        }
      });
      let links = Object.values(this.graph.getModel().cells).filter(cell => Object.prototype.hasOwnProperty.call(cell, 'edge'));
      this.linkEdgeList = links;
      this.modelListInGraph = modelListInGraph;
      this.dataOutputInGraph = dataOutputInGraph;
      this.dataInputInGraph = dataInputInGraph;
      this.dataLinkInGraph = dataLinkInGraph;
    },

    judgeInputList() {
      let dataInputInGraph = this.dataInputInGraph;
      dataInputInGraph.forEach(input => {
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

    closeDialog(val) {
      this.editCellVisible = val;
    },

    submitCellForm(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          this.graph.getModel().beginUpdate();
          let cell = this.graph.getSelectionCell();
          if (cell.name == this.cellForm.name) {
            this.$message({
              message: 'Name changed is as same as before',
              type: 'warning'
            });
          } else {
            try {
              cell.name = this.cellForm.name;
              this.graph.refresh(cell); // 刷新cell
              this.$message({
                message: 'Refresh the node successfully!',
                type: 'success'
              });
            } finally {
              this.graph.getModel().endUpdate();
            }
          }
        }
      });
    },

    handleSelectionChange(selectModel) {
      this.selectionCells = [];
      if (!selectModel.cells.length) {
        return;
      }
      this.selectionCells = this.graph.getSelectionCells();
    },

    deleteCellsConfirmDialog() {
      this.deleteCellsVisible = true;
    },

    deleteCells() {
      let cells = this.selectionCells;
      let tmpSet = new Set(this.selectionCells);

      cells.forEach(cell => {
        this.findDeleteCell(cell, tmpSet);
      });
      this.deleteCellsVisible = false;

      this.graph.removeCells(Array.from(tmpSet), true);
    },

    findDeleteCell(cell, deleteSet) {
      deleteSet.add(cell);
      if (cell.edges) {
        cell.edges.forEach(tmpEdge => {
          if (tmpEdge.target !== cell) {
            deleteSet.add(tmpEdge.target);
            this.findDeleteCell(tmpEdge.target, deleteSet);
          }
        });
      }
    },

    getUndoRedoCell() {
      let cells = [];
      if (this.undoMng) {
        let undoIndex = this.undoMng.indexOfNextAdd - 1;

        if (this.undoMng.history[undoIndex]) {
          cells = this.undoMng.history[undoIndex].changes.map(change => {
            if (change.child) {
              return change.child;
            } else {
              return change.cell;
            }
          });
        }
      }
      return cells;
    },
    //撤销
    undo() {
      if (!this.undoMng) {
        throw new Error('mxUndoManager 没有初始化');
      }
      this.undoMng.undo();
    },
    //重做
    redo() {
      if (!this.undoMng) {
        throw new Error('mxUndoManager 没有初始化');
      }
      this.undoMng.redo();
      // console.info("前进的Cells", this.getUndoRedoCell());
    },

    //创建一个新的画布
    showCreateNewTask() {
      this.currentTask = {
        taskName: '',
        taskDescription: ''
      };
      this.isNewTaskContainerShow = true;
    },

    async createNewTask() {
      let postJson = {
        projectId: this.projectId,
        ...this.taskInfo
      };
      // console.log(postJson);
      // let data = await post(`/integrateTasks`, postJson);

      let data = await saveIntegrateTask(postJson);
      this.currentTask = data;
      this.isNewTaskContainerShow = false;
      // console.log(data);
    },

    //保存task
    async updateGraph() {
      if (this.graph.getModel().cells.length < 1) {
        this.$message.error('Please select at least one model.');
        return;
      }

      this.getCells();

      //王梓欢配置文件
      let xml = generateXml(this.currentTask.taskName, this.modelListInGraph, this.dataInputInGraph, this.dataLinkInGraph, this.dataOutputInGraph, this.linkEdgeList);

      //mxgraph xml文件
      let graphXml = this.graph.getGraphXml();

      //model action
      let action = generateAction(this.currentTask.id, this.modelListInGraph, this.dataInputInGraph, this.dataLinkInGraph, this.dataOutputInGraph, this.linkEdgeList, 'task');

      let postJson = {
        xml: xml,
        note: graphXml,
        // modelActions: modelActions,modelACTIONlIST
        taskName: this.currentTask.taskName,
        taskDescription: this.currentTask.taskDescription,
        action: action
      };

      await updateIntegrateTask(this.currentTask.id, postJson);
      let content = { content: 'Simulation Scenario', degree: '100%', type: 'success', icon: 'el-icon-sunny' };

      await updatePerformanceById('context', this.projectId, content);
      // await patch(`/integrateTasks/${this.currentTask.id}`, postJson);
      // console.log(data);
    },

    selectTask(val) {
      this.currentTask = val;
      // this.taskInfo = val;
      let note = val.note;
      this.importModelXML(note);
      this.isNewTaskContainerShow = false;
      // console.log(val);
    },

    importModelXML(xmlTxt) {
      //xml to json
      this.graph.getModel().beginUpdate();
      try {
        const doc = mxUtils.parseXml(xmlTxt);
        const root = doc.documentElement;
        const dec = new mxCodec(root.ownerDocument);
        dec.decode(root, this.graph.getModel());
      } finally {
        this.graph.getModel().endUpdate();
      }
      // this._restoreModel();
    },

    async showInstanceStatus(value) {
      this.currentTaskInstance = value;
      await this.getOutputs(value.tid);
    },

    getInstanceAction(action) {
      let outputList = this.dataOutputInGraph;
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

      let xml = generateXml(this.currentTask.taskName, this.modelListInGraph, this.dataInputInGraph, this.dataLinkInGraph, this.dataOutputInGraph, this.linkEdgeList);
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
      let list = [...this.dataInputInGraph, ...this.dataLinkInGraph];
      let inputList = list.filter(event => event.md5 == modelCell.md5);
      let outputList = this.dataOutputInGraph.filter(event => event.md5 == modelCell.md5);
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
      // console.log(this.dataOutputInGraph);
      this.dataOutputInGraph.forEach(async eventCell => {
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
        action: generateAction(this.currentTask.id, this.modelListInGraph, this.dataInputInGraph, this.dataLinkInGraph, this.dataOutputInGraph, this.linkEdgeList, 'taskInstance'),
        status: 0,
        tid: tid
      };
      let data = await saveIntegrateTaskInstance(postJson);
      this.instanceList.push(data);
    },

    async updateTaskInstances() {
      let postJson = {
        action: generateAction(this.currentTask.id, this.modelListInGraph, this.dataInputInGraph, this.dataLinkInGraph, this.dataOutputInGraph, this.linkEdgeList, 'taskInstance'),
        status: 1
      };
      // console.log(postJson, this.currentTaskInstance.id);
      await updateIntegrateTaskInstanceById(this.currentTaskInstance.id, postJson);
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
    }
  },

  async mounted() {}
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
  height: 100%;
  // display: flex;
  // position: relative;

  .mainContainer {
    float: left;
    // position: relative;
    // top: 0;
    // left: 300px;
    width: calc(100% - 505px);
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
      min-height: 850px;
      background: rgb(251, 251, 251) url('./../../../assets/images/mxgraph/point.gif') 0 0 repeat;
      border-radius: 4px;
    }
  }

  .rightBar {
    // position: relative;
    float: left;
    // right: 0;
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
