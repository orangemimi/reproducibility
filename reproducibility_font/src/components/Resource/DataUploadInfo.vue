<!-- data upload information -->
<template>
  <div class="main">
    <el-tabs v-model="activeName" size="mini">
      <el-tab-pane label="Upload File" name="file">
        <data-upload-form :formType="'file'" :initFormData="initFormData"></data-upload-form>
      </el-tab-pane>
      <el-tab-pane label="Parameter" name="parameter" style="height:200px">
        <data-upload-form :formType="'parameter'" :initFormData="initFormData"></data-upload-form>
      </el-tab-pane>
      <el-tab-pane label="Shared File" name="sharedFile">
        <data-upload-form :formType="'sharedFile'" :initFormData="initFormData"></data-upload-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import dataUploadForm from './DataUploadForm.vue';
// import addImage from '_com/AddImage';

export default {
  components: {
    dataUploadForm
  },
  props: {
    initFormData: {
      type: Object
    }
  },

  data() {
    return {
      activeName: 'file'
      // form: {}
    };
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  height: 100%;
  width: 100%;
}
</style>
