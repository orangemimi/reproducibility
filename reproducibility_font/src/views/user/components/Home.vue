<!--  -->
<template>
  <div class="main">
    <user-home-card :infoObject="infoObject"></user-home-card>
  </div>
</template>

<script>
import userHomeCard from '_com/Cards/UserHomeCard';
export default {
  components: { userHomeCard },

  watch: {},

  computed: {},

  data() {
    return {};
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  width: 100%;
  height: 100%;
  // margin-left: 200px;
  // background-color: rgba($color: #000000, $alpha: 1);
}
</style>
