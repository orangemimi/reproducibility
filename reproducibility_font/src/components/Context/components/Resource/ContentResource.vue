<template>
  <div>
    <vue-scroll style="height: 320px" :ops="ops">
      <div class="resource">
        <resource-card :type="'plus'" />
        <!-- :key="'info-'+ index"这种写法原因，这里写了两个for循环，尽管都加上了key值,然而又将key的值都写成index，会导致vue警告 -->
        <resource-card :type="'input'" :resourceItem="item" v-for="(item, index) in resourceInfo.inputs" :key="'info-' + index" />
        <resource-card :type="'parameter'" :resourceItem="item" v-for="(item, index) in resourceInfo.parameters" :key="'info1-' + index" />
        <resource-card :type="'output'" :resourceItem="item" v-for="(item, index) in resourceInfo.outputs" :key="'info2-' + index" />
      </div>
    </vue-scroll>
  </div>
</template>

<script>
import resourceCard from './ResourceCard.vue';

import { getAllResource } from '@/api/request';
export default {
  components: {
    resourceCard,
  },
  data() {
    return {
      projectId: this.$route.params.id,
      resourceInfo: {},
      ops: {
        bar: {
          onlyShowBarOnScroll: false,
          keepShow: true,
          background: '#c1c1c1',
          opacity: 1,
          hoverStyle: false,
          specifyBorderRadius: false,
          minSize: 0,
          size: '6px',
          disable: false,
        },
        scrollPanel: {
          scrollingY: false,
          scrollingX: true,
        },
      },
    };
  },
  methods: {
    init() {
      this.getAllResource();
    },
    async getAllResource() {
      let data = await getAllResource(this.projectId);
      this.resourceInfo = data;
    },
  },
  created() {
    this.init();
  },
};
</script>

<style lang="scss" scoped>
.resource {
  height: 100%;
  width: 100%;
  display: flex;
}
</style>