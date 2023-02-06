<!-- BInd reosurce -->
<template>
  <div class="main">
    <div v-if="resourceType == 'file'">
      <file-upload @returnFileUrl="returnFileUrl"></file-upload>
    </div>

    <div v-if="resourceType == 'shared_file'">
      <el-form :inline="false" :model="form" label-width="50px" size="small">
        <el-form-item label="Value">
          <el-input v-model="form.value" placeholder="value"></el-input>
        </el-form-item>
        <el-form-item label="Token">
          <el-input v-model="form.token" placeholder="token"></el-input>
        </el-form-item>
      </el-form>
      Please enter the url in data container!
    </div>

    <div v-if="resourceType == 'parameter'">
      <el-input v-model="form.value"></el-input>

      Please enter the url in data container!
    </div>

    <div class="submitBtn">
      <el-button @click="submitBtn">Submit</el-button>
    </div>
  </div>
</template>

<script>
import fileUpload from './FileUpload';
import { mapState } from 'vuex';

export default {
  components: { fileUpload },

  watch: {
    resourceType: {
      handler(val) {
        this.type == val;
      },
      // 深度观察监听
      deep: true,
      immediate: true
    }
  },

  props: {
    resourceType: {
      type: String
    }
  },

  computed: {
    ...mapState({
      role: state => state.permission.role
    })
  },

  data() {
    return {
      activeName: 'file',

      form: {
        value: '',
        token: ''
      }
    };
  },

  methods: {
    returnFileUrl(value) {
      this.form.value = value.address;
    },

    handleClick() {
      this.value = '';
    },
    submitBtn() {
      let result = {
        url: this.form.value,
        token: this.form.token
      };
      this.$emit('returnResourceUrl', result);
    }
    //get all the file
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  min-height: 200px;
}
</style>
