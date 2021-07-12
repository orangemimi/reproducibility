<!--  -->
<template>
  <div>
    <table border="1" class="gridtable">
      <tr>
        <td class="title">Temporal Scale</td>
        <td class="content" :colspan="6" v-if="temporalInfo.temporalScale.type != ''">{{ temporalInfo.temporalScale.type }}</td>
        <td class="content" :colspan="6" v-else>Undefined</td>
      </tr>
      <tr>
        <td class="title">Temporal Reference</td>
        <td class="content" :colspan="6" v-if="temporalInfo.temporalReference.value != ''">{{ temporalInfo.temporalReference.value }}</td>
        <td class="content" :colspan="6" v-else>Undefined</td>
      </tr>

      <tr>
        <td class="title" :rowspan="temporalInfo.temporalExtentList.length + 2">Temporal Extents</td>
        <td class="content" :colspan="6" v-if="temporalInfo.temporalExtentList.length == 0">Undefined</td>
      </tr>
      <tr v-show="temporalInfo.temporalExtentList.length != 0">
        <td :colspan="3">From</td>
        <td :colspan="3">To</td>
      </tr>

      <tr v-show="temporalInfo.temporalExtentList.length != 0" v-for="(extent, indexExtent) in temporalInfo.temporalExtentList" :key="indexExtent + 'extent'">
        <td class="content" :colspan="3">{{ extent.from }}</td>
        <td class="content" :colspan="3">{{ extent.to }}</td>
      </tr>

      <tr>
        <td class="title" :rowspan="temporalInfo.stepConstraintList.length + 2">Step Constraint</td>
        <td class="content" :colspan="6" v-if="temporalInfo.stepConstraintList.length == 0">Undefined</td>
      </tr>
      <tr v-show="temporalInfo.stepConstraintList.length != 0">
        <!-- <td :colspan="2">Name</td> -->
        <td :colspan="2">Value</td>
        <td :colspan="2">Unit</td>
      </tr>

      <tr v-show="temporalInfo.stepConstraintList.length != 0" v-for="(item, index) in temporalInfo.stepConstraintList" :key="index">
        <!-- <td class="content" :colspan="2">{{ item.name }}</td> -->
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

  watch: {
    temporalInfoForm: {
      handler(val) {
        // this.temporalInfo = val;

        this.show(val);
      },
      // 深度观察监听
      deep: true,
      immediate: true
    }
  },

  computed: {},

  data() {
    return { temporalInfo: {} };
  },

  methods: {
    show(val) {
      this.$set(this, 'temporalInfo', val);
      // console.log('11111', this.temporalInfo);
      this.$forceUpdate();
    }
  },

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
