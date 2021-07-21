import mxgraph from './index';
import FileSaver from 'file-saver';
// import _ from 'lodash';

const { mxConstants, mxCellState, mxCellEditor, mxEvent, mxRubberband, mxUndoManager, mxCodec, mxUtils } = mxgraph;

export default {
  data() {
    return {};
  },
  computed: {},
  mounted() {},

  methods: {
    init() {
      this._setDefaultConfig();
      this._initUndoManager();
      this._setDefaultEdgeStyle();
      this._listenGraphEvent();
      this._initGraph();
      this.getGraphXml();
    },

    _setDefaultConfig() {
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
    },

    _setDefaultEdgeStyle() {
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

    _initUndoManager() {
      this.undoMng = new mxUndoManager();

      let listen = (sender, evt) => {
        this.undoMng.undoableEditHappened(evt.getProperty('edit'));
      };

      this.graph.getModel().addListener(mxEvent.UNDO, listen);
      this.graph.getView().addListener(mxEvent.UNDO, listen);
    },

    _listenGraphEvent() {
      this.graph.addListener(mxEvent.DOUBLE_CLICK, async (graph, evt) => {
        // DOUBLE_CLICK
        let cell = {
          doi: evt.properties.cell.doi,
          style: evt.properties.cell.style,
          type: evt.properties.cell.type
        };

        let clickModelType = cell.type;
        // let dataType = cell.style.includes('data');
        if (clickModelType == 'model') {
          this.dataItemModelbarKey++;
          // console.log(this.dataItemModelbarKey);
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

          const clickModelType = cell.style.includes('modelType'); //是否单机Model组件
          const dataType = cell.style.includes('data'); //是否单击data组件

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
        } else {
          //单击空白处
          this.cell = {};
        }
      });

      //增加连接线
      this.graph.addListener(mxEvent.CELLS_ADDED, (sender, evt) => {
        const cell = evt.properties.cells[0];

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

            this.graph.getModel().endUpdate();
          }
          this.$message.info('Add a line');
        }
      });

      // 监听 mxGraph 事件
      this.mxGraphSelectionModel = this.graph.getSelectionModel();
      this.mxGraphSelectionModel.addListener(mxEvent.CHANGE, this.handleSelectionChange);
    },

    _initGraph() {
      this.graph.convertValueToString = cell => {
        // 从value中获取显示的内容
        return cell.name;
      };
    },

    getGraphXml() {
      let encoder = new mxCodec();
      let graphXml = encoder.encode(this.graph.getModel());
      let xml = mxUtils.getPrettyXml(graphXml);
      return xml;
    },

    exportGraph() {
      let graphXml = this.getGraphXml();
      let xml = mxUtils.getPrettyXml(graphXml);
      const blob = new Blob([xml], {
        type: 'text/plain;charset=utf-8'
      });
      FileSaver.saveAs(blob, 'mxgraph.xml');
    },

    _getCells() {
      // this.modelListInGraph = this.modelOutputInGraph = this.modelInputInGraph = this.modelLinkInGraph = [];
      let modelListInGraph = [];
      let modelOutputInGraph = [];
      let modelInputInGraph = [];
      let modelLinkInGraph = [];
      // let dataLineInputInGrapg = [];

      Object.values(this.graph.getModel().cells).forEach(cell => {
        if (cell.style != undefined) {
          if (cell.style.includes('modelType')) {
            modelListInGraph.push(cell);
          } else if (cell.style.includes('dataOutputType')) {
            cell.type = 'output';
            modelOutputInGraph.push(cell);
          } else if (cell.style.includes('dataInputType')) {
            cell.type = 'input';
            modelInputInGraph.push(cell);
          }
        }
      });

      // console.log(this.graph.getModel().cells);
      let links = Object.values(this.graph.getModel().cells).filter(cell => Object.prototype.hasOwnProperty.call(cell, 'edge'));
      this.linkEdgeList = links;

      this.modelListInGraph = modelListInGraph;
      this.modelOutputInGraph = modelOutputInGraph;
      this.modelInputInGraph = modelInputInGraph;
      this.modelLinkInGraph = modelLinkInGraph;
    }
  }
};
