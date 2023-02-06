<!-- Userhome -->
<template>
  <div class="UserHomeCardmain">
    <el-row>
      <el-col :span="16" class="left">
        <div class="l-top">
          <slot name="tit"></slot>
        </div>
        <div class="l-bottom">
          <slot name="describe"></slot>
        </div>
      </el-col>
      <el-col :span="8" class="right">
        <div class="r-top">
          <slot name="man"></slot>
        </div>
        <div class="r-bottom">
          <slot name="nav"></slot>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    return {};
  },

  methods: {},

  mounted() {}
};
</script>

<style lang="scss" scoped>
.UserHomeCardmain {
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  background: #ffffff;
  .l-top,
  .r-top {
    margin-top: 20px;
    margin-left: 20px;
  }
  .l-bottom {
    margin-left: 20px;
    margin-top: 60px;
  }
  .r-bottom {
    margin-left: 20px;
    margin-top: 30px;
    margin-bottom: 20px;
  }
  .right {
    margin-top: 18px;
    margin-bottom: 18px;
  }
  .right {
    border-left: 1px solid #e5e5e5;
  }
}
</style>
