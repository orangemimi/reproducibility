<template>
  <div class="main">
    <div class="check">
      <el-row>
        <el-col :span="20">
          <el-checkbox-group v-model="checkList" @change="handleCheckAllChange">
            <el-checkbox label="Created by me" :checked="true"></el-checkbox>
            <el-checkbox label="Joined by me" :checked="true"></el-checkbox>
          </el-checkbox-group>
        </el-col>
        <el-col :span="4">
          <div class="btn">
            <el-button type="text" @click="createProject">Create</el-button>
          </div>
        </el-col>
      </el-row>
    </div>

    <vue-scroll style="max-height:calc(100% - 50px)">
      <div class="project">
        <el-row :gutter="20">
          <el-col v-for="(project, index) in projectList" :key="index" class="el-col-4-8">
            <div v-if="project.creator == userId">
              <div class="project-card" v-show="create">
                <project-card :project="project"></project-card>
              </div>
            </div>
            <div v-else>
              <div class="project-card" v-show="join">
                <project-card :project="project"></project-card>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </vue-scroll>

    <el-dialog title="New Project" :visible.sync="dialogNewProject" width="30%">
      <create @dialogShow="dialogShow"/>
    </el-dialog>
  </div>
</template>

<script>
import { getMyProjects } from '@/api/request';
import projectCard from './ProjectCard.vue';
import create from '../../projects/components/create.vue'

export default {
  data() {
    return {
      projectList: [],
      checkList: [],
      userId: this.$store.state.user.userId,
      create: true,
      join: true,
      dialogNewProject: false
    };
  },
  components: { projectCard, create },
  methods: {
    async getprojects() {
      this.projectList = await getMyProjects();
    },
    handleCheckAllChange(val) {
      if (val.length == 2) {
        this.create = true;
        this.join = true;
      } else if (val.length == 0) {
        this.create = false;
        this.join = false;
      } else {
        if (val[0] == 'Created by me') {
          this.create = true;
          this.join = false;
        } else {
          this.create = false;
          this.join = true;
        }
      }
    },
    createProject() {
      this.dialogNewProject = true
    },

    dialogShow(val) {
      this.dialogNewProject = val
    }
  },
  mounted() {
    this.getprojects();
  },
};
</script>

<style lang="scss" scoped>
.main {
  width: 90%;
  margin: 30px auto;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  height: 779px;
  background: white;
  border-radius: 4px;
  .check {
    border-bottom: 1px solid #dadce0;
    height: 50px;
    .el-checkbox {
      margin-top: 15px;
      margin-left: 30px;
    }
    .btn {
      margin-top: 5px;
    }
  }
}
.project {
  padding: 20px 20px; // for col

  .project-card:hover {
    cursor: pointer;
  }
  .el-col-4-8 {
    width: 20%;
  }
}

</style>