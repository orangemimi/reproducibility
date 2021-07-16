<!--  -->
<template>
  <div class="main">
    <el-row class="search_input">
      <el-input placeholder="Search model/tool" size="mini">
        <el-button slot="append" icon="el-icon-search"></el-button>
      </el-input>
    </el-row>
    <el-row class="collapse_scroll">
      <vue-scroll style="height: calc(86vh); width: 100%" :ops="ops">
        <el-collapse v-model="activeNames" class="collapse">
          <el-collapse-item title="General" name="general">
            <general-toolbar ref="generalBar"></general-toolbar>
          </el-collapse-item>
          <el-collapse-item title="Models" name="models">
            <!-- <model-item-toolbar ref="modelBar" @getModels="getModels"></model-item-toolbar> -->
            <div v-for="(model, index) in publicModels" :key="index" ref="modelItemList">
              <div class="choose-model-contain">
                <model-card :modelFrom="model" @click.native="getModelInfo(model)"></model-card>
              </div>
            </div>
          </el-collapse-item>
          <el-collapse-item title="Data Processing Methods" name="dataProcessings">
            <data-processing-toolbar ref="dataProcessingBar" @getDataServices="getDataServices"></data-processing-toolbar>
          </el-collapse-item>
          <el-collapse-item title="Related Datas" name="modelRelatedDatas">
            <div v-if="modelDoubleClick">
              <data-item-toolbar :cell="cell" ref="dataItemBar" @getInAndOut="getInAndOut"></data-item-toolbar>
            </div>
          </el-collapse-item>
        </el-collapse>
      </vue-scroll>
    </el-row>
  </div>
</template>

<script>
import mxgraph from '_com/MxGraph/index';
// import modelItemToolbar from '_com/MxGraphBars/ModelItemToolbar';
import dataItemToolbar from '_com/MxGraphDialogs/ModelItemConfiguration';
import generalToolbar from '_com/MxGraphBars/GeneralToolbar';
import dataProcessingToolbar from '_com/MxGraphBars/DataProcessingToolbar';
// import { initSetTimeOut } from '@/utils/utils';

import modelCard from '_com/Cards/MxModelCard';
import { getModelItemsByPrivacy } from '@/api/request';
const {
  // mxGraph,

  mxUtils,
  mxConstants
} = mxgraph;

export default {
  components: {
    // modelItemToolbar,
    dataItemToolbar,
    generalToolbar,
    dataProcessingToolbar,
    modelCard
  },

  watch: {},

  computed: {},

  data() {
    return {
      modelItemList: [],
      inputItemList: [],
      outputItemList: [],
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
      activeNames: ['general', 'models'],
      modelDoubleClick: false,
      graph: this.$parent.graph,

      //
      publicModelFilter: { page: 0, pageSize: 8 },
      publicModels: [],
      switchValue: true
    };
  },

  methods: {
    async init() {
      await this.getPublicModels();

      this.initLeftBar('generalBar');
      this.initLeftBar('modelBar');
    },
    //--------------初始化 bar的modelItem的内容--由 AllModels组件返回
    getModels(val) {
      console.log('modelitm', val);
      this.$set(this, 'modelItemList', val);
    },

    async getPublicModels() {
      let { content } = await getModelItemsByPrivacy('public', this.publicModelFilter.page, this.publicModelFilter.pageSize);
      this.$set(this, 'modelItemList', content);
      // this.$emit('getModels', content);
    },

    getDataServices(val) {
      console.log(val);
    },

    //--------------初始化 bar的dataItem的内容--由 data-item-toolbar组件返回
    getInAndOut(input, output) {
      // this.state = val;

      this.inputItemList = input;
      this.outputItemList = output;
      this.initLeftBar('inputBar');
      this.initLeftBar('outputBar');
    },

    differCellStyle(type) {
      if (type == 'modelBar') {
        return {
          fontColor: '#f6f6f6',
          fillColor: '#07689f',
          strokeColor: '',
          shape: 'rectangle'
        };
      }
      if (type == 'inputBar') {
        return {
          fillColor: '#fff8f8',
          fontColor: '#24292E',
          strokeColor: '',
          shape: 'parallelogram',
          fixedSize: 1
        };
      }
      if (type == 'link') {
        return {
          fillColor: 'rgb(255, 220, 220)',
          fontColor: '#24292E',
          strokeColor: '',
          shape: 'parallelogram',
          fixedSize: 1
        };
      }
      if (type == 'outputBar') {
        return {
          fillColor: '#b9e6d3', //b9e6d3 f4d160
          fontColor: '#24292E',
          strokeColor: '',
          shape: 'parallelogram'
        };
      }
      return {
        fillColor: '#b9e6d3', //b9e6d3 f4d160
        fontColor: '#24292E',
        strokeColor: '',
        shape: 'rhombus'
      };
    },

    initLeftBar(panel) {
      let refType;
      let barRef; //对应各个Bar组件中的ref
      let barItemList = [];
      let styleIn = this.differCellStyle(panel);

      if (panel == 'modelBar') {
        refType = 'modelBar';
        barRef = 'modelItemList';
        barItemList = this.modelItemList;
      } else if (panel == 'inputBar') {
        refType = 'dataItemBar';
        barRef = 'inputItemList';
        barItemList = this.inputItemList;
      } else if (panel == 'outputBar') {
        refType = 'dataItemBar';
        barRef = 'outputItemList';
        barItemList = this.outputItemList;
      } else {
        refType = 'generalBar';
        barRef = 'general';
        barItemList = this.generalList;
      }
      console.log(refType, barRef, this.modelItemList);

      // const domArray = this.$refs[refType].$refs[barRef];
      const domArray = this.$refs['modelItemList'];

      if (!(domArray instanceof Array) || domArray.length <= 0) {
        return;
      }

      domArray.forEach((dom, domIndex) => {
        const dragItem = barItemList[domIndex];

        let cellStyle = this.getCellStyle(styleIn, dragItem);

        const dropHandler = (graph, evt, cell, x, y) => {
          this.addCell(dragItem, x, y, panel, cellStyle);
        };

        const createDragPreview = () => {
          const elt = document.createElement('div');
          elt.style.border = '2px dotted black';
          elt.style.width = `200px`;
          elt.style.height = `50px`;
          elt.shape = styleIn.shape;
          return elt;
        };

        mxUtils.makeDraggable(dom, this.graph, dropHandler, createDragPreview(), 0, 0, false, true);
      });
    },

    getCellStyle(styleIn, cell) {
      // console.log(item);
      let styleObj = {
        ...styleIn,
        strokeWidth: '1.5',
        align: mxConstants.ALIGN_CENTER,
        // verticalAlign: mxConstants.ALIGN_,
        imageAlign: mxConstants.ALIGN_CENTER,
        imageVerticalAlign: mxConstants.ALIGN_TOP
      };

      if (cell.optional == 'false' && (cell.type == 'input' || cell.type == 'link')) {
        styleObj.strokeColor = '#d13030';
      }

      //转换成cell中的style格式
      const style = Object.keys(styleObj)
        .map(attr => `${attr}=${styleObj[attr]}`)
        .join(';');

      return style;
    },

    addCell(item, x, y, type, styleObj) {
      this.graph.getModel().beginUpdate();

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

      try {
        if (type == 'modelBar') {
          vertex.name = item.name;
          vertex.doi = item.doi;
          vertex.type = 'model';
          vertex.iterationNum = '1';
          vertex.md5 = item.md5;
        } else {
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
          vertex.type = item.type;

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
    }
  },

  mounted() {
    this.init();
  }
};
</script>
<style lang="scss" scoped>
.main {
  width: 300px;
  position: relative;
  //   display: flex;
  //   position: relative;
  .search_input {
    padding: 0 10px 5px 0;
    width: 300px;
  }
  .collapse_scroll {
    position: absolute;
    left: 0;
    top: 30px;
    .collapse {
      width: 300px;
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
