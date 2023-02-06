<template>
  <div>
    <el-card class="box-card" shadow="hover">
      <div class="body" @click="clickProjectCard">
        <el-avatar
          :size="60"
          shape="square"
          :src="project.picture != '' && project.picture != undefined ? project.picture : imgBase64(project.name)"
        ></el-avatar>
        <p class="title">{{ project.name }}</p>
        <p class="introduction" :title="project.introduction">{{ project.introduction }}</p>
      </div>
      <div class="details">
        <i class="el-icon-star-on" style="margin-right: 10px">{{ project.starCount }}</i>
        <i class="iconfont icon-fork" style="font-size: 13px; margin-right: 10px">{{ project.folkCount }}</i>
        <i class="el-icon-view" style="margin-right: 2px"></i>
        {{ project.watchCount }}
      </div>
      <div class="tags">
        <div v-for="(item, index) in tags" :key="index" style="display: flex">
          <div class="circle" :style="getColor(item)"></div>
          <p class="tag">{{ item }}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { imgBase64 } from '@/lib/utils';
export default {
  props: {
    project: {
      type: Object
    }
  },

  computed: {
    getColor() {
      return function(tag) {
        let temp = '';
        for (let i = 0; i < tag.length; i++) {
          temp += tag[i].charCodeAt().toString(16);
        }
        if (temp.length > 6) {
          // 长度大于6时取后6位作为颜色值
          temp = temp.substr(-6);
        } else if (temp.length > 3) {
          // 长度小于6大于3时取后3位作为颜色值
          temp = temp.substr(-3);
        } else {
          // 若长度小于三则设置默认色
          temp = 'aquamarine';
        }
        return 'background-color: #' + temp;
      };
    }
  },
  data() {
    return {
      tags: []
    };
  },
  methods: {
    imgBase64(projectName) {
      return imgBase64(projectName);
    },
    async clickProjectCard() {
      await this.$store.dispatch('permission/getRole', {
        project: this.project,
        userId: this.$store.userId
      });
      this.$router.push({ path: `/project/${this.project.id}/info` });
    }
  },
  created() {
    if (this.project.tags == null) {
      this.tags = [];
    } else {
      this.tags = this.project.tags;
    }
  }
};
</script>

<style scoped lang="scss">
.el-card {
  margin-bottom: 20px;
  .body {
    text-align: center;
    height: 200px;
    .el-avatar {
      margin-bottom: 20px;
    }
    .title {
      font-size: 19px;
      font-weight: 600;
      margin-bottom: 5px;
    }
    .introduction {
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 3;
      overflow: hidden;
    }
  }
  .details {
    margin-left: 13px;
    // margin-top: 8px;
    font-size: 13px;
  }
  .tags {
    display: flex;
    height: 17px;
    margin-top: 10px;
    .circle {
      border-radius: 50%;
      width: 11px;
      height: 11px;
      // background-color: black;
      margin-top: 5px;
      margin-left: 15px;
    }
    .tag {
      margin-left: 3px;
      font-size: 13px;
    }
  }
}
</style>
