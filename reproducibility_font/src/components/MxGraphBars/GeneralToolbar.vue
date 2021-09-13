<!-- general bar -->
<template>
  <div class="main">
    <div v-show="showType == 'rhombus'">
      <div v-for="(item, index) in rhombusList" :key="index" ref="general">
        <div class="tool">
          <img :src="item.icon" :alt="item['name']" class="image" :title="item['name']" />
          <!-- <div class="name">{{ item['name'] }}</div> -->
        </div>
      </div>
    </div>
    <div v-show="showType == 'rectangle'">
      <div v-for="(item, index) in rectangleList" :key="index" ref="code">
        <div class="tool">
          <img :src="item.icon" :alt="item['name']" class="image" :title="item['name']" />
          <!-- <div class="name">{{ item['name'] }}</div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { generalList } from './toolbar';
export default {
  props: {
    showType: {
      type: String
    }
  },

  components: {},

  watch: {},

  computed: {
    rhombusList: () => {
      return generalList.filter(item => item.style.shape == 'rhombus');
    }, // general toolbar
    rectangleList: () => {
      return generalList.filter(item => item.style.shape == 'rectangle');
    }
  },

  data() {
    return {};
  },

  methods: {},

  mounted() {
    // console.log(this.rhombusList)
    // console.log(this.rectangleList)
  }
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
