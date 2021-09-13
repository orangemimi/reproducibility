<template>
  <div>
    <el-row>Register</el-row>
    <el-row>
      <div class="register-form">
        <el-form
          ref="registerForm"
          :rules="registerRules"
          :model="registerForm"
          label-width="150px"
          label-position="right"
        >
          <el-form-item label="Name" prop="name" required>
            <el-input v-model="registerForm.name" />
          </el-form-item>
          <el-form-item label="E-mail" prop="email" required>
            <el-input v-model="registerForm.email" />
          </el-form-item>
          <el-form-item label="Password" prop="password" required>
            <el-input v-model="registerForm.password" type="password" autocomplete="off" />
          </el-form-item>
          <el-form-item label="Confirm Password" prop="checkPassword" required>
            <el-input v-model="registerForm.checkPassword" type="password" autocomplete="off" />
          </el-form-item>
          <el-form-item label="Organization" prop="organization">
            <el-input v-model="registerForm.organization" />
          </el-form-item>
        </el-form>
        <el-button @click="toLogin">Login</el-button>
        <el-button @click="reset">Reset</el-button>
        <el-button @click="submit">Submit</el-button>
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
    toLogin() {},
    reset() {},
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
  width: 500px;
}
</style>
