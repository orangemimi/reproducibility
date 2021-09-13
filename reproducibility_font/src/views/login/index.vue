<template>
  <div class="main">
    <div class="login-box">
      <div class="radio-group">
        <div class="radio-btn" :class="{ active: tab == 'login' }" @click="tab = 'login'">Login</div>
        <div class="radio-btn" :class="{ active: tab == 'register' }" @click="tab = 'register'">Register</div>
      </div>
      <el-form :model="formItem" status-icon ref="formItem" label-width="80px">
        <el-form-item label="E-mail" prop="pass">
          <el-input v-model="formItem.email" placeholder="Please enter your email"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="pass">
          <el-input v-model="formItem.password" type="password" placeholder="Please enter the passward"></el-input>
        </el-form-item>
      </el-form>
      <SendEmailAndChangePWD v-bind:f_email="formItem.email" style="text-align: center;"/>
    </div>

    <div class="btn-wrapper">
      <span class="btn" @click="handleClick">Enter</span>
    </div>
    
  </div>
</template>

<script>
// import { post } from '@/axios';
import { mapActions } from 'vuex';
import SendEmailAndChangePWD from './components/SendEmailAndChangePWD.vue';
// import md5 from 'js-md5';
// import jwtDecode from 'jwt-decode';
export default {
  data() {
    return {
      tab: 'login',
      formItem: {
        email: '',
        password: '',
      },
    };
  },
  components: { SendEmailAndChangePWD },
  methods: {
    ...mapActions({ logout: 'user/handleLogOut' }),
    async handleClick() {
      try {
        await this.logout();
        //如果需要向actions里面传值就手动调用,不能使用this.方法名 调用
        await this.$store.dispatch('user/handleLogIn', this.formItem);

        let redirect = decodeURIComponent(this.$route.query.redirect || '/');
        if (redirect != undefined) {
          this.$router.push({
            path: redirect,
          });
        } else {
          this.$router.push({ name: 'Home' });
        }
      } catch (error) {
        this.$throw(error);
      }
    },
    submit() {
      this.handleClick();
    },
  },
  created: function () {
    var _this = this;
    document.onkeydown = function () {
      let key = window.event.keyCode;
      if (key == 13) {
        _this.submit();
      }
    };
  },
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
