<!-- info -->
<template>
  <div class="information">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <el-row :gutter="10">
        <el-col :span="12">
          <div class="info-card">
            <div class="info-card-top">
              <div class="info-detail">
                <div class="content">
                  <strong>Title:</strong>
                  {{ projectInfo.name }}
                </div>
                <div class="content">
                  <strong>Privacy:</strong>
                  {{ projectInfo.privacy }}
                </div>
                <div class="content">
                  <strong>Category:</strong>
                  Reproduction
                </div>
                <div class="content">
                  <strong style="float:left">Tag:</strong>
                  <div v-for="(item, index) in projectInfo.tag" :key="index">
                    <el-tag type="info" style="float:left">{{ item }}</el-tag>
                  </div>
                </div>

                <div class="content">
                  <strong>Create time:</strong>
                  {{ dateFormat(projectInfo.createTime) }}
                </div>
                <div class="content">
                  <strong>Update time:</strong>
                  {{ dateFormat(projectInfo.updateTime) }}
                </div>
              </div>

              <div class="info-img">
                <div style="float:right">
                  <!-- <el-button-group> -->
                  <el-button type="info" plain size="small" @click="editProjectInfoDialog" v-show="role == 'builder'">
                    Edit
                  </el-button>
                  <!-- </el-button-group> -->
                  <el-button-group>
                    <el-button type="info" plain size="small">
                      <i class="el-icon-share"></i>
                    </el-button>
                    <el-button type="info" plain size="small">
                      <i class="el-icon-more"></i>
                    </el-button>
                  </el-button-group>
                </div>
                <div>
                  <avatar
                    username="projectInfo.name"
                    :src="projectInfo.picture"
                    :size="150"
                    style="margin-top: 10px"
                    :rounded="false"
                    class="avatar-img"
                  ></avatar>
                </div>
              </div>
            </div>
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
        <el-col :span="12">
          <div class="info-card-right">
            <div class="participants">
              <div class="info">
                <div class="title">Participants</div>
                <div class="add-participant">
                  <el-button size="small" @click="addParticipantDialogShow = true">
                    + Add participant
                  </el-button>
                </div>
              </div>

              <div class="content">
                <vue-scroll :ops="ops" class="scroll" style="height: calc(100vh - 554px);">
                  <user-card :user="{ name: creator.name, role: 'builder' }" class=""></user-card>
                  <div v-for="(member, index) in members" :key="index">
                    <user-card :user="member"></user-card>
                  </div>
                </vue-scroll>
              </div>
            </div>
            <div class="citation">
              <div class="info">
                <div class="title">Citation</div>
                <div class="edit">
                  <el-button size="small" v-show="role == 'builder'">Edit</el-button>
                </div>
              </div>

              <!-- bind with document -->
              <div class="content">
                <el-input v-model="projectInfo.citation" :disabled="true"></el-input>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-col>

    <!-- edit the project dialog -->
    <el-dialog
      title="Edit Project Info"
      :visible.sync="editProjectInfoDialogShow"
      width="40%"
      :close-on-click-modal="false"
    >
      <edit-info-form
        :projectInfo="projectInfo"
        @editProjectInfoResponse="editProjectInfoResponse"
      ></edit-info-form>
    </el-dialog>

    <!-- share the project with email  -->
    <el-dialog
      :title="'Invite a collaborator to ' + projectInfo.name"
      :visible.sync="addParticipantDialogShow"
      width="40%"
      :close-on-click-modal="false"
    >
      <share-project></share-project>
    </el-dialog>
  </div>
</template>

<script>
import { getProjectAndUsers } from '@/api/request';
import { dateFormat } from '@/utils/utils';
import { mapState } from 'vuex';
import Avatar from 'vue-avatar';
import userCard from '_com/UserCard';
import editInfoForm from './components/EditProjectInfo';
import shareProject from '_com/ShareProject';
// import reBuilderCard from '_com/UserCard/ReBuilderCard';
export default {
  components: {
    Avatar,
    userCard,
    editInfoForm,
    shareProject
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
    .info-card-top {
      width: 100%;
      height: 280px;

      .info-detail {
        width: 60%;
        height: 100%;
        float: left;
        .content {
          margin-bottom: 5px;
          clear: both;

          .el-tag {
            display: inline;
            font-size: 14px;
            margin: 0 5px;
          }
        }
      }

      .info-img {
        // width: 40%;
        // height: 100%;
        float: right;
        // border: 1px solid $btnBorder;
        .avatar-img {
          float: right;
          border: 1px solid $btnBorder;
        }
        .el-avatar {
          float: right;
          border: 1px solid $btnBorder;
        }
      }
    }

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
