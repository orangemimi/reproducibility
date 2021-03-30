<template>
  <div class="main">
    <div class="modelbarContainer">
      <el-row>
        <all-models
          ref="allModels"
          @getPersonalModels="getPersonalModels"
          @getPublicModels="getPublicModels"
        ></all-models>
      </el-row>
    </div>

    <div class="mainContainer">
      <div class="modelbarTop">
        <!-- <el-button @click="updateGraph" type="text" size="mini">Output</el-button> -->
        <el-button @click="exportGraph" type="text" size="mini">Export as XML</el-button>
        <!-- <input @change="readFile" ref="importInput" class="hide" type="file" />
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
        <integrate-tasks @selectTask="selectTask" style="float: left"></integrate-tasks>
      </div>
      <vue-scroll style="height: 630px; width: 100%">
        <div class="graphContainer" ref="container"></div>
      </vue-scroll>

      <!-- <div class="outline-wrapper">
        <h4>导航器</h4>
        <div id="graphOutline" />
      </div> -->
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
        <vue-scroll style="height: 630px; width: 100%">
          <data-item-modelbar
            :cell="cell"
            ref="dataItem"
            @getInAndOut="getInAndOut"
            :key="dataItemModelbarKey"
          ></data-item-modelbar>
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
  </div>
  <!-- </div> -->
</template>

<script>
import mxgraph from '@/components/MxGraph/index';

// import nodeCard from '@/components/MxGraph/components/NodeCard';
// import editCell from '@/components/MxGraph/components/EditCell';
import FileSaver from 'file-saver';

import allModels from './AllModels';
import { get, post, patch } from '@/axios';
import dataItemModelbar from './../../Construction/DataItemToolbar';
import dataItemInfo from './../../Construction/DataItemInfo';

import integrateTasks from './../../Construction/IntegrateTasks';
import { mapState } from 'vuex';

const {
  mxGraph,
  mxOutline,
  mxEvent,
  // mxCell,
  // mxGeometry,
  mxUtils,
  // mxEventObject,
  // mxConnectionHandler,
  // mxGraphHandler,
  mxRubberband,
  mxConstants,
  mxCellState,
  mxCellEditor,
  // mxGraphView,
  mxCodec,
  mxUndoManager
  // mxCellOverlay
} = mxgraph;

export default {
  props: {
    sendXml: {
      type: String
    }
  },

  components: {
    // nodeCard,
    // editCell,
    allModels,
    dataItemModelbar,
    dataItemInfo,
    integrateTasks
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
    }
  },

  computed: {
    ...mapState({
      userId: state => state.user.userId,
      userName: state => state.user.name
    })
  },

  data() {
    return {
      projectId: this.$route.params.id,
      contentHeight: 0,
      graph: null,
      editCellVisible: false,
      // graphXml: "",
      selectEdge: {},
      selectVertex: {},
      selectionCells: [],
      deleteCellsVisible: false,
      checked: false,
      undoMng: null,

      cellForm: {
        name: ''
      },
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

      doi: '',
      cell: {}, //双击事件 cell
      state: {},
      inputItemList: [],
      outputItemList: [],
      mxGraphSelectionModel: {},
      nodeActiveName: 'info',
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
      dataItemModelbarKey: 0,
      taskInfo: {
        taskName: '',
        taskDescription: ''
      },

      isNewTaskContainerShow: true,

      currentTask: {},

      //document
      nodeList: [],
      modelItemList2: [],
      dataItemList: [],

      //task
      taskList: []
    };
  },

  methods: {
    async init() {
      await this.getIntegrateTasks();
      // await this.getMethods();
      this.initSize();
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

    async getIntegrateTasks() {
      let data = await get(`/integrateTasks/all/${this.projectId}`); //获得该项目的所有tasks
      if (data != null) {
        this.nodeList = data.nodeList;
        this.modelItemList2 = data.modelItemList2;
        this.dataItemList = data.dataItemList;
      }
    },

    // async getMethods() {
    //   let data = await get(`/methods/${this.projectId}`); //获得该项目的所有tasks
    //   if (data != null) {
    //     this.taskList = data;
    //   }
    // },

    getInAndOut(input, output) {
      // this.state = val;
      this.inputItemList = input;
      this.outputItemList = output;
      this.initModelbar('inputItemList');
      this.initModelbar('outputItemList');
    },

    initSize() {
      this.contentHeight = window.innerHeight - 310;
      // console.log(this.contentHeight);
    },

    createGraph() {
      this.graph = new mxGraph(this.$refs.container);
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

    setCursor() {},

    initPopupMenu() {
      // console.log(document.body);
      mxEvent.disableContextMenu(document.body);
      this.graph.popupMenuHandler.autoExpand = true;
    },

    listenGraphEvent() {
      // 监听双击事件
      this.graph.addListener(mxEvent.DOUBLE_CLICK, async (graph, evt) => {
        // DOUBLE_CLICK
        let cell = evt.properties.cell;
        if (!cell) {
          return;
        }
        let clickModelType = cell.style.includes('modelType');
        // let dataType = cell.style.includes('data');
        if (clickModelType) {
          this.dataItemModelbarKey++;
          // console.log(this.dataItemModelbarKey);
          await this.$refs.dataItem.initSetTimeOut();
          this.modelDoubleClick = true;
          this.cell = cell;
          this.dataDoubleClick = this.dataClick = this.modelClick = false;
        }
        // else if (dataType) {
        //   this.dataDoubleClick = true;
        //   this.dataNode = cell;
        //   this.modelDoubleClick = this.modelClick = this.dataClick = false;
        // }
      });

      // 监听单击事件
      //单击空白处 dialog隐藏
      this.graph.addListener(mxEvent.CLICK, (sender, evt) => {
        let cell = evt.properties.cell;
        if (!cell) {
          return;
        }
        const clickModelType = cell.style.includes('modelType');
        const dataType = cell.style.includes('data');
        if (clickModelType) {
          // 使用 mxGraph 事件中心，触发自定义事件
          // this.cell = cell;
          this.modelClick = true;
          this.modelDoubleClick = this.dataClick = this.dataDoubleClick = false;
        } else if (dataType) {
          this.modelDoubleClick = this.modelClick = this.dataDoubleClick = false;
          this.dataClick = true;
          // console.log(cell);
          this.dataNode = cell;
        }

        // if (evt.properties.hasOwnProperty("cell")) {
        //   this.cell = evt.properties.cell;
        //   // this.getCellInfo(this.cell);
        // } else {
        //   if (sender.isMouseDown == false) {
        //     // this.cell = {};
        //     return;
        //   }
        // }
      });

      //连接线
      // this.graph.addListener(mxEvent.CONNECT_CELL, (sender, evt) => {
      //   // const cell = evt.properties.cell;
      //   console.log(11111111111);
      // });

      // 监听 mxGraph 事件
      this.mxGraphSelectionModel = this.graph.getSelectionModel();
      this.mxGraphSelectionModel.addListener(mxEvent.CHANGE, this.handleSelectionChange);
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
      //   this.graph.setResizeContainer(true);

      // 重复连接
      this.graph.setMultigraph(true);

      // Enables rubberband selection
      new mxRubberband(this.graph);

      mxCellEditor.prototype.blurEnabled = true;
      //
      // this.mxEvent.disableContextMenu(this.document.body);

      //失焦
      //   if(this.graph.)
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

      if (!(domArray instanceof Array) || domArray.length <= 0) {
        return;
      }

      domArray.forEach((dom, domIndex) => {
        const modelItem = listName[domIndex];

        console.log(modelItem);
        const dropHandler = (graph, evt, cell, x, y) => {
          console.log(evt);
          this.addCell(modelItem, x, y, panel, styleIn);
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
      if (item.type == 'response' && (item.optional == 'False' || item.optional == 'false')) {
        styleObj.strokeColor = '#d13030';
      }

      this.graph.getModel().beginUpdate();

      try {
        if (type == 'modelItemList') {
          let vertex = this.addCellToContainer(styleObj, x, y);
          vertex.name = item.name;
          vertex.doi = item.doi;
          // vertex.step = "1";
          vertex.iterationNum = '1';
        } else {
          let selectionCell = this.selectionCells[0];
          let vertex = this.addCellToContainer(styleObj, x, y);

          vertex.name = item.name;
          vertex.eventId = item.eventId;
          vertex.stateName = item.stateName;
          vertex.md5 = item.md5;
          vertex.doi = item.doi;
          vertex.optional = item.optional;

          if (type == 'inputItemList') {
            this.addEdge(vertex, selectionCell);
          } else if (type == 'outputItemList') {
            this.addEdge(selectionCell, vertex);
          }
        }
      } finally {
        this.graph.getModel().endUpdate();
      }
    },

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

    addEdge(source, target) {
      this.graph.insertEdge(this.graph.getDefaultParent(), null, null, source, target, null);
    },

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

    initUndoManager() {
      this.undoMng = new mxUndoManager();

      let listen = (sender, evt) => {
        this.undoMng.undoableEditHappened(evt.getProperty('edit'));
      };

      this.graph.getModel().addListener(mxEvent.UNDO, listen);
      this.graph.getView().addListener(mxEvent.UNDO, listen);
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
      // console.info("后退的Cells", this.getUndoRedoCell());
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
      this.taskInfo = {
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
      let data = await post(`/integrateTasks`, postJson);
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
      await this.getCells();
      await this.getProcessDoc();
      let xml = this.generateXml();
      let graphXml = this.getGraphXml();
      // let modelActions = this.generateTaskModelInfo();
      let postJson = {
        xml: xml,
        mxgraph: graphXml,
        // modelActions: modelActions,modelACTIONlIST
        taskName: this.taskInfo.taskName,
        taskDescription: this.taskInfo.taskDescription
      };
      // console.log(postJson);
      let data = await patch(`/integrateTasks/${this.currentTask.id}`, postJson);
      console.log(data);
    },

    selectTask(val) {
      this.currentTask = val;
      this.taskInfo = val;
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

    async runGraph() {
      let xml = this.generateXml();
      let file = new File([xml], this.taskInfo.taskName + '.xml', {
        type: 'text/xml'
      });
      let formData = new FormData();
      formData.append('file', file);
      formData.append('taskName', this.taskInfo.taskName);
      let data = await post(`/integrateTasks`, formData);
      console.log(data);
    },

    currentEventWithFile(val) {
      // let dataItem = {
      //   name: val.name,
      //   value: val.value,
      //   eventId: val.eventId,
      //   isDirect: val.isDirect,
      //   reference: ''
      // };

      this.graph.getModel().beginUpdate();

      try {
        Object.values(this.graph.getModel().cells).forEach(cell => {
          if (cell.id == val.id) {
            cell.value = val.value;
          }
        });
      } finally {
        this.graph.getModel().endUpdate();
      }
    },

    async getCells() {
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
            dataOutputInGraph.push(cell);
          } else if (cell.style.includes('dataInputType')) {
            cell.type = 'url';
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
          node.step = model.id;
          node.ref = model.doi; //model doi//除了wzh integratedtask之外 其他均用doi作为标识符
          nodeList.push(node);

          //GET MODEL INFOMATION
          let data = await get(`/modelTask/ModelBehaviorOrdinary/${model.doi}`);
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
                type: 'input',
                datasetItem: link.target.datasetItem
              };
              // if (link.source.hasOwnProperty("datasetItem")) {
              //   event.datasetItem = link.source.datasetItem;
              // }

              eventList.push(event);

              if (link.source.value == undefined) {
                return;
              }
              // let dataItem={
              //   name=
              // }
            }
            if (link.source.id == model.id && link.target.style.includes('dataOutputType')) {
              let event = {
                name: link.target.name,
                dataRef: link.target.value,
                stateName: link.target.stateName,
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
      let data = await post(`/methods`, method);
      console.log(data);
      //modelItem
    },

    generateXml() {
      let version = '1.0';
      let uid = this.generateGUID();
      // let dataLinks = [];
      let xml = '';
      let name = this.taskInfo.taskName;

      xml += `<TaskConfiguration uid='${uid}' name='${name}' version='${version}'>`;

      xml += '<Models>';

      //没有md5-->只有doi       xml += `<Model name='${model.name}' pid='${model.md5}' description='' doi='${model.doi}'/>`;
      this.modelListInGraph.forEach(model => {
        xml += `<Model name='${model.name}' description='' pid='${model.md5}'/>`;
      });
      xml += `</Models>`;

      //modelAction标签
      xml += `<ModelActions>`;

      this.modelListInGraph.forEach(model => {
        xml += `<ModelAction id='${model.id}' name = '${model.name}' description = ''
        model='${model.md5} ' step ='${model.step}' iterationNum='${model.iterationNum}'>`;

        let inputList = this.dataInputInGraph.filter(event => event.md5 == model.md5);
        let outputList = this.dataOutputInGraph.filter(event => event.md5 == model.md5);

        xml += `<Inputs>`;
        inputList.forEach(item => {
          xml += `<DataConfiguration id='${item.eventId}' state='${item.stateName}' event='${item.name}'>`;

          if (item.type == 'url') {
            xml += `<Data value='${item.url}' type="${item.type}"/>`;
          } else if (item.type == 'link') {
            let link = this.linkEdgeList.filter(el => el.target.eventId == item.eventId);
            xml += `<Data link='${link[0].target.eventId}' type="${item.type}"/>`;
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
      xml += '</ModelActions>';

      //data links标签
      xml += '<DataLinks>';
      this.linkEdgeList.forEach(item => {
        xml += `<DataLink from='${item.source.eventId}'  to='${item.target.eventId}'  model='' config='' />`;
      });
      xml += '</DataLinks></TaskConfiguration>';
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

    generateTaskModelInfo() {
      let addModelActions = [];
      //拼接集成模型中的models部分
      let models = this.modelListInGraph;
      models.forEach(model => {
        let addModelAction = {
          id: model.id,
          modelOid: model.doi,
          md5: model.md5,
          name: model.name,
          description: model.description,
          outputData: [],
          inputData: [],
          step: model.step,
          iterationNum: model.iterationNum
        };

        let inputList = this.dataInputInGraph.filter(event => event.md5 == model.md5);
        let outputList = this.dataOutputInGraph.filter(event => event.md5 == model.md5);

        inputList.forEach(item => {
          let inputData = {
            eventId: item.eventId,
            data: item.data,
            eventName: item.name,
            optional: item.optional,
            stateName: item.stateName
          };
          if (item.type == 'url') {
            inputData.type = item.type;
            inputData.value = item.value;
            inputData.fileName = item.fileName; //文件名
            inputData.suffix = item.suffix; //文件类型
          } else if (item.type == 'link') {
            // let link = this.linkEdgeList.filter(el => el.target.eventId == item.eventId);
            inputData.link = item.link;
            inputData.linkEvent = item.link[0].target.eventId;
          }
          addModelAction.inputData.push();
        });

        outputList.forEach(item => {
          addModelAction.inputData.push({
            eventId: item.eventId,
            data: item.data,
            eventName: item.name,
            optional: item.optional,
            stateName: item.stateName
          });
        });
        addModelActions.push(addModelAction);
      });

      // console.log(addModelActions);
      return addModelActions;
    }
  },
  async mounted() {
    await this.$refs.allModels.initSetTimeOut();
    await this.init();
  },
  created() {
    this.initSize();
  }
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
  height: 100%;
  display: flex;
  position: relative;

  .integrateTaskContainer {
    position: relative;
    right: 0;
    width: 195px;
    padding: 0 5px;
    box-shadow: 0px 0px 5px rgb(207, 207, 207);
  }

  .modelbarContainer {
    position: relative;
    width: 200px;
    // margin: 0 5px;
    // background-color: rgb(251, 251, 251);
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
    position: absolute;
    width: 300px;
    padding: 0 5px;
    background-color: rgba(243, 243, 243, 0.5);
    z-index: auto;
    height: 90%;
    top: 60px;
    right: 0px;
  }

  .mainContainer {
    width: 100%;
    height: 100%;
    .modelbarTop {
      background: rgb(251, 251, 251);
      padding-left: 10px;
      border-radius: 4px;
      margin-bottom: 10px;
    }
    .graphContainer {
      overflow: hidden;
      height: 100%;
      width: 100%;
      min-width: 930px;
      min-height: 630px;
      background: rgb(251, 251, 251) url('./../../../../../assets/images/mxgraph/point.gif') 0 0
        repeat;
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
}
</style>
