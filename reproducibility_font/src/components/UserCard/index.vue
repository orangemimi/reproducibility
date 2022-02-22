<!-- user card creator member different -->
<template>
  <div class="user">
    <div v-if="user.role == 'builder'" class="builder">
      <div class="block">
        <el-avatar :size="50" :src="user.avatar == '' ? '' : 'http://172.21.212.103:8088/userServer' + user.avatar" :title="user.name" @click.native="click">{{user.name}}</el-avatar>
      </div>
      <div class="text" :title="user.name" @click="click">{{ user.name }}</div>
    </div>
    <div v-else class="member">
      <!-- <div class="member"> -->
      <div class="block">
        <el-avatar :size="50" :src="user.avatar == '' ? '' : 'http://172.21.212.103:8088/userServer' + user.avatar" :title="user.name" @click.native="click"></el-avatar>
      </div>
      <div class="text" :title="user.name" @click="click">{{ user.name }}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
    },
  },

  computed: {},

  data() {
    return {};
  },

  methods: {
    click() {
      console.log(1)
      this.$router.push({
        path: `/Homepage/${this.user.id}`,
      });
    },
  },

  mounted() {
    console.log(this.user);
  },
};
</script>
<style lang="scss" scoped>
.user {
  // height: 50px;
  // line-height: 50px;
  // width: 280px;
  // padding: 0 15px;
  // margin-bottom: 5px;
  // border: 1px solid $btnBorder;
  // vertical-align: middle;
  // font-family: inherit;

  .text {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    width: 60px;
  }
  // .builder {
  // }
  // .member {
  // }
}
.el-avatar:hover {
  cursor: pointer;
}
.text:hover {
  cursor: pointer;
}
</style>
