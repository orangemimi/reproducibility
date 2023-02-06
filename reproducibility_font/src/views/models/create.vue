<!-- create model resource -->
<template>
  <div class="main-contain">
    <el-row>
      <el-col :span="24">
        <el-form ref="form" :model="form" label-width="90px" size="small">
          <el-form-item label="Name">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="Description">
            <el-input v-model="form.description" />
          </el-form-item>
          <el-form-item label="Doi">
            <el-input v-model="form.doi" />
          </el-form-item>
          <el-form-item label="Privacy">
            <el-radio-group v-model="form.privacy">
              <el-radio label="public">Public</el-radio>
              <el-radio label="discoverable">Discoverable</el-radio>
              <el-radio label="private">Private</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="Tags">
            <el-input
              v-model="inputTagValue"
              ref="addTagRef"
              size="small"
              @keyup.enter.native="handleInputConfirm"
              @blur="handleInputConfirm"
              style="margin-bottom:5px"
            >
              <template slot="append">
                + New Tag
              </template>
            </el-input>

            <el-tag :key="tag" v-for="tag in form.tags" closable :disable-transitions="false" @close="handleClose(tag)">
              {{ tag }}
            </el-tag>
          </el-form-item>
          <el-form-item label="Source">
            <el-input v-model="form.source" />
          </el-form-item>
          <el-form-item label="Image">
            <add-image @uploadImgResponse="uploadImgResponse" :uploadPath="'models/picture'"></add-image>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <div class="title"><el-button @click="addModel">Create</el-button></div>
  </div>
</template>

<script>
import addImage from '_com/AddImage';
import { post } from '@/axios';
export default {
  components: { addImage },

  watch: {},

  computed: {},

  data() {
    return {
      form: {
        name: '',
        description: '',
        doi: '',
        privacy: '',
        tags: [],
        thumbnail: '',
        source: '',
        type: 'service'
      },
      inputTagValue: ''
    };
  },

  methods: {
    async addModel() {
      let data = await post(`/modelItems`, this.form);
      console.log(data);
      this.$notify({
        title: 'Success',
        message: 'You have add the model service successfully!',
        type: 'success'
      });
    },

    uploadImgResponse(val) {
      this.form.thumbnail = val;
    },

    handleClose(val) {
      this.form.tags.splice(this.form.tags.indexOf(val), 1);
    },

    handleInputConfirm() {
      let inputTagValue = this.inputTagValue;
      if (inputTagValue) {
        this.form.tags.push(inputTagValue);
      }
      this.inputTagValue = '';
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main-contain {
  //   width: 100%;
  .title {
    text-align: center;
    // width: 100%;
  }
}
</style>
