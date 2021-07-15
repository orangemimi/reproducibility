<!-- data upload information -->
<template>
  <div>
    <el-form ref="form" :model="form" label-width="100px">
      <el-form-item label="Alia">
        <el-input v-model="form.alia"></el-input>
      </el-form-item>
      <el-form-item label="Description">
        <el-input v-model="form.description"></el-input>
      </el-form-item>
      <el-form-item label="Source">
        <el-input v-model="form.source"></el-input>
      </el-form-item>
      <el-form-item label="Privacy">
        <el-radio-group v-model="form.privacy">
          <el-radio label="public">Public</el-radio>
          <el-radio label="discoverable">Discoverable</el-radio>
          <el-radio label="private">Private</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="Image">
<<<<<<< HEAD:reproducibility_font/src/components/Resource/DataEdit.vue
        <add-image @uploadImgResponse="uploadImgResponse" :uploadPath="'projectResources/picture'"></add-image>
=======
        <add-image
          @uploadImgResponse="uploadImgResponse"
          :uploadPath="'resources/picture'"
        ></add-image>
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Resource/DataEdit.vue
      </el-form-item>
      <el-form-item label="Upload">
        <data-upload></data-upload>
      </el-form-item>
    </el-form>
    <div>
      <el-button @click="submit">Submit</el-button>
    </div>
  </div>
</template>

<script>
// import { post } from '@/axios';
import { updateResources } from '@/api/request';
import addImage from '_com/AddImage';
import dataUpload from './DataUpload';
export default {
  components: { addImage, dataUpload },

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
          this.getAllRecords();
        }
      }
    }
  },

  computed: {},

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
        userUpload: ''
      }
    };
  },

  methods: {
    uploadImgResponse(val) {
      this.form.picture = val;
    },

    async submit() {
<<<<<<< HEAD:reproducibility_font/src/components/Resource/DataEdit.vue
      this.form.userUpload = true;
      let data = await updateRequest(this.form);
=======
      this.form.isDirect = true;
      let data = await updateResources(this.form);
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Resource/DataEdit.vue

      console.log(data);
      this.$notify({
        title: 'Success',
        message: 'You have upload the file successfully!',
        type: 'success'
      });
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped></style>
