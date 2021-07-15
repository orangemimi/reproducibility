<!--  -->
<template>
  <div>
    <div>Info</div>
    <data-item-toolbar :cell="cell" @selectItemListToGraph="selectItemListToGraph"></data-item-toolbar>
  </div>
</template>

<script>
import dataItemToolbar from '_com/MxGraphBars/DataItemToolbar';
export default {
  props: {
    cell: {
      type: Object
    }
  },
  components: { dataItemToolbar },

  watch: {},

  computed: {},

  data() {
    return {};
  },

  methods: {
    selectItemListToGraph(val) {
      this.$emit('selectItemListToGraph', val);
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped></style>
