<template>
  <div class="mian">
    <el-tabs tab-position="left" style="height: 100%">
      <el-tab-pane label="Test" class="text">
        <el-input v-model="textGroup.title" placeholder="Please enter the title" style="width: 95%; margin-bottom: 10px"></el-input>
        <el-input type="textarea" :rows="9" placeholder="Please enter the content" v-model="textGroup.content" style="width: 95%" resize="none"></el-input>
        <el-button type="primary" round style="margin-top: 10px" @click="textSubmit">submit</el-button>
      </el-tab-pane>
      <el-tab-pane label="Picture" class="picture">
        <el-form label-position="right" label-width="100px" :model="groupForm">
          <el-form-item label="Group Name">
            <el-input v-model="groupForm.name" style="width: 95%" placeholder="Please enter the name"></el-input>
          </el-form-item>
          <el-form-item label="Origin">
            <el-input v-model="groupForm.origin" style="width: 95%" placeholder="Please enter the origin"></el-input>
          </el-form-item>
          <el-form-item label="Remarks">
            <el-input
              type="textarea"
              style="margin-bottom: 10px; width: 95%"
              :rows="2"
              placeholder="Remarks"
              v-model="groupForm.remarks"
              resize="none"
            ></el-input>
          </el-form-item>
        </el-form>

        <vue-scroll style="height: 200px">
          <add-image @scenarioUser="scenarioUser"></add-image>
        </vue-scroll>
        <div class="picture-btn">
          <el-button type="primary" round @click="pictureSubmit">Submit</el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Video"></el-tab-pane>
      <el-tab-pane label="Form" class="form">
        <el-input v-model="formGroup.title" placeholder="Please enter the title" style="width: 95%; margin-bottom: 10px"></el-input>
        <el-input v-model="formGroup.origin" placeholder="Please enter the origin" style="width: 95%; margin-bottom: 10px"></el-input>
        <el-input type="textarea" :rows="5" placeholder="Please enter the content" v-model="formGroup.description" style="width: 95%" resize="none"></el-input>
        <div class="form-item">
          <vue-scroll style="height: 150px; width:70%; margin: 0 auto;">
            <el-upload class="upload-demo" action :on-change="formHandleChange" :auto-upload="false" ref="form" :on-remove="formHandleChange">
              <el-button size="small" type="success" style="margin-top: 10px;">Select</el-button>
              <template #tip>
                <div class="el-upload__tip">Select the form you want to upload</div>
              </template>
            </el-upload>
          </vue-scroll>
        </div>
        <el-button type="primary" round style="margin-top: 10px" @click="formSubmit">submit</el-button>
      </el-tab-pane>
      <el-tab-pane label="Map">Map</el-tab-pane>
      <el-tab-pane label="Flow chart">Flow chart</el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { addTextGroup, addPictureGroup, addFormGroup } from '@/api/request';

import addImage from '_com/AddImage/index1.vue';
export default {
  data() {
    return {
      textGroup: {
        content: '',
        title: '',
      },

      groupForm: {
        origin: '',
        name: '',
        remarks: '',
      },
      pictureList: [],

      formGroup: {
        title: '',
        origin: '',
        description: '',
        formList: [],
      },
    };
  },
  components: {
    addImage,
  },
  methods: {
    formHandleChange(file, fileList) {
      this.formGroup.formList = fileList
    },
    scenarioUser(fileList) {
      console.log(fileList);
      this.pictureList = fileList;
    },
    async addTextGroup() {
      let form = JSON.parse(JSON.stringify(this.textGroup));
      form.projectId = this.$route.params.id;
      let data = await addTextGroup(form);
      console.log(data);
    },
    textSubmit() {
      this.addTextGroup();
    },
    async addPictureGrop() {
      let formData = new FormData();
      formData.append('groupName', this.groupForm.name);
      formData.append('origin', this.groupForm.origin);
      formData.append('remarks', this.groupForm.remarks);
      formData.append('projectId', this.$route.params.id);
      formData.append('datafile', this.pictureList[0].raw);
      let data = await addPictureGroup(formData);
      console.log(data);
    },
    pictureSubmit() {
      this.addPictureGrop();
    },
    async addFormGroup() {
      let formData = new FormData();
      formData.append('title', this.formGroup.title);
      formData.append('origin', this.formGroup.origin);
      formData.append('description', this.formGroup.description);
      formData.append('datafile', this.formGroup.formList[0].raw);
      formData.append('projectId', this.$route.params.id);
      let data = await addFormGroup(formData);
      console.log(data);
    },
    formSubmit() {
      this.addFormGroup();
    },
  },
};
</script>

<style lang="scss" scoped>
.main {
  .text {
    text-align: center;
   
  }
  .picture {
    .picture-btn {
      text-align: center;
    }
  }
  .form {
    text-align: center;
    .el-upload__tip {
      font-weight: 500;
    }
  }
}
</style>