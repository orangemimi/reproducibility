import mxgraph from './index';
import FileSaver from 'file-saver';
// import _ from 'lodash';

const { mxConstants, mxCellState, mxCellEditor, mxEvent, mxRubberband, mxUndoManager, mxCodec, mxUtils } = mxgraph;

function _setDefaultConfig(vueItem) {
  //允许连线
  vueItem.graph.setConnectable(true);

  // 编辑时按回车键不换行，而是完成输入
  vueItem.graph.setEnterStopsCellEditing(true);

  // 可修改
  vueItem.graph.setCellsEditable(true);

  // 从工具栏拖动到目标细胞时细胞边界是否产生光圈
  vueItem.graph.setDropEnabled(true);

  // 禁止游离线条
  vueItem.graph.setAllowDanglingEdges(false);
  vueItem.graph.setDisconnectOnMove(false);
  vueItem.graph.setConnectableEdges(false);

  //设置线条弯曲程度
  vueItem.graph.setCellsBendable(true);

  // 禁止节点折叠
  vueItem.graph.foldingEnabled = false;

  // 文本包裹效果必须开启此配置
  vueItem.graph.setHtmlLabels(true);

  // 拖拽过程对齐线
  vueItem.graph.graphHandler.guidesEnabled = true;

  // 容器大小自适应
  vueItem.graph.setResizeContainer(true);

  // 重复连接
  vueItem.graph.setMultigraph(true);

  // Enables rubberband selection
  new mxRubberband(vueItem.graph);

  vueItem.graph.popupMenuHandler.autoExpand = true;

  mxCellEditor.prototype.blurEnabled = true;
}

function _setDefaultEdgeStyle(vueItem) {
  const style = vueItem.graph.getStylesheet().getDefaultEdgeStyle();
  Object.assign(style, {
    [mxConstants.STYLE_ROUNDED]: true, // 设置线条拐弯处为圆角
    [mxConstants.STYLE_STROKEWIDTH]: '1',
    [mxConstants.STYLE_STROKECOLOR]: '#333333',
    [mxConstants.STYLE_EDGE]: mxConstants.EDGESTYLE_ORTHOGONAL,
    [mxConstants.STYLE_FONTCOLOR]: '#333333',
    [mxConstants.STYLE_LABEL_BACKGROUNDCOLOR]: '#ffa94d'
  });

  // 设置拖拽线的过程出现折线，默认为直线
  vueItem.graph.connectionHandler.createEdgeState = () => {
    const edge = vueItem.graph.createEdge();
    return new mxCellState(vueItem.graph.view, edge, vueItem.graph.getCellStyle(edge));
  };
}

function _initUndoManager(vueItem) {
  vueItem.undoMng = new mxUndoManager();

  let listen = (sender, evt) => {
    vueItem.undoMng.undoableEditHappened(evt.getProperty('edit'));
  };

  vueItem.graph.getModel().addListener(mxEvent.UNDO, listen);
  vueItem.graph.getView().addListener(mxEvent.UNDO, listen);
}

function _listenGraphEvent(vueItem) {
  vueItem.graph.addListener(mxEvent.DOUBLE_CLICK, async (graph, evt) => {
    // DOUBLE_CLICK
    let cell = {
      doi: evt.properties.cell.doi,
      style: evt.properties.cell.style,
      type: evt.properties.cell.type
    };

    let clickModelType = cell.type;
    // let dataType = cell.style.includes('data');
    if (clickModelType == 'model') {
      vueItem.dataItemModelbarKey++;
      // console.log(vueItem.dataItemModelbarKey);
      await vueItem.$refs.dataItemBar.initSetTimeOut();
      vueItem.modelDoubleClick = true;
      vueItem.cell = JSON.parse(JSON.stringify(cell));
      vueItem.dataDoubleClick = vueItem.dataClick = vueItem.modelClick = false;
    }
  });

  // 监听单击事件
  //单击空白处 dialog隐藏
  vueItem.graph.addListener(mxEvent.CLICK, (sender, evt) => {
    let isCell = Object.prototype.hasOwnProperty.call(evt.properties, 'cell');
    if (isCell) {
      let cell = evt.properties.cell;

      const clickModelType = cell.style.includes('modelType'); //是否单机Model组件
      const dataType = cell.style.includes('data'); //是否单击data组件

      if (clickModelType) {
        // 使用 mxGraph 事件中心，触发自定义事件
        // vueItem.cell = cell;
        vueItem.modelClick = true;
        vueItem.modelDoubleClick = vueItem.dataClick = vueItem.dataDoubleClick = false;
      } else if (dataType) {
        vueItem.modelDoubleClick = vueItem.modelClick = vueItem.dataDoubleClick = false;
        vueItem.dataClick = true;
        // console.log(cell);
        vueItem.dataNode = cell;
      }
    } else {
      //单击空白处
      vueItem.cell = {};
    }
  });

  //增加连接线
  vueItem.graph.addListener(mxEvent.CELLS_ADDED, (sender, evt) => {
    const cell = evt.properties.cells[0];

    if (cell.vertex) {
      vueItem.$message.info('Add a node');
    } else if (cell.edge) {
      //判断是否是link to next dataitem
      let linkCell = cell.target;
      if (linkCell.style.includes('dataInputType')) {
        vueItem.graph.getModel().beginUpdate();

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
        vueItem.graph.getModel().setStyle(linkCell, style);

        vueItem.graph.getModel().endUpdate();
      }
      vueItem.$message.info('Add a line');
    }
  });

  // 监听 mxGraph 事件
  vueItem.mxGraphSelectionModel = vueItem.graph.getSelectionModel();
  vueItem.mxGraphSelectionModel.addListener(mxEvent.CHANGE, vueItem.handleSelectionChange);
}

function _initGraph(vueItem) {
  vueItem.graph.convertValueToString = cell => {
    // 从value中获取显示的内容
    return cell.name;
  };
}

export function getGraphXml(vueItem) {
  let encoder = new mxCodec();
  let graphXml = encoder.encode(vueItem.graph.getModel());
  let xml = mxUtils.getPrettyXml(graphXml);
  return xml;
}

export function exportGraph(vueItem) {
  let graphXml = getGraphXml(vueItem);
  let xml = mxUtils.getPrettyXml(graphXml);
  const blob = new Blob([xml], {
    type: 'text/plain;charset=utf-8'
  });
  FileSaver.saveAs(blob, 'mxgraph.xml');
}

export function _getCells(vueItem) {
  // vueItem.modelListInGraph = vueItem.modelOutputInGraph = vueItem.modelInputInGraph = vueItem.modelLinkInGraph = [];
  let modelListInGraph = [];
  let modelOutputInGraph = [];
  let modelInputInGraph = [];
  let modelLinkInGraph = [];
  // let dataLineInputInGrapg = [];

  Object.values(vueItem.graph.getModel().cells).forEach(cell => {
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

  // console.log(vueItem.graph.getModel().cells);
  let links = Object.values(vueItem.graph.getModel().cells).filter(cell => Object.prototype.hasOwnProperty.call(cell, 'edge'));
  vueItem.linkEdgeList = links;

  vueItem.modelListInGraph = modelListInGraph;
  vueItem.modelOutputInGraph = modelOutputInGraph;
  vueItem.modelInputInGraph = modelInputInGraph;
  vueItem.modelLinkInGraph = modelLinkInGraph;
}

// function importGraph(xmlTxt) {
//   //xml to json
//   this.graph.getModel().beginUpdate();
//   try {
//     const doc = mxUtils.parseXml(xmlTxt);
//     const root = doc.documentElement;
//     const dec = new mxCodec(root.ownerDocument);
//     dec.decode(root, this.graph.getModel());
//     this.getCells();
//   } finally {
//     this.graph.getModel().endUpdate();
//   }
//   this.restoreModel();
// }

// function restoreModel() {
//   Object.values(this.graph.getModel().cells).forEach(cell => {
//     if (cell.vertex && cell.data) {
//       cell.data = JSON.parse(cell.data);
//     }
//   });
// }

const genGraph = vueItem => {
  _setDefaultConfig(vueItem);
  _initUndoManager(vueItem);
  _setDefaultEdgeStyle(vueItem);
  _listenGraphEvent(vueItem);
  _initGraph(vueItem);
  getGraphXml(vueItem);
  // _getCells(vueItem);
  // _configCustomEvent(vueItem);
};

export { genGraph };
