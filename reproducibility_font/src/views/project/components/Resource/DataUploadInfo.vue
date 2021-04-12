<!-- data upload information -->
<template>
  <div class="main">
    <!-- <el-button @click="test">get computa</el-button> -->
    <el-form ref="form" :model="form" label-width="100px">
      <el-form-item label="Description">
        <el-input v-model="form.description"></el-input>
      </el-form-item>

      <el-form-item label="Privacy">
        <el-radio-group v-model="form.privacy">
          <el-radio label="public">Public</el-radio>
          <el-radio label="discoverable">Discoverable</el-radio>
          <el-radio label="private">Private</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="Image">
        <add-image
          @uploadImgResponse="uploadImgResponse"
          :uploadPath="'resources/picture'"
        ></add-image>
      </el-form-item>
    </el-form>
    <div class="drag">
      <el-upload
        drag
        action
        :auto-upload="true"
        :file-list="fileList"
        :show-file-list="true"
        ref="upload"
        :http-request="submitUpload"
        :on-remove="handleRemove"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          Drag a file here to upload or
          <em>Click to upload</em>
        </div>
      </el-upload>
    </div>
    <div class="submit">
      <el-button @click="submit">Submit</el-button>
    </div>
  </div>
</template>

<script>
import { post } from '@/axios';
import { deleteDataItemsById } from '@/api/request';
import { saveDataItems } from '@/api/request';
import addImage from '_com/AddImage';
export default {
  components: {
    addImage
  },

  props: {
    projectInfo: {
      type: Object
    }
  },

  watch: {
    projectInfo: {
      handler(val) {
        if (val != null) {
          this.projectId = val.id;
        }
      }
    }
  },

  data() {
    return {
      projectId: '',
      form: {
        alia: '',
        name: '',
        type: '',
        description: '',
        privacy: 'discoverable',
        source: '',
        thumbnail: '',
        isDirect: ''
      },

      uploadFileForm: new FormData(), //上传文件的form
      fileList: [], //el-upload上传的文件列表,
      dataItemList: [],
      //   file: {},
      ops: {
        bar: {
          background: '#808695'
        }
      },
      nodeInfo: this.nodeInformation
    };
  },

  methods: {
    uploadImgResponse(val) {
      this.form.picture = val;
    },

    handleRemove(file) {
      this.dataItemList = this.dataItemList.filter(item => {
        return item.name != file.name;
      });
    },

    async remove(resource) {
      await deleteDataItemsById(resource.id);
      // await del(`/dataItems/${resource.id}`);
      this.dataItemList.splice(
        this.dataItemList.findIndex(item => item.id === resource.id),
        1
      );
    },

    //上传文件到服务器
    async submitUpload(param) {
      this.uploadFileForm = new FormData();
      this.uploadFileForm.append('file', param.file);
      console.log(this.uploadFileForm.get('file'));

      let { data } = await post(`/dataContainer/uploadSingle`, this.uploadFileForm);
      console.log(data);
      this.form.name = data.file_name;
      this.form.suffix = this.getSuffix(param.file.name);
      this.form.fileSize = this.renderSize(param.file.size);

      this.form.url = `http://221.226.60.2:8082/data/${data.id}`;
      this.form.projectId = this.projectInfo.id;
    },

    getSuffix(filename) {
      let index = filename.lastIndexOf('.');
      return filename.substr(index + 1);
    },

    renderSize(value) {
      if (null == value || value == '') {
        return '0 Bytes';
      }
      var unitArr = new Array('Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB');
      var index = 0,
        srcsize = parseFloat(value);
      index = Math.floor(Math.log(srcsize) / Math.log(1024));
      var size = srcsize / Math.pow(1024, index);
      //  保留的小数位数
      size = size.toFixed(2);
      return size + unitArr[index];
    },

    //data item保存到数据库
    //上传数据直接保存到dataItems,即用户的资源可全部显示，之后选择所需的数据，之后保存选择的数据之后 保存到resource数据表里面去
    async submit() {
      this.form.isDirect = true;
      await saveDataItems(this.form);
      // let data = await post(`/dataItems`, this.form);
      // console.log(data);
      this.$notify({
        title: 'Success',
        message: 'You have upload the file successfully!',
        type: 'success'
      });

      this.$emit('uploadSuccess', true);
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  height: 100%;
  width: 100%;
  // position: relative;
  .drag {
    // margin-left: 20%;
    /deep/ .el-icon-upload {
      margin: 0;
    }

    /deep/ .el-upload-dragger {
      width: calc(37vw);
      height: 90px;
    }
  }
  .submit {
    margin-left: 88.5%;
  }
}
</style>
