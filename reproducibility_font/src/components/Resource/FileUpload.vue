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
        <!-- <div class="btn"></div> -->
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
        :data="fileItemListDirect"
        tooltip-effect="dark"
        style="width: 100%"
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
        <el-table-column label="File size" width="180">
          <template #default="scope">{{ scope.row.fileSize }}</template>
        </el-table-column>
        <el-table-column label="Upload time" width="200" show-overflow-tooltip>
          <template #default="scope">{{ scope.row.createTime }}</template>
        </el-table-column>
      </el-table>
    </div>
    <div class="contentBottom">
      <div class="selectFile" v-show="currentRow != ''">
        <!-- {{ currentRow }} -->
        <div style="float:left">{{ currentRow.name }} . {{ currentRow.suffix }}</div>
        <i class="el-icon-error" style="float:right;" @click="cancleRow" />
      </div>
    </div>

    <!-- upload data -->
    <el-dialog title="Upload data" :visible.sync="uploadFileDialogShow" width="40%" :close-on-click-modal="false">
      <data-upload-info @uploadSuccess="uploadSuccess"></data-upload-info>
    </el-dialog>
  </div>
</template>

<script>
import { getFileItemsByJwtUserId, getFileItemByCreatorId, saveFileItem, updateFileItemById, postDataContainer } from '@/api/request';
// import dataUpload from './FileUpload'; //dialogcontent
import dataUploadInfo from './DataUploadInfo'; //dialogcontent
import { getUuid, getSuffix, renderSize, getTime } from '@/utils/utils';
import { mapState } from 'vuex';

export default {
  components: {
    dataUploadInfo
  },

  data() {
    return {
      uploadFileDialogShow: false, //upload data dialog
      fileItemList: [],
      fileItemListFromResource: [],
      projectId: this.$route.params.id,
      modelItemList: [],
      checkAll: false,
      checkedFileItemList: [],
      isIndeterminate: false,

      //table
      multipleSelection: [],

      //add folder
      isAddFolder: false,
      folderName: '',
      currentRow: ''
      // fileItemListDirect: []
    };
  },
  computed: {
    ...mapState({
      role: state => state.permission.role
    }),
    fileItemListDirect() {
      if (this.fileItemList.length != 0 || this.fileItemList != null || this.fileItemList != undefined) {
        return this.fileItemList.filter(item => item.userUpload == true);
      } else {
        return [];
      }
    }
  },

  methods: {
    //close the dialog
    uploadSuccess(val) {
      if (val) {
        this.uploadFileDialogShow = false;
      }
    },

    downloadFileResource(data) {
      window.open(data.address);
    },

    //get all the data
    async getFileCollection() {
      let data = await getFileItemsByJwtUserId();
      // let data = await get(`/fileItems`);
      this.fileItemList = data;
      // this.fileItemListDirect = this.getFileItemListDirect();

      // await this.getSelectedFile();
    },

    getFileItemListDirect() {
      if (this.role == 'builder') {
        if (this.fileItemList.length != 0 || this.fileItemList != null || this.fileItemList != undefined) {
          return this.fileItemList.filter(item => item.userUpload == true);
        } else {
          return [];
        }
      }
      if (this.role == 'rebuilder_operator') {
        return this.fileItemListFromResource;
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
    // handleSelectionChange(val) {
    //   // this.multipleSelection = val;
    //   console.log(val);
    // },

    // selectRow(selection, row) {
    //   let val = row;
    //   // this.multipleSelection.push(row);
    //   if (selection.some(el => el == row)) {
    //     if (val.folder && val.children.length != 0 && val.children != null) {
    //       val.children.forEach(child => {
    //         this.multipleSelection.push(child);
    //       });
    //     }
    //   } else {
    //     if (val.folder && val.children.length != 0 && val.children != null) {
    //       val.children.forEach(child => {
    //         this.multipleSelection.splice(item => item.id == child.id);
    //       });
    //     }
    //   }

    //   console.log('selection', this.multipleSelection);
    //   this.toggleSelection(this.multipleSelection);
    // },

    handleCurrentChange(row) {
      this.currentRow = row;
      this.$emit('returnFileUrl', row);
    },
    cancleCurrentRow() {
      this.currentRow = '';
    },

    async getFileAsOperator() {
      let data = await getFileItemByCreatorId(this.projectId);
      this.fileItemListFromResource = data;
      this.$refs.multipleTable.toggleAllSelection();
      // console.log('DATA', data);
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
      await this.saveProjectResource(form);
    },

    //上传文件到服务器
    async submitUpload(param) {
      let uploadFileForm = new FormData();
      uploadFileForm.append('file', param.file);
      let data = await postDataContainer(uploadFileForm);

      console.log('submitupload', data);

      let form = {
        name: data.file_name,
        alia: '',
        source: '',
        thumbnail: '',
        privacy: '',
        suffix: getSuffix(param.file.name),
        fileSize: renderSize(param.file.size),
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
        this.fileItemList.push(data);
      } else {
        form.parent = this.currentRow.id;
        form.id = getUuid();
        form.createTime = getTime();
        let parentFile = this.currentRow;
        if (parentFile.children == null) {
          parentFile.children = [];
        }
        parentFile.children.push(form);
        await updateFileItemById(parentFile.id, parentFile);
      }
      //  this.fileItemListDirect
    },

    collapseClass(params) {
      return params.folder === true ? 'el-icon-folder' : 'el-icon-document';
    },
    cancleRow() {
      this.currentRow = '';
    }
  },
  async mounted() {
    await this.getFileCollection();
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

  .contentBottom {
    height: 40px;
    line-height: 40px;
    margin-top: 10px;

    .selectFile {
      background-color: lightgoldenrodyellow;
      float: left;
      width: 250px;
      margin: 0 0 0 10px;
      border: 1px solid #dddddd;
      border-radius: 4px;
      // border-top: 5px solid #67c23a;
    }
    .submitBtn {
      float: right;
      // width: 30%;
    }
  }
}
</style>
