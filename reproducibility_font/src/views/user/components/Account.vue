<!--  -->
<template>
  <div>
    <div style="text-align: center;">
      <avatar
        :username="avatar != 'undefined' && avatar != 'null' && avatar != undefined && avatar != '' ? avatar : this.$store.state.user.name"
        :size="150"
        style="margin: 20px auto 0px"
      ></avatar>
      <el-button class="button" type="text">Edit</el-button>
    </div>
    <div class="container">
      <my-account />
    </div>
  </div>
</template>

<script>
import Avatar from 'vue-avatar';
import MyAccount from '../../../components/Account/MyAccount.vue';
export default {
  components: { Avatar, MyAccount },
  watch: {},

  computed: {},

  data() {
    return {
      avatar: this.$store.state.user.avatar
    };
  },

  methods: {

  },

  mounted() {
  },
};
</script>
<style lang="scss" scoped>
.container {
  margin: 10px auto;
  width: 40%;
}
.button {
  font-size: 18px;
}
</style>
