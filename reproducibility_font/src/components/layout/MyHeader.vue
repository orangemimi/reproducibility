<template>
  <div class="app-head">
    <el-row :gutter="20">
      <el-col :span="4" class="logo">
        <div @click="handleSelect('1')">OpenGMS 2R</div>
      </el-col>
      <el-col class="nav-bar" :span="13" :offset="4">
        <el-menu
          default-active="activeIndex"
          mode="horizontal"
          @select="handleSelect"
        >
          <el-menu-item index="1">Home</el-menu-item>
          <el-menu-item index="2">Projects</el-menu-item>
          <el-submenu index="3">
            <template slot="title">Resources</template>
            <el-menu-item index="3-1">Model</el-menu-item>
            <el-menu-item index="3-2">Data Process</el-menu-item>
          </el-submenu>
          <el-menu-item index="4">Help</el-menu-item>
        </el-menu>
      </el-col>

      <el-col class="user" :span="2" :offset="1">
        <el-dropdown
          placement="bottom-start"
          v-if="user.name"
          @command="handleCommond"
        >
          <avatar
            :username="user.name"
            :size="40"
            style="margin-top:10px"
            :title="user.name"
          ></avatar>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout">logout</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import { mapState, mapActions } from "vuex";
export default {
  data() {
    return {
      activeIndex: 1
    };
  },
  computed: {
    ...mapState(["user"])
  },
  methods: {
    ...mapActions(["handleLogOut"]),
    handleSelect(key) {
      switch (key) {
        case "1":
          {
            this.$router.push({ name: "home" });
          }
          break;
        case "2":
          {
            this.$router.push({ name: "g2s_list" });
          }
          break;
        case "3-1":
          {
            this.$router.push({ name: "model" });
          }
          break;
        case "3-2":
          {
            this.$router.push({ name: "process" });
          }
          break;
        case "4":
          {
            //TOD
          }
          break;
      }
    },
    async handleCommond(command) {
      if (command == "logout") {
        await this.handleLogOut();
        location.reload();
      }
    }
  },
  components: {
    Avatar
  }
};
</script>
<style scoped>
.el-menu--horizontal {
  border: none;
}
.logo div {
  font: bold 28px arial;
  text-decoration: none;
  line-height: 60px;
  color: #409eff;
  line-height: 80px;
  cursor: pointer;
}
.app-head {
  background-color: white;
  box-shadow: 0px 1px 12px #d1d1d1;
  text-align: center;
  padding: 0px 40px;
  margin-bottom: 20px !important;
}
.app-head .nav-bar {
  margin: 0px 20px;
  font-size: 16px;
  color: white;
}
</style>
