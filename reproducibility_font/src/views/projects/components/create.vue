<template>
  <div>
    <el-form ref="form" :model="form" label-width="90px" size="mini">
      <el-form-item label="Name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="Introduction">
        <el-input v-model="form.introduction"></el-input>
      </el-form-item>
      <el-form-item label="Description">
        <el-input type="textarea" v-model="form.description" resize="none"></el-input>
      </el-form-item>
      <el-form-item label="Privacy">
        <el-select v-model="form.privacy" placeholder="请选择">
          <el-option label="public" value="public"></el-option>
          <el-option label="private" value="private"></el-option>
        </el-select>
        <!-- <el-switch v-model="form.privacy" active-text="public" inactive-text="private"></el-switch> -->
      </el-form-item>
      <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">
        {{ tag }}
      </el-tag>
      <el-input
        class="input-new-tag"
        v-if="inputVisible"
        v-model="inputValue"
        ref="saveTagInput"
        size="small"
        @keyup.enter.native="handleInputConfirm"
        @blur="handleInputConfirm"
      ></el-input>
      <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
    </el-form>
    <add-image @getfile="getfile" class="picture" />
    <div class="btn">
      <el-button @click="cancel" size="small">Cancel</el-button>
      <el-button type="primary" @click="saveProject" size="small">Confirm</el-button>
    </div>
  </div>
</template>

<script>
import { saveProject, postFile, saveScenario } from '@/api/request';
import addImage from '_com/AddImage/index1.vue';
export default {
  components: { addImage },
  data() {
    return {
      form: {
        name: '',
        description: '',
        introduction: '',
        privacy: 'public'
      },
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      pictureFile: ''
    };
  },
  methods: {
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },
    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (this.dynamicTags.indexOf(inputValue) != -1) {
        this.$notify({
          title: 'warning',
          message: 'The same label exists!',
          type: 'warning'
        });
      } else {
        if (inputValue) {
          this.dynamicTags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      }
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    getfile(val) {
      console.log(val);
      this.pictureFile = val;
    },
    async saveProject() {
      let form = new FormData();
      form.append('datafile', this.pictureFile.raw);
      form.append('name', this.form.name);
      let pictureData = await postFile(form);
      let jsonData = {
        project: {
          name: this.form.name,
          description: this.form.description,
          introduction: this.form.introduction,
          privacy: this.form.privacy,
          tags: this.dynamicTags,
          picture: 'http://221.226.60.2:8082/data/' + pictureData.data.data.id
        }
      };
      let data = await saveProject(jsonData);
      await saveScenario({ projectId: data.id });
      console.log(data);
      this.cancel();
    },

    cancel() {
      this.$emit('dialogShow', false);
    }
  }
};
</script>

<style lang="scss" scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.picture {
  margin-top: 10px;
  margin-left: 10px;
}
.btn {
  margin-top: 10px;
  text-align: center;
}
</style>
