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
          <!-- <el-form-item label="Tag">
          <el-input v-model="form.tag" />
        </el-form-item> -->
          <el-form-item label="Image">
            <add-image @uploadImgResponse="uploadImgResponse"></add-image>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <div class="title"><el-button @click="createProject">Create</el-button></div>
  </div>
</template>

<script>
import addImage from '_com/AddImage';
import { post, get, patch } from '@/axios';
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
        tag: [],
        picture: '',
        userInfo: {}
      }
    };
  },

  methods: {
    async createProject() {
      let data = await post(`/projects`, this.form);
      console.log(data);
      this.userInfo.createdProjects.push(data.id);
      let update = { createdProjects: this.userInfo.createdProjects };
      await patch(`/users`, update);

      this.$notify({
        title: 'Success',
        message: 'You have create the project successfully!',
        type: 'success'
      });
    },
    async getUserInfo() {
      let data = await get(`/users`);
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
