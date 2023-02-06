<template>
  <div>
    <el-row>
      <el-col :span="18">
        <vue-scroll style="height: 100%; width: 100%">
          <div ref="container" class="container" @contextmenu.prevent></div>
        </vue-scroll>
      </el-col>
      <!-- <el-col :span="6">
        <vue-scroll style="height: 100%; width: 100%" v-if="taskInfo != ''">
          <el-timeline style="margin-left: 10px">
            <el-timeline-item
              timestamp="2018/4/3"
              placement="top"
              v-for="(iteam, index) in taskInfo.finished"
              :key="index"
              :type="iteam.result == 0 ? 'danger' : 'success'"
            >
              <el-card class="card" :class="iteam.result == 0 ? 'failed' : 'completed'">
                <h4>{{ iteam.name }}</h4>
                <p>{{ iteam.id }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </vue-scroll>
      </el-col> -->
    </el-row>

    <el-dialog title="Detail" :visible.sync="outputDialogVisible" width="50%" :modal="false">
      <el-tabs>
        <el-tab-pane>
          <template #label>
            <i class="el-icon-s-order"></i>
            Data
          </template>
          <el-row class="dataInfo">
            <div class="data">
              <div class="dataTitle">name:</div>
              <div class="dataDetail">{{ clickedCell.name }}</div>
            </div>
            <div class="data">
              <div class="dataTitle">Event Description:</div>
              <div class="dataDetail">{{ clickedCell.nodeAttribute.eventDescription }}</div>
            </div>

            <div class="data" v-if="!getOutputValue">
              <div class="dataTitle">value:</div>
              <div class="dataDetail">Please wait for the calculation result!</div>
            </div>
          </el-row>
          <div class="btn" v-if="getOutputValue">
            <el-button type="primary" plain size="small" @click="download('output')">Download</el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane>
          <template #label>
            <i class="el-icon-s-platform"></i>
            View
          </template>
          <el-empty description="This data does not support visualization temporarily"></el-empty>
        </el-tab-pane>
      </el-tabs>

      <el-divider></el-divider>
    </el-dialog>
    <el-dialog title="Detail" :visible.sync="inputDialogVisible" width="50%" :modal="false" v-if="inputDialogVisible">
      <el-tabs>
        <el-tab-pane>
          <template #label>
            <i class="el-icon-s-order"></i>
            Data
          </template>
          <el-row class="dataInfo">
            <div class="data">
              <div class="dataTitle">Name:</div>
              <div class="dataDetail">{{ clickedCell.name }}</div>
            </div>
            <div class="data">
              <div class="dataTitle">Event Description:</div>
              <div class="dataDetail">{{ clickedCell.nodeAttribute.eventDescription }}</div>
            </div>
            <div class="data">
              <div class="dataTitle">Type:</div>
              <div class="dataDetail" v-if="clickedCell.nodeAttribute.isParameter == 'true'">Parameter</div>
              <div class="dataDetail" v-else>File</div>
            </div>
            <div class="data" v-if="clickedCell.nodeAttribute.isParameter == 'true'">
              <div class="dataTitle">Value:</div>
              <div class="dataDetail">{{ clickedCell.nodeAttribute.dataSelect.value }}</div>
            </div>
          </el-row>
          <div class="btn" v-if="clickedCell.nodeAttribute.isParameter == 'false'">
            <el-button type="primary" plain size="small" @click="download('input')">Download</el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane>
          <template #label>
            <i class="el-icon-s-platform"></i>
            View
          </template>
          <el-empty description="This data does not support visualization temporarily"></el-empty>
        </el-tab-pane>
      </el-tabs>

      <el-divider></el-divider>
    </el-dialog>
  </div>
</template>

<script>
import { genGraph } from '_com/MxGraph/initMx';
import mxgraph from '_com/MxGraph/index';
import { getCellStyle } from '_com/Scenario/components/configuration.js';
const {
  // mxGraph
  mxGraphHandler,
  mxEvent
} = mxgraph;

export default {
  props: {
    instance: {
      type: Object
    }
  },
  computed: {
    getOutputValue() {
      let id = this.clickedCell.id;
      if (this.clickedCell.type == 'modelServiceOutput') {
        for (let i = 0; i < this.taskInfo.modelActionList.completed.length; i++) {
          let temp = this.taskInfo.modelActionList.completed[i].outputData.outputs;
          for (let j = 0; j < temp.length; j++) {
            if (temp[j].dataId == id) {
              return true;
            }
          }
        }
      }
      if (this.clickedCell.type == 'dataServiceOutput') {
        for (let i = 0; i < this.taskInfo.dataProcessingList.completed.length; i++) {
          let temp = this.taskInfo.dataProcessingList.completed[i].outputData.outputs;
          for (let j = 0; j < temp.length; j++) {
            if (temp[j].dataId == id) {
              return true;
            }
          }
        }
      }
      return false;
    }
  },

  data() {
    return {
      container: null,
      graph: '',
      taskConfig: '',
      taskInfo: '',
      outputDialogVisible: false,
      inputDialogVisible: false,
      type: '',
      clickedCell: {
        nodeAttribute: {
          dataSelect: {}
        }
      }
    };
  },

  watch: {
    instance: {
      handler() {
        this.changeCell();
        // console.log(this.graph.getModel())
        // console.log(this.taskInfo)
      }
    }
  },
  methods: {
    init() {
      this.getInstanceXML();

      this.createGraph();
      this.createCell();
      this.addListener();
      this.changeCell();
    },

    getInstanceXML() {
      this.taskConfig = this.instance.taskContent;
    },

    getTaskInfo() {
      this.taskInfo = this.instance.taskInfo;
    },

    createGraph() {
      this.container = this.$refs.container;
      this.graph = genGraph(this.container);
      this.graph.setPanning(true);
    },
    createCell() {
      this.graph.importGraph(this.taskConfig);
      this.graph.getModel().beginUpdate();
      try {
        this.graph.setCellsResizable(false);
        mxGraphHandler.prototype.setMoveEnabled(false);
      } finally {
        this.graph.getModel().endUpdate();
      }
    },
    addListener() {
      this.graph.addListener(mxEvent.CLICK, (sender, evt) => {
        let cell = evt.getProperty('cell'); // cell may be null
        if (cell != undefined && cell.type != undefined) {
          this.type = cell.type;
          this.clickedCell = cell;
          if (cell.type == 'dataServiceInput' || cell.type == 'modelServiceInput') {
            console.log(cell);
            this.inputDialogVisible = true;
          } else if (cell.type == 'dataServiceOutput' || cell.type == 'modelServiceOutput') {
            console.log(cell);
            this.outputDialogVisible = true;
          } else if (cell.type == 'dataServiceLink' || cell.type == 'modelServiceLink') {
            console.log(cell);
            // this.dialogVisible = true;
          } else if (cell.type == 'dataService' || cell.type == 'modelService') {
            console.log(cell);
            // this.dialogVisible = true;
          }
        }
      });
    },
    download(type) {
      if (type == 'output') {
        let modelList = this.instance.taskInfo.modelActionList.completed;
        let dataList = this.instance.taskInfo.dataProcessingList.completed;
        let list = modelList.concat(dataList);
        console.log(list);
        for (let i = 0; i < list.length; i++) {
          for (let j = 0; j < list[i].outputData.outputs.length; j++) {
            if (this.clickedCell.id == list[i].outputData.outputs[j].dataId) {
              location.href = list[i].outputData.outputs[j].dataContent.value;
              return;
            }
          }
        }
      }
      if (type == 'input') {
        location.href = this.clickedCell.nodeAttribute.dataSelect.value;
      }
    },

    changeCell() {
      this.getTaskInfo();
      this.taskInfo.modelActionList.running.forEach(item => {
        this.changeCellStyleByStatus(0, this.graph.getModel().cells[item.id], false);
        item.inputData.inputs.forEach(input => {
          this.changeCellStyleByStatus(0, this.graph.getModel().cells[input.dataId], true);
        });
        item.outputData.outputs.forEach(output => {
          this.changeCellStyleByStatus(0, this.graph.getModel().cells[output.dataId], true);
        });
      });
      this.taskInfo.modelActionList.failed.forEach(item => {
        this.changeCellStyleByStatus(2, this.graph.getModel().cells[item.id], false);
        item.inputData.inputs.forEach(input => {
          this.changeCellStyleByStatus(2, this.graph.getModel().cells[input.dataId], true);
        });
        item.outputData.outputs.forEach(output => {
          this.changeCellStyleByStatus(2, this.graph.getModel().cells[output.dataId], true);
        });
      });
      this.taskInfo.modelActionList.completed.forEach(item => {
        this.changeCellStyleByStatus(1, this.graph.getModel().cells[item.id], false);
        item.inputData.inputs.forEach(input => {
          this.changeCellStyleByStatus(1, this.graph.getModel().cells[input.dataId], true);
        });
        item.outputData.outputs.forEach(output => {
          this.changeCellStyleByStatus(1, this.graph.getModel().cells[output.dataId], true);
        });
      });
      this.taskInfo.dataProcessingList.running.forEach(item => {
        this.changeCellStyleByStatus(0, this.graph.getModel().cells[item.id], false);
        item.inputData.inputs.forEach(input => {
          this.changeCellStyleByStatus(0, this.graph.getModel().cells[input.dataId], true);
        });
        item.outputData.outputs.forEach(output => {
          this.changeCellStyleByStatus(0, this.graph.getModel().cells[output.dataId], true);
        });
      });
      this.taskInfo.dataProcessingList.failed.forEach(item => {
        this.changeCellStyleByStatus(2, this.graph.getModel().cells[item.id], false);
        item.inputData.inputs.forEach(input => {
          this.changeCellStyleByStatus(2, this.graph.getModel().cells[input.dataId], true);
        });
        item.outputData.outputs.forEach(output => {
          this.changeCellStyleByStatus(2, this.graph.getModel().cells[output.dataId], true);
        });
      });
      this.taskInfo.dataProcessingList.completed.forEach(item => {
        this.changeCellStyleByStatus(1, this.graph.getModel().cells[item.id], false);
        item.inputData.inputs.forEach(input => {
          this.changeCellStyleByStatus(1, this.graph.getModel().cells[input.dataId], true);
        });
        item.outputData.outputs.forEach(output => {
          this.changeCellStyleByStatus(1, this.graph.getModel().cells[output.dataId], true);
        });
      });
    },

    changeCellStyleByStatus(status, item, isData) {
      //runnning
      let style;
      if (status == 0) {
        style = {
          fontColor: '#f6f6f6',
          fillColor: '#E6A23C'
        };
      }
      //finish
      if (status == 1) {
        style = {
          fillColor: '#67C23A',
          fontColor: '#24292E'
        };
      } //error
      if (status == 2) {
        style = {
          fillColor: '#ce1212',
          fontColor: '#f6f6f6'
        };
      }
      if (isData) {
        style.shape = 'parallelogram';
        style.fixedSize = 1;
      }
      let styleIn = getCellStyle(style, item);
      this.graph.getModel().setStyle(item, styleIn);
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style lang="scss" scoped>
.container {
  min-width: calc(100%);
  min-height: calc(100vh);
  background: rgb(251, 251, 251) url('./../../../assets/images/mxgraph/point.gif') 0 0 repeat;
}
.card {
  min-height: 100px;
}
.completed {
  background-color: #f0f9eb;
  border: 1px solid #67c23a;
  border-top: 5px solid #67c23a;
}
.failed {
  background-color: #fdf6ec;
  border: 1px solid #e6a23c;
  border-top: 5px solid #e6a23c;
}
.dataInfo {
  .data {
    font-size: 16px;
    line-height: 30px;

    .dataTitle {
      clear: both;
      font-weight: 600;
      width: 150px;
      float: left;
    }
    .dataDetail {
      float: left;
      // width: 700px;
    }
  }
}
.btn {
  margin-left: 20px;
  margin-top: 10px;
  float: left;
}
</style>
