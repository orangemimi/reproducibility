<template>
  <div class="main">
    <div class="backgroud">
      <img src="@/assets/images/bg_model.jpg" />
    </div>
    <div class="login">
      <div class="login-box">
        <div class="radio-group">
          <div class="radio-btn" :class="{ active: tab == 'login' }" @click="tab = 'login'">Login</div>
          <div class="radio-btn" :class="{ active: tab == 'register' }" @click="tab = 'register'">Register</div>
        </div>
        <div v-show="tab == 'login'">
          <el-form :model="formItem" status-icon ref="formItem" label-width="80px" class="input">
            <el-form-item label="E-mail" prop="pass" class="test">
              <el-input v-model="formItem.email" placeholder="Please enter your email"></el-input>
            </el-form-item>
            <el-form-item label="Password" prop="pass">
              <el-input v-model="formItem.password" type="password" placeholder="Please enter the passward" @keyup.enter.native="handleClick"></el-input>
            </el-form-item>
          </el-form>
          <SendEmailAndChangePWD v-bind:f_email="formItem.email" style="text-align: center" />
          <div class="btn-wrapper">
            <span class="btn" @click="handleClick">Enter</span>
          </div>
        </div>
        <div v-show="tab == 'register'">
          <register />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { post } from '@/axios';
import { mapActions } from 'vuex';
import SendEmailAndChangePWD from './components/SendEmailAndChangePWD.vue';
import register from '../register/index.vue';
import { getProjectById } from '@/api/request';
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
  components: { SendEmailAndChangePWD, register },
  methods: {
    ...mapActions({ logout: 'user/handleLogOut' }),
    async handleClick() {
      try {
        await this.logout();
        //如果需要向actions里面传值就手动调用,不能使用this.方法名 调用
        await this.$store.dispatch('user/handleLogIn', this.formItem);

        let redirect = decodeURIComponent(this.$route.query.redirect || '/');
        if (redirect != undefined) {
          let arr = redirect.split('/');
          if (arr.length >= 3 && arr[1] == 'project') {
            await this.judgeRole(arr[2]);
          }
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

    async judgeRole(projectId) {
      let projectInfo = await getProjectById(projectId);
      let userId = this.$store.state.user.userId;
      await this.$store.dispatch('permission/getRole', { project: projectInfo, userId: userId });
    },
  },
  mounted() {
    if (this.$route.params.tab == 'register') {
      this.tab = 'register';
    }
  },
};
</script>

<style lang="scss" scoped>
.main {
  position: relative;
  img {
    width: 100%;
    object-fit: cover;
    height: 100vh;
    vertical-align: middle;
  }
  .login {
    position: absolute;
    left: calc((100% - 500px) / 2);
    top: 25%;
  }
}

.login-box {
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 4px;
  width: 500px;
  margin: 20px auto 40px;
  padding: 0 40px 36px;
  box-shadow: $normalBoxShadow;
  .radio-group {
    text-align: center;
    cursor: pointer;
    .radio-btn {
      display: inline-block;
      padding: 10px 20px;
      margin: 10px 10px;
      color: #999999;
      &.active {
        color: white;
        border-bottom: 2px solid white;
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
/deep/.el-form-item__label {
  color: white;
}
</style>
