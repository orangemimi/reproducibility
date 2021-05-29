<template>
  <div class="main">
    <!-- {{ contextForm }} -->
    <vue-scroll style="height: calc(40vh) " :ops="ops">
      <el-form ref="contextForm" :model="contextForm" label-width="110px" @submit.native.prevent size="mini">
        <el-form-item label="Theme">
          <el-input v-if="tagInputVisible" v-model="themeTag" ref="tagInput" @keyup.enter.native="addTags" @blur="addTags" style="width: 300px"></el-input>
          <el-button v-else @click="showTagInput" type="text" size="small">+ New Tag</el-button>
          <div v-if="contextForm != null">
            <el-tag :key="tagIndex" v-for="(tag, tagIndex) in contextForm.themes" closable :disable-transitions="false" @close="delTags(tag)" style="margin: 0 2px">
              {{ tag }}
            </el-tag>
          </div>
        </el-form-item>
        <el-form-item label="Purpose">
          <el-input v-model="contextForm.purpose" placeholder="Please enter the content."></el-input>
        </el-form-item>
        <el-form-item label="Object">
          <el-input v-model="contextForm.objects" placeholder="Please enter the content."></el-input>
        </el-form-item>
        <el-form-item label="Spatio scale">
          <el-button @click="addSpatialInfodialogVisible = true" size="mini">Edit</el-button>
          <spatial-info-table :spatialInfoForm="spatialInfoForm"></spatial-info-table>
        </el-form-item>

        <el-form-item label="Temporal scale">
          <el-button @click="addTemporalInfodialogVisible = true" size="mini">Edit</el-button>
          <!-- {{ temporalInfoForm }} -->
          <temporal-info-table :temporalInfoForm="temporalInfoForm"></temporal-info-table>
        </el-form-item>
        <el-form-item label="Method">
          <el-input v-model="contextForm.methods" placeholder="Please enter the content."></el-input>
        </el-form-item>
        <el-form-item label="Discussion">
          <el-input v-model="contextForm.discussion" placeholder="Please enter the content."></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitContext">Submit</el-button>
        </el-form-item>
      </el-form>
    </vue-scroll>

    <el-dialog :visible.sync="addSpatialInfodialogVisible" width="40%" title="Add Temporal Info" class="spatialDialog">
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
            <el-table
              :data="spatialInfoForm.resolutionConstraintList"
              border
              style="width: 100%"
              :row-style="{ height: '40px' }"
              :cell-style="{ padding: '0px' }"
              :header-row-style="{ height: '40px' }"
              :header-cell-style="{ padding: '0px' }"
            >
              <el-table-column prop="name" label="name" width="60"></el-table-column>
              <el-table-column prop="value" label="value" width="200">
                <template #default="scope">
                  <el-input v-model="scope.row.value"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="unit" label="unit">
                <template #default="scope"><el-input v-model="scope.row.unit"></el-input></template>
              </el-table-column>
            </el-table>
          </template>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="handleSpatialInfoClose" size="mini">Confirm</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog :visible.sync="addTemporalInfodialogVisible" width="40%" class="temporalDialog" title="Add Temporal Info">
      <el-form ref="spatialInfo" :model="temporalInfoForm" label-width="150px" @submit.native.prevent size="mini" class="temporalForm">
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
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="handleTemporalInfoClose" size="mini">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { getContextByProjectId, updateContexByProjectId, saveRecord, updatePerformanceById } from '@/api/request';
import { mapState } from 'vuex';
import timeExtentHorizontal from '_com/TimeExtent/TimeExtentHorizontal';
import temporalInfoTable from '_com/ContextTable/TemporalInfoTable';
import spatialInfoTable from '_com/ContextTable/SpatialInfoTable';
import { hasProperty } from '@/utils/utils';

// import timeExtentVertical from '_com/TimeExtent/TimeExtentVertical';

export default {
  computed: {
    ...mapState({
      userId: state => state.user.userId,
      userName: state => state.user.name,
      role: state => state.permission.role
    })
  },
  components: {
    timeExtentHorizontal,
    temporalInfoTable,
    spatialInfoTable
    // descriptionComp
    // timeExtentVertical
  },

  data() {
    return {
      projectId: this.$route.params.id,
      contextForm: {
        themes: [],
        purpose: '',
        objects: '',
        spatialInfoList: [],
        temporalInfoList: [],
        methods: '',
        discussion: ''
      },

      themeTag: '',
      tagInputVisible: false,

      //spatial-temporal-info
      addSpatialInfodialogVisible: false,
      addTemporalInfodialogVisible: false,
      spatialInfoForm: {
        enable: false,
        spatialReference: {
          general: '',
          wkt: ''
        },
        spatialDimension: '',
        spatialScale: {
          type: '',
          description: ''
        },
        spatialExtentList: '',
        resolutionConstraintList: [
          { name: 'x', value: '', unit: '' },
          { name: 'y', value: '', unit: '' }
        ]
      },

      temporalInfoForm: {
        enable: false,
        temporalScale: {
          type: '',
          description: ''
        },
        temporalReference: {
          value: ''
        },
        temporalExtentList: [],
        stepConstraintList: []
      },
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
      temporalExtentList: [],
      temporalUnitList: ['second', 'hour', 'day', 'month', 'year'],
      temporalReferenceList: ['GMT', 'UTC', 'UNIX'],
      addTemporalCount: [],
      addSpatialExtentCount: [],
      addTemporalConstraintCount: [],
      otherTemplateReferenceVisible: false,
      stepConstraintList: [{ value: '', unit: '' }],
      spatialExtentList: [{ value: '', type: '' }],
      spatialExtentTypeList: ['Text', 'Envelop', 'Polygon'],
      // updateContext: true
      //mxgraph scrollbar
      ops: {
        bar: {
          onlyShowBarOnScroll: false,
          keepShow: true,
          background: '#c1c1c1',
          opacity: 1,
          hoverStyle: false,
          specifyBorderRadius: false,
          minSize: 0,
          size: '6px',
          disable: false
        }
      }
    };
  },

  methods: {
    async getContext() {
      // console.log(this.projectId);
      if (this.projectId == null) {
        this.$message({
          message: 'Get information error!',
          type: 'error'
        });
        return;
      }
      let data = await getContextByProjectId(this.projectId);
      // console.log('context', data);
      this.contextForm = data;
      console.log(data);
      this.spatialInfoForm = data.spatialInfoList[0];
      this.temporalInfoForm = data.temporalInfoList[0];
      this.temporalExtentList = data.temporalInfoList[0].temporalExtentList[0];
      this.$forceUpdate();
    },
    async submitContext() {
      // this.$refs.contextForm.validate(async valid => {
      //   if (valid) {

      let data = await updateContexByProjectId(this.projectId, this.contextForm);
      await this.saveRecord(data, 'updated');
      await this.updatePerformance();
      // }
      // });
      this.$emit('closeForm', false);
    },

    async saveRecord(context, tag) {
      let record = {};
      record.nodeId = context.id;
      record.nodeType = 'contextDefinition';
      record.projectId = this.projectId;
      record.content = `${this.userName} has ${tag} a context definition in this reproducible project`;
      await saveRecord(record);
    },

    async updatePerformance() {
      let content = { content: 'Context Definition', degree: '100%', type: 'success', icon: 'el-icon-edit' };

      await updatePerformanceById('context', this.projectId, content);
    },

    showTagInput() {
      this.tagInputVisible = true;
      this.$nextTick(() => {
        this.$refs.tagInput.$refs.input.focus();
        // console.log(_);
      });
    },
    addTags() {
      console.log('5555555555555555');
      let tag = this.themeTag;
      if (!hasProperty(this.contextForm, 'themes') || this.contextForm.themes == null) {
        this.contextForm.themes = [];
      }
      if (tag != '') {
        this.contextForm.themes = [];
        this.contextForm.themes.push(tag);
      }
      this.tagInputVisible = false;
      this.themeTag = '';
    },
    delTags(tag) {
      this.contextForm.themes.splice(this.contextForm.themes.indexOf(tag), 1);
    },
    handleSpatialInfoClose() {
      this.addSpatialInfodialogVisible = false;
      this.spatialInfoForm.spatialExtentList = this.spatialExtentList;
      // this.spatialInfoForm.resolutionConstraintList =this
      this.contextForm.spatialInfoList = [];
      this.contextForm.spatialInfoList.push(this.spatialInfoForm);
    },
    handleTemporalInfoClose() {
      this.addTemporalInfodialogVisible = false;
      this.temporalInfoForm.temporalExtentList = this.temporalExtentList;
      this.temporalInfoForm.stepConstraintList = this.stepConstraintList;

      this.contextForm.temporalInfoList = [];
      this.contextForm.temporalInfoList.push(this.temporalInfoForm);

      // console.log(this.contextForm);
    },
    addTemporalExtents() {
      this.addTemporalCount.push({});
    },
    addSpatialExtent() {
      this.addSpatialExtentCount.push({});
      this.spatialExtentList.push({ value: '', unit: '' });
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
    //设置表格行的样式
    // eslint-disable-next-line no-unused-vars
    tableRowStyle({ row, rowIndex }) {
      return 'background-color:pink;font-size:15px;';
    },
    otherTemplateReference() {
      this.otherTemplateReferenceVisible = true;
    }
  },
  created() {
    this.getContext();
  }
};
</script>
<style lang="scss" scoped>
.main {
  margin: 0 20px;
  height: 100%;
  .spatialDialog {
    .spatialForm {
      .prepend {
        width: 70%;
      }
      .append {
        width: 30%;
      }
    }
  }

  .temporalDialog {
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
}
</style>
