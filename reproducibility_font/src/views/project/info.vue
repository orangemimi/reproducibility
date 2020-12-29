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
                  <strong>Tag:</strong>
                  <div v-for="(item, index) in projectInfo.tag" :key="index">{{ item }}</div>
                </div>

                <div class="content">
                  <strong>Create time:</strong>
                  {{ projectInfo.createTime }}
                </div>
                <div class="content">
                  <strong>Update time:</strong>
                  {{ projectInfo.updateTime }}
                </div>
              </div>

              <div class="info-img">
                <div style="float:right">
                  <el-button-group>
                    <el-button type="info" plain size="small">Edit</el-button>
                  </el-button-group>
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
                  <el-button size="small">+ Add participants</el-button>
                </div>
              </div>

              <div class="content">
                <vue-scroll :ops="ops" class="scroll" style="height: calc(100vh - 554px);">
                  <user-card :user="{ name: creator.name, role: 'builder' }" class=""></user-card>
                  <div v-for="(member, index) in members" :key="index">
                    <!-- {{ { name: member.name, role: member.role } }} -->
                    <user-card :user="member"></user-card>
                  </div>
                </vue-scroll>
              </div>
            </div>
            <div class="citation">
              <div class="info">
                <div class="title">Citation</div>
                <div class="edit">
                  <el-button size="small">Edit</el-button>
                </div>
              </div>

              <!-- bind with document -->
              <div class="content">
                <el-input></el-input>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-col>
  </div>
</template>

<script>
import { get } from '@/axios';
import Avatar from 'vue-avatar';
import userCard from '_com/UserCard';
// import reBuilderCard from '_com/UserCard/ReBuilderCard';
export default {
  components: {
    Avatar,
    userCard
    // reBuilderCard
  },

  watch: {},

  computed: {},

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
      }
    };
  },

  methods: {
    async init() {
      await this.getProjectInfo();
      // await this.judgeRole(this.projectInfo);
    },

    async getProjectInfo() {
      let data = await get(`/projects/user/${this.projectId}`);
      console.log(data);
      this.projectInfo = data.project;
      this.creator = data.creator;
      this.members = data.members;
    },

    async getAllUsers() {}
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
