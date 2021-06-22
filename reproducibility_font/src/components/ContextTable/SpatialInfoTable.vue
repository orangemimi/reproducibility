<!--  -->
<template>
  <table border="1" class="gridtable">
    <tr>
      <td class="title">Spatio Dimension</td>
      <td class="content" :colspan="3" v-if="spatialInfoForm.spatialDimension != ''">
        {{ spatialInfoForm.spatialDimension }}
        <span v-show="spatialInfoForm.spatialDimension != ''">Dimension</span>
      </td>
      <td class="content" :colspan="3" v-else>
        Undefined
      </td>
    </tr>
    <tr>
      <td class="title">Spatio Scale</td>
      <td class="content" :colspan="3" v-if="spatialInfoForm.spatialScale.type != ''">{{ spatialInfoForm.spatialScale.type }}</td>
      <td class="content" :colspan="3" v-else>Undefined</td>
    </tr>
    <tr>
      <td class="title">Spatio Reference</td>
      <td class="content" :colspan="3" v-if="spatialInfoForm.spatialReference.wkt != ''">{{ spatialInfoForm.spatialReference.wkt }}</td>
      <td class="content" :colspan="3" v-else>Undefined</td>
    </tr>
    <tr>
      <td class="title" :rowspan="spatialInfoForm.spatialExtentList.length + 1">Spatio Extents</td>
      <td class="content" :colspan="3" v-if="spatialInfoForm.spatialExtentList.length == 0">Undefined</td>
    </tr>

    <tr v-show="spatialInfoForm.spatialExtentList.length != 0" v-for="(item, index) in spatialInfoForm.spatialExtentList" :key="index + 'item'">
      <td class="content" :colspan="3">{{ item.value }}</td>
    </tr>

    <tr>
      <td class="title" :rowspan="spatialInfoForm.resolutionConstraintList.length + 2">Resolution Constraint</td>
      <td class="content" :colspan="3" v-if="spatialInfoForm.resolutionConstraintList.length == 0">Undefined</td>
    </tr>

    <tr v-show="spatialInfoForm.resolutionConstraintList.length != 0">
      <td>Name</td>
      <td>Value</td>
      <td>Unit</td>
    </tr>
    <tr v-show="spatialInfoForm.resolutionConstraintList.length != 0" v-for="(item, index) in spatialInfoForm.resolutionConstraintList" :key="index">
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
table.gridtable {
  font-family: verdana, arial, sans-serif;
  font-size: 11px;
  color: #333333;
  border: 1px solid #dcdfe6;
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
  }
  .content {
    width: calc(20vw);
  }
}
</style>
