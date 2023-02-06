<template>
  <div class="card" ref="card" :style="styleVar">
    <el-row>
      <el-col class="row" :span="24">
        <div @click="clickHander('rename')"><span>重命名</span></div>
      </el-col>
      <el-col class="row" :span="24">
        <div><span>查看</span></div>
      </el-col>
      <el-col class="row" :span="24">
        <div><span>排列方式</span></div>
      </el-col>
      <el-col style="margin-top: 10px"><el-divider class="divider"></el-divider></el-col>
      <el-col class="row" :span="24">
        <div><span>分组依据</span></div>
      </el-col>
      <el-col class="row" :span="24">
        <div><span>刷新</span></div>
      </el-col>
      <el-col class="row" :span="24">
        <div><span>复制</span></div>
      </el-col>
      <el-col style="margin-top: 10px"><el-divider class="divider"></el-divider></el-col>
      <el-col class="row" :span="24">
        <div><span>粘贴</span></div>
      </el-col>
      <el-col class="row" :span="24">
        <div @click="clickHander('delete')"><span>删除</span></div>
      </el-col>
      <el-col class="row" :span="24" style="margin-bottom: 10px">
        <div><span>属性</span></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  props: {
    position: {
      type: Object
    }
  },
  computed: {
    styleVar() {
      return {
        '--left': this.position.x + 'px',
        '--top': this.position.y + 'px'
      };
    }
  },
  methods: {
    clickHander(type) {
      this.$emit('menu', type);
    }
  },

  mounted() {}
};
</script>

<style lang="scss" scoped>
.card {
  width: 200px;
  //   height: 200px;
  background-color: #202020;
  position: absolute;
  left: var(--left);
  top: var(--top);
  border-radius: 4px;
  color: white;
  .divider {
    margin-top: 0px;
    margin-bottom: 0px;
    width: 90%;
    margin: 0 auto;
  }
  .row {
    margin-top: 10px;
    margin-left: 10px;
    width: 90%;
  }
}
.card:hover {
  cursor: default;
}
.row:hover {
  background-color: #333333;
}
</style>
