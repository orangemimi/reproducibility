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
          <model-item-toolbar ref="modelBar" @getModels="getModels"></model-item-toolbar>
        </el-collapse-item>
        <el-collapse-item title="Data Processing Methods" name="dataProcessings">
          <data-service-toolbar ref="dataServiceBar" @getDataServices="getDataServices"></data-service-toolbar>
        </el-collapse-item>
        <el-collapse-item title="Related Datas" name="modelRelatedDatas">
          <!-- <div v-if="modelDoubleClick">
            <data-item-toolbar :cell="cell" ref="dataItemBar" @getInAndOut="getInAndOut"></data-item-toolbar>
          </div> -->
        </el-collapse-item>
      </el-collapse>
    </vue-scroll>

    <div class="dialogs">
      <el-dialog :visible.sync="modelDoubleClick" width="50%" title="Configuration" destroy-on-close :close-on-click-modal="false">
        <data-item-toolbar :cell="cell" @selectItemListToGraph="selectItemListToGraph"></data-item-toolbar>
      </el-dialog>
      <el-dialog :visible.sync="dataDoubleClick" width="50%" title="Configuration" destroy-on-close :close-on-click-modal="false">
        <!-- Configuration -->
        <data-cell-info :cell="dataNode" @currentEventWithFile="currentEventWithFile"></data-cell-info>
      </el-dialog>
      <el-dialog :visible.sync="codeDoubleClick" width="50%" title="Configuration" destroy-on-close :close-on-click-modal="false">
        <data-service-configuration></data-service-configuration>
      </el-dialog>
      <el-dialog :visible.sync="dataServiceDoubleClick" width="50%" title="Configuration" destroy-on-close :close-on-click-modal="false">
        <data-service-configuration></data-service-configuration>
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
import dataItemToolbar from '_com/MxGraphBars/DataItemToolbar';
import generalToolbar from '_com/MxGraphBars/GeneralToolbar';
import dataServiceToolbar from '_com/MxGraphBars/DataServiceToolbar';
import dataCellInfo from '_com/DataCellInfo/Info';
import { initSetTimeOut } from '@/utils/utils';
import { generalList } from '_com/MxGraphBars/toolbar';
import { differCellStyle, getCellStyle } from './configuration';
import mxgraph from '_com/MxGraph/index';
import dataServiceConfiguration from './DataServiceConfiguration';

const { mxUtils, mxEvent } = mxgraph;

export default {
  components: { modelItemToolbar, dataItemToolbar, generalToolbar, dataServiceToolbar, dataCellInfo, dataServiceConfiguration },

  computed: {
    rhombusList: () => {
      return generalList.filter(item => item.style.shape == 'rhombus');
    }, // general toolbar
    rectangleList: () => {
      return generalList.filter(item => item.style.shape == 'rectangle');
    }
  },

  data() {
    return {
      //collapse
      activeNames: ['general', 'models'],

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
          disable: false
        }
      },
      cell: {}
    };
  },

  methods: {
    async init() {
      this.graph = this.$parent.graph;

      this.initLeftBar('generalBar');
      this.initLeftBar('codeBar');
      this.initLeftBar('modelBar');
      this.initLeftBar('dataServiceBar');
    },

    getModels(val) {
      // console.log('modelitm', val);
      this.$set(this, 'modelItemList', val);
    },

    getDataServices(val) {
      console.log('dataService', val);
      this.$set(this, 'dataServiceItemList', val);
    },

    //--------------初始化 bar的dataItem的内容--由 data-item-toolbar组件返回
    getInAndOut(input, output) {
      // this.state = val;

      this.inputItemList = input;
      this.outputItemList = output;
      this.initLeftBar('inputBar');
      this.initLeftBar('outputBar');
    },

    selectItemListToGraph(val) {
      val.forEach((item, index) => {
        let panel = 'inputBar';
        if (item.type == 'input') {
          panel = 'inputBar';
        }
        if (item.type == 'output') {
          panel = 'outputBar';
        }
        let styleIn = differCellStyle(panel);
        let cellStyle = getCellStyle(styleIn, item);
        this.addCell(item, this.cell.geometry.x - 100 + 200 * index, this.cell.geometry.y - 120, panel, cellStyle);
      });
    },

    initLeftBar(panel) {
      let refType;
      let barRef; //对应各个Bar组件中的ref
      let barItemList = [];
      let styleIn = differCellStyle(panel);

      if (panel == 'modelBar') {
        refType = 'modelBar';
        barRef = 'modelItemList';
        barItemList = this.modelItemList;
      } else if (panel == 'dataServiceBar') {
        refType = 'dataServiceBar';
        barRef = 'dataServiceItemList';
        barItemList = this.dataServiceItemList;
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
        barItemList = this.rhombusList;
      } else if (panel == 'codeBar') {
        refType = 'codeBar';
        barRef = 'code';
        barItemList = this.rectangleList;
      }

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

        if (type == 'modelBar') {
          vertex.name = item.name;
          vertex.doi = item.doi;
          vertex.type = 'model';
          vertex.iterationNum = '1';
          vertex.md5 = item.md5;
        }
        if (type == 'codeBar') {
          vertex.type = 'code';
        }
        if (type == 'generalBar') {
          vertex.type = 'general';
        }
        if (type == 'inputBar' || type == 'outputBar') {
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
          vertex.datasetItem = item.datasetItem;
          vertex.stateId = item.stateId; //event description
          vertex.stateName = item.stateName;
          vertex.stateDescription = item.stateDescription;
          vertex.md5 = item.md5;
          vertex.doi = item.doi;
          vertex.optional = item.optional;
          vertex.linkModelCellId = selectionCell.id; //放置输入输出node时 与其关联的model的nodeId
          vertex.type = item.type; //input or output

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

    listenGraphEvent() {
      // 监听双击事件
      this.graph.addListener(mxEvent.DOUBLE_CLICK, async (graph, evt) => {
        // DOUBLE_CLICK
        let cell = evt.properties.cell;
        let clickModelType = cell.type;
        if (clickModelType == 'model') {
          this.modelDoubleClick = true;
          this.domFlag++;
          await initSetTimeOut();
          this.cell = cell;
          this.activeNames.push('modelRelatedDatas');
          this.dataDoubleClick = this.dataClick = this.modelClick = this.codeDoubleClick = false;
        } else if (clickModelType == 'code') {
          this.modelDoubleClick = this.modelClick = this.dataClick = this.dataDoubleClick = false;
          this.codeDoubleClick = true;
        } else {
          this.modelDoubleClick = this.modelClick = this.dataClick = this.codeDoubleClick = false;
          this.dataDoubleClick = true;
          // console.log(cell);
          this.dataNode = cell;
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
            this.modelDoubleClick = this.dataClick = this.dataDoubleClick = this.codeClick = false;
          } else if (clickModelType == 'code') {
            this.codeClick = true;
            this.modelDoubleClick = this.dataClick = this.dataDoubleClick = this.modelClick = false;
          } else {
            this.modelDoubleClick = this.modelClick = this.dataDoubleClick = this.codeClick = false;
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
        }
      });

      // 监听 mxGraph 事件
      this.mxGraphSelectionModel = this.graph.getSelectionModel();
      this.mxGraphSelectionModel.addListener(mxEvent.CHANGE, this.handleSelectionChange);
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
    }
  },

  mounted() {}
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
