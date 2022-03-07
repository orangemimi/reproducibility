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
            <el-input v-model="inputTagValue" ref="addTagRef" size="small" @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm" style="margin-bottom:5px">
              <template slot="append">
                + New Tag
              </template>
            </el-input>

            <el-tag :key="tagIndex" v-for="(tag, tagIndex) in form.tags" closable :disable-transitions="false" @close="handleClose(tagIndex)">
              {{ tag }}
            </el-tag>
          </el-form-item>
          <el-form-item label="Image">
            <add-image @uploadImgResponse="uploadImgResponse" :uploadPath="'projects/picture'"></add-image>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <div class="title"><el-button @click="createProject">Create</el-button></div>
  </div>
</template>

<script>
import addImage from '_com/AddImage';
import { saveMethod, saveScenario, saveProjectResource, saveContext, saveProject, getUserProjectInfo, savePerformance } from '@/api/request';
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
        userInfo: {},
        members:[]
      },
      inputTagValue: ''
    };
  },

  methods: {
    async createProject() {
      let data = await saveProject(this.form);

      // await updateUserCreatedProjects(data.id);

      await saveMethod({ projectId: data.id, version: '1.0' });

      await saveScenario({ projectId: data.id });
      await saveProjectResource({ projectId: data.id });
      await saveContext({ projectId: data.id });

      let completionJson = {
        projectId: data.id
      };

      await savePerformance(completionJson);

      this.userInfo.createdProjects.push(data.id);
      this.$router.push({name: 'Projects'})
    },

    //为了获得创建的项目信息
    async getUserInfo() {
      let data = await getUserProjectInfo();
      this.userInfo = data;
      console.log(data);
    },

    uploadImgResponse(val) {
      this.form.picture = val;
    },

    handleInputConfirm() {
      if(this.inputTagValue != '') {
        this.form.tags.push(this.inputTagValue)
        this.inputTagValue = ''
      }
    },
    handleClose(index) {
      this.form.tags.splice(index, 1);
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
