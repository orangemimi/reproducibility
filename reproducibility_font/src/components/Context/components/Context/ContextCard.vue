<template>
  <div>
    <router-link target="_blank" :to="{ path: `/project/context/${projectId}` }">
      <el-card shadow="hover" class="main">
        <div class="top" v-if="type == 'EssentialInformation'">
          <img :src="imgPath(context.snapshot, 'Context')" style="height: 250px; width: 100%" />
        </div>
        <div class="top" v-else></div>
      </el-card>
    </router-link>
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
  // box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  /deep/ .el-card__body {
    padding: 0px;
  }
  .top {
    height: 250px;
  }
}
</style>