<template>
  <div class="main">
    <div class="mainContainerMask" v-show="isNewTaskShow">
      <div class="taskInfoForm">
        <el-form ref="taskInfoForm" :model="taskInfo" label-width="180px">
          <el-form-item label="Task Name">
            <el-input v-model="taskInfo.taskName"></el-input>
          </el-form-item>
          <el-form-item label="Task Description">
            <el-input v-model="taskInfo.taskDescription"></el-input>
          </el-form-item>
          <el-form-item label="Task Type">
            <el-select v-model="taskInfo.type" placeholder="Please select">
              <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
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
    <div class="customToolbarContainer" v-show="isIntegrateTaskShow">
      <mx-graph :taskInfoInit="taskInfoInit" v-if="taskInfoInit != null"></mx-graph>
    </div>
    <div class="customToolbarContainer" v-show="isNoteTaskShow">
      <comparison-task :taskInfoInit="taskInfoInit" v-if="taskInfoInit != null"></comparison-task>
    </div>
    <div class="customToolbarContainer" v-show="isRDTaskShow">
      <RDDocument :taskInfoInit="taskInfoInit" v-if="taskInfoInit != null"></RDDocument>
    </div>
  </div>
</template>

<script>
import { getScenarioByProjectId, updateScenarioByProjectId, saveIntegrateTask, getIntegrateTaskByTaskId } from '@/api/request';
import mxGraph from './components/MxGraph';
import { mapState } from 'vuex';
import integrateTasks from '_com/IntegrateTasks';
import comparisonTask from '_com/Comparison/Comparison';
import RDDocument from '@/components/RDDocument/RDDocument.vue';
// import { initSetTimeOut } from '@/utils/utils';

export default {
  components: {
    mxGraph,
    integrateTasks,
    comparisonTask,
    RDDocument
  },

  computed: {
    ...mapState({
      userId: state => state.user.userId,
      userName: state => state.user.name
    })
  },
  data() {
    return {
      projectId: this.$route.params.id,
      scenario: {},
      isNewTaskShow: false,
      isIntegrateTaskShow: false,
      isNoteTaskShow: false,
      isRDTaskShow: false,
      taskInfo: { type: 'integrateTask' },
      taskInfoInit: null,
      taskList: [],
      content: '', //task selected mxgraph
      typeOptions: [
        { label: 'Integrate Task', value: 'integrateTask' },
        { label: 'Notebook', value: 'notebook' },
        { label: 'RDDocument', value: 'RDDocument' }
      ]
    };
  },

  methods: {
    async init() {
      await this.getScenario();
      // await this.createNewTask();
    },

    async getScenario() {
      let data = await getScenarioByProjectId(this.projectId);
      if (data == null || data.selectTaskId == '' || data.selectTaskId == null) {
        this.isNewTaskShow = true;
        return;
      } else {
        if (data.type == 'integrateTask') {
          let data2 = await getIntegrateTaskByTaskId(data.selectTaskId);
          this.$set(this, 'taskInfoInit', data2);
          this.isRDTaskShow = this.isNoteTaskShow = this.isNewTaskShow = false;
          this.isIntegrateTaskShow = true;
        }
        if (data.type == 'notebook') {
          this.isRDTaskShow = this.isIntegrateTaskShow = this.isNewTaskShow = false;
          this.isNoteTaskShow = true;
          this.taskInfoInit = await getIntegrateTaskByTaskId(data.selectTaskId);
        }
        if (data.type == 'RDDocument') {
          this.isNoteTaskShow = this.isIntegrateTaskShow = this.isNewTaskShow = false;
          this.isRDTaskShow = true;
          this.taskInfoInit = await getIntegrateTaskByTaskId(data.selectTaskId);
        }
      }
    },

    async createNewTask() {
      // let noteType = '';
      this.isNewTaskShow = false;
      let postJson = {
        projectId: this.projectId,
        taskContent: '',
        type: this.taskInfo.type,
        ...this.taskInfo
      };
      if (this.taskInfo.type == 'integrateTask') {
        postJson.taskContent = '<mxGraphModel><root><mxCell id="0"/><mxCell id="1" parent="0"/></root></mxGraphModel>';
        this.isIntegrateTaskShow = true;
      }
      if (this.taskInfo.type == 'notebook') {
        this.isNoteTaskShow = true;
      }
      if (this.taskInfo.type == 'RDDocument') {
        postJson.processes = [];
        this.isRDTaskShow = true;
      }
      let data = await saveIntegrateTask(postJson);
      this.taskInfoInit = data;
      await updateScenarioByProjectId(this.projectId, { selectTaskId: data.id, type: this.taskInfo.type });
      // await initSetTimeOut();
    },

    selectTask(val) {
      //选择task之后的回调
      this.taskInfoInit = val;
      this.isIntegrateTaskShow = false;
    }
  },

  created() {
    this.init();
  }
};
</script>
<style lang="scss" scoped>
.main {
  // margin: 20px 0;
  height: 100%;
  width: 100%;

  .mainContainerMask {
    // display: flex;
    position: relative;
    width: 100%;
    .taskInfoForm {
      margin: 100px 240px;
      width: 700px;
    }
    .taskInfoBtn {
      text-align: center;
      width: 100%;

      .createTask {
        margin-left: 475px;
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
</style>
