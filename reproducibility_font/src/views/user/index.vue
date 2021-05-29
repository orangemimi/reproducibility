<!-- user page -->
<template>
  <div class="user">
    <div class="menu">
      <el-menu default-active="2" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
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
        <el-menu-item index="4">
          <i class="el-icon-setting"></i>
          <template #title>Message</template>
        </el-menu-item>
        <el-menu-item index="4">
          <i class="el-icon-setting"></i>
          <template #title>Feedback</template>
        </el-menu-item>
      </el-menu>
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
  .menu {
    width: 200px;
    height: 100%;
    background-color: rgba($color: #ffffff, $alpha: 1);
  }
}
</style>
