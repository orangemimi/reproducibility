<!-- user page -->
<template>
  <div class="user">
    <div>User Page</div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { getUserProjectInfo } from '@/api/request';
export default {
  watch: {},

  computed: {
    ...mapState({
      userId: state => state.user.userId,
      role: state => state.permission.role
      // token: state => state.user.token
    })
  },

  data() {
    return { projectList: [], userInfo: {} };
  },

  methods: {
    async init() {
      await this.getUserInfo();
    },
    async getUserInfo() {
      this.userInfo = await getUserProjectInfo();
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
  .project-card:hover {
    cursor: pointer;
  }
}
</style>
