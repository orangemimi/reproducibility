<template>
  <div>
    <div class="main">
      <el-form label-position="top" label-width="80px" :model="form">
        <el-form-item label="Name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
      </el-form>
      <div class="form-item">
        <el-upload
          class="upload"
          action
          :on-change="formHandleChange"
          ref="upload"
          :auto-upload="false"
          :limit="1"
          :on-exceed="handleExceed"
          :on-remove="handRemove"
        >
          <el-button size="small" type="success" style="margin-top: 10px">Select</el-button>
          <template #tip>
            <div class="el-upload__tip">Select the file you want to upload</div>
          </template>
        </el-upload>
      </div>
      <el-button type="primary" class="btn" @click="submit">Submit</el-button>
    </div>
  </div>
</template>

<script>
import { addFileItem } from '@/api/request';
export default {
  props: {
    stack: {
      type: Array
    }
  },
  data() {
    return {
      form: {
        name: ''
      },
      selectedFile: ''
    };
  },
  methods: {
    formHandleChange(file) {
      this.selectedFile = file;
    },
    handleExceed() {
      this.$message.warning(`当前限制选择 1 个文件`);
    },
    handRemove() {
      this.selectedFile = '';
    },
    submit() {
      this.addFileItem();
    },
    async addFileItem() {
      if (this.selectedFile != '') {
        let formData = new FormData();
        formData.append('name', this.form.name);
        formData.append('parent', this.stack[this.stack.length - 1]);
        formData.append('storey', this.stack.length - 1);
        formData.append('datafile', this.selectedFile.raw);
        let data = await addFileItem(formData);
        this.$emit('uploadData', data);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.main {
  height: 300px;
  width: 100%;
  position: relative;
  .form-item {
    text-align: center;
  }
  .btn {
    position: absolute;
    bottom: 10px;
    margin-left: calc(50% - 40px);
  }
}
</style>
