<template>
  <div class="main">
    <div class="row-style">
      <!-- <div class="table"> -->
      <el-table ref="multipleTable" :data="dataItemListDirect" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" max-height="350" :row-style="{ height: '0' }" :cell-style="{ padding: '4px' }">
        <template slot="empty">
          Please upload a file
        </template>
        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column label="Name" show-overflow-tooltip>
          <template #default="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="Type" width="70">
          <template #default="scope">{{ scope.row.type }}</template>
        </el-table-column>
        <el-table-column label="File size" width="100">
          <template #default="scope">{{ scope.row.fileSize }}</template>
        </el-table-column>
        <el-table-column label="Upload time" width="180" show-overflow-tooltip>
          <template #default="scope">{{ scope.row.createTime }}</template>
        </el-table-column>
      </el-table>
      <!-- </div> -->
    </div>
    <div class="btnList">
      <div class="btn">
        <el-button size="mini" @click="uploadDataDialogShow = true">
          Upload
        </el-button>
      </div>
      <div class="btn"><el-button size="mini" @click="submitBtn">Submit</el-button></div>
    </div>

    <!-- upload data -->
    <el-dialog title="Upload data" :visible.sync="uploadDataDialogShow" width="40%" :close-on-click-modal="false">
      <data-upload-info @uploadSuccess="uploadSuccess"></data-upload-info>
    </el-dialog>
  </div>
</template>

<script>
import { getDataItemsByJwtUserId, getResourcesById, updateResource } from '@/api/request';
// import dataUpload from './DataUpload'; //dialogcontent
import dataUploadInfo from './DataUploadInfo'; //dialogcontent
export default {
  components: {
    // dataUpload,
    dataUploadInfo
    // manageTools
  },

  data() {
    return {
      uploadDataDialogShow: false, //upload data dialog
      dataItemList: [],
      projectId: this.$route.params.id,
      modelItemList: [],
      checkAll: false,
      checkedDataItemList: [],
      isIndeterminate: false,

      //table
      multipleSelection: []
    };
  },
  computed: {
    dataItemListDirect() {
      if (this.dataItemList.length != 0 || this.dataItemList != null || this.dataItemList != undefined) {
        return this.dataItemList.filter(item => item.isDirect == true);
      } else {
        return [];
      }
    }
  },

  methods: {
    //close the dialog
    uploadSuccess(val) {
      if (val) {
        this.uploadDataDialogShow = false;
      }
    },

    downloadDataResource(data) {
      window.open(data.url);
    },

    //get all the data
    async getDataCollection() {
      let data = await getDataItemsByJwtUserId();
      // let data = await get(`/dataItems`);
      this.dataItemList = data;

      await this.getSelectedData();
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
      this.multipleSelection = val;
    },

    //submit
    async submitBtn() {
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: 'Warning',
          message: 'You have not select any data!',
          type: 'warning'
        });
      } else {
        let filter = [];
        this.multipleSelection.forEach(ele => {
          filter.push(ele.id);
        });
        // console.log(filter);
        await updateResource(this.projectId, {
          dataItemCollection: filter
        });
        // let data = await patch(`resources/data/${this.projectId}`, {
        //   dataItemCollection: filter
        // });
        // console.log(data);
      }
    }
  },
  async mounted() {
    await this.getDataCollection();
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
    padding: 5% 35% 0 35%;

    .btn {
      float: left;
      margin-right: 10px;
      // width: 300px;
    }
  }
}
</style>
