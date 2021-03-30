<!--  -->
<template>
  <div>
    <!-- <el-row>
      <div v-for="(task, index) in taskList" :key="index">
        <el-col :span="22" :offset="1">
          <el-card @click.native="selectTask(task)">
            {{ task.taskName }}
          </el-card>
          <el-divider />
        </el-col>
      </div>
    </el-row> -->
    <el-dropdown @command="selectTask">
      <span class="el-dropdown-link">
        Select an Exist Task
        <i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-for="(task, index) in taskList" :key="index" :command="task">
          {{ task.taskName }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import { get } from '@/axios';
export default {
  components: {},
  // props: {
  //   currentTask: {
  //     type: Object,
  //   },
  // },
  // watch: {
  //   currentTask: {
  //     handler(val) {
  //       if (val != "") {
  //         this.newTask = val;
  //         this.taskList.push(val);
  //       }
  //     },
  //     deep: true,
  //   },
  // },

  computed: {},

  data() {
    return {
      projectId: this.$route.params.id,
      taskList: []
      // newTask: this.addNewTask,
    };
  },

  methods: {
    async init() {
      await this.getIntegrateTasks();
    },
    async getIntegrateTasks() {
      let data = await get(`/integrateTasks/all/${this.projectId}`); //获得该项目的所有tasks
      if (data != null) {
        this.taskList = data;
      }
    },
    selectTask(task) {
      // console.log("task", task);
      this.$emit('selectTask', task);
    }
  },

  async mounted() {
    await this.init();
  }
};
</script>
<style lang="scss" scoped></style>
