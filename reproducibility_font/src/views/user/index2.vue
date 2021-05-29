<!-- user page -->
<template>
  <div class="user">
    <div>User Page</div>
    <div>
      get notice from other
    </div>
    <div v-for="(item, index) in recipientNoticeList" :key="index" class="recipient">
      <!-- {{ recipientNoticeList }} -->
      <!-- <el-col :span="22" :offset="1"> -->
      <div v-if="item.state == 'unread'">
        <recipient-card :noticeItem="item"></recipient-card>
      </div>
      <div v-if="item.state == 'read'">
        <recipient-card :noticeItem="item"></recipient-card>
      </div>
      <div v-if="item.state == 'approve'">
        <recipient-card :noticeItem="item"></recipient-card>
      </div>
      <div v-if="item.state == 'disapprove'">
        <recipient-card :noticeItem="item"></recipient-card>
      </div>
      <!-- </el-col> -->
    </div>
    notice from myself
    <div>{{ senderNoticeList }}</div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { getNoticesByRecipientId, getNoticesBySenderId } from '@/api/request';
import recipientCard from '_com/Cards/RecipientCard';
export default {
  components: {
    recipientCard
  },
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
  padding: 0 20px; // for col
  .recipient {
    width: 100%;
  }
}
</style>
