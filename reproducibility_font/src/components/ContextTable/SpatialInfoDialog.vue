<!--  -->
<template>
  <div class="content">
    <el-form ref="spatialInfo" :model="spatialInfoForm" label-width="150px" @submit.native.prevent size="mini" class="spatialForm">
      <el-form-item label="Spatio Dimension">
        <template>
          <el-select v-model="spatialInfoForm.spatialDimension" placeholder="Select the Dimension">
            <el-option v-for="item in spatialDimensiomOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </template>
      </el-form-item>
      <el-form-item label="Spatio Scale">
        <el-input v-model="spatialInfoForm.spatialScale.type" placeholder="Please enter the content."></el-input>
      </el-form-item>

      <el-form-item label="Spatio Reference">
        <el-input v-model="spatialInfoForm.spatialReference.wkt" placeholder="Please enter the content.">
          <template #prepend>
            <el-select v-model="spatialInfoForm.spatialReference.general" placeholder="Select the Reference" class="prepend" style="width:200px">
              <el-option v-for="item in spatialReferenceOptions" :key="item" :label="item" :value="item"></el-option>
            </el-select>
            <!-- <el-button size="mini" @click="otherTemplateReference" class="append">Other Reference</el-button> -->
          </template>
        </el-input>
      </el-form-item>

      <el-form-item label="Spatio Extents" class="extents">
        <!-- <el-input v-model="spatialExtentList" placeholder="Please enter the content."></el-input> -->
        <el-input v-model="spatialExtentList[0].value" placeholder="Please enter the content." class="prepend">
          <template #append>
            <el-select v-model="spatialExtentList[0].type" placeholder="Select the Unit" style="width:140px">
              <el-option v-for="item in spatialExtentTypeList" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </template>
        </el-input>
        <el-button @click="addSpatialExtent" size="mini" class="append">Add Extents</el-button>

        <div v-for="(d, index) in addSpatialExtentCount" :key="index">
          <el-input v-model="spatialExtentList[index + 1].value" placeholder="Please enter the content." class="prepend">
            <template #append>
              <el-select v-model="spatialExtentList[index + 1].type" placeholder="Select the Unit" style="width:140px">
                <el-option v-for="item in spatialExtentTypeList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </template>
          </el-input>
        </div>
      </el-form-item>

      <el-form-item label="Resolution Constraint">
        <template>
          <el-button size="mini" @click="addSpatialConstraint" style="position: absolute;left: -60px; top: 30px;">Add</el-button>
          <el-table
            :data="spatialInfoForm.resolutionConstraintList"
            border
            style="width: 100%"
            :row-style="{ height: '40px' }"
            :cell-style="{ padding: '0px' }"
            :header-row-style="{ height: '40px' }"
            :header-cell-style="{ padding: '0px' }"
          >
            <el-table-column prop="name" label="name" width="150">
              <template #default="scope">
                <el-input v-model="scope.row.name"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="value" label="value" width="150">
              <template #default="scope">
                <el-input v-model="scope.row.value"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="unit" label="unit">
              <template #default="scope"><el-input v-model="scope.row.unit"></el-input></template>
            </el-table-column>
            <el-table-column label="edit" width="100">
              <template #default="scope">
                <el-button size="mini" @click="deleteConstraint(scope)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-form-item>
    </el-form>

    <el-button type="primary" @click="handleSpatialInfoClose" size="mini">Confirm</el-button>
  </div>
</template>

<script>
export default {
  props: {
    spatialInfo: {
      type: Object
    }
  },
  components: {},

  watch: {},

  computed: {},

  data() {
    return {
      spatialInfoForm: this.spatialInfo,
      spatialDimensiomOptions: [
        {
          value: '2',
          label: '2 Dimension'
        },
        {
          value: '3',
          label: '3 Dimension'
        }
      ],
      spatialReferenceOptions: ['projected', 'geographic', 'specific', 'all'],
      spatialExtentTypeList: ['Text', 'Envelop', 'Polygon'],
      spatialExtentList: [{ value: '', type: '' }],
      addSpatialExtentCount: []
    };
  },

  methods: {
    addSpatialExtent() {
      this.addSpatialExtentCount.push({});
      this.spatialExtentList.push({ value: '', unit: '' });
    },
    handleSpatialInfoClose() {
      this.addSpatialInfodialogVisible = false;
      this.spatialInfoForm.spatialExtentList = this.spatialExtentList;
      // this.spatialInfoForm.resolutionConstraintList =this
      // this.spatialInfoForm.spatialInfoList = [];
      this.spatialInfoForm.spatialInfoList.push(this.spatialInfoForm);
      this.$emit('getSpatialInfoReturn', this.spatialInfoForm);
    },
    addSpatialConstraint() {
      this.spatialInfoForm.resolutionConstraintList.push({ name: '', value: '', unit: '' });
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.content {
  .spatialForm {
    .prepend {
      width: 70%;
    }
    .append {
      width: 30%;
    }
  }
}
</style>
