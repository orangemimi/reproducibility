<template>
  <div class="projects">
    Projects
    <div class="cards">
      <div v-if="$route.params.userId == $store.state.user.userId">
        <el-row :gutter="20">
          <div v-for="(item, index) in joinedProjects" :key="index">
            <el-col :span="12"><project-card :projectId="item" :type="'projects'" class="card" /></el-col>
          </div>
        </el-row>
      </div>
      <div v-else>
        <el-row :gutter="20">
          <div v-for="(item, index) in publicProjects" :key="index">
            <el-col :span="12"><project-card :projectId="item" :type="'projects'" class="card" /></el-col>
          </div>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import projectCard from './Card/projectCard.vue';
export default {
  props: {
    joinedProjects: {
      type: Array
    },
    publicProjects: {
      type: Array
    }
  },

  data() {
    return {};
  },

  methods: {},

  components: {
    projectCard
  },

  mounted() {
    // this.init();
  }
};
</script>

<style lang="scss" scoped>
.projects {
  font-size: 18px;
  .cards {
    margin-top: 5px;
    .card {
      margin-bottom: 5px;
    }
  }
}
</style>
