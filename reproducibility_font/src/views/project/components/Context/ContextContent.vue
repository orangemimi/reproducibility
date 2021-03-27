<template>
  <div class="main">
    <el-form
      ref="contextForm"
      :model="contextForm"
      label-width="110px"
      @submit.native.prevent
      size="mini"
    >
      <el-form-item label="Theme">
        <el-input
          v-if="tagInputVisible"
          v-model="themeTag"
          ref="tagInput"
          @keyup.enter.native="addTags"
          @blur="addTags"
          style="width: 300px"
        ></el-input>
        <el-button v-else @click="showTagInput" type="text" size="small">+ New Tag</el-button>
        <div v-if="contextForm != null">
          <el-tag
            :key="tagIndex"
            v-for="(tag, tagIndex) in contextForm.themes"
            closable
            :disable-transitions="false"
            @close="delTags(tag)"
            style="margin: 0 2px"
          >
            {{ tag }}
          </el-tag>
        </div>
      </el-form-item>
      <el-form-item label="Purpose">
        <el-input v-model="contextForm.purpose" placeholder="Please enter the content."></el-input>
      </el-form-item>
      <el-form-item label="Object">
        <el-input v-model="contextForm.objects" placeholder="Please enter the content."></el-input>
      </el-form-item>
      <el-form-item label="Temporal scale">
        <el-input
          v-model="contextForm.temporalScale"
          placeholder="Please enter the content."
        ></el-input>
      </el-form-item>
      <el-form-item label="Spatio scale">
        <el-input
          v-model="contextForm.spatioScale"
          placeholder="Please enter the content."
        ></el-input>
      </el-form-item>
      <el-form-item label="Method">
        <el-input v-model="contextForm.methods" placeholder="Please enter the content."></el-input>
      </el-form-item>
      <el-form-item label="Discussion">
        <el-input
          v-model="contextForm.discussion"
          placeholder="Please enter the content."
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitContext">Submit</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { post, get, patch } from '@/axios';
import { mapState } from 'vuex';

export default {
  components: {},
  props: {
    projectInfo: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      userId: state => state.user.userId,
      userName: state => state.user.name
    })
  },

  watch: {
    projectInfo: {
      handler(val) {
        if (val != null) {
          this.projectInfomation = val;
          this.getContext();
        }
      }
    }
  },

  data() {
    return {
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
      tagInputVisible: false,
      projectInfomation: this.projectInfo,
      updateContext: true
    };
  },

  methods: {
    async getContext() {
      console.log(this.projectInfomation.id);
      if (this.projectInfomation.id == null) {
        this.$message({
          message: 'Get information error!',
          type: 'error'
        });
        return;
      }
      let data = await get(`/contextDefinition/${this.projectInfomation.id}`);
      if (data == null) {
        this.updateContext = false;
      } else {
        this.updateContext = true;
        this.contextForm = data;
      }
    },
    submitContext() {
      this.$refs.contextForm.validate(async valid => {
        if (valid) {
          if (!this.updateContext) {
            let contextForm = this.contextForm;
            contextForm.projectId = this.projectInfomation.id;
            let data = await post(`/contextDefinition`, contextForm);
            this.saveRecord(data, 'created');
            this.$message({
              message: 'You have save the context successfully',
              type: 'success'
            });
          } else {
            let data = await patch(
              `/contextDefinition/${this.projectInfomation.id}`,
              this.contextForm
            );
            this.saveRecord(data, 'updated');
            this.$message({
              message: 'You have update the context successfully',
              type: 'success'
            });
          }
        }
      });
      this.$emit('closeForm', false);
    },

    async saveRecord(context, tag) {
      let record = {};
      record.nodeId = context.id;
      record.nodeType = 'contextDefinition';
      record.projectId = this.projectInfomation.id;
      record.content = `${this.userName} has ${tag} a context definition in this reproducible project`;
      await post(`/records`, record);
    },

    showTagInput() {
      this.tagInputVisible = true;
      this.$nextTick(_ => {
        this.$refs.tagInput.$refs.input.focus();
        console.log(_);
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
  created() {},
  mounted() {
    // this.getContext();
  }
};
</script>
<style lang="scss" scoped>
.main {
  margin: 0 20px;
  height: 100%;
}
</style>
