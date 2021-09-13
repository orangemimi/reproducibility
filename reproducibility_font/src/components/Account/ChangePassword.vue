
<template>
  <div>
    <el-button type="primary" @click="dialogVisible = true">Change Password</el-button>

    <el-dialog title="Change Password" :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false">
      <div class="input"><el-input v-model="oldPWD" placeholder="old password"></el-input></div>
      <div class="input"><el-input v-model="newPWD" placeholder="new password" type="password"></el-input></div>
      <div class="input"><el-input v-model="confirm" placeholder="confirm password" type="password"></el-input></div>
      <div class="btn">
        <el-button type="primary" round @click="submit">submit</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { changePassword } from '@/api/request';
import md5 from 'js-md5';
export default {
  data() {
    return {
      dialogVisible: false,
      oldPWD: '',
      newPWD: '',
      confirm: '',
    };
  },
  methods: {

    async submit() {
      if (this.oldPWD == '') {
        this.$message({
          type: 'error',
          message: 'Old password cannot be empty!',
        });
        return;
      } else if (this.newPWD == '') {
        this.$message({
          type: 'error',
          message: 'New password cannot be empty!',
        });
        return;
      } else if (this.newPWD != this.confirm) {
        this.$message({
          type: 'error',
          message: 'Inconsistent passwords!',
        });
        return;
      } else {
        try {
          await changePassword(md5(this.oldPWD), md5(this.newPWD));
          this.$message({
            type: 'success',
            message: 'Password modified successfully!',
          });
          this.dialogVisible = false;
        } catch {
          this.$message({
            type: 'error',
            message: 'Password modified failed, please check whether the password is entered correctly!',
          });
        }
      }
    },
  },
};
</script>

<style scoped lang="scss">
.input {
  padding: 18px 0;
}
.btn {
  align-items: center;
}
</style>
