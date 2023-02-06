<template>
  <div class>
    <div class="customToolbarContainer">
      <!-- <div class="toolbarContainer">
        <el-col>
          <el-row>
            <div class="toolbarTitle">Node type</div>
          </el-row>
          <el-row>
            <div class="items">
              <div v-for="item in toolbarItems" :key="item.title" ref="toolItem" class="item">
                <node-card :item="item"></node-card>
              </div>
            </div>
          </el-row>
        </el-col>
      </div> -->
      <div class="mainContainer">
        <div class="toolbarTop">
          <!-- <el-button @click="saveGraph" type="text" size="mini">Output</el-button> -->
          <el-button @click="exportGraph" type="text" size="mini">Export as XML</el-button>
          <!-- <input @change="readFile" ref="importInput" class="hide" type="file" />
          <el-button @click="importGraphFile" type="text" size="mini">Import mxGraph</el-button> -->
          <el-button @click="checked ? deleteCells() : deleteCellsConfirmDialog()" type="text" size="mini" :disabled="selectionCells.length == 0">
            Delete
          </el-button>
          <el-button @click="undo" type="text" size="mini">Undo</el-button>
          <el-button @click="redo" type="text" size="mini">Redo</el-button>
          <!-- <el-button @click="exportPic" type="text" size="mini">导出图片</el-button> -->
        </div>
        <vue-scroll style="height: 630px; width: 1172px">
          <div class="graphContainer" ref="container"></div>
        </vue-scroll>
      </div>

      <div class="editCellContainer" v-show="editCellVisible">
        <!-- <edit-cell :visible="editCellVisible" @currentGraph="grapg"></edit-cell> -->
        <div>Edit the node</div>
        <div class="editCellForm">
          <el-form ref="cellForm" :model="cellForm" :rules="cellFormRules" size="mini">
            <el-form-item label="Node name" prop="name">
              <el-input v-model="cellForm.name"></el-input>
            </el-form-item>
          </el-form>
        </div>

        <el-button @click="editCellVisible = false">Close the dialog</el-button>
        <el-button type="primary" @click="submitCellForm('cellForm')">Submit</el-button>
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
  </div>
</template>

<script>
import { toolbarItems } from './toolbar';
import mxgraph from './index';
import nodeCard from './components/nodeCard';
import editCell from './components/editCell';
import FileSaver from 'file-saver';

const {
  mxGraph,
  mxOutline,
  mxEvent,
  mxCell,
  mxGeometry,
  mxUtils,
  mxEventObject,
  mxConnectionHandler,
  mxGraphHandler,
  mxRubberband,
  mxConstants,
  mxCellState,
  mxCellEditor,
  mxGraphView,
  mxCodec,
  mxUndoManager
} = mxgraph;

export default {
  props: {
    sendXml: {
      type: String
    }
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
  components: { nodeCard, editCell },
  computed: {
    toolbarItems: () => toolbarItems
  },
  data() {
    return {
      contentHeight: 0,
      graph: null,
      editCellVisible: false,
      graphXml: '',
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
      getXml: this.sendXml
    };
  },
  methods: {
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
        return cell.title;
      };
    },

    listenGraphEvent() {
      // 监听双击事件
      this.graph.addListener(mxEvent.DOUBLE_CLICK, (graph, evt) => {
        // const cell = evt.properties.cell;
        const cell = evt.properties.cell;
        this.getCellInfo(cell);
        // this.handleSelectionChange(evt.properties);
        if (cell.source == null && cell.target == null) {
          // console.log("node");
          this.editCellVisible = true;
        } else {
          // console.log("line");
          this.editCellVisible = true;
        }

        // console.info(cell); // 在控制台输出双击的cell
      });

      // 监听单击事件
      //单击空白处 dialog隐藏
      this.graph.addListener(mxEvent.CLICK, (sender, evt) => {
        // const cell = evt.properties.cell;
        if (sender.isMouseDown == false) {
          this.editCellVisible = false;
          return;
        }
      });

      //连接线
      // this.graph.addListener(mxEvent.CONNECT_CELL, (sender, evt) => {
      //   // const cell = evt.properties.cell;
      //   console.log(11111111111);
      // });

      // 监听 mxGraph 事件
      const mxGraphSelectionModel = this.graph.getSelectionModel();
      mxGraphSelectionModel.addListener(mxEvent.CHANGE, this.handleSelectionChange);
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

    addCell(toolItem, x, y) {
      const { width, height } = toolItem;
      const styleObj = toolItem.style;
      const style = Object.keys(styleObj)
        .map(attr => `${attr}=${styleObj[attr]}`)
        .join(';');
      const parent = this.graph.getDefaultParent();

      this.graph.getModel().beginUpdate();

      try {
        let vertex = this.graph.insertVertex(parent, null, null, x, y, width, height, style);
        vertex.title = toolItem.title;
        this.cellForm.name = toolItem.title; //form表单
      } finally {
        //   this.graph.getModel.ac
        this.graph.getModel().endUpdate();
        // this.editCellVisible = true;
      }
    },

    initToolbar() {
      const domArray = this.$refs.toolItem;

      if (!(domArray instanceof Array) || domArray.length <= 0) {
        return;
      }
      domArray.forEach((dom, domIndex) => {
        const toolItem = this.toolbarItems[domIndex];
        const { width, height } = toolItem;

        const dropHandler = (graph, evt, cell, x, y) => {
          this.addCell(toolItem, x, y);
        };
        const createDragPreview = () => {
          const elt = document.createElement('div');

          elt.style.border = '2px dotted black';
          elt.style.width = `${width}px`;
          elt.style.height = `${height}px`;
          return elt;
        };

        mxUtils.makeDraggable(dom, this.graph, dropHandler, createDragPreview(), 0, 0, false, true);
      });
    },

    getGraphXml() {
      let encoder = new mxCodec();
      this.graphXml = encoder.encode(this.graph.getModel());
    },

    exportGraph() {
      this.getGraphXml();
      let xml = mxUtils.getPrettyXml(this.graphXml);
      const blob = new Blob([xml], {
        type: 'text/plain;charset=utf-8'
      });
      FileSaver.saveAs(blob, 'mxgraph.xml');
    },

    importGraphFile(evt) {
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
          if (cell.title == this.cellForm.name) {
            this.$message({
              message: 'Name changed is as same as before',
              type: 'warning'
            });
          } else {
            try {
              cell.title = this.cellForm.name;
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

    getCellInfo(cell) {
      this.cellForm.name = cell.title;
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
    }
  },
  mounted() {
    this.initSize();
    this.createGraph();
    this.initConnectStyle();
    this.initGraph();
    this.initToolbar();
    this.initUndoManager();
    this.setDefaultEdgeStyle();
    this.listenGraphEvent();
    this.getGraphXml();
    // this.getCellInfo();
  },
  created() {
    this.initSize();
  }
};
</script>

<style lang="scss" scoped>
.customToolbarContainer {
  width: 100%;
  height: 100%;
  display: flex;
  position: relative;

  .toolbarContainer {
    position: relative;
    width: 160px;
    // margin: 0 5px;
    // background-color: rgb(251, 251, 251);
    .toolbarTitle {
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
  }

  .mainContainer {
    position: relative;
    width: 100%;
    .toolbarTop {
      background: rgb(251, 251, 251);
      padding-left: 10px;
      border-radius: 4px;
      margin-bottom: 10px;
    }
    .graphContainer {
      position: relative;
      overflow: hidden;
      height: 100%;
      width: 100%;
      min-width: 1110px;
      min-height: 850px;
      background: rgb(251, 251, 251) url('./images/grid.gif') 0 0 repeat;
      border-radius: 4px;
    }
  }

  .editCellContainer {
    position: relative;
    width: 400px;
  }
}
</style>
