<!-- data upload information -->
<template>
  <div class="main">
    <!-- {{ formType }} -->
    <el-form class="resource" ref="form" :model="form" label-width="100px" size="mini" :rules="Rules">
      <el-form-item label="Name" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>

      <el-form-item label="Description">
        <el-input v-model="form.description"></el-input>
      </el-form-item>

      <el-form-item label="Data" prop="value">
        <div v-if="formType == 'file'">
          <!-- <div v-if="currentFile.name != null || currentFile.name != undefined" class="select_data">
            <div class="select-data select-data-line">
              <div class="data-name">{{ currentFile.name }}</div>
              <i class="el-icon-close" @click="remove"></i>
            </div>
          </div>
          <div class="drag" v-else>
            <el-upload drag action :auto-upload="true" :show-file-list="false" ref="upload" :http-request="submitUpload" :on-success="handleSuccess">
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">
                Drag a file here to upload or
                <em>Click to upload</em>
              </div>
            </el-upload>
          </div> -->
          <file-upload @returnFileUrl="returnFileUrl"></file-upload>
        </div>

        <div v-else-if="formType == 'parameter'">
          <el-radio-group v-model="form.type">
            <el-radio label="Input"></el-radio>
            <el-radio label="Date"></el-radio>
          </el-radio-group>

          <div v-if="form.type == 'Input'"><el-input v-model="form.value"></el-input></div>
          <div v-else><el-date-picker v-model="form.value" type="date" placeholder="Select the date"></el-date-picker></div>
        </div>

        <div v-else>
          <el-input v-model="form.value"></el-input>
        </div>
      </el-form-item>

      <el-form-item label="Token" v-show="formType == 'shared_file'" :prop="formType == 'shared_file' ? 'token' : ''">
        <el-input v-model="form.token"></el-input>
      </el-form-item>

      <!-- <el-form-item label="Keywords">
        <el-input v-model="form.keywords[0]"></el-input>
      </el-form-item> -->

      <!-- <el-form-item label="Spatial Info" v-show="formType != 'parameter'">
        <el-button @click="addSpatialInfodialogVisible = true" size="mini" class="spatioEdit">Edit</el-button>
        <spatial-info-table :spatialInfoForm="spatialInfoForm"></spatial-info-table>
      </el-form-item>
      <el-form-item label="Temporal Info" v-show="formType != 'parameter'">
        <el-button @click="addTemporalInfodialogVisible = true" size="mini" class="temporalEdit">Edit</el-button>
        <temporal-info-table :temporalInfoForm="temporalInfoForm"></temporal-info-table>
      </el-form-item> -->

      <!-- <el-form ref="formRestriction" :model="form" label-width="100px" size="mini"> -->

      <el-form-item label="Type" v-show="formType == 'parameter'" prop="type">
        <el-radio-group v-model="form.restriction.type" v-for="(item, index) in typeEnums" :key="index">
          <el-radio :label="item"></el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item label="Decimal" v-show="formType == 'parameter'">
        <el-input v-model="form.restriction.decimal"></el-input>
      </el-form-item> -->
      <!-- <el-form-item v-model="form.restriction.content"></el-form-item> -->
      <!-- <el-form-item label="Unit" v-show="formType == 'parameter'">
        {{ form.restriction.unit.value }}

        <el-button size="mini" @click="drawer = true">Add Unit</el-button>
      </el-form-item> -->

      <!-- </el-form> -->

      <el-form-item label="State">
        <el-radio-group v-model="form.state">
          <el-radio label="Public">Public</el-radio>
          <el-radio label="Private">Private</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="Version" prop="version">
        <el-input v-model="form.version"></el-input>
      </el-form-item>
      <el-form-item label="Reference">
        <el-input v-model="form.agentAttribute.reference"></el-input>
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

    <unit-drawer :drawer="drawer" @selectUnit="selectUnit" @closeDrawer="closeDrawer"></unit-drawer>

    <el-dialog :visible.sync="addSpatialInfodialogVisible" width="40%" title="Add Spatial Info" class="spatialDialog" :append-to-body="true">
      <spatial-info-dialog :spatialInfo="spatialInfoForm"></spatial-info-dialog>

      <!-- <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="handleSpatialInfoClose" size="mini">Confirm</el-button>
        </span>
      </template> -->
    </el-dialog>

    <el-dialog :visible.sync="addTemporalInfodialogVisible" width="40%" class="temporalDialog" title="Add Temporal Info" :append-to-body="true">
      <temporal-info-dialog :temporalInfo="temporalInfoForm"></temporal-info-dialog>
    </el-dialog>
  </div>
</template>

<script>
// import { post } from '@/axios';
import { saveDataItem, postDataContainer } from '@/api/request';
// import addImage from '_com/AddImage';
import { VueTreeList, Tree, TreeNode } from '_com/TreeDescription';
import { getSuffix, renderSize } from '@/utils/utils';

// import temporalInfoTable from '_com/ContextTable/TemporalInfoTable';
import temporalInfoDialog from '_com/ContextTable/TemporalInfoDialog';
// import spatialInfoTable from '_com/ContextTable/SpatialInfoTable';
import spatialInfoDialog from '_com/ContextTable/SpatialInfoDialog';
import unitDrawer from '_com/UnitDrawer/UnitDrawer';
import fileUpload from './FileUpload';

export default {
  props: {
    formType: {
      type: String
    },
    initFormData: {
      type: Object
    }
  },

  watch: {
    initFormData: {
      handler(val) {
        if (val != '') {
          this.form = val;
        }
      },
      deep: true
    }
  },
  components: {
    // addImage,
    VueTreeList,
    // temporalInfoTable,
    // spatialInfoTable,
    temporalInfoDialog,
    spatialInfoDialog,
    unitDrawer,
    fileUpload
    // addChildValue
  },

  data() {
    return {
      projectId: this.$route.params.id,
      value1: '',
      form: {
        name: '',
        type: 'Input',
        value: '',
        token: '',
        description: '',
        keywords: [],
        agentAttribute: {
          organization: {},
          reference: ''
        },
        activityAttribute: {
          relatedActivity: {
            name: '',
            description: ''
          },
          relatedResource: {
            name: '',
            description: ''
          },
          relationshipLink: {
            description: ''
          }
        },
        state: 'Public',
        version: '1.0',
        format: this.formType,
        restriction: {
          type: 'String',
          decimal: '',
          content: '',
          unit: { value: '' },
          spatialInfo: {},
          temporalInfo: {}
        }
      },

      Rules: {
        name: [{ required: true, message: 'Name cannot be empty', trigg: 'blur' }],
        value: [{ required: true, message: 'Data cannot be empty' }],
        version: [{ required: true, message: 'Version cannot be empty', trigg: 'blur' }],
        token: [{ required: true, message: 'Token cannot be empty', trigg: 'blur' }]
      },

      fileForm: {
        alia: '',
        name: '',
        type: '',
        description: '',
        privacy: 'discoverable',
        folder: false,
        source: '',
        thumbnail: '',
        userUpload: '',
        address: ''
      },

      typeEnums: ['String', 'Number', 'Date'],

      uploadFileForm: new FormData(), //上传文件的form
      fileList: [], //el-upload上传的文件列表,
      dataItemList: [],
      //   file: {},
      ops: {
        bar: {
          background: '#808695'
        }
      },
      nodeInfo: this.nodeInformation,

      //tree
      treeData: new Tree([
        {
          name: 'Name',
          value: 'Description',
          id: 1,
          pid: 0
        }
      ]),
      // addChildDialogShow: false

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

      //unit drawer
      showUnitDrawer: false,
      drawer: false,
      addSpatialInfodialogVisible: false,
      addTemporalInfodialogVisible: false,

      //file uolad related
      currentFile: {}
    };
  },

  methods: {
    uploadImgResponse(val) {
      this.form.picture = val;
    },

    remove() {
      // await del(`/fileItems/${resource.id}`);
      // this.form = {
      //   alia: '',
      //   name: '',
      //   type: '',
      //   description: '',
      //   privacy: 'discoverable',
      //   source: '',
      //   thumbnail: '',
      //   userUpload: '',
      //   address: '',
      // };
      this.currentFile = {};
    },

    //上传文件到服务器
    async submitUpload(param) {
      console.log(param.file);

      this.uploadFileForm = new FormData();
      this.uploadFileForm.append('file', param.file);
      // console.log(this.uploadFileForm.get('file'));
      let data = await postDataContainer(this.uploadFileForm);

      this.fileForm.name = data.file_name;
      this.fileForm.suffix = getSuffix(param.file.name);
      this.fileForm.fileSize = renderSize(param.file.size);
      this.fileForm.folder = false;
      this.fileForm.userUpload = true;

      this.fileForm.address = `http://221.226.60.2:8082/data/${data.id}`;

      this.$forceUpdate();
    },

    handleSuccess(response, file, fileList) {
      console.log(response, file, fileList);
      if (file.status == 'success') {
        this.currentFile = this.fileForm;
      }
    },

    //data item保存到数据库
    //上传数据直接保存到fileItems,即用户的资源可全部显示，之后选择所需的数据，之后保存选择的数据之后 保存到resource数据表里面去
    submit() {
      this.$refs['form'].validate(async valid => {
        if (!valid) {
          this.$notify.error({
            title: 'Error',
            message: 'Validation failed'
          });
          return;
        }

        // console.log('getFile', this.uploadFileForm.get('file'));
        // if (this.uploadFileForm.get('file') != null) {
        //   this.fileForm.description = this.form.description;
        //   await saveFileItem(this.fileForm);
        // }
        this.form.restriction.spatialInfo = this.spatialInfoForm;
        this.form.restriction.temporalInfo = this.temporalInfoForm;
        this.form.projectId = this.projectId;
        console.log(this.form);
        let data = await saveDataItem(this.form);
        console.log(data);
        let result = {
          data: data,
          flag: true
        };
        this.$emit('uploadSuccess', result);
      });
      // if (formType == 'file') {

      // } else if (formType == 'parameter') {
      //   let jsonObj = {
      //     Dataset: {
      //       XDO: {
      //         _name: this.form.name,
      //         _kernelType: this.form.restriction.type,
      //         _value: this.form.value
      //       }
      //     },
      //   };
      //   // let file = new File(this.$x2js.js2xml(jsonObj), 'test.xml', {type: 'textain'})
      //   console.log(this.$x2js.js2xml(jsonObj));
      // }
    },

    returnFileUrl(val) {
      console.log(val);
      this.form.value = val.address;
    },

    //tree

    addNode() {
      var node = new TreeNode({ name: 'new node', isLeaf: false });
      if (!this.data.children) this.data.children = [];
      this.data.addChildren(node);
    },
    onDel(node) {
      node.remove();
    },
    onClick(params) {
      // eslint-disable-next-line no-console
      console.log(params);
    },

    closeDrawer() {
      this.drawer = false;
    },
    selectUnit(val) {
      console.log(val);
      this.form.restriction.unit = val.name;
    }
  },

  mounted() {
    // console.log(this.formType)
    // console.log(this.initFormData)
  }
};
</script>
<style lang="scss" scoped>
.main {
  height: 100%;
  width: 100%;

  // position: relative;
  .file {
    .drag {
      margin-bottom: 20px;
      /deep/ .el-icon-upload {
        margin: 0;
      }

      /deep/ .el-upload-dragger {
        width: calc(50vw);
        height: 100px;
      }
    }
  }

  .select_data {
    .select-data-line {
      margin: 15px 0 0 0;
      line-height: 30px;
      .el-button {
        width: 138px;
      }
    }
    .select-data {
      background-color: #f0f9eb;
      display: inline-block;
      height: 32px;
      padding: 0 10px;

      font-size: 12px;
      color: #67c23a;
      border: 1px solid #e1f3d8;
      border-radius: 4px;
      box-sizing: border-box;
      white-space: nowrap;

      .data-name {
        float: left;
        width: 200px;
        font-weight: 600;
        font-size: 16px;
        // padding-left: 5px;
      }
    }

    .select-data:hover {
      // width: 250px;
      cursor: pointer;
      transition: all 0.2s ease-out;
      color: #60a83c;
      background-color: #c8f3d3;
    }
  }

  .resource {
    .source {
      /deep/ .el-form-item__content {
        line-height: 20px;
      }
      .tree {
        border: 1px solid #eee;
        .content {
          // line-height: 40px;
          // height: 20px;
          .name {
            width: 100px;
            float: left;
          }
          .value {
            // width: 100px;
            float: left;
          }
        }
      }
    }
  }
  .submit {
    margin-left: 88.5%;
  }
}
</style>
