<template>
  <div class="overview">
    <div class="top">
      Popular projects
      <div class="card">
        <div v-if="publicProjects.length <= 6">
          <el-row :gutter="20">
            <div v-for="(item, index) in publicProjects" :key="index">
              <el-col :span="12"><project-card :projectId="item" :type="'overview'" /></el-col>
            </div>
          </el-row>
        </div>

        <div v-else>
          <el-row :gutter="20">
            <div v-for="(item, index) in 6" :key="index">
              <el-col :span="12"><project-card :projectId="publicProjects[index]" :type="'overview'" /></el-col>
            </div>
          </el-row>
        </div>
      </div>
    </div>
    <div class="bottom"></div>
  </div>
</template>

<script>
import projectCard from './Card/projectCard.vue';
export default {
  data() {
    return {
    
    };
  },
  props: {
    publicProjects: {
      type: Array,
    },
  },

  components: {
    projectCard,
  },
  methods: {

  },
  mounted() {},
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