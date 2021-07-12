<!-- general bar -->
<template>
  <div class="mainTool">
    <div v-for="(item, index) in generalList" :key="index" ref="general">
      <div class="tool">
        <img :src="item.icon" :alt="item['name']" class="image" />
        <div class="name">{{ item['name'] }}</div>
      </div>
      <!-- <div class="rec" v-show="showBarType == 'code'" ref="code">
        <img :src="item.icon" :alt="item['name']" class="recImage" />
        <div class="name">{{ item['name'] }}</div>
      </div> -->
    </div>
  </div>
</template>

<script>
import { codeList } from './CodeToolbar';
export default {
  props: {
    showBarType: {
      type: String
    }
  },
  components: {},

  watch: {},

  computed: {
    codeList: () => codeList // general toolbar
  },

  data() {
    return {};
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped>
.mainTool {
  .tool {
    width: 200px;
    .name {
      position: absolute;
      top: 25px;
      left: 105px;
      // text-align: center;
    }
    .image {
      width: 130px;
      margin: 10px 0px 0 70px;
      text-align: center;
    }
  }

  .rec {
    width: 150px;
    height: 100px;
    .recImage {
      width: 90px;
      margin: 10px 0px 0 70px;
      text-align: center;
    }
  }
}
</style>
