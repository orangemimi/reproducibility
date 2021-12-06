<!--  -->
<template>
  <div class="leftContainer">
    <div class="model-top">
      <div class="model-title">
        <el-row v-show="switchValue">Public Models</el-row>
        <el-row v-show="!switchValue">Private Models</el-row>
      </div>
      <el-switch v-model="switchValue" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
    </div>
    <div v-show="switchValue" class="card-contain">
      <vue-scroll :ops="ops" style="max-height: 390px;">
        <div v-for="(model, index) in publicModels" :key="index" ref="modelItemList">
          <div class="choose-model-contain">
            <model-card :modelFrom="model" @click.native="getModelInfo(model)"></model-card>
          </div>
        </div>
      </vue-scroll>
    </div>
    <div v-show="!switchValue" class="card-contain">
      <vue-scroll :ops="ops" style="height: 300px">
        <div v-for="(model, index) in personalModels" :key="index" ref="modelItemList">
          <div class="choose-model-contain">
            <model-card :modelFrom="model" @click.native="getModelInfo(model)"></model-card>
          </div>
        </div>
      </vue-scroll>
    </div>
  </div>
</template>

<script>
import modelCard from '_com/Cards/MxModelCard';
import { getModelItemsByPrivacy, getModelItemsByProvider, getModelsByProjectId } from '@/api/request';

export default {
  components: { modelCard },

  data() {
    return {
      projectId: this.$route.params.id,
      publicModels: [],
      personalModels: [],

      sentModels: [], //需要发送的工具
      ops: {
        bar: {
          background: '#808695',
          keepShow: true,
        },
      },

      publicModelFilter: { page: 1, pageSize: 8 },
      personalModelFilter: { page: 0, pageSize: 8 },

      switchValue: true,
    };
  },

  watch: {
    publicModels: {
      handler: function() {
        this.$nextTick(function () {
          this.$emit('getModels', this.publicModels);
        });
      },
      
    },
  },

  methods: {
    async getPublicModels() {
      let { content } = await getModelItemsByPrivacy('public', this.publicModelFilter.page, this.publicModelFilter.pageSize);
      this.$set(this, 'publicModels', content);
    },

    async getModelsByProjectId() {
      let data = await getModelsByProjectId(this.projectId)
      console.log(data)
      this.publicModels = data
      console.log(this.publicModels)
    },

    async getPersonalModels() {
      let data = await getModelItemsByProvider(this.personalModelFilter.page, this.personalModelFilter.pageSize);
      this.$set(this, 'personalModels', data);
      this.$emit('getModels', data);
    },

    getModelInfo() {
      console.log(this.$refs['modelItemList']);
      return this.$refs['modelItemList'];
    },
  },
  created() {
    this.getModelsByProjectId()
  },
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
  width: 100%;
  padding: 0 5px;
  // margin: 0 5px;
  .el-card__body {
    padding: 0px;
    
  }

  .model-top {
    padding: 5px 0;
    font-size: 16px;
    font-weight: 600;
    height: 40px;
    .model-title {
      float: left;
      width: 120px;
    }
  }

  .card-contain {
    max-height: 390px;
    width: 100%;
    clear: both;

    >>> .el-card.is-always-shadow,
    .el-card.is-hover-shadow:focus,
    .el-card.is-hover-shadow:hover {
      box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.1);
    }
  }
  .choose-model-contain {
    margin-bottom: 10px;
  }
}
</style>
