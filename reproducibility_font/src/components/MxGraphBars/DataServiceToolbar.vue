<!--  -->
<template>
  <div class="leftContainer">
    <div class="card-contain">
      <vue-scroll :ops="ops" style="max-height: 390px">
        <div v-if="dataProcessing.length == 0">
          <el-empty description="No Data"></el-empty>
        </div>
        <div v-for="(model, index) in dataProcessing" :key="index" ref="dataServiceItemList" v-else>
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
import { getAllByProjectId } from '@/api/request';

export default {
  components: { modelCard },

  data() {
    return {
      projectId: this.$route.params.id,
      dataProcessing: [],
      ops: {
        bar: {
          background: '#808695',
          keepShow: true,
        },
      },

      dataServiceFilter: { page: 0, pageSize: 8 },
    };
  },
  watch: {
    dataProcessing: function () {
      this.$nextTick(function () {
        this.$emit('getDataServices', this.dataProcessing);
      });
    },
  },

  methods: {
    async getAllInfo() {
      let content  = await getAllByProjectId(this.projectId);
      console.log(content)
      this.$set(this, 'dataProcessing', content);
    },

    getModelInfo() {
      return this.$refs['dataServiceItemList'];
      // this.$emit('chooseModel',model)
    },
  },
  created() {
    this.getAllInfo();
    // this.getPersonalModels();
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
    // height: 390px;
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
