<!-- general bar -->
<template>
  <div class="main">
    <div v-for="(item, index) in generalList" :key="index" ref="general">
      <div class="tool">
        <img :src="item.icon" :alt="item['name']" class="image" />
        <div class="name">{{ item['name'] }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { generalList } from './toolbar';
export default {
  components: {},

  watch: {},

  computed: {
    generalList: () => generalList // general toolbar
  },

  data() {
    return {};
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  width: 200px;
  .image {
    width: 130px;
    margin: 10px 0px 0 70px;
    text-align: center;
  }
  .tool:hover {
    cursor: pointer;
  }
  .name {
    position: absolute;
    top: 25px;
    left: 105px;
    // text-align: center;
  }
}
</style>
