<!--  -->
<template>
  <div class="btn">
    <div class="btn-left">
      <div>
        <i class="el-icon-edit"></i>
        Star
      </div>
    </div>
    <div class="btn-right">
      <div>
        {{ count }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    count: {
      type: Number
    }
  },
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
.btn {
  width: 120px;
  font-size: 12px;
  font-weight: 550;
  text-align: center;
  vertical-align: middle;

  box-shadow: $btnShadow;
  transition: 0.2s cubic-bezier(0.3, 0, 0.5, 1);

  .btn-left {
    width: 80px;
    float: left;
    border: 1px solid $btnBorder;
    border-radius: 4px 0px 0 4px;
    background-color: $contain2Background;
    transition-property: color, background-color, border-color;
  }
  .btn-left:hover {
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
