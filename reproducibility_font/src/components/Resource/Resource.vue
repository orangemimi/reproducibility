<template>
  <div class="main">
    <div class="btnList" v-if="role == 'builder'">
      <div class="btn">
        <!-- <el-button size="mini" @click="uploadFileDialogShow = true">
          <i class="el-icon-upload"></i>
          Upload File
        </el-button> -->
        <div class="btn"><el-button size="mini" @click="addData">Add Item</el-button></div>
      </div>
    </div>
    <!-- {{ dataItemList }} -->
    <div class="row-style">
      <el-table
        ref="multipleTable"
        :data="dataItemList"
        tooltip-effect="dark"
        style="width: 100%"
        max-height="350"
        :row-style="{ height: '0' }"
        :cell-style="{ padding: '4px' }"
        border
        :row-key="getRowKeys"
        :expand-row-keys="expands"
        @current-change="handleCurrentChange"
      >
        <template slot="empty">
          Please upload a data
        </template>
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="Name">
                <span>{{ scope.row.name }}</span>
              </el-form-item>
              <el-form-item label="Description">
                <span>{{ scope.row.description }}</span>
              </el-form-item>
              <el-form-item label="keywords">
                <span>{{ scope.row.keywords }}</span>
              </el-form-item>
              <el-form-item label="Format">
                <span>{{ scope.row.format }}</span>
              </el-form-item>
              <div v-if="scope.row.format != 'parameter'">
                <el-form-item label="Spatial Info">
                  <span>{{ scope.row.restriction.spatialInfo }}</span>
                </el-form-item>
                <el-form-item label="Temporal Info">
                  <span>{{ scope.row.restriction.temporalInfo }}</span>
                </el-form-item>
              </div>
              <div v-else>
                <el-form-item label="Spatial Info">
                  <span>{{ scope.row.restriction.type }}</span>
                </el-form-item>
                <el-form-item label="Temporal Info">
                  <span>{{ scope.row.restriction.unit }}</span>
                </el-form-item>
              </div>
            </el-form>
          </template>
        </el-table-column>

        <el-table-column label="Name" width="160">
          <template #default="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="Format" width="100">
          <template #default="scope">
            <span>{{ scope.row.format }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Upload time" width="160">
          <template #default="scope">{{ scope.row.createTime }}</template>
        </el-table-column>
        <el-table-column label="Value" fixed="right">
          <template #default="scope">
            <div v-if="scope.row.value != null">
              <el-button size="mini">Download</el-button>
            </div>
            <div v-else>
              <el-button size="mini" @click="bindResource(scope.row)">Bind</el-button>
              <el-button size="mini" @click="editDataItem">Edit</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- upload data -->
    <el-dialog title="Upload resource" :visible.sync="uploadDataDialogShow" width="40%" :close-on-click-modal="false" :append-to-body="true">
      <data-upload-info @uploadSuccess="uploadSuccess"></data-upload-info>
    </el-dialog>

    <el-dialog title="Upload resource" :visible.sync="editDataDialogShow" width="40%" :close-on-click-modal="false" :append-to-body="true">
      <data-upload-info @uploadSuccess="uploadSuccess" :initFormData="currentRow"></data-upload-info>
    </el-dialog>

    <el-dialog title="Bind value" :visible.sync="bindResourceDialogShow" width="40%" :close-on-click-modal="false" :append-to-body="true">
      <bind-resource @returnResourceUrl="returnResourceUrl" :resourceType="currentRow.format"></bind-resource>
    </el-dialog>

    <el-dialog title="Upload file" :visible.sync="uploadFileDialogShow" width="50%" :close-on-click-modal="false" :append-to-body="true">
      <file-upload></file-upload>
    </el-dialog>
  </div>
</template>

<script>
import { getDataItemsByProjectId, updateDataItemById, saveFileItem, postDataContainer } from '@/api/request';
// import dataUpload from './DataUpload'; //dialogcontent
import dataUploadInfo from './DataUploadInfo'; //dialogcontent
import bindResource from './BindResource';
import fileUpload from './FileUpload';
import { getUuid, getSuffix, renderSize, getTime } from '@/utils/utils';
import { mapState } from 'vuex';

export default {
  components: {
    dataUploadInfo,
    bindResource,
    fileUpload
  },

  data() {
    return {
      uploadDataDialogShow: false, //upload data dialog
      editDataDialogShow: false,
      dataItemList: [],
      dataItemListFromResource: [],
      projectId: this.$route.params.id,
      modelItemList: [],
      checkAll: false,
      checkedDataItemList: [],
      isIndeterminate: false,

      //table
      multipleSelection: [],

      //add folder
      isAddFolder: false,
      folderName: '',
      currentRow: {},
      // dataItemListDirect: [],
      drawer: false,
      // 要展开的行，数值的元素是row的key值
      expands: [],
      bindResourceDialogShow: false,
      uploadFileDialogShow: false,
      selectItem: {}
    };
  },
  computed: {
    ...mapState({
      role: state => state.permission.role
    })
  },

  methods: {
    //add data dialog show
    addData() {
      this.uploadDataDialogShow = true;
    },
    //close the dialog
    uploadSuccess(val) {
      if (val) {
        this.uploadDataDialogShow = false;
      }
    },

    downloadDataResource(data) {
      window.open(data.address);
    },

    //get all the data
    async getDataCollection() {
      let data = await getDataItemsByProjectId(this.projectId);
      // console.log('dataITem', data);
      // let data = await get(`/dataItems`);
      this.dataItemList = data;
      // this.dataItemListDirect = this.getDataItemListDirect();

      // await this.getSelectedData();
    },

    // getDataItemListDirect() {
    //   if (this.role == 'builder') {
    //     if (this.dataItemList.length != 0 || this.dataItemList != null || this.dataItemList != undefined) {
    //       return this.dataItemList.filter(item => item.userUpload == true);
    //     } else {
    //       return [];
    //     }
    //   }
    //   if (this.role == 'rebuilder_operator') {
    //     return this.dataItemListFromResource;
    //   }
    //   return [];
    // },

    //init table selection
    toggleSelection(rows) {
      if (rows) {
        this.$nextTick(() => {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row);
          });
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
      this.$forceUpdate();
    },

    //selection change
    handleSelectionChange(val) {
      // this.multipleSelection = val;
      console.log(val);
    },

    selectRow(selection, row) {
      let val = row;
      // this.multipleSelection.push(row);
      if (selection.some(el => el == row)) {
        if (val.folder && val.children.length != 0 && val.children != null) {
          val.children.forEach(child => {
            this.multipleSelection.push(child);
          });
        }
      } else {
        if (val.folder && val.children.length != 0 && val.children != null) {
          val.children.forEach(child => {
            this.multipleSelection.splice(item => item.id == child.id);
          });
        }
      }

      console.log('selection', this.multipleSelection);
      this.toggleSelection(this.multipleSelection);
    },

    handleCurrentChange(row) {
      this.currentRow = row;
    },
    cancleCurrentRow() {
      this.currentRow = '';
    },

    async getDataAsOperator() {
      let data = await getDataItemsByProjectId(this.projectId);
      this.dataItemListFromResource = data;
      this.$refs.multipleTable.toggleAllSelection();
      // console.log('DATA', data);
    },

    //submit
    // async submitBtn() {
    //   if (this.multipleSelection.length == 0) {
    //     this.$notify({
    //       title: 'Warning',
    //       message: 'You have not select any data!',
    //       type: 'warning'
    //     });
    //     return;
    //   }

    //   //用id重组一个新数组
    //   let filter = [];
    //   this.multipleSelection.forEach(ele => {
    //     filter.push(ele.id);
    //   });
    //   await updateDataItemById(this.projectId, {
    //     dataItemCollection: filter
    //   });

    //   let content = { content: 'Resource Collection', degree: '100%', type: 'success', icon: 'el-icon-folder' };

    //   await updatePerformanceById('resource', this.projectId, content);
    // },

    addFolderShow() {
      this.folderName = '';
      this.isAddFolder = true;
    },
    closeAddFolder() {
      this.isAddFolder = false;
    },

    // async uploadFolder() {
    //   let form = {
    //     alia: '',
    //     name: this.folderName,
    //     folder: true,
    //     description: '',
    //     privacy: 'discoverable',
    //     parent: '',
    //     children: [],
    //     userUpload: true
    //   };
    //   await this.saveProjectResource(form);
    // },

    //上传文件到服务器
    async submitUpload(param) {
      let uploadDataForm = new FormData();
      uploadDataForm.append('data', param.data);
      let data = await postDataContainer(uploadDataForm);

      let form = {
        name: data.data_name,
        suffix: getSuffix(param.data.name),
        dataSize: renderSize(param.data.size),
        address: `http://221.226.60.2:8082/data/${data.id}`,
        projectId: this.projectId,
        userUpload: true,
        children: [],
        parent: '',
        folder: false
      };

      await this.saveProjectResource(form);
    },

    async saveProjectResource(form) {
      if (this.currentRow == '') {
        let data = await saveFileItem(form);
        this.dataItemList.push(data);
      } else {
        form.parent = this.currentRow.id;
        form.id = getUuid();
        form.createTime = getTime();
        let parentData = this.currentRow;
        if (parentData.children == null) {
          parentData.children = [];
        }
        parentData.children.push(form);
        await updateDataItemById(parentData.id, parentData);
      }
    },

    collapseClass(params) {
      return params.folder === true ? 'el-icon-folder' : 'el-icon-document';
    },
    getRowKeys(row) {
      console.log('row', row);
      return row.id;
    },
    clickTable(row) {
      this.$refs.multipleTable.toggleRowExpansion(row);
    },
    editDataItem() {
      this.editDataDialogShow = true;
    },
    bindResource(selectItem) {
      this.bindResourceDialogShow = true;
      this.selectItem = this.dataItemList.find(item => item.id == selectItem.id);
      // console.log(item);
    },

    async returnResourceUrl(value) {
      this.selectItem.value = value;
      // let json ={value:value}
      let data = await updateDataItemById(this.selectItem.id, this.selectItem);
      console.log('updateData', data);
    }
  },
  async mounted() {
    if (this.role == 'builder') {
      await this.getDataCollection();
    }
    if (this.role == 'rebuilder_operator') {
      await this.getDataAsOperator();
    }
  }
};
</script>

<style lang="scss" scoped>
.main {
  padding: 0 10px;
  height: 100%;
  width: 100%;

  .row-style {
    padding: 0 10px;
    height: 100%;
    width: 100%;
    // position: relative;
  }

  .btnList {
    width: 100%;
    // padding: 0 0 0 35%;
    float: right;

    .btn {
      float: right;
      margin-right: 10px;
    }
  }
  /* 用来设置当前页面element全局table 选中某行时的背景色*/
  .el-table__body tr.current-row > td {
    background-color: #69a8ea !important;
    color: #fff;
  }

  .drawer {
    /deep/.el-drawer__body {
      height: 0;
      // height:0 overflow;flex:1
    }
  }
}
</style>
