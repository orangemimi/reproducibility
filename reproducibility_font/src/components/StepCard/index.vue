<template>
  <div class="card">
    <el-row>
      <el-col :span="24">
        <div>
          <!-- <div class="top">
            <div class="title">{{ info.btnType }}</div>
            <div class="top-icon"><i class="el-icon-top-right"></i></div>
          </div> -->
          <div class="content">
            <div class="content-comp">
              <component :is="typeMapping(info.btnType)"></component>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ContextContent from '_com/Context/Context';
import ScenarioContent from '_com/Scenario/Scenario';
// import ReScenarioContent from '_com/Scenario/reScenario';
import ResourceContent from '_com/Resource/Resource';
import ResultContent from '_com/Result/Result';
// import contentResource from '_com/Context/components/Resource/ContentResource.vue';
import ResourceDefinition from '_com/Resource/components/ResourceDefinition.vue';
import contentScenario from '_com/Context/components/Scenario/ContentScenario.vue';
import contentContext from '_com/Context/components/Context/ContentContext.vue';
import forkScenario from '_com/Scenario/ForkScenario.vue';

// import AnalysisContent from "./AnalysisContent";
export default {
  components: {
    ContextContent,
    ScenarioContent,
    ResourceContent,
    ResultContent,
    // contentResource,
    ResourceDefinition,
    contentScenario,
    contentContext,
    forkScenario
  },
  props: {
    cardInfo: {
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
        case 'Content Definition':
          {
            vueType = 'ContextContent';
          }
          break;
        case 'Simulation Scenario':
          {
            vueType = 'ScenarioContent';
          }
          break;
        case 'Reproducible Simulation Scenario':
          {
            vueType = 'ReScenarioContent';
          }
          break;
        case 'Resource Collection':
          {
            vueType = 'ResourceContent';
          }
          break;
        case 'Expected Result':
          {
            vueType = 'ResultContent';
          }
          break;
        case 'Resource Definition':
          {
            vueType = 'ResourceDefinition';
          }
          break;
        case 'Scenario Definition':
          {
            vueType = 'contentScenario';
          }
          break;
        case 'Context Definition':
          {
            vueType = 'contentContext';
          }
          break;
        case 'forkScenario': {
          vueType = 'forkScenario';
        }
      }
      return vueType;
    }
  }
};
</script>

<style lang="scss" scoped>
.card {
  background: #ffffff;
  // box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
  // opacity: 1;
  height: 100%;
  width: 100%;
  // margin: 10px;
  // border-bottom-left-radius: 4px;
  // border-bottom-right-radius: 4px;

  // .top {
  //   // background: $headerBackground;
  //   color: rgb(255, 255, 255);
  //   font-size: 16px;
  //   line-height: 30px;
  //   height: 30px;
  //   border-top-right-radius: 4px;
  //   border-top-left-radius: 4px;
  //   .title {
  //     margin-left: 10px;
  //     //   width: 200px;
  //     float: left;
  //   }
  //   .top-icon {
  //     float: right;
  //     margin-right: 10px;
  //     width: 20px;
  //   }
  // }
  .content-comp {
    // padding: 10px;
    width: 100%;
    height: 100%;
  }
}
</style>
