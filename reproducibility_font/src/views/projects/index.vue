<!--  -->
<template>
  <div class="project">
    <div>projects</div>
    <el-row :gutter="20">
      <el-col :span="4" v-for="(project, index) in projectList" :key="index">
        <div @click="judgeRole(project)" class="project-card">
          <project-card :project="project"></project-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { get } from '@/axios';
import projectCard from './components/ProjectCard';
import { mapState } from 'vuex';
export default {
  components: { projectCard },

  watch: {},

  computed: {
    ...mapState({
      userId: state => state.user.userId,
      role: state => state.permission.role,
      token: state => state.user.token
    })
  },

  data() {
    return { projectList: [] };
  },

  methods: {
    async init() {
      await this.getAllProjects();
    },

    async getAllProjects() {
      let data = await get(`/projects`);
      this.projectList = data;
    },

    async judgeRole(project) {
      console.log(this.userId);
      await this.$store.dispatch('permission/getRole', {
        project: project,
        userId: this.userId
      });
      console.log(this.role);
      this.$router.push({ path: `/project/${project.id}/info` });
    }
  },

  mounted() {
    this.init();
  }
};
</script>
<style lang="scss" scoped>
.project {
  padding: 0 20px; // for col
  .project-card:hover {
    cursor: pointer;
  }
}
</style>
