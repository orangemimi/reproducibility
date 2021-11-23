<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <p>All activity</p>
        <el-divider></el-divider>
        <div v-if="dataList != ''">
          <div v-if="dataList.public.length == 0">
            <el-empty description="No Data"></el-empty>
          </div>
          <div v-for="(item, index) in dataList.public" :key="index" class="card">
            <record :record="item" />
            <el-divider></el-divider>
          </div>
          <el-pagination :page-size="20" :pager-count="5" layout="prev, pager, next" :total="dataList.public.length"></el-pagination>
        </div>
      </div>
    </el-col>
  </div>
</template>

<script>
import record from './components/record.vue';
import { getAllInstancesOfReproductionByProjectId } from '@/api/request';
export default {
  components: { record },
  data() {
    return {
      projectId: this.$route.params.id,
      dataList: '',
    };
  },
  methods: {
    async getData() {
      this.dataList = await getAllInstancesOfReproductionByProjectId(this.projectId);
      console.log(this.dataList);
    },
  },
  mounted() {
    this.getData();
  },
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
  //   min-height: calc(100vh - 240px);
  height: 100%;

  .main-card {
    background-color: white;
    min-height: calc(100vh - 242px);
    height: 100%;
    padding: 20px 45px;
    .el-pagination {
      text-align: center;
    }
  }
}
</style>