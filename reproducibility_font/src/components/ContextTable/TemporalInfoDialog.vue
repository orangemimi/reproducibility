<!--  -->
<template>
  <div class="content">
    <el-form ref="temporalInfo" :model="temporalInfoForm" label-width="150px" @submit.native.prevent size="mini" class="temporalForm">
      <el-form-item label="Temporal Scale">
        <el-input v-model="temporalInfoForm.temporalScale.type" placeholder="Please enter the content."></el-input>
      </el-form-item>

      <el-form-item label="Temporal Reference" class="reference">
        <div v-if="otherTemplateReferenceVisible == false">
          <el-select v-model="temporalInfoForm.temporalReference.value" placeholder="Select the Reference" class="prepend">
            <el-option v-for="item in temporalReferenceList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
          <el-button size="mini" @click="otherTemplateReference" class="append">Other Reference</el-button>
        </div>
        <div v-else>
          <el-input v-model="temporalInfoForm.temporalReference.value" placeholder="Please enter the content." class="prepend">
            <template #append>
              <el-button @click="otherTemplateReferenceVisible = false" class="append">Return</el-button>
            </template>
          </el-input>
        </div>
      </el-form-item>

      <el-form-item label="Temporal Extents" class="extends">
        <div class="block">
          <time-extent-horizontal @getReturnTime="getReturnTime" class="extent_comp"></time-extent-horizontal>

          <div v-for="(d, index) in addTemporalCount" :key="index">
            <time-extent-horizontal @getReturnTime="getReturnTime" class="extent_comp"></time-extent-horizontal>
          </div>
          <el-button @click="addTemporalExtents" size="mini" class="append">Add Extent</el-button>
        </div>
      </el-form-item>

      <el-form-item label="Step Constraint" title="Add Spatial Info" class="reference">
        <el-input v-model="stepConstraintList[0].value" placeholder="Please enter the content." class="prepend">
          <template #append>
            <el-select v-model="stepConstraintList[0].unit" placeholder="Select the Unit" style="width:140px">
              <el-option v-for="item in temporalUnitList" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </template>
        </el-input>

        <div v-for="(d, index) in addTemporalConstraintCount" :key="index">
          <el-input v-model="stepConstraintList[index + 1].value" placeholder="Please enter the content." class="prepend">
            <template #append>
              <el-select v-model="stepConstraintList[index + 1].unit" placeholder="Select the Unit" style="width:140px">
                <el-option v-for="item in temporalUnitList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </template>
          </el-input>
        </div>
        <el-button @click="addTemporalConstraint" size="mini" class="append">Add Constraint</el-button>
      </el-form-item>
    </el-form>

    <el-button type="primary" @click="handleTemporalInfoClose" size="mini">Confirm</el-button>
  </div>
</template>

<script>
import timeExtentHorizontal from '_com/TimeExtent/TimeExtentHorizontal';
export default {
  props: {
    temporalInfo: {
      type: Object
    }
  },
  components: { timeExtentHorizontal },

  watch: {},

  computed: {},

  data() {
    return {
      temporalInfoForm: this.temporalInfo,
      addTemporalConstraintCount: [],
      otherTemplateReferenceVisible: false,
      temporalExtentList: [],
      temporalUnitList: ['second', 'hour', 'day', 'month', 'year'],
      temporalReferenceList: ['GMT', 'UTC', 'UNIX'],
      addTemporalCount: [],

      stepConstraintList: [{ value: '', unit: '' }]
    };
  },

  methods: {
    handleTemporalInfoClose() {
      this.addTemporalInfodialogVisible = false;
      this.temporalInfoForm.temporalExtentList = this.temporalExtentList;
      this.temporalInfoForm.stepConstraintList = this.stepConstraintList;
      this.$emit('getTemporalInfoReturn', this.temporalInfoForm);

      // this.temporalInfoForm.temporalInfoList.push(this.temporalInfoForm);

      console.log(this.temporalInfoForm);
    },
    addTemporalExtents() {
      this.addTemporalCount.push({});
    },

    addTemporalConstraint() {
      this.addTemporalConstraintCount.push({});

      this.stepConstraintList.push({ value: '', unit: '' });
    },
    getReturnTime(val) {
      let time = {
        from: val[0],
        to: val[1]
      };
      this.temporalExtentList.push(time);
    },
    deleteConstraint(row) {
      console.log(row.$index);
      console.log(this.spatialInfoForm.resolutionConstraintList);
      this.spatialInfoForm.resolutionConstraintList.splice(row.$index);
    },
    otherTemplateReference() {
      this.otherTemplateReferenceVisible = true;
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.content {
  .temporalForm {
    .reference {
      .prepend {
        width: 70%;
      }
      .append {
        width: 30%;
      }
    }
    .extends {
      .block {
        .extent_comp {
          float: left;
          width: 70%;

          /deep/.el-date-editor--datetimerange.el-input__inner {
            width: 100%;
          }
          /deep/.el-date-editor .el-range-separator {
            width: 10%;
          }
          /depp/.el-range-editor.el-input__inner {
            padding: 0;
          }
        }
        .append {
          width: 30%;
        }
      }
    }
  }
}
</style>
