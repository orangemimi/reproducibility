<!--  -->
<template>
  <div class="leftBar">
    <el-input placeholder="Search model/tool" size="mini" class="search_input">
      <el-button slot="append" icon="el-icon-search"></el-button>
    </el-input>

    <vue-scroll style="height: calc(86vh); width: 100%" :ops="ops">
      <el-collapse v-model="activeNames" class="collapse">
        <el-collapse-item title="General" name="general">
          <general-toolbar ref="generalBar" :showType="'rhombus'"></general-toolbar>
          <general-toolbar ref="codeBar" :showType="'rectangle'"></general-toolbar>
        </el-collapse-item>
        <el-collapse-item title="Models" name="models">
          <model-item-toolbar ref="modelService" @getModels="getModels"></model-item-toolbar>
        </el-collapse-item>
        <el-collapse-item title="Data Service Methods" name="dataServices">
          <data-service-toolbar ref="dataService" @getDataServices="getDataServices"></data-service-toolbar>
        </el-collapse-item>
        <!-- <el-collapse-item title="Related Datas" name="modelRelatedDatas">
          <div v-if="modelDoubleClick">
            <data-item-toolbar :cell="cell" ref="dataItemBar" @getInAndOut="getInAndOut"></data-item-toolbar>
          </div>
        </el-collapse-item> -->
      </el-collapse>
    </vue-scroll>

    <div class="dialogs">
      <el-dialog :visible.sync="modelDoubleClick" width="50%" title="Configuration" :destroy-on-close="true" :close-on-click-modal="false">
        <data-item-toolbar :cell="currentCell" @selectItemListToGraph="selectItemListToGraph"></data-item-toolbar>
      </el-dialog>
      <el-dialog :visible.sync="dataDoubleClick" width="50%" title="Configuration" :destroy-on-close="true" :close-on-click-modal="false">
        <!-- Configuration -->
        <data-cell-info :cell="dataNode"></data-cell-info>
      </el-dialog>
      <el-dialog :visible.sync="codeDoubleClick" width="50%" title="Configuration" destroy-on-close :close-on-click-modal="false">
        <data-service-code-configuration @selectItemListToGraph="selectItemListToGraph"></data-service-code-configuration>
      </el-dialog>
      <el-dialog :visible.sync="dataServiceDoubleClick" width="50%" title="Configuration" destroy-on-close :close-on-click-modal="false">
        <data-service-configuration :cell="currentCell" @selectItemListToGraph="selectItemListToGraph"></data-service-configuration>
      </el-dialog>
      <!-- <el-drawer title="Configuration" :visible.sync="codeDoubleClick" size="45%">
        <el-col :span="22" :offset="1">
          <vue-scroll style="height: calc(90vh) " :ops="ops">
            <data-service-configuration></data-service-configuration>
          </vue-scroll>
        </el-col>
      </el-drawer> -->
    </div>
  </div>
</template>

<script>
import modelItemToolbar from '_com/MxGraphBars/ModelItemToolbar';
import dataItemToolbar from '_com/MxGraphDialogs/ModelItemConfiguration';
import generalToolbar from '_com/MxGraphBars/GeneralToolbar';
import dataServiceToolbar from '_com/MxGraphBars/DataServiceToolbar';

import { hasProperty } from '@/utils/utils';
import { generalList } from '_com/MxGraphBars/toolbar';
import { differCellStyle, getCellStyle } from './configuration';
import mxgraph from '_com/MxGraph/index';
import dataServiceCodeConfiguration from '_com/MxGraphDialogs/DataServiceCodeConfiguration';
import dataServiceConfiguration from '_com/MxGraphDialogs/DataServiceConfiguration';
import dataCellInfo from '_com/MxGraphDialogs/DataCellInfo';
const { mxUtils, mxEvent } = mxgraph;

export default {
  components: {
    modelItemToolbar,
    dataItemToolbar,
    generalToolbar,
    dataServiceToolbar,
    dataCellInfo,
    dataServiceCodeConfiguration,
    dataServiceConfiguration,
  },

  computed: {
    rhombusList: () => {
      return generalList.filter((item) => item.style.shape == 'rhombus');
    }, // general toolbar
    rectangleList: () => {
      return generalList.filter((item) => item.style.shape == 'rectangle');
    },
  },

  data() {
    return {
      //collapse
      activeNames: ['general', 'models', 'dataServices'],

      //modelbar
      modelItemList: [],
      dataServiceItemList: [],
      inputItemList: [],
      outputItemList: [],
      graph: {},
      //dom
      domFlag: 0,

      modelClick: false,
      modelDoubleClick: false,
      dataClick: false,
      dataDoubleClick: false,
      codeClick: false,
      codeDoubleClick: false,
      dataServiceDoubleClick: false,

      selectionCells: [],
      dataNode: {},
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
      },
      currentCell: {},
    };
  },

  methods: {
    async init() {
      this.graph = this.$parent.graph;

      this.initLeftBar('generalBar');
      this.initLeftBar('codeBar');
      this.initLeftBar('modelService');
      this.initLeftBar('dataService');
    },

    getModels(val) {
      // console.log('modelitm', val);
      this.$set(this, 'modelItemList', val);
    },

    getDataServices(val) {
      // console.log('dataService', val);
      this.$set(this, 'dataServiceItemList', val);
    },

    //--------------初始化 bar的dataItem的内容--由 data-item-toolbar组件返回
    // getInAndOut(input, output) {
    //   // this.state = val;

    //   this.inputItemList = input;
    //   this.outputItemList = output;
    //   this.initLeftBar('modelServiceInput');
    //   this.initLeftBar('modelServiceOutput');
    // },

    selectItemListToGraph(val) {
      if (this.modelDoubleClick == true) {
        console.log('hah');
        this.modelDoubleClick = false;
      } else if (this.dataDoubleClick == true) {
        this.dataDoubleClick = false;
      } else if (this.codeDoubleClick == true) {
        this.codeDoubleClick = false;
      } else if (this.dataServiceDoubleClick == true) {
        this.dataServiceDoubleClick = false;
      }
      val.forEach((item, index) => {
        let panel = '';
        if (item.type == 'input') {
          panel = 'modelServiceInput';
          this.getSelectItemStyleAddToGraph(panel, item, this.currentCell.geometry.x - 100 + 200 * index, this.currentCell.geometry.y - 120);
        }
        if (item.type == 'output') {
          panel = 'modelServiceOutput';
          this.getSelectItemStyleAddToGraph(panel, item, this.currentCell.geometry.x - 100 + 200 * index, this.currentCell.geometry.y + 120);
        }
        if (hasProperty(item, 'nodeType') && (item.nodeType == 'input' || item.nodeType == 'parameter')) {
          panel = 'dataServiceInput';
          this.getSelectItemStyleAddToGraph(panel, item, this.currentCell.geometry.x - 100 + 200 * index, this.currentCell.geometry.y - 120);
        }
        if (hasProperty(item, 'nodeType') && item.nodeType == 'output') {
          panel = 'dataServiceOutput';
          this.getSelectItemStyleAddToGraph(panel, item, this.currentCell.geometry.x - 100 + 200 * index, this.currentCell.geometry.y + 120);
        }
      });
    },

    getSelectItemStyleAddToGraph(panel, item, x, y) {
      let styleIn = differCellStyle(panel);
      let cellStyle = getCellStyle(styleIn, item);
      this.addCell(item, x, y, panel, cellStyle);
    },

    initLeftBar(panel) {
      let barRef; //对应各个Bar组件中的ref
      let barItemList = [];
      let styleIn = differCellStyle(panel);

      if (panel == 'modelService') {
        barRef = 'modelItemList';
        barItemList = this.modelItemList;
      } else if (panel == 'dataService') {
        barRef = 'dataServiceItemList';
        barItemList = this.dataServiceItemList;
      } else if (panel == 'modelServiceInput') {
        barRef = 'inputItemList';
        barItemList = this.inputItemList;
      } else if (panel == 'modelServiceOutput') {
        barRef = 'outputItemList';
        barItemList = this.outputItemList;
      } else if (panel == 'generalBar') {
        barRef = 'general';
        barItemList = this.rhombusList;
      } else if (panel == 'codeBar') {
        barRef = 'code';
        barItemList = this.rectangleList;
      }

      const domArray = this.$refs[panel].$refs[barRef];

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

      try {
        this.selectionCells = this.graph.getSelectionCells();

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

        vertex.nodeAttribute = {};

        if (type == 'modelService') {
          vertex.name = item.name;
          vertex.type = type;
          vertex.iterationNum = '1';
          vertex.nodeAttribute.doi = item.doi;
          vertex.nodeAttribute.md5 = item.md5;
          vertex.nodeAttribute.type = item.type; //service
        }
        if (type == 'codeBar') {
          vertex.type = 'code';
        }
        if (type == 'generalBar') {
          vertex.type = 'general';
        }
        if (type == 'dataService') {
          vertex.type = type;
          vertex.name = item.name;
          vertex.nodeAttribute.dataServiceId = item.dataServiceId;
          vertex.nodeAttribute.token = item.token;
          vertex.nodeAttribute.type = item.type; //Processing
        }
        if (type == 'modelServiceInput' || type == 'modelServiceOutput') {
          if (this.selectionCells.length == 0) {
            this.$notify.error({
              title: 'Error',
              message: 'You have not select any model',
            });

            return;
          }

          let selectionCell = this.selectionCells[0];

          vertex.name = item.name;
          vertex.type = type; //modelServiceInput or modelServiceOutput
          vertex.description = item.description; //event description
          vertex.nodeAttribute.eventId = item.eventId;
          vertex.nodeAttribute.eventDescription = item.description;
          vertex.nodeAttribute.eventName = item.name;
          vertex.nodeAttribute.datasetItem = item.datasetItem;
          vertex.nodeAttribute.stateId = item.stateId; //event description
          vertex.nodeAttribute.stateName = item.stateName;
          vertex.nodeAttribute.stateDescription = item.stateDescription;
          vertex.nodeAttribute.md5 = item.md5;
          vertex.nodeAttribute.doi = item.doi;
          vertex.nodeAttribute.optional = item.optional;
          vertex.nodeAttribute.type = item.type;
          vertex.linkModelCellId = selectionCell.id; //放置输入输出node时 与其关联的model的nodeId

          if (type == 'modelServiceInput') {
            this.addEdge(vertex, selectionCell);
          } else if (type == 'modelServiceOutput') {
            this.addEdge(selectionCell, vertex);
          }
        }
        if (type == 'dataServiceInput' || type == 'dataServiceOutput') {
          if (this.selectionCells.length == 0) {
            this.$notify.error({
              title: 'Error',
              message: 'You have not select any model',
            });
            return;
          }

          let selectionCell = this.selectionCells[0];

          vertex.name = item.name;
          vertex.type = type; //panel
          vertex.description = item.description;
          vertex.nodeAttribute.dataServiceId = item.dataServiceId;
          vertex.nodeAttribute.token = item.token;
          vertex.nodeAttribute.type = item.type; //Processing
          vertex.linkModelCellId = selectionCell.id; //放置输入输出node时 与其关联的model的nodeId

          if (type == 'dataServiceInput') {
            this.addEdge(vertex, selectionCell);
          } else if (type == 'dataServiceOutput') {
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

    listenGraphEvent() {
      // 监听双击事件
      this.graph.addListener(mxEvent.DOUBLE_CLICK, async (graph, evt) => {
        // DOUBLE_CLICK
        if (evt.properties.cell != undefined) {
          let cell = evt.properties.cell;
          let clickModelType = cell.type;
          if (clickModelType == 'modelService') {
            this.currentCell = cell;
            this.modelDoubleClick = true;
            this.domFlag++;
            // await initSetTimeOut();
            this.activeNames.push('modelRelatedDatas');
            this.dataDoubleClick = this.dataClick = this.modelClick = this.codeDoubleClick = this.dataServiceDoubleClick = false;
          } else if (clickModelType == 'dataService') {
            // await initSetTimeOut();
            this.currentCell = cell;
            this.modelDoubleClick = this.modelClick = this.dataClick = this.dataDoubleClick = this.codeDoubleClick = false;
            this.dataServiceDoubleClick = true;
          } else if (clickModelType == 'code') {
            this.modelDoubleClick = this.modelClick = this.dataClick = this.dataDoubleClick = this.dataServiceDoubleClick = false;
            this.codeDoubleClick = true;
          } else {
            this.modelDoubleClick = this.modelClick = this.dataClick = this.codeDoubleClick = this.dataServiceDoubleClick = false;

            // console.log(cell);
            this.dataNode = cell;
            this.dataDoubleClick = true;
          }
        }
      });

      // 监听单击事件
      //单击空白处 dialog隐藏
      this.graph.addListener(mxEvent.CLICK, (sender, evt) => {
        let isCell = Object.prototype.hasOwnProperty.call(evt.properties, 'cell');
        if (isCell) {
          let cell = evt.properties.cell;

          const clickModelType = cell.type;

          if (clickModelType == 'modelService') {
            // 使用 mxGraph 事件中心，触发自定义事件
            // this.currentCell = cell;
            this.modelClick = true;
            this.modelDoubleClick = this.dataClick = this.dataDoubleClick = this.codeClick = false;
          } else if (clickModelType == 'code') {
            this.codeClick = true;
            this.modelDoubleClick = this.dataClick = this.dataDoubleClick = this.modelClick = false;
          } else {
            this.modelDoubleClick = this.modelClick = this.dataDoubleClick = this.codeClick = false;

            // console.log(cell);
            this.dataNode = cell;
            this.dataClick = true;
          }
        } else {
          //单击空白处
          this.currentCell = {};
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

          this.graph.getModel().beginUpdate();
          try {
            if (linkCell.type == 'modelServiceInput') {
              linkCell.type = 'modelServiceLink';
            }
            if (linkCell.type == 'dataServiceInput') {
              linkCell.type = 'dataServiceLink';
            }
            let style = getCellStyle(differCellStyle(linkCell.type), linkCell);

            this.graph.getModel().setStyle(linkCell, style);
          } finally {
            this.graph.getModel().endUpdate();
          }
        }
      });

      // 监听 mxGraph 事件
      console.log(this.mxGraphSelectionModel);
      // this.mxGraphSelectionModel = this.graph.getSelectionModel();
      // this.mxGraphSelectionModel.addListener(mxEvent.CHANGE, this.handleSelectionChange);
      console.log(this.mxGraphSelectionModel);
    },

    currentEventWithFile(val) {
      this.graph.getModel().beginUpdate();

      try {
        Object.values(this.graph.getModel().cells).forEach((cell) => {
          if (cell.id == val.id) {
            cell.value = val.value;
            this.$message({
              message: 'You have submit the file successfully',
              type: 'success',
            });
          }
        });
      } finally {
        this.graph.getModel().endUpdate();
      }
    },
  },

  mounted() {},
};
</script>
<style lang="scss" scoped>
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
</style>
