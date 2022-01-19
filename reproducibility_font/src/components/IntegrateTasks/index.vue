<!--  -->
<template>
  <div class="select">

    <el-dropdown @command="selectTask">
      <span class="el-dropdown-link">
        Select an Exist Task
        <i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <!-- {{ taskList }} -->
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item v-for="(task, index) in taskList" :key="index" :command="task">
            {{ task.taskName }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script>
import { getAllIntegrateTasksByProjectId } from '@/api/request';
export default {
  data() {
    return {
      projectId: this.$route.params.id,
      taskList: [],
    };
  },

  methods: {
    async init() {
      await this.getIntegrateTasks();
    },
    async getIntegrateTasks() {
      let data = await getAllIntegrateTasksByProjectId(this.projectId);
      if (data != null) {
        this.taskList = data;
        return;
      }
      this.taskList = [];
    },

    selectTask(task) {
      this.$emit('selectTask', task);
    },
  },

  async mounted() {
    await this.init();
  },
};
</script>
<style lang="scss" scoped>
.select:hover {
  cursor: pointer;
}
</style>
