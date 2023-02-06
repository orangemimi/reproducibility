<template>
  <el-dropdown @command="handClick">
    <el-button type="primary" size="mini">
      Select
      <i class="el-icon-arrow-down el-icon--right"></i>
    </el-button>
    <template #dropdown>
      <el-dropdown-menu>
        <vue-scroll style="height: 200px">
          <el-dropdown-item v-for="(item, index) in tidList" :key="index" :command="item">{{ item }}</el-dropdown-item>
        </vue-scroll>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script>
import { getInstanceOfUncompleted } from '@/api/request';
export default {
  data() {
    return {
      tidList: []
    };
  },
  methods: {
    async getInstanceOfUncompleted() {
      let data = await getInstanceOfUncompleted('616d15def4d34c0e9651350e');
      this.tidList = data;
    },
    async handClick(val) {
      this.$emit('getTid', val);
    }
  },
  created() {
    this.getInstanceOfUncompleted();
    console.log(1);
  }
};
</script>

<style lang="scss" scoped></style>
