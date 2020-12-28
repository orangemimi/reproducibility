<!--  -->
<template>
  <div class="card">
    <div class="card-image">{{ project.privacy }}</div>
    <div class="card-title">{{ project.name }}</div>
    <div class="card-content"></div>
  </div>
</template>

<script>
export default {
  props: {
    project: {
      type: Object
    }
  },
  components: {},

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
.card {
  width: 100%;
  height: 300px;
  background-color: white;
  box-shadow: $normalBoxShadow;
}
</style>
