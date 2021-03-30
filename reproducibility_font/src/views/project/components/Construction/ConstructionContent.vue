<template>
  <div class="main">
    <div class="mainContainerMask" v-show="isNewTaskContainerShow">
      <div class="taskInfoForm">
        <el-form ref="taskInfoForm" :model="taskInfo" label-width="180px">
          <el-form-item label="Task Name">
            <el-input v-model="taskInfo.taskName"></el-input>
          </el-form-item>
          <el-form-item label="Task Description">
            <el-input v-model="taskInfo.taskDescription"></el-input>
          </el-form-item>
        </el-form>
      </div>

      <!-- 进入，如果没有task，则弹出创建一个新画布 -->
      <div class="taskInfoBtn">
        <el-button @click="createNewTask" class="createTask">Create a New Task</el-button>
        <div class="or">or</div>
        <integrate-tasks @selectTask="selectTask" class="selectTask"></integrate-tasks>
        <div class="newTaskBtnTip">
          Please Enter the task name and description to create your task!
        </div>
      </div>
    </div>
    <div class="customToolbarContainer" v-show="!isNewTaskContainerShow">
      <mx-graph :sendXml="mxgraph"></mx-graph>
    </div>
  </div>
</template>

<script>
import { get, post, patch } from '@/axios';
import mxGraph from './components/MxGraph';
import { mapState } from 'vuex';

import integrateTasks from './../Construction/IntegrateTasks';
export default {
  props: {
    projectInfo: {
      type: Object
    }
  },
  watch: {
    projectInfo: {
      handler(val) {
        if (val != null) {
          // this.projectInfomation = val;
        }
      }
    }
  },

  components: {
    mxGraph,
    integrateTasks
  },

  computed: {
    ...mapState({
      userId: state => state.user.userId,
      userName: state => state.user.name
    }),

    sendFlowChartFromXml() {
      return `<mxGraphModel><root><mxCell id="0"/><mxCell id="1" parent="0"/>${this.sendXml}</root></mxGraphModel>`;
    }
  },
  data() {
    return {
      projectId: this.projectInfo.id,
      stepInfo: {
        stepName: this.$route.params.stepName,
        stepId: this.$route.params.stepId,
        stepType: this.$route.params.stepType
      },
      // inheritResources: this.$route.params.inheritResources,
      contentWidth: 0,
      contentHeight: 0,
      selectedResources: {},
      ops: {
        bar: {
          background: '#808695'
        }
      },
      isOpenTool: false,
      typeSelected: 'All',
      typeOptions: [
        'All',
        'General step',
        'Context definition & resource collection',
        'Data processing',
        'Data visualization',
        'Geographic model construction',
        'Model effectiveness evaluation',
        'Geographical simulation',
        'Quantitative and qualitative analysis',
        'Decision-making for management',
        'Others'
      ],
      panelList: [],
      jupyterModal: false,
      currentModelInfo: {
        toolUrl: '',
        toolId: ''
      },
      modelUrl: '',
      modelDoi: '16e31602-bd05-4da4-bd01-cb7582c21ae8',
      // pageParams: {},
      modelInstances: [],
      instanceFolk: { id: '' },
      checkedInstances: [],
      // dataNodes: [],
      // dataNodesIntermedia: [],
      // mxNodes: [],
      // sendXml: "",
      // xml: "", //save to mongodb
      // updateXml: false,
      flowChartSent: {},

      //getOutputs
      record: {},
      timer: {},
      stateList: [],
      checkAll: false,
      checkedForMap: [],
      // nextStartIndex: 0,
      deleteInstanceDialog: false,
      deleteInstanceId: '',
      filterCreateMapInstances: [],

      activeTabs: 'modelItem',
      updateXml: false,
      initXml: '',
      // directDataResource: [],
      // inDirectDataResource: [],

      //
      publicTools: [],
      personalTools: [],

      //task
      isNewTaskContainerShow: true,
      currentTask: {},
      taskInfo: {
        taskName: '',
        taskDescription: ''
      },
      taskList: [],
      mxgraph: '' //task selected mxgraph
    };
  },

  methods: {
    initSize() {
      //   this.contentWidth = window.innerWidth - 450;
      // this.contentHeight = window.innerHeight - 260;
    },
    async init() {
      // this.getPageParams();
      await this.getPublicTools();
      await this.getPersonalTools();
      await this.getDataItem();
      await this.getMap();
    },

    async getPublicTools() {
      let data = await get('/tool/inquiry/?key=privacy&value=Public');
      this.$set(this, 'publicTools', data);
    },

    async getPersonalTools() {
      let data = await get(`/tool/findByProvider/${this.userId}`);
      this.$set(this, 'personalTools', data);
    },

    chooseTool(toolInfo) {
      console.log(toolInfo);
      if (toolInfo.toolName == 'Jupyter notebook') {
        this.jupyterModal = true;
        return;
      }
      this.activeTabs = 'modelItem';
      this.currentModelInfo.toolId = toolInfo.tid;
      this.currentModelInfo.toolUrl = toolInfo.toolUrl;
    },

    async getDataItem() {
      let dataItem = await get(`/dataItems/${this.projectInfo.id}`);
      this.$set(this.selectedResources, 'dataItemList', dataItem);
      if (this.$route.params.isInherit) {
        this.selectedResources.dataItemList = dataItem.filter(
          item =>
            (item.stepInherit != null && item.stepInherit.some(id => id == this.stepInfo.stepId)) ||
            item.stepBindId == this.stepInfo.stepId
        );
      } else {
        this.selectedResources.dataItemList = dataItem.filter(
          item => item.stepBindId == this.stepInfo.stepId || item.isDirect == true
        );
      }
    },

    async getOutputs(instance) {
      this.stateList = JSON.parse(instance.states);
      // console.log(instance, this.stateList);?
      let refreshForm = {};
      refreshForm['ip'] = instance.ip;
      refreshForm['port'] = instance.port;
      refreshForm['tid'] = instance.tid;
      //获得结果
      this.record = {};
      this.timer = setInterval(async () => {
        if (this.record.status == 2) {
          clearInterval(this.timer);
          await this.getStateEventOut(this.record, instance);
          return;
        } else {
          let { data } = await post('/modelTask/getRecord', instance);
          this.record = data;
        }
      }, 2000);
    },

    async getStateEventOut(record, instance) {
      let outList = this.stateList;
      let outputUrl = record.outputs;
      outList.forEach((state, index) => {
        state.Event.forEach((event, eventIndex) => {
          outputUrl.forEach(el => {
            if (el.statename == state.name && el.event == event.name) {
              this.$set(this.stateList[index].Event[eventIndex], 'url', el.url);
            }
          });
        });
      });
      // console.log(this.stateList);
      let stepResource = {
        states: JSON.stringify(this.stateList),
        status: record.status //2 finish
      };
      let { data } = await post(`/modelInstance/update/${instance.id}`, stepResource);
      this.saveModelInstance(data);
    },

    //findindexof--改进
    saveModelInstance(instance) {
      this.modelInstances.forEach((item, index) => {
        if (item.id == instance.id) {
          this.modelInstances.splice(index, 1);
        }
      });
      this.modelInstances.push(instance);
    },

    getModelDoi(url) {
      let arr = url.split('/');
      this.modelDoi = arr[arr.length - 1];
    },

    async mapCreate() {
      this.filterCreateMapInstances = this.filterModelInstance.filter(
        instance => instance.checkedForMap == true
      );

      if (this.filterCreateMapInstances == '') {
        this.$message({
          message: 'There is no selected instance',
          type: 'error'
        });
      } else {
        this.checkedInstances = [...this.filterCreateMapInstances];
        this.activeTabs = 'conceptMap';
      }
    },

    async getMap() {
      let data = await get(`/flowcharts/one/${this.stepInfo.stepId}`);
      if (data == null) {
        this.updateXml = false;
      } else {
        // this.getSelectedInstances(data.modelInstanceIdList);
        this.updateXml = true;
        this.initXml = data.mapXml;

        let modelInstanceIdList = data.modelInstanceIdList;
        this.modelInstances.forEach(item => {
          if (modelInstanceIdList.some(id => id == item.id)) {
            item.checkedForMap = true;
          }
        });
      }
    },

    async emitxml(val) {
      await this.createFlowChart(val);
    },

    async createFlowChart(xml) {
      // console.log(xml)
      let instances = Array.from(this.checkedInstances, ({ id }) => id);

      let flowChartSent = {
        modelInstanceIdList: instances,
        mapXml: xml
      };

      if (this.updateXml) {
        let data = await patch(`/flowcharts/${this.stepInfo.stepId}`, flowChartSent);
        if (data != null) {
          this.$message({
            message: 'You have update this map successfully',
            type: 'success'
          });
        }
      } else {
        flowChartSent.stepId = this.stepInfo.stepId;
        flowChartSent.stepName = this.stepInfo.stepName;
        flowChartSent.type = this.stepInfo.stepType;
        flowChartSent.userId = this.userId;
        flowChartSent.pid = this.projectInfo.id;

        let data = await post(`/flowcharts`, flowChartSent);
        if (data != null) {
          this.$message({
            message: 'You have created this map successfully',
            type: 'success'
          });
        }
        this.updateXml = true;
      }
      this.initXml = xml;
    },

    async createNewTask() {
      let postJson = {
        projectId: this.projectInfo.id,
        ...this.taskInfo
      };
      // console.log(postJson);
      let data = await post(`/integrateTasks`, postJson);
      this.currentTask = data;
      this.isNewTaskContainerShow = false;
      // console.log(data);
    },

    selectTask(val) {
      //选择task之后的回调
      this.currentTask = val;
      this.taskInfo = val;
      this.mxgraph = val.mxgraph;
      // this.importModelXML(mxgraph);
      this.isNewTaskContainerShow = false;
    }
  },

  created() {
    // this.initSize();
    // if (this.userName) {
    //   this.init();
    // }
  }
  // mounted() {
  //   this.getIntegrateTasks();
  // }
};
</script>
<style lang="scss" scoped>
.main {
  margin: 20px 0;
  height: 100%;
  width: 100%;

  // .main {
  // width: 100%;
  // height: 100%;
  // display: flex;
  // position: relative;
  .mainContainerMask {
    // display: flex;
    position: relative;
    width: 100%;
    .taskInfoForm {
      margin: 100px 190px;
      width: 700px;
    }
    .taskInfoBtn {
      // margin: 80px 50px;
      text-align: center;
      .createTask {
        margin-left: 400px;
        width: 200px;
        float: left;
      }
      .or {
        margin-left: 8px;
        width: 10px;
        font-size: 18px;
        line-height: 40px;
        float: left;
      }
      .selectTask {
        width: 200px;
        line-height: 40px;
        float: left;
      }
      .newTaskBtnTip {
        clear: both;
        margin-top: 30px;
        line-height: 50px;
        font-style: italic;
        color: rgb(102, 102, 102);
        font-size: 14px;
      }
    }
    z-index: 999;
  }
}
.instance-name {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  word-break: break-all;
  float: left;
  width: 200px;
  text-align: left;
}
.data-info {
  height: 20px;
}
.data-info-hover {
  display: none;
}
.data-info-name {
  float: left;
  width: 220px;
}
.data-info:hover {
  cursor: pointer;
  background-color: rgba(99, 142, 197, 0.2);

  //  -webkit-transform: translateY(-4px);
  //     -ms-transform: translateY(-4px);
  //     transform: translateY(-4px);
  -webkit-transition: all 0.2s ease-out;
  transition: all 0.2s ease-out;
  .data-info-hover {
    display: block;
    float: left;
    // right: 5px;
    color: rgb(45, 54, 92);
    // clear: both;
  }
}
.box-card {
  height: 100px;
  margin-bottom: 10px;
}
.finishing {
  // background-color: aqua;
}
.calculating {
  background-color: rgba(205, 231, 211, 0.2);
  border-color: rgba(205, 231, 211, 0.2);
  box-shadow: 0px 0px 5px rgb(127, 148, 131);
  border-bottom: 2px solid rgb(27, 73, 38);
  text-align: center;
}
.calculating-icon {
  color: rgb(30, 44, 35);
  font-weight: 600;
  text-align: center;
}
.error {
  background-color: rgba(231, 205, 205, 0.2);
  border-color: rgba(231, 205, 205, 0.2);
  box-shadow: 0px 0px 5px rgb(148, 127, 127);
  border-bottom: 2px solid rgb(73, 27, 27);
  text-align: center;
}
.error-icon {
  color: rgb(202, 37, 37);
  font-weight: 600;
  text-align: center;
}
.noCurrentModel {
  font-size: 40px;
  color: rgb(136, 136, 136);
  font-weight: 600;

  position: absolute;
  top: 20%;
  left: 15%;
}
.process-data {
  >>> .el-card--body {
    padding: 15px;
  }
}
.tool-top {
  padding: 5px 0;
  font-size: 16px;
  font-weight: 600;
  height: 40px;
  .tool-title {
    float: left;
    width: 100px;
  }
}
</style>
