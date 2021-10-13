<template>
  <div>
    <p>Notice</p>
    <div class="container">
      <div class="header">
        <div v-bind:class="{ active: tabselected == 1 }" @click="tabselect(1)" class="sel" style="margin-left: 20px">
          <el-badge :is-dot="this.$store.state.user.unreadApplynum != 0 ? true : false" class="item">Apply</el-badge>
        </div>
        <div v-bind:class="{ active: tabselected == 2 }" @click="tabselect(2)" class="sel"><el-badge :is-dot="this.$store.state.user.unreadReplynum != 0 ? true : false" class="item">Reply</el-badge></div>
        <div v-bind:class="{ active: tabselected == 3 }" @click="tabselect(3)" class="sel" style="margin-right: 20px">
          <el-badge :is-dot="false" class="item">Notice</el-badge>
        </div>
      </div>
      <div class="main">
        <div v-show="tabselected == 1 ? true : false"><join-projects /></div>
        <div v-show="tabselected == 2 ? true : false"><reply-notice /></div>
        <div v-show="tabselected == 3 ? true : false"></div>
      </div>
    </div>
  </div>
</template>

<script>
import JoinProjects from '../../../components/Notice/JoinProjects.vue';
import ReplyNotice from '../../../components/Notice/ReplyNotice.vue';
import { changeAllReplyisread } from '@/api/request';
export default {
  data() {
    return {
      tabselected: 1,
    };
  },
  components: { JoinProjects, ReplyNotice },
  methods: {
    async tabselect(num) {
      this.tabselected = num;
      if(num == 2 && this.$store.state.user.unreadReplynum != 0) {
        await changeAllReplyisread()
        localStorage.setItem("unreadReplynum", 0)
        this.$store.state.user.unreadReplynum = 0
      }
    }
  }
};
</script>

<style scoped lang="scss">
.item {
  margin-top: 10px;
  margin-right: 40px;
}
p {
  font-size: 24px;
  text-align: center;
  margin-top: 50px;
}
.container {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  height: 600px;
  width: 70%;
  margin: 20px auto;
  background: #ffffff;
  border-radius: 4px;
  .header {
    height: 50px;
    display: flex;
    flex-direction: row;
    .sel:hover {
      cursor: pointer
    }
    .sel {
      flex: 1;
      text-align: center;
      align-self: center;
      padding-bottom: 10px;
      border-bottom: 1px solid #dadce0;
    }
    .active {
      border-bottom: 1px solid blue;
    }
  }
  .main {
    border: 1px solid #dadce0;
    margin: 5px 20px 5px 20px;
    border-radius: 4px;
  }
}
</style>