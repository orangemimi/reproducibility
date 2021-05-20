<!--  -->
<template>
  <div>
    <el-input v-model="data.content" />
    <div>
      <children :data="data.children"></children>
    </div>
  </div>
</template>

<script>
export default {
  name: 'children',
  props: ['data'],
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
<style lang="scss" scoped></style>
