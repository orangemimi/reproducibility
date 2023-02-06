<template>
  <el-card class="box-card">
    <div class="card-header">
      <span>Personal Infomation</span>
      <edit-userinfo :user="user" :form="form" />
    </div>
    <div v-for="(value, key) in form" :key="key">
      <div v-if="user[key] != undefined" class="item">{{ key + ': ' + user[key] }}</div>
      <div v-else class="item">{{ key + ': ' + value }}</div>
    </div>
    <div class="changePWD"><change-password /></div>
  </el-card>
</template>

<script>
import EditUserinfo from './EditUserinfo.vue';
import ChangePassword from './ChangePassword.vue';
import { getUserinfo } from '@/api/request';
export default {
  data() {
    return {
      user: localStorage,
      form: {
        name: '',
        email: '',
        // title: '',
        // organizations: '',
        // domain: '',
        phone: '',
        country: '',
        state: '',
        city: ''
      }
    };
  },
  methods: {
    async init() {
      let temp = await getUserinfo();
      this.user = temp.data;
    }
  },
  components: { EditUserinfo, ChangePassword },

  mounted() {
    this.init();
  }
};
</script>

<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bolder;
  font-size: 24px;
}
.item {
  padding: 13px 0;
}
.changePWD {
  text-align: center;
}
</style>
