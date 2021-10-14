<template>
  <div class="card">
    <p class="title">{{ project.name }}</p>
    <p class="des">{{ project.description }}</p>
    <div>
      <el-row>
        <el-col :span="4">
          <div class="tags">
            <div class="circle"></div>
            <p class="tag">hah</p>
          </div>
        </el-col>
        <el-col :span="4">
            <i class="el-icon-star-on"></i>
        </el-col>
        <el-col :span="4"></el-col>
      </el-row>
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
  },
  data() {
    return {
      project: {},
    };
  },
  methods: {
    async init() {
      let data = await getProjectById(this.projectId);
      this.project = data;
      console.log(data);
    },
  },
  mounted() {
    console.log(this.projectId);
    this.init();
  },
};
</script>

<style lang="scss" scoped>
.card {
  width: 100%;
  height: 140px;

  border-radius: 4px;
  border: 1px solid #c0c4cc;
  .title {
    margin-top: 25px;
    margin-left: 15px;
    font-size: 16px;
    font-weight: bolder;
    color: #409eff;
  }
  .des {
    margin-top: 10px;
    margin-left: 15px;
    font-size: 15px;
    margin-bottom: 10px;
  }
  .tags {
    display: flex;
    .circle {
      border-radius: 50%;
      width: 13px;
      height: 13px;
      background-color: black;
      margin-top: 7px;
      margin-left: 15px;
    }
    .tag {
        margin-left: 3px;
    }
  }
}
</style>