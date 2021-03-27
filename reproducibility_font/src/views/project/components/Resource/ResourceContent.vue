<template>
  <div class="main">
    <el-row class="row-style">
      <div class="data_container">
        <el-button @click="uploadDataDialogShow = true" size="mini">Upload data</el-button>
        <div>
          <el-checkbox
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            @change="handleCheckAllChange"
          >
            Select all
          </el-checkbox>
          <div style="margin: 15px 0;"></div>
          <el-checkbox-group v-model="checkedDataItemList" @change="checkDataItem">
            <el-checkbox v-for="(item, index) in dataItemListDirect" :label="item" :key="index">
              {{ item.name }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
    </el-row>

    <!-- upload data -->
    <el-dialog
      :title="'Upload data to ' + projectInfo.name"
      :visible.sync="uploadDataDialogShow"
      width="40%"
      :close-on-click-modal="false"
    >
      <data-upload-info :projectInfo="projectInfo"></data-upload-info>
    </el-dialog>
  </div>
</template>

<script>
import { get } from '@/axios';
// import dataUpload from './DataUpload'; //dialogcontent
import dataUploadInfo from './DataUploadInfo'; //dialogcontent
export default {
  props: {
    projectInfo: {
      type: Object
    }
  },

  watch: {
    projectInfo: {
      handler(val) {
        if (val != null) {
          this.projectId = val.id;
          this.getDataCollection();
        }
      }
    }
  },

  components: {
    // dataUpload,
    dataUploadInfo
    // manageTools
  },

  data() {
    return {
      uploadDataDialogShow: false, //upload data dialog
      dataItemList: [],
      projectId: this.projectInfo.id,
      modelItemList: [],
      checkAll: false,
      checkedDataItemList: [],
      isIndeterminate: false
    };
  },
  computed: {
    dataItemListDirect() {
      return this.dataItemList.filter(item => item.isDirect == true);
    }
  },

  methods: {
    downloadDataResource(data) {
      window.open(data.url);
    },

    // async getResources() {
    //   let dataItem = await get(`/dataItems/${this.projectId}`);
    //   let modelItem = await get(`/toolItems/${this.projectId}`);

    //   this.modelItemList = modelItem;
    //   this.dataItemList = dataItem;
    // },
    async getDataCollection() {
      let data = await get(`/dataItems/${this.projectInfo.id}`, this.uploadFileForm);
      this.dataItemList = data;
    },

    handleCheckAllChange(value) {
      this.checkedDataItemList = value ? this.dataItemList : [];
      this.isIndeterminate = false;
    },

    checkDataItem(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.dataItemList.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.dataItemList.length;
    }
  },
  async mounted() {
    // await this.getDataCollection();
  }
};
</script>

<style lang="scss" scoped>
.main {
  margin: 0 20px;
  height: 100%;

  .row-style {
    margin: 0 10px;
    height: 100%;

    .data_container {
      // width: 300px;
      float: left;
      margin-left: 15px;
      height: 95%;
    }
  }

  .tools_container {
    // width: 40px;
    float: left;
  }
  .container_divider {
    float: left;
    height: 95%;
  }
}
</style>
