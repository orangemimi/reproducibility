<!--  -->
<template>
  <div class="card">
    <!-- <div class="card-image">{{ project.privacy }}</div>
    <div class="card-title">{{ project.name }}</div>
    <div class="card-content"></div> -->

    <el-card class="box-card">
      <div @click="judgeRole(project)">
        <img :src="imgPath(project.picture, project.name)" />
        <div class="content">
          <h3 class="title" :title="project.name">{{ project.name }}</h3>
          <p class="desc" :title="project.description">{{ project.introduction }}</p>
        </div>
      </div>

      <!-- <el-button class="config-btn" type="text" @click="view(project.id)">View</el-button> -->
      <div v-show="project.userRole == 'visitor'">
        <el-button class="config-btn" type="text" @click="joinProjectCommand">Join</el-button>

        <!-- <el-dropdown @command="joinProjectCommand">
          <span class="el-dropdown-link">
            Join this project
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="rebuilder_explorer">as explorer</el-dropdown-item>
              <el-dropdown-item command="rebuilder_operator">as operator</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown> -->
      </div>
    </el-card>
  </div>
</template>

<script>
import { imgBase64 } from '@/lib/utils';
import { mapState } from 'vuex';
import { saveNotice } from '@/api/request';
export default {
  props: {
    project: {
      type: Object
    }
  },
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
    return {};
  },

  methods: {
    imgPath(picture, name) {
      if (picture != undefined && picture != '') {
        return picture;
      }
      return imgBase64(name);
    },
    view(id) {
      this.$router.push({
        path: `/resource/${id}/${this.type}`
      });
    },
    async joinProjectCommand() {
      let notice = { recipientId: this.project.creator, type: 'apply', content: { type: 'joinProject', role: 'rebuilder_operator', projectId: this.project.id, projectName: this.project.name} };
      await saveNotice(notice);
    },

    async judgeRole(project) {
      console.log(this.role);
      await this.$store.dispatch('permission/getRole', {
        project: project,
        userId: this.userId
      });
      console.log(this.role);
    
      this.$router.push({ path: `/project/${project.id}/info` });
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.card {
  width: 100%;
  height: 400px;
  // background-color: white;
  // box-shadow: $normalBoxShadow;

  .box-card {
    margin: 10px 2%;
    width: 300px;
    height: 100%;
    transition: box-shadow 0.1s ease;
    .el-card__body {
      padding: 0;
    }
    .content {
      padding: 10px 18px;
      word-wrap: break-word;
      min-height: 115px;
      max-height: 115px;
      .title {
        max-height: 25px;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 1;
        overflow: hidden;
      }
      .desc {
        max-height: 44px;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }
    }

    &:hover {
      box-shadow: $normalBoxShadow;
    }
    img {
      width: 100%;
      object-fit: cover;
      height: 200px;
    }
    .config-btn {
      float: right;
      margin: 0px 20px;
    }
  }
}
</style>
