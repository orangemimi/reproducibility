<template>
  <div class="app-head" ref="head">
    <el-row :gutter="20">
      <el-col :span="3" class="logo">
        <div @click="handleSelect('1')">Reproducibility</div>
      </el-col>
      <el-col class="nav-bar" :span="6" :offset="4">
        <el-menu default-active="activeIndex" mode="horizontal" @select="handleSelect" text-color="#fff" background-color="#24292E" active-text-color="#ffd04b">
          <el-menu-item index="1">Home</el-menu-item>
          <el-menu-item index="2">Projects</el-menu-item>
          <el-menu-item index="3">Model</el-menu-item>
          <el-submenu index="4">
            <template slot="title">Dataservice</template>
            <el-menu-item index="4-1">Official Repository</el-menu-item>
            <el-menu-item index="4-2">Community Repository</el-menu-item>
          </el-submenu>
          <el-menu-item index="5">Help</el-menu-item>
        </el-menu>
      </el-col>
      <el-col v-if="user.name == null || user.name == undefined" class="user" :span="3" :offset="7" style="margin-top: 10px">
        <el-button @click="register" type="text">Regisiter</el-button>
        <el-button @click="login" type="text">Login</el-button>
      </el-col>

      <el-col v-else class="user" :span="2" :offset="8">
        <el-dropdown placement="bottom-start" @command="handleCommond">
          <div class="personal">
            <avatar
              :username="user.avatar != 'undefined' && user.avatar != 'null' && user.avatar != undefined && user.avatar != '' ? user.avatar : user.name"
              :size="40"
              style="margin-top: 10px"
            ></avatar>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="homepage">
              <i class="el-icon-monitor" />
              homepage
            </el-dropdown-item>
            <el-dropdown-item command="space">
              <i class="el-icon-menu" />
              individual space
            </el-dropdown-item>
            <el-dropdown-item command="setting">
              <i class="el-icon-setting" />
              setting
            </el-dropdown-item>
            <el-dropdown-item command="logout">
              <i class="el-icon-location" />
              logout
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-badge
          :is-dot="this.$store.state.user.unreadApplynum == 0 && this.$store.state.user.unreadReplynum == 0 ? false : true"
          class="item"
          style="margin-left: 10px"
        >
          <i class="el-icon-message-solid" @click="toNotice"></i>
        </el-badge>
        <el-badge :value="selectModelCount" :max="10" class="item">
          <i class="iconfont icon-gouwuche" @click="handleClick"></i>
        </el-badge>
      </el-col>
    </el-row>
    <el-drawer :visible.sync="drawer" direction="rtl" size="300px" :withHeader="false" v-if="drawer">
      <drawer-card @close="close" @success="success" />
    </el-drawer>
  </div>
</template>
<script>
import Avatar from 'vue-avatar';
import { mapState, mapActions } from 'vuex';
import drawerCard from './components/DrawerCard.vue';
export default {
  data() {
    return {
      activeIndex: 1,
      selectModelCount: this.$store.state.user.selectNum,
      drawer: false,
    };
  },
  computed: {
    ...mapState(['user']),
  },
  watch: {
    '$store.state.user.selectNum': {
      handler(val) {
        this.selectModelCount = val;
      },
    },
  },


  methods: {
    ...mapActions({ handleLogOut: 'user/handleLogOut' }),

    handleClick() {
      this.drawer = true;
    },
    close() {
      this.drawer = false;
    },
    success() {
      this.$store.state.user.selectNum = 0;
    },

    handleSelect(key) {
      switch (key) {
        case '1':
          {
            if (this.$router.currentRoute.name != 'Home') {
              this.$router.push({
                name: 'Home',
              });
            }
          }
          break;
        case '2':
          {
            if (this.$router.currentRoute.name != 'Projects') {
              this.$router.push({
                name: 'Projects',
              });
            }
          }
          break;
        case '3':
          {
            if (this.$router.currentRoute.name != 'Models') {
              this.$router.push({
                name: 'Models',
              });
            }
          }
          break;
        case '4-1':
          {
            if (this.$router.currentRoute.name != 'OfficialData') {
              this.$router.push({
                name: 'OfficialData',
              });
            }
          }
          break;
        case '4-2':
          {
            if (this.$router.currentRoute.name != 'CommunityData') {
              this.$router.push({
                name: 'CommunityData',
              });
            }
          }
          break;
        case '5': {
          break;
        }
      }
    },
    async handleCommond(command) {
      if (command == 'logout') {
        await this.handleLogOut();
        location.reload();
      } else if (command == 'space') {
        this.toUserPage();
      } else if (command == 'homepage') {
        this.toHomePage();
      }
    },
    register() {
      this.$router.push({
        name: 'Login',
        params: { tab: 'register' },
      });
    },
    login() {
      this.$router.push({
        name: 'Login',
      });
    },
    toNotice() {
      if (this.$router.currentRoute.name != 'UserMessage') {
        this.$router.push({
          name: 'UserMessage',
        });
      }
    },

    toUserPage() {
      if (this.$router.currentRoute.name != 'UserHome') {
        this.$router.push({
          name: 'UserHome',
        });
      }
    },
    toHomePage() {
      if (this.$router.currentRoute.name != 'Homepage') {
        this.$router.push({
          path: `/Homepage/${this.$store.state.user.userId}`,
        });
      }
    },
  },
  mounted() {},
  components: {
    Avatar,
    drawerCard,
  },
};
</script>

<style scoped lang="scss">
.personal:hover {
  cursor: pointer;
}
.el-badge:hover {
  cursor: pointer;
}
.item {
  font-size: 18px;
  margin-right: 10px;
}

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
    // line-height: 60px;
  }

  .nav-bar {
    font-size: 20px;
    font-weight: 600;
    min-width: 400px;
    .el-menu--horizontal > .el-menu-item {
      color: $white;
    }

    .el-menu--horizontal > .el-menu-item.is-active {
      border-bottom: 2px solid $selectOrangeBorderColor;
    }
    .el-menu {
      background-color: $headerBackground;
    }
    .el-menu--horizontal .el-menu-item:not(.is-disabled):focus,
    .el-menu--horizontal .el-menu-item:not(.is-disabled):hover {
      background-color: $headerBackground;
      color: $whiteHover;
      border-bottom: 2px solid $selectOrangeBorderColor;
    }
    .el-menu--horizontal {
      border: none;
    }
  }
}
</style>
