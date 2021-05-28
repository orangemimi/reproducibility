<!--  -->
<template>
  <div>
    {{ temporalInfoForm }}
    <table border="1" class="gridtable">
      <tr>
        <td class="title">Temporal Scale</td>
        <td class="content" :colspan="6">{{ temporalInfoForm.temporalScale.type }}</td>
      </tr>
      <tr>
        <td class="title">Temporal Reference</td>
        <td class="content" :colspan="6">{{ temporalInfoForm.temporalReference.value }}</td>
      </tr>

      <tr>
        <td class="title" :rowspan="temporalInfoForm.temporalExtentList.length + 2">Temporal Extents</td>
      </tr>
      <tr>
        <td :colspan="3">From</td>
        <td :colspan="3">To</td>
      </tr>

      <tr v-for="(item, index) in temporalInfoForm.temporalExtentList" :key="index">
        <td class="content" :colspan="3">{{ item.from }}</td>
        <td class="content" :colspan="3">{{ item.to }}</td>
      </tr>

      <tr>
        <td class="title" :rowspan="temporalInfoForm.stepConstraintList.length + 2">Step Constraint</td>
      </tr>
      <tr>
        <td :colspan="2">Name</td>
        <td :colspan="2">Value</td>
        <td :colspan="2">Unit</td>
      </tr>

      <tr v-for="(item, index) in temporalInfoForm.stepConstraintList" :key="index">
        <td class="content" :colspan="2">{{ item.name }}</td>
        <td class="content" :colspan="2">{{ item.value }}</td>
        <td class="content" :colspan="2">{{ item.unit }}</td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  props: {
    temporalInfoForm: {
      type: Object
    }
  },
  components: {},

  watch: {},

  computed: {},

  data() {
    return { temporalInfo: this.temporalInfoForm };
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
    width: calc(20vw);
  }
}
</style>
