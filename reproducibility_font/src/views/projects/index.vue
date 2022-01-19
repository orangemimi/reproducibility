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
            <div v-for="(item, index) in myProjects" :key="index" style="display: flex">
              <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" :size="30"></el-avatar>
              <div style="height: 30px; line-height: 30px">
                {{ item.userName }}
                <span>/</span>
                {{ item.projectName }}
              </div>
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
      <el-form ref="form" :model="form" label-width="90px" size="mini">
        <el-form-item label="Name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="Purpose">
          <el-input v-model="form.purpose"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input type="textarea" v-model="form.description" resize="none"></el-input>
        </el-form-item>
        <el-form-item label="Privacy">
          <el-select v-model="form.privacy" placeholder="请选择">
            <el-option label="public" value="public"></el-option>
            <el-option label="private" value="private"></el-option>
          </el-select>
          <!-- <el-switch v-model="form.privacy" active-text="public" inactive-text="private"></el-switch> -->
        </el-form-item>
        <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">
          {{ tag }}
        </el-tag>
        <el-input
          class="input-new-tag"
          v-if="inputVisible"
          v-model="inputValue"
          ref="saveTagInput"
          size="small"
          @keyup.enter.native="handleInputConfirm"
          @blur="handleInputConfirm"
        ></el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
      </el-form>
      <add-image @getfile="getfile" class="picture" />
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogNewProject = false">Cancel</el-button>
        <el-button type="primary" @click="saveProject">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getUserProjects, getProjectsPage, saveProject } from '@/api/request';
// import { oneOf } from '@/utils/utils';
import projectCard from './components/ProjectCard';
import addImage from '_com/AddImage/index1.vue';
import { mapState } from 'vuex';
export default {
  components: { projectCard, addImage },

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
      inputVisible: false,
      inputValue: '',
      dynamicTags: [],
      imageUrl: '',
      form: {
        name: '',
        description: '',
        purpose: '',
        privacy: 'public',
      },
    };
  },

  methods: {
    async init() {
      this.myProjects = await getUserProjects();
      let data = await getProjectsPage(0, 10);
      this.projectList = data.content;
      this.total = data.totalElements;
    },

    async change(val) {
      let data = await getProjectsPage(val - 1, 10);
      this.projectList = data.content;
    },

    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (this.dynamicTags.indexOf(inputValue) != -1) {
        this.$notify({
          title: 'warning',
          message: 'The same label exists!',
          type: 'warning',
        });
      } else {
        if (inputValue) {
          this.dynamicTags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      }
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    getfile(val) {
      console.log(val);
    },
    async saveProject() {
      let jsonData = {
        project: {
          name: this.form.name,
          description: this.form.description,
          purpose: this.form.purpose,
          privacy: this.form.privacy,
          tags: this.dynamicTags,
        },
        performance: {
          completion: {
            context: { content: 'Context Definition', degree: '0%', type: 'warning', icon: 'el-icon-edit' },
            resource: { content: 'Resource Collection', degree: '0%', type: 'warning', icon: 'el-icon-folder' },
            scenario: { content: 'Simulation Scenario', degree: '0%', type: 'warning', icon: 'el-icon-sunny' },
            results: { content: 'Excepted Results', degree: '0%', type: 'warning', icon: 'el-icon-document' },
          },
        },
      };
      let data = await saveProject(jsonData)
      console.log(data)
      this.dialogNewProject = false
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
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
.picture {
  margin-top: 10px;
  margin-left: 10px;
}
</style>
