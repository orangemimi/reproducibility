<template>
  <div class="app-head">
    <el-row :gutter="20">
      <el-col :span="3" class="logo">
        <div @click="handleSelect('1')">Reproducibility</div>
      </el-col>
      <el-col class="nav-bar" :span="5" :offset="5">
        <el-menu default-active="activeIndex" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1">Home</el-menu-item>
          <el-menu-item index="2">Projects</el-menu-item>
          <el-menu-item index="3">
            Resources
          </el-menu-item>
          <el-menu-item index="4">Help</el-menu-item>
        </el-menu>
      </el-col>
      <!-- {{ user.token }} -->
      <el-col v-if="user.token == null" class="user" :span="3" :offset="7">
        <el-button @click="register">Regisiter</el-button>
        <el-button @click="login">Login</el-button>
      </el-col>
      <el-col v-else class="user" :span="1" :offset="9">
        <el-dropdown placement="bottom-start" v-if="user.userInfo" @command="handleCommond">
          <avatar :username="user.userInfo.userName" :size="40" style="margin-top: 10px"></avatar>

          <!-- <avatar :username="user.avatar == undefined || null ? user.name : user.avatar" :size="40" style="margin-top: 10px" :title="user.name"></avatar> -->
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout">logout</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import Avatar from 'vue-avatar';
import { mapState, mapActions } from 'vuex';
export default {
  data() {
    return {
      activeIndex: 1
    };
  },
  computed: {
    ...mapState(['user'])
  },
  methods: {
    ...mapActions(['handleLogOut']),
    handleSelect(key) {
      switch (key) {
        case '1':
          {
            this.$router.push({
              name: 'Home'
            });
          }
          break;
        case '2':
          {
            this.$router.push({
              name: 'g2s_list'
            });
          }
          break;
        case '3':
          {
            this.$router.push({
              name: 'resource'
            });
          }
          break;
        case '4':
          {
            //TOD
          }
          break;
      }
    },
    async handleCommond(command) {
      if (command == 'logout') {
        await this.handleLogOut();
        location.reload();
      }
    },
    register() {
      this.$router.push({
        name: 'Register'
      });
    },
    login() {
      this.$router.push({
        name: 'Login'
      });
    }
  },
  components: {
    Avatar
  }
};
</script>

<style scoped lang="scss">
.app-head {
  background-color: $headerBackground;
  text-align: center;
  padding: 0px 40px;
  color: $white;
  height: 60px;
  width: 100%;

  .logo div {
    font: bold 28px arial;
    cursor: pointer;
    text-decoration: none;
    vertical-align: middle;
    line-height: 60px;
  }
  .user {
    vertical-align: middle;
    line-height: 60px;
  }

  .nav-bar {
    font-size: 20px;
    font-weight: 600;
    min-width: 400px;
    .el-menu--horizontal > .el-menu-item {
      color: $white;
    }
    .el-menu {
      background-color: $headerBackground;
    }
    .el-menu--horizontal .el-menu-item:not(.is-disabled):focus,
    .el-menu--horizontal .el-menu-item:not(.is-disabled):hover {
      background-color: $headerBackground;
      color: $whiteHover;
    }
    .el-menu--horizontal {
      border: none;
    }
  }
}
</style>
