<!--  -->
<template>
  <div class="main">
    <!-- new notice -->
    <!-- {{ notice }} -->
    <div v-if="notice.state == 'unread'" class="item" @click="readNotice">
      <el-badge value="new" type="danger" class="badge">
        <el-card class="unreadCard" shadow="hover">
          <div class="task_name">
            <!-- {{ notice.content }} -->
          </div>
          <div class="task_createTime">
            <i class="el-icon-time" />
            {{ notice.createTime }}
          </div>
        </el-card>
      </el-badge>
    </div>
    <!-- notice read but not approve-->
    <div v-else @click="readReadedNotice" class="item">
      <div :class="notice.state == 'read' ? 'readCard' : notice.state == 'approve' ? 'approveCard' : 'disapproveCard'">
        <el-card shadow="hover">
          <div class="task_name">
            <!-- {{ notice.content }} -->
          </div>
          <div class="task_createTime">
            <i class="el-icon-time" />
            {{ notice.createTime }}
          </div>
        </el-card>
      </div>
    </div>
    <!-- notice approve or disapprove -->

    <div class="dialogs">
      <el-dialog title="Read Notice" :visible.sync="joinProjectDialog" width="30%" :close-on-click-modal="false">
        <span v-show="dialogFooter">
          <el-button @click="joinProjectApprove" size="mini">Agree</el-button>
          <el-button @click="joinProjectDisapprove" size="mini">Refuse</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { changeNoticeState, updateProjectMembers } from '@/api/request';

export default {
  props: {
    noticeItem: {
      type: Object
    }
  },

  watch: {
    noticeItem: {
      handler(val) {
        this.notice = val;
        this.init();
      },
      deep: true
    }
  },

  computed: {},

  data() {
    return {
      notice: this.noticeItem,
      joinProjectDialog: false,
      dialogFooter: true
    };
  },

  methods: {
    readNotice() {
      if (this.notice.content.type == 'joinProject') {
        this.joinProjectDialog = true;
        this.dialogFooter = true;
      }
    },

    readReadedNotice() {
      if (this.notice.content.type == 'joinProject') {
        this.joinProjectDialog = true;
        this.dialogFooter = false;
      }
    },
    async joinProjectApprove() {
      let notice = this.notice;

      await changeNoticeState(notice.id, 'approve');
      
      await updateProjectMembers(notice.content.projectId, { memberId: notice.senderId, role: notice.content.role });

      this.joinProjectDialog = false;
    },

    async joinProjectDisapprove() {
      let notice = this.notice;
      await changeNoticeState(notice.id, 'read');

      this.joinProjectDialog = false;
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  height: 60px;
  width: 50%;
  margin-bottom: 15px;

  .item {
    width: 100%;

    .task_name {
      font-size: 14px;
      font-weight: 600;
      margin-bottom: 10px;
      // padding-left: 10px;
    }
    .task_createTime {
      font-size: 12px;
      text-align: right;
      color: #5c5c5c;
    }
    .unreadCard {
      background-color: #ebedf9;
      border: 1px solid rgba(62, 82, 124, 0.8);
      // border-top: 5px solid #67c23a;
    }
    .readCard {
      background-color: #ebedf9;
      border: 1px solid rgba(64, 124, 62, 0.8);

      /deep/ .el-card {
        width: 100%;
        height: 100%;
      }
      /deep/ .el-card__body {
        padding: 10px 5px;
      }

      .el-card:hover {
        box-shadow: 0px 0px 2px 1px rgba(27, 94, 238, 0.2);
        cursor: pointer;
      }
    }
  }
}
</style>
