<template>
  <div class="main">
    <el-form ref="contextForm" :model="contextForm" label-width="110px" @submit.native.prevent size="mini">
      <el-form-item label="Theme">
        <el-input v-if="tagInputVisible" v-model="themeTag" ref="tagInput" @keyup.enter.native="addTags" @blur="addTags" style="width: 300px"></el-input>
        <el-button v-else @click="showTagInput" type="text" size="small">+ New Tag</el-button>
        <div v-if="contextForm != null">
          <el-tag :key="tagIndex" v-for="(tag, tagIndex) in contextForm.themes" closable :disable-transitions="false" @close="delTags(tag)" style="margin: 0 2px">
            {{ tag }}
          </el-tag>
        </div>
      </el-form-item>
      <el-form-item label="Purpose">
        <el-input v-model="contextForm.purpose" placeholder="Please enter the content." :disabled="!(role == 'builder')"></el-input>
      </el-form-item>
      <el-form-item label="Object">
        <el-input v-model="contextForm.objects" placeholder="Please enter the content." :disabled="!(role == 'builder')"></el-input>
      </el-form-item>
      <el-form-item label="Temporal scale">
        <el-input v-model="contextForm.temporalScale" placeholder="Please enter the content." :disabled="!(role == 'builder')"></el-input>
      </el-form-item>
      <el-form-item label="Spatio scale">
        <el-input v-model="contextForm.spatialScale" placeholder="Please enter the content." :disabled="!(role == 'builder')"></el-input>
      </el-form-item>
      <el-form-item label="Method">
        <el-input v-model="contextForm.methods" placeholder="Please enter the content." :disabled="!(role == 'builder')"></el-input>
      </el-form-item>
      <el-form-item label="Discussion">
        <el-input v-model="contextForm.discussion" placeholder="Please enter the content." :disabled="!(role == 'builder')"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitContext">Submit</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getContextByProjectId, updateContexByProjectId, saveRecord, updatePerformanceById } from '@/api/request';
import { mapState } from 'vuex';

export default {
  computed: {
    ...mapState({
      userId: state => state.user.userId,
      userName: state => state.user.name,
      role: state => state.permission.role
    })
  },

  data() {
    return {
      projectId: this.$route.params.id,
      contextForm: {
        themes: [],
        purpose: '',
        objects: '',
        temporalScale: '',
        spatialScale: '',
        methods: '',
        discussion: ''
      },

      themeTag: '',
      tagInputVisible: false
      // updateContext: true
    };
  },

  methods: {
    async getContext() {
      // console.log(this.projectId);
      if (this.projectId == null) {
        this.$message({
          message: 'Get information error!',
          type: 'error'
        });
        return;
      }
      let data = await getContextByProjectId(this.projectId);
      // console.log('context', data);
      this.contextForm = data;
    },
    async submitContext() {
      // this.$refs.contextForm.validate(async valid => {
      //   if (valid) {

      let data = await updateContexByProjectId(this.projectId, this.contextForm);
      await this.saveRecord(data, 'updated');
      await this.updatePerformance();
      // }
      // });
      this.$emit('closeForm', false);
    },

    async saveRecord(context, tag) {
      let record = {};
      record.nodeId = context.id;
      record.nodeType = 'contextDefinition';
      record.projectId = this.projectId;
      record.content = `${this.userName} has ${tag} a context definition in this reproducible project`;
      await saveRecord(record);
    },

    async updatePerformance() {
      let content = { content: 'Context Definition', degree: '100%', type: 'success', icon: 'el-icon-edit' };

      await updatePerformanceById('context', this.projectId, content);
    },

    showTagInput() {
      this.tagInputVisible = true;
      this.$nextTick(() => {
        this.$refs.tagInput.$refs.input.focus();
        // console.log(_);
      });
    },
    addTags() {
      let tag = this.themeTag;
      if (tag != '') {
        this.contextForm.themes.push(tag);
      }
      this.tagInputVisible = false;
      this.themeTag = '';
    },
    delTags(tag) {
      this.contextForm.themes.splice(this.contextForm.themes.indexOf(tag), 1);
    }
  },
  created() {
    this.getContext();
  }
};
</script>
<style lang="scss" scoped>
.main {
  margin: 0 20px;
  height: 100%;
}
</style>
