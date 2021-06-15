<!--  -->
<template>
  <table border="1" class="gridtable">
    <tr>
      <td class="title">Spatio Dimension</td>
      <td class="content" :colspan="3">
        {{ spatialInfoForm.spatialDimension }}
        <span v-show="spatialInfoForm.spatialDimension != ''">Dimension</span>
      </td>
    </tr>
    <tr>
      <td class="title">Spatio Scale</td>
      <td class="content" :colspan="3">{{ spatialInfoForm.spatialScale.type }}</td>
    </tr>
    <tr>
      <td class="title">Spatio Reference</td>
      <td class="content" :colspan="3">{{ spatialInfoForm.spatialReference.value }}</td>
    </tr>
    <tr>
      <td class="title" :rowspan="spatialInfoForm.spatialExtentList.length == 0 ? 1 : spatialInfoForm.spatialExtentList.length + 1">Spatio Extents</td>
    </tr>

    <!-- <tr v-if="spatialInfoForm.spatialExtentList.length == 0">
      <td class="content" :colspan="3"></td>
    </tr> -->
    <tr v-for="(item, index) in spatialInfoForm.spatialExtentList" :key="index + 'item'">
      <td class="content" :colspan="3">{{ item.value }}</td>
    </tr>
    <tr>
      <td class="title" :rowspan="spatialInfoForm.resolutionConstraintList.length + 2">Resolution Constraint</td>
    </tr>
    <tr>
      <td>Name</td>
      <td>Value</td>
      <td>Unit</td>
    </tr>
    <tr v-for="(item, index) in spatialInfoForm.resolutionConstraintList" :key="index">
      <td class="content">{{ item.name }}</td>
      <td class="content">{{ item.value }}</td>
      <td class="content">{{ item.unit }}</td>
    </tr>
  </table>
</template>

<script>
export default {
  props: {
    spatialInfoForm: {
      type: Object
    }
  },
  components: {},

  watch: {},

  computed: {},

  data() {
    return { spatialInfo: this.spatialInfoForm };
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped>
table {
  font-family: verdana, arial, sans-serif;
  font-size: 8px;
  color: #333333;
  border: 1px solid #dcdfe6;
  // border-width: 1px;
  // border-color: #dcdfe6;
  border-collapse: collapse;
  line-height: 15px;
  th {
    border-width: 1px;
    padding: 8px;
  }
  td {
    border-width: 1px;
    padding: 8px;
  }
  .title {
    width: 150px;
    color: $fontColorGrayNormal;
    font-style: italic;
    font-size: 8px;
  }
  .content {
    width: calc(9vw);
  }
}
</style>
