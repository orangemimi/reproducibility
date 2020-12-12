<template>
  <div>
    <div class="login-box">
      <div class="radio-group">
        <div class="radio-btn" :class="{ active: tab == 'login' }" @click="tab = 'login'">
          Login
        </div>
        <div class="radio-btn" :class="{ active: tab == 'register' }" @click="tab = 'register'">
          Register
        </div>
      </div>
      <el-form :model="formItem" status-icon ref="formItem" label-width="80px">
        <el-form-item label="E-mail" prop="pass">
          <el-input v-model="formItem.email" placeholder="Please enter your name"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="pass">
          <el-input v-model="formItem.password" type="password" placeholder="Please enter the passward"></el-input>
        </el-form-item>
      </el-form>
    </div>

    <div class="btn-wrapper">
      <span class="btn" @click="handleClick">Enter</span>
    </div>
  </div>
</template>

<script>
import { post } from '@/axios';
import { mapActions } from 'vuex';
import md5 from 'js-md5';
import jwtDecode from 'jwt-decode';
export default {
  data() {
    return {
      tab: 'login',
      formItem: {
        email: '',
        password: ''
      }
    };
  },
  methods: {
    ...mapActions(['handleLogOut', 'handleLogIn']),
    async handleClick() {
      try {
        await this.handleLogOut();
        let form = new FormData();
        form.append('email', this.formItem.email);
        form.append('password', md5(this.formItem.password)); //前端加密
        let data = await post(`/users/login`, form); //name,password,type,userId
        console.log(data);

        const decode = jwtDecode(data.token);
        console.log(decode);

        await this.handleLogIn({
          userInfo: data.userInfo,
          token: data.token
        });
        let redirect = decodeURIComponent(this.$route.query.redirect || '/');
        if (redirect != undefined) {
          this.$router.push({
            path: redirect
          });
        } else {
          this.$router.push({ name: 'Home' });
        }
      } catch (error) {
        this.$throw(error);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.login-box {
  background: #ffffff;
  width: 400px;
  margin: 20px auto 40px;
  padding: 0 40px 36px;
  box-shadow: $normalBoxShadow;
  .radio-group {
    text-align: center;
    .radio-btn {
      display: inline-block;
      padding: 10px 20px;
      margin: 10px 10px;
      color: #999999;
      &.active {
        color: #212121;
        border-bottom: 2px solid #212121;
      }
    }
  }
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
    background: rgba(0, 0, 0, 0.5);
    color: #ffffff;
    transition: background 0.3s ease;
    font-size: 14px;
    border-radius: 50px;
    &:hover {
      background: #409eff;
      cursor: pointer;
    }
  }
}
</style>
