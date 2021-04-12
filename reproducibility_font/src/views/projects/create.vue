<!--  -->
<template>
  <div class="main-contain">
    <el-row>
      <div class="title">Create project</div>
    </el-row>
    <el-row>
      <el-col :span="10" :offset="7">
        <el-form ref="form" :model="form" label-width="80px" size="small">
          <el-form-item label="Name">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="Description">
            <el-input v-model="form.description" />
          </el-form-item>
          <el-form-item label="Purpose">
            <el-input v-model="form.purpose" />
          </el-form-item>
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
              style="margin-bottom:5px"
            >
              <template slot="append">
                + New Tag
              </template>
            </el-input>

            <el-tag
              :key="tag"
              v-for="tag in form.tags"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)"
            >
              {{ tag }}
            </el-tag>
          </el-form-item>
          <el-form-item label="Image">
            <add-image
              @uploadImgResponse="uploadImgResponse"
              :uploadPath="'projects/picture'"
            ></add-image>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <div class="title"><el-button @click="createProject">Create</el-button></div>
  </div>
</template>

<script>
import addImage from '_com/AddImage';
import { saveMethods } from '@/api/request';
import {
  saveResources,
  saveProject,
  updateUsersByJwtUserId,
  getUserInfoByJwtUserId
} from '@/api/request';
export default {
  components: { addImage },

  watch: {},

  computed: {},

  data() {
    return {
      form: {
        name: '',
        description: '',
        privacy: '',
        tags: [],
        picture: '',
        userInfo: {}
      }
    };
  },

  methods: {
    async createProject() {
      let data = await saveProject(this.form);

      await saveMethods({ projectId: data.id, version: '1.0' });
      // console.log(document);
      saveResources({ projectId: data.id });

      this.userInfo.createdProjects.push(data.id);
      let update = { createdProjects: this.userInfo.createdProjects };
      await updateUsersByJwtUserId(update);

      this.$notify({
        title: 'Success',
        message: 'You have create the project successfully!',
        type: 'success'
      });
    },

    //为了获得创建的项目信息
    async getUserInfo() {
      let data = await getUserInfoByJwtUserId();
      this.userInfo = data;
      console.log(data);
    },
    uploadImgResponse(val) {
      this.form.picture = val;
    }
  },

  mounted() {
    this.getUserInfo();
  }
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
