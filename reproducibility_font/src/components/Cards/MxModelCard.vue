<!-- card -->
<template>
  <div class="main">
    <el-card class="cardBody" shadow="hover">
      <div class="card_info">
        <h4 :title="item.name" class="card_info_name">
          {{ item.name }}
        </h4>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  props: {
    modelFrom: {
      type: Object
    }
  },

  watch: {
    modelFrom: {
      handler(val) {
        this.item = val;
      }
    }
  },

  data() {
    return {
      item: this.modelFrom,
      isOpen: this.isOpenTool,
      openTool: false
      // toolPreview
    };
  },

  methods: {}
};
</script>
<style lang="scss" scoped>
.main {
  /deep/ .el-card:hover {
    background-color: #3067d61c;
    border: 0.5px solid #25252534;
  }

  /deep/ .el-card__body {
    padding: 5px;
    height: 40px;
  }

  .cardBody {
    background-color: rgb(255, 255, 255);
    cursor: pointer;
    // height: 42px;
    width: 100%;
    padding: 0 5px;

    .card_info {
      width: 100%;
      .card_info_name {
        display: block;
        width: 120px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
    }
  }
}
</style>
