<!--  -->
<template>
  <div class="main">
    <div class="instances">
      <div v-for="(item, index) in instanceList" :key="index">
        <instance-card :instanceItem="item"></instance-card>
      </div>
    </div>
    <div class="page">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page.sync="pageFilter.page"
        :page-size="pageFilter.pageSize"
        background
        layout="prev, pager, next"
        :total="instanceList.length + 1"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import instanceCard from './InstanceCard';
import { getAllInstancesByTaskId } from '@/api/request';
export default {
  components: { instanceCard },

  props: {
    currentTaskInit: {
      type: Object
    }
  },

  watch: {
    currentTaskInit: {
      handler(val) {
        this.currentTask = val;
        this.init();
      },
      deep: true
    }
  },

  computed: {},

  data() {
    return {
      currentTask: this.currentTaskInit,
      pageFilter: {
        pageSize: 8,
        page: 0
      },
      instanceList: []
    };
  },

  methods: {
    async init() {
      await this.getAllInstancesByTaskId(0);
    },

    async getAllInstancesByTaskId(page) {
      let data = await getAllInstancesByTaskId(this.currentTask.id, page, this.pageFilter.pageSize);
      // let data = await get(
      //   `/integrateTaskInstances/${this.currentTask.id}/${page}/${this.pageFilter.pageSize}`
      // );
      this.instanceList = data.content;
      this.pageFilter.page++;
      console.log('instances', data);
    },

    async handleCurrentChange(val) {
      await this.getAllInstancesByTaskId(val++);
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  position: relative;
  width: 100%;
  .page {
    position: absolute;
    bottom: 0;
    // padding: 0 auto;
  }
}
</style>
