<!--  -->
<template>
  <div>
    <div class="btn">
      <el-button class="btn-left" :disabled="isDisable" @click="folkClick">
        <div>
          <i class="iconfont icon-fork"></i>
          Fork
        </div>
      </el-button>
      <div class="btn-right">
        <div>
          {{ count }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    count: {
      type: Number
    },
    isDisable: {
      type: Boolean
    }
  },
  components: {},

  computed: {},

  data() {
    return {
      projectId: this.$route.params.id,
      isDisable2: this.isDisable
    };
  },

  methods: {
    folkClick() {
      this.$router.push({
        path: `/project/fork/${this.projectId}`
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.btn {
  width: 120px;
  font-size: 12px;
  font-weight: 550;
  text-align: center;
  vertical-align: middle;

  box-shadow: $btnShadow;
  transition: 0.2s cubic-bezier(0.3, 0, 0.5, 1);

  // .btn-left {
  /deep/.el-button {
    line-height: 32px;
    font-size: 12px;
    font-weight: 550;
    padding: 0;
    text-align: center;
    vertical-align: middle;
    width: 80px;
    float: left;
    border: 1px solid $btnBorder;
    border-radius: 4px 0px 0 4px;
    background-color: $contain2Background;
    transition-property: color, background-color, border-color;
  }
  // }
  /deep/.el-button:hover {
    cursor: pointer;
    background-color: $btnHoverBg;
  }

  .btn-right {
    width: 40px;
    float: left;
    border: 1px solid $btnBorder;
    border-radius: 0px 4px 4px 0px;
    background-color: $contain2Background;
  }
  .btn-right:hover {
    cursor: pointer;
    color: $blueEmplasisFont;
  }
}
</style>
