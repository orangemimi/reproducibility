<template>
  <div class="main">
    <div class="btnList" v-if="role == 'builder'">
      <div v-if="!isAddFolder">
        <div class="btn">
          <el-upload action :auto-upload="true" :show-file-list="false" ref="upload" :http-request="submitUpload">
            <el-button size="mini">
              <i class="el-icon-upload"></i>
              Upload
            </el-button>
          </el-upload>
        </div>
        <div class="btn"><el-button size="mini" @click="submitBtn">Submit</el-button></div>
        <div class="btn"><el-button size="mini" @click="addFolderShow">Add folder</el-button></div>
      </div>
      <div v-else>
        <el-input v-model="folderName">
          <template #suffix>
            <i class="el-input__icon el-icon-check" @click="uploadFolder"></i>
            <i class="el-input__icon el-icon-close" @click="closeAddFolder"></i>
          </template>
        </el-input>
      </div>
    </div>
    <div class="row-style">
      <el-table
        ref="multipleTable"
        :data="dataItemListDirect"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        @select="selectRow"
        max-height="350"
        :row-style="{ height: '0' }"
        :cell-style="{ padding: '4px' }"
        row-key="id"
        :tree-props="{ children: 'children' }"
        border
        default-expand-all
        @current-change="handleCurrentChange"
        highlight-current-row
      >
        <template slot="empty">
          Please upload a file
        </template>
        <el-table-column type="selection" width="50" v-if="role == 'builder'"></el-table-column>

        <el-table-column label="Name" show-overflow-tooltip>
          <template #default="scope">
            <i class="collapse" :class="collapseClass(scope.row)"></i>
            {{ scope.row.name }}
            <span v-show="scope.row.folder == false">.{{ scope.row.suffix }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Type" show-overflow-tooltip width="70">
          <template #default="scope">
            <span v-show="scope.row.folder == true">Folder</span>
            <span v-show="scope.row.folder == false">File</span>
          </template>
        </el-table-column>
        <el-table-column label="File size" width="80">
          <template #default="scope">{{ scope.row.fileSize }}</template>
        </el-table-column>
        <el-table-column label="Upload time" width="160" show-overflow-tooltip>
          <template #default="scope">{{ scope.row.createTime }}</template>
        </el-table-column>
      </el-table>
    </div>

    <!-- upload data -->
    <el-dialog title="Upload data" :visible.sync="uploadDataDialogShow" width="40%" :close-on-click-modal="false">
      <data-upload-info @uploadSuccess="uploadSuccess"></data-upload-info>
    </el-dialog>
  </div>
</template>

<script>
import { getDataItemsByJwtUserId, updateResource, updatePerformanceById, getDataItemByCreatorId, saveDataItem, updateDataItemById, postDataContainer } from '@/api/request';
// import dataUpload from './DataUpload'; //dialogcontent
import dataUploadInfo from './DataUploadInfo'; //dialogcontent
import { getUuid, getSuffix, renderSize, getTime } from '@/utils/utils';
import { mapState } from 'vuex';

export default {
  components: {
    dataUploadInfo
  },

  data() {
    return {
      uploadDataDialogShow: false, //upload data dialog
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
      currentRow: '',
      dataItemListDirect: []
    };
  },
  computed: {
    ...mapState({
      role: state => state.permission.role
    })
  },

  methods: {
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
      let data = await getDataItemsByJwtUserId();
      // let data = await get(`/fileItems`);
      this.dataItemList = data;
      this.dataItemListDirect = this.getDataItemListDirect();

      await this.getSelectedData();
    },

    getDataItemListDirect() {
      if (this.role == 'builder') {
        if (this.dataItemList.length != 0 || this.dataItemList != null || this.dataItemList != undefined) {
          return this.dataItemList.filter(item => item.userUpload == true);
        } else {
          return [];
        }
      }
      if (this.role == 'rebuilder_operator') {
        return this.dataItemListFromResource;
      }
      return [];
    },

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
      let data = await getDataItemByCreatorId(this.projectId);
      this.dataItemListFromResource = data;
      this.$refs.multipleTable.toggleAllSelection();
      // console.log('DATA', data);
    },

    //submit
    async submitBtn() {
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: 'Warning',
          message: 'You have not select any data!',
          type: 'warning'
        });
        return;
      }

      //用id重组一个新数组
      let filter = [];
      this.multipleSelection.forEach(ele => {
        filter.push(ele.id);
      });
      await updateResource(this.projectId, {
        dataItemCollection: filter
      });

      let content = { content: 'Resource Collection', degree: '100%', type: 'success', icon: 'el-icon-folder' };

      await updatePerformanceById('resource', this.projectId, content);
    },

    addFolderShow() {
      this.folderName = '';
      this.isAddFolder = true;
    },
    closeAddFolder() {
      this.isAddFolder = false;
    },

    async uploadFolder() {
      let form = {
        alia: '',
        name: this.folderName,
        folder: true,
        description: '',
        privacy: 'discoverable',
        parent: '',
        children: [],
        userUpload: true
      };
      await this.saveResource(form);
    },

    //上传文件到服务器
    async submitUpload(param) {
      let uploadFileForm = new FormData();
      uploadFileForm.append('file', param.file);
      let data = await postDataContainer(uploadFileForm);

      let form = {
        name: data.file_name,
        suffix: getSuffix(param.file.name),
        fileSize: renderSize(param.file.size),
        address: `http://221.226.60.2:8082/data/${data.id}`,
        projectId: this.projectId,
        userUpload: true,
        children: [],
        parent: '',
        folder: false
      };

      await this.saveResource(form);
    },

    async saveResource(form) {
      if (this.currentRow == '') {
        let data = await saveDataItem(form);
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
}
</style>
