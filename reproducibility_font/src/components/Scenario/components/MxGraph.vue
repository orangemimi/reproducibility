<template>
  <div class="main">
    <div class="leftBar" v-if="role == 'builder'">
      <el-input placeholder="Search model/tool" size="mini" class="search_input">
        <el-button slot="append" icon="el-icon-search"></el-button>
      </el-input>

      <vue-scroll style="height: calc(86vh); width: 100%" :ops="ops">
        <el-collapse v-model="activeNames" class="collapse">
          <el-collapse-item title="General" name="general">
            <general-toolbar ref="generalBar"></general-toolbar>
          </el-collapse-item>
          <el-collapse-item title="Models" name="models">
            <model-item-toolbar ref="modelBar" @getModels="getModels"></model-item-toolbar>
          </el-collapse-item>
          <el-collapse-item title="Data Processing Methods" name="dataProcessings">
            <data-processing-toolbar ref="dataProcessingBar" @getDataProcessings="getDataProcessings"></data-processing-toolbar>
          </el-collapse-item>
          <el-collapse-item title="Related Datas" name="modelRelatedDatas">
            <div v-if="modelDoubleClick">
              <data-item-toolbar :cell="cell" ref="dataItemBar" @getInAndOut="getInAndOut"></data-item-toolbar>
            </div>
          </el-collapse-item>
        </el-collapse>
      </vue-scroll>
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

        <transition name="fade" mode="out-in">
          <div class="bell" @click="setAsSelectTaskInConstruction">
            <i :class="isSelectTaskInConsruction ? 'el-icon-message-solid' : ' el-icon-bell'"></i>
          </div>
        </transition>

        <integrate-tasks @selectTask="selectTask" style="float:right"></integrate-tasks>
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
      <el-dialog :visible.sync="dataDoubleClick" width="50%" title="Configuration">
        <span>
          <!-- Configuration -->
          <data-cell-info :cell="dataNode" @currentEventWithFile="currentEventWithFile"></data-cell-info>
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
  saveDataItem,
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
import { generateAction, generateXml, differCellStyle, getCellStyle } from './configuration';
import { initSetTimeOut, hasProperty } from '@/utils/utils';

import dataCellInfo from '_com/DataCellInfo/Info';
import integrateTasks from '_com/IntegrateTasks';
import instanceCard from '_com/Cards/InstanceCard';

import modelItemToolbar from '_com/MxGraphBars/ModelItemToolbar';
import dataItemToolbar from '_com/MxGraphBars/DataItemToolbar';
import generalToolbar from '_com/MxGraphBars/GeneralToolbar';
import dataProcessingToolbar from '_com/MxGraphBars/DataProcessingToolbar';
import { generalList } from '_com/MxGraphBars/toolbar';

const {
  // mxGraph,
  mxEvent,
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
    modelItemToolbar,
    dataItemToolbar,
    dataCellInfo,
    integrateTasks,
    instanceCard,
    generalToolbar,
    dataProcessingToolbar
  },

  watch: {
    taskInfoInit: {
      async handler(val) {
        if (JSON.stringify(val) != '{}') {
          await initSetTimeOut();
          // debugger;
          this.currentTask = val;

          this.init();
          await this.getAllIntegrateTaskInstances(0);
          this.graph.importGraph(val.mxgraph);
        }
      },
      deep: true
    }
  },

  computed: {
    generalList: () => generalList, // general toolbar
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

      //collapse
      activeNames: ['general', 'models'],

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
      // selectInstanceId:''
    };
  },

  methods: {
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
    getModels(val) {
      // console.log('modelitm', val);
      this.$set(this, 'modelItemList', val);
    },

    getDataProcessings(val) {
      console.log(val);
    },

    //--------------初始化 bar的dataItem的内容--由 data-item-toolbar组件返回
    getInAndOut(input, output) {
      // this.state = val;

      this.inputItemList = input;
      this.outputItemList = output;
      this.initLeftBar('inputBar');
      this.initLeftBar('outputBar');
    },
    // handleCollapseChange(val) {

    // },

    //初始化mxgraph
    async init() {
      this.container = this.$refs.container;
      this.createGraph();
      this.listenGraphEvent();
      this.initLeftBar('generalBar');
      this.initLeftBar('modelBar');
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
          this.activeNames.push('modelRelatedDatas');
          this.dataDoubleClick = this.dataClick = this.modelClick = false;
        } else {
          this.modelDoubleClick = this.modelClick = this.dataClick = false;
          this.dataDoubleClick = true;
          // console.log(cell);
          this.dataNode = cell;
        }
      });

      // 监听单击事件
      //单击空白处 dialog隐藏
      this.graph.addListener(mxEvent.CLICK, (sender, evt) => {
        let isCell = Object.prototype.hasOwnProperty.call(evt.properties, 'cell');
        if (isCell) {
          let cell = evt.properties.cell;

          const clickModelType = cell.type;

          if (clickModelType == 'model') {
            // 使用 mxGraph 事件中心，触发自定义事件
            // this.cell = cell;
            this.modelClick = true;
            this.modelDoubleClick = this.dataClick = this.dataDoubleClick = false;
          } else {
            this.modelDoubleClick = this.modelClick = this.dataDoubleClick = false;
            this.dataClick = true;
            // console.log(cell);
            this.dataNode = cell;
          }
        } else {
          //单击空白处
          this.cell = {};
        }
      });

      //监听增加连接线
      this.graph.addListener(mxEvent.CELLS_ADDED, (sender, evt) => {
        const cell = evt.properties.cells[0];
        if (cell.vertex) {
          // this.$message.info('Add a node');
        } else if (cell.edge) {
          //判断是否是link to next dataitem
          let linkCell = cell.target;
          if (linkCell.type == 'input') {
            this.graph.getModel().beginUpdate();
            try {
              linkCell.type = 'link';

              let style = getCellStyle(differCellStyle(linkCell.type), linkCell);

              this.graph.getModel().setStyle(linkCell, style);
            } finally {
              this.graph.getModel().endUpdate();
            }
          }
        }
      });

      // 监听 mxGraph 事件
      this.mxGraphSelectionModel = this.graph.getSelectionModel();
      this.mxGraphSelectionModel.addListener(mxEvent.CHANGE, this.handleSelectionChange);
    },

    initLeftBar(panel) {
      let refType;
      let barRef; //对应各个Bar组件中的ref
      let barItemList = [];
      let styleIn = differCellStyle(panel);

      if (panel == 'modelBar') {
        refType = 'modelBar';
        barRef = 'modelItemList';
        barItemList = this.modelItemList;
      } else if (panel == 'inputBar') {
        refType = 'dataItemBar';
        barRef = 'inputItemList';
        barItemList = this.inputItemList;
      } else if (panel == 'outputBar') {
        refType = 'dataItemBar';
        barRef = 'outputItemList';
        barItemList = this.outputItemList;
      } else {
        refType = 'generalBar';
        barRef = 'general';
        barItemList = this.generalList;
      }

      const domArray = this.$refs[refType].$refs[barRef];

      if (!(domArray instanceof Array) || domArray.length <= 0) {
        return;
      }

      domArray.forEach((dom, domIndex) => {
        const dragItem = barItemList[domIndex];

        let cellStyle = getCellStyle(styleIn, dragItem);

        const dropHandler = (graph, evt, cell, x, y) => {
          this.addCell(dragItem, x, y, panel, cellStyle);
        };

        const createDragPreview = () => {
          const elt = document.createElement('div');
          elt.style.border = '2px dotted black';
          elt.style.width = `200px`;
          elt.style.height = `50px`;
          return elt;
        };

        mxUtils.makeDraggable(dom, this.graph, dropHandler, createDragPreview(), 0, 0, false, true);
      });
    },

    addCell(item, x, y, type, styleObj) {
      this.graph.getModel().beginUpdate();

      let vertex = this.graph.insertVertex(
        this.graph.getDefaultParent(),
        null,
        null,
        x,
        y,
        200, //width
        50, //height
        styleObj
      );
      setTimeout(() => {
        this.graph.getModel().setStyle(vertex, styleObj);
      }, 1000);

      try {
        if (type == 'modelBar') {
          vertex.name = item.name;
          vertex.doi = item.doi;
          vertex.type = 'model';
          vertex.iterationNum = '1';
          vertex.md5 = item.md5;
        } else {
          if (this.selectionCells.length == 0) {
            this.$notify.error({
              title: 'Error',
              message: 'You have not select any model'
            });

            return;
          }

          let selectionCell = this.selectionCells[0];

          vertex.name = item.name;
          vertex.eventId = item.eventId;
          vertex.description = item.description; //event description
          vertex.datasetItem = item.datasetItem;
          vertex.stateId = item.stateId; //event description
          vertex.stateName = item.stateName;
          vertex.stateDescription = item.stateDescription;
          vertex.md5 = item.md5;
          vertex.doi = item.doi;
          vertex.optional = item.optional;
          vertex.linkModelCellId = selectionCell.id; //放置输入输出node时 与其关联的model的nodeId
          vertex.type = item.type;

          if (type == 'inputBar') {
            this.addEdge(vertex, selectionCell);
          } else if (type == 'outputBar') {
            this.addEdge(selectionCell, vertex);
          }
        }
      } finally {
        this.graph.getModel().endUpdate();
      }
    },

    addEdge(source, target) {
      this.graph.insertEdge(this.graph.getDefaultParent(), null, null, source, target, null);
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
        mxgraph: graphXml,
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
      let mxgraph = val.mxgraph;
      this.importModelXML(mxgraph);
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
          url: content.value,
          suffix: content.suffix,
          description: '',
          isDirect: false //--false是中间数据
        };

        let data = await saveDataItem(dataJson);
        eventCell.fileId = data.id;
        eventCell.value = data.url;
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

    currentEventWithFile(val) {
      this.graph.getModel().beginUpdate();

      try {
        Object.values(this.graph.getModel().cells).forEach(cell => {
          if (cell.id == val.id) {
            cell.value = val.value;
            this.$message({
              message: 'You have submit the file successfully',
              type: 'success'
            });
          }
        });
      } finally {
        this.graph.getModel().endUpdate();
      }
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
  display: flex;
  position: relative;

  .leftBar {
    position: relative;
    width: 300px;
    // padding-right: 10px;
    .search_input {
      padding: 0 10px 5px 0;
    }

    .collapse {
      padding-right: 10px;

      /deep/.el-collapse-item__header {
        font-size: 16px;
        font-weight: 600;
        font-style: italic;
        background-color: rgba($color: $blueEmplasisFont, $alpha: 0.1);
        line-height: 35px;
        height: 35px;
        color: #6386cc;
        text-align: center;
        // border-radius: 5px;
        padding-left: 20px;
        border-top: 3px solid #939fb8;
      }
      /deep/ .el-collapse-item__wrap {
        background-color: rgba($color: #3066d6, $alpha: 0.05);
      }
    }

    .modelbarContainer {
      background-color: rgba($color: #b2b5c7, $alpha: 0.1);
      border: 0.5px solid #a7a8ad;
      border-radius: 5px;
      // box-shadow: $normalBoxShadow;
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 50%;

      .modelbarTitle {
        font-size: 20px;
        font-weight: 600;
        text-align: center;
        margin: 5px 0;
      }

      .items {
        margin-top: 2px;
        .item {
          margin: 0 10px;
          width: 120px;
          text-align: center;
          margin-bottom: 5px;
        }
      }
      // z-index: 1;
    }

    .editCellContainer {
      background-color: rgba(243, 243, 243, 0.5);
      border: 0.5px solid rgb(243, 243, 243);
      border-radius: 5px;
      position: absolute;
      width: 100%;
      padding: 0 5px;

      z-index: auto;
      height: 50%;
      bottom: 0px;
      left: 0px;
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
    }
    .page {
      position: absolute;
      bottom: 0;
    }
  }

  .mainContainer {
    position: relative;
    // top: 0;
    // left: 300px;
    width: calc(100% - 500px);
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
      background: rgb(251, 251, 251) url('./../../../assets/images/mxgraph/point.gif') 0 0 repeat;
      border-radius: 4px;
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
