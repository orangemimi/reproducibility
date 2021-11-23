<!-- user page -->
<template>
  <div class="user">
    <div class="menu_left">
      <el-menu :default-active="index" @select="handleSelect">
        <el-menu-item index="1">
          <i class="el-icon-location"></i>
          <template #title>Home</template>
        </el-menu-item>
        <el-menu-item index="2">
          <i class="el-icon-menu"></i>
          <template #title>Resources</template>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-document"></i>
          <template #title>Projects</template>
        </el-menu-item>
        <el-menu-item index="4">
          <i class="el-icon-document"></i>
          <template #title>Models</template>
        </el-menu-item>
        <el-menu-item index="5">
          <i class="el-icon-document"></i>
          <template #title>DataServices</template>
        </el-menu-item>
        <el-menu-item index="6">
          <i class="el-icon-setting"></i>
          <template #title>Account</template>
        </el-menu-item>
        <el-menu-item index="7">
          <i class="el-icon-setting"></i>
          <template #title>Message</template>
        </el-menu-item>
        <el-menu-item index="8">
          <i class="el-icon-setting"></i>
          <template #title>Feedback</template>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="content">
      <router-view class="scroll-item"></router-view>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { getNoticesByRecipientId, getNoticesBySenderId } from '@/api/request';
export default {
  components: {},
  watch: {},

  computed: {
    ...mapState({
      userId: (state) => state.user.userId,
      role: (state) => state.permission.role,
      token: (state) => state.user.token,
    }),
  },

  //state用来跟踪状态
  data() {
    return { recipientNoticeList: [], senderNoticeList: [], userInfo: {}, index: '1' };
  },

  methods: {
    async init() {
      await this.getUserInfo();
      switch (this.$router.currentRoute.name) {
        case 'UserHome': {
          this.index = '1';
          break;
        }
        case 'UserResource': {
          this.index = '2';
          break;
        }
        case 'UserProject': {
          this.index = '3';
          break;
        }
        case 'UserModel': {
          this.index = '4';
          break;
        }
        case 'UserDataService': {
          this.index = '5';
          break;
        }
        case 'UserAccount': {
          this.index = '6';
          break;
        }
        case 'UserMessage': {
          this.index = '7';
          break;
        }
      }
    },
    async getUserInfo() {
      // this.userInfo = await getUserByJwtUserId();
      this.recipientNoticeList = await getNoticesByRecipientId();
      this.senderNoticeList = await getNoticesBySenderId();
    },
    handleSelect(key) {
      //对重复选中的push会报错，加上if判断是否重复点击
      switch (key) {
        case '1':
          {
            if (this.$router.currentRoute.name != 'UserHome') {
              this.$router.push({
                name: 'UserHome',
              });
            }
          }
          break;
        case '2':
          {
            if (this.$router.currentRoute.name != 'UserResource') {
              this.$router.push({
                name: 'UserResource',
              });
            }
          }
          break;
        case '3':
          {
            if (this.$router.currentRoute.name != 'UserProject') {
              this.$router.push({
                name: 'UserProject',
              });
            }
          }
          break;
        case '4':
          {
            //TOD
            if (this.$router.currentRoute.name != 'UserModel') {
              this.$router.push({
                name: 'UserModel',
              });
            }
          }
          break;
        case '5': {
          if (this.$router.currentRoute.name != 'UserDataService') {
            this.$router.push({
              name: 'UserDataService',
            });
          }
          break;
        }
        case '6': {
          if (this.$router.currentRoute.name != 'UserAccount') {
            this.$router.push({
              name: 'UserAccount',
            });
          }
          break;
        }
        case '7': {
          if (this.$router.currentRoute.name != 'UserMessage') {
            this.$router.push({
              name: 'UserMessage',
            });
          }
        }
      }
    },
  },

  mounted() {
    this.init();
  },
};
</script>
<style lang="scss" scoped>
.user {
  // padding: 0 20px; // for col
  height: 100%;
  display: flex;

  .menu_left {
    width: 200px;
    height: 100%;
    float: left;
    background-color: rgba($color: #ffffff, $alpha: 1);
  }
  .content {
    width: 100%;
    height: 100%;
  }
}
</style>
