<template>
  <div class="main">
    <div class="btnList" v-if="role == 'builder'">
      <div v-if="!isAddFolder">
        <div class="btn">
          <!-- <el-button size="mini" @click="uploadDataDialogShow = true">
            Upload
          </el-button> -->
          <!-- <el-upload
            class="upload-demo"
            action="https://jsonplaceholder.typicode.com/posts/"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            multiple
            :limit="3"
            :on-exceed="handleExceed"
            :file-list="fileList"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload> -->

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
        <el-table-column type="selection" width="50" v-if="role == 'builder'">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked" :label="scope.row.name" :key="scope.row.id" @change="handleCheckAllChange(scope)"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column label="Name" show-overflow-tooltip>
          <template #default="scope">
            <i class="collapse" :class="collapseClass(scope.row)"></i>
            {{ scope.row.name }}
            <span v-show="scope.row.folder == false">.{{ scope.row.suffix }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Type" show-overflow-tooltip>
          <template #default="scope">
            <span v-show="scope.row.folder == true">Folder</span>
            <span v-show="scope.row.folder == false">File</span>
          </template>
        </el-table-column>
        <el-table-column label="File size" width="100">
          <template #default="scope">{{ scope.row.fileSize }}</template>
        </el-table-column>
        <el-table-column label="Upload time" width="180" show-overflow-tooltip>
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
import { getFileItemsByJwtUserId, getResourcesById, updateResource, updatePerformanceById, getFileItemByCreatorId, saveFileItem, updateFileItemById, postDataContainer } from '@/api/request';
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
      let data = await getFileItemsByJwtUserId();
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

    //get resources
    async getSelectedData() {
      let data = await getResourcesById(this.projectId);

      let dataSelected = data;

      this.multipleSelection = this.dataItemListDirect.filter(item => dataSelected.some(selection => selection.id == item.id));
      this.toggleSelection(this.multipleSelection);
    },

    //init table selection
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },

    //selection change
    handleSelectionChange(val) {
      // this.multipleSelection = val;
      console.log(val);
    },

    selectRow(selection, row) {
      console.log('multi', this.multipleSelection);
      console.log(selection, row);
      let val = selection[0];
      if (val.folder && val.children.length != 0 && val.children != null) {
        this.multipleSelection.push(val.children);
      }
      console.log('selection', this.multipleSelection);
    },

    handleCheckAllChange(scope) {
      // 查找父级函数
      console.log(scope.row);
      // 如果有子项，则子项的选框选择跟当前一致
      if (scope.row.children) {
        this.handleCheckAll(scope.row, scope.row.checked);
      }
      // 查找父级选框
      this.getParent(this.tableData, scope.row.id).forEach(item => {
        // 判断父级是否有子元素
        console.log(item);
        if (!item.children) {
          item.checked = scope.row.checked;
        } else {
          var num = 0;
          item.children.forEach(item => {
            // 判断子元素的checked是否为true, 并记录选中的数量
            if (item.checked === true) {
              num += 1;
            }
          });
          // 判断子元素的checked 是否全部 为true(true是选中) 如果是全部选中，那么将父元素的checked状态改成true，否则为false
          if (num === item.children.length) {
            item.checked = true;
          } else {
            item.checked = false;
          }
        }
      });

      // 是否所有子项都已经勾选
      if (this.isCheckAllEv(this.tableData) === true) {
        this.checkPageAll = true;
      } else {
        this.checkPageAll = false;
      }
    },
    // 将子元素的状态 改成和父元素的状态一样
    handleCheckAll(row, checked) {
      row.checked = checked;
      if (row.children) {
        let that = this;
        row.children.forEach((element, i) => {
          that.handleCheckAll(row.children[i], checked);
        });
      }
    },

    // 查找父级函数
    getParent(data2, nodeId2) {
      var arrRes = [];
      if (data2.length === 0) {
        if (nodeId2) {
          arrRes.push(data2);
        }
        return arrRes;
      }
      let rev = (data, nodeId) => {
        for (var i = 0, length = data.length; i < length; i++) {
          let node = data[i];
          if (nodeId === data[i].id) {
            arrRes.push(node);
            rev(data2, node.p_id);
            break;
          } else {
            if (node.children) {
              rev(node.children, nodeId);
            }
          }
        }
        return arrRes;
      };
      arrRes = rev(data2, nodeId2);
      return arrRes;
    },
    //get select

    handleCurrentChange(row) {
      this.currentRow = row;
    },
    cancleCurrentRow() {
      this.currentRow = '';
      console.log('currnetrow', this.currentRow);
    },

    async getDataAsOperator() {
      let data = await getFileItemByCreatorId(this.projectId);
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
        await updateFileItemById(parentData.id, parentData);
      }
    },

    collapseClass(params) {
      return params.folder === true ? 'el-icon-phone' : 'el-icon-delete';
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
