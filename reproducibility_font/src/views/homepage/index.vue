<template>
  <div>
    <el-row>
      <el-col :span="3" :offset="4">
        <div class="left">
          <div class="block">
            <el-avatar :size="size">user</el-avatar>
          </div>
          <p>{{ localuser.name }}</p>
          <div>
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
            <!-- <el-tab-pane>
              <template #label>
                <span>
                  <i class="el-icon-date"></i>
                  我的行程
                </span>
              </template>
              我的行程
            </el-tab-pane> -->
            <el-tab-pane label="Overview" name="first"><over-view :joinedProjects="localuser.joinedProjects"/></el-tab-pane>
            <el-tab-pane name="second">
              <template #label>
                <span>
                  Projects
                  <el-avatar :size="20">{{ localuser.joinedProjects.length }}</el-avatar>
                </span>
              </template>
              {{ localuser.joinedProjects.length }}
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
import { getAllUserInfo } from '@/api/request';
import overView from './components/overview.vue';
export default {
  data() {
    return {
      userId: this.$route.params.userId,
      localuser: {
        joinedProjects: [],
      },
      remoteuser: {},
      size: document.body.clientWidth / 8,
      activeName: 'first',
    };
  },
  components: {
    overView,
  },
  methods: {
    async init() {
      let data = await getAllUserInfo(this.userId);
      this.localuser = data.localhost;
      this.remoteuser = data.remote;
      console.log(data);
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