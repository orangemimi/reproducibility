<!--  -->
<template>
  <div>
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
        <td class="title" :rowspan="spatialInfoForm.spatialExtentList.length + 1">Spatio Extents</td>
      </tr>
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
  </div>
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
table.gridtable {
  font-family: verdana, arial, sans-serif;
  font-size: 11px;
  color: #333333;
  border-width: 1px;
  border-color: #666666;
  border-collapse: collapse;
  line-height: 15px;
  th {
    border-width: 1px;
    padding: 8px;
    border-style: solid;
    border-color: #c7c7c7;
    background-color: #dedede;
  }
  td {
    border-width: 1px;
    padding: 8px;
    border-style: solid;
    border-color: #c7c7c7;
    background-color: #ffffff;
  }
  .title {
    width: 150px;
  }
  .content {
    width: calc(9vw);
  }
}
</style>
