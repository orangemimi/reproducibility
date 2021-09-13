<template>
  <div class="main">
    <vue-scroll style="height: calc(42vh) " :ops="ops">
      <el-form ref="contextForm" :model="contextForm" @submit.native.prevent size="mini" label="top" style="margin:0 10px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Theme">
              <el-input v-if="tagInputVisible" v-model="themeTag" ref="tagInput" @keyup.enter.native="addTags" @blur="addTags" style="width: 300px"></el-input>
              <el-button v-else @click="showTagInput" type="text" size="small">+ New Tag</el-button>
              <div v-if="contextForm != null">
                <el-tag
                  :key="tagIndex"
                  v-for="(tag, tagIndex) in contextForm.themes"
                  closable
                  :disable-transitions="false"
                  @close="delTags(tagIndex)"
                  style="margin: 0 2px"
                >
                  {{ tag }}
                </el-tag>
              </div>
            </el-form-item>
            <el-form-item label="Purpose">
              <el-input v-model="contextForm.purpose" placeholder="Please enter the content." type="textarea" maxlength="550" show-word-limit></el-input>
            </el-form-item>
            <!-- <el-form-item label="Object">
              <el-input v-model="contextForm.objects" placeholder="Please enter the content."></el-input>
            </el-form-item> -->
            <el-form-item label="Spatio scale">
              <el-button @click="addSpatialInfodialogVisible = true" size="mini" class="spatioEdit">Edit</el-button>
              <spatial-info-table :spatialInfoForm="spatialInfoForm"></spatial-info-table>
            </el-form-item>
          </el-col>

          <el-col :span="12" style="margin-top:50px">
            <el-form-item label="Temporal scale">
              <el-button @click="addTemporalInfodialogVisible = true" size="mini" class="temporalEdit">Edit</el-button>
              <!-- {{ temporalInfoForm }} -->
              <temporal-info-table :temporalInfoForm="temporalInfoForm"></temporal-info-table>
            </el-form-item>
            <!-- <el-form-item label="Method">
              <el-input v-model="contextForm.methods" placeholder="Please enter the content."></el-input>
            </el-form-item> -->
            <el-form-item label="Discussion">
              <el-input v-model="contextForm.discussion" placeholder="Please enter the content." type="textarea" maxlength="50" show-word-limit></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitContext">Submit</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </vue-scroll>

    <el-dialog :visible.sync="addSpatialInfodialogVisible" width="40%" title="Add Spatial Info" class="spatialDialog">
      <spatial-info-dialog :spatialInfo="spatialInfoForm" @getSpatialInfoReturn="getSpatialInfoReturn"></spatial-info-dialog>

      <!-- <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="handleSpatialInfoClose" size="mini">Confirm</el-button>
        </span>
      </template> -->
    </el-dialog>

    <el-dialog :visible.sync="addTemporalInfodialogVisible" width="40%" class="temporalDialog" title="Add Temporal Info">
      <temporal-info-dialog :temporalInfo="temporalInfoForm" @getTemporalInfoReturn="getTemporalInfoReturn"></temporal-info-dialog>
    </el-dialog>
  </div>
</template>

<script>
import { getContextByProjectId, updateContexByProjectId, saveRecord, updatePerformanceById } from '@/api/request';
import { mapState } from 'vuex';
import temporalInfoTable from '_com/ContextTable/TemporalInfoTable';
import temporalInfoDialog from '_com/ContextTable/TemporalInfoDialog';
import spatialInfoTable from '_com/ContextTable/SpatialInfoTable';
import spatialInfoDialog from '_com/ContextTable/SpatialInfoDialog';
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
    temporalInfoTable,
    spatialInfoTable,
    temporalInfoDialog,
    spatialInfoDialog

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
        spatialInfo: {},
        temporalInfo: {},
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
        spatialExtentList: [],
        resolutionConstraintList: []
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
      if (data.spatialInfo != null) {
        this.spatialInfoForm = data.spatialInfo;
      }
      if (data.temporalInfo != null) {
        this.temporalInfoForm = data.temporalInfo;
        // this.temporalInfoForm.temporalExtentList = data.temporalInfo.temporalExtentList[0];
      }
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
        // this.contextForm.themes = [];
        this.contextForm.themes.push(tag);
      }
      this.tagInputVisible = false;
      this.themeTag = '';
    },
    delTags(tagIndex) {
      this.contextForm.themes.splice(tagIndex, 1);
    },

    //设置表格行的样式
    // eslint-disable-next-line no-unused-vars
    tableRowStyle({ row, rowIndex }) {
      return 'background-color:pink;font-size:15px;';
    },

    //get spatial info back
    getSpatialInfoReturn(value) {
      console.log('spatialInfo', value);
      this.spatialInfoForm = value;
      this.addSpatialInfodialogVisible = false;
    },
    getTemporalInfoReturn(value) {
      this.temporalInfoForm = value;
      this.addTemporalInfodialogVisible = false;
    }
  },
  created() {
    this.getContext();
  }
};
</script>
<style lang="scss" scoped>
.main {
  margin: 0 10px;
  height: 100%;
  // .spatioEdit {
  //   position: absolute;
  //   left: -60px;
  //   top: 30px;
  // }
  // .temporalEdit {
  //   position: absolute;
  //   left: -60px;
  //   top: 30px;
  // }

  .temporalDialog {
  }
}
</style>
