<!-- resourceTable -->
<template>
  <div class="main">
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
  </div>
</template>

<script>
import { getFileItemsByJwtUserId } from '@/api/request';

export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    return {
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
      currentRow: '',
      fileItemListDirect: []
    };
  },

  methods: {
    async getFileCollection() {
      let file = await getFileItemsByJwtUserId();
      // let file = await get(`/fileItems`);
      this.fileItemList = file;
      this.fileItemListDirect = this.getFileItemListDirect();

      await this.getSelectedFile();
    },

    getFileItemListDirect() {
      if (this.fileItemList.length != 0 || this.fileItemList != null || this.fileItemList != undefined) {
        return this.fileItemList.filter(item => item.userUpload == true);
      } else {
        return [];
      }
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
    },
    cancleCurrentRow() {
      this.currentRow = '';
    }
  },

  mounted() {
    this.getFileCollection();
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
