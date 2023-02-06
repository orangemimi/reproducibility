<template>
  <div class="main">
    <el-row>
      <el-col :span="1">
        <div>
          <el-avatar :size="50">user</el-avatar>
        </div>
      </el-col>
      <el-col :span="23">
        <div class="card" v-if="record != null">
          <div class="card-top">
            <div class="text">{{ record.userInfo.name }}</div>
            <div class="text">ran the task</div>
            <div class="text">{{ getDate }}</div>
          </div>
          <div class="card-bottom">
            <div class="title">
              <div class="circle success" v-if="record.status == 1"></div>
              <div class="circle fail" v-else></div>
              Computationally reproducible
            </div>
            <el-divider></el-divider>
            <div class="content">
              <div class="result">
                <el-tag>标签一</el-tag>
                <el-tag>标签一</el-tag>
                <el-tag>标签一</el-tag>
              </div>
            </div>
            <el-divider class="divider-bottom"></el-divider>
            <div class="comments">
              <!-- <i class="iconfont icon-good-fill-copy"></i> -->
              <i class="iconfont icon-good-fill"></i>
              <i class="iconfont icon-comments-fill"></i>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { dateFormat } from '@/utils/utils';
export default {
  props: {
    record: {
      type: Object
    }
  },
  data() {
    return {};
  },
  computed: {
    getDate() {
      return dateFormat(new Date(parseInt(this.record.createTime)));
    }
  },
  methods: {},
  mounted() {}
};
</script>

<style lang="scss" scoped>
.main {
  height: 100%;
  .card {
    //   background: black;
    height: 100%;
    width: 100%;

    .card-top {
      height: 50px;
      display: flex;
      .text {
        line-height: 50px;
        margin-right: 3px;
      }
    }
    .card-bottom {
      height: 100%;
      background-color: #f6f6f6;
      border-radius: 4px;
      padding-top: 15px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      .title {
        margin-left: 35px;
        display: flex;
        .circle {
          border-radius: 50%;
          width: 12px;
          height: 12px;
          margin-right: 3px;
          margin-top: 6px;
        }
        .success {
          background-color: #67c23a;
        }
        .fail {
          background-color: #f56c6c;
        }
      }
      .el-divider {
        width: 95%;
        margin-left: auto;
        margin-right: auto;
      }
      .divider-bottom {
        margin-bottom: 5px;
      }
      .content {
        .result {
          margin-left: 35px;
          .el-tag {
            margin-right: 3px;
          }
        }
      }
      .comments {
        margin-left: 35px;
      }
    }
  }
}
</style>
