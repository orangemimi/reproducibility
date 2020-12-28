<!-- info -->
<template>
  <div class="information">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <el-row :gutter="10">
        <el-col :span="12">
          <div class="info-card">
            <div class="info-card-top">
              <div class="info-img">
                <img src="r/pictureFile/f97dcd2d-e1cf-4c10-a99c-d40667ba0899地球2.png " />
              </div>

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
                  <div v-for="(item, index) in projectInfo.tag" :key="index"></div>
                  {{ item }}
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
          <div class="info-card">
            {{ creator }}
            <div class="" v-for="(member, index) in members" :key="index">
              {{ member.name }}
            </div>
          </div>
        </el-col>
      </el-row>
    </el-col>
  </div>
</template>

<script>
import { get } from '@/axios';
export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    return { projectId: this.$route.params.id, projectInfo: {}, creator: {}, members: [] };
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

      .info-img {
        width: 280px;
        height: 100%;
        border: 1px solid $btnBorder;
        float: left;
        margin-right: 32px;

        img {
          width: 100%;
          height: 100%;
        }
      }
      .info-detail {
        height: 100%;

        .content {
          margin-bottom: 5px;
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
}
</style>
