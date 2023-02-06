<template>
  <div>
    <vue-scroll style="height: 320px" :ops="ops">
      <div class="resource">
        <div v-for="(item, index) in modelList" :key="'index1' + index">
          <resource-card :modelItem="{ type: 'Model', data: item }"></resource-card>
        </div>
        <div v-for="(item, index) in dataserverList" :key="'index2' + index">
          <resource-card :modelItem="{ type: 'Dataserver', data: item }"></resource-card>
        </div>
      </div>
    </vue-scroll>
  </div>
</template>

<script>
import ResourceCard from './ResourceCard.vue';
import { getModelsByProjectId, getAllByProjectId } from '@/api/request';
export default {
  data() {
    return {
      projectId: this.$route.params.id,
      modelList: [],
      dataserverList: [],
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
          disable: false
        },
        scrollPanel: {
          scrollingY: false,
          scrollingX: true
        }
      }
    };
  },
  components: {
    ResourceCard
  },

  methods: {
    async getModelsByProjectId() {
      let data = await getModelsByProjectId(this.projectId);
      this.modelList = data;
    },
    async getAllByProjectId() {
      let data = await getAllByProjectId(this.projectId);
      this.dataserverList = data;
    },
    async init() {
      await this.getModelsByProjectId();
      await this.getAllByProjectId();
    }
  },
  async mounted() {
    await this.init();
  }
};
</script>

<style lang="scss" scoped>
.resource {
  padding: 5px;
  display: flex;
}
</style>
