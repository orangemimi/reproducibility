<template>
  <div>
    <vue-scroll style="height: 320px" :ops="ops">
      <div class="scenario">
        <scenario-card :type="'plus'" />
        <scenario-card :type="'iconfont icon-text'" />
        <scenario-card :type="'iconfont icon-picture'" />
        <scenario-card :type="'iconfont icon-video'" />
        <scenario-card :type="'iconfont icon-biaoge'" />
        <scenario-card :type="'iconfont icon-map'" />
        <scenario-card :type="'iconfont icon-flow-chart'" />
      </div>
    </vue-scroll>
  </div>
</template>

<script>
import scenarioCard from './ScenarioCard.vue';
export default {
  components: {
    scenarioCard,
  },
  data() {
    return {
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
};
</script>

<style lang="scss" scoped>
.scenario {
  height: 100%;
  width: 100%;
  display: flex;
}
</style>