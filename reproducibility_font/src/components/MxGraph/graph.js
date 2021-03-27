import mxgraph from './index';

const {
  mxGraph,
  mxVertexHandler,
  mxConstants,
  mxCellState,
  mxPerimeter,
  mxCellEditor,
  mxGraphHandler,
  mxEvent,
  mxEdgeHandler,
  mxShape,
  mxConnectionConstraint,
  mxPoint,
  mxEventObject,
  mxCodec,
  mxObjectCodec,
  mxUtils,
  mxImageExport,
  mxXmlCanvas2D,
  mxCodecRegistry,
} = mxgraph;

Object.assign(mxEvent, {
  EDGE_START_MOVE: 'edgeStartMove',
  VERTEX_START_MOVE: 'vertexStartMove',
});



export class Graph extends mxgraph {
  static getStyleDict(cell) {
    return _.compact(cell.getStyle().split(';'))
      .reduce((acc, item) => {
        const [key, value] = item.split('=');
        acc[key] = value;
        return acc;
      }, {});
  }

  static convertStyleToString(styleDict) {
    const style = Object.entries(styleDict)
      .map(([key, value]) => `${key}=${value}`)
      .join(';')
      .replace(/=undefined/g, '');
    return `${style};`;
  }

  static getCellPosition(cell) {
    return _.pick(cell.getGeometry(), ['x', 'y']);
  }

  constructor(container) {
    super(container);
    this._init();
  }

  _init() {
    this._setDefaultConfig();
    // this._configConstituent();
    // this._putVertexStyle();
    // this._setDefaultEdgeStyle();
    // this._setAnchors();
    this._configCustomEvent();
    // this._configCoder();
  }

  _setDefaultConfig() {
    this.setConnectable(true);
    mxEvent.disableContextMenu(this.container);

    // 固定节点大小
    this.setCellsResizable(false);

    // 编辑时按回车键不换行，而是完成输入
    this.setEnterStopsCellEditing(false);
    // 编辑时按 escape 后完成输入
    mxCellEditor.prototype.escapeCancelsEditing = false;
    // 失焦时完成输入
    mxCellEditor.prototype.blurEnabled = true;

    // 禁止节点折叠
    this.foldingEnabled = false;
    // 文本包裹效果必须开启此配置
    this.setHtmlLabels(true);

    // 拖拽过程对齐线
    mxGraphHandler.prototype.guidesEnabled = true;

    // 禁止游离线条
    this.setDisconnectOnMove(false);
    this.setAllowDanglingEdges(false);
    mxGraph.prototype.isCellMovable = cell => !cell.edge;

    // 禁止调整线条弯曲度
    this.setCellsBendable(true);

    // 禁止从将label从线条上拖离
    mxGraph.prototype.edgeLabelsMovable = false;
  }

  _configCustomEvent() {
    //自定义单击事件
    const graph = this;
    const oldStart = mxEdgeHandler.prototype.start;
    mxEdgeHandler.prototype.start = function start(...args) {
      oldStart.apply(this, args);
      graph.fireEvent(new mxEventObject(mxEvent.EDGE_START_MOVE,
        'edge', this.state.cell,
        'source', this.isSource,
      ));
    };


    const oldCreatePreviewShape = mxGraphHandler.prototype.createPreviewShape;
    mxGraphHandler.prototype.createPreviewShape = function createPreviewShape(...args) {
      graph.fireEvent(new mxEventObject(mxEvent.VERTEX_START_MOVE));
      return oldCreatePreviewShape.apply(this, args);
    };

  }
  importModelXML(xmlTxt) {
    //xml to json
    this.getModel().beginUpdate();
    try {
      const doc = mxUtils.parseXml(xmlTxt);
      const root = doc.documentElement;
      const dec = new mxCodec(root.ownerDocument);
      dec.decode(root, this.getModel());
    } finally {
      this.getModel().endUpdate();
    }
    this._restoreModel();
  }
}
