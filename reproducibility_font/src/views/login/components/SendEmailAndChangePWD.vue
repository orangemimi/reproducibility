
<template>
  <div>
    <el-button type="text" @click="dialogClick">Forget your password ?</el-button>
    <el-dialog title="Reset Password" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false">
      <div v-show="flag == 1 ? true : false">
        <el-steps :active="1" align-center>
          <el-step title="Step1" description="Send verification code"></el-step>
          <el-step title="Step2" description="Submit new password"></el-step>
        </el-steps>
        <el-form status-icon label-width="100px" class="demo-ruleForm">
          <el-form-item label="Email">
            <el-input id="email" v-model="email"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="send_email">Submit</el-button>
            <el-button @click="reset('email')">Reset</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div v-show="flag == 0 ? true : false">
        <el-steps :active="2" align-center>
          <el-step title="Step1" description="Send verification code"></el-step>
          <el-step title="Step2" description="Submit new password"></el-step>
        </el-steps>
        <el-form status-icon label-width="150px" class="demo-ruleForm">
          <el-form-item label="Verification Code">
            <el-input v-model="code" id="code"></el-input>
          </el-form-item>
          <el-form-item label="New password">
            <el-input id="password" v-model="password" type="password"></el-input>
          </el-form-item>
          <el-form-item label="Confirm Password">
            <el-input v-model="confirm_password" type="password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="warning" @click="previousClick">Previous</el-button>
            <el-button type="primary" @click="changePWDbyCode(code, password)">Submit</el-button>
            <el-button @click="reset('code')">Reset</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { sendCodeEmail, changePWDbyCode } from '@/api/request';
import md5 from 'js-md5';
export default {
  data() {
    return {
      dialogVisible: false,
      flag: 1,
      email: '',
      code: '',
      password: '',
      confirm_password: '',
    };
  },
  props: ['f_email'],
  methods: {
    send_email() {
      this.$confirm('send verification code to this mailbox ?', 'prompt', {
        confirmButtonText: 'confirm',
        cancelButtonText: 'cancel',
      })
        .then(async () => {
          try {
            await sendCodeEmail(this.email);
            this.$message({
              type: 'success',
              message: 'The message has been sent, pay attention to check!',
            });
            this.flag = 0;
          } catch {
            this.$message({
              type: 'error',
              message: 'Mailbox does not exist!',
            });
          }
        })
        .catch(() => {});
    },

    async changePWDbyCode(code, password) {
      if (this.password != this.confirm_password) {
        this.$message({
          type: 'error',
          message: 'Inconsistent passwords!',
        });
        return;
      } else if (this.password == '') {
        this.$message({
          type: 'error',
          message: 'Password cannot be empty!',
        });
        return;
      } else {
        try {
          let pwd = md5(password);
          await changePWDbyCode(this.email, code, pwd);
          this.$message({
            type: 'success',
            message: 'Password modified successfully!',
          });
          this.dialogVisible = false;
        } catch {
          this.$message({
            type: 'error',
            message: 'Invalid verification code!',
          });
        }
      }
    },

    reset(id) {
      this.email = '';
      document.getElementById(id).focus();
    },
    dialogClick() {
      this.dialogVisible = true;
      this.email = this.f_email;
    },
    previousClick() {
      this.flag = 1;
    },
  },
};
</script>

<style scoped>
.demo-ruleForm {
  margin-top: 50px;
  margin-right: 75px;
}
</style>


