<template>
  <div class="main">
<<<<<<< HEAD
    <div class="leftBar" v-if="role == 'builder'">
      <el-input placeholder="Search model/tool" size="mini" class="search_input">
        <el-button slot="append" icon="el-icon-search"></el-button>
      </el-input>

      <vue-scroll style="height: calc(86vh); width: 100%" :ops="ops">
        <el-collapse v-model="activeNames" class="collapse">
          <el-collapse-item title="General" name="general">
            <general-toolbar ref="generalBar"></general-toolbar>
            <!-- <code-toolbar ref="codeBar"></code-toolbar> -->
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
=======
    <div class="leftBar">
      <div class="modelbarContainer">
        <el-row>
          <all-models
            ref="allModels"
            @getPersonalModels="getPersonalModels"
            @getPublicModels="getPublicModels"
          ></all-models>
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
              ref="dataItem"
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
    </div>
    <div class="mainContainer">
      <div class="modelbarTop">
        <el-button @click="generateAction" type="text" size="mini">Export as Picture</el-button>
        <!-- <el-button @click="exportGraph" type="text" size="mini">Export as XML</el-button>
        <input @change="readFile" ref="importInput" class="hide" type="file" />
        <el-button @click="importGraphFile" type="text" size="mini">Import mxGraph</el-button> -->
        <el-button
          @click="checked ? deleteCells() : deleteCellsConfirmDialog()"
          type="text"
          size="mini"
          :disabled="selectionCells.length == 0"
        >
>>>>>>> parent of f11cd19 (mxgraph)
          Delete
        </el-button>
        <el-button @click="undo" type="text" size="mini">Undo</el-button>
        <el-button @click="redo" type="text" size="mini">Redo</el-button>
        <!-- 创建一个新的画布  均为create-->
        <el-button @click="showCreateNewTask" type="success" size="mini">New Task</el-button>
        <!-- 保存现有的画布 均为update-->
        <el-button @click="updateGraph" type="warning" size="mini">Save Task</el-button>
        <el-button @click="runGraph" size="mini">Run Task</el-button>
<<<<<<< HEAD
        <el-button @click="compareResult" size="mini">Compare Result</el-button>

        <transition name="fade" mode="out-in">
          <div class="bell" @click="setAsSelectTaskInConstruction">
            <i :class="isSelectTaskInConsruction ? 'el-icon-message-solid' : ' el-icon-bell'"></i>
          </div>
        </transition>

        <integrate-tasks @selectTask="selectTask" style="float:right"></integrate-tasks>
=======
        <integrate-tasks @selectTask="selectTask" style="float: left"></integrate-tasks>
>>>>>>> parent of f11cd19 (mxgraph)
      </div>
      <vue-scroll style="height: 800px; width: calc(100%)" :ops="ops">
        <div class="graphContainer" ref="container"></div>
      </vue-scroll>
<<<<<<< HEAD
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
=======

      <!-- <div class="outline-wrapper">
        <h4>导航器</h4>
        <div id="graphOutline" />
      </div> -->
    </div>

    <div class="rightBar">
      <integrate-task-instances :currentTaskInit="currentTask"></integrate-task-instances>
>>>>>>> parent of f11cd19 (mxgraph)
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
<<<<<<< HEAD

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
>>>>>>> parent of f11cd19 (mxgraph)
  </div>
  <!-- </div> -->
</template>

<script>
<<<<<<< HEAD
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
// import codeToolbar from '_com/MxGraphBars/CodeToolbar';
import dataProcessingToolbar from '_com/MxGraphBars/DataProcessingToolbar';
import { generalList, codeList } from '_com/MxGraphBars/toolbar';
import comparison from './Comparison';

const {
  // mxGraph,
  mxEvent,
  mxUtils,
  mxCodec
=======
import mxgraph from '@/components/MxGraph/index';

import FileSaver from 'file-saver';
import allModels from './AllModels';
import { get, post, patch } from '@/axios';
import dataItemToolbar from './../../Construction/DataItemToolbar';
import dataItemInfo from './../../Construction/DataItemInfo';
import integrateTasks from './../../Construction/IntegrateTasks';
import integrateTaskInstances from './IntegrateTaskInstances';
import { mapState } from 'vuex';

const {
  mxGraph,
  mxOutline,
  mxEvent,
  mxUtils,
  mxRubberband,
  mxConstants,
  mxCellState,
  mxCellEditor,
  mxCodec,
  mxUndoManager
  // mxRubberBand
>>>>>>> parent of f11cd19 (mxgraph)
} = mxgraph;

export default {
  props: {
<<<<<<< HEAD
=======
    sendXml: {
      type: String
    },
>>>>>>> parent of f11cd19 (mxgraph)
    taskInfoInit: {
      type: Object
    }
  },

  components: {
<<<<<<< HEAD
    modelItemToolbar,
    dataItemToolbar,
    dataCellInfo,
    integrateTasks,
    instanceCard,
    generalToolbar,
    // codeToolbar,
    dataProcessingToolbar,
    comparison
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
          this.graph.importGraph(val.note);
=======
    allModels,
    dataItemToolbar,
    dataItemInfo,
    integrateTasks,
    integrateTaskInstances
  },

  watch: {
    sendXml: {
      handler(val) {
        if (val != '') {
          this.getXml = val;
          this.importGraph(this.getXml);
        }
      },
      deep: true
    },
    taskInfoInit: {
      handler(val) {
        if (JSON.stringify(val) != '{}') {
          console.log('initTak', val);
          this.currentTask = val;
>>>>>>> parent of f11cd19 (mxgraph)
        }
      },
      deep: true
    }
  },

  computed: {
<<<<<<< HEAD
    generalList: () => generalList, // general toolbar
    codeList: () => codeList,
    ...mapState({
      role: state => state.permission.role
=======
    ...mapState({
      userId: state => state.user.userId,
      userName: state => state.user.name
>>>>>>> parent of f11cd19 (mxgraph)
    })
  },

  data() {
    return {
      projectId: this.$route.params.id,
<<<<<<< HEAD

      graph: null,
      editCellVisible: false,

=======
      contentHeight: 0,
      graph: null,
      editCellVisible: false,
      // graphXml: "",
      selectEdge: {},
      selectVertex: {},
>>>>>>> parent of f11cd19 (mxgraph)
      selectionCells: [],
      deleteCellsVisible: false,
      checked: false,
      undoMng: null,

      cellForm: {
        name: ''
      },
<<<<<<< HEAD

      getXml: this.sendXml,
      //modelbar
      modelItemList: [],
=======
      cellFormRules: {
        name: [
          {
            required: true,
            message: 'The name cannot be empty',
            trigger: 'blur'
          }
        ]
      },
      getXml: this.sendXml,
      //modelbar
      modelItemList: [],
      publicModels: [],
      personalModels: [],
>>>>>>> parent of f11cd19 (mxgraph)

      doi: '',
      cell: {}, //双击事件 cell
      state: {},
      inputItemList: [],
      outputItemList: [],
<<<<<<< HEAD

=======
      mxGraphSelectionModel: {},
      nodeActiveName: 'info',
>>>>>>> parent of f11cd19 (mxgraph)
      dataNode: {},

      modelClick: false,
      modelDoubleClick: false,
<<<<<<< HEAD

      dataClick: false,
      dataDoubleClick: false,

      codeClick: false,
      codeDoubleClick: false,

=======
      dataClick: false,
      dataDoubleClick: false,
>>>>>>> parent of f11cd19 (mxgraph)
      modelListInGraph: [],
      dataInputInGraph: [],
      dataLinkInGraph: [], //下一模型的输入数据
      dataOutputInGraph: [],
      linkEdgeList: [],

      stateList: [],
<<<<<<< HEAD
      // dataItemModelbarKey: 0,

=======
      dataItemModelbarKey: 0,
>>>>>>> parent of f11cd19 (mxgraph)
      taskInfo: {
        taskName: '',
        taskDescription: ''
      },

      isNewTaskContainerShow: true,

      currentTask: {},

      //document
      nodeList: [],
<<<<<<< HEAD
=======
      modelItemList2: [],
>>>>>>> parent of f11cd19 (mxgraph)
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
<<<<<<< HEAD

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
      // this.initLeftBar('codeBar');
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
=======
      oldScrollTop: 0,
      oldScrollLeft: 0
    };
  },

  methods: {
    async init() {
      this.container = this.$refs.container;

      this.createGraph();
      this.initConnectStyle();
      this.initGraph();

      this.initUndoManager();

      this.setDefaultEdgeStyle();

      this.listenGraphEvent();

      this.getGraphXml();

      this.initModelbar('modelItemList');
      // this.initPopupMenu();
    },

    //--------------初始化 bar的modelItem的内容--由 AllModels组件返回
    getPublicModels(val) {
      this.$set(this, 'publicModels', val);
      this.modelItemList = this.publicModels;
    },

    getPersonalModels(val) {
      this.$set(this, 'personalModels', val);
      this.modelItemList = this.personalModels;
    },

    getInAndOut(input, output) {
      // this.state = val;
      this.inputItemList = input;
      this.outputItemList = output;
      this.initModelbar('inputItemList');
      this.initModelbar('outputItemList');
    },

    // Creates the graph inside the given container
    createGraph() {
      this.graph = new mxGraph(this.$refs.container);
    },

    initConnectStyle() {
      //允许连线
      this.graph.setConnectable(true);

      // 编辑时按回车键不换行，而是完成输入
      this.graph.setEnterStopsCellEditing(true);

      // 可修改
      this.graph.setCellsEditable(true);

      // 从工具栏拖动到目标细胞时细胞边界是否产生光圈
      this.graph.setDropEnabled(true);

      // 禁止游离线条
      this.graph.setAllowDanglingEdges(false);
      this.graph.setDisconnectOnMove(false);

      this.graph.setConnectableEdges(false);

      //设置线条弯曲程度
      this.graph.setCellsBendable(true);

      // 禁止节点折叠
      this.graph.foldingEnabled = false;

      // 文本包裹效果必须开启此配置
      this.graph.setHtmlLabels(true);

      // 拖拽过程对齐线
      this.graph.graphHandler.guidesEnabled = true;

      // 容器大小自适应
      this.graph.setResizeContainer(true);

      // 重复连接
      this.graph.setMultigraph(true);

      // Enables rubberband selection
      new mxRubberband(this.graph);

      this.graph.popupMenuHandler.autoExpand = true;

      mxCellEditor.prototype.blurEnabled = true;
      // this.mxEvent.disableContextMenu(this.document.body);
    },

    initGraph() {
      this.graph.convertValueToString = cell => {
        // 从value中获取显示的内容
        return cell.name;
      };
      new mxOutline(this.graph, document.getElementById('graphOutline'));
      // let outline = new mxOutline(this.graph, document.getElementById('graphOutline'));
      // console.log(outline);
      this.setCursor();
    },

    initUndoManager() {
      this.undoMng = new mxUndoManager();

      let listen = (sender, evt) => {
        this.undoMng.undoableEditHappened(evt.getProperty('edit'));
      };

      this.graph.getModel().addListener(mxEvent.UNDO, listen);
      this.graph.getView().addListener(mxEvent.UNDO, listen);
    },

    //设置为折线
    setDefaultEdgeStyle() {
      const style = this.graph.getStylesheet().getDefaultEdgeStyle();
      Object.assign(style, {
        [mxConstants.STYLE_ROUNDED]: true, // 设置线条拐弯处为圆角
        [mxConstants.STYLE_STROKEWIDTH]: '1',
        [mxConstants.STYLE_STROKECOLOR]: '#333333',
        [mxConstants.STYLE_EDGE]: mxConstants.EDGESTYLE_ORTHOGONAL,
        [mxConstants.STYLE_FONTCOLOR]: '#333333',
        [mxConstants.STYLE_LABEL_BACKGROUNDCOLOR]: '#ffa94d'
      });

      // 设置拖拽线的过程出现折线，默认为直线
      this.graph.connectionHandler.createEdgeState = () => {
        const edge = this.graph.createEdge();
        return new mxCellState(this.graph.view, edge, this.graph.getCellStyle(edge));
      };
    },

    initPopupMenu() {
      // console.log(document.body);
      mxEvent.disableContextMenu(document.body);
      this.graph.popupMenuHandler.autoExpand = true;
>>>>>>> parent of f11cd19 (mxgraph)
    },

    listenGraphEvent() {
      // 监听双击事件
      this.graph.addListener(mxEvent.DOUBLE_CLICK, async (graph, evt) => {
        // DOUBLE_CLICK
<<<<<<< HEAD
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
=======
        let cell = { doi: evt.properties.cell.doi, style: evt.properties.cell.style };

        let clickModelType = cell.style.includes('modelType');
        // let dataType = cell.style.includes('data');
        if (clickModelType) {
          this.dataItemModelbarKey++;
          // console.log(this.dataItemModelbarKey);
          await this.$refs.dataItem.initSetTimeOut();
          this.modelDoubleClick = true;
          // this.$set(this, 'cell', cell);
          this.cell = JSON.parse(JSON.stringify(cell));
          // this.cell = cell;
          this.dataDoubleClick = this.dataClick = this.modelClick = false;
        }
        // else if (dataType) {
        //   this.dataDoubleClick = true;
        //   this.dataNode = cell;
        //   this.modelDoubleClick = this.modelClick = this.dataClick = false;
        // }
>>>>>>> parent of f11cd19 (mxgraph)
      });

      // 监听单击事件
      //单击空白处 dialog隐藏
      this.graph.addListener(mxEvent.CLICK, (sender, evt) => {
        let isCell = Object.prototype.hasOwnProperty.call(evt.properties, 'cell');
        if (isCell) {
          let cell = evt.properties.cell;

<<<<<<< HEAD
          const clickModelType = cell.type;

          if (clickModelType == 'model') {
=======
          const clickModelType = cell.style.includes('modelType'); //是否单机Model组件
          const dataType = cell.style.includes('data'); //是否单击data组件

          if (clickModelType) {
>>>>>>> parent of f11cd19 (mxgraph)
            // 使用 mxGraph 事件中心，触发自定义事件
            // this.cell = cell;
            this.modelClick = true;
            this.modelDoubleClick = this.dataClick = this.dataDoubleClick = false;
<<<<<<< HEAD
          } else {
=======
          } else if (dataType) {
>>>>>>> parent of f11cd19 (mxgraph)
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

<<<<<<< HEAD
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
=======
      //增加连接线
      this.graph.addListener(mxEvent.CELLS_ADDED, (sender, evt) => {
        const cell = evt.properties.cells[0];
        // if (this.graph.isPart(cell)) {
        //   return;
        // }

        if (cell.vertex) {
          this.$message.info('Add a node');
        } else if (cell.edge) {
          //判断是否是link to next dataitem
          let linkCell = cell.target;
          if (linkCell.style.includes('dataInputType')) {
            this.graph.getModel().beginUpdate();
            linkCell.type = 'link';
            let styleObj = {
              dataOutputType: '',
              fillColor: '#E6A23C',
              strokeColor: '',
              strokeWidth: '1.5',
              shape: 'rectangle',
              align: mxConstants.ALIGN_CENTER,
              // verticalAlign: mxConstants.ALIGN_,
              imageAlign: mxConstants.ALIGN_CENTER,
              imageVerticalAlign: mxConstants.ALIGN_TOP
            };
            if (linkCell.optional == 'false' || linkCell.optional == 'False') {
              styleObj.strokeColor = '#d13030';
            } else {
              styleObj.strokeColor = 'rgb(200, 200, 200)';
            }

            const style = Object.keys(styleObj)
              .map(attr => `${attr}=${styleObj[attr]}`)
              .join(';');
            this.graph.getModel().setStyle(linkCell, style);
            // const styleDict = this.getStyleDict(cell);
            // console.log(styleDict);

            // try {
            //   cell.target.style.replace('dataInputType', 'dataLinkType');
            //   cell.target.style.replace('#fff8f8', '#E6A23C');
            // } finally {
            this.graph.getModel().endUpdate();
            // }
          }
          this.$message.info('Add a line');
>>>>>>> parent of f11cd19 (mxgraph)
        }
      });

      // 监听 mxGraph 事件
      this.mxGraphSelectionModel = this.graph.getSelectionModel();
      this.mxGraphSelectionModel.addListener(mxEvent.CHANGE, this.handleSelectionChange);
    },

<<<<<<< HEAD
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
=======
    initModelbar(panel) {
      let refType;
      let listName = [];
      let styleIn = {};
      if (panel == 'modelItemList') {
        refType = 'allModels';
        listName = this.modelItemList;
        styleIn = {
          modelType: '',
          fillColor: '#f8f5ec',
          shape: 'rectangle'
        };
      } else if (panel == 'inputItemList') {
        refType = 'dataItem';
        listName = this.inputItemList;
        styleIn = {
          dataInputType: '',
          fillColor: '#fff8f8',
          shape: 'rectangle'
        };
      } else if (panel == 'outputItemList') {
        refType = 'dataItem';
        listName = this.outputItemList;
        styleIn = {
          dataOutputType: '',
          fillColor: '#f0f8ff',
          shape: 'rectangle'
        };
      }

      const domArray = this.$refs[refType].$refs[panel];
>>>>>>> parent of f11cd19 (mxgraph)

      if (!(domArray instanceof Array) || domArray.length <= 0) {
        return;
      }

      domArray.forEach((dom, domIndex) => {
<<<<<<< HEAD
        const dragItem = barItemList[domIndex];

        let cellStyle = getCellStyle(styleIn, dragItem);

        const dropHandler = (graph, evt, cell, x, y) => {
          this.addCell(dragItem, x, y, panel, cellStyle);
=======
        const modelItem = listName[domIndex];

        console.log(modelItem);
        const dropHandler = (graph, evt, cell, x, y) => {
          console.log(evt);
          this.addCell(modelItem, x, y, panel, styleIn);
>>>>>>> parent of f11cd19 (mxgraph)
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

<<<<<<< HEAD
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
=======
    addCell(item, x, y, type, styleIn) {
      // console.log(item);
      let styleObj = {
        ...styleIn,
        strokeColor: 'rgb(200, 200, 200)',
        strokeWidth: '1.5',
        shape: 'rectangle',
        align: mxConstants.ALIGN_CENTER,
        // verticalAlign: mxConstants.ALIGN_,
        imageAlign: mxConstants.ALIGN_CENTER,
        imageVerticalAlign: mxConstants.ALIGN_TOP
      };
      if (item.type == 'response' && item.optional == 'false') {
        styleObj.strokeColor = '#d13030';
      }

      this.graph.getModel().beginUpdate();

      try {
        if (type == 'modelItemList') {
          let vertex = this.addCellToContainer(styleObj, x, y);
>>>>>>> parent of f11cd19 (mxgraph)
          vertex.name = item.name;
          vertex.doi = item.doi;
          vertex.type = 'model';
          vertex.iterationNum = '1';
<<<<<<< HEAD
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
=======
        } else {
          let selectionCell = this.selectionCells[0];
          let vertex = this.addCellToContainer(styleObj, x, y);
>>>>>>> parent of f11cd19 (mxgraph)

          vertex.name = item.name;
          vertex.eventId = item.eventId;
          vertex.description = item.description; //event description
<<<<<<< HEAD
          vertex.datasetItem = item.datasetItem;
=======
>>>>>>> parent of f11cd19 (mxgraph)
          vertex.stateId = item.stateId; //event description
          vertex.stateName = item.stateName;
          vertex.stateDescription = item.stateDescription;
          vertex.md5 = item.md5;
          vertex.doi = item.doi;
          vertex.optional = item.optional;
          vertex.linkModelCellId = selectionCell.id; //放置输入输出node时 与其关联的model的nodeId
<<<<<<< HEAD
          vertex.type = item.type;

          if (type == 'inputBar') {
            this.addEdge(vertex, selectionCell);
          } else if (type == 'outputBar') {
=======

          if (type == 'inputItemList') {
            vertex.type = 'input';
            this.addEdge(vertex, selectionCell);
          } else if (type == 'outputItemList') {
            vertex.type = 'output';
>>>>>>> parent of f11cd19 (mxgraph)
            this.addEdge(selectionCell, vertex);
          }
        }
      } finally {
        this.graph.getModel().endUpdate();
      }
    },

<<<<<<< HEAD
=======
    addCellToContainer(styleObj, x, y) {
      const style = Object.keys(styleObj)
        .map(attr => `${attr}=${styleObj[attr]}`)
        .join(';');

      return this.graph.insertVertex(
        this.graph.getDefaultParent(),
        null,
        null,
        x,
        y,
        200, //width
        50, //height
        style
      );
    },

>>>>>>> parent of f11cd19 (mxgraph)
    addEdge(source, target) {
      this.graph.insertEdge(this.graph.getDefaultParent(), null, null, source, target, null);
    },

<<<<<<< HEAD
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
=======
    getGraphXml() {
      let encoder = new mxCodec();
      let graphXml = encoder.encode(this.graph.getModel());
      let xml = mxUtils.getPrettyXml(graphXml);
      // console.log(xml);
      return xml;
      // console.log(this.graphXml, this.graph.getModel());
    },

    exportGraph() {
      let graphXml = this.getGraphXml();
      // console.log(graphXml);
      let xml = mxUtils.getPrettyXml(graphXml);
      const blob = new Blob([xml], {
        type: 'text/plain;charset=utf-8'
      });
      FileSaver.saveAs(blob, 'mxgraph.xml');
    },

    importGraphFile() {
      this.$refs['importInput'].click();
    },

    readFile(evt) {
      const file = evt.target.files[0];
      const reader = new FileReader();
      reader.onload = e => {
        const txt = e.target.result;
        this.importGraph(txt);
        this.getCells();
      };
      reader.readAsText(file);
    },

    importGraph(xmlTxt) {
      //xml to json
      this.graph.getModel().beginUpdate();
      try {
        const doc = mxUtils.parseXml(xmlTxt);
        const root = doc.documentElement;
        const dec = new mxCodec(root.ownerDocument);
        dec.decode(root, this.graph.getModel());
        this.getCells();
      } finally {
        this.graph.getModel().endUpdate();
      }
      this.restoreModel();
    },

    restoreModel() {
      Object.values(this.graph.getModel().cells).forEach(cell => {
        if (cell.vertex && cell.data) {
          cell.data = JSON.parse(cell.data);
        }
      });
>>>>>>> parent of f11cd19 (mxgraph)
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
<<<<<<< HEAD
=======
      // console.info("后退的Cells", this.getUndoRedoCell());
>>>>>>> parent of f11cd19 (mxgraph)
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
<<<<<<< HEAD
      // let data = await post(`/integrateTasks`, postJson);

      let data = await saveIntegrateTask(postJson);
=======

      let data = await saveIntegrateTasks(postJson);
      // let data = await post(`/integrateTasks`, postJson);

>>>>>>> parent of f11cd19 (mxgraph)
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
<<<<<<< HEAD

      //王梓欢配置文件
      let xml = generateXml(this.currentTask.taskName, this.modelListInGraph, this.dataInputInGraph, this.dataLinkInGraph, this.dataOutputInGraph, this.linkEdgeList);

      //mxgraph xml文件
      let graphXml = this.graph.getGraphXml();

      //model action
      let action = generateAction(this.currentTask.id, this.modelListInGraph, this.dataInputInGraph, this.dataLinkInGraph, this.dataOutputInGraph, this.linkEdgeList, 'task');

      let postJson = {
        xml: xml,
        note: graphXml,
=======
      await this.getProcessDoc();

      //王梓欢配置文件
      let xml = this.generateXml();

      //mxgraph xml文件
      let graphXml = this.getGraphXml();

      //model action
      let action = this.generateAction();

      let postJson = {
        xml: xml,
        mxgraph: graphXml,
>>>>>>> parent of f11cd19 (mxgraph)
        // modelActions: modelActions,modelACTIONlIST
        taskName: this.currentTask.taskName,
        taskDescription: this.currentTask.taskDescription,
        action: action
      };
<<<<<<< HEAD

      await updateIntegrateTask(this.currentTask.id, postJson);
      let content = { content: 'Simulation Scenario', degree: '100%', type: 'success', icon: 'el-icon-sunny' };

      await updatePerformanceById('context', this.projectId, content);
      // await patch(`/integrateTasks/${this.currentTask.id}`, postJson);
      // console.log(data);
=======
      // console.log(postJson);

      let data = await updateIntegrateTasks(this.currentTask.id, postJson);
      console.log(data);
>>>>>>> parent of f11cd19 (mxgraph)
    },

    selectTask(val) {
      this.currentTask = val;
      // this.taskInfo = val;
<<<<<<< HEAD
      let note = val.note;
      this.importModelXML(note);
=======
      let mxgraph = val.mxgraph;
      this.importModelXML(mxgraph);
>>>>>>> parent of f11cd19 (mxgraph)
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

<<<<<<< HEAD
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
>>>>>>> parent of f11cd19 (mxgraph)
      let file = new File([xml], this.currentTask.taskName + '.xml', {
        type: 'text/xml'
      });
      let formData = new FormData();
      formData.append('file', file);

      //get tid from manager server
<<<<<<< HEAD
      let tid = await runtask(formData);

      await this.addTaskInstance(tid);
=======
      let tid = await post(`/managerServer/runtask`, formData);
>>>>>>> parent of f11cd19 (mxgraph)

      await this.getOutputs(tid);

      //save to  the instance
    },

    async getOutputs(tid) {
      //获得结果
<<<<<<< HEAD

=======
>>>>>>> parent of f11cd19 (mxgraph)
      this.record = {};
      this.timer = setInterval(async () => {
        if (this.record.status == 1) {
          clearInterval(this.timer);
          await this.putOutputToCell();
<<<<<<< HEAD

          return;
        } else {
          let data = await checkTaskStatus(tid);
          this.record = data;
          this.changeCellColor(data);
=======
          await this.addTaskInstances();
          return;
        } else {
          let data = await get(`/managerServer/checkTaskStatus/${tid}`);
          this.record = data;
>>>>>>> parent of f11cd19 (mxgraph)
        }
      }, 3000);
    },

<<<<<<< HEAD
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
=======
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
        address: response.value,
        suffix: response.suffix,
        description: '',
        userUpload: false //--false是中间数据
      };
<<<<<<<< HEAD:reproducibility_font/src/components/Scenario/components/1.vue
      let data = await post(`/r/fileItems`, dataJson);
========

      let data = await post(`/r/dataItems`, dataJson);
>>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/components/MxGraph copy.vue
      if (data != undefined) {
        this.$message({
          message: 'You have submit the related data resource successfully',
          type: 'success'
        });
      }
    },

    async addTaskInstances() {
      let action = this.generateAction();

      let postJson = {
        taskId: this.currentTask.id,
        action: action,
        status: this.record.status,
        tid: this.record.taskInfo.taskId
      };
      // console.log(postJson);
      let data2 = await post(`/integrateTaskInstances`, postJson);
      console.log(data2);
>>>>>>> parent of f11cd19 (mxgraph)
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

<<<<<<< HEAD
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
=======
    getCells() {
      // this.modelListInGraph = this.dataOutputInGraph = this.dataInputInGraph = this.dataLinkInGraph = [];
      let modelListInGraph = [];
      let dataOutputInGraph = [];
      let dataInputInGraph = [];
      let dataLinkInGraph = [];
      // let dataLineInputInGrapg = [];

      Object.values(this.graph.getModel().cells).forEach(cell => {
        if (cell.style != undefined) {
          if (cell.style.includes('modelType')) {
            modelListInGraph.push(cell);
          } else if (cell.style.includes('dataOutputType')) {
            cell.type = 'output';
            dataOutputInGraph.push(cell);
          } else if (cell.style.includes('dataInputType')) {
            cell.type = 'input';
            dataInputInGraph.push(cell);
          }
        }
      });

      // console.log(this.graph.getModel().cells);
      let links = Object.values(this.graph.getModel().cells).filter(cell =>
        Object.prototype.hasOwnProperty.call(cell, 'edge')
      );
      this.linkEdgeList = links;

      this.modelListInGraph = modelListInGraph;
      this.dataOutputInGraph = dataOutputInGraph;
      this.dataInputInGraph = dataInputInGraph;
      this.dataLinkInGraph = dataLinkInGraph;
    },

    async getProcessDoc() {
      let nodeList = [];
      let modelItemList2 = [];
      let dataItemList = [];

      // let modelListInGraph = this.modelListInGraph;
      // let dataOutputInGraph = this.dataOutputInGraph;
      let dataInputInGraph = this.dataInputInGraph;
      // let dataLinkInGraph = this.dataLinkInGraph;
      let linkEdgeList = this.linkEdgeList;

      await Promise.all(
        this.modelListInGraph.map(async model => {
          //GET NODE
          let node = {};
          node.name = model.name;
          node.description = model.description; //model description
          node.type = 'simulationDeduction'; //dataProcessing/simulationDeduction/visualization/resultAnalysis
          // node.step = model.id;
          node.ref = model.doi; //model doi//除了wzh integratedtask之外 其他均用doi作为标识符
          nodeList.push(node);

          //GET MODEL INFOMATION
          let data = await get(`/portal/modelBehavior/${model.doi}`);
          model.md5 = data.md5; //integrate task

          //get modelItemList2

          let modelItem = {
            name: data.name,
            descriprion: data.description,
            modelRef: model.doi,
            nodeRef: model.id,
            stateList: []
          };

          //GET ALL RELATED EVENT
          let eventList = [];
          // let dataItemList = [];
          linkEdgeList.forEach(link => {
            if (link.target.id == model.id && link.source.style.includes('dataInputType')) {
              let event = {
                name: link.source.name,
                dataRef: link.source.value,
                stateName: link.source.stateName,
                stateDescription: link.source.stateDescription,

                type: 'input',
                datasetItem: link.target.datasetItem
              };

              eventList.push(event);

              if (link.source.value == undefined) {
                return;
              }
            }
            if (link.source.id == model.id && link.target.style.includes('dataOutputType')) {
              let event = {
                name: link.target.name,
                dataRef: link.target.value,
                stateName: link.target.stateName,
                stateDescription: link.source.stateDescription,
                type: 'output',
                datasetItem: link.target.datasetItem
              };
              eventList.push(event);
            }
          });

          //获得 这个model的所有state
          let stateInfoList = data.mdlJson.ModelClass[0].Behavior[0].StateGroup[0].States[0].State;
          let stateInfos = [];
          stateInfoList.forEach(state => {
            let { Event, ...list } = state;
            console.log(Event);
            list.Event = [];
            stateInfos.push(list);
          });

          //将已有的event塞入其中
          stateInfos.forEach(state => {
            eventList.forEach(item => {
              if (state.name == item.stateName) {
                state.Event.push(item);
              }
            });
          });
          modelItem.stateList = stateInfos;
          modelItemList2.push(modelItem);
        })
      );

      console.log(dataInputInGraph);
      dataInputInGraph.forEach(input => {
        if (input.fileId == '' || input.fileId == undefined) {
          return;
        }
        let dataItem = {
          name: input.fileName,
          value: input.value,
          id: input.fileId
        };
        dataItemList.push(dataItem);
      });

      let method = {
        userId: this.userId,
        pid: this.projectId,
        nodeList: nodeList,
        modelCollection: modelItemList2,
        dataCollection: dataItemList
      };
      console.log(method);
      // let data = await post(`/methods`, method);
      // console.log(data);
      //modelItem
    },

    generateXml() {
      let version = '1.0';
      let uid = this.generateGUID();
      let name = this.currentTask.taskName;

      let xml = `<TaskConfiguration uid='${uid}' name='${name}' version='${version}'>`;

      xml += '<Models>';

      //没有md5-->只有doi  xml += `<Model name='${model.name}' pid='${model.md5}' description='' doi='${model.doi}'/>`;
      this.modelListInGraph.forEach(model => {
        xml += `<Model name='${model.name}' description='' pid='${model.md5}'/>`;
      });
      xml += `</Models>`;

      //modelAction标签
      xml += `<ModelActions>`;

      this.modelListInGraph.forEach(model => {
        xml += `<ModelAction id='${model.id}' name = '${model.name}' description = ''
        model='${model.md5}' iterationNum='${model.iterationNum}'>`;

        let inputList = this.dataInputInGraph.filter(event => event.md5 == model.md5);
        let outputList = this.dataOutputInGraph.filter(event => event.md5 == model.md5);

        xml += `<Inputs>`;
        inputList.forEach(item => {
          xml += `<DataConfiguration id='${item.eventId}' state='${item.stateName}' event='${item.name}'>`;

          if (item.type == 'input') {
            xml += `<Data value='${item.value}' type="url"/>`;
          } else if (item.type == 'link') {
            let link = this.linkEdgeList.filter(el => el.target.eventId == item.eventId);
            xml += `<Data link='${link[0].target.eventId}' type="link"/>`;
          }
          xml += `</DataConfiguration>`;
        });
        xml += `</Inputs>`;
        xml += `<Outputs>`;

        outputList.forEach(item => {
          xml += `<DataConfiguration id='${item.eventId}' state='${item.stateName}' event='${item.name}' />`;
        });

        xml += '</Outputs></ModelAction>';
      });
      xml += '</ModelActions></TaskConfiguration>';
      // console.log(xml);
      return xml;
    },

    generateGUID() {
      let s = [];
      let hexDigits = '0123456789abcdef';
      for (let i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
      }
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
          modelRef: model.md5,
          name: model.name,
          description: model.description,
          behavior: {}
        };

        let inputList = this.dataInputInGraph.filter(event => event.md5 == model.md5);
        let outputList = this.dataOutputInGraph.filter(
          event => event.md5 == model.md5 && event.value != null
        );

        let stateList = [];
        let state = {
          id: inputList[0].stateId,
          name: inputList[0].stateName,
          description: inputList[0].stateDescription
        };
        let eventList = [];

        inputList.forEach(item => {
          let inputData = {
            id: item.eventId,
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

        outputList.forEach(item => {
          let output = {
            id: item.eventId,
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
        stateList.push(state);
        modelItem.behavior.stateList = stateList;
        // this.$set(modelItem, 'stateList', stateList);
        modelList.push(modelItem);
      });

      action.modelItemList = modelList;
      //dataItemList
      let inputList = this.dataInputInGraph.filter(
        event => Object.prototype.hasOwnProperty.call(event, 'value') && event.value != ''
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
      // console.log(addModelActions);

      return action;
    }
  },
  async mounted() {
    await this.$refs.allModels.initSetTimeOut();
    await this.init();
  }
>>>>>>> parent of f11cd19 (mxgraph)
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
  height: 100%;
<<<<<<< HEAD
  // display: flex;
  // position: relative;

  .leftBar {
    // position: relative;
    width: 300px;
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
  display: flex;
  position: relative;

  .leftBar {
    position: relative;
    width: 300px;
>>>>>>> parent of f11cd19 (mxgraph)

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

<<<<<<< HEAD
  .mainContainer {
    float: left;
    // position: relative;
    // top: 0;
    // left: 300px;
    width: calc(100% - 505px);
=======
  .rightBar {
    position: relative;
    right: 0;
    width: 200px;
    margin-left: 5px;
    padding: 0 5px;
    box-shadow: 0px 0px 5px rgb(207, 207, 207);
    background-color: rgba(243, 243, 243, 0.9);
  }

  .mainContainer {
    position: relative;
    // top: 0;
    // left: 300px;
    width: calc(100% - 500px);
>>>>>>> parent of f11cd19 (mxgraph)
    height: 100%;
    .modelbarTop {
      background: rgb(251, 251, 251);
      padding-left: 10px;
      border-radius: 4px;
<<<<<<< HEAD
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
=======
      margin-bottom: 10px;
>>>>>>> parent of f11cd19 (mxgraph)
    }
    .graphContainer {
      position: relative;
      overflow: hidden;
      height: 100%;
      width: 100%;
      min-width: calc(100%);
      min-height: 800px;
<<<<<<< HEAD
      background: rgb(251, 251, 251) url('./../../../assets/images/mxgraph/point.gif') 0 0 repeat;
=======
      background: rgb(251, 251, 251) url('./../../../../../assets/images/mxgraph/point.gif') 0 0
        repeat;
>>>>>>> parent of f11cd19 (mxgraph)
      border-radius: 4px;
    }
  }

<<<<<<< HEAD
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

=======
>>>>>>> parent of f11cd19 (mxgraph)
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
<<<<<<< HEAD
  .dialogs {
    /deep/ .el-dialog__body {
      padding: 20px;
    }
  }
=======
>>>>>>> parent of f11cd19 (mxgraph)
}
</style>
