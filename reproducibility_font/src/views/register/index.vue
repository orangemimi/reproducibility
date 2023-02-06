<template>
  <div>
    <!-- <el-row>Register</el-row> -->
    <el-row>
      <div class="register-form">
        <el-form ref="registerForm" :rules="registerRules" :model="registerForm" label-width="150px" label-position="right">
          <el-form-item label="Name" prop="name" required label-width="90px">
            <el-input v-model="registerForm.name" placeholder="Please enter your name" ref="name" />
          </el-form-item>
          <el-form-item label="E-mail" prop="email" required label-width="90px">
            <el-input v-model="registerForm.email" placeholder="Please enter your email" />
          </el-form-item>
          <el-form-item label="Password" prop="password" required label-width="90px">
            <el-input v-model="registerForm.password" type="password" autocomplete="off" placeholder="Please enter your password" />
          </el-form-item>
          <el-form-item label="Confirm" prop="checkPassword" required label-width="90px">
            <el-input v-model="registerForm.checkPassword" type="password" autocomplete="off" placeholder="Confirm your password" />
          </el-form-item>
          <el-form-item label="Organization" prop="organization" label-width="90px">
            <el-input v-model="registerForm.organization" placeholder="Please enter your organization" />
          </el-form-item>
        </el-form>
        <div class="btn-wrapper">
          <span @click="reset" class="btn" style="margin-right: 5px;">Reset</span>
          <span @click="submit" class="btn" style="margin-left: 5px;">Submit</span>
        </div>
      </div>
    </el-row>
  </div>
</template>

<script>
import md5 from 'js-md5';
import { saveUser } from '@/api/request';
export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter the password!'));
      } else {
        if (this.registerForm.checkPassword !== '') {
          this.$refs.registerForm.validateField('checkPassword');
        }
        callback();
      }
    };

    var validateCheckPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter the password again!'));
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };

    return {
      registerForm: {
        name: '',
        email: '',
        password: '',
        checkPassword: '',
        organization: ''
      },

      registerRules: {
        password: [{ validator: validatePass, trigger: 'blur' }],
        checkPassword: [{ validator: validateCheckPass, trigger: 'blur' }]
      }
    };
  },

  methods: {
    reset() {
      this.registerForm.name = '';
      this.registerForm.email = '';
      this.registerForm.password = '';
      this.registerForm.checkPassword = '';
      this.registerForm.organization = '';
      this.$refs['name'].focus();
    },
    submit() {
      this.$refs['registerForm'].validate(async valid => {
        if (valid) {
          let form = {
            name: this.registerForm.name,
            email: this.registerForm.email,
            organization: this.registerForm.organization,
            password: md5(this.registerForm.password), //前端加密
            joinedProjects: [],
            createdProjects: []
          };
          await saveUser(form);
          this.$notify({
            title: 'Success',
            message: 'Register successfully!',
            type: 'success'
          });
        } else {
          this.$notify.error({
            title: 'Error',
            message: 'Register failed!'
          });
        }
      });
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.register-form {
  margin: 0 auto;
}
.btn-wrapper {
  margin-top: 40px;
  text-align: center;
  a {
    text-decoration: none;
    color: #ffffff;
  }
  .btn {
    padding: 12px 32px;
    background: rgba(32, 160, 241, 0.5);
    color: #ffffff;
    transition: rgba(0, 0, 0, 0.5) 0.3s ease;
    font-size: 14px;
    border-radius: 50px;
    &:hover {
      background: #409eff;
      cursor: pointer;
    }
  }
}
</style>
