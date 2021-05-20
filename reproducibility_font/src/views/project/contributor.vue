<!-- info -->
<template>
  <div class="information">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="info-card">
        <div class="content">
          <div class="des">
            <strong>Description:</strong>
            <div class="content">
              {{ projectInfo.description }}
            </div>
          </div>
        </div>
      </div>
    </el-col>
  </div>
</template>

<script>
import { getProjectAndUsers } from '@/api/request';
import { dateFormat } from '@/utils/utils';
import { mapState } from 'vuex';
// import reBuilderCard from '_com/UserCard/ReBuilderCard';
export default {
  components: {
    // reBuilderCard
  },

  watch: {},

  computed: {
    ...mapState({
      // userId: state => state.user.userId,
      role: state => state.permission.role
      // token: state => state.user.token
    })
  },

  data() {
    return {
      projectId: this.$route.params.id,
      projectInfo: {},

      ops: {
        bar: {
          background: '#808695'
        }
      }
    };
  },

  methods: {
    async init() {
      await this.getProjectInfo();
      // await this.judgeRole(this.projectInfo);
    },

    async getProjectInfo() {
      let data = await getProjectAndUsers(this.projectId);
      // console.log(data);
      this.projectInfo = data.project;
      this.creator = data.creator;
      this.members = data.members;
    },

    editProjectInfoDialog() {
      this.editProjectInfoDialogShow = true;
    },

    async getAllUsers() {},

    async editProjectInfoResponse(val) {
      if (val) {
        this.editProjectInfoDialogShow = false;
        await this.getProjectInfo();

        this.$notify({
          title: 'Success',
          message: 'You have update the project successfully!',
          type: 'success'
        });
      }
    },

    dateFormat(time) {
      return dateFormat(time);
    }
  },

  mounted() {
    this.init();
  }
};
</script>
<style lang="scss" scoped>
.information {
  width: 100%;
  min-height: calc(100vh - 240px);
  .info-card {
    background-color: white;
    min-height: calc(100vh - 242px);
    padding: 20px 32px;

    .content {
      width: 100%;
      margin-top: 10px;
      word-wrap: break-word;
      word-break: normal;
    }
  }
}
</style>
