<!-- create model resource -->
<template>
  <div class="main-contain">
    <el-row>
      <el-col :span="24">
        <el-form ref="form" :model="form" label-width="120px" size="small">
          <el-form-item label="Name">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="Token">
            <el-input v-model="form.token" />
          </el-form-item>
          <el-form-item label="Data Serice Id">
            <el-input v-model="form.dataServiceId">
              <el-dropdown slot="append" @command="handclick">
                <el-button icon="el-icon-more-outline"></el-button>
                <el-dropdown-menu slot="dropdown">
                  <div v-for="(item, index) in dataServiceInfo" :key="index">
                    <el-dropdown-item :command="index">{{ item.name }}</el-dropdown-item>
                  </div>
                </el-dropdown-menu>
              </el-dropdown>
            </el-input>
          </el-form-item>
          <el-form-item label="Type">
            <el-radio-group v-model="form.type">
              <el-radio label="public">Processing</el-radio>
              <el-radio label="discoverable">Visualization</el-radio>
            </el-radio-group>
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
              style="margin-bottom: 5px"
            >
              <template slot="append">+ New Tag</template>
            </el-input>

            <el-tag :key="tag" v-for="tag in form.tags" closable :disable-transitions="false" @close="handleClose(tag)">
              {{ tag }}
            </el-tag>
          </el-form-item>
          <el-form-item label="Source">
            <el-input v-model="form.source" />
          </el-form-item>
          <el-form-item label="Image">
            <add-image @uploadImgResponse="uploadImgResponse" :uploadPath="'dataService/picture'"></add-image>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <div class="title"><el-button @click="addService">Create</el-button></div>
  </div>
</template>

<script>
import addImage from '_com/AddImage';
import { saveDataService, getAllProcessing } from '@/api/request';
export default {
  components: { addImage },

  watch: {},

  computed: {},

  data() {
    return {
      form: {
        name: '',
        token: '',
        dataServiceId: '',
        tags: [],
        Privacy: '',
        type: 'service'
      },
      inputTagValue: '',
      dataServiceInfo: []
    };
  },

  methods: {
    async addService() {
      let data = await saveDataService(this.form);
      console.log(data);
      this.$notify({
        title: 'Success',
        message: 'You have add the model service successfully!',
        type: 'success'
      });
    },

    handclick(val) {
      // console.log(val)
      this.form.name = this.dataServiceInfo[val].name;
      this.form.dataServiceId = this.dataServiceInfo[val].id;
      // console.log(this.form.name)
      // console.log(this.form.dataServiceId)
    },

    async findData(token) {
      let data = await getAllProcessing({ token: token });

      let testJson = eval('(' + data + ')');
      for (let i = 0; i < testJson.length; i++) {
        this.dataServiceInfo.push({
          name: testJson[i].list.name,
          id: testJson[i].list.id
        });
      }
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

  mounted() {
    // this.findData("uGi4gMg94+ux4nuugF0M9tlqpCtZFRqem1kl/J2Vra8=")
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
