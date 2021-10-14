<template>
  <div class="overview">
    <div class="top">
      Popular projects
      <div class="card">
        <div v-if="joinedProjects.length <= 6">
          <el-row :gutter="20">
            <div v-for="(item, index) in joinedProjects" :key="index">
              <el-col :span="12"><overview-card :projectId="item" /></el-col>
            </div>
          </el-row>
        </div>

        <div v-else>
          <el-row :gutter="20">
            <div v-for="(item, index) in 6" :key="index">
              <el-col :span="12"><overview-card :projectId="joinedProjects[index]" /></el-col>
            </div>
          </el-row>
        </div>
      </div>
    </div>
    <div class="bottom"></div>
  </div>
</template>

<script>
import overviewCard from './overviewCard/overviewCard.vue';
export default {
  props: {
    joinedProjects: {
      type: Array,
    },
  },
  components: {
    overviewCard,
  },
};
</script>

<style lang="scss" scoped>
.overview {
  width: 100%;
  .top {
    font-size: 18px;
    .card {
      margin: 0 40;
      margin-top: 5px;
    }
  }
}
</style>