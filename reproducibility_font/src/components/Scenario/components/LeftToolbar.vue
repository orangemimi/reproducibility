<!--  -->
<template>
  <div class="leftBar">
    <el-input placeholder="Search model/tool" size="mini" class="search_input">
      <el-button slot="append" icon="el-icon-search"></el-button>
    </el-input>

    <vue-scroll style="height: 905px; width: 100%" :ops="ops">
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
      <!-- el-dialog的destroy-on-close属性在关闭时会再次通知组件，拉黑这个属性，改用v-if -->
      <el-dialog :visible.sync="modelDoubleClick" width="50%" title="Configuration" :close-on-click-modal="false">
        <data-item-toolbar :cell="currentCell" @selectItemListToGraph="selectItemListToGraph" v-if="modelDoubleClick"></data-item-toolbar>
      </el-dialog>
      <el-dialog :visible.sync="dataDoubleClick" width="50%" title="Configuration" :close-on-click-modal="false">
        <!-- Configuration -->
        <data-cell-info :cell="dataNode" @isUpload="isUpload" @dataSelect="dataSelect" v-if="dataDoubleClick"></data-cell-info>
      </el-dialog>
      <el-dialog :visible.sync="codeDoubleClick" width="50%" title="Configuration" :close-on-click-modal="false">
        <data-service-code-configuration @selectItemListToGraph="selectItemListToGraph" v-if="codeDoubleClick"></data-service-code-configuration>
      </el-dialog>
      <el-dialog :visible.sync="dataServiceDoubleClick" width="50%" title="Configuration" :close-on-click-modal="false">
        <data-service-configuration
          :cell="currentCell"
          @selectItemListToGraph="selectItemListToGraph"
          v-if="dataServiceDoubleClick"
        ></data-service-configuration>
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
import bus from './bus';
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
      flag: {
        modelFlag: 0,
        dataFlag: 0,
      },
    };
  },

  watch: {
    flag: {
      handler: function () {
        if (this.flag.dataFlag == 1 && this.flag.modelFlag == 1) {
          this.init();
          this.listenGraphEvent();
        }
      },
      deep: true,
    },
  },

  methods: {
    init() {
      this.graph = this.$parent.graph;
      this.initLeftBar('modelService');
      this.initLeftBar('dataService');
      this.initLeftBar('generalBar');
      this.initLeftBar('codeBar');
    },

    getModels(val) {
      this.$set(this, 'modelItemList', val);
      this.flag.modelFlag = 1;
    },

    getDataServices(val) {
      this.$set(this, 'dataServiceItemList', val);
      this.flag.dataFlag = 1;
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
        this.modelDoubleClick = false;
      } else if (this.dataDoubleClick == true) {
        this.dataDoubleClick = false;
      } else if (this.codeDoubleClick == true) {
        this.codeDoubleClick = false;
      } else if (this.dataServiceDoubleClick == true) {
        this.dataServiceDoubleClick = false;
      }
      console.log(val);
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
      let styleIn = ''
      if(panel == 'modelServiceInput' && item.isParam == 'true') {
        styleIn = differCellStyle('parameter')
      } else {
        styleIn = differCellStyle(panel);
      }
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

        //         makeDraggable: function(	element,
        //                                   graphF,
        //                                    funct,
        //                              dragElement,
        //                                       dx,
        //                                       dy,
        //                               autoscroll,
        //                             scalePreview,
        //                     highlightDropTargets,
        //                            getDropTarget	)

        // Parameters
        //   element	DOM element to make draggable.
        //   graphF	mxGraph that acts as the drop target or a function that takes a mouse event and returns the current mxGraph.
        //   funct	Function to execute on a successful drop.
        //   dragElement	Optional DOM node to be used for the drag preview.
        //   dx	Optional horizontal offset between the cursor and the drag preview.
        //   dy	Optional vertical offset between the cursor and the drag preview.
        //   autoscroll	Optional boolean that specifies if autoscroll should be used.  Default is mxGraph.autoscroll.
        //   scalePreview	Optional boolean that specifies if the preview element should be scaled according to the graph scale.  If this is true, then the offsets will also be scaled.  Default is false.
        //   highlightDropTargets	Optional boolean that specifies if dropTargets should be highlighted.  Default is true.
        //   getDropTarget	Optional function to return the drop target for a given location (x, y).  Default is mxGraph.getCellAt.
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
        console.log(vertex);

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
          vertex.nodeAttribute.oid = item.oid;
          // vertex.nodeAttribute.type = item.type; //Processing
        }
        if (type == 'modelServiceInput' || type == 'modelServiceOutput') {
          console.log(item);
          if (this.selectionCells.length == 0) {
            this.$notify.error({
              title: 'Error',
              message: 'You have not select any model',
            });

            return;
          }

          let selectionCell = this.selectionCells[0];

          vertex.name = item.eventName;
          vertex.type = type; //modelServiceInput or modelServiceOutput
          // vertex.isParameter = item.nodeType == 'parameter' ? true : false;
          // vertex.description = item.description; //event description
          vertex.nodeAttribute.eventId = item.eventId;
          vertex.nodeAttribute.eventDescription = item.eventDescription;
          vertex.nodeAttribute.eventName = item.eventName;
          vertex.nodeAttribute.stateId = item.stateId; //event description
          vertex.nodeAttribute.stateName = item.stateName;
          vertex.nodeAttribute.stateDescription = item.stateDescription;
          vertex.nodeAttribute.optional = item.optional;
          vertex.nodeAttribute.type = item.type;
          vertex.nodeAttribute.md5 = item.md5;
          // vertex.linkModelCellId = selectionCell.id; //放置输入输出node时 与其关联的model的nodeId

          if (type == 'modelServiceInput') {
            vertex.nodeAttribute.dataSelect = {
              value: '',
              dataSelectId: '',
              name: '',
              type: '',
            };
            vertex.nodeAttribute.isParameter = item.isParam;
            this.addEdge(vertex, selectionCell);
          } else if (type == 'modelServiceOutput') {
            this.addEdge(selectionCell, vertex);
            // vertex.nodeAttribute.upload = false;
            vertex.nodeAttribute.value = '';
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
          vertex.nodeAttribute.description = item.description;
          vertex.nodeAttribute.isParameter = item.nodeType == 'parameter' ? 'true' : 'false';
          // vertex.nodeAttribute.dataServiceId = item.dataServiceId;
          // vertex.nodeAttribute.token = item.token;
          // vertex.nodeAttribute.type = item.type; //Processing
          // vertex.linkModelCellId = selectionCell.id; //放置输入输出node时 与其关联的model的nodeId

          if (type == 'dataServiceInput') {
            vertex.nodeAttribute.dataSelect = {
              dataSelectId: '',
              value: '',
              name: '',
              type: '',
            };
            this.addEdge(vertex, selectionCell);
          } else if (type == 'dataServiceOutput') {
            vertex.nodeAttribute.value = '';
            vertex.nodeAttribute.upload = false;
            this.addEdge(selectionCell, vertex);
            // vertex.upload = false;
          }
        }
      } finally {
        this.graph.getModel().endUpdate();
      }
    },

    addEdge(source, target) {
      this.graph.insertEdge(this.graph.getDefaultParent(), null, null, source, target, null);
    },

    isUpload(val) {
      this.dataNode.upload = val;
      console.log(val);
    },
    dataSelect(val) {
      this.dataNode.nodeAttribute.dataSelect = val;
    },

    listenGraphEvent() {
      // 监听双击事件
      this.graph.addListener(mxEvent.DOUBLE_CLICK, async (graph, evt) => {
        // DOUBLE_CLICK
        if (evt.properties.cell != undefined && evt.properties.cell.type != undefined) {
          let cell = evt.properties.cell;
          let clickModelType = cell.type;

          if (clickModelType == 'modelService') {
            console.log(cell);
            this.currentCell = cell;
            this.modelDoubleClick = true;
            // await initSetTimeOut();
            this.dataDoubleClick = this.dataClick = this.modelClick = this.codeDoubleClick = this.dataServiceDoubleClick = false;
          } else if (clickModelType == 'dataService') {
            // await initSetTimeOut();
            this.currentCell = cell;
            console.log(cell);
            this.modelDoubleClick = this.modelClick = this.dataClick = this.dataDoubleClick = this.codeDoubleClick = false;
            this.dataServiceDoubleClick = true;
          } else if (clickModelType == 'code') {
            this.modelDoubleClick = this.modelClick = this.dataClick = this.dataDoubleClick = this.dataServiceDoubleClick = false;
            this.codeDoubleClick = true;
          } else {
            this.modelDoubleClick = this.modelClick = this.dataClick = this.codeDoubleClick = this.dataServiceDoubleClick = false;

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
          this.graph.addSelectionCell(cell);
          bus.$emit('go', this.graph.getSelectionCells());
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
          // console.log(this.graph.getSelectionCells())
          bus.$emit('go', []);
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
      // console.log(this.mxGraphSelectionModel);
      // this.mxGraphSelectionModel = this.graph.getSelectionModel();
      // this.mxGraphSelectionModel.addListener(mxEvent.CHANGE, this.handleSelectionChange);
      // console.log(this.mxGraphSelectionModel);
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
