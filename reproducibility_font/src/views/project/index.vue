<!-- project info -->
<template>
  <div>
    <div class="page-header">
      <div class="nav">
        <el-col class="breadcrumb" :span="12">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item class="name">
              {{ name }}
            </el-breadcrumb-item>
            <el-breadcrumb-item>{{ projectInfo.name }}</el-breadcrumb-item>
          </el-breadcrumb>
        </el-col>
        <el-col class="info-btn" :span="12">
          <folk-btn :count="3" class="folk"></folk-btn>
          <star-btn :count="2" class="star"></star-btn>
          <watch-btn :count="1" class="watch"></watch-btn>
        </el-col>
      </div>

      <div class="menu">
        <!-- {{ role }} -->
        <builder-menu v-if="role == 'builder'" @toRouterType="toRouterType"></builder-menu>
        <re-builder-menu v-else></re-builder-menu>
      </div>
    </div>
    <div class="page-content"><router-view class="scroll-item"></router-view></div>
  </div>
</template>

<script>
import { get } from '@/axios';
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
      role: state => state.permission.role,
      name: state => state.user.name,
      userId: state => state.user.userId
    })
  },

  watch: {},

  data() {
    return {
      projectId: this.$route.params.id,
      projectInfo: {}
    };
  },

  methods: {
    async init() {
      await this.getProjectInfo();
      // await this.judgeRole(this.projectInfo);
    },

    async getProjectInfo() {
      let data = await get(`/projects/one/${this.projectId}`);
      console.log(data);
      this.projectInfo = data;
    },

    async judgeRole(project) {
      await this.$store.dispatch('permission/getRole', {
        project: project,
        userId: this.userId
      });
    },

    toRouterType(val) {
      this.$router.push({
        name: val
      });
    }
  },

  mounted() {
    this.init();
  }
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
    height: 32px;
    line-height: 32px;
    .breadcrumb {
      padding: 5px 0;
      /deep/ .el-breadcrumb__inner {
        font-size: 22px;
        font-weight: 550;
        color: $blueEmplasisFont;
      }
      .name {
        /deep/ .el-breadcrumb__inner {
          font-weight: 400;
        }
      }
    }
    .info-btn {
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
    height: 72px;
  }
}
</style>
