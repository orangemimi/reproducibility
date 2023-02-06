<template>
  <div class="main" @click="clickHander" :title="type" :class="type"></div>
</template>

<script>
import { imgBase64 } from '@/lib/utils';

export default {
  data() {
    return {
      projectId: this.$route.params.id,
      context: ''
    };
  },
  props: {
    type: {
      type: String
    }
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
      switch (this.type) {
        case 'EssentialInformation':
          this.$router.push({
            path: `/project/context/${this.projectId}`
          });
          break;
        case 'Spatial':
          this.$router.push({
            path: `/project/spatial/${this.projectId}`
          });
          break;
        case 'Temporal':
          this.$router.push({
            path: `/project/temporal/${this.projectId}`
          });
          break;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
  height: 300px;
  border-radius: 4px;
  margin-top: 15px;
  background-color: white;

  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  /deep/ .el-card__body {
    padding: 0px;
  }
}
.EssentialInformation {
  background: url('../../../../assets/images/green1.jpg') no-repeat;
  background-size: cover;
  opacity: 0.4;
}
.Spatial {
  background: url('../../../../assets/images/green2.jpg') no-repeat;
  background-size: cover;
  opacity: 0.4;
}
.Temporal {
  background: url('../../../../assets/images/green3.jpg') no-repeat;
  background-size: cover;
  opacity: 0.4;
}
.test1 {
  background: url('../../../../assets/images/green4.jpg') no-repeat;
  background-size: cover;
  opacity: 0.4;
}
.test2 {
  background: url('../../../../assets/images/green5.jpg') no-repeat;
  background-size: cover;
  opacity: 0.4;
}
.test3 {
  background: url('../../../../assets/images/green6.jpg') no-repeat;
  background-size: cover;
  opacity: 0.4;
}
.main:hover {
  cursor: pointer;
}
</style>
