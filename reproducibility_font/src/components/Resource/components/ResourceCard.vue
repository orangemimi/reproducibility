<template>
  <div>
    <el-card :body-style="{ padding: '0px' }">
      <img :src="imgPath(modelItem.data.image, modelItem.data.name)" class="image" @click="cardClick" />
      <div style="padding: 14px">
        <div class="title" :title="modelItem.data.name">{{ modelItem.data.name }}</div>
        <div class="bottom">
          <el-tag size="small">{{ modelItem.type }}</el-tag>
          <el-button type="primary" icon="el-icon-delete" class="button" size="mini"></el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { imgBase64 } from '@/lib/utils';
export default {
  data() {
    return {};
  },
  props: {
    modelItem: {
      type: Object
    }
  },

  methods: {
    imgPath(snapshot, name) {
      if (snapshot != undefined && snapshot != null && snapshot != '') {
        return snapshot;
      } else {
        return imgBase64(name);
      }
    },
    cardClick() {
      window.open('http://geomodeling.njnu.edu.cn/computableModel/' + this.modelItem.data.doi, '_blank');
    }
  }
};
</script>

<style lang="scss" scoped>
.el-card {
  width: 220px;
  height: 300px;
  margin-right: 10px;
  .title {
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 1;
    height: 20px;
    overflow: hidden;
    &:hover {
      cursor: pointer;
    }
  }
  .image {
    width: 220px;
    height: 220px;
    &:hover {
      cursor: pointer;
    }
  }
  .bottom {
    margin-top: 5px;
    .button {
      float: right;
    }
  }
}
</style>
