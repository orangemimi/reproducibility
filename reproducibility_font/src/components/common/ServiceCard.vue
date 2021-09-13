<template>
  <div>
    <el-card class="box-card">
      <img :src="imgPath(item.snapshot, item.name)" />
      <div class="content">
        <h3 class="title" :title="item.name">{{ item.name }}</h3>
        <p class="desc" :title="item.des">{{ item.description }}</p>
      </div>
      <el-button class="config-btn" type="text" @click="view(item.id)">View</el-button>
    </el-card>
  </div>
</template>

<script>
import { imgBase64 } from '@/lib/utils';
export default {
  props: ['item', 'type'],
  methods: {
    imgPath(snapshot, name) {
      if (snapshot != undefined) {
        return snapshot;
      } else {
        return imgBase64(name);
      }
    },
    view(id) {
      this.$router.push({
        path: `/resource/${id}/${this.type}`
      });
    }
  },
  mounted() {
    // console.log(this.item)
    // console.log(this.item.snapshot)
  }
};
</script>

<style lang="scss" scoped>
.box-card {
  margin: 10px 2%;
  width: 300px;
  min-height: 275px;
  transition: box-shadow 0.1s ease;
  .el-card__body {
    padding: 0;
  }
  .content {
    padding: 10px 18px;
    word-wrap: break-word;
    min-height: 115px;
    max-height: 115px;
    .title,
    .desc {
      max-height: 44px;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }
  }

  &:hover {
    box-shadow: 0px 0px 20px #666;
  }
  img {
    width: 100%;
    object-fit: cover;
    height: 200px;
  }
  .config-btn {
    float: right;
    margin: 0px 20px;
  }
}
</style>
