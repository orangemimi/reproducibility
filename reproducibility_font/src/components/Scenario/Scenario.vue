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
      <mx-graph :taskInfoInit="taskInfoInit"></mx-graph>
    </div>
  </div>
</template>

<script>
import { getScenarioByProjectId, updateScenarioByProjectId, saveIntegrateTask, getIntegrateTaskByTaskId } from '@/api/request';
import mxGraph from './components/MxGraph';
import { mapState } from 'vuex';

import integrateTasks from '_com/IntegrateTasks';
// import { initSetTimeOut } from '@/utils/utils';

export default {
  components: {
    mxGraph,
    integrateTasks
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
      isNewTaskContainerShow: true,
      taskInfo: {},
      taskInfoInit: {},
      taskList: [],
      mxgraph: '' //task selected mxgraph
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
        this.isNewTaskContainerShow = true;
        return;
      }

      this.taskInfoInit = await getIntegrateTaskByTaskId(data.selectTaskId);
      this.isNewTaskContainerShow = false;
    },

    async createNewTask() {
      let postJson = {
        projectId: this.projectId,
        mxgraph: '<mxGraphModel><root><mxCell id="0"/><mxCell id="1" parent="0"/></root></mxGraphModel>',
        ...this.taskInfo
      };
      let data = await saveIntegrateTask(postJson);
      console.log('scenario', data);
      this.taskInfoInit = data;
      await updateScenarioByProjectId(this.projectId, { selectTaskId: data.id });

      this.isNewTaskContainerShow = false;
      // await initSetTimeOut();
    },

    selectTask(val) {
      //选择task之后的回调
      this.taskInfoInit = val;
      this.isNewTaskContainerShow = false;
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
