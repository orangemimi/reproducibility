<template>
  <div>
    <el-row>
      <el-col :span="3" :offset="4">
        <div class="left">
          <div class="block">
            <el-avatar :size="size">user</el-avatar>
          </div>
          <p>{{ localuser.name }}</p>
          <div v-if="userId == $route.params.userId">
            <el-button round>Edit Profile</el-button>
          </div>
          <div>
            <el-row class="social">
              <el-col :span="8">
                <i class="el-icon-user">0 followers</i>
              </el-col>
              <el-col :span="8">
                <i class="el-icon-user-solid">0 following</i>
              </el-col>
              <el-col :span="8">
                <i class="el-icon-star-off">0</i>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-col>
      <el-col :span="13">
        <div class="right">
          <el-tabs v-model="activeName" @tab-click="handleClick">
            
            <el-tab-pane label="Overview" name="first"><over-view :publicProjects="publicProjects" /></el-tab-pane>
            <el-tab-pane name="second">
              <template #label>
                <span>
                  Projects
                  <el-avatar :size="20" v-if="$route.params.userId == $store.state.user.userId">{{ joinedProjects.length }}</el-avatar>
                  <el-avatar :size="20" v-else>{{ publicProjects.length }}</el-avatar>
                </span>
              </template>
              <projects :joinedProjects="joinedProjects" :publicProjects="publicProjects" />
            </el-tab-pane>
            <el-tab-pane name="third">
              <template #label>
                <span>
                  Models
                  <el-avatar :size="20">0</el-avatar>
                </span>
              </template>
              角色管理
            </el-tab-pane>
            <el-tab-pane name="fourth">
              <template #label>
                <span>
                  Dataservices
                  <el-avatar :size="20">0</el-avatar>
                </span>
              </template>
              定时任务补偿
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getAllUserInfo, getPrivacy } from '@/api/request';
import overView from './components/overview.vue';
import projects from './components/projects.vue';
export default {
  data() {
    return {
      userId: this.$route.params.userId,
      joinedProjects: [],
      publicProjects: [],
      localuser: {},
      remoteuser: {},
      size: document.body.clientWidth / 8,
      activeName: 'first',
    };
  },
  components: {
    overView,
    projects,
  },
  methods: {
    async init() {
      let data = await getAllUserInfo(this.userId);
      this.localuser = data.localhost;
      this.remoteuser = data.remote;
      if (data.localhost.joinedProjects != null) {
        for (let i = 0; i < data.localhost.joinedProjects.length; i++) {
          this.joinedProjects.push(data.localhost.joinedProjects[i]);
        }
      }
      if (data.localhost.createdProjects != null) {
        for (let i = 0; i < data.localhost.createdProjects.length; i++) {
          this.joinedProjects.push(data.localhost.createdProjects[i]);
        }
      }
      for (let i = 0; i < this.joinedProjects.length; i++) {
        if ((await getPrivacy(this.joinedProjects[i])) == 'public') {
          this.publicProjects.push(this.joinedProjects[i]);
        }
      }
    },

    handleClick() {},

  },
  mounted() {
    this.init();
    // console.log(this.$route.params)
  },
};
</script>

<style scoped lang="scss">
.block {
  margin-top: 55px;
}
.el-button {
  margin-top: 15px;
  width: 100%;
}
.social {
  margin-top: 20px;
  font-size: 12px;
}
p {
  margin-top: 10px;
  font-size: 25px;
}

.right {
  margin-top: 55px;
  margin-left: 40px;
}
</style>