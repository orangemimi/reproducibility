<template>
  <div style="height: 100%">
    <el-card shadow="never" class="card_contain">
      <div class="container_back">
        Files you have
        <br />
        updated to
        <br />
        apply to
        <br />
        this project
      </div>
      <vue-scroll :ops="ops" style="height: 100%; width: 270px">
        <div v-for="(item, index) in dataItemList" :key="index" class="files">
          <div class="file_name">{{ item.name }}</div>
          <i class="el-icon-close" @click="remove(item)"></i>
        </div>
      </vue-scroll>
    </el-card>
  </div>
</template>
<script>
// import { get, del, post } from '@/axios';
import { mapState } from 'vuex';
export default {
  props: {
    itemList: {
      type: Array
    }
  },

  watch: {
    itemList: {
      handler(val) {
        this.dataItemList = val;
      },
      deep: true
    }
  },

  computed: {
    ...mapState({
      role: state => state.permission.role,
      name: state => state.user.name,
      userId: state => state.user.userId
    })
  },

  data() {
    return {
      dataItemList: [],

      ops: {
        bar: {
          background: '#808695'
        }
      }
    };
  },

  methods: {
    // async getDataItem() {
    //   let dataItem = await get(`/dataItems/${this.projectId}`);
    //   this.dataItemList = dataItem.filter(item => {
    //     return item.isDirect == true;
    //   });
    // },
    // download() {
    //   let url = `/dataContainer/download/${this.eventUrl.value}`;
    //   window.open(url);
    // },
    // //上传文件发生改变时
    // onChange(file, fileList) {
    //   this.file = file;
    //   this.fileList = fileList;
    // },
    // handleRemove(file) {
    //   this.dataItemList = this.dataItemList.filter(item => {
    //     return item.name != file.name;
    //   });
    // },
    // async remove(resource) {
    //   await del(`/dataItems/${resource.id}`);
    //   this.dataItemList.splice(
    //     this.dataItemList.findIndex(item => item.id === resource.id),
    //     1
    //   );
    // },
    // //上传文件到服务器
    // async submitUpload() {
    //   this.uploadFileForm = new FormData();
    //   this.uploadFileForm.append('file', this.file.raw);
    //   let uid = await post(`/dataContainer/uploadSingle`, this.uploadFileForm);
    //   let list = {
    //     userId: this.userInfo.userId,
    //     pid: this.projectId,
    //     url: `http://221.226.60.2:8082/data?uid=${uid}`,
    //     name: this.file.name,
    //     isDirect: true, //if true -- 是直接上传的数据    --false是中间数据
    //     stepBindId: this.stepInfo.stepId,
    //     stepBindName: this.stepInfo.name
    //   };
    //   let data = await post(`/dataItems`, list);
    //   this.dataItemList.push(data);
    // }
  },
  mounted() {
    // this.getDataItem();
  }
};
</script>

<style lang="scss" scoped>
.upload_btn {
  height: 40px;
  font-size: 14px;
}
.card_contain {
  height: 100%;
  width: 300px;
  clear: both;
  >>> .el-card__body {
    padding: 15px;
  }
  .container_back {
    height: 100%;
    position: absolute;
    font-weight: 600;
    font-size: 35px;
    color: rgba(153, 153, 153, 0.315);
    user-select: none;
    text-align: center;
    // white-space: normal;
    top: 120px;
    line-height: 40px;
    width: 260px;
  }
}
.submitBtn {
  position: absolute;
  top: 0px;
  left: 110px;
}

.file_name {
  float: left;
  width: 240px;
  // padding-left: 5px;
}
.files {
  margin: 5px 0;
  font-size: 14px;
  padding: 3px 0 3px 3px;
}
.files:hover {
  cursor: pointer;
  transition: all 0.2s ease-out;
  color: #176edf;
  background-color: rgba(120, 159, 231, 0.1);
}
</style>
