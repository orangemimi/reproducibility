<template>
  <div>
    <el-form label-position="right" ref="form" label-width="90px" :model="dataForm" :rules="Rules" :disabled="flag">
      <el-form-item label="Name" prop="name">
        <el-input v-model="dataForm.name"></el-input>
      </el-form-item>
      <el-form-item label="Description">
        <el-input v-model="dataForm.description"></el-input>
      </el-form-item>
      <el-form-item label="State">
        <el-radio-group v-model="dataForm.state">
          <el-radio label="Public">Public</el-radio>
          <el-radio label="Private">Private</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="Data" :prop="dataForm.format == 'file' ? '' : 'value'" v-show="dataForm.format != 'file'">
        <div v-if="dataForm.format == 'parameter'">
          <el-radio-group v-model="type">
            <el-radio label="Input"></el-radio>
            <el-radio label="Date"></el-radio>
          </el-radio-group>
          <div v-if="type == 'Input'"><el-input v-model="dataForm.value"></el-input></div>
          <div v-else><el-date-picker v-model="dataForm.value" type="date" placeholder="Select the date"></el-date-picker></div>
        </div>
        <div v-else-if="dataForm.format != 'file'">
          <el-input v-model="dataForm.value"></el-input>
        </div>
      </el-form-item>

      <el-form-item label="SpatialInfo" v-show="dataForm.format != 'parameter'">
        <el-button @click="addSpatialInfodialogVisible = true" size="mini" class="spatioEdit">Edit</el-button>
        <spatial-info-table :spatialInfoForm="spatialInfoForm"></spatial-info-table>
      </el-form-item>
      <el-form-item label="TemporalInfo" v-show="dataForm.format != 'parameter'">
        <el-button @click="addTemporalInfodialogVisible = true" size="mini" class="temporalEdit">Edit</el-button>
        <temporal-info-table :temporalInfoForm="temporalInfoForm"></temporal-info-table>
      </el-form-item>

      <el-form-item label="Token" v-show="dataForm.format == 'shared_file'" :prop="dataForm.format == 'shared_file' ? 'token' : ''">
        <el-input v-model="dataForm.token"></el-input>
      </el-form-item>

      <el-form-item label="Version" prop="version">
        <el-input v-model="dataForm.version"></el-input>
      </el-form-item>
      <el-form-item label="Reference">
        <el-input v-model="dataForm.agentAttribute.reference"></el-input>
      </el-form-item>
      <el-form-item label="Organization">
        <div class="tree">
          <vue-tree-list
            @click="onClick"
            @delete-node="onDel"
            :model="treeData"
            default-tree-node-name="new node"
            default-leaf-node-name="new leaf"
            v-bind:default-expanded="false"
          >
            <template v-slot:leafNameDisplay="slotProps" class="content">
              <div class="name">
                {{ slotProps.model.name }}
              </div>
              <div class="value">{{ slotProps.model.value }}</div>
            </template>
          </vue-tree-list>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button @click="submit">Submit</el-button>
      </el-form-item>
    </el-form>

    <el-button @click="flag = false">Edit</el-button>

    <el-dialog :visible.sync="addSpatialInfodialogVisible" width="40%" title="Add Spatial Info" class="spatialDialog" :append-to-body="true">
      <spatial-info-dialog :spatialInfo="spatialInfoForm"></spatial-info-dialog>
    </el-dialog>

    <el-dialog :visible.sync="addTemporalInfodialogVisible" width="40%" class="temporalDialog" title="Add Temporal Info" :append-to-body="true">
      <temporal-info-dialog :temporalInfo="temporalInfoForm"></temporal-info-dialog>
    </el-dialog>
  </div>
</template>

<script>
import { Tree, VueTreeList } from '_com/TreeDescription';
import temporalInfoTable from '_com/ContextTable/TemporalInfoTable';
import spatialInfoTable from '_com/ContextTable/SpatialInfoTable';
import temporalInfoDialog from '_com/ContextTable/TemporalInfoDialog';
import spatialInfoDialog from '_com/ContextTable/SpatialInfoDialog';

export default {
  props: {
    initFormData: {
      type: Object,
    },
  },
  components: {
    VueTreeList,
    temporalInfoTable,
    spatialInfoTable,
    temporalInfoDialog,
    spatialInfoDialog,
  },
  data() {
    return {
      type: '',
      flag: true,
      addSpatialInfodialogVisible: false,
      addTemporalInfodialogVisible: false,
      dataForm: {
        agentAttribute: {},
      },
      treeData: new Tree([
        {
          name: 'Name',
          value: 'Description',
          id: 1,
          pid: 0,
        },
      ]),
      Rules: {
        name: [{ required: true, message: 'Name cannot be empty', trigg: 'blur' }],
        value: [{ required: true, message: 'Data cannot be empty' }],
        version: [{ required: true, message: 'Version cannot be empty', trigg: 'blur' }],
        token: [{ required: true, message: 'Token cannot be empty', trigg: 'blur' }],
      },
      spatialInfoForm: {
        enable: false,
        spatialReference: {
          general: '',
          wkt: '',
        },
        spatialDimension: '',
        spatialScale: {
          type: '',
          description: '',
        },
        spatialExtentList: [],
        resolutionConstraintList: [],
      },
      temporalInfoForm: {
        enable: false,
        temporalScale: {
          type: '',
          description: '',
        },
        temporalReference: {
          value: '',
        },
        temporalExtentList: [],
        stepConstraintList: [],
      },
    };
  },
  methods: {
    onClick(params) {
      // eslint-disable-next-line no-console
      console.log(params);
    },
    onDel(node) {
      node.remove();
    },
    remove() {
      this.currentFile = {};
    },
    submit() {
      this.$refs['form'].validate(async (valid) => {
        if (!valid) {
          this.$notify.error({
            title: 'Error',
            message: 'Validation failed',
          });
          return;
        }
        console.log(111);
      });
    },
  },
  mounted() {
    this.dataForm = this.initFormData;
    let value = this.dataForm.value;
    if (this.dataForm.format == 'parameter') {
      if (value[4] == '-' && value[7] == '-' && value[10] == 'T' && value[13] == ':' && value[16] == ':' && value[19] == '.') {
        this.type = 'Date';
      } else {
        this.type = 'Input';
      }
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
