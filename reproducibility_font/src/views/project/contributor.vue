<!-- info -->
<template>
  <div class="information">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="info-card">
        <div class="info-card-bottom">
          <div class="des">
            <strong>Description:</strong>
            <div class="content">
              {{ projectInfo.description }}
            </div>
          </div>
          <div class="intro">
            <strong>Introduction:</strong>
            <div class="content">
              {{ projectInfo.introduction }}
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
      creator: {},
      members: [],
      ops: {
        bar: {
          background: '#808695'
        }
      },
      editProjectInfoDialogShow: false,
      addParticipantDialogShow: false
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

    .info-card-bottom {
      width: 100%;
      margin-top: 10px;
      word-wrap: break-word;
      word-break: normal;

      .des {
        margin-bottom: 5px;
        .content {
          margin-top: 5px;
          height: 100px;
          border: 1px dotted $btnBorder;
        }
      }
      .intro {
        margin-bottom: 5px;
        .content {
          margin-top: 5px;
          height: 200px;
          border: 1px dotted $btnBorder;
        }
      }
    }
  }

  .info-card-right {
    background-color: white;
    min-height: calc(100vh - 242px);
    padding: 20px 32px;
    .participants {
      .info {
        height: 32px;
        line-height: 32px;
        margin-bottom: 5px;
        .title {
          float: left;
          font-weight: 800;
        }
        .add-participant {
          float: right;
          .el-button {
            background-color: $contain2Background;
            color: $normalFontColor;
            font-weight: 400;
            font-family: inherit;
          }
          .el-button:hover {
            color: $normalFontColor;
            border-color: $btnBorder;
            background-color: $btnHoverBg;
          }
        }
      }
      .content {
        .scroll {
        }
      }
    }

    .citation {
      .info {
        margin-bottom: 5px;

        .title {
          float: left;
          font-weight: 800;
        }
        .edit {
          float: right;
          .el-button {
            background-color: $contain2Background;
            color: $normalFontColor;
            font-weight: 400;
            font-family: inherit;
          }
          .el-button:hover {
            color: $normalFontColor;
            border-color: $btnBorder;
            background-color: $btnHoverBg;
          }
        }
      }

      .content {
        margin-top: 5px;
        height: 200px;
      }
    }
  }
}
</style>
