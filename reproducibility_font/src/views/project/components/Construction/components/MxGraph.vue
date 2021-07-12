<template>
  <div class="main">
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
    <div class="leftBar" v-if="role == 'builder'">
      <el-input placeholder="Search model/tool" size="mini" class="search_input">
        <el-button slot="append" icon="el-icon-search"></el-button>
      </el-input>

      <vue-scroll style="height: calc(86vh); width: 100%" :ops="ops">
        <el-collapse v-model="activeNames" class="collapse">
          <el-collapse-item title="General" name="general">
            <general-toolbar ref="generalBar"></general-toolbar>
            <code-toolbar ref="codeBar"></code-toolbar>
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
=======
    <div class="leftBar">
      <div class="modelbarContainer">
        <el-row>
          <all-models ref="modelBar" @getModels="getModels"></all-models>
        </el-row>
      </div>
      <div class="editCellContainer">
        <!-- <edit-cell :visible="editCellVisible" @currentGraph="grapg"></edit-cell> -->
        <div v-if="modelClick" class="normalContaniner">
          <div>Node Info</div>
          <!-- <vue-scroll style="height: 630px; width: 100%">
            <data-item-info :cell="dataNode"></data-item-info>
          </vue-scroll> -->
        </div>
        <div v-show="modelDoubleClick" class="normalContaniner">
          <div>Node Info</div>
          <vue-scroll style="height: 400px; width: 100%">
            <data-item-toolbar
              :cell="cell"
              ref="dataItemBar"
              @getInAndOut="getInAndOut"
              :key="dataItemModelbarKey"
            ></data-item-toolbar>
          </vue-scroll>
        </div>
        <div v-show="dataClick" class="expandContaniner">
          <div>Node Info</div>
          <data-item-info
            :cell="dataNode"
            @currentEventWithFile="currentEventWithFile"
          ></data-item-info>
        </div>
        <div v-show="dataDoubleClick">
          <div>Node Info</div>
          <!-- <data-item-info :cell="dataNode"></data-item-info> -->
        </div>
      </div>
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
    </div>
    <div class="mainContainer">
      <div class="modelbarTop">
        <!-- <el-button @click="generateAction" type="text" size="mini">Export as Picture</el-button> -->
        <!-- <el-button @click="exportGraph" type="text" size="mini">Export as XML</el-button>
        <input @change="readFile" ref="importInput" class="hide" type="file" />
        <el-button @click="importGraphFile" type="text" size="mini">Import mxGraph</el-button> -->
        <el-button
          @click="checked ? deleteCells() : deleteCellsConfirmDialog()"
          type="text"
          size="mini"
          :disabled="selectionCells.length == 0"
        >
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
      <vue-scroll style="height: 800px; width: calc(100%)" :ops="ops">
        <div class="graphContainer" ref="container"></div>
      </vue-scroll>
    </div>

    <div class="rightBar">
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
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
=======
      <integrate-task-instances :currentTaskInit="currentTask"></integrate-task-instances>
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
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
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue

    <div class="dialogs">
      <el-dialog :visible.sync="dataDoubleClick" width="50%" title="Configuration">
        <span>
          <!-- Configuration -->
          <data-cell-info :cell="dataNode" @currentEventWithFile="currentEventWithFile"></data-cell-info>
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
=======
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
  </div>
  <!-- </div> -->
</template>

<script>
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
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
import { generateAction, generateXml, differCellStyle, getCellStyle } from './configuration';
import { initSetTimeOut, hasProperty } from '@/utils/utils';

import dataCellInfo from '_com/DataCellInfo/Info';
import integrateTasks from '_com/IntegrateTasks';
import instanceCard from '_com/Cards/InstanceCard';

import modelItemToolbar from '_com/MxGraphBars/ModelItemToolbar';
import dataItemToolbar from '_com/MxGraphBars/DataItemToolbar';
import generalToolbar from '_com/MxGraphBars/GeneralToolbar';
import codeToolbar from '_com/MxGraphBars/CodeToolbar';
import dataProcessingToolbar from '_com/MxGraphBars/DataProcessingToolbar';
import { generalList, codeList } from '_com/MxGraphBars/toolbar';
import comparison from './Comparison';
=======
import mxgraph from '@/components/MxGraph/index';
import { getGraphXml } from '@/components/MxGraph/Graph';
import { genGraph } from '@/components/MxGraph/mx2';
import {
  saveIntegrateTasks,
  updateIntegrateTasks,
  saveIntegrateTaskInstances
} from '@/api/request';
import allModels from './AllModels';
import { get, post } from '@/axios';
import dataItemToolbar from './../../Construction/DataItemToolbar';
import dataItemInfo from './../../Construction/DataItemInfo';
import integrateTasks from './../../Construction/IntegrateTasks';
import integrateTaskInstances from './IntegrateTaskInstances';
import { mapState } from 'vuex';
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue

const {
  // mxGraph,
  mxEvent,
  mxUtils,
  mxCodec
} = mxgraph;

export default {
  props: {
    sendXml: {
      type: String
    },
    taskInfoInit: {
      type: Object
    }
  },

  components: {
    allModels,
    dataItemToolbar,
    dataItemInfo,
    integrateTasks,
    instanceCard,
    generalToolbar,
    codeToolbar,
    dataProcessingToolbar,
    comparison
  },

  watch: {
    sendXml: {
      handler(val) {
        if (val != '') {
          this.getXml = val;
          this.graph.importGraph(this.getXml);
        }
      },
      deep: true
    },
    taskInfoInit: {
      handler(val) {
        if (JSON.stringify(val) != '{}') {
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
          await initSetTimeOut();
          // debugger;
          this.currentTask = val;

          this.init();
          await this.getAllIntegrateTaskInstances(0);
          this.graph.importGraph(val.note);
=======
          // console.log('initTak', val);
          this.currentTask = val;
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
        }
      },
      deep: true
    }
  },

  computed: {
    generalList: () => generalList, // general toolbar
    codeList: () => codeList,
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

      codeClick: false,
      codeDoubleClick: false,

      modelListInGraph: [],
      dataInputInGraph: [],
      dataLinkInGraph: [], //下一模型的输入数据
      dataOutputInGraph: [],
      linkEdgeList: [],

      stateList: [],
      dataItemModelbarKey: 0,

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
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
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
      instanceList: [],

      comparisonDialogShow: false
      // selectInstanceId:''
=======
      isSelectTaskInConsruction: true
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
    };
  },

  methods: {
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
    compareResult() {
      this.comparisonDialogShow = true;
    },
    async setAsSelectTaskInConstruction() {
=======
    setAsSelectTaskInConstruction() {
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
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
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
      // console.log('modelitm', val);
=======
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
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

    //初始化mxgraph
    async init() {
      this.container = this.$refs.container;
      this.createGraph();
      this.listenGraphEvent();
      this.initLeftBar('generalBar');
      // this.initLeftBar('codeBar');
      this.initLeftBar('modelBar');
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
      this.getScenario();
    },

    async getScenario() {
      let data = await getScenarioByProjectId(this.projectId);
      // console.log(data.selectTaskId, this.currentTask.id);
      let flag = data.selectTaskId == this.currentTask.id;
      this.$set(this, 'isSelectTaskInConsruction', flag);
      // console.log(this.isSelectTaskInConsruction);
=======
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
    },

    // Creates the graph inside the given container
    createGraph() {
      this.graph = genGraph(this.container);
    },

    listenGraphEvent() {
      // 监听双击事件
      this.graph.addListener(mxEvent.DOUBLE_CLICK, async (graph, evt) => {
        // DOUBLE_CLICK
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
        let cell = evt.properties.cell;
=======
        let cell = {
          doi: evt.properties.cell.doi,
          style: evt.properties.cell.style,
          type: evt.properties.cell.type
        };

>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
        let clickModelType = cell.type;
        if (clickModelType == 'model') {
          this.dataItemModelbarKey++;
          await this.$refs.dataItemBar.initSetTimeOut();
          this.modelDoubleClick = true;
          this.cell = JSON.parse(JSON.stringify(cell));
          this.dataDoubleClick = this.dataClick = this.modelClick = false;
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
          this.$message.info('Add a line');
        }
      });

      // 监听 mxGraph 事件
      this.mxGraphSelectionModel = this.graph.getSelectionModel();
      this.mxGraphSelectionModel.addListener(mxEvent.CHANGE, this.handleSelectionChange);
    },

<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
=======
    differCellStyle(type) {
      if (type == 'modelBar') {
        return {
          fontColor: '#f6f6f6',
          fillColor: '#07689f',
          strokeColor: '',
          shape: 'rectangle'
        };
      }
      if (type == 'inputBar') {
        return {
          fillColor: '#fff8f8',
          fontColor: '#24292E',
          strokeColor: '',
          shape: 'parallelogram',
          fixedSize: 1
        };
      }
      if (type == 'link') {
        return {
          fillColor: '#ffdcdc',
          fontColor: '#24292E',
          strokeColor: '',
          shape: 'parallelogram',
          fixedSize: 1
        };
      }
      return {
        fillColor: '#f4d160',
        fontColor: '#24292E',
        strokeColor: '',
        shape: 'parallelogram',
        fixedSize: 1
      };
    },

>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
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
      } else if (panel == 'generalBar') {
        refType = 'generalBar';
        barRef = 'general';
        barItemList = this.generalList;
      }
      // else if (panel == 'codeBar') {
      //   refType = 'codeBar';
      //   barRef = 'general';
      //   barItemList = this.codeList;
      // }

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
      let links = Object.values(this.graph.getModel().cells).filter(cell =>
        Object.prototype.hasOwnProperty.call(cell, 'edge')
      );
      this.linkEdgeList = links;
      this.modelListInGraph = modelListInGraph;
      this.dataOutputInGraph = dataOutputInGraph;
      this.dataInputInGraph = dataInputInGraph;
      this.dataLinkInGraph = dataLinkInGraph;
    },

<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
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
=======
      console.log(
        this.modelListInGraph,
        this.dataOutputInGraph,
        this.dataInputInGraph,
        this.dataLinkInGraph
      );
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
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

      let data = await saveIntegrateTasks(postJson);
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
      let graphXml = getGraphXml(this);

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

<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
      await updateIntegrateTask(this.currentTask.id, postJson);
      let content = { content: 'Simulation Scenario', degree: '100%', type: 'success', icon: 'el-icon-sunny' };

      await updatePerformanceById('context', this.projectId, content);
=======
      await updateIntegrateTasks(this.currentTask.id, postJson);
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
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

<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
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
=======
    async runGraph() {
      let xml = this.generateXml();
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
      let file = new File([xml], this.currentTask.taskName + '.xml', {
        type: 'text/xml'
      });
      let formData = new FormData();
      formData.append('file', file);

      //get tid from manager server
      let tid = await runtask(formData);

<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
      await this.addTaskInstance(tid);

=======
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
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
          await this.addTaskInstances();
          return;
        } else {
          let data = await checkTaskStatus(tid);
          this.record = data;
        }
      }, 3000);
    },

    putOutputToCell() {
      let actionResponse = this.record.taskInfo.modelActions;

      this.dataOutputInGraph.forEach(async event => {
        let outputActionList = actionResponse.filter(
          response => event.linkModelCellId == response.id
        );
        let outputValueJson = outputActionList[0].outputData.outputs.filter(
          out => out.dataId == event.eventId && out.state == event.stateName
        );

        event.value = outputValueJson[0].dataContent.value;

        await this.saveRelatedDataItemToResource(outputValueJson[0].dataContent);
      });
    },

    async saveRelatedDataItemToResource(response) {
      let dataJson = {
        name: response.name,
        url: response.value,
        suffix: response.suffix,
        description: '',
        isDirect: false //--false是中间数据
      };

<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
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
=======
      let data = await post(`/dataItems`, dataJson);
      if (data != undefined) {
        this.$message({
          message: 'You have submit the related data resource successfully',
          type: 'success'
        });
      }
    },

    async addTaskInstances() {
      let action = this.generateAction();

>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
      let postJson = {
        name: this.currentTask.taskName + '-Instance',
        taskId: this.currentTask.id,
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
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

=======
        action: action,
        status: this.record.status,
        tid: this.record.taskInfo.taskId
      };
      // console.log(postJson);
      await saveIntegrateTaskInstances(postJson);
      // await post(`/integrateTaskInstances`, postJson);
      // console.log(data2);
    },

>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
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
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue

      this.instanceList = data.content;

      this.instancePageFilter.page++;
      // console.log('instances', data);
    },

    async handleCurrentChange(val) {
      await this.getAllIntegrateTaskInstances(val++);
=======
      s[14] = '4'; // bits 12-15 of the time_hi_and_version field to 0010
      s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
      s[8] = s[13] = s[18] = s[23] = '-';
      let uuid = s.join('');
      return uuid;
    },

    generateAction() {
      let action = {
        taskId: this.currentTask.id,
        modelItemList: [],
        dataItemList: [],
        parameterList: [],
        conditionList: [],
        dependencyList: []
      };

      let modelList = [];
      //拼接集成模型中的models部分
      let models = this.modelListInGraph;
      models.forEach(model => {
        let modelItem = {
          id: model.id,
          // modelOid: model.doi,
          reference: model.md5,
          name: model.name,
          description: model.description,
          behavior: {}
        };

        //get same model input & output
        let allInputList = [...this.dataInputInGraph, ...this.dataLinkInGraph];

        let inputList = allInputList.filter(event => event.md5 == model.md5);
        let outputList = this.dataOutputInGraph.filter(event => event.md5 == model.md5);

        let stateIdList = [];
        if (inputList.length != 0) {
          inputList.forEach(input =>
            stateIdList.push({
              id: input.stateId,
              name: input.stateName,
              description: input.stateDescription
            })
          );
        }
        //获得所有的状态
        let stateIdListNew = stateIdList.reduce(
          (all, next) => (all.some(item => item.id == next.id) ? all : [...all, next]),
          []
        );

        // let stateList = [];

        stateIdListNew.forEach(state => {
          let eventList = [];

          let inputInState = inputList.filter(el => el.stataId == state.id);
          let outputInState = outputList.filter(el => el.stataId == state.id);

          inputInState.forEach(item => {
            let inputData = {
              id: item.id,
              reference: item.eventId,
              name: item.name,
              description: item.description,
              optional: item.optional,
              type: item.type
            };
            if (item.type == 'input') {
              inputData.dataRef = item.fileId;
            } else if (item.type == 'link') {
              // let link = this.linkEdgeList.filter(el => el.target.eventId == item.eventId);
              inputData.link = item.link[0].target.eventId; //if type ==link, dataRef = 上一个output的
              inputData.dataRef = item.link[0].target.fileId;
            }

            eventList.push(inputData);
          });

          outputInState.forEach(item => {
            let output = {
              id: item.id,
              reference: item.eventId,
              name: item.name,
              description: item.description,
              optional: item.optional,
              type: item.type
            };
            if (Object.prototype.hasOwnProperty.call(item, 'fileId')) {
              output.dataRef = item.fileId;
            } else {
              output.dataRef = '';
            }
            eventList.push(output);
          });

          state.eventList = eventList;
        });

        modelItem.behavior.stateList = stateIdListNew;
        // this.$set(modelItem, 'stateList', stateList);
        modelList.push(modelItem);
      });

      action.modelItemList = modelList;
      //dataItemList
      let inputList = this.dataInputInGraph.filter(
        event =>
          Object.prototype.hasOwnProperty.call(event, 'value') &&
          event.value != '' &&
          event.value != undefined
      );
      let outputList = this.dataOutputInGraph.filter(
        event =>
          Object.prototype.hasOwnProperty.call(event, 'value') &&
          event.value != '' &&
          event.value != undefined
      );
      let dataItemList = [];

      inputList.forEach(item => {
        let inputData = {
          id: item.fileId,
          name: item.fileName,
          description: item.fileDescription,
          value: item.value
        };

        dataItemList.push(inputData);
      });

      outputList.forEach(item => {
        let output = {
          id: item.fileId,
          name: item.fileName,
          description: item.description,
          value: item.value
        };
        dataItemList.push(output);
      });

      action.dataItemList = dataItemList;

      let linkList = this.linkEdgeList;
      let dependencyList = [];

      linkList.forEach(edge => {
        let dependency = {};
        const beforeCell = edge.source;
        const nextCell = edge.target;

        let from, to;

        if (
          beforeCell.type == 'output' ||
          beforeCell.type == 'input' ||
          beforeCell.type == 'link'
        ) {
          from = {
            type: 'data',
            id: beforeCell.id,
            reference: beforeCell.eventId
          };
        } else {
          from = {
            type: 'model',
            id: beforeCell.id,
            reference: beforeCell.md5
          };
        }

        if (nextCell.type == 'output' || nextCell.type == 'input' || nextCell.type == 'link') {
          to = {
            type: 'data',
            id: nextCell.id,
            reference: nextCell.eventId
          };
        } else {
          to = {
            type: 'model',
            id: nextCell.id,
            reference: nextCell.md5
          };
        }

        dependency = {
          id: edge.id,
          name: `${from.type}: ${beforeCell.name} to ${to.type}: ${nextCell.name} denpendency`,
          from: from,
          to: to
        };

        dependencyList.push(dependency);
      });

      action.dependencyList = dependencyList;
      console.log('action', action);
      return action;
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue
    }
  },

  async mounted() {
    await this.$refs.modelBar.initSetTimeOut();
    await this.init();
  }
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
  height: 100%;
  // display: flex;
  // position: relative;

  .leftBar {
    // position: relative;
    width: 300px;
<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/MxGraph.vue
    float: left;
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
=======
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph.vue

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
      margin-bottom: 10px;

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
      background: rgb(251, 251, 251) url('./../../../../../assets/images/mxgraph/point.gif') 0 0
        repeat;
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
}
</style>
