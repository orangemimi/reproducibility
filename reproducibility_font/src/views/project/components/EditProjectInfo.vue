<!-- update -->
<template>
  <div class="main-contain">
    <el-row>
      <el-col :span="24">
        <el-form ref="form" :model="form" label-width="100px" size="small">
          <el-form-item label="Name">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="Introduction">
            <el-input v-model="form.introduction" />
          </el-form-item>
          <!-- <el-form-item label="Purpose">
            <el-input v-model="form.purpose" />
          </el-form-item> -->
          <el-form-item label="Privacy">
            <el-radio-group v-model="form.privacy">
              <el-radio label="public">Public</el-radio>
              <el-radio label="discoverable">Discoverable</el-radio>
              <el-radio label="private">Private</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="Tag">
            <el-input
              v-model="inputTagValue"
              ref="addTagRef"
              size="small"
              @keyup.enter.native="handleInputConfirm"
              @blur="handleInputConfirm"
              style="margin-bottom: 5px"
            >
              <template slot="append">+ New Tag</template>
            </el-input>

            <el-tag :key="tag" v-for="tag in form.tags" closable :disable-transitions="false" @close="handleClose(tag)">
              {{ tag }}
            </el-tag>
          </el-form-item>
          <el-form-item label="Image">
            <!-- <add-image @uploadImgResponse="uploadImgResponse" :fileUrl="form.picture" :uploadPath="'projects/picture'"></add-image> -->
            <add-image @getfile="getfile" class="picture" />
          </el-form-item>
          <el-form-item label="Description">
            <!-- <el-input v-model="form.description" /> -->
            <wang-editor :html="form.description" @textChange="textChange" />
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <div class="title"><el-button @click="submitEdit" type="primary">Submit</el-button></div>
  </div>
</template>

<script>
// import addImage from '_com/AddImage';
import addImage from '_com/AddImage/index1.vue';
import wangEditor from '_com/WangEditor/WangEditor.vue';

import { updateProject, postFile } from '@/api/request';
export default {
  props: {
    projectInfo: {
      type: Object
    }
  },

  components: { addImage, wangEditor },

  // watch: {
  //   projectInfomation: {
  //     handler(val) {
  //       console.log(val);
  //       this.form = val;
  //     },
  //     deep: true
  //   }
  // },

  computed: {},

  data() {
    return {
      form: JSON.parse(JSON.stringify(this.projectInfo)),
      inputTagValue: '',
      pictureFile: ''
    };
  },

  methods: {
    async submitEdit() {
      if (this.pictureFile != '') {
        let temp = new FormData();
        temp.append('datafile', this.pictureFile.raw);
        temp.append('name', this.form.name);
        let pictureData = await postFile(temp);
        this.form.picture = 'http://221.226.60.2:8082/data/' + pictureData.data.data.id;
        console.log(this.form);
      }

      let data = await updateProject(this.projectInfo.id, this.form);
      console.log(data);
      let result = { data: data, flag: true };
      this.$emit('editProjectInfoResponse', result);
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
    },

    getfile(val) {
      this.pictureFile = val;
    },

    textChange(val) {
      this.form.description = val;
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
  .el-tag + .el-tag {
    margin-left: 5px;
  }
}
</style>
