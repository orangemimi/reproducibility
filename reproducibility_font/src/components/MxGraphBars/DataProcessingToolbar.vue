<!--  -->
<template>
  <div class="leftContainer">
    <div class="card-contain">
      <vue-scroll :ops="ops" style="height: 390px">
        <div v-for="(model, index) in dataProcessing" :key="index" ref="modelItemList">
          <div class="choose-model-contain">
            <model-card :modelFrom="model" @click.native="getModelInfo(model)"></model-card>
          </div>
        </div>
      </vue-scroll>
    </div>
  </div>
</template>

<script>
import modelCard from './ModelCard';
import { getDataServices } from '@/api/request';

export default {
  components: { modelCard },

  data() {
    return {
      projectId: this.$route.params.id,
      dataProcessing: [],
      ops: {
        bar: {
          background: '#808695',
          keepShow: true
        }
      },

      dataServiceFilter: { page: 0, pageSize: 8 }
    };
  },

  methods: {
    async getAllInfo() {
      let { content } = await getDataServices(this.dataServiceFilter.page, this.dataServiceFilter.pageSize);
      // let { content } = await get(
      //   `/modelItems/public/${this.dataServiceFilter.page}/${this.dataServiceFilter.pageSize}`
      // );

      this.$set(this, 'dataProcessing', content);
      this.$emit('getDataProcessings', content);
    },

    getModelInfo() {
      return this.$refs['modelItemList'];
      // this.$emit('chooseModel',model)
    }
  },
  created() {
    this.getAllInfo();
    // this.getPersonalModels();
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
    height: 300px;
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
