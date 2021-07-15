<!--  -->
<template>
  <div class="main">
    555
    <el-row>
      <el-card shadow="hover" class="box-card">
        <i class="el-icon-plus" />
      </el-card>
    </el-row>
    <el-row>
      <div class="instances">
        <div v-for="(item, index) in instanceList" :key="index">
          <instance-card :instanceItem="item" @click.native="showConfiguration(item)"></instance-card>
        </div>
      </div>
    </el-row>
    <el-row>
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
    </el-row>
  </div>
</template>

<script>
import instanceCard from '_com/Cards/InstanceCard';
import { getAllIntegrateTaskInstancesByTaskId } from '@/api/request';
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
        if (Object.prototype.hasOwnProperty.call(val, 'id')) {
          this.currentTask = val;
          this.init();
        }
      },
      deep: true,
      immediate: true
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
      await this.getAllInstances(0);
    },

    async getAllInstances(page) {
      let data = await getAllIntegrateTaskInstancesByTaskId(this.currentTask.id, page, this.pageFilter.pageSize);
      // let data = await get(
      //   `/integrateTaskInstances/${this.currentTask.id}/${page}/${this.pageFilter.pageSize}`
      // );
      if (data == null) {
        this.instanceList = [];
        return;
      }

      this.instanceList = data.content;
      this.pageFilter.page++;
      // console.log('instances', data);
    },

    async handleCurrentChange(val) {
      await this.getAllInstances(val++);
    },

    showConfiguration(item) {
      this.$emit('showInstanceStatus', item);
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  position: relative;
  width: 100%;
  .instances {
    width: 100%;
  }
  .page {
    position: absolute;
    bottom: 0;
    // padding: 0 auto;
  }
}
</style>
