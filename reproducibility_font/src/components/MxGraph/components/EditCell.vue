<template>
  <div>
    <div class="editNodeContainer" v-show="dialogVisible">
      <span>这是一段信息</span>

      <el-button @click="closeDialog()">取 消</el-button>
      <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
    </div>
  </div>
</template>

<script>
// import mxgraph from '../index.js';

export default {
  props: {
    visible: {
      type: Boolean
    },
    currentGraph: {
      type: Object
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val;
        console.log(val);
      }
    },
    currentGraph: {
      handler(val) {
        this.graph = val;
        console.log(val);
      }
    }
  },
  components: {},
  data() {
    return { dialogVisible: this.visible, graph: this.currentGraph };
  },
  methods: {
    closeDialog() {
      this.dialogVisible = false;
    },
    editCellValue() {}
  },
  created() {},
  mounted() {}
};
</script>
<style lang="scss" scoped>
.editNodeContainer {
  position: relative;
  width: 500px;
}
</style>
