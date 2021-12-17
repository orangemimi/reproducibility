<template>
  <div class="main" @click="clickHander" title="EssentialInformation">
    <el-row>
      <el-col :span="15">
        <div class="left" v-if="type == 'EssentialInformation'">
          <img :src="imgPath(context.snapshot, 'EssentialInformation')" style="height: 280px; width: 100%" />
        </div>
        <div class="left" v-else-if="type == 'dimension'">
          
        </div>
      </el-col>
      <el-col :span="9">
        <div class="right">
          <div class="view">
            <i class="el-icon-view"></i>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { imgBase64 } from '@/lib/utils';

export default {
  data() {
    return {
      projectId: this.$route.params.id,
      context: '',
    };
  },
  props: {
    type: {
      type: String,
    },
  },
  methods: {
    imgPath(snapshot, name) {
      if (snapshot != undefined) {
        return snapshot;
      } else {
        return imgBase64(name);
      }
    },

    clickHander() {
      let routeData = this.$router.resolve({
        path: `/project/context/${this.projectId}`,
      });
      window.open(routeData.href, '_blank');
    },
  },
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
  height: 300px;
  border-radius: 4px;
  margin-bottom: 15px;
  background-color: white;

  // border: solid 1px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  /deep/ .el-card__body {
    padding: 0px;
  }
  .left {
    padding: 10px;
  }
  .right {
    .view {
      margin-right: 20px;
      margin-top: 5px;
      .el-icon-view {
        float: right;
      }
    }
  }
}
.main:hover {
  cursor: pointer;
}
</style>