<!--  -->
<template>
  <div class="project">
    <el-row>
      <el-col :span="4">
        <div class="left">
          <div class="new-project">
            projects
            <el-button type="success" icon="el-icon-document-add" size="mini" class="btn" @click="dialogNewProject = true"></el-button>
          </div>
          <el-input placeholder="Please enter the content" size="small" prefix-icon="el-icon-search"></el-input>
          <div v-if="myProjects.length > 0">
            <div v-for="(item, index) in myProjects" :key="index" style="display: flex; margin-bottom: 5px">
              <el-avatar :src="item.picture" :size="30" style="margin-right: 5px" v-if="item.picture != '' && item.picture != undefined"></el-avatar>
              <el-avatar :src="imgBase64(item.projectName)" :size="30" style="margin-right: 5px" v-else></el-avatar>
              <router-link tag="div" :to="{ path: `/project/${item.projectId}/info` }">
                <div style="height: 30px; line-height: 30px" class="router">
                  {{ item.userName }}
                  <span>/</span>
                  {{ item.projectName }}
                </div>
              </router-link>
            </div>
            <el-divider></el-divider>
          </div>
        </div>
      </el-col>
      <el-col :span="20">
        <el-row :gutter="20">
          <el-col :span="5" v-for="(project, index) in projectList" :key="index">
            <div class="project-card">
              <project-card :project="project"></project-card>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <div style="text-align: center; margin-top: 20px">
      <el-pagination background layout="prev, pager, next" :total="total" @current-change="change" :page-size="10"></el-pagination>
    </div>

    <el-dialog title="New Project" :visible.sync="dialogNewProject" width="30%">
      <create @dialogShow="dialogShow" />
    </el-dialog>
  </div>
</template>

<script>
import { getUserProjects, getProjectsPage } from '@/api/request';
import create from './components/create.vue';
import { imgBase64 } from '@/lib/utils';
import projectCard from './components/ProjectCard';
import { mapState } from 'vuex';
export default {
  components: { projectCard, create },

  watch: {},

  computed: {
    ...mapState({
      userId: (state) => state.user.userId,
      role: (state) => state.permission.role,
      token: (state) => state.user.token,
    }),
  },

  data() {
    return {
      projectList: [],
      userProjectsInfo: {},
      myProjects: [],
      total: 0,
      dialogNewProject: false,
    };
  },

  methods: {
    async init() {
      this.myProjects = await getUserProjects();
      console.log(this.myProjects);
      let data = await getProjectsPage(0, 10);
      this.projectList = data.content;
      this.total = data.totalElements;
    },

    imgBase64(projectName) {
      return imgBase64(projectName);
    },

    async change(val) {
      let data = await getProjectsPage(val - 1, 10);
      this.projectList = data.content;
    },

    dialogShow(val) {
      this.dialogNewProject = val;
    },
  },

  mounted() {
    this.init();
  },
};
</script>
<style lang="scss" scoped>
.left {
  margin-top: 10px;
  .new-project {
    display: flex;
    margin-bottom: 5px;

    .btn {
      margin-left: auto;
      margin-right: 20px;
    }
  }
  .el-input {
    width: calc(100% - 20px);
    margin-bottom: 25px;
  }
  .router {
    width: 220px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
  }

  .router:hover {
    cursor: pointer;
    color: #1f77b4;
    text-decoration: underline;
  }
}
.el-col-5 {
  width: 20%;
}
.project {
  padding: 0 20px; // for col
  .project-card:hover {
    cursor: pointer;
  }
}
</style>
