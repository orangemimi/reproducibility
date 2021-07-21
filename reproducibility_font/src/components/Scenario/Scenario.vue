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
      <mx-graph :taskInfoInit="taskInfoInit"></mx-graph>
    </div>
    <div class="customToolbarContainer" v-show="isNoteTaskShow">
      <comparison-task :taskInfoInit="taskInfoInit"></comparison-task>
    </div>
  </div>
</template>

<script>
import { getScenarioByProjectId, updateScenarioByProjectId, saveIntegrateTask, getIntegrateTaskByTaskId } from '@/api/request';
import mxGraph from './components/MxGraph';
import { mapState } from 'vuex';
import integrateTasks from '_com/IntegrateTasks';
import comparisonTask from '_com/Comparison/Comparison';
// import { initSetTimeOut } from '@/utils/utils';

export default {
  components: {
    mxGraph,
    integrateTasks,
    comparisonTask
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
      isNewTaskShow: true,
      isIntegrateTaskShow: false,
      isNoteTaskShow: false,
      taskInfo: { type: 'integrateTask' },
      taskInfoInit: { content: '' },
      taskList: [],
      content: '', //task selected mxgraph
      typeOptions: [
        { label: 'Integrate Task', value: 'integrateTask' },
        { label: 'Notebook', value: 'notebook' }
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
          this.isNoteTaskShow = this.isNewTaskShow = false;
          this.isIntegrateTaskShow = true;
          let data2 = await getIntegrateTaskByTaskId(data.selectTaskId);
          console.log(' this.taskInfoInit2', data2);
          this.$set(this, 'taskInfoInit', data2);
          console.log(' this.taskInfoInit', this.taskInfoInit);
        }
        if (data.type == 'notebook') {
          this.isIntegrateTaskShow = this.isNewTaskShow = false;
          this.isNoteTaskShow = true;
          this.taskInfoInit = await getIntegrateTaskByTaskId(data.selectTaskId);
        }
      }
    },

    async createNewTask() {
      // let noteType = '';
      this.isNewTaskShow = false;
      let postJson = {
        projectId: this.projectId,
        content: '',
        ...this.taskInfo
      };
      if (this.taskInfo.type == 'integrateTask') {
        postJson.content = '<mxGraphModel><root><mxCell id="0"/><mxCell id="1" parent="0"/></root></mxGraphModel>';
        this.isIntegrateTaskShow = true;
      }
      if (this.taskInfo.type == 'notebook') {
        this.isNoteTaskShow = true;
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
  margin: 20px 0;
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
