import mxgraph from './index';
import FileSaver from 'file-saver';
// import Shapes from './Shapes';
// import { init } from 'echarts';
// import _ from 'lodash';

const { mxGraph, mxConstants, mxCellState, mxCellEditor, mxEvent, mxRubberband, mxUndoManager, mxCodec, mxUtils, mxActor, mxPoint, mxCellRenderer } = mxgraph;

export class Graph extends mxGraph {
  constructor(container) {
    super(container);
    this._init();
  }

  _init() {
    this._setDefaultConfig();
    this._initUndoManager();
    this._setDefaultEdgeStyle();
    this._initGraph();
    this.getGraphXml();
    this.set();
    // _getCells(this);
    // _configCustomEvent(this);
  }

  _setDefaultConfig() {
    //允许连线
    this.setConnectable(true);

    // 编辑时按回车键不换行，而是完成输入
    this.setEnterStopsCellEditing(true);

    // 可修改
    this.setCellsEditable(true);

    // 从工具栏拖动到目标细胞时细胞边界是否产生光圈
    this.setDropEnabled(true);

    // 禁止游离线条
    this.setAllowDanglingEdges(false);
    this.setDisconnectOnMove(false);
    this.setConnectableEdges(false);

    //设置线条弯曲程度
    this.setCellsBendable(true);

    // 禁止节点折叠
    this.foldingEnabled = false;

    // 文本包裹效果必须开启此配置
    this.setHtmlLabels(true);

    // 拖拽过程对齐线
    this.graphHandler.guidesEnabled = true;

    // 容器大小自适应
    this.setResizeContainer(true);

    // 重复连接
    this.setMultigraph(true);

    // Enables rubberband selection
    new mxRubberband(this.graph);

    this.popupMenuHandler.autoExpand = true;

    mxCellEditor.prototype.blurEnabled = true;
  }

  _setDefaultEdgeStyle() {
    const style = this.getStylesheet().getDefaultEdgeStyle();
    Object.assign(style, {
      [mxConstants.STYLE_ROUNDED]: true, // 设置线条拐弯处为圆角
      [mxConstants.STYLE_STROKEWIDTH]: '1',
      [mxConstants.STYLE_STROKECOLOR]: '#333333',
      [mxConstants.STYLE_EDGE]: mxConstants.EDGESTYLE_ORTHOGONAL,
      [mxConstants.STYLE_FONTCOLOR]: '#333333',
      [mxConstants.STYLE_LABEL_BACKGROUNDCOLOR]: '#ffa94d'
    });

    // 设置拖拽线的过程出现折线，默认为直线
    this.connectionHandler.createEdgeState = () => {
      const edge = this.createEdge();
      return new mxCellState(this.view, edge, this.getCellStyle(edge));
    };
  }

  _initUndoManager() {
    this.undoMng = new mxUndoManager();

    let listen = (sender, evt) => {
      this.undoMng.undoableEditHappened(evt.getProperty('edit'));
    };

    this.getModel().addListener(mxEvent.UNDO, listen);
    this.getView().addListener(mxEvent.UNDO, listen);
  }

  _initGraph() {
    this.convertValueToString = cell => {
      // 从value中获取显示的内容
      return cell.name;
    };
  }

  getGraphXml() {
    let encoder = new mxCodec();
    let graphXml = encoder.encode(this.getModel());
    let xml = mxUtils.getPrettyXml(graphXml);
    return xml;
  }

  exportGraph() {
    let graphXml = this.getGraphXml();
    let xml = mxUtils.getPrettyXml(graphXml);
    const blob = new Blob([xml], {
      type: 'text/plain;charset=utf-8'
    });
    FileSaver.saveAs(blob, 'mxgraph.xml');
  }

  importGraph(xmlTxt) {
    //xml to json
    this.getModel().beginUpdate();
    try {
      const doc = mxUtils.parseXml(xmlTxt);
      const root = doc.documentElement;
      const dec = new mxCodec(root.ownerDocument);
      dec.decode(root, this.getModel());
      this.getCells();
    } finally {
      this.getModel().endUpdate();
    }
    this.restoreModel();
  }

  restoreModel() {
    Object.values(this.getModel().cells).forEach(cell => {
      if (cell.vertex && cell.data) {
        cell.data = JSON.parse(cell.data);
      }
    });
  }

  // Parallelogram shape
  ParallelogramShape() {
    mxActor.call(this);
  }

  set() {
    mxUtils.extend(this.ParallelogramShape, mxActor);
    this.ParallelogramShape.prototype.size = 0.2;
    this.ParallelogramShape.prototype.fixedSize = 20;
    this.ParallelogramShape.prototype.isRoundable = function() {
      return true;
    };
    this.ParallelogramShape.prototype.redrawPath = function(c, x, y, w, h) {
      var fixed = mxUtils.getValue(this.style, 'fixedSize', '0') != '0';

      var dx = fixed
        ? Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'size', this.fixedSize))))
        : w * Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.style, 'size', this.size))));
      var arcSize = mxUtils.getValue(this.style, mxConstants.STYLE_ARCSIZE, mxConstants.LINE_ARCSIZE) / 2;
      this.addPoints(c, [new mxPoint(0, h), new mxPoint(dx, 0), new mxPoint(w, 0), new mxPoint(w - dx, h)], this.isRounded, arcSize, true);
      c.end();
    };

    mxCellRenderer.registerShape('parallelogram', this.ParallelogramShape);
  }
}

let graph = {};

export const genGraph = container => {
  graph = new Graph(container);
  return graph;
};

// export { genGraph };
