<template>
  <div class="main">
    <div class="header">
      <i class="icon-operation el-icon-back" @click="iconClick('left')"></i>
      <i class="icon-operation el-icon-right" @click="iconClick('right')"></i>
      <i class="icon-operation el-icon-top"></i>
      <el-input
        size="mini"
        placeholder="请输入内容"
        suffix-icon="el-icon-refresh-right"
        prefix-icon="el-icon-s-platform"
        style="width: 30%; margin-right: 10px"
      ></el-input>
      <el-input size="mini" placeholder="请输入内容" suffix-icon="el-icon-date" style="width: 20%"></el-input>
    </div>
    <el-row>
      <el-col :span="24">
        <!-- <el-table
          :data="tableData"
          style="width: 100%"
          max-height="532"
          height="532"
          @row-contextmenu="test"
          :default-sort="{ prop: 'name', order: 'descending' }"
          class="customer-table"
          @cell-click="handleCurrentChange"
          :row-class-name="tableRowClassName"
          @cell-mouse-enter="mouseEnter"
          @cell-mouse-leave="mouseLeave"
          @header-click="headerClick"
          
        > -->
        <el-table
          :data="tableData"
          style="width: 100%"
          max-height="532"
          height="532"
          :default-sort="{ prop: 'name', order: 'descending' }"
          class="customer-table"
          @row-dblclick="rowDblclick"
        >
          <el-table-column prop="name" sortable label="Name" width="300">
            <template slot-scope="scope">
              <svg class="icon" aria-hidden="true" style="width: 20px; height: 20px" v-if="scope.row.folder">
                <use xlink:href="#icon-wenjianjia"></use>
              </svg>
              <i class="el-icon-document" v-if="scope.row.folder == false"></i>
              <span style="margin-left: 10px">{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="date" sortable width="150">
            <template slot="header">
              <el-divider direction="vertical" class="divider"></el-divider>
              Date
            </template>
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.date }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="province" sortable width="150">
            <template slot="header">
              <el-divider direction="vertical" class="divider"></el-divider>
              Type
            </template>
            <template slot-scope="scope">
              <span style="margin-left: 10px" v-if="scope.row.folder">folder</span>
              <span style="margin-left: 10px" v-else>file</span>
            </template>
          </el-table-column>
          <el-table-column prop="city" sortable width="120">
            <template slot="header">
              <el-divider direction="vertical" class="divider"></el-divider>
              Size
            </template>
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.fileSize }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getFileItemByStoreyAndParent } from '@/api/request';
import { dateFormat } from '@/utils/utils';
export default {
  data() {
    return {
      tableData: [],
      stack: ['-1'],
      arrayStack: []
    };
  },
  methods: {
    async getFileItemByStoreyAndParent() {
      let data = await getFileItemByStoreyAndParent('0', '-1');
      data.forEach(item => {
        item.date = dateFormat(item.date, 'yyyy/MM/dd hh:mm');
      });
      this.tableData = data;
    },
    async init() {
      await this.getFileItemByStoreyAndParent();
    },
    iconClick(type) {
      if (type == 'left') {
        if (this.stack.length > 1) {
          let temp = this.arrayStack[this.arrayStack.length - 1];
          this.stack.pop();
          this.arrayStack.pop();
          this.tableData = temp;
        }
      }
      if (type == 'right') {
        console.log(this.stack);
        console.log(this.arrayStack);
      }
    },
    async rowDblclick(row) {
      if (row.folder) {
        let data = await getFileItemByStoreyAndParent(this.stack.length.toString(), row.id);
        data.forEach(item => {
          item.date = dateFormat(item.date, 'yyyy/MM/dd hh:mm');
        });
        this.stack.push({
          id: row.id,
          name: row.name
        });
        this.arrayStack.push(this.tableData);
        this.tableData = data;
      } else {
        let data = {
          row: row,
          stack: this.stack
        };
        this.$emit('selectFile', data);
      }
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style lang="scss" scoped>
.icon-operation {
  margin-right: 10px;
}
.main /deep/ .el-table__row > td {
  border: none;
}
</style>
