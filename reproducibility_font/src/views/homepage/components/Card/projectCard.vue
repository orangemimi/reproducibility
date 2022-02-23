<template>
  <div class="card" v-if="project != ''" v-cloak>
    <div class="head">
      <p class="title">{{ project.name }}</p>
      <p class="privacy" v-show="type == 'projects' && $route.params.userId == $store.state.user.userId">{{ project.privacy }}</p>
    </div>
    <div class="des">
      <p>{{ project.description }}</p>
    </div>
    <div>
      <div class="details">
        <i class="el-icon-star-on" style="margin-right: 10px">{{ project.starCount }}</i>
        <i class="iconfont icon-fork" style="font-size: 13px;margin-right: 10px">{{ project.folkCount }}</i>
        <i class="el-icon-view" style="margin-right: 2px"></i>{{ project.watchCount }}
      </div>
      <div class="tags">
        <div v-for="(item, index) in tags" :key="index" style="display: flex">
          <div class="circle" :style="getColor(item)"></div>
          <p class="tag">{{ item }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getProjectById } from '@/api/request';
export default {
  props: {
    projectId: {
      type: String,
    },
    type: {
      type: String,
    },
  },
  data() {
    return {
      project: '',
      tags: [],
    };
  },

  computed: {
    getColor() {
      return function (tag) {
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
    },
  },

  methods: {
    async init() {
      let data = await getProjectById(this.projectId);
      this.project = data;
      if (data.tags == null) {
        this.tags = [];
      } else {
        this.tags = data.tags;
      }
      // console.log(data);
    },
  },
  mounted() {
    this.init();
  },
};
</script>

<style lang="scss" scoped>
[v-cloak] {
  display: none !important;
}
.card {
  width: 100%;
  height: 170px;
  // box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  border-radius: 4px;
  border: 1px solid #c0c4cc;
  .head {
    display: flex;
    position: relative;
    .title {
      font-weight: bolder;
      color: #409eff;
    }
    .privacy {
      position: absolute;
      left: 75%;
      opacity: 0.5;
    }
    font-size: 13px;
    margin-top: 25px;
    margin-left: 15px;
  }
  .des {
    margin-top: 10px;
    margin-left: 15px;
    font-size: 13px;
    margin-bottom: 13px;
    height: 40px;
  }
  .tags {
    display: flex;
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
  .details {
    margin-left: 13px;
    margin-top: 8px;
    font-size: 13px;
  }
}
</style>