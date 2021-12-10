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
        <!-- <el-button @click="checked ? deleteCells() : deleteCellsConfirmDialog()" type="text" size="mini" :disabled="this.graph.getSelectionCells().length == 0"> -->
        <!-- <i class="el-icon-delete" @click="checked ? deleteCells() : deleteCellsConfirmDialog()" :disabled="selectionCells.length == 0"></i> -->
        <el-divider direction="vertical"></el-divider>

        <el-dropdown trigger="click" @command="zoom">
          <el-button type="text" size="mini">
            {{ size }}%
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item :command="50">50%</el-dropdown-item>
            <el-dropdown-item :command="75">75%</el-dropdown-item>
            <el-dropdown-item :command="100">100%</el-dropdown-item>
            <el-dropdown-item :command="125">125%</el-dropdown-item>
            <el-dropdown-item :command="150">150%</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <el-divider direction="vertical"></el-divider>
        <el-button @click="zoomIn" size="mini" type="text" icon="el-icon-zoom-in"></el-button>
        <el-button @click="zoomOut" size="mini" type="text" icon="el-icon-zoom-out"></el-button>
        <el-divider direction="vertical"></el-divider>

        <el-button @click="undo" type="text" size="mini" :disabled="undoMng.indexOfNextAdd > 1 ? false : true" icon="iconfont icon-undo"></el-button>
        <el-button
          @click="redo"
          type="text"
          size="mini"
          :disabled="undoMng.indexOfNextAdd < undoMng.history.length ? false : true"
          icon="iconfont icon-redo"
        ></el-button>
        <el-divider direction="vertical"></el-divider>
        <el-button
          @click="checked ? deleteCells() : deleteCellsConfirmDialog()"
          type="text"
          size="mini"
          :disabled="selectionCells.length == 0"
          icon="el-icon-delete"
        />
        <!-- 创建一个新的画布  均为create-->
        <el-button @click="showCreateNewTask" type="success" size="mini">New Task</el-button>
        <!-- 保存现有的画布 均为update-->
        <el-button @click="updateGraph" :type="undoMng.history.length == 1 ? 'success' : 'warning'" size="mini" icon="el-icon-collection">
          {{ currentTask.taskName }}
        </el-button>
        <el-button @click="runGraph" size="mini">Run Task</el-button>
        <el-button @click="compareResult" size="mini">Compare Result</el-button>

        <!-- <el-badge is-dot class="item">{{ currentTask.taskName }}</el-badge> -->
        <el-button @click="clear" size="mini">Clear</el-button>
        <!-- <el-button @click="zoom" size="mini">zoom</el-button> -->

        <!-- <img src="/static/images/calcModel.png" alt=""> -->

        <transition name="fade" mode="out-in">
          <div class="bell" @click="setAsSelectTaskInConstruction">
            <i :class="isSelectTaskInConsruction ? 'el-icon-message-solid' : ' el-icon-bell'"></i>
          </div>
        </transition>

        <integrate-tasks @selectTask="selectTask" style="float: right"></integrate-tasks>
      </div>
      <vue-scroll style="height: 850px; width: calc(100%)" :ops="ops" ref="scroll">
        <div class="graphContainer" ref="container" @contextmenu.prevent></div>
      </vue-scroll>
    </div>

    <div class="rightBar">
      <div class="instances">
        <el-row>
          <div v-for="(item, index) in instanceList" :key="index">
            <instance-card
              :instanceItem="item"
              :taskItem="currentTask"
              :role="'builder'"

              @check="check"
              @changeSelectInstanceId="changeSelectInstanceId"
            ></instance-card>
          </div>
        </el-row>
      </div>
      <div class="page">
        <el-pagination
          @current-change="handleCurrentChange"
          :page-size="7"
          :pager-count="5"
          :small="true"
          background
          layout="prev, pager, next"
          :total="total"
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
        <div style="height: 500px">
          <!-- Configuration -->
          <comparison />
        </div>
      </el-dialog>
    </div>

    <el-dialog title="New Task" :visible.sync="isNewTaskContainerShow" :close-on-click-modal="false">
      <el-form :model="newTaskForm">
        <el-form-item label="TaskName" :label-width="formLabelWidth">
          <el-input v-model="newTaskForm.taskName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="TaskDescription" :label-width="formLabelWidth">
          <el-input v-model="newTaskForm.taskDescription" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Task Type">
          <el-select v-model="newTaskForm.type" placeholder="Please select">
            <el-option v-for="item in newTaskForm.typeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="isNewTaskContainerShow = false">Cancel</el-button>
          <el-button type="primary" @click="confirmNewTask">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
  <!-- </div> -->
</template>

<script>
import bus from './bus';
import { mapState } from 'vuex';
import mxgraph from '_com/MxGraph/index';
import { genGraph } from '_com/MxGraph/initMx';
import {
  saveIntegrateTask,
  updateIntegrateTask,
  saveIntegrateTaskInstance,
  updateIntegrateTaskInstanceById,
  getScenarioByProjectId,
  getAllIntegrateTaskInstancesByTaskId,
  getAllInstances,
  runtask,
  checkTaskStatus,
  updatePerformanceById,
  updateScenarioByProjectId,
  // postDataContainer,
} from '@/api/request';
import { generateAction, generateXml1, getCellStyle } from './configuration';
// import { hasProperty } from '@/utils/utils';

import integrateTasks from '_com/IntegrateTasks';
import instanceCard from '_com/Cards/InstanceCard';
import comparison from '_com/Comparison/Comparison';
import leftToolbar from './LeftToolbar.vue';

const {
  // mxGraph,
  mxEvent,
  mxUtils,
  mxCodec,
} = mxgraph;

export default {
  props: {
    taskInfoInit: {
      type: Object,
    },
  },

  components: {
    integrateTasks,
    instanceCard,
    comparison,
    leftToolbar,
  },

  computed: {
    ...mapState({
      role: (state) => state.permission.role,
    }),
  },

  data() {
    return {
      size: 100,
      total: 0,
      newTaskForm: {
        taskName: '',
        taskDescription: '',
        type: 'integrateTask',
        typeOptions: [{ label: 'Integrate Task', value: 'integrateTask' }],
      },
      formLabelWidth: '120px',
      selectId: '',
      projectId: this.$route.params.id,

      graph: null,
      editCellVisible: false,

      selectionCells: [],
      deleteCellsVisible: false,
      checked: false,
      undoMng: {
        indexOfNextAdd: 1,
        history: [{}],
      },

      cellForm: {
        name: '',
      },

      getXml: this.sendXml,

      doi: '',
      // cell: {}, //双击事件 cell
      // state: {},

      modelListInGraph: [],
      modelInputInGraph: [],
      modelLinkInGraph: [], //下一模型的输入数据
      modelOutputInGraph: [],
      linkEdgeList: [],

      dataServiceListInGraph: [],
      dataServiceInputInGraph: [],
      dataServiceLinkInGraph: [],
      dataServiceOutputListInGraph: [],

      stateList: [],
      // dataItemModelbarKey: 0,

      taskInfo: {
        taskName: '',
        taskDescription: '',
      },

      isNewTaskContainerShow: false, //暂未用到

      currentTask: '',

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
        rail: {
          keepShow: true,
        },
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
        pageSize: 7,
        page: 0,
      },
      instanceList: [],
      allInstanceList: [],

      comparisonDialogShow: false,
      // selectInstanceId:''
    };
  },


  methods: {
    zoom(val) {
      this.size = val;
      this.graph.zoomTo(val / 100);
    },
    zoomIn() {
      this.size = this.size + 10;
      this.graph.zoomTo(this.size / 100);
    },
    zoomOut() {
      this.size = this.size - 10;
      this.graph.zoomTo(this.size / 100);
    },

    clear() {
      this.graph.removeCells(this.graph.getChildVertices(this.graph.getDefaultParent()));
      const codec = new mxCodec();
      const encodedModel = codec.encode(this.graph.getModel());
      const xml = mxUtils.getXml(encodedModel);
      const json = this.$x2js.xml2js(xml);
      console.log(xml);
      console.log(json);
    },

    getSelectionCells() {
      bus.$on('go', (data) => {
        this.selectionCells = data;
      });
    },

    async confirmNewTask() {
      let postJson = {
        projectId: this.projectId,
        taskContent: '',
        taskName: this.newTaskForm.taskName,
        taskDescription: this.newTaskForm.taskDescription,
        creator: this.currentTask.creator,
      };
      if (this.newTaskForm.type == 'integrateTask') {
        postJson.taskContent = '<mxGraphModel><root><mxCell id="0"/><mxCell id="1" parent="0"/></root></mxGraphModel>';
      }
      let data = await saveIntegrateTask(postJson);
      this.taskInfo = data;
      this.currentTask = data;
      this.graph.removeCells(this.graph.getChildVertices(this.graph.getDefaultParent()));
      this.isNewTaskContainerShow = false;
      if (data.id == this.selectId) {
        this.isSelectTaskInConsruction = true;
      } else {
        this.isSelectTaskInConsruction = false;
      }
      this.undoMng.history = [this.undoMng.history[this.undoMng.indexOfNextAdd - 1]];
      this.undoMng.indexOfNextAdd = 1;
      this.allInstanceList = [];
      this.instanceList = [];
    },

    compareResult() {
      this.comparisonDialogShow = true;
    },

    async setAsSelectTaskInConstruction() {
      if (!this.isSelectTaskInConsruction) {
        let taskId = this.currentTask.id;
        await updateScenarioByProjectId(this.projectId, { selectTaskId: taskId });
        this.isSelectTaskInConsruction = true;
      } else {
        this.$notify({
          message: 'It is already the SelectTask',
          type: 'warning',
        });
      }
      // this.isSelectTaskInConsruction = !this.isSelectTaskInConsruction;
      // let taskId = '';
      // //已经取消了选择的Task
      // if (this.isSelectTaskInConsruction) {
      //   taskId = '';
      // } else {
      //   taskId = this.currentTask.id;
      // }

      // await updateScenarioByProjectId(this.projectId, { selectTaskId: taskId });
      // console.log('!!!');
    },
    //--------------初始化 bar的modelItem的内容--由 AllModels组件返回

    // handleCollapseChange(val) {

    // },

    //初始化mxgraph
    async init() {
      //创建画布

      this.currentTask = this.taskInfoInit;
      console.log(this.taskInfoInit)
      this.container = this.$refs.container;
      this.createGraph();

      this.getScenario();
      this.graph.setPanning(true);

      this.initUndoMng();
      this.getSelectionCells();

      await this.getAllInstances();
      // await this.getAllIntegrateTaskInstances(1);
      this.graph.importGraph(this.taskInfoInit.taskContent);

      

    },

    async getScenario() {
      let data = await getScenarioByProjectId(this.projectId);
      this.selectId = data.selectTaskId;
      let flag = data.selectTaskId == this.currentTask.id;
      this.isSelectTaskInConsruction = flag;;
      this.graph.removeCells(this.graph.getChildVertices(this.graph.getDefaultParent()));
    },

    // Creates the graph inside the given container
    createGraph() {
      this.graph = genGraph(this.container);
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

      console.log(this.graph.getModel().cells);

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

    judgeInputList() {
      let modelInputInGraph = this.modelInputInGraph;
      // console.log(modelInputInGraph)
      modelInputInGraph.forEach((input) => {
        // console.log(input)
        if (input.nodeAttribute.dataSelect.value == '') {
          this.$notify.error({
            title: 'Error',
            message: 'You have not bind the data',
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
      this.$refs[form].validate((valid) => {
        if (valid) {
          this.graph.getModel().beginUpdate();
          let cell = this.graph.getSelectionCell();
          if (cell.name == this.cellForm.name) {
            this.$message({
              message: 'Name changed is as same as before',
              type: 'warning',
            });
          } else {
            try {
              cell.name = this.cellForm.name;
              this.graph.refresh(cell); // 刷新cell
              this.$message({
                message: 'Refresh the node successfully!',
                type: 'success',
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
      // this.deleteCellsVisible = true;
      console.log(this.selectionCells);
      this.graph.removeCells(this.selectionCells);
      this.selectionCells = [];
    },

    deleteCells() {
      let cells = this.selectionCells;
      let tmpSet = new Set(this.selectionCells);

      cells.forEach((cell) => {
        this.findDeleteCell(cell, tmpSet);
      });
      this.deleteCellsVisible = false;

      this.graph.removeCells(Array.from(tmpSet), true);
    },

    findDeleteCell(cell, deleteSet) {
      deleteSet.add(cell);
      if (cell.edges) {
        cell.edges.forEach((tmpEdge) => {
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
          cells = this.undoMng.history[undoIndex].changes.map((change) => {
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
    //mxUndoManager初始化
    initUndoMng() {
      let undoMng = new mxgraph.mxUndoManager();
      let listener = function (sender, evt) {
        undoMng.undoableEditHappened(evt.getProperty('edit'));
      };
      // console.log(this.graph.getModel());
      this.graph.getModel().addListener(mxEvent.UNDO, listener);
      this.graph.getView().addListener(mxEvent.UNDO, listener);
      this.undoMng = undoMng;
    },
    //撤销
    undo() {
      // console.log(this.undoMng);
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
      // this.currentTask = {
      //   taskName: '',
      //   taskDescription: '',
      // };
      this.isNewTaskContainerShow = true;
    },

    async createNewTask() {
      let postJson = {
        projectId: this.projectId,
        ...this.taskInfo,
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
      if (Object.keys(this.graph.getModel().cells).length < 1) {
        this.$message.error('Please select at least one model.');
        return;
      }

      this.getCells();

      //王梓欢配置文件
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
      //mxgraph xml文件
      let graphXml = this.graph.getGraphXml();

      //model action
      let action = generateAction(
        this.currentTask.id,
        this.modelListInGraph,
        this.modelInputInGraph,
        this.modelLinkInGraph,
        this.modelOutputInGraph,
        this.linkEdgeList,
        'task'
      );

      let postJson = {
        configuration: xml,
        taskContent: graphXml,
        // modelActions: modelActions,modelACTIONlIST
        taskName: this.currentTask.taskName,
        taskDescription: this.currentTask.taskDescription,
        action: action,
      };

      await updateIntegrateTask(this.currentTask.id, postJson);
      let content = {
        content: 'Simulation Scenario',
        degree: '100%',
        type: 'success',
        icon: 'el-icon-sunny',
      };

      await updatePerformanceById('context', this.projectId, content);
      // await patch(`/integrateTasks/${this.currentTask.id}`, postJson);
      // console.log(data);
      this.undoMng.history = [this.undoMng.history[this.undoMng.indexOfNextAdd - 1]];
      this.undoMng.indexOfNextAdd = 1;
    },

    async selectTask(val) {
      this.currentTask = val;
      this.taskInfo = val;
      if (val.id == this.selectId) {
        this.isSelectTaskInConsruction = true;
      } else {
        this.isSelectTaskInConsruction = false;
      }
      this.graph.removeCells(this.graph.getChildVertices(this.graph.getDefaultParent()));
      this.graph.importGraph(val.taskContent);
      this.undoMng.history = [this.undoMng.history[this.undoMng.indexOfNextAdd - 1]];
      this.undoMng.indexOfNextAdd = 1;
      this.allInstanceList = [];
      await this.getAllInstances();
      await this.getAllIntegrateTaskInstances(1);

      // let note = val.note;
      // this.importModelXML(note);
      // this.isNewTaskContainerShow = false;
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

    getInstanceAction(action) {
      let outputList = this.modelOutputInGraph;
      let dataItem = action.dataItemList;
      outputList.forEach((out) => {
        let dataOutputValue = dataItem.filter((item) => item.id == out.fileId);
        if (dataOutputValue.length != 0) {
          out.value = dataOutputValue.value;
        }
      });
    },

    async runGraph() {
      this.getCells();

      // this.modelInputInGraph.forEach(async (input) => {
      //   console.log(input);

      //   if (input.nodeAttribute.datasetItem.isParams == 'true') {
      //     let Dataset = {
      //       Dataset: {
      //         XDO: {
      //           _name: input.nodeAttribute.datasetItem.UdxDeclaration[0].UdxNode[0].UdxNode[0].name,
      //           _kernelType: input.nodeAttribute.datasetItem.UdxDeclaration[0].UdxNode[0].UdxNode[0].name,
      //           _value: input.dataResourceRelated.value,
      //         },
      //       },
      //     };
      //     let xml = this.$x2js.js2xml(Dataset);
      //     let file = new File([xml], input.dataResourceRelated.name + '.xml', { type: 'text/xml' });
      //     let uploadFileForm = new FormData();
      //     uploadFileForm.append('file', file);
      //     let data = await postDataContainer(uploadFileForm);
      //     console.log(data);
      //     input.dataResourceRelated.value = `http://221.226.60.2:8082/data/${data.id}`;
      //   }
      // });

      // this.dataServiceInputInGraph.forEach((input) => {
      //   console.log(input);
      //   debugger;
      //   if (input.isParameter) {
      //     let Dataset = {
      //       Dataset: {
      //         XDO: {
      //           _name: input.nodeAttribute.datasetItem.UdxDeclaration[0].UdxNode[0].UdxNode[0].name,
      //           _kernelType: input.nodeAttribute.datasetItem.UdxDeclaration[0].UdxNode[0].UdxNode[0].name,
      //           _value: input.dataResourceRelated.value,
      //         },
      //       },
      //     };
      //     let xml = this.$x2js.js2xml(Dataset);
      //     let file = new File([xml], input.dataResourceRelated.name + '.xml', { type: 'text/xml' });
      //     let uploadFileForm = new FormData();
      //     uploadFileForm.append('file', file);
      //     let data = await postDataContainer(uploadFileForm);
      //     console.log(data);
      //   }
      // });

      //是否有input中有空 无法run
      if (!this.judgeInputList()) {
        return;
      }

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

      // await this.getOutputs(tid);

      //save to  the instance
    },

    async getOutputs(tid) {
      //获得结果

      this.record[tid] = {};
      this.timer = setInterval(async () => {
        if (this.record[tid].status == 1) {
          clearInterval(this.timer);
          this.instanceList.forEach((item, index) => {
            if(item.id == this.record[tid].id) {
              this.instanceList.splice(index, 1, this.record[tid])
            }
          })
          return;
        } else {
          let data = await checkTaskStatus(tid);
          this.record[tid] = data;
          // console.log(this.record);
          // this.changeCellColor(data);
        }
      }, 3000);
    },

    changeCellColor(data) {
      let modelActions = data.taskInfo.modelActionList;
      let runningTask = modelActions.running; //yellow
      let failedTask = modelActions.failed; //red
      let completedTask = modelActions.completed;

      let dataProcessing = data.taskInfo.dataProcessingList;
      let processingRunningTask = dataProcessing.running;
      let processingFailedTask = dataProcessing.failed;
      let processingCompleteTask = dataProcessing.completed;

      this.getCells();
      let modelListInGraph = this.modelListInGraph;
      let dataServiceListInGraph = this.dataServiceListInGraph;

      modelListInGraph.forEach((modelCell) => {
        //判断runnign里面是否有model
        if (runningTask.some((task) => task.id == modelCell.id)) {
          //change color
          this.changeCellStyleByStatus(0, modelCell, false);
          this.changeDataCellByStatus(0, modelCell, false);
        }
        //判断completedTask里面是否有model
        if (completedTask.some((task) => task.id == modelCell.id)) {
          //change color

          this.changeCellStyleByStatus(1, modelCell, false);

          this.changeDataCellByStatus(1, modelCell, false);
        }
        //判断failedTask里面是否有model
        if (failedTask.some((task) => task.id == modelCell.id)) {
          //change color
          this.changeCellStyleByStatus(2, modelCell, false);

          this.changeDataCellByStatus(2, modelCell, false);
        }
      });

      dataServiceListInGraph.forEach((cell) => {
        if (processingRunningTask.some((task) => task.id == cell.id)) {
          this.changeCellStyleByStatus(0, cell, false);
          this.changeDataCellByStatus(0, cell, false);
        }
        if (processingCompleteTask.some((task) => task.id == cell.id)) {
          //change color

          this.changeCellStyleByStatus(1, cell, false);

          this.changeDataCellByStatus(1, cell, false);
        }
        //判断failedTask里面是否有model
        if (processingFailedTask.some((task) => task.id == cell.id)) {
          //change color
          this.changeCellStyleByStatus(2, cell, false);

          this.changeDataCellByStatus(2, cell, false);
        }
      });
    },

    changeDataCellByStatus(status, modelCell) {
      let list = [...this.modelInputInGraph, ...this.modelLinkInGraph];
      let inputList = list.filter((event) => event.md5 == modelCell.md5);
      let outputList = this.modelOutputInGraph.filter((event) => event.md5 == modelCell.md5);
      inputList.forEach((item) => {
        this.changeCellStyleByStatus(status, item, true);
      });
      outputList.forEach((item) => {
        this.changeCellStyleByStatus(status, item, true);
      });
    },

    changeCellStyleByStatus(status, item, isData) {
      //runnning
      let style;
      if (status == 0) {
        style = {
          fontColor: '#f6f6f6',
          fillColor: '#E6A23C',
        };
      }
      //finish
      if (status == 1) {
        style = {
          fillColor: '#67C23A',
          fontColor: '#24292E',
        };
      } //error
      if (status == 2) {
        style = {
          fillColor: '#ce1212',
          fontColor: '#f6f6f6',
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
          id: cell.id,
          name: cell.name,
        });
      });
      this.dataServiceListInGraph.forEach((cell) => {
        taskInfo.dataProcessingList.waiting.push({
          id: cell.id,
          name: cell.name,
        });
      });
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
        taskInfo: taskInfo,
        taskContent: this.currentTask.taskContent,
        status: 0,
        tid: tid,
      };
      let data = await saveIntegrateTaskInstance(postJson);
      this.currentTaskInstance = data;
      if (this.instanceList.length == 7) {
        this.instanceList.pop();
        this.total = this.total + 1
      }
      this.instanceList.splice(0, 0, data);

    },

    async updateTaskInstances(type) {
      let postJson = {};
      if (type == 'model') {
        postJson = {
          action: generateAction(
            this.currentTask.id,
            this.modelListInGraph,
            this.modelInputInGraph,
            this.modelLinkInGraph,
            this.modelOutputInGraph,
            this.linkEdgeList,
            'taskInstance'
          ),
          status: 1,
        };
      } else if (type == 'dataProcessing') {
        postJson = {
          action: generateAction(
            this.currentTask.id,
            this.dataServiceListInGraph,
            this.dataServiceInputInGraph,
            this.dataServiceLinkInGraph,
            this.dataServiceOutputListInGraph,
            this.linkEdgeList,
            'taskInstance'
          ),
          status: 1,
        };
      }
      // console.log(postJson, this.currentTaskInstance.id);

      await updateIntegrateTaskInstanceById(this.currentTaskInstance.id, postJson);
      this.allInstanceList.forEach((instance) => {
        if (instance.id == this.currentTaskInstance.id) {
          instance.status = 1;
        }
      });
    },

    async getAllInstances() {
      let data = await getAllInstances(this.currentTask.id, 0, 7);
      this.instanceList = data.content;
      this.total = data.total
      console.log(data)
    },

    //instance list
    async getAllIntegrateTaskInstances(page) {
      let data = await getAllIntegrateTaskInstancesByTaskId(this.currentTask.id, page - 1, this.instancePageFilter.pageSize);
      if (data == null) {
        this.instanceList = [];
        return;
      }

      this.instanceList = data.content;

      // this.instancePageFilter.page++;

      // console.log(this.instanceList)
      // console.log(this.instancePageFilter)
      // console.log('instances', data);
    },

    async handleCurrentChange(val) {
      let data = await getAllInstances(this.currentTask.id, val - 1, 7)
      this.instanceList = data.content;
      this.total = data.total
    },

    changeSelectInstanceId(val) {
      this.currentTask.selectInstanceId = val
    },
    check(val) {
      this.instanceList.forEach((item, index) => {
        if(val.id == item.id) {
          this.instanceList.splice(index, 1, val)
        }
      })
    }
    
  },

  mounted() {
    this.init();
  },
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
      // position: relative;
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
