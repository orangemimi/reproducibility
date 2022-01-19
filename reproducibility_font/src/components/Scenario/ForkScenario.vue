<template>
  <div>
    <el-carousel :autoplay="false" type="card" height="550px">
      <el-carousel-item v-for="(item, index) in taskContentList" :key="index">
        <mx-graph-card :record="item"></mx-graph-card>
      </el-carousel-item>
    </el-carousel>
    <scenario/>
  </div>
</template>

<script>
import mxGraphCard from './components/MxGraphCard.vue';
import scenario from './Scenario.vue'
import { getRecords } from '@/api/request';
export default {
  data() {
    return {
      projectId: this.$route.params.id,
      taskContentList: [],
    };
  },
  components: {
    mxGraphCard,
    scenario
  },
  methods: {
    async getRecords() {
      let data = await getRecords(this.projectId);
      this.taskContentList = data;
    },
  },
  async mounted() {
    await this.getRecords();
  },
};
</script>

<style lang="scss" scoped>
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>