<!--  -->
<template>
  <div>
    <el-row>
      <div class="leftContainer">
        <div class="tool-top">
          <div class="tool-title">
            <el-row v-show="switchValue">Public Tools</el-row>
            <el-row v-show="!switchValue">Private Tools</el-row>
          </div>
          <el-switch
            v-model="switchValue"
            active-color="#13ce66"
            inactive-color="#ff4949"
          ></el-switch>
        </div>
        <div v-show="switchValue" class="card-contain">
          <vue-scroll :ops="ops" style="height: 480px">
            <div v-for="(tool, index) in publicTools" :key="index" ref="toolItemList">
              <div class="choose-tool-contain">
                <tool-card :toolFrom="tool" @click.native="getTool(tool)"></tool-card>
              </div>
            </div>
          </vue-scroll>
        </div>
        <div v-show="!switchValue" class="card-contain">
          <vue-scroll :ops="ops" style="height: 480px">
            <div v-for="(tool, index) in personalTools" :key="index" ref="toolItemList">
              <div class="choose-tool-contain">
                <model-card
                  :toolFrom="tool"
                  :isOpenTool="false"
                  @click.native="getTool(tool)"
                ></model-card>
              </div>
            </div>
          </vue-scroll>
        </div>
      </div>
    </el-row>
  </div>
</template>

<script>
import modelCard from './ModelCard';
import { get, del, post, put } from '@/axios';

export default {
  components: { modelCard },
  data() {
    return {
      projectId: this.$route.params.projectId,
      publicTools: [],
      personalTools: [],
      userId: this.$store.getters.userInfo.userId,
      sentTools: [], //需要发送的工具
      ops: {
        bar: {
          background: '#808695'
        }
      },
      typeSelected: 'All',
      typeOptions: [
        'All',
        'General step',
        'Context definition & resource collection',
        'Data processing',
        'Data visualization',
        'Geographic model construction',
        'Model effectiveness evaluation',
        'Geographical simulation',
        'Quantitative and qualitative analysis',
        'Decision-making for management',
        'Others'
      ],
      switchValue: false
    };
  },

  mounted() {
    this.init();
  },

  methods: {
    initSetTimeOut() {
      return new Promise(function(reslove, reject) {
        // 模拟异步，加载组件
        setTimeout(function() {
          console.log('等了1秒钟');
          reslove(1000);
        }, 1000);
      });
    },
    init() {
      this.getPublicTools();
      this.getPersonalTools();
    },

    async getPublicTools() {
      let data = await get('/modelItems/inquiry/?key=privacy&value=Public');
      this.$set(this, 'publicTools', data);
      this.$emit('getPublicTools', data);
    },

    async getPersonalTools() {
      let data = await get(`/modelItems/findByProvider/${this.userId}`);
      this.$set(this, 'personalTools', data);
      this.$emit('getPersonalTools', data);
    },
    getTool() {
      return this.$refs['toolItemList'];

      // this.$emit('chooseTool',tool)
    }
  }
};
</script>
<style lang="scss" scoped>
.ellipsis {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: top;
}
.leftContainer {
  // float: left;
  width: 160px;
  // margin: 0 5px;
  >>> .el-card__body {
    padding: 0px;
  }
}

.tool-top {
  padding: 5px 0;
  font-size: 16px;
  font-weight: 600;
  height: 40px;
  .tool-title {
    float: left;
    width: 100px;
  }
}

.card-contain {
  height: 500px;
  width: 100%;
  clear: both;

  >>> .el-card.is-always-shadow,
  .el-card.is-hover-shadow:focus,
  .el-card.is-hover-shadow:hover {
    box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.1);
  }
}
.choose-tool-contain {
  margin-bottom: 10px;
}
</style>
