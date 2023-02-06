<template>
  <div>
    <div class="card">
      <img :src="imgPath(image, modelItem.label)" class="image" />
      <div class="body">
        <div class="title">{{ modelItem.label }}</div>
        <div class="bottom">
          <div class="type">
            <el-tag size="small" :type="modelItem.classify == 'model' ? 'danger' : 'warning'">{{ modelItem.classify }}</el-tag>
          </div>
          <el-button type="text" size="mini" class="btn" @click="clickView">View</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPictureByDOI } from '@/api/request';
import { imgBase64 } from '@/lib/utils';
export default {
  props: {
    modelItem: {
      type: Object
    }
  },
  data() {
    return {
      image: ''
    };
  },
  methods: {
    imgPath(snapshot, name) {
      if (snapshot != undefined || snapshot != null || snapshot != '') {
        return snapshot;
      } else {
        return imgBase64(name);
      }
    },
    clickView() {
      window.open('http://geomodeling.njnu.edu.cn/computableModel/' + this.modelItem.doi, '_blank');
    },
    async getPictureByDOI() {
      let data = await getPictureByDOI(this.modelItem.doi);
      this.image = data;
    },
    async init() {
      if (this.modelItem.classify == 'model') {
        await this.getPictureByDOI();
      } else {
        this.image = '';
      }
    }
  },
  async mounted() {
    await this.init();
  }
};
</script>

<style lang="scss" scoped>
.card {
  height: 120px;
  width: 100%;
  padding: 5px;
  border: solid 0.5px #dcdfe6;
  border-radius: 4px;
  display: flex;
  margin-top: 15px;
  .image {
    height: 110px;
    width: 110px;
  }
  .body {
    position: relative;
    .title {
      margin-left: 10px;
    }
    .bottom {
      bottom: 10px;
      position: absolute;
      margin-left: 10px;
      display: flex;
      .btn {
        margin-left: 100px;
      }
    }
  }
}
</style>
