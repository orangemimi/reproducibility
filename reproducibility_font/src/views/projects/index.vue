<!--  -->
<template>
  <div class="project">
    <div>projects</div>
    <el-row :gutter="20">
      <el-col :span="4" v-for="(project, index) in projectList" :key="index">
        <div class="project-card">
          <project-card :project="project"></project-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getAllProjects } from '@/api/request';
// import { oneOf } from '@/utils/utils';
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
    return { projectList: [], pageFilter: { page: 0, pageSize: 8 }, userProjectsInfo: {} };
  },

  methods: {
    async init() {
      // await this.getUserProjets();
      await this.getAllProjects();
    },

    async getAllProjects() {
<<<<<<< HEAD
      this.projectList = await getAllProjects(this.pageFilter.page, this.pageFilter.pageSize);
      // this.projectList.forEach(async project => {
      //   this.judgeRole(project);
      // });
=======
      let data = await getAllProjects(this.pageFilter.page, this.pageFilter.pageSize);
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
>>>>>>> parent of f11cd19 (mxgraph)
    }

    // async judgeRole(project) {
    //   await this.$store.dispatch('permission/getRole', {
    //     project: project,
    //     userId: this.userId
    //   });
    // }
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
