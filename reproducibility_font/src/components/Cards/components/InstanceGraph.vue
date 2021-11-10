<template>
  <div>
    <el-row>
      <el-col :span="18">
        <vue-scroll style="height: 100%; width: 100%">
          <div ref="container" class="container" @contextmenu.prevent></div>
        </vue-scroll>
      </el-col>
      <el-col :span="6">
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
      </el-col>
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
              <div class="dataTitle">description:</div>
              <div class="dataDetail">{{ clickedCell.description }}</div>
            </div>

            <div class="data" v-if="clickedCell.value != '' && clickedCell.upload == 1">
              <div class="dataTitle">value:</div>
              <div class="dataDetail"><el-button type="primary" @click="download(clickedCell.value)">DownLoad</el-button></div>
            </div>
            <div class="data" v-if="clickedCell.value != '' && clickedCell.upload == 0">
              <div class="dataTitle">value:</div>
              <div class="dataDetail">{{ clickedCell.value }}</div>
            </div>
            <div class="data" v-if="clickedCell.value == ''">
              <div class="dataTitle">value:</div>
              <div class="dataDetail">Please wait for the calculation result!</div>
            </div>
          </el-row>
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
    <el-dialog title="Detail" :visible.sync="inputDialogVisible" width="50%" :modal="false">
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
              <div class="dataTitle">Description:</div>
              <div class="dataDetail">{{ clickedCell.description }}</div>
            </div>
            <div class="data">
              <div class="dataTitle">Type:</div>
              <div class="dataDetail" v-if="clickedCell.isParameter">Parameter</div>
              <div class="dataDetail" v-else>File</div>
            </div>
          </el-row>
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
import { getInstanceXML, getTaskInfo } from '@/api/request';
const {
  // mxGraph
  mxGraphHandler,
  mxEvent,
} = mxgraph;

export default {
  props: {
    instanceId: {
      type: String,
    },
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
      clickedCell: '',
    };
  },
  methods: {
    async init() {
      await this.getInstanceXML();
      await this.getTaskInfo();
      console.log(this.taskInfo);
      this.createGraph();
      this.createCell();
      this.addListener();
    },

    async getInstanceXML() {
      this.taskConfig = await getInstanceXML(this.instanceId);
    },

    async getTaskInfo() {
      let data = await getTaskInfo(this.instanceId);
      data.dataProcessingList.failed.forEach((model) => {
        model.result = 0;
      });
      data.modelActionList.failed.forEach((model) => {
        model.result = 0;
      });
      this.taskInfo = {
        running: [...data.dataProcessingList.running, ...data.modelActionList.running],
        finished: [...data.dataProcessingList.completed, ...data.modelActionList.completed, ...data.dataProcessingList.failed, ...data.modelActionList.failed],
      };
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
    download(url) {
      console.log(url);
      location.href = url;
    },
  },
  mounted() {
    this.init();
  },
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
</style>