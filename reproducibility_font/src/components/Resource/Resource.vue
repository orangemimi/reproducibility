<template>
  <div class="main">
    <el-table
      :data="dataItemList"
      style="width: 100%"
      max-height="380"
      :default-sort="{ prop: 'date', order: 'descending' }"
      @select="select"
      @select-all="selectAll"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="Date" width="180" :sortable="true" prop="date">
        <template #default="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.updateTime }}</span>
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
            <el-tag size="medium">{{ scope.row.format }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Data Type" width="180" :sortable="true" prop="dataType">
        <template #default="scope">
          <div class="name-wrapper">
            <el-tag size="medium">{{scope.row.type}}</el-tag>
            <!-- <el-tag size="medium">STRING</el-tag> -->
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Description" width="300" prop="description">
        <template #default="scope">
          <span style="margin-left: 10px">{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right">
        <template #header>
          <el-row>
            <el-col :span="15">
              <el-input size="mini" placeholder="输入关键字搜索" />
            </el-col>
            <el-col :span="2" :offset="1">
              <el-button size="mini" class="el-icon-plus" @click="addResourceDialogShow = true"></el-button>
            </el-col>
            <el-col :span="2" :offset="2">
              <el-button size="mini" class="el-icon-delete" @click="deleteBtn"></el-button>
            </el-col>
          </el-row>
        </template>
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">EDIT</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">DEL</el-button>
          <el-button size="mini" class="el-icon-view"></el-button>
          <el-link :href="scope.row.value" style="margin-left: 10px"> 
            <el-button size="mini" class="el-icon-download"></el-button>
          </el-link>
          
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="Add resource" :visible.sync="addResourceDialogShow" width="40%" :close-on-click-modal="false" >
      <add-resource @dataItem="dataItem" v-if="addResourceDialogShow"/>
    </el-dialog>

    <el-dialog title="Edit resource" :visible.sync="editResourceDialogShow" width="40%" :close-on-click-modal="false" >
      <add-resource :parameter="editParameter" @update="update" v-if="editResourceDialogShow"/>
    </el-dialog>
  </div>
</template>

<script>
import { getDataItemsByProjectId, deleteDataItemById, batchDelete, updatePerformanceById } from '@/api/request';
import addResource from './components/AddResource.vue';
import { dateFormat } from '@/utils/utils';
import { mapState } from 'vuex';

export default {
  components: {
    addResource,
  },

  data() {
    return {
      dataItemList: [],
      dataItemListFromResource: [],
      projectId: this.$route.params.id,
      addResourceDialogShow: false,
      editResourceDialogShow: false,
      editParameter: {},
      selection: [],
    };
  },
  computed: {
    ...mapState({
      role: (state) => state.permission.role,
    }),
  },

  methods: {
    async dataItem(val) {
      await updatePerformanceById('resource', this.projectId, {content: 'add resource'})
      console.log(val);
      let date = new Date();
      console.log(dateFormat(date, 'yyyy/MM/dd hh:mm'));
      this.dataItemList.push({
        updateTime: dateFormat(date, 'yyyy/MM/dd hh:mm'),
        name: val.name,
        format: val.format,
        type: val.type,
        description: val.description,
        id: val.id,
      });
      this.addResourceDialogShow = false;
    },

    async update(val) {
      await updatePerformanceById('resource', this.projectId, 'update resource')
      console.log(val)
      this.dataItemList.forEach(dataItem => {
        if(dataItem.id == val.id) {
          dataItem.name = val.name
          dataItem.format = val.type
          dataItem.type = val.dataType
          dataItem.description = val.desc
        }
      })
      console.log(this.dataItemList)
      this.editResourceDialogShow = false
    },

    handleEdit(row) {
      console.log(row)
      this.editParameter = row
      this.editResourceDialogShow = true
    },
    async handleDelete(index, row) {
      this.$confirm('This operation will permanently delete the file. Do you want to continue?', 'Tips', {
        confirmButtonText: 'comfirm',
        cancelButtonText: 'cancel',
        type: 'warning',
      })
        .then(async () => {
          await deleteDataItemById(row.id);
          await updatePerformanceById('resource', this.projectId, 'delete resource')
          this.dataItemList.forEach((item, itemIndex) => {
            if (item.id == row.id) {
              this.dataItemList.splice(itemIndex, 1);
            }
          });
          this.$message({
            type: 'success',
            message: 'Delete succeeded!',
          });
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: 'Deletion cancelled!',
          });
        });
    },


    select(selection) {
      this.selection = [];
      selection.forEach((item) => {
        this.selection.push(item.id);
      });
    },
    selectAll(selection) {
      this.selection = [];
      selection.forEach((item) => {
        this.selection.push(item.id);
      });
    },
    async deleteBtn() {
      if (this.selection.length > 0) {
        this.$confirm('This operation will permanently delete the file. Do you want to continue?', 'Tips', {
          confirmButtonText: 'comfirm',
          cancelButtonText: 'cancel',
          type: 'warning',
        })
          .then(async () => {
            await batchDelete(this.selection);
            await updatePerformanceById('resource', this.projectId, 'delete resource')
            this.dataItemList.forEach((item, itemIndex) => {
              this.selection.forEach((select) => {
                if (item.id == select) {
                  this.dataItemList.splice(itemIndex, 1);
                }
              });
            });
            this.$message({
              type: 'success',
              message: 'Delete succeeded!',
            });
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: 'Deletion cancelled!',
            });
          });
      } else {
        this.$notify({
          title: 'warning',
          message: 'Please select the data to delete!',
          type: 'warning',
        });
      }
    },

    //get all the data
    async getDataCollection() {
      let data = await getDataItemsByProjectId(this.projectId);
      data.forEach((item) => {
        item.updateTime = dateFormat(item.updateTime, 'yyyy/MM/dd hh:mm');
      });
      this.dataItemList = data;
      console.log(data)
    },
    async getDataAsOperator() {
      let data = await getDataItemsByProjectId(this.projectId);
      this.dataItemListFromResource = data;
      this.$refs.multipleTable.toggleAllSelection();
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
