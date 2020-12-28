<template>
  <div class="container">
    <div class="header">
      <router-view name="header"></router-view>
    </div>
    <!-- <div class="main" :style="{ minHeight: contentHeight - 120 + 'px' }"> -->
    <div class="main" style="min-height: calc(100vh - 120px)">
      <router-view name="main"></router-view>
    </div>
    <div class="footer">
      <router-view name="footer"></router-view>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      contentHeight: 0
    };
  },
  methods: {
    initSize() {
      this.contentHeight = window.innerHeight;
    }
  },
  created() {
    this.initSize();
  }
};
</script>

<style scoped lang="scss">
.container {
  min-width: 1000px;
  display: flex;
  flex-direction: column;
  height: 100%;

  .header {
    height: 60px;
  }
  .main {
    flex: 1 0 auto;
  }
  .footer {
    height: 60px;
    width: 100%;
    flex: 0 0 auto;
  }
}
</style>
