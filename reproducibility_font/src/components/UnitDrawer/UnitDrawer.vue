<!-- unit drawer -->
<template>
  <div class="main">
    <el-drawer
      title="Unit"
      :visible.sync="flag"
      :with-header="false"
      direction="rtl"
      :append-to-body="true"
      class="drawer"
      size="20%"
      :before-close="beforeClose"
      :destroy-on-close="true"
    >
      <el-row>
        <div v-for="(unit, index) in unitList" :key="index">
          <el-button type="text" @click="selectUnit(unit)">{{ unit.name }}</el-button>
        </div>
      </el-row>
      <el-row>
        <div class="page">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="pageFilter.page"
            :page-size="pageFilter.pageSize"
            :pager-count="5"
            background
            layout="prev, pager, next"
            :total="total"
          ></el-pagination>
        </div>
      </el-row>
    </el-drawer>
  </div>
</template>

<script>
import { getAllUnitsFromPoral } from '@/api/request';
export default {
  components: {},

  props: {
    drawer: {
      type: Boolean
    }
  },

  watch: {
    drawer: {
      handler(val) {
        if (val) {
          this.init();
          this.flag = val;
        }
      },
      deep: true,
      immediate: true
    }
  },

  computed: {},

  data() {
    return {
      //   currentTask: this.currentTaskInit,
      flag: false,
      pageFilter: {
        pageSize: 10,
        page: 0
      },
      unitList: [],
      total: 0
    };
  },

  methods: {
    async init() {
      await this.getAllUnits(0);
    },

    async getAllUnits(page) {
      let data = await getAllUnitsFromPoral(page, this.pageFilter.pageSize);
      // let data = await get(
      //   `/integrateTaskInstances/${this.currentTask.id}/${page}/${this.pageFilter.pageSize}`
      // );
      if (data == null) {
        this.unitList = [];
        return;
      }

      this.total = data.total;
      this.unitList = data.content;
      // this.pageFilter.page++;
      this.pageFilter.page = page + 1;
      // console.log('instances', data);
    },

    async handleCurrentChange(val) {
      await this.getAllUnits(val - 1);
    },

    selectUnit(unit) {
      this.$emit('selectUnit', unit);
    },

    beforeClose(done) {
      // console.log(done)
      done();
      console.log(222);
      this.$emit('closeDrawer', true);
    }
  }
};
</script>
<style lang="scss" scoped>
.main {
  height: 100%;
  width: 200px;
  .drawer {
    /deep/.el-drawer__body {
      height: 0;
      // height:0 overflow;flex:1
    }
  }

  .page {
    position: absolute;
    bottom: 0;
    // padding: 0 auto;
  }
}
</style>
