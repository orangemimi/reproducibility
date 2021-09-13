
<template>
  <div>
    <el-table
      :data="noticeList"
      style="width: 100%"
      max-height="450"
      :default-sort="{ prop: 'createTime', order: 'descending' }"
      :row-class-name="tableRowClassName"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column sortable fixed prop="createTime" label="Date" width="200"></el-table-column>
      <el-table-column sortable prop="projectname" label="Project Name" width="175"></el-table-column>
      <el-table-column sortable prop="sendername" label="Applicant" width="175"></el-table-column>
      <el-table-column sortable prop="state" label="State" width="175"></el-table-column>
      <el-table-column prop="type" label="Description" width="175"></el-table-column>
      <el-table-column fixed="right" label="Operate" width="200">
        <template #default="scope">
          <el-button v-show="scope.row.state == 'unread' ? true : false" @click.prevent="agree(scope)" type="success" size="small">Agree</el-button>
          <el-button v-show="scope.row.state == 'unread' ? true : false" @click.prevent="ignore(scope)" type="warning" size="small">Ignore</el-button>
          <el-button v-show="scope.row.state == 'unread' ? false : true" @click.prevent="del(scope)" type="text" size="small">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="notice_btn">
      <el-button @click="delread" :disabled="unreadnum == 0 ? true : false" type="text" style="margin-right: 30px">Delete the Read</el-button>
      <el-button @click="delselected" :disabled="selectionList.length == 0 ? true : false" type="text" style="margin-left: 30px">Delete</el-button>
    </div>
  </div>
</template>

<script>
import { getNoticesByRecipientIdTrue, changeNoticeState, changeRecState, updateProjectMembers } from '@/api/request';
export default {
  data() {
    return {
      selectionList: [],
      unreadnum: 0,
      noticeList: [],
    };
  },
  methods: {
    async init() {
      await this.getNoticesByRecipientIdTrue();
    },
    async getNoticesByRecipientIdTrue() {
      this.noticeList = await getNoticesByRecipientIdTrue();
      for (let i = 0; i < this.noticeList.length; i++) {
        this.noticeList[i]['projectname'] = this.noticeList[i].content.projectName;
        this.noticeList[i]['sendername'] = this.noticeList[i].content.sendername[0];
      }
    },

    async agree(scope) {
      let form = { memberId: scope.row.senderId, role: scope.row.content.role };
      await updateProjectMembers(scope.row.content.projectId, form);
      await changeNoticeState(scope.row.id, 'approve');
      scope.row.state = 'approve';
      this.unreadnum += 1;
      this.$store.state.user.unreadApplynum -= 1;
      localStorage.setItem('unreadApplynum', this.$store.state.user.unreadApplynum);
    },
    async ignore(scope) {
      await changeNoticeState(scope.row.id, 'refuse');
      scope.row.state = 'refuse';
      this.unreadnum += 1;
      this.$store.state.user.unreadApplynum -= 1;
      localStorage.setItem('unreadApplynum', this.$store.state.user.unreadApplynum);
    },
    async del(scope) {
      await changeRecState(scope.row.id);
      for (let i = 0; i < this.noticeList.length; i++) {
        if (this.noticeList[i].id == scope.row.id) {
          this.noticeList.splice(i, 1);
          this.unreadnum -= this.unreadnum;
          break;
        }
      }
    },
    async delread() {
      //删除全部已读信息
      for (let i = 0; i < this.noticeList.length; i++) {
        if (this.noticeList[i].state != 'unread') {
          await changeRecState(this.noticeList[i].id);
          this.noticeList.splice(i, 1);
          i = i - 1;
        }
      }
      this.unreadnum = 0;
    },
    async delselected() {
      //删除所选信息
      let temp = this.selectionList; //困扰很久的一个bug，这里必须有一个变量替换this.selectionList，因为在这个函数执行的时候，handleSelectionChange也在执行
      //   for (let i = 0; i < this.selectionList.length; i++) {       错误写法
      for (let i = 0; i < temp.length; i++) {
        for (let j = 0; j < this.noticeList.length; j++) {
          if (temp[i].id == this.noticeList[j].id) {
            if (this.noticeList[j].state == 'unread') {
              await changeNoticeState(this.noticeList[j].id, 'refuse');
              await changeRecState(this.noticeList[j].id);
              this.noticeList.splice(j, 1);
              this.$store.state.user.unreadApplynum -= 1;
              localStorage.setItem('unreadApplynum', this.$store.state.user.unreadApplynum);
            } else {
              await changeRecState(this.noticeList[j].id);
              this.noticeList.splice(j, 1);
              this.unreadnum -= this.unreadnum;
            }
            break;
          }
        }
      }
      this.multipleSelection = [];
      this.selectionList = [];
    },

    tableRowClassName({ row }) {
      if (row.state == 'unread') {
        return 'success-row';
      }
      return '';
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.selectionList = val;
    },
  },
  mounted: async function () {
    await this.init();
    let num = 0;
    for (let i = 0; i < this.noticeList.length; i++) {
      if (this.noticeList[i].state != 'unread') {
        num++;
      }
    }
    this.unreadnum = num;
  },
};
</script>

<style>
.el-table .success-row {
  background: #f0f9eb;
}
.notice_btn {
  text-align: center;
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>
