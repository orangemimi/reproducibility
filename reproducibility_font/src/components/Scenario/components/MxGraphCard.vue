<template>
  <div>
    <div class="modelbarTop">
      <div class="annotate model"></div>
      model
      <div class="annotate dataService"></div>
      dataService
      <div class="annotate input"></div>
      input
      <div class="annotate parameter"></div>
      parameter
      <div class="annotate output"></div>
      output
    </div>
    <div class="container" ref="container" @contextmenu.prevent></div>
  </div>
</template>

<script>
import { genGraph } from '_com/MxGraph/initMx';
import mxgraph from '_com/MxGraph/index';
const { mxGraphHandler } = mxgraph;
export default {
  data() {
    return {
      container: '',
      graph: ''
    };
  },

  props: {
    record: {
      type: Object
    }
  },

  watch: {
    record: {
      handler(val) {
        console.log(val);
        console.log('执行watch');
      },
      deep: true
    }
  },

  methods: {
    createGraph() {
      this.graph = genGraph(this.container);
      this.graph.setPanning(true);
    },

    createCell() {
      this.graph.importGraph(this.record.taskContent);
      this.graph.getModel().beginUpdate();
      try {
        this.graph.zoomTo(0.75);
        this.graph.setCellsResizable(false);
        mxGraphHandler.prototype.setMoveEnabled(false);
      } finally {
        this.graph.getModel().endUpdate();
      }
    },

    init() {
      this.container = this.$refs.container;
      this.createGraph();
      this.createCell();
    }
  },
  mounted() {
    console.log(this.record);
    this.init();
    console.log(this.graph.model);
  }
};
</script>

<style lang="scss" scoped>
.container {
  position: relative;
  overflow: hidden;
  // height: 100%;
  // width: 100%;
  min-width: calc(100%);
  height: 530px;
  //   background: rgb(251, 251, 251) 0 0 repeat;
  border-radius: 4px;
}
.modelbarTop {
  background: rgb(251, 251, 251);
  padding-left: 10px;
  border-radius: 4px;
  margin-bottom: 5px;
  display: flex;
  .annotate {
    height: 10px;
    width: 10px;
    margin-right: 5px;
    margin-left: 10px;
    margin-top: 8px;
  }
  .model {
    background: #07689f;
  }
  .dataService {
    background: #00fff8;
  }
  .input {
    background: #fff0f0;
  }
  .parameter {
    background: #f497e8;
  }
  .output {
    background: #b9e6d3;
  }
}
</style>
