<template>
  <div class="card" v-if="project != ''">
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
        <i class="el-icon-share">{{ project.folkCount }}</i>
      </div>
      <div class="tags">
        <div v-for="(item, index) in 2" :key="index" style="display: flex">
          <div class="circle"></div>
          <p class="tag">hah</p>
        </div>
        <!-- <div v-for="(item, index) in tags" :key="index">
          <div class="circle"></div>
          <p class="tag">hah</p>
        </div> -->
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
      type: String
    }
  },
  data() {
    return {
      project: '',
      tags: [],
    };
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
      console.log(data);
    },
  },
  mounted() {
    console.log(this.projectId);
    this.init();
    console.log(this.$route.params.userId)
    console.log(this.type)
    console.log(this.$store)
  },
};
</script>

<style lang="scss" scoped>
.card {
  width: 100%;
  height: 170px;

  border-radius: 4px;
  border: 1px solid #c0c4cc;
  .head {
    display: flex;
    .title {
      font-weight: bolder;
      color: #409eff;
    }
    .privacy {
      position: relative;
      left: 70%;
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
      background-color: black;
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