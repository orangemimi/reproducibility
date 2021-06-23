<!-- data upload information -->
<template>
  <div class="main">
    <el-tabs v-model="activeName" size="mini">
      <el-tab-pane label="Upload File" name="file">
        <data-upload-form :formType="'file'"></data-upload-form>
      </el-tab-pane>
      <el-tab-pane label="Parameter" name="parameter" style="height:200px">
        <data-upload-form :formType="'parameter'"></data-upload-form>
      </el-tab-pane>
      <el-tab-pane label="Shared File" name="sharedFile">
        <data-upload-form :formType="'sharedFile'"></data-upload-form>
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

  data() {
    return {
      activeName: 'file'
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
