<!--  -->
<template>
  <div class="upload-container">
    <el-upload ref="upload" list-type="picture-card" :on-change="onChange" :http-request="submitUpload" :file-list="fileList" action :auto-upload="true">
      <i slot="default" class="el-icon-plus"></i>

      <div slot="file" slot-scope="{ file }">
        <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
        <span class="el-upload-list__item-actions">
          <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
            <i class="el-icon-zoom-in"></i>
          </span>
          <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleDownload(file)">
            <i class="el-icon-download"></i>
          </span>
          <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
            <i class="el-icon-delete"></i>
          </span>
        </span>
      </div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" style="z-index: 9999">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
  </div>
</template>

<script>
import { post } from '@/axios';

import { mapState } from 'vuex';
export default {
  props: {
    fileUrl: {
      type: String
    },
    uploadPath: {
      type: String
    }
  },

  components: {},

  watch: {
    fileUrl: {
      handler(val) {
        if (val) {
          this.fileList = [{ url: this.fileUrl }];
        }
      },
      deep: true
    }
  },

  computed: {
    ...mapState({
      userId: state => state.user.userId
    })
  },

  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      uploadFileForm: new FormData(), //上传文件的form
      fileList: [], //el-upload上传的文件列表,
      dataItemList: [],
      file: {}
      // ordinaryFileUrl:
    };
  },

  methods: {
    //上传文件发生改变时
    onChange(file) {
      this.file = file;
      this.fileList = [file];
    },

    handleRemove(file) {
      this.$refs.upload.clearFiles();
      console.log(file);
    },

    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },

    handleDownload(file) {
      console.log(file);
    },

    async submitUpload({ file }) {
      console.log(file);
      let form = new FormData();
      form.append('pictureFile', file);
      let fileName = await post(`${this.uploadPath}`, form);
      let url = `http://${window.location.host}/r/${this.userId}/projectPicture/${fileName}`;
      console.log(url);
      this.file.staticUrl = url;
      this.$emit('uploadImgResponse', url);
    },
    clear() {
      this.file = {};
      this.fileUrl = '';
    }
  },

  mounted() {
    // this.clear();
  }
};
</script>
<style scoped lang="scss">
.upload-container {
  /deep/ .el-upload--picture-card {
    width: 100px;
    height: 100px;
    line-height: 110px;
  }
  /deep/ .el-upload-list__item {
    width: 100px;
    height: 100px;
  }
}
</style>
