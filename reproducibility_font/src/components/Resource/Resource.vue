<template>
  <div class="main">
    <!-- <div class="btnList" v-if="role == 'builder'">
      <div class="btn">

        <div class="btn"><el-button size="mini" @click="addData">Add Item</el-button></div>
      </div>
    </div>
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
          <template #default="scope">
            <edit-resource :initFormData="scope.row"/>
          </template>
        </el-table-column>

        <el-table-column label="Name" width="130">
          <template #default="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="Format" width="100">
          <template #default="scope">
            <span>{{ scope.row.format }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Upload time" width="130">
          <template #default="scope">{{ scope.row.createTime }}</template>
        </el-table-column>
        <el-table-column label="Operate" fixed="right">
          <template #default="scope">
            <div v-if="scope.row.format != 'parameter'">
              <el-button size="mini">Download</el-button>
              <el-button size="mini" @click="delDataItem(scope)">Del</el-button>
            </div>
            <div v-else>

              <el-button size="mini" @click="delDataItem(scope)">Del</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div> -->
    <el-table :data="tableData" style="width: 100%" max-height="380" :default-sort="{ prop: 'date', order: 'descending' }">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="Date" width="180" :sortable="true" prop="date">
        <template #default="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.date }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" width="180" :sortable="true" prop="name">
        <template #default="scope">
          <span style="margin-left: 10px">{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Type" width="180" :sortable="true" prop="type">
        <template #default="scope">
          <div class="name-wrapper">
            <el-tag size="medium">{{ scope.row.type }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Data Type" width="180" :sortable="true" prop="dataType">
        <template #default="scope">
          <div class="name-wrapper">
            <el-tag size="medium">{{ scope.row.dataType }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Description" width="300" prop="description">
        <template #default="scope">
          <span style="margin-left: 10px">{{ scope.row.des }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right">
        <template #header>
          <el-row>
            <el-col :span="15">
              <el-input size="mini" placeholder="输入关键字搜索" />
            </el-col>
            <el-col :span="2" :offset="1">
              <el-button size="mini" class="el-icon-plus"></el-button>
            </el-col>
            <el-col :span="2" :offset="2">
              <el-button size="mini" class="el-icon-delete"></el-button>
            </el-col>
          </el-row>
          
        </template>
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          <el-button size="mini" class="el-icon-view"></el-button>
          <el-button size="mini" class="el-icon-download"></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="Upload resource" :visible.sync="uploadDataDialogShow" width="40%" :close-on-click-modal="false" :append-to-body="true">
      <data-upload-info @uploadSuccess="uploadSuccess"></data-upload-info>
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
import { getDataItemsByProjectId, updateDataItemById, saveFileItem, postDataContainer, deleteDataItemById } from '@/api/request';
// import dataUpload from './DataUpload'; //dialogcontent
import dataUploadInfo from './DataUploadInfo'; //dialogcontent
import bindResource from './BindResource';
import fileUpload from './FileUpload';
// import editResource from './EditResource.vue';
import { getUuid, getSuffix, renderSize, getTime } from '@/utils/utils';
import { mapState } from 'vuex';

export default {
  components: {
    dataUploadInfo,
    bindResource,
    fileUpload,
    // editResource,
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

      initFormData: {},

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
      selectItem: {},

      tableData: [
        {
          date: '2016-05-02',
          name: '王小虎',
          des: '上海市普陀区金沙江路 1518 弄',
          type: 'input',
          dataType: 'FILE',
        },
        {
          date: '2016-05-04',
          name: '王小虎',
          des: '上海市普陀区金沙江路 1517 弄',
          type: 'input',
          dataType: 'FILE',
        },
        {
          date: '2016-05-01',
          name: '王小虎',
          des: '上海市普陀区金沙江路 1519 弄',
          type: 'parameter',
          dataType: 'INT',
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          des: '上海市普陀区金沙江路 1516 弄',
          type: 'input',
          dataType: 'FILE',
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          des: '上海市普陀区金沙江路 1516 弄',
          type: 'input',
          dataType: 'FILE',
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          des: '上海市普陀区金沙江路 1516 弄',
          type: 'input',
          dataType: 'FILE',
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          des: '上海市普陀区金沙江路 1516 弄',
          type: 'input',
          dataType: 'FILE',
        },
      ],
    };
  },
  computed: {
    ...mapState({
      role: (state) => state.permission.role,
    }),
  },

  methods: {
    //add data dialog show
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    addData() {
      this.uploadDataDialogShow = true;
    },
    //close the dialog
    uploadSuccess(val) {
      if (val.flag) {
        this.uploadDataDialogShow = false;
        this.dataItemList.push(val.data);
      }
    },

    async delDataItem(scope) {
      console.log(scope);
      await deleteDataItemById(scope.row.id);
      this.dataItemList.splice(scope.$index, 1);
      this.dataItemList;
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
          rows.forEach((row) => {
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
      if (selection.some((el) => el == row)) {
        if (val.folder && val.children.length != 0 && val.children != null) {
          val.children.forEach((child) => {
            this.multipleSelection.push(child);
          });
        }
      } else {
        if (val.folder && val.children.length != 0 && val.children != null) {
          val.children.forEach((child) => {
            this.multipleSelection.splice((item) => item.id == child.id);
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
        folder: false,
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
      // console.log('row', row);
      return row.id;
    },
    clickTable(row) {
      this.$refs.multipleTable.toggleRowExpansion(row);
    },

    bindResource(selectItem) {
      this.bindResourceDialogShow = true;
      this.selectItem = this.dataItemList.find((item) => item.id == selectItem.id);
      // console.log(item);
    },

    async returnResourceUrl(value) {
      console.log(value);
      this.selectItem.value = value.url;
      if (value[1] != '') {
        this.selectItem.token = value.token;
      }
      // let json ={value:value}
      let data = await updateDataItemById(this.selectItem.id, this.selectItem);
      console.log('updateData', data);
    },
  },
  async mounted() {
    if (this.role == 'builder') {
      await this.getDataCollection();
    }
    if (this.role == 'rebuilder_operator') {
      await this.getDataAsOperator();
    }
  },
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
