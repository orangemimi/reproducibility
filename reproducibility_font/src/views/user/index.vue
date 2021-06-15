<!-- user page -->
<template>
  <div class="user">
    <div class="menu_left">
      <el-menu default-active="1" class="el-menu-vertical-demo" @select="handleSelect">
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
          <template #title>Models</template>
        </el-menu-item>
        <el-menu-item index="4">
          <i class="el-icon-setting"></i>
          <template #title>Account</template>
        </el-menu-item>
        <el-menu-item index="5">
          <i class="el-icon-setting"></i>
          <template #title>Message</template>
        </el-menu-item>
        <el-menu-item index="6">
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
      userId: state => state.user.userId,
      role: state => state.permission.role,
      token: state => state.user.token
    })
  },

  data() {
    return { recipientNoticeList: [], senderNoticeList: [], userInfo: {} };
  },

  methods: {
    async init() {
      await this.getUserInfo();
    },
    async getUserInfo() {
      // this.userInfo = await getUserByJwtUserId();
      this.recipientNoticeList = await getNoticesByRecipientId();
      this.senderNoticeList = await getNoticesBySenderId();
    },
    handleSelect(key) {
      switch (key) {
        case '1':
          {
            this.$router.push({
              name: 'UserHome'
            });
          }
          break;
        case '2':
          {
            this.$router.push({
              name: 'UserResource'
            });
          }
          break;
        case '3':
          {
            this.$router.push({
              name: 'UserModel'
            });
          }
          break;
        case '4':
          {
            //TOD
            this.$router.push({
              name: 'UserAccount'
            });
          }
          break;
      }
    }
  },

  mounted() {
    this.init();
  }
};
</script>
<style lang="scss" scoped>
.user {
  // padding: 0 20px; // for col
  height: 100%;
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
