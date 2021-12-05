<!-- project info -->
<template>
  <div>
    <div class="page-header">
      <div class="nav">
        <el-col class="breadcrumb" :span="12">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item class="name">
              {{ creator.name }}
            </el-breadcrumb-item>
            <el-breadcrumb-item class="project">{{ projectInfo.name }}</el-breadcrumb-item>
          </el-breadcrumb>
        </el-col>
        <el-col class="info-btn" :span="12">
          <folk-btn :count="3" class="folk" :isDisable="this.role == 'builder' ? true : false" @click="forkProject"></folk-btn>
          <star-btn class="star"></star-btn>
          <watch-btn :count="1" class="watch"></watch-btn>
        </el-col>
      </div>

      <div class="menu">
        <!-- {{ role }} -->
        <builder-menu v-if="role == 'builder'" @toRouterType="toRouterType"></builder-menu>
        <re-builder-menu v-else @toRouterType="toRouterType"></re-builder-menu>
      </div>
    </div>
    <div class="page-content">
      <router-view class="scroll-item"></router-view>
    </div>
  </div>
</template>

<script>
import { getProjectAndUsers, forkProject } from '@/api/request';
import watchBtn from '_com/PageHeaderBtn/WatchBtn';
import starBtn from '_com/PageHeaderBtn/StarBtn';
import folkBtn from '_com/PageHeaderBtn/FolkBtn';
import { mapState } from 'vuex';
import builderMenu from './components/BuilderMenu';
import reBuilderMenu from './components/ReBuilderMenu';

export default {
  components: { watchBtn, starBtn, folkBtn, builderMenu, reBuilderMenu },

  computed: {
    ...mapState({
      role: (state) => state.permission.role,
      name: (state) => state.user.name,
      userId: (state) => state.user.userId,
    }),
  },

  watch: {},

  data() {
    return {
      projectId: this.$route.params.id,
      projectInfo: {},
      creator: {},
      members: [],
    };
  },

  methods: {
    // getIsDisable() {
    //   return this.role == 'builder' ? true : false;
    // },
    async init() {
      await this.getProjectInfo();
      await this.judgeRole(this.projectInfo);
    },

    async getProjectInfo() {
      // let data = await getProjectById(this.projectId);

      let data = await getProjectAndUsers(this.projectId);

      this.projectInfo = data.project;
      this.creator = data.creator;
      this.members = data.members;
    },

    async judgeRole(project) {
      await this.$store.dispatch('permission/getRole', {
        project: project,
        userId: this.userId,
      });
    },

    toRouterType(val) {
      console.log(val);
      if (val != this.$router.currentRoute.name) {
        this.$router.push({
          name: val,
        });
      }
    },

    async forkProject() {
      if (this.role == 'builder') {
        return;
      }
      await this.createProject();
    },

    async createProject() {
      let form = {
        name: this.projectInfo.name,
        description: this.projectInfo.description,
        purpose: this.projectInfo.purpose,
        privacy: this.projectInfo.privacy,
        picture: this.projectInfo.picture,
        forkingProjectId: this.projectInfo.id,
      };
      let data = await forkProject(form);

      this.userInfo.folkedProjects.push(data.id);
      // await updateUserByJwtUserId({ createdProjects: this.userInfo.createdProjects });
    },
  },

  mounted() {
    this.init();
  },
};
</script>
<style lang="scss" scoped>
.page-header {
  height: 120px;
  width: 100%;
  background-color: $contain2Background;
  box-shadow: $underLineNavBoxShadow;
  padding: 16px 32px 0 32px;

  .nav {
    .breadcrumb {
      padding: 5px 0;
      /deep/ .el-breadcrumb__inner {
        font-size: 18px;
        font-weight: 550;
        line-height: 22px;
        color: $blueEmplasisFont;
      }
      .name {
        font-weight: 400;
      }
      .project {
        width: 600px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
    .info-btn {
      line-height: 32px;
      float: right;
      .watch {
        float: right;
        margin: 0 10px;
      }
      .star {
        float: right;
        margin: 0 10px;
      }
      .folk {
        float: right;
        margin: 0 10px;
      }
    }
  }
  .menu {
    clear: both;
    // height: 32px;
    line-height: 72px;
  }
}
</style>
