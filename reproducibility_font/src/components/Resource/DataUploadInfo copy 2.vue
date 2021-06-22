<!-- data upload information -->
<template>
  <div class="main">
    <el-form class="resource" ref="form" :model="form" label-width="100px" size="mini">
      <el-form-item label="Name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>

      <el-form-item label="Description">
        <el-input v-model="form.description"></el-input>
      </el-form-item>

      <el-form-item label="Keywords">
        <el-input v-model="form.keywords"></el-input>
      </el-form-item>

      <!-- <el-form-item label="Source" class="source">
        <el-button icon="el-icon-circle-plus-outline" @click="addNode"></el-button>

        <div class="tree">
          <vue-tree-list @click="onClick" @delete-node="onDel" :model="data" default-tree-node-name="new node" default-leaf-node-name="new leaf" v-bind:default-expanded="false">
            <template v-slot:leafNameDisplay="slotProps" class="content">
              <div class="name">
                {{ slotProps.model.name }}
              </div>
              <div class="value">{{ slotProps.model.value }}</div>
            </template>
          </vue-tree-list>
        </div>
      </el-form-item> -->

      <el-form-item label="State">
        <el-radio-group v-model="form.state">
          <el-radio label="Public">Public</el-radio>
          <el-radio label="Private">Private</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- <el-form-item label="Restriction">
       
      </el-form-item> -->

      <el-form-item class="file" label="Data">
        <el-tabs v-model="activeName" type="border-card" size="mini">
          <el-tab-pane label="Upload File" name="file" style="height:300px">
            <div class="drag" v-if="form.format == 'file'">
              <el-upload drag action :auto-upload="true" :show-file-list="false" ref="upload" :http-request="submitUpload" :on-remove="handleRemove">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">
                  Drag a file here to upload or
                  <em>Click to upload</em>
                </div>
              </el-upload>
            </div>
            <div v-else class="select_data">
              <div class="select-data select-data-line">
                <div class="data-name">{{ form.name }}</div>
                <i class="el-icon-close" @click="remove"></i>
              </div>
            </div>
            <div>
              <spatial-info-table :spatialInfoForm="spatialInfoForm"></spatial-info-table>
            </div>
            <div>
              <temporal-info-table :temporalInfoForm="temporalInfoForm"></temporal-info-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="Parameter" name="parameter" style="height:200px">
            <el-radio-group v-model="form.type">
              <el-radio label="Input"></el-radio>
              <el-radio label="Date"></el-radio>
            </el-radio-group>

            <div v-if="form.type == 'Input'"><el-input v-model="form.value"></el-input></div>
            <div v-else><el-date-picker v-model="value1" type="date" placeholder="Select the date"></el-date-picker></div>
            <el-form ref="formRestriction" :model="form" label-width="100px" size="mini">
              <el-form-item label="Type">
                <el-radio-group v-model="form.restriction.type" v-for="(item, index) in typeEnums" :key="index">
                  <el-radio :label="item"></el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="Decimal">
                <el-input v-model="form.restriction.decimal"></el-input>
              </el-form-item>
              <!-- <el-form-item v-model="form.restriction.content"></el-form-item> -->
              <el-form-item label="Unit">
                {{ form.restriction.unit }}
                <el-button size="mini" @click="drawer = true">Add Unit</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="Shared File" name="sharedFile" style="height:140px">
            <el-input v-model="form.value"></el-input>

            <div>
              <spatial-info-table :spatialInfoForm="spatialInfoForm"></spatial-info-table>
            </div>
            <div>
              <temporal-info-table :temporalInfoForm="temporalInfoForm"></temporal-info-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-form-item>

      <el-form-item label="Version">
        <el-input v-model="form.version"></el-input>
      </el-form-item>
      <el-form-item label="Reference">
        <el-input v-model="form.agentAttribute.reference"></el-input>
      </el-form-item>

      <el-form-item label="Organization">
        <!-- <el-input v-model="form.agentAttribute.organization">
          <el-button slot="append" icon="el-icon-search" @click="addDescription">Add description</el-button>
        </el-input> -->

        <div class="tree">
          <vue-tree-list @click="onClick" @delete-node="onDel" :model="treeData" default-tree-node-name="new node" default-leaf-node-name="new leaf" v-bind:default-expanded="false">
            <template v-slot:leafNameDisplay="slotProps" class="content">
              <div class="name">
                {{ slotProps.model.name }}
              </div>
              <div class="value">{{ slotProps.model.value }}</div>
            </template>
          </vue-tree-list>
        </div>
      </el-form-item>
    </el-form>

    <div class="submit">
      <el-button @click="submit">Submit</el-button>
    </div>

    <unit-drawer :drawer="drawer" @selectUnit="selectUnit" @closeDrawer="closeDrawer"></unit-drawer>
  </div>
</template>

<script>
import { post } from '@/axios';
import { saveFileItem } from '@/api/request';
// import addImage from '_com/AddImage';
import { VueTreeList, Tree, TreeNode } from '_com/TreeDescription';
import temporalInfoTable from '_com/ContextTable/TemporalInfoTable';
import spatialInfoTable from '_com/ContextTable/SpatialInfoTable';
import unitDrawer from '_com/UnitDrawer/UnitDrawer';

export default {
  components: {
    // addImage,
    VueTreeList,
    temporalInfoTable,
    spatialInfoTable,
    unitDrawer
    // addChildValue
  },

  data() {
    return {
      projectId: this.$route.params.id,
      form: {
        alia: '',
        name: '',
        type: 'Input',
        description: '',
        privacy: 'discoverable',
        folder: false,
        agentAttribute: {
          organization: '',
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
        thumbnail: '',
        userUpload: '',
        address: '',
        state: 'Public',
        version: '1.0',
        format: 'file',
        restriction: {
          type: '',
          decimal: '',
          content: '',
          unit: ''
        }
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
      activeName: 'file',
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
      drawer: false
    };
  },

  methods: {
    uploadImgResponse(val) {
      this.form.picture = val;
    },

    handleRemove(file) {
      this.dataItemList = this.dataItemList.filter(item => {
        return item.name != file.name;
      });
    },

    async remove() {
      // await del(`/fileItems/${resource.id}`);
      this.form = {
        alia: '',
        name: '',
        type: '',
        description: '',
        privacy: 'discoverable',
        source: '',
        thumbnail: '',
        userUpload: '',
        address: ''
      };
    },

    //上传文件到服务器
    async submitUpload(param) {
      this.uploadFileForm = new FormData();
      this.uploadFileForm.append('file', param.file);
      // console.log(this.uploadFileForm.get('file'));

      let { data } = await post(`/dataContainer/uploadSingle`, this.uploadFileForm);

      this.form.name = data.file_name;
      this.form.suffix = this.getSuffix(param.file.name);
      this.form.fileSize = this.renderSize(param.file.size);

      this.form.address = `http://221.226.60.2:8082/data/${data.id}`;
      this.form.projectId = this.projectId;
      this.$forceUpdate();
    },

    getSuffix(filename) {
      let index = filename.lastIndexOf('.');
      return filename.substr(index + 1);
    },

    renderSize(value) {
      if (null == value || value == '') {
        return '0 Bytes';
      }
      var unitArr = new Array('Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB');
      var index = 0,
        srcsize = parseFloat(value);
      index = Math.floor(Math.log(srcsize) / Math.log(1024));
      var size = srcsize / Math.pow(1024, index);
      //  保留的小数位数
      size = size.toFixed(2);
      return size + unitArr[index];
    },

    //data item保存到数据库
    //上传数据直接保存到fileItems,即用户的资源可全部显示，之后选择所需的数据，之后保存选择的数据之后 保存到resource数据表里面去
    async submit() {
      this.form.userUpload = true;
      this.form.organizaiton = this.treeData;
      await saveFileItem(this.form);
      // let data = await post(`/fileItems`, this.form);
      // console.log(data);
      this.$notify({
        title: 'Success',
        message: 'You have upload the file successfully!',
        type: 'success'
      });

      this.$emit('uploadSuccess', true);
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
      this.form.restriction.unit = val.name;
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  height: 100%;
  width: 100%;

  // position: relative;
  .file {
    /deep/.el-tabs__item {
      padding: 0 10px;
      height: 20px;
      line-height: 20px;
      font-size: 12px;
    }
    .drag {
      margin-bottom: 20px;
      /deep/ .el-icon-upload {
        margin: 0;
      }

      /deep/ .el-upload-dragger {
        width: calc(30vw);
        height: 120px;
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
