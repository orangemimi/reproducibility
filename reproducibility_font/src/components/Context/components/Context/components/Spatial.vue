<template>
  <div class="map">
    <spatial-info @selectData="getSpatialInfo" :spatialInfo="spatialInfo" />
  </div>
</template>

<script>
import spatialInfo from './SpatialInfo.vue';
import { getSpatial } from '@/api/request';
export default {
  components: {
    spatialInfo,
  },
  data() {
    return {
      spatialInfo: [],
      projectId: this.$route.params.projectId,
    };
  },
  methods: {
    getSpatialInfo(val) {
      this.spatialInfo = val;
      console.log(val);
    },
    async getSpatial() {
      let data = await getSpatial(this.projectId);
      console.log(data);
      this.spatialInfo = data
    },
    async init() {
      await this.getSpatial();
    },
  },
  async mounted() {
    await this.init();
  },
};
</script>

<style lang="scss" scoped>
.map {
    width: 500px;
}
</style>