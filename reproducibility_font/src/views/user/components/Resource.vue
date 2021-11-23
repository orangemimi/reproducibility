<!-- 555 -->
<template>
  <div>
    <div class="main" @contextmenu.prevent="show($event)">
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
          v-model="input1"
        ></el-input>
        <el-input size="mini" placeholder="请输入内容" suffix-icon="el-icon-date" style="width: 20%"></el-input>
      </div>

      <el-row>
        <el-col :span="24">
          <el-table
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
            @row-dblclick="rowDblclick"
          >
            <el-table-column prop="name" sortable label="Name" width="300">
              <template slot-scope="scope">
                <svg class="icon" aria-hidden="true" style="width: 20px; height: 20px" v-if="scope.row.folder">
                  <use xlink:href="#icon-wenjianjia"></use>
                </svg>
                <i class="el-icon-document" v-if="scope.row.folder == false"></i>
                <span style="margin-left: 10px" v-if="scope.row.flag == false">{{ scope.row.name }}</span>
                <el-input
                  size="small"
                  v-model="scope.row.name"
                  v-else
                  style="width: 50%; margin-left: 10px"
                  ref="input"
                  @blur="blur(scope.row)"
                  @keyup.enter.native="enterEvent()"
                ></el-input>
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
      <div class="footer">
        <span style="margin-left: 20px">2个项目</span>
        <el-divider direction="vertical"></el-divider>
      </div>
    </div>
    <menu-card v-if="flagMenu" :position="position" ref="menu" @menu="menu"></menu-card>
    <blank-menu-card v-if="flagBlank" :position="position" ref="blankMenu" @blankMenu="blankMenu"></blank-menu-card>
    <el-dialog title="Upload Data" :visible.sync="centerDialogVisible" width="30%" center v-if="centerDialogVisible">
      <upload-card :stack="stack" @uploadData="uploadData"/>
    </el-dialog>
  </div>
</template>

<script>
import menuCard from './MenuCard.vue';
import blankMenuCard from './BlankMenuCard.vue';
import uploadCard from './UploadCard.vue';

import { getFileItemByStoreyAndParent, saveFileItem, Rename } from '@/api/request';
import { dateFormat } from '@/utils/utils';
export default {
  components: { menuCard, blankMenuCard, uploadCard },
  data() {
    return {
      tableData: [],
      flagMenu: false,
      flagBlank: false,
      position: {},
      selectedItem: [],
      enter: false,
      input1: '> user',
      stack: ['-1'],
      arrayStack: [],
      oldName: '',
      centerDialogVisible: false
    };
  },
  methods: {
    //右击单元格跳出菜单栏
    test(row, column, event) {
      this.selectedItem = [];
      this.selectedItem.push(row);
      this.flagBlank = false;
      if (this.flagMenu == true) {
        this.$refs.menu.$el.style.cssText = '--left:' + event.pageX + 'px; --top:' + event.pageY + 'px;';
      } else {
        this.position.x = event.pageX;
        this.position.y = event.pageY;
        this.flagMenu = true;
      }
    },
    //右击空白处（非单元格）跳出菜单栏
    show(e) {
      // console.log(e.target.className)
      if (
        e.target.className == 'el-table__body-wrapper is-scrolling-none' ||
        e.target.className == 'el-table__empty-text' ||
        e.target.className == 'el-table__empty-block'
      ) {
        this.flagMenu = false;
        this.selectedItem = [];
        if (this.flagBlank == true) {
          this.$refs.blankMenu.$el.style.cssText = '--left:' + e.pageX + 'px; --top:' + e.pageY + 'px;';
        } else {
          this.position.x = e.pageX;
          this.position.y = e.pageY;
          this.flagBlank = true;
        }
      }
    },
    //单击选中单元格
    handleCurrentChange(row) {
      this.flagMenu = false;
      this.flagBlank = false;
      this.selectedItem = [];
      this.selectedItem.push(row);
    },
    //用来判断鼠标是否处在单元格上
    mouseEnter() {
      this.enter = true;
    },
    mouseLeave() {
      this.enter = false;
    },
    //点击表格头让菜单栏消失
    headerClick() {
      this.flagMenu = false;
      this.flagBlank = false;
    },
    //是选中的菜单栏背景颜色改变
    tableRowClassName({ row }) {
      for (let i = 0; i < this.selectedItem.length; i++) {
        if (this.selectedItem[i].id == row.id) {
          return 'selected-row';
        }
      }
      // this.selectedItem.forEach(item => {            //这种写法错误原因，return只是跳出foreach
      //   if(item == row.id) {
      //     console.log(2)
      //     return "selected-row"
      //   }
      // })
      return '';
    },
    //双击单元格进入下一层
    async rowDblclick(row) {
      let data = await getFileItemByStoreyAndParent(this.stack.length.toString(), row.id);
      data.forEach((item) => {
        item['flag'] = false;
        item.date = dateFormat(item.date, 'yyyy/MM/dd hh:mm');
      });
      this.stack.push(row.id);
      this.arrayStack.push(this.tableData);
      this.tableData = data;
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

    async init() {
      await this.getFileItemByStoreyAndParent();
      document.addEventListener('click', () => {
        if (this.enter == false) {
          this.flagMenu = false;
          this.flagBlank = false;
          this.selectedItem = [];
        }
      });
    },
    //查询用户根目录
    async getFileItemByStoreyAndParent() {
      let data = await getFileItemByStoreyAndParent('0', '-1');
      data.forEach((item) => {
        item.date = dateFormat(item.date, 'yyyy/MM/dd hh:mm');
        item['flag'] = false;
      });
      this.tableData = data;
    },

    //新建文件夹时修改文件夹名
    async blur(row) {
      row.flag = false;
      if (row.name != this.oldName) {
        let date = await this.Rename(row.id, row.name);
        row.date = dateFormat(date, 'yyyy/MM/dd hh:mm');
      }
      this.oldName = '';
    },
    enterEvent() {
      this.$refs.input.blur();
    },

    //重命名方法，返回文件更新时间
    async Rename(id, name) {
      let form = {
        id: id,
        name: name,
      };
      let data = await Rename(form);
      return data;
    },

    uploadData(val) {
      this.tableData.push({
        name: val.name,
        id: val.id,
        folder: val.folder,
        fileSize: val.fileSize,
        flag: false,
        date: dateFormat(val.updateTime, 'yyyy/MM/dd hh:mm')
      })
      this.centerDialogVisible = false
    },

    //空白栏菜单触发相应的方法事件
    async blankMenu(val) {
      if (val == 'newFolder') {
        let jsonData = {
          name: 'New folder',
          fileSize: '',
          folder: true,
          parent: this.stack[this.stack.length - 1],
          storey: this.stack.length - 1,
        };
        let data = await saveFileItem(jsonData);
        this.tableData.push({
          name: data.name,
          date: dateFormat(data.updateTime, 'yyyy/MM/dd hh:mm'),
          fileSize: data.fileSize,
          id: data.id,
          folder: data.folder,
          flag: true,
        });
        await this.$nextTick();
        this.$refs.input.focus();
        this.$refs.input.select();
        this.oldName = data.name;
      } else if (val == 'new') {
        this.centerDialogVisible = true
      }
    },

    //选中菜单栏触发的对应方法事件
    async menu(val) {
      if (val == 'rename') {
        this.tableData.forEach(async (item) => {
          if (item.id == this.selectedItem[0].id) {
            item.flag = true;
            await this.$nextTick();
            this.$refs.input.focus();
            this.$refs.input.select();
            this.oldName = item.name;
          }
        });
      }
    },
  },
  mounted() {
    this.init();
  },
};
</script>


<style lang="scss" scoped>
.main {
  width: 80%;
  height: 600px;
  background-color: white;
  padding-top: 20px;
  margin: 0 auto;
  margin-top: 100px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  border-radius: 4px;
  position: relative;

  .main /deep/ .el-table .selected-row {
    background-color: #ecf5ff;
  }

  .header {
    margin-left: 10px;
    .icon-operation {
      margin-right: 10px;
    }
  }
  .left {
    border-right: 1px solid #ebeef5;
    height: 500px;
    width: 100%;
  }
  .footer {
    height: 20px;
    position: absolute;
    bottom: 0;
    background-color: black;
    border-bottom-right-radius: 4px;
    border-bottom-left-radius: 4px;
    width: 100%;
    color: white;
    line-height: 20px;
  }
  .divider {
    margin-left: 0px;
  }
}
.main /deep/ .el-table__row > td {
  border: none;
}
</style>

