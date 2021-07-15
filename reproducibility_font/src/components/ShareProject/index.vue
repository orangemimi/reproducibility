<!-- update -->
<template>
  <div class="main-contain">
    <el-row>
      <el-col :span="24">
        <!-- <el-input v-model="inputValue" placeholder="Search by email" size="medium"></el-input> -->
        <el-autocomplete
          v-model="inputValue"
          :fetch-suggestions="querySearchAsync"
          placeholder="Search by email"
          :trigger-on-focus="false"
          @select="handleSelect"
        ></el-autocomplete>
      </el-col>
    </el-row>
    <div class="title"><el-button @click="sendEmail" type="primary">Send</el-button></div>
  </div>
</template>

<script>
import { getUserInfoLike, saveNotices, saveEmails } from '@/api/request';
export default {
  props: {
    projectInfo: {
      type: Object
    }
  },

  computed: {},

  data() {
    return {
      //   form: JSON.parse(JSON.stringify(this.projectInfo)),
      inputValue: '', //输入框的值
      selectUser: {},
      searchList: [] //匹配的值
    };
  },

  methods: {
    async getRecipientInfo(email) {
      let form = new FormData();
      form.append('value', email);

      let data = await getUserInfoLike(email);
      console.log(data);
      return data;
    },

    async querySearchAsync(queryString, callback) {
      let data = await this.getRecipientInfo(queryString);
      callback(data);
    },

    handleSelect(item) {
      this.selectUser = item;
    },

    async sendEmail() {
      let json = { recipient: this.selectUser.email };
      let data = await saveEmails(json);
      console.log(data);
      let notice = { recipientId: this.selectUser.userId };
      let send = await saveNotices(notice);
      console.log(send);
    }
  }
};
</script>
<style lang="scss" scoped>
.main-contain {
  //   width: 100%;
  .title {
    text-align: right;
    // width: 100%;
  }
}
</style>
