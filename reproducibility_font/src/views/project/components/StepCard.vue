<template>
  <div class="card">
    <el-row>
      <el-col :span="24">
        <div>
          <div class="top">
            <div class="title">{{ info.btnType }}</div>
            <div class="top-icon"><i class="el-icon-top-right"></i></div>
          </div>
          <div class="content">
            <div class="content-comp">
              <component :is="typeMapping(info.btnType)" :projectInfo="projectInfo"></component>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ContextContent from './Context/ContextContent';
import ConstructionContent from './Construction/ConstructionContent';
import ResourceContent from './Resource/ResourceContent';
// import ResultContent from "./ResultContent";
// import AnalysisContent from "./AnalysisContent";
export default {
  components: {
    ContextContent,
    ConstructionContent,
    ResourceContent
    // ResultContent,
    // AnalysisContent,
  },
  props: {
    cardInfo: {
      type: Object
    },
    projectInfo: {
      type: Object
    }
  },
  watch: {
    cardInfo: {
      handler(val) {
        this.info = val;
      },
      deep: true
    }
  },
  data() {
    return {
      info: this.cardInfo
    };
  },
  methods: {
    typeMapping(type) {
      let vueType;
      switch (type) {
        case 'Context Definition':
          {
            vueType = 'ContextContent';
          }
          break;
        case 'Simulation Construction':
          {
            vueType = 'ConstructionContent';
          }
          break;
        case 'Resource Collection':
          {
            vueType = 'ResourceContent';
          }
          break;
        case 'Result Analysis':
          {
            vueType = 'ResultContent';
          }
          break;
      }
      return vueType;
    }
  }
};
</script>

<style lang="scss" scoped>
.card {
  background: #ffffff;
  box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
  opacity: 1;
  height: 100%;
  margin: 10px;
  .top {
    background: $headerBackground;
    color: rgb(255, 255, 255);
    font-size: 16px;
    line-height: 30px;
    height: 30px;
    .title {
      margin-left: 10px;
      //   width: 200px;
      float: left;
    }
    .top-icon {
      float: right;
      margin-right: 10px;
      width: 20px;
    }
  }
  .content-comp {
    margin: 10px;
  }
}
</style>
